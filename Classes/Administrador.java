/**
 * Classe Administrador - representa um administrador do sistema.
 * Herda de Usuario e tem permissões especiais (password de acesso).
 * Polimorfismo: getInfo() retorna informação diferente da classe Jogador.
 */
public class Administrador extends Usuario {

    // Atributos específicos do Administrador
    private String password;
    private String nivelAcesso;

    /**
     * Construtor do Administrador.
     * @param nome       Nome do administrador
     * @param email      Email do administrador
     * @param password   Password de acesso ao painel admin
     */
    public Administrador(String nome, String email, String password) {
        super(nome, email); // chama o construtor da superclasse
        this.password = password;
        this.nivelAcesso = "TOTAL";
    }

    // Getters e Setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    /**
     * Verifica se a password fornecida está correta.
     * @param tentativa Password introduzida pelo utilizador
     * @return true se a password estiver correta
     */
    public boolean verificarPassword(String tentativa) {
        return this.password.equals(tentativa);
    }

    /**
     * Implementação polimórfica do método getInfo().
     * Comportamento diferente do Jogador (polimorfismo).
     * @return Informações do administrador
     */
    @Override
    public String getInfo() {
        return "Administrador: " + getNome() + " | Acesso: " + nivelAcesso;
    }
}