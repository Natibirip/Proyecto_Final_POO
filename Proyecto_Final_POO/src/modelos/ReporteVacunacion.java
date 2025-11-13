package modelos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase auxiliar que genera un reporte sobre el estado de vacunación de un paciente.
 * 
 * Calcula automáticamente las vacunas completas, pendientes y el porcentaje de cobertura.
 * 
 * @author Equipo
 * @version 1.1 (solo ArrayList)
 */
public class ReporteVacunacion implements Serializable {

    private static final long serialVersionUID = 1L;

    // ====== Atributos ======
    private Paciente paciente;
    private ArrayList<Vacuna> vacunasCompletas;
    private ArrayList<Vacuna> vacunasPendientes;

    // ====== Constructor ======
    /**
     * Crea un nuevo reporte de vacunación.
     * 
     * @param paciente Paciente al que pertenece el reporte
     * @param vacunasCompletas Lista de vacunas aplicadas
     * @param vacunasPendientes Lista de vacunas pendientes
     */
    public ReporteVacunacion(Paciente paciente, ArrayList<Vacuna> vacunasCompletas, ArrayList<Vacuna> vacunasPendientes) {
        this.paciente = paciente;
        this.vacunasCompletas = (vacunasCompletas != null) ? vacunasCompletas : new ArrayList<Vacuna>();
        this.vacunasPendientes = (vacunasPendientes != null) ? vacunasPendientes : new ArrayList<Vacuna>();
    }

    // ====== Getters ======
    public Paciente getPaciente() {
        return paciente;
    }

    public ArrayList<Vacuna> getVacunasCompletas() {
        return vacunasCompletas;
    }

    public ArrayList<Vacuna> getVacunasPendientes() {
        return vacunasPendientes;
    }

    // ====== Método especial ======
    /**
     * Calcula el porcentaje de vacunas aplicadas sobre el total.
     * 
     * @return porcentaje entre 0 y 100
     */
    public double getPorcentajeCobertura() {
        int total = vacunasCompletas.size() + vacunasPendientes.size();
        if (total == 0) return 0.0;
        return (vacunasCompletas.size() * 100.0) / total;
    }

    // ====== Métodos auxiliares opcionales ======
    public void agregarVacunaCompleta(Vacuna v) {
        if (v != null && !vacunasCompletas.contains(v)) {
            vacunasCompletas.add(v);
        }
    }

    public void agregarVacunaPendiente(Vacuna v) {
        if (v != null && !vacunasPendientes.contains(v)) {
            vacunasPendientes.add(v);
        }
    }

    @Override
    public String toString() {
        return "ReporteVacunacion [paciente=" + paciente.getNombre() + 
               ", completas=" + vacunasCompletas.size() + 
               ", pendientes=" + vacunasPendientes.size() + 
               ", cobertura=" + getPorcentajeCobertura() + "%]";
    }
}
