package vistas;

import controlador.ClinicaControladora;
import modelos.Medico;
import modelos.RolUsuario;
import modelos.Usuario;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
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
    private JMenu menuAdministracion;
    private JMenu menuConsultas;
    
    // Panel de Dashboard (NUEVO)
    private JPanel panelDashboard;

    public Principal(Usuario usuario) {
        this.usuarioActual = usuario;

        // Configuración de la Ventana
        setTitle("Sistema de Gestión Clínica - Menú Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // 1. Crear Barra de Menú (INTACTO)
        crearBarraMenu();

        // 2. Crear Panel Central (MODIFICADO PARA DASHBOARD)
        crearPanelCentral();

        // 3. Crear Barra de Estado (Info del usuario)
        crearBarraEstado();

        // 4. Aplicar Restricciones según el Rol
        aplicarPermisos();
        
        // 5. Cargar Widgets Dinámicos (NUEVO)
        cargarWidgetsSegunRol();
    }

    private void crearBarraMenu() {
        // ... (TU CÓDIGO DE MENÚ SE MANTIENE EXACTAMENTE IGUAL AQUÍ) ...
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

        // --- MENU PACIENTES ---
        JMenu menuPacientes = new JMenu("Pacientes");
        JMenuItem itemNuevoPaciente = new JMenuItem("Registrar Paciente");
        itemNuevoPaciente.addActionListener(e -> {
            RegClientePrt2 ventanaCliente = new RegClientePrt2();
            ventanaCliente.setVisible(true);
        });
        JMenuItem itemBuscarPaciente = new JMenuItem("Buscar / Editar Paciente");
        itemBuscarPaciente.addActionListener(e -> {
            BuscarPacientesFrame listaPaciente = new BuscarPacientesFrame();
            listaPaciente.setVisible(true);
        });

        menuPacientes.add(itemNuevoPaciente);
        menuPacientes.add(itemBuscarPaciente);

        // --- MENU CITAS ---
        JMenu menuCitas = new JMenu("Citas");
        JMenuItem itemAgendarCita = new JMenuItem("Agendar Nueva Cita");
        itemAgendarCita.addActionListener(e -> {
            RegCita ventanaCita = new RegCita(null);
            ventanaCita.setVisible(true);
        });
        JMenuItem itemVerAgenda = new JMenuItem("Ver Agenda");
        itemVerAgenda.addActionListener(e -> {
            ListaCitasFrame ventanaListaCita = new ListaCitasFrame(usuarioActual);
            ventanaListaCita.setVisible(true);
        });

        menuCitas.add(itemAgendarCita);
        menuCitas.add(itemVerAgenda);

        // --- MENU CONSULTAS ---
        menuConsultas = new JMenu("Consultas Médicas");
        itemNuevaConsulta = new JMenuItem("Realizar Consulta / Diagnóstico");
        itemHistorialMedico = new JMenuItem("Ver Historial Clínico");
        itemHistorialMedico.addActionListener(e -> {
            VerHistorialFrame ventanaHistorial = new VerHistorialFrame(usuarioActual);
            ventanaHistorial.setVisible(true);
        });
        
        menuConsultas.add(itemNuevaConsulta);
        menuConsultas.add(itemHistorialMedico);

        // --- MENU VACUNAS ---
        JMenu menuVacunas = new JMenu("Vacunación");
        JMenuItem itemInventario = new JMenuItem("Gestión de Inventario");
        itemInventario.addActionListener(e -> {
            GestionVacunasFrame ventanaVacunas = new GestionVacunasFrame();
            ventanaVacunas.setVisible(true);
        });

        menuVacunas.add(itemInventario);

        // --- MENU ADMINISTRACIÓN ---
        menuAdministracion = new JMenu("Administración");
        itemGestionarUsuarios = new JMenuItem("Gestionar Usuarios y Roles");
        itemGestionarUsuarios.addActionListener(e -> {
            GestionUsuariosFrame ventanaUsuarios = new GestionUsuariosFrame();
            ventanaUsuarios.setVisible(true);
        });
        
        menuAdministracion.add(itemGestionarUsuarios);

        // Agregar menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuPacientes);
        menuBar.add(menuCitas);
        menuBar.add(menuConsultas);
        menuBar.add(menuVacunas);
        
        JMenu mnEnfermedades = new JMenu("Enfermedades");
        menuBar.add(mnEnfermedades);
        
        JMenuItem mntmEnfermedadesBajoVigilancia = new JMenuItem("Enfermedades bajo vigilancia");
        mntmEnfermedadesBajoVigilancia.addActionListener(e -> {
            GestionEnfermedadesFrame ventanaEnfermedades = new GestionEnfermedadesFrame();
            ventanaEnfermedades.setVisible(true);
        });
        mnEnfermedades.add(mntmEnfermedadesBajoVigilancia);
        
        JMenu mnReportes = new JMenu("Reportes");
        menuBar.add(mnReportes);
        
        JMenuItem mntmReportesConsultas = new JMenuItem("Reportes consultas");
        mntmReportesConsultas.addActionListener(e -> {
            ReportesGraficosFrame ventanaReportes = new ReportesGraficosFrame();
            ventanaReportes.setVisible(true);
        });
        mnReportes.add(mntmReportesConsultas);
        menuBar.add(menuAdministracion);
        
        JMenuItem mntmGestionarPersonal = new JMenuItem("Gestionar Personal");
        mntmGestionarPersonal.addActionListener(e -> {
            GestionPersonalFrame ventanaPersonal = new GestionPersonalFrame();
            ventanaPersonal.setVisible(true);
        });
        menuAdministracion.add(mntmGestionarPersonal);

        setJMenuBar(menuBar);
    }

    // --- Panel Central con Dashboard ---
    private void crearPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.LIGHT_GRAY);

        // Título de Bienvenida
        JLabel lblBienvenida = new JLabel("Hola, " + usuarioActual.getUsername());
        lblBienvenida.setBackground(new Color(192, 192, 192));
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        lblBienvenida.setBorder(new EmptyBorder(20, 30, 20, 0));
        lblBienvenida.setForeground(new Color(60, 60, 60));
        panelCentral.add(lblBienvenida, BorderLayout.NORTH);

        // Contenedor de Tarjetas (Widgets)
        panelDashboard = new JPanel();
        panelDashboard.setLayout(new GridLayout(0, 3, 20, 20)); // Grid adaptable
        panelDashboard.setBackground(new Color(173, 216, 230));
        panelDashboard.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JScrollPane scroll = new JScrollPane(panelDashboard);
        scroll.setBorder(null); // Sin borde feo
        
        panelCentral.add(scroll, BorderLayout.CENTER);
        getContentPane().add(panelCentral, BorderLayout.CENTER);
    }

    private void crearBarraEstado() {
        JPanel panelEstado = new JPanel();
        panelEstado.setBorder(new BevelBorder(BevelBorder.LOWERED));
        panelEstado.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelEstado.setBackground(SystemColor.control);

        String rol = usuarioActual.getRol().toString();
        String usuario = usuarioActual.getUsername();
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        JLabel lblInfo = new JLabel(" Usuario: " + usuario + " | Rol: " + rol + " | Fecha: " + fecha);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panelEstado.add(lblInfo);
        getContentPane().add(panelEstado, BorderLayout.SOUTH);
    }

    private void aplicarPermisos() {
        RolUsuario rol = usuarioActual.getRol();

        if (rol == RolUsuario.SECRETARIA) {
            menuConsultas.setVisible(false);
            menuAdministracion.setVisible(false);
        } else if (rol == RolUsuario.MEDICO) {
            menuAdministracion.setVisible(false);
            menuConsultas.setVisible(true);
        }
    }

    private void cerrarSesion() {
        this.dispose();
        new LoginScreen().setVisible(true);
    }

    // =================================================================
    // LÓGICA DE WIDGETS DINÁMICOS SEGÚN ROL
    // =================================================================
    
 // =================================================================
    // LÓGICA DE WIDGETS DINÁMICOS SEGÚN ROL (ACTUALIZADO)
    // =================================================================
    
    private void cargarWidgetsSegunRol() {
        panelDashboard.removeAll(); // Limpiar
        ClinicaControladora ctrl = ClinicaControladora.getInstance();
        RolUsuario rol = usuarioActual.getRol();

        // --- CASO 1: MÉDICO ---
        if (rol == RolUsuario.MEDICO) {
            
            // Si el usuario tiene un objeto Medico asociado, contamos SUS citas
            int citasHoy = 0;
            if(usuarioActual.getPersonaAsociada() instanceof Medico) {
                Medico m = (Medico) usuarioActual.getPersonaAsociada();
                citasHoy = ctrl.contarCitasMedicoHoy(m);
            }
            
            // Tarjeta 1: Citas de Hoy (Agenda Personal)
            agregarTarjeta("Mis Citas de Hoy", String.valueOf(citasHoy), new Color(100, 149, 237), e -> {
                new ListaCitasFrame(usuarioActual).setVisible(true); 
            });

            // Tarjeta 2: Registrar Nuevo Paciente (CAMBIO SOLICITADO)
            agregarTarjeta("Registrar Paciente", "Nuevo +", new Color(60, 179, 113), e -> {
                new RegClientePrt2().setVisible(true);
            });
            
            // Tarjeta 3: Historiales / Búsqueda
            agregarTarjeta("Historiales", "Búsqueda", new Color(255, 165, 0), e -> {
                new VerHistorialFrame(usuarioActual).setVisible(true);
            });
        } 
        
        // --- CASO 2: SECRETARIA ---
        else if (rol == RolUsuario.SECRETARIA) {
            
            int citasTotal = ctrl.contarCitasHoy();
            int totalPacientes = ctrl.contarPacientes();
            
            // Tarjeta 1: Gestión de Agenda (General)
            agregarTarjeta("Agenda Global Hoy", citasTotal + " citas", new Color(70, 130, 180), e -> {
                new ListaCitasFrame(usuarioActual).setVisible(true);
            });

            // Tarjeta 2: Buscar Paciente (CAMBIO SOLICITADO)
            // Muestra el total y lleva a la búsqueda, pero NO crea.
            agregarTarjeta("Directorio Pacientes", totalPacientes + " reg.", new Color(255, 165, 0), e -> {
                new BuscarPacientesFrame().setVisible(true);
            });

            // Tarjeta 3: Agendar Cita
            agregarTarjeta("Nueva Cita", "Agendar", new Color(147, 112, 219), e -> {
                new RegCita(this).setVisible(true);
            });
        } 
        
        // --- CASO 3: ADMINISTRADOR (6 WIDGETS) ---
        else if (rol == RolUsuario.ADMINISTRADOR) {
            
            int alertasStock = ctrl.contarAlertasStock();
            int totalPacientes = ctrl.contarPacientes();
            int totalMedicos = ctrl.contarMedicos();
            int totalEnf = ctrl.contarEnfermedades();
            
            // Fila 1
            // 1. Alertas Stock
            Color colorAlerta = (alertasStock > 0) ? new Color(220, 20, 60) : new Color(46, 139, 87);
            agregarTarjeta("Alertas Stock Vacunas", alertasStock + " críticas", colorAlerta, e -> {
                new GestionVacunasFrame().setVisible(true);
            });

            // 2. Base de Pacientes
            agregarTarjeta("Base de Pacientes", totalPacientes + " total", new Color(70, 130, 180), e -> {
                new BuscarPacientesFrame().setVisible(true);
            });
            
            // 3. Reportes BI
            agregarTarjeta("Reportes BI", "Analíticas", new Color(255, 140, 0), e -> {
                new ReportesGraficosFrame().setVisible(true);
            });
            
            // Fila 2 (Nuevos Widgets)
            // 4. Gestión Usuarios
            agregarTarjeta("Usuarios Sistema", "Accesos", Color.GRAY, e -> {
                new GestionUsuariosFrame().setVisible(true);
            });

            // 5. Gestión Enfermedades (NUEVO)
            agregarTarjeta("Catálogo Enfermedades", totalEnf + " tipos", new Color(123, 104, 238), e -> {
                new GestionEnfermedadesFrame().setVisible(true);
            });

            // 6. Gestión Personal Médico (NUEVO)
            agregarTarjeta("Plantilla Médica", totalMedicos + " Doctores", new Color(0, 128, 128), e -> {
                new GestionPersonalFrame().setVisible(true);
            });
        }

        panelDashboard.revalidate();
        panelDashboard.repaint();
    }

    private void agregarTarjeta(String titulo, String valor, Color colorBorde, ActionListener accion) {
        PanelTarjeta tarjeta = new PanelTarjeta(titulo, valor, colorBorde, accion);
        panelDashboard.add(tarjeta);
    }

    // =================================================================
    // CLASE INTERNA VISUAL: LA TARJETA (WIDGET)
    // =================================================================
 // =================================================================
    // CLASE INTERNA VISUAL: LA TARJETA CON GRADIENTE
    // =================================================================
    class PanelTarjeta extends JPanel {
        
        public PanelTarjeta(String titulo, String valor, Color colorTema, ActionListener accion) {
            setLayout(new BorderLayout());
            
            // IMPORTANTE: Desactivar la opacidad predeterminada para pintar el gradiente
            setOpaque(false);
            
            // Borde izquierdo grueso con el color del tema
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY), // Borde fino
                BorderFactory.createMatteBorder(0, 10, 0, 0, colorTema)       // Borde grueso izq
            ));
            setPreferredSize(new Dimension(200, 150)); // Tamaño base

            // Panel Contenido
            JPanel contenido = new JPanel(new GridLayout(2, 1));
            contenido.setOpaque(false); // Transparente para ver el gradiente de fondo
            contenido.setBorder(new EmptyBorder(15, 15, 10, 15));

            JLabel lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lblTitulo.setForeground(Color.DARK_GRAY); // Gris oscuro para contraste
            
            JLabel lblValor = new JLabel(valor);
            lblValor.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblValor.setForeground(Color.BLACK); // Negro para resaltar el número

            contenido.add(lblTitulo);
            contenido.add(lblValor);
            add(contenido, BorderLayout.CENTER);

            // Botón de Acción (Flecha o texto)
            if (accion != null) {
                JButton btnIr = new JButton("Ir a sección →");
                btnIr.setBorderPainted(false);
                btnIr.setContentAreaFilled(false);
                btnIr.setFocusPainted(false);
                btnIr.setOpaque(false); // Transparente
                btnIr.setForeground(colorTema); // Usamos el color del tema para el texto del botón
                btnIr.setFont(new Font("SansSerif", Font.BOLD, 12));
                btnIr.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnIr.setHorizontalAlignment(SwingConstants.RIGHT);
                btnIr.addActionListener(accion);
                add(btnIr, BorderLayout.SOUTH);
            }
        }

        // --- MÉTODO PARA PINTAR EL GRADIENTE ---
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            
            // Mejora la calidad del renderizado (bordes suaves)
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // DEFINICIÓN DEL GRADIENTE: Blanco -> Azul Muy Suave
            Color colorInicio = Color.WHITE;
            Color colorFin = new Color(225, 240, 255); // Azul hielo (#E1F0FF)

            // Coordenadas: (0,0) arriba -> (0, h) abajo (Gradiente Vertical)
            GradientPaint gp = new GradientPaint(0, 0, colorInicio, 0, h, colorFin);

            g2d.setPaint(gp);
            // Pintamos el rectángulo del fondo
            g2d.fillRect(0, 0, w, h);

            // Llamada a super necesaria para el funcionamiento correcto de Swing
            super.paintComponent(g); 
        }
    }
}