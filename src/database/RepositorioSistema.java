import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class RepositorioSistema {

    private final BancoDados bancoDados;
    private final Map<Clube, Integer> idsClubes;
    private final Map<Campeonato, Integer> idsCampeonatos;
    private final Map<Grupo, Integer> idsGrupos;
    private final Map<Participante, Integer> idsParticipantes;
    private final Map<Partida, Integer> idsPartidas;
    private boolean conectado;
    private String mensagemStatus;

    public RepositorioSistema() {
        this.bancoDados = new BancoDados();
        this.idsClubes = new IdentityHashMap<>();
        this.idsCampeonatos = new IdentityHashMap<>();
        this.idsGrupos = new IdentityHashMap<>();
        this.idsParticipantes = new IdentityHashMap<>();
        this.idsPartidas = new IdentityHashMap<>();
        inicializar();
    }

    public boolean estaConectado() {
        return conectado;
    }

    public String getMensagemStatus() {
        return mensagemStatus;
    }

    public void carregarDados(List<Clube> clubes, List<Campeonato> campeonatos, List<Grupo> grupos,
            List<Participante> participantes) {
        if (!conectado) {
            return;
        }

        Map<Integer, Clube> clubesPorId = new HashMap<>();
        Map<Integer, Campeonato> campeonatosPorId = new HashMap<>();
        Map<Integer, Grupo> gruposPorId = new HashMap<>();
        Map<Integer, Participante> participantesPorId = new HashMap<>();
        Map<Integer, Partida> partidasPorId = new HashMap<>();

        try (Connection conexao = bancoDados.conectar()) {
            carregarClubes(conexao, clubes, clubesPorId);
            carregarCampeonatos(conexao, campeonatos, campeonatosPorId);
            carregarGrupos(conexao, grupos, gruposPorId);
            carregarClubesDosCampeonatos(conexao, clubesPorId, campeonatosPorId);
            carregarParticipantes(conexao, participantes, gruposPorId, participantesPorId);
            carregarPartidas(conexao, campeonatosPorId, clubesPorId, partidasPorId);
            carregarApostas(conexao, participantesPorId, partidasPorId);
            for (Participante participante : participantes) {
                participante.calcularPontuacao();
            }
        } catch (RuntimeException | SQLException ex) {
            conectado = false;
            mensagemStatus = "Erro ao carregar dados do banco. " + ex.getMessage();
        }
    }

    public void salvarClube(Clube clube) {
        if (!conectado) {
            return;
        }
        try (Connection conexao = bancoDados.conectar()) {
            int id = inserirOuBuscar(conexao, "clubes", "nome", clube.getNome());
            idsClubes.put(clube, id);
            registrarEvento(conexao, "CLUBE", "Clube cadastrado: " + clube.getNome());
        } catch (SQLException ex) {
            falhar("Erro ao salvar clube no banco.", ex);
        }
    }

    public void salvarCampeonato(Campeonato campeonato) {
        if (!conectado) {
            return;
        }
        try (Connection conexao = bancoDados.conectar()) {
            int id = inserirOuBuscar(conexao, "campeonatos", "nome", campeonato.getNome());
            idsCampeonatos.put(campeonato, id);
            registrarEvento(conexao, "CAMPEONATO", "Campeonato cadastrado: " + campeonato.getNome());
        } catch (SQLException ex) {
            falhar("Erro ao salvar campeonato no banco.", ex);
        }
    }

    public void salvarGrupo(Grupo grupo) {
        if (!conectado) {
            return;
        }
        try (Connection conexao = bancoDados.conectar()) {
            int id = inserirOuBuscar(conexao, "grupos", "nome", grupo.getNome());
            idsGrupos.put(grupo, id);
            registrarEvento(conexao, "GRUPO", "Grupo cadastrado: " + grupo.getNome());
        } catch (SQLException ex) {
            falhar("Erro ao salvar grupo no banco.", ex);
        }
    }

    public void salvarParticipante(Participante participante) {
        if (!conectado) {
            return;
        }
        try (Connection conexao = bancoDados.conectar()) {
            int grupoId = obterGrupoId(conexao, participante.getGrupo());
            Integer existente = buscarIdPorNome(conexao, "participantes", participante.getNome());

            if (existente == null) {
                try (PreparedStatement stmt = conexao.prepareStatement(
                        "INSERT INTO participantes (nome, grupo_id, pontuacao) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, participante.getNome());
                    stmt.setInt(2, grupoId);
                    stmt.setInt(3, participante.getPontuacao());
                    stmt.executeUpdate();
                    existente = lerIdGerado(stmt);
                }
            } else {
                try (PreparedStatement stmt = conexao.prepareStatement(
                        "UPDATE participantes SET grupo_id = ?, pontuacao = ? WHERE id = ?")) {
                    stmt.setInt(1, grupoId);
                    stmt.setInt(2, participante.getPontuacao());
                    stmt.setInt(3, existente);
                    stmt.executeUpdate();
                }
            }

            idsParticipantes.put(participante, existente);
            registrarEvento(conexao, "PARTICIPANTE", "Participante cadastrado: " + participante.getNome());
        } catch (SQLException ex) {
            falhar("Erro ao salvar participante no banco.", ex);
        }
    }

    public void vincularClubeAoCampeonato(Campeonato campeonato, Clube clube) {
        if (!conectado) {
            return;
        }
        try (Connection conexao = bancoDados.conectar()) {
            int campeonatoId = obterCampeonatoId(conexao, campeonato);
            int clubeId = obterClubeId(conexao, clube);
            try (PreparedStatement stmt = conexao.prepareStatement(
                    "INSERT IGNORE INTO campeonato_clubes (campeonato_id, clube_id) VALUES (?, ?)")) {
                stmt.setInt(1, campeonatoId);
                stmt.setInt(2, clubeId);
                stmt.executeUpdate();
            }
            registrarEvento(conexao, "VINCULO",
                    "Clube " + clube.getNome() + " vinculado ao campeonato " + campeonato.getNome());
        } catch (SQLException ex) {
            falhar("Erro ao vincular clube ao campeonato no banco.", ex);
        }
    }

    public void salvarPartida(Campeonato campeonato, Partida partida) {
        if (!conectado) {
            return;
        }
        try (Connection conexao = bancoDados.conectar()) {
            int campeonatoId = obterCampeonatoId(conexao, campeonato);
            int mandanteId = obterClubeId(conexao, partida.getClubeMandante());
            int visitanteId = obterClubeId(conexao, partida.getClubeVisitante());
            Integer existente = buscarPartida(conexao, campeonatoId, mandanteId, visitanteId, partida);

            if (existente == null) {
                try (PreparedStatement stmt = conexao.prepareStatement(
                        "INSERT INTO partidas (campeonato_id, clube_mandante_id, clube_visitante_id, data_hora) "
                                + "VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, campeonatoId);
                    stmt.setInt(2, mandanteId);
                    stmt.setInt(3, visitanteId);
                    stmt.setTimestamp(4, Timestamp.valueOf(partida.getDataHora()));
                    stmt.executeUpdate();
                    existente = lerIdGerado(stmt);
                }
            }

            idsPartidas.put(partida, existente);
            registrarEvento(conexao, "PARTIDA", "Partida cadastrada: " + partida.getDescricao());
        } catch (SQLException ex) {
            falhar("Erro ao salvar partida no banco.", ex);
        }
    }

    public void salvarAposta(Participante participante, Partida partida, Aposta aposta) {
        if (!conectado) {
            return;
        }
        try (Connection conexao = bancoDados.conectar()) {
            int participanteId = obterParticipanteId(conexao, participante);
            int partidaId = obterPartidaId(conexao, partida);
            try (PreparedStatement stmt = conexao.prepareStatement(
                    "INSERT INTO apostas (participante_id, partida_id, gols_mandante, gols_visitante, data_registro) "
                            + "VALUES (?, ?, ?, ?, ?) "
                            + "ON DUPLICATE KEY UPDATE gols_mandante = VALUES(gols_mandante), "
                            + "gols_visitante = VALUES(gols_visitante), data_registro = VALUES(data_registro)")) {
                stmt.setInt(1, participanteId);
                stmt.setInt(2, partidaId);
                stmt.setInt(3, aposta.getGolsMandante());
                stmt.setInt(4, aposta.getGolsVisitante());
                stmt.setTimestamp(5, Timestamp.valueOf(aposta.getDataRegistro()));
                stmt.executeUpdate();
            }
            registrarEvento(conexao, "APOSTA",
                    "Aposta de " + participante.getNome() + " em " + partida.getDescricao());
        } catch (SQLException ex) {
            falhar("Erro ao salvar aposta no banco.", ex);
        }
    }

    public void salvarResultado(Partida partida) {
        if (!conectado || !partida.temResultado()) {
            return;
        }
        try (Connection conexao = bancoDados.conectar()) {
            int partidaId = obterPartidaId(conexao, partida);
            try (PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE partidas SET gols_mandante = ?, gols_visitante = ? WHERE id = ?")) {
                stmt.setInt(1, partida.getResultado().getGolsMandante());
                stmt.setInt(2, partida.getResultado().getGolsVisitante());
                stmt.setInt(3, partidaId);
                stmt.executeUpdate();
            }
            registrarEvento(conexao, "RESULTADO", "Resultado registrado: " + partida.getDescricao()
                    + " -> " + partida.getResultado().descrever());
        } catch (SQLException ex) {
            falhar("Erro ao salvar resultado no banco.", ex);
        }
    }

    public void atualizarPontuacao(Participante participante) {
        if (!conectado) {
            return;
        }
        try (Connection conexao = bancoDados.conectar()) {
            int participanteId = obterParticipanteId(conexao, participante);
            try (PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE participantes SET pontuacao = ? WHERE id = ?")) {
                stmt.setInt(1, participante.getPontuacao());
                stmt.setInt(2, participanteId);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            falhar("Erro ao atualizar pontuacao no banco.", ex);
        }
    }

    public List<String> listarEventosRecentes() {
        List<String> eventos = new ArrayList<>();
        if (!conectado) {
            eventos.add(mensagemStatus);
            return eventos;
        }
        try (Connection conexao = bancoDados.conectar();
                PreparedStatement stmt = conexao.prepareStatement(
                        "SELECT tipo, descricao, criado_em FROM eventos ORDER BY id DESC LIMIT 20");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                eventos.add(rs.getTimestamp("criado_em") + " | " + rs.getString("tipo") + " | "
                        + rs.getString("descricao"));
            }
        } catch (SQLException ex) {
            eventos.add("Nao foi possivel ler o historico: " + ex.getMessage());
        }
        return eventos;
    }

    private void inicializar() {
        try (Connection conexao = bancoDados.conectar(); Statement stmt = conexao.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS clubes ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nome VARCHAR(120) NOT NULL UNIQUE,"
                    + "criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS campeonatos ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nome VARCHAR(120) NOT NULL UNIQUE,"
                    + "criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS grupos ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nome VARCHAR(120) NOT NULL UNIQUE,"
                    + "criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS participantes ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nome VARCHAR(120) NOT NULL UNIQUE,"
                    + "grupo_id INT NOT NULL,"
                    + "pontuacao INT NOT NULL DEFAULT 0,"
                    + "criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY (grupo_id) REFERENCES grupos(id))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS campeonato_clubes ("
                    + "campeonato_id INT NOT NULL,"
                    + "clube_id INT NOT NULL,"
                    + "PRIMARY KEY (campeonato_id, clube_id),"
                    + "FOREIGN KEY (campeonato_id) REFERENCES campeonatos(id),"
                    + "FOREIGN KEY (clube_id) REFERENCES clubes(id))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS partidas ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "campeonato_id INT NOT NULL,"
                    + "clube_mandante_id INT NOT NULL,"
                    + "clube_visitante_id INT NOT NULL,"
                    + "data_hora DATETIME NOT NULL,"
                    + "gols_mandante INT NULL,"
                    + "gols_visitante INT NULL,"
                    + "UNIQUE KEY uk_partida (campeonato_id, clube_mandante_id, clube_visitante_id, data_hora),"
                    + "FOREIGN KEY (campeonato_id) REFERENCES campeonatos(id),"
                    + "FOREIGN KEY (clube_mandante_id) REFERENCES clubes(id),"
                    + "FOREIGN KEY (clube_visitante_id) REFERENCES clubes(id))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS apostas ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "participante_id INT NOT NULL,"
                    + "partida_id INT NOT NULL,"
                    + "gols_mandante INT NOT NULL,"
                    + "gols_visitante INT NOT NULL,"
                    + "data_registro DATETIME NOT NULL,"
                    + "UNIQUE KEY uk_aposta (participante_id, partida_id),"
                    + "FOREIGN KEY (participante_id) REFERENCES participantes(id),"
                    + "FOREIGN KEY (partida_id) REFERENCES partidas(id))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS eventos ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "tipo VARCHAR(40) NOT NULL,"
                    + "descricao VARCHAR(255) NOT NULL,"
                    + "criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            conectado = true;
            mensagemStatus = "Conectado ao banco: " + bancoDados.getUrl();
        } catch (RuntimeException | SQLException ex) {
            conectado = false;
            mensagemStatus = "Banco indisponivel: " + ex.getMessage();
        }
    }

    private void carregarClubes(Connection conexao, List<Clube> clubes, Map<Integer, Clube> clubesPorId)
            throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement("SELECT id, nome FROM clubes ORDER BY id");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Clube clube = new Clube(rs.getString("nome"));
                clubes.add(clube);
                clubesPorId.put(rs.getInt("id"), clube);
                idsClubes.put(clube, rs.getInt("id"));
            }
        }
    }

    private void carregarCampeonatos(Connection conexao, List<Campeonato> campeonatos,
            Map<Integer, Campeonato> campeonatosPorId) throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement("SELECT id, nome FROM campeonatos ORDER BY id");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Campeonato campeonato = new Campeonato(rs.getString("nome"));
                campeonatos.add(campeonato);
                campeonatosPorId.put(rs.getInt("id"), campeonato);
                idsCampeonatos.put(campeonato, rs.getInt("id"));
            }
        }
    }

    private void carregarGrupos(Connection conexao, List<Grupo> grupos, Map<Integer, Grupo> gruposPorId)
            throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement("SELECT id, nome FROM grupos ORDER BY id");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Grupo grupo = new Grupo(rs.getString("nome"));
                grupos.add(grupo);
                gruposPorId.put(rs.getInt("id"), grupo);
                idsGrupos.put(grupo, rs.getInt("id"));
            }
        }
    }

    private void carregarClubesDosCampeonatos(Connection conexao, Map<Integer, Clube> clubesPorId,
            Map<Integer, Campeonato> campeonatosPorId) throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement(
                "SELECT campeonato_id, clube_id FROM campeonato_clubes ORDER BY campeonato_id, clube_id");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Campeonato campeonato = campeonatosPorId.get(rs.getInt("campeonato_id"));
                Clube clube = clubesPorId.get(rs.getInt("clube_id"));
                if (campeonato != null && clube != null) {
                    campeonato.adicionarClube(clube);
                }
            }
        }
    }

    private void carregarParticipantes(Connection conexao, List<Participante> participantes,
            Map<Integer, Grupo> gruposPorId, Map<Integer, Participante> participantesPorId) throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement(
                "SELECT id, nome, grupo_id, pontuacao FROM participantes ORDER BY id");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Grupo grupo = gruposPorId.get(rs.getInt("grupo_id"));
                if (grupo == null) {
                    continue;
                }
                Participante participante = new Participante(rs.getString("nome"));
                grupo.adicionarParticipante(participante);
                participantes.add(participante);
                participantesPorId.put(rs.getInt("id"), participante);
                idsParticipantes.put(participante, rs.getInt("id"));
            }
        }
    }

    private void carregarPartidas(Connection conexao, Map<Integer, Campeonato> campeonatosPorId,
            Map<Integer, Clube> clubesPorId, Map<Integer, Partida> partidasPorId) throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement(
                "SELECT id, campeonato_id, clube_mandante_id, clube_visitante_id, data_hora, "
                        + "gols_mandante, gols_visitante FROM partidas ORDER BY id");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Campeonato campeonato = campeonatosPorId.get(rs.getInt("campeonato_id"));
                Clube mandante = clubesPorId.get(rs.getInt("clube_mandante_id"));
                Clube visitante = clubesPorId.get(rs.getInt("clube_visitante_id"));
                if (campeonato == null || mandante == null || visitante == null) {
                    continue;
                }
                Partida partida = new Partida(mandante, visitante, rs.getTimestamp("data_hora").toLocalDateTime());
                Integer golsMandante = lerInteiroOpcional(rs, "gols_mandante");
                Integer golsVisitante = lerInteiroOpcional(rs, "gols_visitante");
                if (golsMandante != null && golsVisitante != null) {
                    partida.registrarResultado(golsMandante, golsVisitante);
                }
                campeonato.registrarPartida(partida);
                partidasPorId.put(rs.getInt("id"), partida);
                idsPartidas.put(partida, rs.getInt("id"));
            }
        }
    }

    private void carregarApostas(Connection conexao, Map<Integer, Participante> participantesPorId,
            Map<Integer, Partida> partidasPorId) throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement(
                "SELECT participante_id, partida_id, gols_mandante, gols_visitante, data_registro "
                        + "FROM apostas ORDER BY id");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Participante participante = participantesPorId.get(rs.getInt("participante_id"));
                Partida partida = partidasPorId.get(rs.getInt("partida_id"));
                if (participante == null || partida == null) {
                    continue;
                }
                Aposta aposta = new Aposta(
                        rs.getInt("gols_mandante"),
                        rs.getInt("gols_visitante"),
                        rs.getTimestamp("data_registro").toLocalDateTime());
                participante.restaurarAposta(partida, aposta);
            }
        }
    }

    private Integer lerInteiroOpcional(ResultSet rs, String coluna) throws SQLException {
        int valor = rs.getInt(coluna);
        if (rs.wasNull()) {
            return null;
        }
        return valor;
    }

    private int inserirOuBuscar(Connection conexao, String tabela, String coluna, String valor) throws SQLException {
        Integer id = buscarIdPorNome(conexao, tabela, valor);
        if (id != null) {
            return id;
        }
        try (PreparedStatement stmt = conexao.prepareStatement(
                "INSERT INTO " + tabela + " (" + coluna + ") VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, valor);
            stmt.executeUpdate();
            return lerIdGerado(stmt);
        }
    }

    private Integer buscarIdPorNome(Connection conexao, String tabela, String nome) throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement(
                "SELECT id FROM " + tabela + " WHERE nome = ?")) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return null;
    }

    private Integer buscarPartida(Connection conexao, int campeonatoId, int mandanteId, int visitanteId, Partida partida)
            throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement(
                "SELECT id FROM partidas WHERE campeonato_id = ? AND clube_mandante_id = ? "
                        + "AND clube_visitante_id = ? AND data_hora = ?")) {
            stmt.setInt(1, campeonatoId);
            stmt.setInt(2, mandanteId);
            stmt.setInt(3, visitanteId);
            stmt.setTimestamp(4, Timestamp.valueOf(partida.getDataHora()));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return null;
    }

    private int obterClubeId(Connection conexao, Clube clube) throws SQLException {
        Integer id = idsClubes.get(clube);
        if (id == null) {
            id = inserirOuBuscar(conexao, "clubes", "nome", clube.getNome());
            idsClubes.put(clube, id);
        }
        return id;
    }

    private int obterCampeonatoId(Connection conexao, Campeonato campeonato) throws SQLException {
        Integer id = idsCampeonatos.get(campeonato);
        if (id == null) {
            id = inserirOuBuscar(conexao, "campeonatos", "nome", campeonato.getNome());
            idsCampeonatos.put(campeonato, id);
        }
        return id;
    }

    private int obterGrupoId(Connection conexao, Grupo grupo) throws SQLException {
        Integer id = idsGrupos.get(grupo);
        if (id == null) {
            id = inserirOuBuscar(conexao, "grupos", "nome", grupo.getNome());
            idsGrupos.put(grupo, id);
        }
        return id;
    }

    private int obterParticipanteId(Connection conexao, Participante participante) throws SQLException {
        Integer id = idsParticipantes.get(participante);
        if (id == null) {
            salvarParticipante(participante);
            id = idsParticipantes.get(participante);
        }
        return id;
    }

    private int obterPartidaId(Connection conexao, Partida partida) throws SQLException {
        Integer id = idsPartidas.get(partida);
        if (id == null) {
            throw new SQLException("Partida ainda nao foi salva no banco: " + partida.getDescricao());
        }
        return id;
    }

    private int lerIdGerado(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Nao foi possivel obter o id gerado.");
    }

    private void registrarEvento(Connection conexao, String tipo, String descricao) throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement(
                "INSERT INTO eventos (tipo, descricao) VALUES (?, ?)")) {
            stmt.setString(1, tipo);
            stmt.setString(2, descricao);
            stmt.executeUpdate();
        }
    }

    private void falhar(String mensagem, SQLException ex) {
        conectado = false;
        mensagemStatus = mensagem + " " + ex.getMessage();
        throw new IllegalArgumentException(mensagemStatus, ex);
    }
}
