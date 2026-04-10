public class Clube {

    private String nome;

    public Clube() {
        this("Clube sem nome");
    }

    public Clube(String nome) {
        setNome(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do clube obrigatorio.");
        }
        this.nome = nome.trim();
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Clube)) {
            return false;
        }
        Clube outro = (Clube) obj;
        return nome.equalsIgnoreCase(outro.nome);
    }

    @Override
    public int hashCode() {
        return nome.toLowerCase().hashCode();
    }
}
