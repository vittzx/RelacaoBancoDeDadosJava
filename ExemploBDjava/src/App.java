import DAO.UsuarioDAO;
import entity.Usuario;

public class App {
    public static void main(String[] args) throws Exception {
        Usuario u = new Usuario();
        
        u.setNome("Alexandre");
        u.setLogin("aleMrod");
        u.setSenha("23456");
        u.setEmail("alexandre@gmail.com");

        new UsuarioDAO().cadastrarUsuario(u);
    }
}
