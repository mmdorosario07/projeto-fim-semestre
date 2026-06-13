import java.io.*;
import java.util.*;

/**
 * Classe Quiz - gere a lógica do jogo.
 * Carrega perguntas do CSV, controla o índice atual e calcula a pontuação.
 */
public class Quiz {

    // Constantes do jogo
    public static final int PONTOS_POR_ACERTO = 3;
    public static final int TOTAL_PERGUNTAS   = 20;
    public static final int TEMPO_POR_PERGUNTA = 60; // segundos

    // Atributos encapsulados
    private List<Pergunta> perguntas;
    private int indicePerguntaAtual;
    private Jogador jogador;

    /**
     * Construtor do Quiz.
     * @param jogador O jogador que está a participar
     */
    public Quiz(Jogador jogador) {
        this.jogador = jogador;
        this.perguntas = new ArrayList<>();
        this.indicePerguntaAtual = 0;
    }

    // Getters e Setters
    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public int getIndicePerguntaAtual() {
        return indicePerguntaAtual;
    }

    public Jogador getJogador() {
        return jogador;
    }

    /**
     * Carrega as perguntas a partir de um ficheiro CSV.
     * Formato: pergunta,opcao1,opcao2,opcao3,opcao4,correta
     * Utiliza try-catch para tratamento de exceções (requisito obrigatório).
     * @param caminho Caminho para o ficheiro CSV
     */
    public void carregarPerguntasCSV(String caminho) {
        perguntas.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                // Ignora o cabeçalho
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                // Divide a linha por vírgula
                String[] partes = linha.split(",");
                if (partes.length == 6) {
                    String enunciado = partes[0].trim();
                    String[] opcoes = {
                            partes[1].trim(),
                            partes[2].trim(),
                            partes[3].trim(),
                            partes[4].trim()
                    };
                    int correta = Integer.parseInt(partes[5].trim());
                    perguntas.add(new Pergunta(enunciado, opcoes, correta));
                }
            }

            // Baralha as perguntas para ordem aleatória
            Collections.shuffle(perguntas);

        } catch (FileNotFoundException e) {
            System.err.println("Erro: ficheiro de perguntas não encontrado — " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato no CSV: " + e.getMessage());
        }
    }

    /**
     * Retorna a pergunta atual.
     * @return Pergunta atual ou null se o quiz terminou
     */
    public Pergunta getPerguntaAtual() {
        if (indicePerguntaAtual < perguntas.size()) {
            return perguntas.get(indicePerguntaAtual);
        }
        return null;
    }

    /**
     * Processa a resposta do jogador.
     * @param indice Índice da opção escolhida
     * @return true se a resposta for correta
     */
    public boolean responder(int indice) {
        Pergunta pergunta = getPerguntaAtual();
        if (pergunta != null && pergunta.verificarResposta(indice)) {
            jogador.adicionarPontos(PONTOS_POR_ACERTO);
            return true;
        }
        return false;
    }

    /**
     * Avança para a próxima pergunta.
     */
    public void avancarPergunta() {
        indicePerguntaAtual++;
    }

    /**
     * Verifica se o quiz terminou.
     * @return true se todas as perguntas foram respondidas
     */
    public boolean terminou() {
        return indicePerguntaAtual >= TOTAL_PERGUNTAS || indicePerguntaAtual >= perguntas.size();
    }

    /**
     * Reinicia o quiz para um novo jogo.
     */
    public void reiniciar() {
        indicePerguntaAtual = 0;
        jogador.reiniciarPontuacao();
        Collections.shuffle(perguntas);
    }

    /**
     * Retorna o número da pergunta atual (começa em 1).
     * @return número da pergunta atual
     */
    public int getNumeroPerguntaAtual() {
        return indicePerguntaAtual + 1;
    }
}