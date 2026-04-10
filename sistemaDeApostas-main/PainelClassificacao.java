import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PainelClassificacao extends JPanel {

    private final Sistema sistema;
    private final DefaultTableModel modelo;
    private final JComboBox<Grupo> comboGrupos;

    public PainelClassificacao(Sistema sistema) {
        this.sistema = sistema;
        this.modelo = new DefaultTableModel(new Object[] { "Posicao", "Participante", "Pontos", "Grupo" }, 0);
        this.comboGrupos = new JComboBox<>();

        setLayout(new BorderLayout(10, 10));

        JPanel topo = new JPanel();
        JButton botaoAtualizar = new JButton("Atualizar Classificacao");
        topo.add(new JLabel("Grupo:"));
        topo.add(comboGrupos);
        topo.add(botaoAtualizar);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);

        botaoAtualizar.addActionListener(e -> atualizarTabela());
        atualizarDados();
    }

    public void atualizarDados() {
        comboGrupos.removeAllItems();
        for (Grupo grupo : sistema.getGrupos()) {
            comboGrupos.addItem(grupo);
        }
        atualizarTabela();
    }

    private void atualizarTabela() {
        sistema.atualizarPontuacoes();
        modelo.setRowCount(0);

        Grupo grupo = (Grupo) comboGrupos.getSelectedItem();
        if (grupo == null) {
            return;
        }

        java.util.List<Participante> classificacao = grupo.getClassificacao();
        for (int i = 0; i < classificacao.size(); i++) {
            Participante participante = classificacao.get(i);
            modelo.addRow(new Object[] {
                    i + 1,
                    participante.getNome(),
                    participante.getPontuacao(),
                    grupo.getNome()
            });
        }
    }
}
