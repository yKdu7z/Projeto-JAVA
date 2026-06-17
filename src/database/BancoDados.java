import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BancoDados {

    private static final String URL_PADRAO =
            "jdbc:mysql://localhost:3306/projeto_java_poo?createDatabaseIfNotExist=true&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO_PADRAO = "root";
    private static final String SENHA_PADRAO = "";

    private final String url;
    private final String usuario;
    private final String senha;

    public BancoDados() {
        this.url = lerConfiguracao("DB_URL", URL_PADRAO);
        this.usuario = lerConfiguracao("DB_USER", USUARIO_PADRAO);
        this.senha = lerConfiguracao("DB_PASSWORD", SENHA_PADRAO);
    }

    public Connection conectar() throws SQLException {
        carregarDriver();
        return DriverManager.getConnection(url, usuario, senha);
    }

    public String getUrl() {
        return url;
    }

    private String lerConfiguracao(String chave, String valorPadrao) {
        String valor = System.getenv(chave);
        if (valor == null || valor.trim().isEmpty()) {
            return valorPadrao;
        }
        return valor.trim();
    }

    private void carregarDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(
                    "Driver MySQL nao encontrado. Adicione o mysql-connector-j ao projeto.", ex);
        }
    }
}
