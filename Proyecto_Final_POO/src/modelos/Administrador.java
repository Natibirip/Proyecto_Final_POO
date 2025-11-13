package modelos;

import java.util.Date;

public class Administrador extends Persona {

    private static final long serialVersionUID = 1L;

    private Usuario usuario; // Su cuenta de acceso al sistema

    // Constructor original
    public Administrador(String cedula, String nombre, String telefono,
                         Date fechaNacimiento, char sexo, Usuario usuario) {
        super(cedula, nombre, telefono, fechaNacimiento, sexo);
        this.usuario = usuario;
    }

    // Constructor adicional **sin usuario**
    public Administrador(String cedula, String nombre, String telefono,
                         Date fechaNacimiento, char sexo) {
        super(cedula, nombre, telefono, fechaNacimiento, sexo);
        this.usuario = null; // Se puede asignar después
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Ejemplo de acción típica del administrador
    public void crearUsuario(Usuario nuevoUsuario) {
        System.out.println("Usuario " + nuevoUsuario.getUsername() + " creado por el administrador " + getNombre());
    }
}
