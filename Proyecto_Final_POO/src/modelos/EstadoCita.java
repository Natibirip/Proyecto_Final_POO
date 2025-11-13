package modelos;

/**
 * Enumeración que representa los posibles estados de una cita médica
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public enum EstadoCita {
    
    /**
     * Cita agendada pero no realizada aún
     */
    PENDIENTE("Pendiente"),
    
    /**
     * Cita realizada y consulta registrada
     */
    REALIZADA("Realizada"),
    
    /**
     * Cita cancelada por el paciente o la clínica
     */
    CANCELADA("Cancelada"),
    
    /**
     * Cita que fue modificada (fecha u hora cambiada)
     */
    MODIFICADA("Modificada");
    
    private final String descripcion;
    
    /**
     * Constructor de la enumeración
     * 
     * @param descripcion Descripción legible del estado
     */
    EstadoCita(String descripcion) {
        this.descripcion = descripcion;
    }
    
    /**
     * Obtiene la descripción del estado
     * 
     * @return Descripción del estado
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
    
    /**
     * Verifica si el estado permite modificaciones
     * 
     * @return true si se puede modificar, false en caso contrario
     */
    public boolean permiteModificacion() {
        return this == PENDIENTE || this == MODIFICADA;
    }
    
    /**
     * Verifica si el estado es final (no se puede cambiar)
     * 
     * @return true si es un estado final, false en caso contrario
     */
    public boolean esFinal() {
        return this == REALIZADA || this == CANCELADA;
    }
}