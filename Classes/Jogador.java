/**
 * Classe Jogador - representa um jogador do quiz.
 * Herda de Usuario e adiciona pontuação e histórico de jogos.
 */
public class Jogador extends Usuario {

    // Atributos específicos do Jogador
    private int pontuacao;
    private int totalJogos;

    /**
     * Construtor do Jogador.
     * @param nome  Nome do jogador
     * @param email Email do jogador
     */
    public Jogador(String nome, String email) {
        super(nome, email); // chama o construtor da superclasse
        this.pontuacao = 0;
        this.totalJogos = 0;
    }

    // Getters e Setters
    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getTotalJogos() {
        return totalJogos;
    }

    public void setTotalJogos(int totalJogos) {
        this.totalJogos = totalJogos;
    }

    /**
     * Adiciona pontos à pontuação atual do jogador.
     * @param pontos Pontos a adicionar
     */
    public void adicionarPontos(int pontos) {
        this.pontuacao += pontos;
    }

    /**
     * Incrementa o número total de jogos.
     */
    public void incrementarJogos() {
        this.totalJogos++;
    }

    /**
     * Reinicia a pontuação para um novo jogo.
     */
    public void reiniciarPontuacao() {
        this.pontuacao = 0;
    }

    /**
     * Implementação polimórfica do método getInfo().
     * @return Informações do jogador
     */
    @Override
    public String getInfo() {
        return "Jogador: " + getNome() + " | Pontuação: " + pontuacao + " | Jogos: " + totalJogos;
    }

    @Override
    public String toString() {
        return getNome() + "," + pontuacao;
    }
}