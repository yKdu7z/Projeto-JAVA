public abstract class Usuario {

    private String nome;

    public Usuario() {
        this("Usuario sem nome");
    }

    public Usuario(String nome) {
        setNome(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do usuario obrigatorio.");
        }
        this.nome = nome.trim();
    }

    public String resumoCadastro() {
        return getTipoUsuario() + ": " + nome;
    }

    public abstract String getTipoUsuario();

    @Override
    public String toString() {
        return nome;
    }
}
