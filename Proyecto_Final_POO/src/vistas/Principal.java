package vistas;

import modelos.RolUsuario;
import modelos.Usuario;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Principal extends JFrame {

    private Usuario usuarioActual;
    
    // Componentes del Menú
    private JMenuItem itemGestionarUsuarios;
    private JMenuItem itemNuevaConsulta;
    private JMenuItem itemHistorialMedico;
    private JMenuItem itemReporteVacunas;
    private JMenu menuAdministracion;
    private JMenu menuConsultas;
    
    // Panel de escritorio para ventanas internas (MDI - opcional)
    // O simplemente usamos un panel con fondo
    private JLabel lblFondo;

    public Principal(Usuario usuario) {
        this.usuarioActual = usuario;

        // Configuración de la Ventana
        setTitle("Sistema de Gestión Clínica - Menú Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // 1. Crear Barra de Menú
        crearBarraMenu();

        // 2. Crear Panel Central (Bienvenida)
        crearPanelCentral();

        // 3. Crear Barra de Estado (Info del usuario)
        crearBarraEstado();

        // 4. Aplicar Restricciones según el Rol
        aplicarPermisos();
    }

    private void crearBarraMenu() {
        JMenuBar menuBar = new JMenuBar();

        // --- MENU ARCHIVO ---
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        JMenuItem itemSalir = new JMenuItem("Salir del Sistema");
        
        itemCerrarSesion.addActionListener(e -> cerrarSesion());
        itemSalir.addActionListener(e -> System.exit(0));
        
        menuArchivo.add(itemCerrarSesion);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        // --- MENU PACIENTES (Accesible para todos, pero con diferencias internas) ---
        JMenu menuPacientes = new JMenu("Pacientes");
        JMenuItem itemNuevoPaciente = new JMenuItem("Registrar Paciente");
        itemNuevoPaciente.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		RegClientePrt2 ventanaCliente = new RegClientePrt2();
                ventanaCliente.setVisible(true);
                //dispose();
        	}
        });
        JMenuItem itemBuscarPaciente = new JMenuItem("Buscar / Editar Paciente");
        itemBuscarPaciente.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		BuscarPacientesFrame listaPaciente = new BuscarPacientesFrame();
                listaPaciente.setVisible(true);
                
        	}
        });

        menuPacientes.add(itemNuevoPaciente);
        menuPacientes.add(itemBuscarPaciente);

        // --- MENU CITAS (Secretaria y Médicos) ---
        JMenu menuCitas = new JMenu("Citas");
        JMenuItem itemAgendarCita = new JMenuItem("Agendar Nueva Cita");
        itemAgendarCita.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		RegCita ventanaCita = new RegCita(null);
                ventanaCita.setVisible(true);
        	}
        });
        JMenuItem itemVerAgenda = new JMenuItem("Ver Agenda");
        itemVerAgenda.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ListaCitasFrame ventanaListaCita = new ListaCitasFrame(usuarioActual);
                ventanaListaCita.setVisible(true);
        	}
        });

        menuCitas.add(itemAgendarCita);
        menuCitas.add(itemVerAgenda);

        // --- MENU CONSULTAS (Área Médica) ---
        menuConsultas = new JMenu("Consultas Médicas");
        itemNuevaConsulta = new JMenuItem("Realizar Consulta / Diagnóstico");
        itemHistorialMedico = new JMenuItem("Ver Historial Clínico");
        itemHistorialMedico.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		VerHistorialFrame ventanaHistorial = new VerHistorialFrame(usuarioActual);
        		ventanaHistorial.setVisible(true);
        	}
        });
        
        menuConsultas.add(itemNuevaConsulta);
        menuConsultas.add(itemHistorialMedico);

        // --- MENU VACUNAS ---
        JMenu menuVacunas = new JMenu("Vacunación");
        JMenuItem itemInventario = new JMenuItem("Gestión de Inventario");
        itemInventario.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		GestionVacunasFrame ventanaVacunas = new GestionVacunasFrame();
        		ventanaVacunas.setVisible(true);
        	}
        });
        itemReporteVacunas = new JMenuItem("Reporte de Cobertura");

        menuVacunas.add(itemInventario);
        menuVacunas.add(itemReporteVacunas);

        // --- MENU ADMINISTRACIÓN (Solo Admin) ---
        menuAdministracion = new JMenu("Administración");
        itemGestionarUsuarios = new JMenuItem("Gestionar Usuarios y Roles");
        itemGestionarUsuarios.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		GestionUsuariosFrame ventanaUsuarios = new GestionUsuariosFrame();
                ventanaUsuarios.setVisible(true);
        	}
        });
        
        menuAdministracion.add(itemGestionarUsuarios);

        // Agregar menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuPacientes);
        menuBar.add(menuCitas);
        menuBar.add(menuConsultas);
        menuBar.add(menuVacunas);
        menuBar.add(menuAdministracion);
        
        JMenuItem mntmGestionarPersonal = new JMenuItem("Gestionar Personal");
        mntmGestionarPersonal.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		GestionPersonalFrame ventanaPersonal = new GestionPersonalFrame();
                ventanaPersonal.setVisible(true);
        	}
        });
        menuAdministracion.add(mntmGestionarPersonal);

        setJMenuBar(menuBar);
    }

    private void crearPanelCentral() {
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridBagLayout()); // Para centrar el texto
        panelCentral.setBackground(Color.WHITE);

        JLabel lblBienvenida = new JLabel("Bienvenido al Sistema Clínico");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 32));
        lblBienvenida.setForeground(new Color(0, 102, 204)); // Azul médico

        panelCentral.add(lblBienvenida);
        getContentPane().add(panelCentral, BorderLayout.CENTER);
    }

    private void crearBarraEstado() {
        JPanel panelEstado = new JPanel();
        panelEstado.setBorder(new BevelBorder(BevelBorder.LOWERED));
        panelEstado.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelEstado.setBackground(SystemColor.control);

        // Información del usuario
        String rol = usuarioActual.getRol().toString();
        String usuario = usuarioActual.getUsername();
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        JLabel lblInfo = new JLabel(" Usuario: " + usuario + " | Rol: " + rol + " | Fecha: " + fecha);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panelEstado.add(lblInfo);
        getContentPane().add(panelEstado, BorderLayout.SOUTH);
    }

    // ==========================================
    // LÓGICA DE PERMISOS (SEGURIDAD)
    // ==========================================
    private void aplicarPermisos() {
        RolUsuario rol = usuarioActual.getRol();

        // 1. ADMINISTRADOR
        if (rol == RolUsuario.ADMINISTRADOR) {
            // Tiene acceso a todo, no ocultamos nada.
        } 
        
        // 2. SECRETARIA
        else if (rol == RolUsuario.SECRETARIA) {
            // No puede ver datos médicos sensibles
            menuConsultas.setVisible(false); // Ocultamos el menú completo de consultas
            // O deshabilitamos opciones específicas:
            // itemNuevaConsulta.setEnabled(false);
            
            // No puede administrar usuarios
            menuAdministracion.setVisible(false);
            
            // Puede ver inventario de vacunas, pero quizás no reportes complejos
            itemReporteVacunas.setEnabled(false); 
        } 
        
        // 3. MEDICO
        else if (rol == RolUsuario.MEDICO) {
            // No puede administrar usuarios
            menuAdministracion.setVisible(false);
            
            // Puede hacer consultas y ver historiales
            menuConsultas.setVisible(true);
            
        }
    }

    private void cerrarSesion() {
        // Cerrar esta ventana
        this.dispose();
        // Abrir login de nuevo
        new LoginScreen().setVisible(true);
    }
}