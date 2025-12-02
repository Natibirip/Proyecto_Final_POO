package modelos;

import java.io.Serializable;
import java.util.Date;

public class Consulta implements Serializable {

    private static final long serialVersionUID = 1L;

    // ====== Atributos ======
    private String id;
    private Paciente paciente;
    private Medico medico;
    private Date fechaConsulta;

    // Datos clínicos
    private String sintomas;
    private String diagnostico;
    private String tratamiento;
    private double pesoRegistrado; // Peso del paciente al momento de la consulta

    // Vigilancia y Privacidad
    private Enfermedad enfermedadVigilada; // Puede ser null
    private boolean vaAlResumen;
    private boolean esPublica; // Checkbox manual del médico

    // Vacunación (Solo 1 por consulta según requerimiento)
    private Vacuna vacunaAplicada; // Puede ser null
    
    private Cita citaAsociada;

    // ====== Constructor ======
    public Consulta(String id, Paciente paciente, Medico medico, Date fechaConsulta,
                    String sintomas, String diagnostico, String tratamiento, double pesoRegistrado,
                    Enfermedad enfermedadVigilada, boolean vaAlResumen, boolean esPublica,
                    Vacuna vacunaAplicada, Cita citaAsociada) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaConsulta = fechaConsulta;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.pesoRegistrado = pesoRegistrado; 
        this.enfermedadVigilada = enfermedadVigilada;
        this.vaAlResumen = vaAlResumen;
        this.esPublica = esPublica;
        this.vacunaAplicada = vacunaAplicada;
        this.citaAsociada = citaAsociada;
    }

    // ====== Getters y Setters ======


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public Medico getMedico() {
		return medico;
	}

	public Date getFechaConsulta() {
		return fechaConsulta;
	}

	public String getSintomas() {
		return sintomas;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public String getTratamiento() {
		return tratamiento;
	}

	public double getPesoRegistrado() {
		return pesoRegistrado;
	}

	public Enfermedad getEnfermedadVigilada() {
		return enfermedadVigilada;
	}

	public boolean isVaAlResumen() {
		return vaAlResumen;
	}

	public boolean isEsPublica() {
		return esPublica;
	}

	public Vacuna getVacunaAplicada() {
		return vacunaAplicada;
	}

	public Cita getCitaAsociada() {
		return citaAsociada;
	}


    /**
     * Determina si la consulta es visible para un médico específico.
     * Reglas:
     * 1. Es el mismo médico que la creó.
     * 2. O la consulta fue marcada como pública manualmente.
     * 3. O tiene una enfermedad vigilada (es pública automáticamente).
     */
    public boolean esVisiblePara(Medico m) {
        if (m == null) return false;
        
        // Comparación de Strings (Cédulas) en lugar de HashCode
        boolean esMiConsulta = this.medico.getCedula().equals(m.getCedula());
        boolean tieneEnfermedadVigilada = (this.enfermedadVigilada != null);
        
        return esMiConsulta || this.esPublica || tieneEnfermedadVigilada;
    }
    
    // Método equals simple basado en ID (String)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Consulta other = (Consulta) obj;
        return id != null && id.equals(other.id);
    }
}
