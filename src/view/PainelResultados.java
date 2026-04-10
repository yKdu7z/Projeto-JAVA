import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PainelResultados extends JPanel {

    private final Sistema sistema;
    private final JComboBox<Partida> comboPartidas;
    private final JTextArea areaResultados;

    public PainelResultados(Sistema sistema) {
        this.sistema = sistema;
        this.comboPartidas = new JComboBox<>();
        this.areaResultados = new JTextArea();

        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(4, 2, 8, 8));
        JTextField campoMandante = new JTextField();
        JTextField campoVisitante = new JTextField();
        JButton botaoSalvar = new JButton("Salvar Resultado");

        formulario.add(new JLabel("Partida:"));
        formulario.add(comboPartidas);
        formulario.add(new JLabel("Gols mandante:"));
        formulario.add(campoMandante);
        formulario.add(new JLabel("Gols visitante:"));
        formulario.add(campoVisitante);
        formulario.add(new JLabel(""));
        formulario.add(botaoSalvar);

        areaResultados.setEditable(false);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(areaResultados), BorderLayout.CENTER);

        botaoSalvar.addActionListener(e -> {
            try {
                sistema.registrarResultado(
                        (Partida) comboPartidas.getSelectedItem(),
                        Integer.parseInt(campoMandante.getText()),
                        Integer.parseInt(campoVisitante.getText()));
                campoMandante.setText("");
                campoVisitante.setText("");
                atualizarDados();
                JOptionPane.showMessageDialog(this, "Resultado registrado com sucesso.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        atualizarDados();
    }

    public void atualizarDados() {
        comboPartidas.removeAllItems();
        for (Partida partida : sistema.getTodasPartidas()) {
            comboPartidas.addItem(partida);
        }

        StringBuilder builder = new StringBuilder();
        for (Partida partida : sistema.getTodasPartidas()) {
            builder.append(partida.getDescricao()).append(" -> ");
            builder.append(partida.temResultado() ? partida.getResultado().descrever() : "Sem resultado");
            builder.append('\n');
        }
        areaResultados.setText(builder.toString());
    }
}
