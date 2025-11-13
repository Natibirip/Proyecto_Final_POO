package modelos;

import java.util.Date;

public class Secretaria extends Persona {

    private static final long serialVersionUID = 1L;

    private Usuario usuario; // Su cuenta de acceso al sistema

    // Constructor con usuario
    public Secretaria(String cedula, String nombre, String telefono,
                      Date fechaNacimiento, char sexo, Usuario usuario) {
        super(cedula, nombre, telefono, fechaNacimiento, sexo);
        this.usuario = usuario;
    }

    // Constructor sin usuario
    public Secretaria(String cedula, String nombre, String telefono,
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

    // Ejemplo de acción típica de la secretaria
    public void registrarCita(Paciente paciente, Medico medico, Date fecha, String hora) {
        // Lógica para registrar una cita
        System.out.println("Cita registrada para " + paciente.getNombre() +
                           " con el médico " + medico.getNombre() + " por la secretaria " + getNombre());
    }
}
