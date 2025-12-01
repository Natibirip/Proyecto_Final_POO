package modelos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Cita implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private String id;
    
    // --- CAMBIO 1: DATOS ADMINISTRATIVOS (TEXTO) ---
    private String nombrePersona;
    private String cedulaPersona;
    
    // --- CAMBIO 2: PACIENTE REAL (OPCIONAL/LINK) ---
    private Paciente pacienteAsociado; // Inicia en null
    
    private Medico medico;
    private Date fecha;
    private String hora; // Formato: "09:00 AM" (O puedes usar Date si prefieres)
    private EstadoCita estado; // Asegúrate de tener este Enum o cambiarlo a String
    private Date fechaCreacion; 
    
    /**
     * Constructor MODIFICADO:
     * Ya no pide un objeto Paciente, pide nombre y cédula (Strings).
     */
    public Cita(String nombrePersona, String cedulaPersona, Medico medico, Date fecha, String hora) {
        this.id = generarId();
        
        // Guardamos los datos administrativos
        this.nombrePersona = nombrePersona;
        this.cedulaPersona = cedulaPersona;
        
        // El paciente real empieza desconectado
        this.pacienteAsociado = null; 
        
        this.medico = medico;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = EstadoCita.PENDIENTE; // Asumo que tienes el Enum creado
        this.fechaCreacion = new Date();
    }
    
    // ========== GENERACIÓN DE ID ==========
    private String generarId() {
        return "CITA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    public String getId() { return id; }

    // Nuevos Getters para los datos crudos
    public String getNombrePersona() { return nombrePersona; }
    public void setNombrePersona(String nombrePersona) { this.nombrePersona = nombrePersona; }

    public String getCedulaPersona() { return cedulaPersona; }
    public void setCedulaPersona(String cedulaPersona) { this.cedulaPersona = cedulaPersona; }

    // Gestión del Paciente Asociado (Vinculación tardía)
    public Paciente getPacienteAsociado() { return pacienteAsociado; }
    
    public void setPacienteAsociado(Paciente pacienteAsociado) {
        this.pacienteAsociado = pacienteAsociado;
    }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
    
    public Date getFechaCreacion() { return fechaCreacion; }
    
    // ========== MÉTODOS DE NEGOCIO ==========
    
    public void marcarComoRealizada() {
        // Validación simple para evitar errores si no tienes el Enum completo
        if (estado != null) {
            this.estado = EstadoCita.REALIZADA;
        }
    }
    
    public boolean cancelar() {
        if (puedeModificarse()) {
            this.estado = EstadoCita.CANCELADA;
            return true;
        }
        return false;
    }
    
    public boolean puedeModificarse() {
        // Lógica de seguridad
        if (estado == EstadoCita.REALIZADA || estado == EstadoCita.CANCELADA) {
            return false;
        }
        Date hoy = new Date();
        return !fecha.before(hoy);
    }
    
    public boolean modificar(Date nuevaFecha, String nuevaHora) {
        if (!puedeModificarse()) return false;
        
        this.fecha = nuevaFecha;
        this.hora = nuevaHora;
        this.estado = EstadoCita.MODIFICADA;
        return true;
    }
    
    // Métodos utilitarios de visualización
    
    public String getNombreMedico() {
        return medico != null ? medico.getNombreConTitulo() : "Sin médico";
    }
    
    public String getFechaFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
    }

    /**
     * Obtiene el paciente real asociado a la cita.
     * Puede retornar null si aún no se ha hecho la vinculación.
     */
    public Paciente getPaciente() {
        return this.pacienteAsociado;
    }
    
    // ========== MÉTODOS SOBRESCRITOS ==========
    
    @Override
    public String toString() {
        // Ajustado para usar nombrePersona en lugar de paciente.getNombre()
        return String.format("[%s] %s - Paciente: %s | Médico: %s | %s",
                id,
                getFechaFormateada(),
                nombrePersona, 
                getNombreMedico(),
                hora);
    }
    
    /**
     * Representación para JTables o Listas
     */
    public String toStringCompacto() {
        // Indica si ya fue vinculado o sigue siendo solo un nombre
        String estadoVinculacion = (pacienteAsociado != null) ? " (Vinculado)" : " (Sin Historia)";
        
        return String.format("%s - %s %s | %s",
                hora,
                nombrePersona,
                estadoVinculacion,
                estado);
    }
}