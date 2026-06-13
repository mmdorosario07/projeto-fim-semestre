import java.io.*;
import java.util.*;

/**
 * Classe Ranking - gere a persistência e ordenação dos melhores resultados.
 * Lê e escreve em ficheiro de texto (requisito de persistência de dados).
 */
public class Ranking {

    // Caminho do ficheiro de ranking
    private static final String FICHEIRO_RANKING = "ranking.txt";

    // Lista de entradas no ranking: nome -> pontuação
    private List<String[]> entradas;

    /**
     * Construtor do Ranking.
     * Carrega automaticamente o ranking do ficheiro.
     */
    public Ranking() {
        this.entradas = new ArrayList<>();
        carregarRanking();
    }

    /**
     * Carrega o ranking a partir do ficheiro de texto.
     * Usa try-catch para tratamento de exceções.
     */
    private void carregarRanking() {
        entradas.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_RANKING))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length == 2) {
                    entradas.add(new String[]{partes[0].trim(), partes[1].trim()});
                }
            }
        } catch (FileNotFoundException e) {
            // Ficheiro ainda não existe — será criado ao guardar
            System.out.println("Ranking ainda não existe. Será criado ao guardar.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar ranking: " + e.getMessage());
        }
    }

    /**
     * Adiciona um jogador ao ranking e guarda no ficheiro.
     * @param nome      Nome do jogador
     * @param pontuacao Pontuação obtida
     */
    public void adicionarEntrada(String nome, int pontuacao) {
        entradas.add(new String[]{nome, String.valueOf(pontuacao)});

        // Ordena por pontuação (descendente)
        entradas.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));

        // Mantém apenas o top 10
        if (entradas.size() > 10) {
            entradas = entradas.subList(0, 10);
        }

        guardarRanking();
    }

    /**
     * Guarda o ranking no ficheiro de texto.
     * Usa try-catch para tratamento de exceções.
     */
    private void guardarRanking() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FICHEIRO_RANKING))) {
            for (String[] entrada : entradas) {
                bw.write(entrada[0] + "," + entrada[1]);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao guardar ranking: " + e.getMessage());
        }
    }

    /**
     * Retorna a lista de entradas do ranking.
     * @return Lista de arrays [nome, pontuacao]
     */
    public List<String[]> getEntradas() {
        return entradas;
    }

    /**
     * Formata o ranking para exibição.
     * @return String formatada com o top 10
     */
    public String getRankingFormatado() {
        StringBuilder sb = new StringBuilder();
        sb.append("🏆 TOP 10 — MELHORES PONTUAÇÕES\n\n");
        if (entradas.isEmpty()) {
            sb.append("Ainda não há resultados registados.");
        } else {
            for (int i = 0; i < entradas.size(); i++) {
                String[] e = entradas.get(i);
                sb.append(String.format("%2d. %-20s %s pts\n", i + 1, e[0], e[1]));
            }
        }
        return sb.toString();
    }
}