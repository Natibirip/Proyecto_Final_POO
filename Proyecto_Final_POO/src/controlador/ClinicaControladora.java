package controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Importamos tus modelos (ajusta los nombres de paquetes si es necesario)
import modelos.*;

public class ClinicaControladora implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // Instancia única (Patrón Singleton)
    private static ClinicaControladora instancia;

    // --- BASE DE DATOS EN MEMORIA (ArrayLists) ---
    private ArrayList<Usuario> usuarios;
    private ArrayList<Paciente> pacientes;
    private ArrayList<Medico> medicos;
    private ArrayList<Administrador> administradores;
    private ArrayList<Secretaria> secretarias;
    
    private ArrayList<Consulta> consultas;
    private ArrayList<Cita> citas;
    private ArrayList<Vacuna> inventarioVacunas;
    // AGREGAR: Lista de enfermedades vigiladas
    private ArrayList<Enfermedad> enfermedadesVigiladas;

    // --- CONSTRUCTOR PRIVADO ---
    private ClinicaControladora() {
        // Inicializamos todas las listas para evitar NullPointerException
        this.usuarios = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.administradores = new ArrayList<>();
        this.secretarias = new ArrayList<>();
        this.consultas = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.inventarioVacunas = new ArrayList<>();
        // AGREGAR: Inicializar lista de enfermedades
        this.enfermedadesVigiladas = new ArrayList<>();
        
        // Verificar si necesitamos el usuario por defecto
        inicializarDatosPorDefecto();
    }

    // --- MÉTODO PARA OBTENER LA INSTANCIA (SINGLETON) ---
    public static ClinicaControladora getInstance() {
        if (instancia == null) {
            instancia = new ClinicaControladora();
        }
        return instancia;
    }

    // --- LÓGICA DE INICIALIZACIÓN ---
    private void inicializarDatosPorDefecto() {
        // Si no hay usuarios, creamos el Administrador inicial
        if (this.usuarios.isEmpty()) {
            // Usuario admin sin persona asociada (null), según tus reglas
            Usuario adminInicial = new Usuario(
                "admin", 
                "admin", 
                RolUsuario.ADMINISTRADOR, 
                null 
            );
            this.usuarios.add(adminInicial);
            System.out.println("Sistema inicializado: Usuario 'admin' creado.");
        }
    }

    // ==========================================
    // GESTIÓN DE USUARIOS Y AUTENTICACIÓN
    // ==========================================

    public Usuario hacerLogin(String username, String password) {
        // Búsqueda lineal (NO Map, NO Streams)
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u; // Login exitoso
            }
        }
        return null; // Login fallido
    }

    public boolean existeUsuario(String username) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void registrarUsuario(Usuario nuevoUsuario) {
        if (!existeUsuario(nuevoUsuario.getUsername())) {
            this.usuarios.add(nuevoUsuario);
        }
    }

    // ==========================================
    // GESTIÓN DE PERSONAS (Búsquedas lineales)
    // ==========================================

    public Paciente buscarPacientePorCedula(String cedula) {
        for (int i = 0; i < pacientes.size(); i++) {
            Paciente p = pacientes.get(i);
            if (p.getCedula().equals(cedula)) {
                return p;
            }
        }
        return null;
    }

    public Medico buscarMedicoPorCedula(String cedula) {
        for (Medico m : medicos) {
            if (m.getCedula().equals(cedula)) {
                return m;
            }
        }
        return null;
    }

    // ==========================================
    // MÉTODOS DE REGISTRO (CRUD BÁSICO)
    // ==========================================

    public void registrarPaciente(Paciente p) {
        // Validar que no exista ya
        if (buscarPacientePorCedula(p.getCedula()) == null) {
            this.pacientes.add(p);
        }
    }

 // Métodos de Registro
    public void registrarSecretaria(Secretaria s) {
        if (s != null) {
            // Validar que no exista la cédula
            for(Secretaria existente : secretarias) {
                if(existente.getCedula().equals(s.getCedula())) return;
            }
            this.secretarias.add(s);
            System.out.println("Secretaria registrada: " + s.getNombre());
        }
    }

    // Asegúrate de tener también el de médicos que vimos antes
    public void registrarMedico(Medico m) {

        if (m != null) this.medicos.add(m);
    }
    // Registra consulta y actualiza historiales si es necesario
    public void registrarConsulta(Consulta c) {
        this.consultas.add(c);
        // Aquí podrías agregar lógica extra, como añadir la consulta 
        // al historial del paciente si la clase Paciente tiene una lista interna.
    }

    // ==========================================
    // GESTIÓN DE VACUNAS (MÉTODOS NUEVOS)
    // ==========================================

    public boolean agregarVacuna(Vacuna v) {
        if (v == null || !v.esValida()) {
            return false;
        }
        
        // Verificar que no exista ya
        if (buscarVacuna(v.getCodigo()) != null) {
            return false;
        }
        
        return inventarioVacunas.add(v);
    }
    
    public boolean eliminarVacuna(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }
        
        Vacuna vacuna = buscarVacuna(codigo);
        if (vacuna != null) {
            return inventarioVacunas.remove(vacuna);
        }
        return false;
    }
    
    public boolean modificarVacuna(String codigo, Vacuna nuevaVacuna) {
        if (codigo == null || nuevaVacuna == null || !nuevaVacuna.esValida()) {
            return false;
        }
        
        Vacuna vacunaExistente = buscarVacuna(codigo);
        if (vacunaExistente == null) {
            return false;
        }
        
        // Si el código cambió, verificar que el nuevo no exista
        if (!codigo.equals(nuevaVacuna.getCodigo())) {
            if (buscarVacuna(nuevaVacuna.getCodigo()) != null) {
                return false;
            }
        }
        
        // Eliminar la antigua y agregar la nueva
        eliminarVacuna(codigo);
        return agregarVacuna(nuevaVacuna);
    }
    
    public List<Vacuna> obtenerVacunas() {
        return new ArrayList<>(inventarioVacunas); // Copia defensiva
    }
    
    public Vacuna buscarVacuna(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        
        for (Vacuna v : inventarioVacunas) {
            if (v.getCodigo().equals(codigo)) {
                return v;
            }
        }
        return null;
    }

    // ==========================================
    // GESTIÓN DE ENFERMEDADES (MÉTODOS NUEVOS)
    // ==========================================

    public boolean agregarEnfermedad(Enfermedad e) {
        if (e == null || !e.esValida()) {
            return false;
        }
        
        // Verificar que no exista ya
        if (buscarEnfermedad(e.getCodigo()) != null) {
            return false;
        }
        
        return enfermedadesVigiladas.add(e);
    }
    
    public boolean eliminarEnfermedad(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }
        
        Enfermedad enfermedad = buscarEnfermedad(codigo);
        if (enfermedad != null) {
            return enfermedadesVigiladas.remove(enfermedad);
        }
        return false;
    }
    
    public List<Enfermedad> obtenerEnfermedades() {
        return new ArrayList<>(enfermedadesVigiladas);
    }
    
    public List<Enfermedad> obtenerEnfermedadesVigiladas() {
        List<Enfermedad> vigiladas = new ArrayList<>();
        for (Enfermedad e : enfermedadesVigiladas) {
            if (e.isEsVigilada() && e.isActiva()) {
                vigiladas.add(e);
            }
        }
        return vigiladas;
    }
    
    public Enfermedad buscarEnfermedad(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        
        for (Enfermedad e : enfermedadesVigiladas) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        return null;
    }

    // Método para forzar el guardado (si implementamos persistencia luego)
    public void guardarDatos() {
        // Lógica de Serialización aquí (ObjectOutputStream)
    }
    
    public void cargarDatos() {
        // Lógica de Deserialización aquí (ObjectInputStream)
    }

    // ==========================================
    // GETTERS (AGREGAR EL NUEVO GETTER)
    // ==========================================

	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}

	public ArrayList<Paciente> getPacientes() {
		return pacientes;
	}

	public ArrayList<Medico> getMedicos() {
		return medicos;
	}

	public ArrayList<Administrador> getAdministradores() {
		return administradores;
	}

	public ArrayList<Secretaria> getSecretarias() {
		return secretarias;
	}

	public ArrayList<Consulta> getConsultas() {
		return consultas;
	}

	public ArrayList<Cita> getCitas() {
		return citas;
	}

	public ArrayList<Vacuna> getInventarioVacunas() {
		return inventarioVacunas;
	}
	
	// AGREGAR: Getter para enfermedades vigiladas
	public ArrayList<Enfermedad> getEnfermedadesVigiladas() {
	    return enfermedadesVigiladas;
	}
}