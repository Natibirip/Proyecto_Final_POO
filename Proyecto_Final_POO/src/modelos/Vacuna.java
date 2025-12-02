package modelos;

import java.io.Serializable;

/**
 * Clase que representa una vacuna disponible en la clínica.
 * Las vacunas son predefinidas por la administración de la clínica.
 * * @author Equipo de Desarrollo
 * @version 1.1
 */
public class Vacuna implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private String codigo;
    private String nombre;
    private String descripcion;
    private String lote; // <--- NUEVO ATRIBUTO
    private boolean activa; 
    
    /**
     * Constructor completo de Vacuna (Actualizado con Lote)
     * * @param codigo Código único
     * @param nombre Nombre de la vacuna
     * @param descripcion Descripción
     * @param lote Número de lote de fabricación
     */
    public Vacuna(String codigo, String nombre, String descripcion, String lote) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.lote = lote;
        this.activa = true;
    }

    /**
     * Constructor de compatibilidad (Mantiene el código anterior funcionando)
     * Asigna un lote por defecto.
     */
    public Vacuna(String codigo, String nombre, String descripcion) {
        this(codigo, nombre, descripcion, "LOTE-STD"); // Lote por defecto
    }
    
    /**
     * Constructor simplificado
     */
    public Vacuna(String codigo, String nombre) {
        this(codigo, nombre, "", "LOTE-STD");
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

    // --- NUEVOS MÉTODOS PARA LOTE ---
    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }
    // --------------------------------
    
    public boolean isActiva() {
        return activa;
    }
    
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
    
    // ========== MÉTODOS DE NEGOCIO ==========
    
    public void desactivar() {
        this.activa = false;
    }
    
    public void activar() {
        this.activa = true;
    }
    
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
        return String.format("[%s] %s (Lote: %s) %s", 
                codigo, 
                nombre,
                lote,
                activa ? "" : "(Inactiva)");
    }
    
    public String toStringSimple() {
        return nombre + " - " + lote;
    }
}