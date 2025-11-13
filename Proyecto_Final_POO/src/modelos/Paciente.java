package modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que representa a un paciente en el sistema de la clínica.
 * Hereda de Persona e incluye información médica adicional.
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public class Paciente extends Persona implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos específicos del paciente
    private List<Consulta> historialConsultas;
    private Map<Vacuna, Boolean> estadoVacunacion; // Vacuna -> está aplicada?
    private Date fechaRegistro;
    private boolean perfilCompleto;
    
    /**
     * Constructor completo para paciente
     * 
     * @param cedula Cédula de identidad del paciente
     * @param nombre Nombre completo del paciente
     * @param telefono Teléfono de contacto
     * @param fechaNacimiento Fecha de nacimiento
     * @param sexo Sexo del paciente ('M' o 'F')
     */
    public Paciente(String cedula, String nombre, String telefono, 
                    Date fechaNacimiento, char sexo) {
        super(cedula, nombre, telefono, fechaNacimiento, sexo);
        this.historialConsultas = new ArrayList<>();
        this.estadoVacunacion = new HashMap<>();
        this.fechaRegistro = new Date(); // Fecha actual
        this.perfilCompleto = false; // Inicialmente perfil básico
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    public List<Consulta> getHistorialConsultas() {
        return new ArrayList<>(historialConsultas); // Retornar copia defensiva
    }
    
    public Map<Vacuna, Boolean> getEstadoVacunacion() {
        return new HashMap<>(estadoVacunacion); // Retornar copia defensiva
    }
    
    public Date getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public boolean isPerfilCompleto() {
        return perfilCompleto;
    }
    
    public void setPerfilCompleto(boolean perfilCompleto) {
        this.perfilCompleto = perfilCompleto;
    }
    
    // ========== MÉTODOS DE NEGOCIO ==========
    
    /**
     * Agrega una consulta al historial del paciente
     * 
     * @param consulta Consulta a agregar
     * @return true si se agregó exitosamente, false en caso contrario
     */
    public boolean agregarConsulta(Consulta consulta) {
        if (consulta == null) {
            return false;
        }
        
        // Verificar que la consulta sea de este paciente
        if (!this.equals(consulta.getPaciente())) {
            return false;
        }
        
        // Agregar al historial
        historialConsultas.add(consulta);
        return true;
    }
    
    /**
     * Actualiza el estado de vacunación de una vacuna específica
     * 
     * @param vacuna Vacuna a actualizar
     * @param aplicada true si la vacuna fue aplicada, false en caso contrario
     */
    public void actualizarVacuna(Vacuna vacuna, boolean aplicada) {
        if (vacuna != null) {
            estadoVacunacion.put(vacuna, aplicada);
        }
    }
    
    /**
     * Verifica si el paciente tiene aplicada una vacuna específica
     * 
     * @param vacuna Vacuna a verificar
     * @return true si la vacuna está aplicada, false en caso contrario
     */
    public boolean tieneVacuna(Vacuna vacuna) {
        return estadoVacunacion.getOrDefault(vacuna, false);
    }
    
    /**
     * Obtiene el número total de consultas del paciente
     * 
     * @return Cantidad de consultas en el historial
     */
    public int getCantidadConsultas() {
        return historialConsultas.size();
    }
    
    /**
     * Obtiene la última consulta del paciente
     * 
     * @return Última consulta o null si no hay consultas
     */
 /*   public Consulta getUltimaConsulta() {
        if (historialConsultas.isEmpty()) {
            return null;
        }
        return historialConsultas.get(historialConsultas.size() - 1);
    }
   */ 
    /**
     * Calcula el porcentaje de cobertura de vacunación
     * 
     * @param vacunasClinica Lista de todas las vacunas disponibles en la clínica
     * @return Porcentaje de cobertura (0.0 a 100.0)
     */
    public double calcularCoberturVacunacion(List<Vacuna> vacunasClinica) {
        if (vacunasClinica == null || vacunasClinica.isEmpty()) {
            return 0.0;
        }
        
        int vacunasAplicadas = 0;
        for (Vacuna v : vacunasClinica) {
            if (tieneVacuna(v)) {
                vacunasAplicadas++;
            }
        }
        
        return (vacunasAplicadas * 100.0) / vacunasClinica.size();
    }
    
    /**
     * Marca el perfil como completo
     * Este método se llama cuando el médico completa todos los datos del paciente
     */
    public void completarPerfil() {
        this.perfilCompleto = true;
    }
    
    /**
     * Inicializa el estado de vacunación con todas las vacunas de la clínica
     * 
     * @param vacunasClinica Lista de vacunas disponibles en la clínica
     */
    public void inicializarEstadoVacunacion(List<Vacuna> vacunasClinica) {
        if (vacunasClinica != null) {
            for (Vacuna v : vacunasClinica) {
                if (!estadoVacunacion.containsKey(v)) {
                    estadoVacunacion.put(v, false);
                }
            }
        }
    }
    
    // ========== MÉTODOS SOBRESCRITOS ==========
    
    @Override
    public String toString() {
        return String.format("Paciente: %s | Cédula: %s | Edad: %d años | " +
                           "Perfil: %s | Consultas: %d",
                getNombre(), getCedula(), getEdad(),
                perfilCompleto ? "Completo" : "Básico",
                getCantidadConsultas());
    }
}