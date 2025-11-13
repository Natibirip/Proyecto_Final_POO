package modelos;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa a un médico en el sistema de la clínica.
 * Hereda de Persona e incluye información profesional adicional.
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public class Medico extends Persona implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos específicos del médico
    private String especialidad; // ÚNICA especialidad por médico
    private int citasPorDia; // Cupo máximo de citas que acepta por día
    private String horarioAtencion; // Ej: "8:00 AM - 5:00 PM"
    private boolean activo; // Si está activo en la clínica
    
    /**
     * Constructor completo para médico
     * 
     * @param cedula Cédula de identidad del médico
     * @param nombre Nombre completo del médico
     * @param telefono Teléfono de contacto
     * @param fechaNacimiento Fecha de nacimiento
     * @param sexo Sexo del médico ('M' o 'F')
     * @param especialidad Especialidad médica (única)
     */
    public Medico(String cedula, String nombre, String telefono, 
                  Date fechaNacimiento, char sexo, String especialidad) {
        super(cedula, nombre, telefono, fechaNacimiento, sexo);
        this.especialidad = especialidad;
        this.citasPorDia = 10; // Valor por defecto
        this.horarioAtencion = "8:00 AM - 5:00 PM"; // Valor por defecto
        this.activo = true; // Por defecto está activo
    }
    
    /**
     * Constructor simplificado (sin fecha de nacimiento)
     * Útil para registro rápido
     * 
     * @param cedula Cédula del médico
     * @param nombre Nombre completo
     * @param telefono Teléfono de contacto
     * @param especialidad Especialidad médica
     */
    public Medico(String cedula, String nombre, String telefono, String especialidad) {
        super(cedula, nombre, telefono, new Date(), 'M'); // Fecha dummy, sexo dummy
        this.especialidad = especialidad;
        this.citasPorDia = 10;
        this.horarioAtencion = "8:00 AM - 5:00 PM";
        this.activo = true;
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    public String getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    public int getCitasPorDia() {
        return citasPorDia;
    }
    
    /**
     * Establece el cupo de citas por día
     * 
     * @param citasPorDia Número de citas (debe ser mayor a 0)
     */
    public void setCitasPorDia(int citasPorDia) {
        if (citasPorDia > 0 && citasPorDia <= 50) { // Máximo razonable: 50 citas/día
            this.citasPorDia = citasPorDia;
        } else {
            throw new IllegalArgumentException(
                "El cupo de citas debe estar entre 1 y 50");
        }
    }
    
    public String getHorarioAtencion() {
        return horarioAtencion;
    }
    
    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    // ========== MÉTODOS DE NEGOCIO ==========
    
    /**
     * Desactiva al médico en el sistema
     * Los médicos desactivados no pueden recibir nuevas citas
     */
    public void desactivar() {
        this.activo = false;
    }
    
    /**
     * Reactiva al médico en el sistema
     */
    public void activar() {
        this.activo = true;
    }
    
    /**
     * Verifica si el médico puede aceptar más citas en un día
     * 
     * @param citasActuales Cantidad de citas ya agendadas para ese día
     * @return true si puede aceptar más citas, false en caso contrario
     */
    public boolean puedeAceptarCitas(int citasActuales) {
        return citasActuales < citasPorDia;
    }
    
    /**
     * Calcula cuántas citas disponibles quedan para un día
     * 
     * @param citasActuales Cantidad de citas ya agendadas
     * @return Número de citas disponibles
     */
    public int getCitasDisponibles(int citasActuales) {
        int disponibles = citasPorDia - citasActuales;
        return disponibles > 0 ? disponibles : 0;
    }
    
    /**
     * Obtiene el nombre del médico con su título
     * 
     * @return Nombre formateado (ej: "Dr. Juan Pérez")
     */
    public String getNombreConTitulo() {
        String titulo = getSexo() == 'M' ? "Dr." : "Dra.";
        return titulo + " " + getNombre();
    }
    
    /**
     * Valida que la especialidad no esté vacía
     * 
     * @return true si la especialidad es válida, false en caso contrario
     */
    public boolean tieneEspecialidadValida() {
        return especialidad != null && !especialidad.trim().isEmpty();
    }
    
    // ========== MÉTODOS SOBRESCRITOS ==========
    
    @Override
    public String toString() {
        return String.format("%s | Especialidad: %s | Cupo: %d citas/día | Estado: %s",
                getNombreConTitulo(), 
                especialidad, 
                citasPorDia,
                activo ? "Activo" : "Inactivo");
    }
    
    /**
     * Representación compacta para mostrar en listas
     * 
     * @return String formateado para JList o JComboBox
     */
    public String toStringCompacto() {
        return String.format("%s - %s (Cupo: %d/día)", 
                getNombreConTitulo(), 
                especialidad, 
                citasPorDia);
    }
}