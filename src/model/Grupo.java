import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Grupo {

    private static final int MAX_PARTICIPANTES = 5;

    private String nome;
    private final List<Participante> participantes;

    public Grupo() {
        this("Grupo sem nome");
    }

    public Grupo(String nome) {
        setNome(nome);
        this.participantes = new ArrayList<>();
    }

    public void adicionarParticipante(Participante participante) {
        if (participante == null) {
            throw new IllegalArgumentException("Informe um participante valido.");
        }
        if (participantes.contains(participante)) {
            throw new IllegalArgumentException("Participante ja esta neste grupo.");
        }
        if (participantes.size() >= MAX_PARTICIPANTES) {
            throw new IllegalArgumentException("Cada grupo suporta no maximo 5 participantes.");
        }
        participantes.add(participante);
        participante.setGrupo(this);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do grupo obrigatorio.");
        }
        this.nome = nome.trim();
    }

    public List<Participante> getParticipantes() {
        return Collections.unmodifiableList(participantes);
    }

    public List<Participante> getClassificacao() {
        List<Participante> classificacao = new ArrayList<>(participantes);
        classificacao.sort(Comparator.comparingInt(Participante::getPontuacao).reversed()
                .thenComparing(Participante::getNome));
        return classificacao;
    }

    @Override
    public String toString() {
        return nome;
    }
}
