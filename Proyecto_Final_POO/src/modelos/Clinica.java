package modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa la configuración de la clínica.
 * Contiene las vacunas y enfermedades que la clínica controla.
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public class Clinica implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private String nombre;
    private String direccion;
    private String telefono;
    private List<Vacuna> vacunasDisponibles;
    private List<Enfermedad> enfermedadesVigiladas;
    private Date fechaCreacion;
    
    /**
     * Constructor completo de Clínica
     * 
     * @param nombre Nombre de la clínica
     * @param direccion Dirección física de la clínica
     * @param telefono Teléfono de contacto
     */
    public Clinica(String nombre, String direccion, String telefono) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.vacunasDisponibles = new ArrayList<>();
        this.enfermedadesVigiladas = new ArrayList<>();
        this.fechaCreacion = new Date();
    }
    
    /**
     * Constructor simplificado solo con nombre
     * 
     * @param nombre Nombre de la clínica
     */
    public Clinica(String nombre) {
        this(nombre, "", "");
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public List<Vacuna> getVacunasDisponibles() {
        return new ArrayList<>(vacunasDisponibles); // Copia defensiva
    }
    
    public List<Enfermedad> getEnfermedadesVigiladas() {
        return new ArrayList<>(enfermedadesVigiladas); // Copia defensiva
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    // ========== MÉTODOS DE GESTIÓN DE VACUNAS ==========
    
    /**
     * Agrega una nueva vacuna a la clínica
     * 
     * @param vacuna Vacuna a agregar
     * @return true si se agregó exitosamente, false si ya existe
     */
    public boolean agregarVacuna(Vacuna vacuna) {
        if (vacuna == null || !vacuna.esValida()) {
            return false;
        }
        
        // Verificar que no exista ya
        if (buscarVacuna(vacuna.getCodigo()) != null) {
            return false;
        }
        
        return vacunasDisponibles.add(vacuna);
    }
    
    /**
     * Elimina una vacuna de la clínica por su código
     * 
     * @param codigo Código de la vacuna a eliminar
     * @return true si se eliminó, false si no se encontró
     */
    public boolean eliminarVacuna(String codigo) {
        Vacuna vacuna = buscarVacuna(codigo);
        if (vacuna != null) {
            return vacunasDisponibles.remove(vacuna);
        }
        return false;
    }
    
    /**
     * Busca una vacuna por su código
     * 
     * @param codigo Código de la vacuna a buscar
     * @return Vacuna encontrada o null si no existe
     */
    public Vacuna buscarVacuna(String codigo) {
        for (Vacuna v : vacunasDisponibles) {
            if (v.getCodigo().equals(codigo)) {
                return v;
            }
        }
        return null;
    }
    
    /**
     * Obtiene solo las vacunas activas
     * 
     * @return Lista de vacunas activas
     */
    public List<Vacuna> getVacunasActivas() {
        List<Vacuna> activas = new ArrayList<>();
        for (Vacuna v : vacunasDisponibles) {
            if (v.isActiva()) {
                activas.add(v);
            }
        }
        return activas;
    }
    
    /**
     * Obtiene la cantidad total de vacunas disponibles
     * 
     * @return Número de vacunas
     */
    public int getCantidadVacunas() {
        return vacunasDisponibles.size();
    }
    
    // ========== MÉTODOS DE GESTIÓN DE ENFERMEDADES ==========
    
    /**
     * Agrega una nueva enfermedad al control de la clínica
     * 
     * @param enfermedad Enfermedad a agregar
     * @return true si se agregó exitosamente, false si ya existe
     */
    public boolean agregarEnfermedad(Enfermedad enfermedad) {
        if (enfermedad == null || !enfermedad.esValida()) {
            return false;
        }
        
        // Verificar que no exista ya
        if (buscarEnfermedad(enfermedad.getCodigo()) != null) {
            return false;
        }
        
        return enfermedadesVigiladas.add(enfermedad);
    }
    
    /**
     * Elimina una enfermedad del control por su código
     * 
     * @param codigo Código de la enfermedad a eliminar
     * @return true si se eliminó, false si no se encontró
     */
    public boolean eliminarEnfermedad(String codigo) {
        Enfermedad enfermedad = buscarEnfermedad(codigo);
        if (enfermedad != null) {
            return enfermedadesVigiladas.remove(enfermedad);
        }
        return false;
    }
    
    /**
     * Busca una enfermedad por su código
     * 
     * @param codigo Código de la enfermedad a buscar
     * @return Enfermedad encontrada o null si no existe
     */
    public Enfermedad buscarEnfermedad(String codigo) {
        for (Enfermedad e : enfermedadesVigiladas) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        return null;
    }
    
    /**
     * Obtiene solo las enfermedades que están marcadas como vigiladas
     * 
     * @return Lista de enfermedades bajo vigilancia
     */
    public List<Enfermedad> getEnfermedadesBajoVigilancia() {
        List<Enfermedad> vigiladas = new ArrayList<>();
        for (Enfermedad e : enfermedadesVigiladas) {
            if (e.isEsVigilada() && e.isActiva()) {
                vigiladas.add(e);
            }
        }
        return vigiladas;
    }
    
    /**
     * Obtiene solo las enfermedades activas
     * 
     * @return Lista de enfermedades activas
     */
    public List<Enfermedad> getEnfermedadesActivas() {
        List<Enfermedad> activas = new ArrayList<>();
        for (Enfermedad e : enfermedadesVigiladas) {
            if (e.isActiva()) {
                activas.add(e);
            }
        }
        return activas;
    }
    
    /**
     * Obtiene la cantidad total de enfermedades controladas
     * 
     * @return Número de enfermedades
     */
    public int getCantidadEnfermedades() {
        return enfermedadesVigiladas.size();
    }
    
    /**
     * Cuenta cuántas enfermedades están bajo vigilancia
     * 
     * @return Número de enfermedades vigiladas
     */
    public int getCantidadEnfermedadesVigiladas() {
        int count = 0;
        for (Enfermedad e : enfermedadesVigiladas) {
            if (e.isEsVigilada()) {
                count++;
            }
        }
        return count;
    }
    
    // ========== MÉTODOS SOBRESCRITOS ==========
    
    @Override
    public String toString() {
        return String.format("Clínica: %s | Vacunas: %d | Enfermedades Vigiladas: %d",
                nombre,
                getCantidadVacunas(),
                getCantidadEnfermedadesVigiladas());
    }
}