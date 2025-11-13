package modelos;

import java.io.Serializable;

/**
 * Representa un usuario del sistema.
 * Puede estar asociado a una Persona, o ser un usuario "genérico" sin persona asignada.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private RolUsuario rol;  // ADMINISTRADOR, SECRETARIA, etc.
    private Persona personaAsociada; // Puede ser null

    // ===== Constructor completo =====
    public Usuario(String username, String password, RolUsuario rol, Persona personaAsociada) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.personaAsociada = personaAsociada;
    }

    // ===== Constructor sin persona asociada =====
    public Usuario(String username, String password, RolUsuario rol) {
        this(username, password, rol, null); // personaAsociada será null
    }

    // ===== Getters y Setters =====
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public Persona getPersonaAsociada() {
        return personaAsociada;
    }

    public void setPersonaAsociada(Persona personaAsociada) {
        this.personaAsociada = personaAsociada;
    }

    // ===== Método auxiliar =====
    public boolean tienePersonaAsociada() {
        return personaAsociada != null;
    }

    @Override
    public String toString() {
        return "Usuario{" +
               "username='" + username + '\'' +
               ", rol=" + rol +
               ", personaAsociada=" + (personaAsociada != null ? personaAsociada.getNombre() : "Ninguna") +
               '}';
    }
}
