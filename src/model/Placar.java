public class Placar {

    private int golsMandante;
    private int golsVisitante;

    public Placar() {
        this(0, 0);
    }

    public Placar(int golsMandante, int golsVisitante) {
        setGolsMandante(golsMandante);
        setGolsVisitante(golsVisitante);
    }

    public int getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(int golsMandante) {
        if (golsMandante < 0) {
            throw new IllegalArgumentException("Gols do mandante nao podem ser negativos.");
        }
        this.golsMandante = golsMandante;
    }

    public int getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(int golsVisitante) {
        if (golsVisitante < 0) {
            throw new IllegalArgumentException("Gols do visitante nao podem ser negativos.");
        }
        this.golsVisitante = golsVisitante;
    }

    public ResultadoPartida getResultadoPartida() {
        if (golsMandante > golsVisitante) {
            return ResultadoPartida.MANDANTE;
        }
        if (golsMandante < golsVisitante) {
            return ResultadoPartida.VISITANTE;
        }
        return ResultadoPartida.EMPATE;
    }

    public String descrever() {
        return golsMandante + " x " + golsVisitante;
    }

    @Override
    public String toString() {
        return descrever();
    }
}
