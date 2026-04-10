import java.time.LocalDateTime;

public class Aposta extends Placar {

    private LocalDateTime dataRegistro;

    public Aposta() {
        this(0, 0, LocalDateTime.now());
    }

    public Aposta(int golsMandante, int golsVisitante) {
        this(golsMandante, golsVisitante, LocalDateTime.now());
    }

    public Aposta(int golsMandante, int golsVisitante, LocalDateTime dataRegistro) {
        super(golsMandante, golsVisitante);
        setDataRegistro(dataRegistro);
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        if (dataRegistro == null) {
            throw new IllegalArgumentException("Data da aposta obrigatoria.");
        }
        this.dataRegistro = dataRegistro;
    }

    @Override
    public String descrever() {
        return super.descrever() + " (aposta em " + Sistema.formatarDataHora(dataRegistro) + ")";
    }
}
