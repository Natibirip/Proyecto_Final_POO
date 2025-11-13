package modelos;

import java.io.Serializable;

/**
 * Clase que representa una enfermedad en el sistema.
 * Algunas enfermedades están bajo vigilancia epidemiológica.
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public class Enfermedad implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private String codigo;
    private String nombre;
    private boolean esVigilada; // Si está bajo vigilancia epidemiológica
    private String descripcion;
    private boolean activa; // Si está activa en el sistema
    
    /**
     * Constructor completo de Enfermedad
     * 
     * @param codigo Código único de la enfermedad (ej: "ENF001")
     * @param nombre Nombre de la enfermedad (ej: "COVID-19")
     * @param esVigilada Si está bajo vigilancia epidemiológica
     * @param descripcion Descripción de la enfermedad
     */
    public Enfermedad(String codigo, String nombre, boolean esVigilada, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.esVigilada = esVigilada;
        this.descripcion = descripcion;
        this.activa = true; // Por defecto está activa
    }
    
    /**
     * Constructor sin descripción
     * 
     * @param codigo Código único de la enfermedad
     * @param nombre Nombre de la enfermedad
     * @param esVigilada Si está bajo vigilancia
     */
    public Enfermedad(String codigo, String nombre, boolean esVigilada) {
        this(codigo, nombre, esVigilada, "");
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
    
    public boolean isEsVigilada() {
        return esVigilada;
    }
    
    public void setEsVigilada(boolean esVigilada) {
        this.esVigilada = esVigilada;
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
     * Marca la enfermedad como bajo vigilancia epidemiológica.
     * Las consultas con enfermedades vigiladas son visibles para todos los médicos.
     */
    public void marcarComoVigilada() {
        this.esVigilada = true;
    }
    
    /**
     * Quita la enfermedad de vigilancia epidemiológica
     */
    public void quitarVigilancia() {
        this.esVigilada = false;
    }
    
    /**
     * Desactiva la enfermedad del sistema
     */
    public void desactivar() {
        this.activa = false;
    }
    
    /**
     * Reactiva la enfermedad en el sistema
     */
    public void activar() {
        this.activa = true;
    }
    
    /**
     * Valida que los datos esenciales de la enfermedad estén completos
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
        
        Enfermedad that = (Enfermedad) obj;
        return codigo != null && codigo.equals(that.codigo);
    }
    
    @Override
    public int hashCode() {
        return codigo != null ? codigo.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(codigo).append("] ");
        sb.append(nombre);
        
        if (esVigilada) {
            sb.append(" ⚠ VIGILADA");
        }
        
        if (!activa) {
            sb.append(" (Inactiva)");
        }
        
        return sb.toString();
    }
    
    /**
     * Representación simple solo con el nombre
     * Útil para ComboBox
     * 
     * @return Nombre de la enfermedad
     */
    public String toStringSimple() {
        return nombre;
    }
    
    /**
     * Representación para ComboBox que incluye estado de vigilancia
     * 
     * @return Nombre con indicador de vigilancia
     */
    public String toStringConVigilancia() {
        return esVigilada ? nombre : nombre;
    }
}