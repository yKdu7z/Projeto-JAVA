import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PainelParticipantes extends JPanel {

    private final Sistema sistema;
    private final DefaultTableModel modeloGrupos;
    private final DefaultTableModel modeloParticipantes;
    private final JComboBox<Grupo> comboGrupos;

    public PainelParticipantes(Sistema sistema) {
        this.sistema = sistema;
        this.modeloGrupos = new DefaultTableModel(new Object[] { "Grupo", "Qtd. Participantes" }, 0);
        this.modeloParticipantes = new DefaultTableModel(new Object[] { "Participante", "Grupo", "Tipo" }, 0);
        this.comboGrupos = new JComboBox<>();

        setLayout(new GridLayout(2, 1, 10, 10));

        JPanel painelGrupos = new JPanel(new BorderLayout(8, 8));
        JTextField campoGrupo = new JTextField();
        JButton btnGrupo = new JButton("Cadastrar Grupo");

        JPanel formGrupo = new JPanel(new GridLayout(2, 2, 8, 8));
        formGrupo.add(new JLabel("Nome do grupo:"));
        formGrupo.add(campoGrupo);
        formGrupo.add(new JLabel(""));
        formGrupo.add(btnGrupo);

        painelGrupos.add(new JLabel("Cadastro de grupos"), BorderLayout.NORTH);
        painelGrupos.add(formGrupo, BorderLayout.SOUTH);
        painelGrupos.add(new JScrollPane(new JTable(modeloGrupos)), BorderLayout.CENTER);

        JPanel painelParticipantes = new JPanel(new BorderLayout(8, 8));
        JTextField campoParticipante = new JTextField();
        JButton btnParticipante = new JButton("Cadastrar Participante");

        JPanel formParticipante = new JPanel(new GridLayout(3, 2, 8, 8));
        formParticipante.add(new JLabel("Nome do participante:"));
        formParticipante.add(campoParticipante);
        formParticipante.add(new JLabel("Grupo:"));
        formParticipante.add(comboGrupos);
        formParticipante.add(new JLabel(""));
        formParticipante.add(btnParticipante);

        painelParticipantes.add(new JLabel("Cadastro de participantes"), BorderLayout.NORTH);
        painelParticipantes.add(formParticipante, BorderLayout.SOUTH);
        painelParticipantes.add(new JScrollPane(new JTable(modeloParticipantes)), BorderLayout.CENTER);

        add(painelGrupos);
        add(painelParticipantes);

        btnGrupo.addActionListener(e -> {
            try {
                sistema.cadastrarGrupo(campoGrupo.getText());
                campoGrupo.setText("");
                atualizarDados();
                JOptionPane.showMessageDialog(this, "Grupo cadastrado com sucesso.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnParticipante.addActionListener(e -> {
            try {
                Grupo grupo = (Grupo) comboGrupos.getSelectedItem();
                sistema.cadastrarParticipante(campoParticipante.getText(), grupo);
                campoParticipante.setText("");
                atualizarDados();
                JOptionPane.showMessageDialog(this, "Participante cadastrado com sucesso.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        atualizarDados();
    }

    public void atualizarDados() {
        comboGrupos.removeAllItems();
        modeloGrupos.setRowCount(0);
        modeloParticipantes.setRowCount(0);

        for (Grupo grupo : sistema.getGrupos()) {
            comboGrupos.addItem(grupo);
            modeloGrupos.addRow(new Object[] { grupo.getNome(), grupo.getParticipantes().size() });
        }

        for (Participante participante : sistema.getParticipantes()) {
            String grupo = participante.getGrupo() == null ? "-" : participante.getGrupo().getNome();
            modeloParticipantes.addRow(new Object[] {
                    participante.getNome(),
                    grupo,
                    participante.getTipoUsuario()
            });
        }
    }
}
