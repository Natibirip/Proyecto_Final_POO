package controlador;

import java.io.*; // IMPORTANTE: Para manejo de archivos
import java.util.ArrayList;
import java.util.List;

import modelos.*;

public class ClinicaControladora implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String ARCHIVO_DATOS = "clinica_datos.dat"; // Nombre del archivo
    
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
    private ArrayList<Enfermedad> enfermedadesVigiladas;
    private ArrayList<String> listaEspecialidades;
    

    // --- CONSTRUCTOR PRIVADO ---
    private ClinicaControladora() {
        // 1. Intentamos cargar los datos del archivo primero
        cargarDatos();

        // 2. Verificamos e inicializamos listas si no existía el archivo (son null)
        inicializarListasSiSonNulas();

        // 3. Si después de cargar/inicializar no hay usuarios, creamos el default
        inicializarDatosPorDefecto();
        
        // 4. Si no hay especialidades, cargamos las default
        if (listaEspecialidades.isEmpty()) {
            cargarEspecialidadesPorDefecto();
        }
    }

    private void inicializarListasSiSonNulas() {
        if (this.usuarios == null) this.usuarios = new ArrayList<>();
        if (this.pacientes == null) this.pacientes = new ArrayList<>();
        if (this.medicos == null) this.medicos = new ArrayList<>();
        if (this.administradores == null) this.administradores = new ArrayList<>();
        if (this.secretarias == null) this.secretarias = new ArrayList<>();
        if (this.consultas == null) this.consultas = new ArrayList<>();
        if (this.citas == null) this.citas = new ArrayList<>();
        if (this.inventarioVacunas == null) this.inventarioVacunas = new ArrayList<>();
        if (this.enfermedadesVigiladas == null) this.enfermedadesVigiladas = new ArrayList<>();
        if (this.listaEspecialidades == null) this.listaEspecialidades = new ArrayList<>();
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
        if (this.usuarios.isEmpty()) {
            Usuario adminInicial = new Usuario("admin", "admin", RolUsuario.ADMINISTRADOR, null);
            this.usuarios.add(adminInicial);
            System.out.println("Sistema inicializado: Usuario 'admin' creado por defecto.");
            guardarDatos(); // Guardamos este estado inicial
        }
    }

    // ==========================================
    // PERSISTENCIA DE DATOS (GUARDAR Y CARGAR)
    // ==========================================

    public void guardarDatos() {
        try {
            // Se usa FileOutputStream para escribir en disco
            FileOutputStream fos = new FileOutputStream(ARCHIVO_DATOS);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Escribimos las listas en orden específico
            oos.writeObject(usuarios);
            oos.writeObject(pacientes);
            oos.writeObject(medicos);
            oos.writeObject(administradores);
            oos.writeObject(secretarias);
            oos.writeObject(consultas);
            oos.writeObject(citas);
            oos.writeObject(inventarioVacunas);
            oos.writeObject(enfermedadesVigiladas);
            oos.writeObject(listaEspecialidades);

            oos.close();
            // System.out.println("Datos guardados correctamente."); // Descomentar para depurar
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (archivo.exists()) {
            try {
                FileInputStream fis = new FileInputStream(archivo);
                ObjectInputStream ois = new ObjectInputStream(fis);

                // Leemos en el MISMO ORDEN que guardamos
                usuarios = (ArrayList<Usuario>) ois.readObject();
                pacientes = (ArrayList<Paciente>) ois.readObject();
                medicos = (ArrayList<Medico>) ois.readObject();
                administradores = (ArrayList<Administrador>) ois.readObject();
                secretarias = (ArrayList<Secretaria>) ois.readObject();
                consultas = (ArrayList<Consulta>) ois.readObject();
                citas = (ArrayList<Cita>) ois.readObject();
                inventarioVacunas = (ArrayList<Vacuna>) ois.readObject();
                enfermedadesVigiladas = (ArrayList<Enfermedad>) ois.readObject();
                listaEspecialidades = (ArrayList<String>) ois.readObject();

                ois.close();
                System.out.println("Datos cargados exitosamente del archivo.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error al cargar los datos. Se iniciará vacío.");
            }
        } else {
            System.out.println("No existe archivo de datos previo. Iniciando sistema nuevo.");
        }
    }

    // ==========================================
    // GESTIÓN DE USUARIOS
    // ==========================================

    public Usuario hacerLogin(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public boolean existeUsuario(String username) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) return true;
        }
        return false;
    }

    public void registrarUsuario(Usuario nuevoUsuario) {
        if (!existeUsuario(nuevoUsuario.getUsername())) {
            this.usuarios.add(nuevoUsuario);
            guardarDatos(); // <--- AUTO-GUARDADO
        }
    }

    // ==========================================
    // GESTIÓN DE PERSONAS
    // ==========================================

    public Paciente buscarPacientePorCedula(String cedula) {
        for (Paciente p : pacientes) {
            if (p.getCedula().equals(cedula)) return p;
        }
        return null;
    }

    public Medico buscarMedicoPorCedula(String cedula) {
        for (Medico m : medicos) {
            if (m.getCedula().equals(cedula)) return m;
        }
        return null;
    }
    
    // Buscar Secretaria
    public Secretaria buscarSecretariaPorCedula(String cedula) {
        for (Secretaria s : secretarias) {
            if (s.getCedula().equals(cedula)) return s;
        }
        return null;
    }

    // ==========================================
    // MÉTODOS DE REGISTRO
    // ==========================================

    public void registrarPaciente(Paciente p) {
        if (buscarPacientePorCedula(p.getCedula()) == null) {
            this.pacientes.add(p);
            guardarDatos(); // <--- AUTO-GUARDADO
        }
    }

    public void registrarSecretaria(Secretaria s) {
        if (s != null && buscarSecretariaPorCedula(s.getCedula()) == null) {
            this.secretarias.add(s);
            System.out.println("Secretaria registrada: " + s.getNombre());
            guardarDatos(); // <--- AUTO-GUARDADO
        }
    }

    public void registrarMedico(Medico m) {
        if (m != null && buscarMedicoPorCedula(m.getCedula()) == null) {
            this.medicos.add(m);
            guardarDatos(); // <--- AUTO-GUARDADO
        }
    }

    public void registrarConsulta(Consulta c) {
        this.consultas.add(c);
        guardarDatos(); // <--- AUTO-GUARDADO
    }

    // ==========================================
    // GESTIÓN DE VACUNAS
    // ==========================================

    public boolean agregarVacuna(Vacuna v) {
        if (v == null || !v.esValida()) return false;
        if (buscarVacuna(v.getCodigo()) != null) return false;
        
        boolean resultado = inventarioVacunas.add(v);
        if(resultado) guardarDatos(); // <--- AUTO-GUARDADO
        return resultado;
    }
    
    public boolean eliminarVacuna(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return false;
        
        Vacuna vacuna = buscarVacuna(codigo);
        if (vacuna != null) {
            boolean resultado = inventarioVacunas.remove(vacuna);
            if(resultado) guardarDatos(); // <--- AUTO-GUARDADO
            return resultado;
        }
        return false;
    }
    
    public boolean modificarVacuna(String codigo, Vacuna nuevaVacuna) {
        if (codigo == null || nuevaVacuna == null || !nuevaVacuna.esValida()) return false;
        Vacuna vacunaExistente = buscarVacuna(codigo);
        if (vacunaExistente == null) return false;
        
        if (!codigo.equals(nuevaVacuna.getCodigo())) {
            if (buscarVacuna(nuevaVacuna.getCodigo()) != null) return false;
        }
        
        // Operación atómica en memoria antes de guardar
        inventarioVacunas.remove(vacunaExistente);
        inventarioVacunas.add(nuevaVacuna);
        guardarDatos(); // <--- AUTO-GUARDADO
        return true;
    }
    
    public List<Vacuna> obtenerVacunas() {
        return new ArrayList<>(inventarioVacunas);
    }
    
    public Vacuna buscarVacuna(String codigo) {
        if (codigo == null) return null;
        for (Vacuna v : inventarioVacunas) {
            if (v.getCodigo().equals(codigo)) return v;
        }
        return null;
    }

    // ==========================================
    // GESTIÓN DE ENFERMEDADES
    // ==========================================

    public boolean agregarEnfermedad(Enfermedad e) {
        if (e == null || !e.esValida()) return false;
        if (buscarEnfermedad(e.getCodigo()) != null) return false;
        
        boolean res = enfermedadesVigiladas.add(e);
        if(res) guardarDatos(); // <--- AUTO-GUARDADO
        return res;
    }
    
    public boolean eliminarEnfermedad(String codigo) {
        Enfermedad enfermedad = buscarEnfermedad(codigo);
        if (enfermedad != null) {
            boolean res = enfermedadesVigiladas.remove(enfermedad);
            if(res) guardarDatos(); // <--- AUTO-GUARDADO
            return res;
        }
        return false;
    }
    
    public List<Enfermedad> obtenerEnfermedades() {
        return new ArrayList<>(enfermedadesVigiladas);
    }
    
    public List<Enfermedad> obtenerEnfermedadesVigiladas() {
        List<Enfermedad> vigiladas = new ArrayList<>();
        for (Enfermedad e : enfermedadesVigiladas) {
            if (e.isEsVigilada() && e.isActiva()) vigiladas.add(e);
        }
        return vigiladas;
    }
    
    public Enfermedad buscarEnfermedad(String codigo) {
        if (codigo == null) return null;
        for (Enfermedad e : enfermedadesVigiladas) {
            if (e.getCodigo().equals(codigo)) return e;
        }
        return null;
    }

    // ==========================================
    // ESPECIALIDADES
    // ==========================================

    private void cargarEspecialidadesPorDefecto() {
        listaEspecialidades.add("Medicina General");
        listaEspecialidades.add("Pediatría");
        listaEspecialidades.add("Cardiología");
        listaEspecialidades.add("Ginecología");
        listaEspecialidades.add("Dermatología");
        listaEspecialidades.add("Ortopedia");
        listaEspecialidades.add("Neurología");
        listaEspecialidades.add("Oftalmología");
    }
    
    public void agregarEspecialidad(String nuevaEspecialidad) {
        if (nuevaEspecialidad == null || nuevaEspecialidad.trim().isEmpty()) return;
        
        String nueva = nuevaEspecialidad.trim();
        boolean existe = false;
        for (String esp : listaEspecialidades) {
            if (esp.equalsIgnoreCase(nueva)) {
                existe = true;
                break;
            }
        }
        
        if (!existe) {
            this.listaEspecialidades.add(nueva);
            System.out.println("Nueva especialidad registrada: " + nueva);
            guardarDatos(); // <--- AUTO-GUARDADO
        }
    }

    


    public void registrarCita(Cita nuevaCita) {
        if (nuevaCita != null) {
            this.citas.add(nuevaCita);
            guardarDatos(); // Guardar en archivo
        }
    }
    
    // ==========================================
    // GETTERS
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


    	public ArrayList<String> getListaEspecialidades() {

    	    return listaEspecialidades;

    	}
}