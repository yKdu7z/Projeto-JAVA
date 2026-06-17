import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PainelBancoDados extends JPanel {

    private final Sistema sistema;
    private final JLabel labelStatus;
    private final JTextArea areaEventos;

    public PainelBancoDados(Sistema sistema) {
        this.sistema = sistema;
        this.labelStatus = new JLabel();
        this.areaEventos = new JTextArea();

        setLayout(new BorderLayout(10, 10));
        areaEventos.setEditable(false);

        JButton botaoAtualizar = new JButton("Atualizar historico");
        botaoAtualizar.addActionListener(e -> atualizarDados());

        JPanel topo = new JPanel(new BorderLayout(8, 8));
        topo.add(labelStatus, BorderLayout.CENTER);
        topo.add(botaoAtualizar, BorderLayout.EAST);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(areaEventos), BorderLayout.CENTER);

        atualizarDados();
    }

    public void atualizarDados() {
        labelStatus.setText(sistema.getStatusBancoDados());

        StringBuilder builder = new StringBuilder();
        for (String evento : sistema.getEventosBancoDados()) {
            builder.append(evento).append('\n');
        }
        if (builder.length() == 0) {
            builder.append("Nenhum evento gravado ainda.");
        }
        areaEventos.setText(builder.toString());
    }
}
