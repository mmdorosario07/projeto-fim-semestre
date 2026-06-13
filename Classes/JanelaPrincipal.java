import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Arrays;

/**
 * Classe JanelaPrincipal - Interface gráfica principal do Quiz de Futebol.
 * Utiliza Swing com JFrame, JButton, JLabel, JTextField e eventos.
 * Gere os ecrãs: Menu, Jogo, Resultado, Ranking e Admin.
 */
public class JanelaPrincipal extends JFrame {

    // ─── Constantes visuais ───────────────────────────────────────────────
    private static final Color COR_VERDE      = new Color(0, 128, 0);
    private static final Color COR_VERDE_DARK = new Color(0, 100, 0);
    private static final Color COR_BRANCO     = Color.WHITE;
    private static final Color COR_CINZA      = new Color(240, 240, 240);
    private static final Color COR_VERMELHO   = new Color(200, 0, 0);
    private static final Color COR_AMARELO    = new Color(255, 215, 0);
    private static final Color COR_AZUL       = new Color(30, 80, 180);
    private static final Font FONTE_TITULO    = new Font("Arial", Font.BOLD, 26);
    private static final Font FONTE_PERGUNTA  = new Font("Arial", Font.BOLD, 16);
    private static final Font FONTE_OPCAO     = new Font("Arial", Font.PLAIN, 14);
    private static final Font FONTE_INFO      = new Font("Arial", Font.BOLD, 13);

    // ─── Estado do jogo ───────────────────────────────────────────────────
    private Jogador      jogador;
    private Quiz         quiz;
    private Ranking      ranking;
    private Administrador admin;
    private javax.swing.Timer timer;
    private int          tempoRestante;

    // ─── Caminho do CSV ───────────────────────────────────────────────────
    private String caminhoCSV = "perguntas.csv";

    // ─── Painel principal (CardLayout para trocar ecrãs) ─────────────────
    private JPanel painelPrincipal;
    private CardLayout cardLayout;

    // ─── Componentes do ecrã de Jogo ─────────────────────────────────────
    private JLabel   lblPergunta, lblNumero, lblPontuacao, lblTimer;
    private JButton[] botoesOpcoes;
    private JPanel   painelFeedback;
    private JLabel   lblFeedback;

    // ─── Construtor ───────────────────────────────────────────────────────
    public JanelaPrincipal() {
        ranking = new Ranking();
        admin   = new Administrador("Admin", "admin@quiz.com", "admin123");

        setTitle("⚽ Quiz de Futebol");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout     = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);
        painelPrincipal.setBackground(COR_BRANCO);

        painelPrincipal.add(criarEcrăMenu(),      "MENU");
        painelPrincipal.add(criarEcrăJogo(),      "JOGO");
        painelPrincipal.add(criarEcrăResultado(), "RESULTADO");
        painelPrincipal.add(criarEcrăRanking(),   "RANKING");
        painelPrincipal.add(criarEcrăAdmin(),     "ADMIN");

        add(painelPrincipal);
        mostrarEcrã("MENU");
        setVisible(true);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ECRÃ 1 — MENU PRINCIPAL
    // ══════════════════════════════════════════════════════════════════════
    private JPanel criarEcrăMenu() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_VERDE_DARK);

        // Cabeçalho
        JLabel lblTitulo = new JLabel("⚽ QUIZ DE FUTEBOL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(COR_AMARELO);
        lblTitulo.setBorder(new EmptyBorder(30, 0, 10, 0));
        painel.add(lblTitulo, BorderLayout.NORTH);

        // Centro — campo para nome + botões
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(COR_VERDE_DARK);
        centro.setBorder(new EmptyBorder(20, 100, 20, 100));

        JLabel lblNome = new JLabel("O teu nome:");
        lblNome.setFont(FONTE_INFO);
        lblNome.setForeground(COR_BRANCO);
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtNome = new JTextField("Jogador");
        txtNome.setFont(FONTE_OPCAO);
        txtNome.setMaximumSize(new Dimension(300, 35));
        txtNome.setHorizontalAlignment(JTextField.CENTER);

        JButton btnJogar   = criarBotao("▶  JOGAR",     COR_AMARELO,    COR_VERDE_DARK);
        JButton btnRanking = criarBotao("🏆  RANKING",   COR_BRANCO,     COR_AZUL);
        JButton btnAdmin   = criarBotao("⚙  ADMINISTRADOR", COR_BRANCO, new Color(80,80,80));

        // Eventos
        btnJogar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            if (nome.isEmpty()) nome = "Jogador";
            iniciarJogo(nome);
        });

        btnRanking.addActionListener(e -> {
            atualizarEcrăRanking();
            mostrarEcrã("RANKING");
        });

        btnAdmin.addActionListener(e -> {
            String pass = JOptionPane.showInputDialog(this,
                    "Introduza a password de administrador:", "Acesso Admin",
                    JOptionPane.PLAIN_MESSAGE);
            if (pass != null && admin.verificarPassword(pass)) {
                mostrarEcrã("ADMIN");
            } else if (pass != null) {
                JOptionPane.showMessageDialog(this, "Password incorreta!", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        centro.add(Box.createVerticalStrut(10));
        centro.add(lblNome);
        centro.add(Box.createVerticalStrut(5));
        centro.add(txtNome);
        centro.add(Box.createVerticalStrut(20));
        centro.add(btnJogar);
        centro.add(Box.createVerticalStrut(10));
        centro.add(btnRanking);
        centro.add(Box.createVerticalStrut(10));
        centro.add(btnAdmin);

        painel.add(centro, BorderLayout.CENTER);

        JLabel lblRodape = new JLabel("20 perguntas · +3 pts por acerto · 60s por pergunta",
                SwingConstants.CENTER);
        lblRodape.setFont(new Font("Arial", Font.ITALIC, 11));
        lblRodape.setForeground(new Color(180,220,180));
        lblRodape.setBorder(new EmptyBorder(0,0,15,0));
        painel.add(lblRodape, BorderLayout.SOUTH);

        return painel;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ECRÃ 2 — JOGO
    // ══════════════════════════════════════════════════════════════════════
    private JPanel criarEcrăJogo() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(COR_BRANCO);
        painel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // ── Topo: número, pontuação e timer ──────────────────────────────
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(COR_VERDE);
        topo.setBorder(new EmptyBorder(8, 15, 8, 15));

        lblNumero    = new JLabel("Pergunta 1/20", SwingConstants.LEFT);
        lblPontuacao = new JLabel("Pontos: 0",    SwingConstants.CENTER);
        lblTimer     = new JLabel("⏱ 60s",        SwingConstants.RIGHT);

        for (JLabel l : new JLabel[]{lblNumero, lblPontuacao, lblTimer}) {
            l.setFont(FONTE_INFO);
            l.setForeground(COR_BRANCO);
        }
        lblTimer.setFont(new Font("Arial", Font.BOLD, 15));

        topo.add(lblNumero,    BorderLayout.WEST);
        topo.add(lblPontuacao, BorderLayout.CENTER);
        topo.add(lblTimer,     BorderLayout.EAST);
        painel.add(topo, BorderLayout.NORTH);

        // ── Centro: enunciado + opções ────────────────────────────────────
        JPanel centro = new JPanel(new BorderLayout(0, 15));
        centro.setBackground(COR_BRANCO);

        lblPergunta = new JLabel("<html><div style='text-align:center'>Pergunta</div></html>",
                SwingConstants.CENTER);
        lblPergunta.setFont(FONTE_PERGUNTA);
        lblPergunta.setForeground(new Color(20,20,20));
        lblPergunta.setBorder(new EmptyBorder(15, 10, 10, 10));
        lblPergunta.setPreferredSize(new Dimension(650, 80));
        centro.add(lblPergunta, BorderLayout.NORTH);

        // 4 botões de opção (A, B, C, D)
        JPanel painelOpcoes = new JPanel(new GridLayout(4, 1, 0, 8));
        painelOpcoes.setBackground(COR_BRANCO);
        botoesOpcoes = new JButton[4];
        String[] letras = {"A", "B", "C", "D"};

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            botoesOpcoes[i] = new JButton(letras[i] + ") Opção " + (i + 1));
            botoesOpcoes[i].setFont(FONTE_OPCAO);
            botoesOpcoes[i].setBackground(COR_CINZA);
            botoesOpcoes[i].setFocusPainted(false);
            botoesOpcoes[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180)),
                    new EmptyBorder(10, 15, 10, 15)));
            botoesOpcoes[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            botoesOpcoes[i].addActionListener(e -> processarResposta(idx));
            painelOpcoes.add(botoesOpcoes[i]);
        }
        centro.add(painelOpcoes, BorderLayout.CENTER);

        // ── Feedback (correto / errado) ───────────────────────────────────
        painelFeedback = new JPanel();
        painelFeedback.setBackground(COR_BRANCO);
        lblFeedback = new JLabel("", SwingConstants.CENTER);
        lblFeedback.setFont(new Font("Arial", Font.BOLD, 14));
        painelFeedback.add(lblFeedback);
        painelFeedback.setVisible(false);
        centro.add(painelFeedback, BorderLayout.SOUTH);

        painel.add(centro, BorderLayout.CENTER);
        return painel;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ECRÃ 3 — RESULTADO FINAL
    // ══════════════════════════════════════════════════════════════════════
    private JPanel criarEcrăResultado() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_VERDE_DARK);
        painel.setName("RESULTADO_PAINEL");
        return painel; // preenchido dinamicamente em mostrarResultado()
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ECRÃ 4 — RANKING
    // ══════════════════════════════════════════════════════════════════════
    private JPanel criarEcrăRanking() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_BRANCO);
        painel.setName("RANKING_PAINEL");
        return painel; // preenchido dinamicamente em atualizarEcrăRanking()
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ECRÃ 5 — ADMINISTRADOR
    // ══════════════════════════════════════════════════════════════════════
    private JPanel criarEcrăAdmin() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(30, 30, 30));
        painel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitulo = new JLabel("⚙ PAINEL DE ADMINISTRADOR", SwingConstants.CENTER);
        lblTitulo.setFont(FONTE_TITULO);
        lblTitulo.setForeground(COR_AMARELO);
        painel.add(lblTitulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(new Color(30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JLabel lblQ  = new JLabel("Pergunta:");    lblQ.setForeground(COR_BRANCO);
        JLabel lblO1 = new JLabel("Opção A:");     lblO1.setForeground(COR_BRANCO);
        JLabel lblO2 = new JLabel("Opção B:");     lblO2.setForeground(COR_BRANCO);
        JLabel lblO3 = new JLabel("Opção C:");     lblO3.setForeground(COR_BRANCO);
        JLabel lblO4 = new JLabel("Opção D:");     lblO4.setForeground(COR_BRANCO);
        JLabel lblC  = new JLabel("Correta (0-3):"); lblC.setForeground(COR_BRANCO);

        JTextField txtQ  = new JTextField(30);
        JTextField txtO1 = new JTextField(30);
        JTextField txtO2 = new JTextField(30);
        JTextField txtO3 = new JTextField(30);
        JTextField txtO4 = new JTextField(30);
        JTextField txtC  = new JTextField(5);

        JLabel[] labels  = {lblQ, lblO1, lblO2, lblO3, lblO4, lblC};
        JTextField[] campos = {txtQ, txtO1, txtO2, txtO3, txtO4, txtC};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            centro.add(labels[i], gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            centro.add(campos[i], gbc);
        }

        JButton btnGuardar = criarBotao("💾  Guardar Pergunta", COR_AMARELO, new Color(50,50,50));
        btnGuardar.addActionListener(e -> {
            try {
                String enunciado = txtQ.getText().trim();
                String[] opcoes  = {txtO1.getText().trim(), txtO2.getText().trim(),
                        txtO3.getText().trim(), txtO4.getText().trim()};
                int correta = Integer.parseInt(txtC.getText().trim());

                if (enunciado.isEmpty() || Arrays.stream(opcoes).anyMatch(String::isEmpty)) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (correta < 0 || correta > 3) {
                    JOptionPane.showMessageDialog(this, "Correta deve ser 0, 1, 2 ou 3!", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                guardarPerguntaCSV(enunciado, opcoes, correta);
                JOptionPane.showMessageDialog(this, "Pergunta guardada com sucesso! ✅", "OK",
                        JOptionPane.INFORMATION_MESSAGE);

                for (JTextField c : campos) c.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O campo 'Correta' deve ser um número (0-3)!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        centro.add(btnGuardar, gbc);

        painel.add(centro, BorderLayout.CENTER);

        JButton btnVoltar = criarBotao("← Voltar ao Menu", COR_BRANCO, new Color(80,80,80));
        btnVoltar.addActionListener(e -> mostrarEcrã("MENU"));
        painel.add(btnVoltar, BorderLayout.SOUTH);

        return painel;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  LÓGICA DO JOGO
    // ══════════════════════════════════════════════════════════════════════

    /** Inicia um novo jogo com o nome fornecido. */
    private void iniciarJogo(String nome) {
        jogador = new Jogador(nome, "");
        quiz    = new Quiz(jogador);
        quiz.carregarPerguntasCSV(caminhoCSV);

        if (quiz.getPerguntas().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível carregar as perguntas.\nVerifique se o ficheiro '"
                            + caminhoCSV + "' existe.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        mostrarEcrã("JOGO");
        mostrarPerguntaAtual();
    }

    /** Atualiza o ecrã de jogo com a pergunta atual. */
    private void mostrarPerguntaAtual() {
        if (quiz.terminou()) {
            pararTimer();
            mostrarResultado();
            return;
        }

        Pergunta p = quiz.getPerguntaAtual();
        lblNumero.setText("Pergunta " + quiz.getNumeroPerguntaAtual() + "/20");
        lblPontuacao.setText("Pontos: " + jogador.getPontuacao());
        lblPergunta.setText("<html><div style='text-align:center;padding:5px'>"
                + p.getEnunciado() + "</div></html>");

        String[] letras = {"A", "B", "C", "D"};
        String[] opcoes = p.getOpcoes();
        for (int i = 0; i < 4; i++) {
            botoesOpcoes[i].setText(letras[i] + ")  " + opcoes[i]);
            botoesOpcoes[i].setBackground(COR_CINZA);
            botoesOpcoes[i].setForeground(Color.BLACK);
            botoesOpcoes[i].setEnabled(true);
        }

        painelFeedback.setVisible(false);
        iniciarTimer();
    }

    /** Processa a resposta escolhida pelo jogador. */
    private void processarResposta(int indice) {
        pararTimer();
        desativarBotoes();

        boolean correta = quiz.responder(indice);
        Pergunta p      = quiz.getPerguntaAtual();

        // Feedback visual
        if (correta) {
            botoesOpcoes[indice].setBackground(new Color(0, 180, 0));
            botoesOpcoes[indice].setForeground(COR_BRANCO);
            lblFeedback.setText("✅  Correto! +3 pontos");
            lblFeedback.setForeground(new Color(0, 150, 0));
        } else {
            botoesOpcoes[indice].setBackground(COR_VERMELHO);
            botoesOpcoes[indice].setForeground(COR_BRANCO);
            botoesOpcoes[indice].setText(botoesOpcoes[indice].getText());
            // Mostra a resposta correta a verde
            botoesOpcoes[p.getRespostaCorreta()].setBackground(new Color(0, 180, 0));
            botoesOpcoes[p.getRespostaCorreta()].setForeground(COR_BRANCO);
            lblFeedback.setText("❌  Errado! Correto: " + p.getTextoRespostaCorreta());
            lblFeedback.setForeground(COR_VERMELHO);
        }
        painelFeedback.setVisible(true);
        lblPontuacao.setText("Pontos: " + jogador.getPontuacao());

        // Avança para a próxima pergunta após 1.5 segundos
        javax.swing.Timer atraso = new javax.swing.Timer(1500, e -> {
            quiz.avancarPergunta();
            mostrarPerguntaAtual();
        });
        atraso.setRepeats(false);
        atraso.start();
    }

    /** Inicia o temporizador de 60 segundos. */
    private void iniciarTimer() {
        pararTimer();
        tempoRestante = Quiz.TEMPO_POR_PERGUNTA;
        lblTimer.setText("⏱ " + tempoRestante + "s");
        lblTimer.setForeground(COR_BRANCO);

        timer = new javax.swing.Timer(1000, e -> {
            tempoRestante--;
            lblTimer.setText("⏱ " + tempoRestante + "s");

            // Aviso vermelho nos últimos 10 segundos
            if (tempoRestante <= 10) {
                lblTimer.setForeground(COR_AMARELO);
            }

            if (tempoRestante <= 0) {
                pararTimer();
                desativarBotoes();
                Pergunta p = quiz.getPerguntaAtual();
                lblFeedback.setText("⏰  Tempo esgotado! Correto: " + p.getTextoRespostaCorreta());
                lblFeedback.setForeground(new Color(200, 100, 0));
                botoesOpcoes[p.getRespostaCorreta()].setBackground(new Color(0, 180, 0));
                botoesOpcoes[p.getRespostaCorreta()].setForeground(COR_BRANCO);
                painelFeedback.setVisible(true);

                javax.swing.Timer atraso = new javax.swing.Timer(1500, ev -> {
                    quiz.avancarPergunta();
                    mostrarPerguntaAtual();
                });
                atraso.setRepeats(false);
                atraso.start();
            }
        });
        timer.start();
    }

    /** Para o temporizador. */
    private void pararTimer() {
        if (timer != null && timer.isRunning()) timer.stop();
    }

    /** Desativa todos os botões de opção. */
    private void desativarBotoes() {
        for (JButton b : botoesOpcoes) b.setEnabled(false);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ECRÃ RESULTADO — construído dinamicamente
    // ══════════════════════════════════════════════════════════════════════
    private void mostrarResultado() {
        int pts  = jogador.getPontuacao();
        int max  = Quiz.TOTAL_PERGUNTAS * Quiz.PONTOS_POR_ACERTO; // 60
        int pct  = (int) ((pts * 100.0) / max);

        ranking.adicionarEntrada(jogador.getNome(), pts);
        jogador.incrementarJogos();

        // Reconstrói o painel de resultado
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_VERDE_DARK);
        painel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel lblTitulo = new JLabel("FIM DO JOGO!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitulo.setForeground(COR_AMARELO);
        painel.add(lblTitulo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(COR_VERDE_DARK);

        String emoji = pct >= 80 ? "🏆" : pct >= 50 ? "⚽" : "📚";
        String msg   = pct >= 80 ? "Excelente! Craque do quiz!"
                : pct >= 50 ? "Bom jogo! Continua a treinar."
                  : "Precisas de estudar mais futebol!";

        JLabel lblEmoji = new JLabel(emoji, SwingConstants.CENTER);
        lblEmoji.setFont(new Font("Serif", Font.PLAIN, 60));
        lblEmoji.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNome = new JLabel(jogador.getNome(), SwingConstants.CENTER);
        lblNome.setFont(new Font("Arial", Font.BOLD, 22));
        lblNome.setForeground(COR_BRANCO);
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPts = new JLabel(pts + " / " + max + " pontos  (" + pct + "%)",
                SwingConstants.CENTER);
        lblPts.setFont(new Font("Arial", Font.BOLD, 28));
        lblPts.setForeground(COR_AMARELO);
        lblPts.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMsg = new JLabel(msg, SwingConstants.CENTER);
        lblMsg.setFont(new Font("Arial", Font.ITALIC, 16));
        lblMsg.setForeground(new Color(200, 240, 200));
        lblMsg.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnJogarNovamente = criarBotao("▶  Jogar Novamente", COR_AMARELO, COR_VERDE_DARK);
        btnJogarNovamente.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJogarNovamente.addActionListener(e -> iniciarJogo(jogador.getNome()));

        JButton btnRanking = criarBotao("🏆  Ver Ranking", COR_BRANCO, COR_AZUL);
        btnRanking.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRanking.addActionListener(e -> {
            atualizarEcrăRanking();
            mostrarEcrã("RANKING");
        });

        JButton btnMenu = criarBotao("⬅  Menu Principal", COR_BRANCO, new Color(80,80,80));
        btnMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMenu.addActionListener(e -> mostrarEcrã("MENU"));

        centro.add(Box.createVerticalStrut(10));
        centro.add(lblEmoji);
        centro.add(Box.createVerticalStrut(5));
        centro.add(lblNome);
        centro.add(Box.createVerticalStrut(10));
        centro.add(lblPts);
        centro.add(Box.createVerticalStrut(5));
        centro.add(lblMsg);
        centro.add(Box.createVerticalStrut(20));
        centro.add(btnJogarNovamente);
        centro.add(Box.createVerticalStrut(8));
        centro.add(btnRanking);
        centro.add(Box.createVerticalStrut(8));
        centro.add(btnMenu);

        painel.add(centro, BorderLayout.CENTER);

        // Substitui o painel de resultado
        painelPrincipal.remove(getComponentByName("RESULTADO_PAINEL"));
        painel.setName("RESULTADO_PAINEL");
        painelPrincipal.add(painel, "RESULTADO");
        mostrarEcrã("RESULTADO");
    }

    // ══════════════════════════════════════════════════════════════════════
    //  RANKING — atualização dinâmica
    // ══════════════════════════════════════════════════════════════════════
    private void atualizarEcrăRanking() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_BRANCO);
        painel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("🏆 TOP 10 — MELHORES PONTUAÇÕES", SwingConstants.CENTER);
        lblTitulo.setFont(FONTE_TITULO);
        lblTitulo.setForeground(COR_VERDE_DARK);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 15, 0));
        painel.add(lblTitulo, BorderLayout.NORTH);

        JPanel lista = new JPanel(new GridLayout(0, 1, 0, 5));
        lista.setBackground(COR_BRANCO);

        List<String[]> entradas = ranking.getEntradas();
        if (entradas.isEmpty()) {
            JLabel sem = new JLabel("Ainda não há resultados registados.", SwingConstants.CENTER);
            sem.setFont(FONTE_OPCAO);
            lista.add(sem);
        } else {
            String[] medalhas = {"🥇", "🥈", "🥉"};
            for (int i = 0; i < entradas.size(); i++) {
                String[] e = entradas.get(i);
                String icone = i < 3 ? medalhas[i] : (i + 1) + ".";
                JLabel linha = new JLabel(
                        String.format("  %s  %-25s %s pts", icone, e[0], e[1]),
                        SwingConstants.LEFT);
                linha.setFont(new Font("Monospaced", Font.PLAIN, 15));
                linha.setOpaque(true);
                linha.setBackground(i % 2 == 0 ? COR_CINZA : COR_BRANCO);
                linha.setBorder(new EmptyBorder(6, 10, 6, 10));
                lista.add(linha);
            }
        }
        painel.add(new JScrollPane(lista), BorderLayout.CENTER);

        JButton btnVoltar = criarBotao("⬅  Voltar", COR_BRANCO, COR_VERDE_DARK);
        btnVoltar.addActionListener(e -> mostrarEcrã("MENU"));
        painel.add(btnVoltar, BorderLayout.SOUTH);

        painelPrincipal.remove(getComponentByName("RANKING_PAINEL"));
        painel.setName("RANKING_PAINEL");
        painelPrincipal.add(painel, "RANKING");
    }

    // ══════════════════════════════════════════════════════════════════════
    //  UTILITÁRIOS
    // ══════════════════════════════════════════════════════════════════════

    /** Cria um botão estilizado. */
    private JButton criarBotao(String texto, Color corTexto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(corTexto);
        btn.setBackground(corFundo);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(280, 42));
        btn.setPreferredSize(new Dimension(260, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    /** Muda o ecrã visível. */
    private void mostrarEcrã(String nome) {
        cardLayout.show(painelPrincipal, nome);
    }

    /** Procura um componente por nome no painel principal. */
    private Component getComponentByName(String nome) {
        for (Component c : painelPrincipal.getComponents()) {
            if (nome.equals(c.getName())) return c;
        }
        return new JPanel(); // fallback vazio
    }

    /** Guarda uma nova pergunta no ficheiro CSV. */
    private void guardarPerguntaCSV(String enunciado, String[] opcoes, int correta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoCSV, true))) {
            bw.write(enunciado + "," + opcoes[0] + "," + opcoes[1] + ","
                    + opcoes[2] + "," + opcoes[3] + "," + correta);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao guardar: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  MAIN — ponto de entrada
    // ══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        // Boa prática: lançar o Swing na Event Dispatch Thread
        SwingUtilities.invokeLater(JanelaPrincipal::new);
    }
}