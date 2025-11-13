package modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Representa una consulta médica entre un paciente y un médico.
 * Incluye información clínica y de vacunación.
 * 
 * @author Equipo
 * @version 1.1
 */
public class Consulta implements Serializable {

    private static final long serialVersionUID = 1L;

    // ====== Atributos ======
    private String id;
    private Paciente paciente;
    private Medico medico;
    private Date fechaConsulta;

    private String sintomas;
    private String diagnostico;
    private String tratamiento;

    private Enfermedad enfermedadVigilada; // Puede ser null
    private boolean vaAlResumen;

    private ArrayList<Vacuna> vacunasAplicadas; // Lista de vacunas aplicadas
    private Cita citaAsociada;

    // ====== Constructor ======
    public Consulta(String id, Paciente paciente, Medico medico, Date fechaConsulta,
                    String sintomas, String diagnostico, String tratamiento,
                    Enfermedad enfermedadVigilada, boolean vaAlResumen,
                    ArrayList<Vacuna> vacunasAplicadas, Cita citaAsociada) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaConsulta = fechaConsulta;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.enfermedadVigilada = enfermedadVigilada;
        this.vaAlResumen = vaAlResumen;
        this.vacunasAplicadas = (vacunasAplicadas != null) ? vacunasAplicadas : new ArrayList<>();
        this.citaAsociada = citaAsociada;
    }

    // ====== Getters y Setters ======

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public Date getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(Date fechaConsulta) { this.fechaConsulta = fechaConsulta; }

    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public Enfermedad getEnfermedadVigilada() { return enfermedadVigilada; }
    public void setEnfermedadVigilada(Enfermedad enfermedadVigilada) { this.enfermedadVigilada = enfermedadVigilada; }

    public boolean isVaAlResumen() { return vaAlResumen; }
    public void setVaAlResumen(boolean vaAlResumen) { this.vaAlResumen = vaAlResumen; }

    public ArrayList<Vacuna> getVacunasAplicadas() { return vacunasAplicadas; }
    public void setVacunasAplicadas(ArrayList<Vacuna> vacunasAplicadas) { this.vacunasAplicadas = vacunasAplicadas; }

    public Cita getCitaAsociada() { return citaAsociada; }
    public void setCitaAsociada(Cita citaAsociada) { this.citaAsociada = citaAsociada; }

    // ====== Métodos adicionales ======

    public void agregarVacuna(Vacuna vacuna) {
        if (vacuna != null && !vacunasAplicadas.contains(vacuna)) {
            vacunasAplicadas.add(vacuna);
        }
    }

    public boolean tieneVacuna(Vacuna vacuna) {
        return vacunasAplicadas != null && vacunasAplicadas.contains(vacuna);
    }

    /**
     * Determina si la consulta puede ser vista por un médico dado.
     */
    public boolean esVisiblePara(Medico m) {
        return this.medico.equals(m) || this.enfermedadVigilada != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consulta)) return false;
        Consulta consulta = (Consulta) o;
        return Objects.equals(id, consulta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
