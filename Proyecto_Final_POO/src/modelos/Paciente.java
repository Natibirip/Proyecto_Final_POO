package modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Clase que representa a un paciente en el sistema de la clínica.
 * Hereda de Persona e incluye información médica adicional.
 * * @author Equipo de Desarrollo
 * @version 1.1 (Refactorizado a ArrayList)
 */
public class Paciente extends Persona implements Serializable {
    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Atributos específicos del paciente
    private ArrayList<Consulta> historialConsultas;
    
    // SISTEMA DE VACUNACIÓN SIN MAPS (ArrayLists Paralelos)
    // listaVacunas guarda la vacuna, listaEstadosVacunas guarda true/false en la misma posición
    private ArrayList<Vacuna> listaVacunas;
    private ArrayList<Boolean> listaEstadosVacunas;
    
    private Date fechaRegistro;
    private Double peso;
    private Double estatura;
    private String TipoSangre;
    private ArrayList<String> Enfermedades;
    private ArrayList<String> Alergias;
    private boolean perfilCompleto;
    
    /**
     * Constructor completo para paciente
     * * @param cedula Cédula de identidad del paciente
     * @param nombre Nombre completo del paciente
     * @param telefono Teléfono de contacto
     * @param fechaNacimiento Fecha de nacimiento
     * @param sexo Sexo del paciente ('M' o 'F')
     */
    
    
    


    public Paciente(String cedula, String nombre, String telefono, Date fechaNacimiento, char sexo,
			 Date fechaRegistro, Double peso, Double estatura, String tipoSangre, boolean perfilCompleto) {
		super(cedula, nombre, telefono, fechaNacimiento, sexo);
		historialConsultas =  new ArrayList<>();;
		listaVacunas = new ArrayList<>();
		listaEstadosVacunas = new ArrayList<>();
		this.fechaRegistro = fechaRegistro;// Fecha actual
		this.peso = peso;
		this.estatura = estatura;
		TipoSangre = tipoSangre;
		Enfermedades = new ArrayList<>();
		Alergias = new ArrayList<>();
		this.perfilCompleto = false;// Inicialmente perfil básico
	}

	// ========== GETTERS Y SETTERS ==========
    
    public ArrayList<Consulta> getHistorialConsultas() {
        // Retornar copia para proteger la lista interna (opcional, pero buena práctica)
        return new ArrayList<>(historialConsultas);
    }
    
    // Nota: Como ya no usamos Map, no podemos devolver Map<Vacuna, Boolean>.
    // Devolvemos las listas para que el controlador o reporte las use si es necesario.
    public ArrayList<Vacuna> getListaVacunas() {
        return listaVacunas;
    }

    public ArrayList<Boolean> getListaEstadosVacunas() {
        return listaEstadosVacunas;
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
    
    public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Double getEstatura() {
		return estatura;
	}

	public void setEstatura(Double estatura) {
		this.estatura = estatura;
	}

	public String getTipoSangre() {
		return TipoSangre;
	}

	public void setTipoSangre(String tipoSangre) {
		TipoSangre = tipoSangre;
	}

	public ArrayList<String> getEnfermedades() {
		return Enfermedades;
	}

	public void setEnfermedades(ArrayList<String> enfermedades) {
		Enfermedades = enfermedades;
	}

	public ArrayList<String> getAlergias() {
		return Alergias;
	}

	public void setAlergias(ArrayList<String> alergias) {
		Alergias = alergias;
	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setHistorialConsultas(ArrayList<Consulta> historialConsultas) {
		this.historialConsultas = historialConsultas;
	}

	public void setListaVacunas(ArrayList<Vacuna> listaVacunas) {
		this.listaVacunas = listaVacunas;
	}

	public void setListaEstadosVacunas(ArrayList<Boolean> listaEstadosVacunas) {
		this.listaEstadosVacunas = listaEstadosVacunas;
	}

	public void agregarAlergia(String alergia) {
		Alergias.add(alergia);
	}
	
	public void agregarEnfermedad(String Enfermedad) {
		Enfermedades.add(Enfermedad);
	}
	/**
     * Agrega una consulta al historial del paciente
     * * @param consulta Consulta a agregar
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
     * Actualiza el estado de vacunación de una vacuna específica.
     * Busca en el ArrayList si la vacuna existe y actualiza su posición paralela.
     * * @param vacuna Vacuna a actualizar
     * @param aplicada true si la vacuna fue aplicada, false en caso contrario
     */
    public void actualizarVacuna(Vacuna vacuna, boolean aplicada) {
        if (vacuna == null) return;

        int index = -1;
        // Buscamos manualmente el índice de la vacuna
        for (int i = 0; i < listaVacunas.size(); i++) {
            if (listaVacunas.get(i).equals(vacuna)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            // Si existe, actualizamos el estado en la lista paralela
            listaEstadosVacunas.set(index, aplicada);
        } else {
            // Si no existe, agregamos a ambas listas
            listaVacunas.add(vacuna);
            listaEstadosVacunas.add(aplicada);
        }
    }
    
    /**
     * Verifica si el paciente tiene aplicada una vacuna específica
     * * @param vacuna Vacuna a verificar
     * @return true si la vacuna está aplicada, false en caso contrario
     */
    public boolean tieneVacuna(Vacuna vacuna) {
        if (vacuna == null) return false;

        for (int i = 0; i < listaVacunas.size(); i++) {
            // Comparamos objetos. Es importante que Vacuna tenga equals() implementado
            if (listaVacunas.get(i).equals(vacuna)) {
                return listaEstadosVacunas.get(i);
            }
        }
        return false; // No encontrada en la lista
    }
    
    /**
     * Obtiene el número total de consultas del paciente
     * * @return Cantidad de consultas en el historial
     */
    public int getCantidadConsultas() {
        return historialConsultas.size();
    }
    
    /**
     * Obtiene la última consulta del paciente
     * * @return Última consulta o null si no hay consultas
     */
    public Consulta getUltimaConsulta() {
        if (historialConsultas.isEmpty()) {
            return null;
        }
        return historialConsultas.get(historialConsultas.size() - 1);
    }
    
    /**
     * Calcula el porcentaje de cobertura de vacunación
     * * @param vacunasClinica ArrayList de todas las vacunas disponibles en la clínica
     * @return Porcentaje de cobertura (0.0 a 100.0)
     */
    public double calcularCoberturVacunacion(ArrayList<Vacuna> vacunasClinica) {
        if (vacunasClinica == null || vacunasClinica.isEmpty()) {
            return 0.0;
        }
        
        int vacunasAplicadas = 0;
        // Recorremos las vacunas del sistema y verificamos si el paciente las tiene
        for (int i = 0; i < vacunasClinica.size(); i++) {
            Vacuna v = vacunasClinica.get(i);
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
     * * @param vacunasClinica ArrayList de vacunas disponibles en la clínica
     */
    public void inicializarEstadoVacunacion(ArrayList<Vacuna> vacunasClinica) {
        if (vacunasClinica != null) {
            for (int i = 0; i < vacunasClinica.size(); i++) {
                Vacuna v = vacunasClinica.get(i);
                // Solo agregamos si no existe ya en nuestra lista personal
                if (!existeEnListaPersonal(v)) {
                    listaVacunas.add(v);
                    listaEstadosVacunas.add(false);
                }
            }
        }
    }

    // Método auxiliar privado para verificar existencia sin Streams
    private boolean existeEnListaPersonal(Vacuna v) {
        for (int i = 0; i < listaVacunas.size(); i++) {
            if (listaVacunas.get(i).equals(v)) {
                return true;
            }
        }
        return false;
    }
    

}