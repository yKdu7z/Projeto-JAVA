import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Participante extends Usuario implements Pontuavel {

    private final Map<Partida, Aposta> apostas;
    private int pontuacao;
    private Grupo grupo;

    public Participante() {
        this("Participante sem nome");
    }

    public Participante(String nome) {
        super(nome);
        this.apostas = new LinkedHashMap<>();
    }

    public void registrarAposta(Partida partida, Aposta aposta) {
        if (partida == null || aposta == null) {
            throw new IllegalArgumentException("Partida e aposta sao obrigatorias.");
        }
        if (!partida.podeReceberAposta(LocalDateTime.now())) {
            throw new IllegalArgumentException("A aposta so pode ser feita ate 20 minutos antes da partida.");
        }
        apostas.put(partida, aposta);
    }

    public void registrarAposta(Partida partida, int golsMandante, int golsVisitante) {
        registrarAposta(partida, new Aposta(golsMandante, golsVisitante));
    }

    @Override
    public int calcularPontuacao() {
        pontuacao = 0;
        for (Map.Entry<Partida, Aposta> entry : apostas.entrySet()) {
            Partida partida = entry.getKey();
            Aposta aposta = entry.getValue();

            if (!partida.temResultado()) {
                continue;
            }

            if (aposta.getGolsMandante() == partida.getResultado().getGolsMandante()
                    && aposta.getGolsVisitante() == partida.getResultado().getGolsVisitante()) {
                pontuacao += 10;
            } else if (aposta.getResultadoPartida() == partida.getResultado().getResultadoPartida()) {
                pontuacao += 5;
            }
        }
        return pontuacao;
    }

    @Override
    public String getTipoUsuario() {
        return "Participante";
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Map<Partida, Aposta> getApostas() {
        return Collections.unmodifiableMap(apostas);
    }

    public int getPontuacao() {
        return pontuacao;
    }
}
