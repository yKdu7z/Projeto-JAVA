import java.time.LocalDateTime;

public class Partida {

    private Clube clubeMandante;
    private Clube clubeVisitante;
    private LocalDateTime dataHora;
    private Placar resultado;

    public Partida(Clube clubeMandante, Clube clubeVisitante, LocalDateTime dataHora) {
        setClubeMandante(clubeMandante);
        setClubeVisitante(clubeVisitante);
        setDataHora(dataHora);
        validarClubesDiferentes();
    }

    public void registrarResultado(int golsMandante, int golsVisitante) {
        this.resultado = new Placar(golsMandante, golsVisitante);
    }

    public void registrarResultado(Placar placar) {
        if (placar == null) {
            throw new IllegalArgumentException("Informe um placar valido.");
        }
        this.resultado = placar;
    }

    public boolean podeReceberAposta(LocalDateTime dataAposta) {
        if (dataAposta == null) {
            return false;
        }
        return !dataAposta.isAfter(dataHora.minusMinutes(20));
    }

    public boolean temResultado() {
        return resultado != null;
    }

    public String getDescricao() {
        return clubeMandante.getNome() + " x " + clubeVisitante.getNome() + " - " + Sistema.formatarDataHora(dataHora);
    }

    public Clube getClubeMandante() {
        return clubeMandante;
    }

    public void setClubeMandante(Clube clubeMandante) {
        if (clubeMandante == null) {
            throw new IllegalArgumentException("Clube mandante obrigatorio.");
        }
        this.clubeMandante = clubeMandante;
    }

    public Clube getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(Clube clubeVisitante) {
        if (clubeVisitante == null) {
            throw new IllegalArgumentException("Clube visitante obrigatorio.");
        }
        this.clubeVisitante = clubeVisitante;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        if (dataHora == null) {
            throw new IllegalArgumentException("Data e hora da partida obrigatorias.");
        }
        this.dataHora = dataHora;
    }

    public Placar getResultado() {
        return resultado;
    }

    private void validarClubesDiferentes() {
        if (clubeMandante.equals(clubeVisitante)) {
            throw new IllegalArgumentException("Uma partida precisa ter dois clubes diferentes.");
        }
    }

    @Override
    public String toString() {
        return getDescricao();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Partida)) {
            return false;
        }
        Partida outra = (Partida) obj;
        return clubeMandante.equals(outra.clubeMandante)
                && clubeVisitante.equals(outra.clubeVisitante)
                && dataHora.equals(outra.dataHora);
    }

    @Override
    public int hashCode() {
        int valor = 17;
        valor = 31 * valor + clubeMandante.hashCode();
        valor = 31 * valor + clubeVisitante.hashCode();
        valor = 31 * valor + dataHora.hashCode();
        return valor;
    }
}
