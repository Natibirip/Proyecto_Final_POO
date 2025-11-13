package modelos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Clase que representa una cita médica en el sistema.
 * Las citas son agendadas por la secretaria y realizadas por el médico.
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public class Cita implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private String id;
    private Paciente paciente;
    private Medico medico;
    private Date fecha;
    private String hora; // Formato: "09:00 AM"
    private EstadoCita estado;
    private boolean esNuevoPaciente; // Si es primera vez del paciente
    private Date fechaCreacion; // Cuándo se agendó la cita
    
    /**
     * Constructor completo de Cita
     * 
     * @param paciente Paciente que solicita la cita
     * @param medico Médico que atenderá la cita
     * @param fecha Fecha de la cita
     * @param hora Hora de la cita (formato: "09:00 AM")
     */
    public Cita(Paciente paciente, Medico medico, Date fecha, String hora) {
        this.id = generarId();
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = EstadoCita.PENDIENTE;
        this.esNuevoPaciente = !paciente.isPerfilCompleto();
        this.fechaCreacion = new Date();
    }
    
    // ========== GENERACIÓN DE ID ==========
    
    /**
     * Genera un ID único para la cita
     * 
     * @return ID único en formato "CITA-UUID"
     */
    private String generarId() {
        return "CITA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    public String getId() {
        return id;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    public Medico getMedico() {
        return medico;
    }
    
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String getHora() {
        return hora;
    }
    
    public void setHora(String hora) {
        this.hora = hora;
    }
    
    public EstadoCita getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }
    
    public boolean isEsNuevoPaciente() {
        return esNuevoPaciente;
    }
    
    public void setEsNuevoPaciente(boolean esNuevoPaciente) {
        this.esNuevoPaciente = esNuevoPaciente;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    // ========== MÉTODOS DE NEGOCIO ==========
    
    /**
     * Marca la cita como realizada
     * Se llama cuando el médico completa la consulta
     */
    public void marcarComoRealizada() {
        if (estado == EstadoCita.PENDIENTE || estado == EstadoCita.MODIFICADA) {
            this.estado = EstadoCita.REALIZADA;
        }
    }
    
    /**
     * Cancela la cita
     * Solo se puede cancelar si está pendiente
     * 
     * @return true si se canceló exitosamente, false si no se pudo cancelar
     */
    public boolean cancelar() {
        if (puedeModificarse()) {
            this.estado = EstadoCita.CANCELADA;
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si la cita puede ser modificada o cancelada
     * Solo se puede modificar antes del día de la cita y si está pendiente
     * 
     * @return true si se puede modificar, false en caso contrario
     */
    public boolean puedeModificarse() {
        // No se puede modificar si ya fue realizada o cancelada
        if (estado.esFinal()) {
            return false;
        }
        
        // No se puede modificar si la fecha ya pasó
        Date hoy = new Date();
        return !fecha.before(hoy);
    }
    
    /**
     * Modifica la fecha y hora de la cita
     * Solo funciona si la cita puede modificarse
     * 
     * @param nuevaFecha Nueva fecha de la cita
     * @param nuevaHora Nueva hora de la cita
     * @return true si se modificó exitosamente, false en caso contrario
     */
    public boolean modificar(Date nuevaFecha, String nuevaHora) {
        if (!puedeModificarse()) {
            return false;
        }
        
        this.fecha = nuevaFecha;
        this.hora = nuevaHora;
        this.estado = EstadoCita.MODIFICADA;
        return true;
    }
    
    /**
     * Verifica si la cita es para hoy
     * 
     * @return true si la cita es hoy, false en caso contrario
     */
    public boolean esHoy() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaCita = sdf.format(fecha);
        String fechaHoy = sdf.format(new Date());
        return fechaCita.equals(fechaHoy);
    }
    
    /**
     * Verifica si la cita ya pasó
     * 
     * @return true si la fecha ya pasó, false en caso contrario
     */
    public boolean yaPaso() {
        Date hoy = new Date();
        return fecha.before(hoy);
    }
    
    /**
     * Obtiene el nombre del paciente de manera rápida
     * 
     * @return Nombre del paciente
     */
    public String getNombrePaciente() {
        return paciente != null ? paciente.getNombre() : "Sin paciente";
    }
    
    /**
     * Obtiene el nombre del médico con título
     * 
     * @return Nombre del médico con título
     */
    public String getNombreMedico() {
        return medico != null ? medico.getNombreConTitulo() : "Sin médico";
    }
    
    /**
     * Obtiene la fecha formateada para mostrar
     * 
     * @return Fecha en formato dd/MM/yyyy
     */
    public String getFechaFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
    }
    
    /**
     * Obtiene fecha y hora juntas formateadas
     * 
     * @return String con fecha y hora
     */
    public String getFechaHoraFormateada() {
        return getFechaFormateada() + " - " + hora;
    }
    
    // ========== MÉTODOS SOBRESCRITOS ==========
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Cita cita = (Cita) obj;
        return id != null && id.equals(cita.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - Paciente: %s | Médico: %s | %s %s | Estado: %s",
                id,
                getFechaFormateada(),
                getNombrePaciente(),
                getNombreMedico(),
                hora,
                esNuevoPaciente ? "⚠ NUEVO" : "",
                estado.getDescripcion());
    }
    
    /**
     * Representación compacta para mostrar en tablas
     * 
     * @return String formateado para JTable
     */
    public String toStringCompacto() {
        return String.format("%s | %s | %s | %s",
                hora,
                getNombrePaciente(),
                estado.getDescripcion(),
                esNuevoPaciente ? "Perfil Básico" : "Perfil Completo");
    }
}