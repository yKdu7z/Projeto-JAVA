import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sistema {

    private static final int MAX_GRUPOS = 5;
    private static final int MAX_PARTICIPANTES_SISTEMA = 5;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final List<Clube> clubes;
    private final List<Campeonato> campeonatos;
    private final List<Grupo> grupos;
    private final List<Participante> participantes;
    private final Administrador administrador;
    private final RepositorioSistema repositorio;

    public Sistema() {
        this.clubes = new ArrayList<>();
        this.campeonatos = new ArrayList<>();
        this.grupos = new ArrayList<>();
        this.participantes = new ArrayList<>();
        this.administrador = new Administrador("Administrador");
        this.repositorio = new RepositorioSistema();
        this.repositorio.carregarDados(clubes, campeonatos, grupos, participantes);
    }

    public Clube cadastrarClube(String nome) {
        validarNomeDisponivel(nome, clubes);
        Clube clube = new Clube(nome);
        clubes.add(clube);
        repositorio.salvarClube(clube);
        return clube;
    }

    public Campeonato cadastrarCampeonato(String nome) {
        validarNomeDisponivel(nome, campeonatos);
        Campeonato campeonato = new Campeonato(nome);
        campeonatos.add(campeonato);
        repositorio.salvarCampeonato(campeonato);
        return campeonato;
    }

    public void adicionarClubeAoCampeonato(Campeonato campeonato, Clube clube) {
        if (campeonato == null || clube == null) {
            throw new IllegalArgumentException("Selecione campeonato e clube.");
        }
        campeonato.adicionarClube(clube);
        repositorio.vincularClubeAoCampeonato(campeonato, clube);
    }

    public Grupo cadastrarGrupo(String nome) {
        if (grupos.size() >= MAX_GRUPOS) {
            throw new IllegalArgumentException("O sistema suporta no maximo 5 grupos.");
        }
        validarNomeDisponivel(nome, grupos);
        Grupo grupo = new Grupo(nome);
        grupos.add(grupo);
        repositorio.salvarGrupo(grupo);
        return grupo;
    }

    public Participante cadastrarParticipante(String nome, Grupo grupo) {
        if (participantes.size() >= MAX_PARTICIPANTES_SISTEMA) {
            throw new IllegalArgumentException("O sistema suporta no maximo 5 participantes.");
        }
        if (grupo == null) {
            throw new IllegalArgumentException("Selecione um grupo para o participante.");
        }
        validarNomeDisponivel(nome, participantes);
        Participante participante = new Participante(nome);
        grupo.adicionarParticipante(participante);
        participantes.add(participante);
        repositorio.salvarParticipante(participante);
        return participante;
    }

    public Partida cadastrarPartida(Campeonato campeonato, Clube mandante, Clube visitante, LocalDateTime dataHora) {
        if (campeonato == null) {
            throw new IllegalArgumentException("Selecione um campeonato.");
        }
        Partida partida = new Partida(mandante, visitante, dataHora);
        campeonato.registrarPartida(partida);
        repositorio.salvarPartida(campeonato, partida);
        return partida;
    }

    public void registrarAposta(Participante participante, Partida partida, int golsMandante, int golsVisitante) {
        if (participante == null || partida == null) {
            throw new IllegalArgumentException("Selecione participante e partida.");
        }
        participante.registrarAposta(partida, golsMandante, golsVisitante);
        participante.calcularPontuacao();
        Aposta aposta = participante.getApostas().get(partida);
        repositorio.salvarAposta(participante, partida, aposta);
        repositorio.atualizarPontuacao(participante);
    }

    public void registrarResultado(Partida partida, int golsMandante, int golsVisitante) {
        if (partida == null) {
            throw new IllegalArgumentException("Selecione uma partida.");
        }
        partida.registrarResultado(golsMandante, golsVisitante);
        atualizarPontuacoes();
        repositorio.salvarResultado(partida);
    }

    public void atualizarPontuacoes() {
        for (Participante participante : participantes) {
            participante.calcularPontuacao();
            repositorio.atualizarPontuacao(participante);
        }
    }

    public List<Clube> getClubes() {
        return Collections.unmodifiableList(clubes);
    }

    public List<Campeonato> getCampeonatos() {
        return Collections.unmodifiableList(campeonatos);
    }

    public List<Grupo> getGrupos() {
        return Collections.unmodifiableList(grupos);
    }

    public List<Participante> getParticipantes() {
        return Collections.unmodifiableList(participantes);
    }

    public List<Partida> getTodasPartidas() {
        List<Partida> partidas = new ArrayList<>();
        for (Campeonato campeonato : campeonatos) {
            partidas.addAll(campeonato.getPartidas());
        }
        return partidas;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public String getStatusBancoDados() {
        return repositorio.getMensagemStatus();
    }

    public List<String> getEventosBancoDados() {
        return repositorio.listarEventosRecentes();
    }

    public static LocalDateTime parseDataHora(String valor) {
        try {
            return LocalDateTime.parse(valor, FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Use o formato dd/MM/yyyy HH:mm.");
        }
    }

    public static String formatarDataHora(LocalDateTime dataHora) {
        return dataHora.format(FORMATTER);
    }

    private void validarNomeDisponivel(String nome, List<?> itens) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome obrigatorio.");
        }
        String nomeNormalizado = nome.trim();
        for (Object item : itens) {
            if (item.toString().equalsIgnoreCase(nomeNormalizado)) {
                throw new IllegalArgumentException("Ja existe um cadastro com esse nome.");
            }
        }
    }
}
