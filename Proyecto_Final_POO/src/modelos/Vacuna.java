package modelos;

import java.io.Serializable;

/**
 * Clase que representa una vacuna disponible en la clínica.
 * Las vacunas son predefinidas por la administración de la clínica.
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public class Vacuna implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private String codigo;
    private String nombre;
    private String descripcion;
    private boolean activa; // Si está disponible actualmente en la clínica
    
    /**
     * Constructor completo de Vacuna
     * 
     * @param codigo Código único de la vacuna (ej: "VAC001")
     * @param nombre Nombre de la vacuna (ej: "COVID-19")
     * @param descripcion Descripción detallada de la vacuna
     */
    public Vacuna(String codigo, String nombre, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activa = true; // Por defecto está activa
    }
    
    /**
     * Constructor simplificado sin descripción
     * 
     * @param codigo Código único de la vacuna
     * @param nombre Nombre de la vacuna
     */
    public Vacuna(String codigo, String nombre) {
        this(codigo, nombre, "");
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public boolean isActiva() {
        return activa;
    }
    
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
    
    // ========== MÉTODOS DE NEGOCIO ==========
    
    /**
     * Desactiva la vacuna (ya no estará disponible para aplicar)
     */
    public void desactivar() {
        this.activa = false;
    }
    
    /**
     * Reactiva la vacuna
     */
    public void activar() {
        this.activa = true;
    }
    
    /**
     * Valida que los datos esenciales de la vacuna estén completos
     * 
     * @return true si es válida, false en caso contrario
     */
    public boolean esValida() {
        return codigo != null && !codigo.trim().isEmpty() &&
               nombre != null && !nombre.trim().isEmpty();
    }
    
    // ========== MÉTODOS SOBRESCRITOS ==========
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Vacuna vacuna = (Vacuna) obj;
        return codigo != null && codigo.equals(vacuna.codigo);
    }
    
    @Override
    public int hashCode() {
        return codigo != null ? codigo.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s %s", 
                codigo, 
                nombre, 
                activa ? "(Activa)" : "(Inactiva)");
    }
    
    /**
     * Representación compacta solo con el nombre (útil para checkboxes)
     * 
     * @return Nombre de la vacuna
     */
    public String toStringSimple() {
        return nombre;
    }
}