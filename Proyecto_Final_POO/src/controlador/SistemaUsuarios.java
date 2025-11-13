package controlador;

import modelos.*;
import java.util.ArrayList;

public class SistemaUsuarios {

    private ArrayList<Usuario> usuarios;

    public SistemaUsuarios() {
        usuarios = new ArrayList<>();
        // Se podría crear un usuario inicial "genérico" sin persona asociada
        usuarios.add(new Usuario("admin", "1234", RolUsuario.ADMINISTRADOR));
    }

    /**
     * Permite autenticar a un usuario por username y password.
     * @return Usuario si coincide, null si no existe.
     */
    public Usuario login(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Agrega un usuario al sistema.
     */
    public void agregarUsuario(Usuario u) {
        usuarios.add(u);
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
}
