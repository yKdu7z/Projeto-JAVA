public class Administrador extends Usuario {

    public Administrador() {
        this("Administrador");
    }

    public Administrador(String nome) {
        super(nome);
    }

    @Override
    public String getTipoUsuario() {
        return "Administrador";
    }
}
