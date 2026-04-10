import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Campeonato {

    private static final int MAX_CLUBES = 8;

    private String nome;
    private final List<Clube> clubes;
    private final List<Partida> partidas;

    public Campeonato() {
        this("Campeonato sem nome");
    }

    public Campeonato(String nome) {
        setNome(nome);
        this.clubes = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }

    public void adicionarClube(Clube clube) {
        if (clube == null) {
            throw new IllegalArgumentException("Informe um clube valido.");
        }
        if (clubes.contains(clube)) {
            throw new IllegalArgumentException("O clube ja foi adicionado a este campeonato.");
        }
        if (clubes.size() >= MAX_CLUBES) {
            throw new IllegalArgumentException("O campeonato suporta no maximo 8 clubes.");
        }
        clubes.add(clube);
    }

    public void registrarPartida(Partida partida) {
        if (partida == null) {
            throw new IllegalArgumentException("Informe uma partida valida.");
        }
        if (!clubes.contains(partida.getClubeMandante()) || !clubes.contains(partida.getClubeVisitante())) {
            throw new IllegalArgumentException("Os clubes da partida precisam estar cadastrados no campeonato.");
        }
        partidas.add(partida);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do campeonato obrigatorio.");
        }
        this.nome = nome.trim();
    }

    public List<Clube> getClubes() {
        return Collections.unmodifiableList(clubes);
    }

    public List<Partida> getPartidas() {
        return Collections.unmodifiableList(partidas);
    }

    @Override
    public String toString() {
        return nome;
    }
}
