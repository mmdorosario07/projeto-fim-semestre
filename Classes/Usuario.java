/**
 * Classe abstrata Usuario - representa um utilizador genérico do sistema.
 * Serve de base para Jogador e Administrador (hierarquia de herança).
 */
public abstract class Usuario {

    // Atributos encapsulados
    private String nome;
    private String email;

    /**
     * Construtor da classe Usuario.
     * @param nome  Nome do utilizador
     * @param email Email do utilizador
     */
    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Método abstrato polimórfico - cada subclasse implementa o seu próprio comportamento.
     * @return String com informações do utilizador
     */
    public abstract String getInfo();

    @Override
    public String toString() {
        return "Usuario{nome='" + nome + "', email='" + email + "'}";
    }
}