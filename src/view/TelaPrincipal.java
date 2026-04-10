import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TelaPrincipal extends JFrame {

    private final CardLayout layout;
    private final JPanel painelCentral;
    private final Sistema sistema;
    private final PainelParticipantes painelParticipantes;
    private final PainelApostas painelApostas;
    private final PainelResultados painelResultados;
    private final PainelClassificacao painelClassificacao;

    public TelaPrincipal() {
        this.sistema = new Sistema();
        this.layout = new CardLayout();
        this.painelCentral = new JPanel(layout);
        this.painelParticipantes = new PainelParticipantes(sistema);
        this.painelApostas = new PainelApostas(sistema);
        this.painelResultados = new PainelResultados(sistema);
        this.painelClassificacao = new PainelClassificacao(sistema);

        setTitle("Sistema de Apostas - Campeonato de Futebol");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel menu = new JPanel(new GridLayout(4, 1, 8, 8));
        menu.setBackground(new Color(20, 30, 50));

        JButton btnParticipantes = criarBotao("Grupos e Participantes");
        JButton btnApostas = criarBotao("Clubes, Campeonato e Apostas");
        JButton btnResultados = criarBotao("Resultados");
        JButton btnClassificacao = criarBotao("Classificacao");

        menu.add(btnParticipantes);
        menu.add(btnApostas);
        menu.add(btnResultados);
        menu.add(btnClassificacao);

        add(menu, BorderLayout.WEST);

        painelCentral.add(painelParticipantes, "participantes");
        painelCentral.add(painelApostas, "apostas");
        painelCentral.add(painelResultados, "resultados");
        painelCentral.add(painelClassificacao, "classificacao");
        add(painelCentral, BorderLayout.CENTER);

        btnParticipantes.addActionListener(e -> mostrarPainel("participantes"));
        btnApostas.addActionListener(e -> mostrarPainel("apostas"));
        btnResultados.addActionListener(e -> mostrarPainel("resultados"));
        btnClassificacao.addActionListener(e -> mostrarPainel("classificacao"));

        mostrarPainel("participantes");
    }

    private void mostrarPainel(String nomePainel) {
        painelParticipantes.atualizarDados();
        painelApostas.atualizarDados();
        painelResultados.atualizarDados();
        painelClassificacao.atualizarDados();
        layout.show(painelCentral, nomePainel);
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFocusPainted(false);
        botao.setBackground(new Color(40, 60, 90));
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        return botao;
    }
}
