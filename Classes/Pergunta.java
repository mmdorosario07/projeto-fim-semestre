/**
 * Classe Pergunta - representa uma pergunta de escolha múltipla do quiz.
 * Encapsula o enunciado, as 4 opções e o índice da resposta correta.
 */
public class Pergunta {

    // Atributos encapsulados
    private String enunciado;
    private String[] opcoes;     // 4 opções de resposta
    private int respostaCorreta; // índice (0-3) da opção correta

    /**
     * Construtor da Pergunta.
     * @param enunciado      Texto da pergunta
     * @param opcoes         Array com as 4 opções de resposta
     * @param respostaCorreta Índice da opção correta (0, 1, 2 ou 3)
     */
    public Pergunta(String enunciado, String[] opcoes, int respostaCorreta) {
        this.enunciado = enunciado;
        this.opcoes = opcoes;
        this.respostaCorreta = respostaCorreta;
    }

    // Getters e Setters
    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String[] getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(String[] opcoes) {
        this.opcoes = opcoes;
    }

    public int getRespostaCorreta() {
        return respostaCorreta;
    }

    public void setRespostaCorreta(int respostaCorreta) {
        this.respostaCorreta = respostaCorreta;
    }

    /**
     * Verifica se a resposta dada está correta.
     * @param indice Índice da opção escolhida pelo jogador
     * @return true se a resposta for correta
     */
    public boolean verificarResposta(int indice) {
        return indice == respostaCorreta;
    }

    /**
     * Retorna o texto da opção correta.
     * @return String com a opção correta
     */
    public String getTextoRespostaCorreta() {
        return opcoes[respostaCorreta];
    }

    @Override
    public String toString() {
        return "Pergunta{enunciado='" + enunciado + "'}";
    }
}