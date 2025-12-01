package modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Paciente extends Persona implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // Atributos específicos
    private ArrayList<Consulta> historialConsultas;
    private ArrayList<Vacuna> listaVacunas;
    private ArrayList<Boolean> listaEstadosVacunas;
    
    private Date fechaRegistro;
    private Double peso;
    private Double estatura;
    private String tipoSangre;
    private ArrayList<String> enfermedades;
    private ArrayList<String> alergias;
    private boolean perfilCompleto;
    
    // --- CONSTRUCTOR VACÍO (NECESARIO PARA EL MAIN DE PRUEBAS) ---
    public Paciente() {
        // Inicializamos con datos dummy para evitar error en super()
        super("", "", "", new Date(), 'M'); 
        inicializarListas();
        this.fechaRegistro = new Date();
        this.peso = 0.0;
        this.estatura = 0.0;
        this.tipoSangre = "O+";
        this.perfilCompleto = false;
    }

    // --- CONSTRUCTOR COMPLETO ---
    public Paciente(String cedula, String nombre, String telefono, Date fechaNacimiento, char sexo,
             Date fechaRegistro, Double peso, Double estatura, String tipoSangre, boolean perfilCompleto) {
        super(cedula, nombre, telefono, fechaNacimiento, sexo);
        inicializarListas();
        this.fechaRegistro = (fechaRegistro != null) ? fechaRegistro : new Date();
        this.peso = peso;
        this.estatura = estatura;
        this.tipoSangre = tipoSangre;
        this.perfilCompleto = perfilCompleto;
    }
    
    // Método auxiliar para no repetir código
    private void inicializarListas() {
        this.historialConsultas = new ArrayList<>();
        this.listaVacunas = new ArrayList<>();
        this.listaEstadosVacunas = new ArrayList<>();
        this.enfermedades = new ArrayList<>();
        this.alergias = new ArrayList<>();
    }
    

    /**
     * Agrega una nueva consulta al historial clínico del paciente.
     */
    public void agregarConsulta(Consulta nuevaConsulta) {
        if (this.historialConsultas == null) {
            this.historialConsultas = new ArrayList<>();
        }
        if (nuevaConsulta != null) {
            this.historialConsultas.add(nuevaConsulta);
        }
    }

    /**
     * Registra o actualiza el estado de una vacuna.
     * Si la vacuna ya está en la lista, actualiza su estado.
     * Si no está, la agrega.
     */
    public void actualizarVacuna(Vacuna vacuna, boolean aplicada) {
        if (vacuna == null) return;

        // Inicializar listas si son nulas (seguridad)
        if (this.listaVacunas == null) this.listaVacunas = new ArrayList<>();
        if (this.listaEstadosVacunas == null) this.listaEstadosVacunas = new ArrayList<>();

        int indice = -1;
        
        // 1. Buscar si la vacuna ya existe en la lista
        for (int i = 0; i < listaVacunas.size(); i++) {
            // Usamos equals (comparar por código o nombre)
            if (listaVacunas.get(i).equals(vacuna)) {
                indice = i;
                break;
            }
        }

        if (indice != -1) {
            // 2. Si existe, actualizamos el estado en la lista paralela
            listaEstadosVacunas.set(indice, aplicada);
        } else {
            // 3. Si no existe, agregamos a ambas listas
            listaVacunas.add(vacuna);
            listaEstadosVacunas.add(aplicada);
        }
    }
    

    // ========== GETTERS Y SETTERS ==========

    public ArrayList<Consulta> getHistorialConsultas() { return historialConsultas; }
    public void setHistorialConsultas(ArrayList<Consulta> historialConsultas) { this.historialConsultas = historialConsultas; }

    public ArrayList<Vacuna> getListaVacunas() { return listaVacunas; }
    public void setListaVacunas(ArrayList<Vacuna> listaVacunas) { this.listaVacunas = listaVacunas; }

    public ArrayList<Boolean> getListaEstadosVacunas() { return listaEstadosVacunas; }
    public void setListaEstadosVacunas(ArrayList<Boolean> listaEstadosVacunas) { this.listaEstadosVacunas = listaEstadosVacunas; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getEstatura() { return estatura; }
    public void setEstatura(Double estatura) { this.estatura = estatura; }

    public String getTipoSangre() { return tipoSangre; }
    public void setTipoSangre(String tipoSangre) { this.tipoSangre = tipoSangre; }

    public ArrayList<String> getEnfermedades() { return enfermedades; }
    public void setEnfermedades(ArrayList<String> enfermedades) { this.enfermedades = enfermedades; }

    public ArrayList<String> getAlergias() { return alergias; }
    public void setAlergias(ArrayList<String> alergias) { this.alergias = alergias; }

    public boolean isPerfilCompleto() { return perfilCompleto; }
    public void setPerfilCompleto(boolean perfilCompleto) { this.perfilCompleto = perfilCompleto; }

    // Métodos de lógica (ArrayList)
    public void agregarAlergia(String alergia) {
        if (this.alergias == null) this.alergias = new ArrayList<>();
        this.alergias.add(alergia);
    }
    
    public void agregarEnfermedad(String enfermedad) {
        if (this.enfermedades == null) this.enfermedades = new ArrayList<>();
        this.enfermedades.add(enfermedad);
    }
    
    // ... (El resto de tus métodos de lógica se mantienen igual)
}