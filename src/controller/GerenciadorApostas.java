import javax.swing.SwingUtilities;

public class GerenciadorApostas {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
