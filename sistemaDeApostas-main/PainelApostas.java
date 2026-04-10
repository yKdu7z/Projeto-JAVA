import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PainelApostas extends JPanel {

    private final Sistema sistema;
    private final JComboBox<Campeonato> comboCampeonatoClubes;
    private final JComboBox<Clube> comboClubeCampeonato;
    private final JComboBox<Campeonato> comboCampeonatoPartida;
    private final JComboBox<Clube> comboMandante;
    private final JComboBox<Clube> comboVisitante;
    private final JComboBox<Participante> comboParticipante;
    private final JComboBox<Partida> comboPartidaAposta;
    private final JTextArea areaResumo;

    public PainelApostas(Sistema sistema) {
        this.sistema = sistema;
        this.comboCampeonatoClubes = new JComboBox<>();
        this.comboClubeCampeonato = new JComboBox<>();
        this.comboCampeonatoPartida = new JComboBox<>();
        this.comboMandante = new JComboBox<>();
        this.comboVisitante = new JComboBox<>();
        this.comboParticipante = new JComboBox<>();
        this.comboPartidaAposta = new JComboBox<>();
        this.areaResumo = new JTextArea();

        setLayout(new BorderLayout(10, 10));

        JPanel formularios = new JPanel();
        formularios.setLayout(new BoxLayout(formularios, BoxLayout.Y_AXIS));

        formularios.add(criarPainelCadastroClube());
        formularios.add(criarPainelCadastroCampeonato());
        formularios.add(criarPainelCadastroPartida());
        formularios.add(criarPainelAposta());

        areaResumo.setEditable(false);

        add(new JScrollPane(formularios), BorderLayout.CENTER);
        add(new JScrollPane(areaResumo), BorderLayout.EAST);

        comboCampeonatoPartida.addActionListener(e -> atualizarClubesDaPartida());
        atualizarDados();
    }

    private JPanel criarPainelCadastroClube() {
        JPanel painel = new JPanel(new GridLayout(2, 2, 8, 8));
        JTextField campoClube = new JTextField();
        JButton botao = new JButton("Cadastrar Clube");

        painel.add(new JLabel("Nome do clube:"));
        painel.add(campoClube);
        painel.add(new JLabel(""));
        painel.add(botao);

        botao.addActionListener(e -> {
            try {
                sistema.cadastrarClube(campoClube.getText());
                campoClube.setText("");
                atualizarDados();
                JOptionPane.showMessageDialog(this, "Clube cadastrado com sucesso.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        return painel;
    }

    private JPanel criarPainelCadastroCampeonato() {
        JPanel externo = new JPanel(new GridLayout(2, 1, 8, 8));

        JPanel painelCampeonato = new JPanel(new GridLayout(2, 2, 8, 8));
        JTextField campoCampeonato = new JTextField();
        JButton botaoCampeonato = new JButton("Cadastrar Campeonato");
        painelCampeonato.add(new JLabel("Nome do campeonato:"));
        painelCampeonato.add(campoCampeonato);
        painelCampeonato.add(new JLabel(""));
        painelCampeonato.add(botaoCampeonato);

        botaoCampeonato.addActionListener(e -> {
            try {
                sistema.cadastrarCampeonato(campoCampeonato.getText());
                campoCampeonato.setText("");
                atualizarDados();
                JOptionPane.showMessageDialog(this, "Campeonato cadastrado com sucesso.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        JPanel painelAssociacao = new JPanel(new GridLayout(3, 2, 8, 8));
        JButton botaoAssociar = new JButton("Adicionar Clube ao Campeonato");
        painelAssociacao.add(new JLabel("Campeonato:"));
        painelAssociacao.add(comboCampeonatoClubes);
        painelAssociacao.add(new JLabel("Clube:"));
        painelAssociacao.add(comboClubeCampeonato);
        painelAssociacao.add(new JLabel(""));
        painelAssociacao.add(botaoAssociar);

        botaoAssociar.addActionListener(e -> {
            try {
                sistema.adicionarClubeAoCampeonato(
                        (Campeonato) comboCampeonatoClubes.getSelectedItem(),
                        (Clube) comboClubeCampeonato.getSelectedItem());
                atualizarDados();
                JOptionPane.showMessageDialog(this, "Clube vinculado ao campeonato.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        externo.add(painelCampeonato);
        externo.add(painelAssociacao);
        return externo;
    }

    private JPanel criarPainelCadastroPartida() {
        JPanel painel = new JPanel(new GridLayout(5, 2, 8, 8));
        JTextField campoDataHora = new JTextField();
        JButton botao = new JButton("Cadastrar Partida");

        painel.add(new JLabel("Campeonato:"));
        painel.add(comboCampeonatoPartida);
        painel.add(new JLabel("Clube mandante:"));
        painel.add(comboMandante);
        painel.add(new JLabel("Clube visitante:"));
        painel.add(comboVisitante);
        painel.add(new JLabel("Data e hora (dd/MM/yyyy HH:mm):"));
        painel.add(campoDataHora);
        painel.add(new JLabel(""));
        painel.add(botao);

        botao.addActionListener(e -> {
            try {
                sistema.cadastrarPartida(
                        (Campeonato) comboCampeonatoPartida.getSelectedItem(),
                        (Clube) comboMandante.getSelectedItem(),
                        (Clube) comboVisitante.getSelectedItem(),
                        Sistema.parseDataHora(campoDataHora.getText()));
                campoDataHora.setText("");
                atualizarDados();
                JOptionPane.showMessageDialog(this, "Partida cadastrada com sucesso.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        return painel;
    }

    private JPanel criarPainelAposta() {
        JPanel painel = new JPanel(new GridLayout(4, 2, 8, 8));
        JTextField campoMandante = new JTextField();
        JTextField campoVisitante = new JTextField();
        JButton botao = new JButton("Registrar Aposta");

        painel.add(new JLabel("Participante:"));
        painel.add(comboParticipante);
        painel.add(new JLabel("Partida:"));
        painel.add(comboPartidaAposta);
        painel.add(new JLabel("Placar (mandante / visitante):"));
        painel.add(criarPainelPlacar(campoMandante, campoVisitante));
        painel.add(new JLabel(""));
        painel.add(botao);

        botao.addActionListener(e -> {
            try {
                sistema.registrarAposta(
                        (Participante) comboParticipante.getSelectedItem(),
                        (Partida) comboPartidaAposta.getSelectedItem(),
                        Integer.parseInt(campoMandante.getText()),
                        Integer.parseInt(campoVisitante.getText()));
                campoMandante.setText("");
                campoVisitante.setText("");
                atualizarDados();
                JOptionPane.showMessageDialog(this, "Aposta registrada com sucesso.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        return painel;
    }

    private JPanel criarPainelPlacar(JTextField campoMandante, JTextField campoVisitante) {
        JPanel painel = new JPanel(new GridLayout(1, 3, 4, 4));
        painel.add(campoMandante);
        painel.add(new JLabel("x"));
        painel.add(campoVisitante);
        return painel;
    }

    public void atualizarDados() {
        preencherCombo(comboClubeCampeonato, sistema.getClubes());
        preencherCombo(comboCampeonatoClubes, sistema.getCampeonatos());
        preencherCombo(comboCampeonatoPartida, sistema.getCampeonatos());
        preencherCombo(comboParticipante, sistema.getParticipantes());
        preencherCombo(comboPartidaAposta, sistema.getTodasPartidas());
        atualizarClubesDaPartida();
        atualizarResumo();
    }

    private void atualizarClubesDaPartida() {
        comboMandante.removeAllItems();
        comboVisitante.removeAllItems();

        Campeonato campeonato = (Campeonato) comboCampeonatoPartida.getSelectedItem();
        if (campeonato == null) {
            return;
        }

        for (Clube clube : campeonato.getClubes()) {
            comboMandante.addItem(clube);
            comboVisitante.addItem(clube);
        }
    }

    private void atualizarResumo() {
        StringBuilder resumo = new StringBuilder();
        resumo.append("Administrador: ").append(sistema.getAdministrador().resumoCadastro()).append("\n\n");

        resumo.append("Clubes cadastrados:\n");
        for (Clube clube : sistema.getClubes()) {
            resumo.append("- ").append(clube.getNome()).append('\n');
        }

        resumo.append("\nCampeonatos:\n");
        for (Campeonato campeonato : sistema.getCampeonatos()) {
            resumo.append("- ").append(campeonato.getNome()).append(" (")
                    .append(campeonato.getClubes().size()).append(" clubes)\n");
        }

        resumo.append("\nPartidas:\n");
        for (Partida partida : sistema.getTodasPartidas()) {
            resumo.append("- ").append(partida.getDescricao()).append('\n');
        }

        areaResumo.setText(resumo.toString());
    }

    private <T> void preencherCombo(JComboBox<T> combo, java.util.List<T> itens) {
        combo.removeAllItems();
        for (T item : itens) {
            combo.addItem(item);
        }
    }
}
