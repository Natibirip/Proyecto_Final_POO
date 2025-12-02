package vistas;

import controlador.ClinicaControladora;
import modelos.*;
import com.toedter.calendar.JDateChooser; 

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VerHistorialFrame extends JFrame {

    // Contexto
    private Usuario usuarioActual;
    private ClinicaControladora controller;
    
    // --- NUEVO: Lista Paralela para manejar los objetos filtrados ---
    private ArrayList<Consulta> listaFiltrada; 

    // Componentes Filtros
    private JTextField txtFiltroNombre;
    private JTextField txtFiltroCedula;
    private JDateChooser dateFiltroFecha;
    private JComboBox<Enfermedad> cmbFiltroEnfermedad;
    private JComboBox<Vacuna> cmbFiltroVacuna;

    // Tabla
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    public VerHistorialFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        this.controller = ClinicaControladora.getInstance();
        
        // Inicializamos la lista paralela
        this.listaFiltrada = new ArrayList<>();

        setTitle("Historial Clínico General - Usuario: " + usuario.getUsername());
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. PANEL SUPERIOR (FILTROS)
        crearPanelFiltros();

        // 2. PANEL CENTRAL (TABLA)
        crearTablaResultados();

        // 3. PANEL INFERIOR (BOTONES)
        crearBotonera();

        // Carga inicial
        filtrarYListar();
    }

    private void crearPanelFiltros() {
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new GridLayout(2, 1)); 
        panelNorte.setBorder(new TitledBorder("Filtros de Búsqueda"));
        
        // Fila 1: Datos Personales y Fecha
        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        
        fila1.add(new JLabel("Paciente:"));
        txtFiltroNombre = new JTextField(15);
        fila1.add(txtFiltroNombre);

        fila1.add(new JLabel("Cédula:"));
        txtFiltroCedula = new JTextField(10);
        fila1.add(txtFiltroCedula);

        fila1.add(new JLabel("Fecha Consulta:"));
        dateFiltroFecha = new JDateChooser();
        dateFiltroFecha.setDateFormatString("dd/MM/yyyy");
        dateFiltroFecha.setPreferredSize(new Dimension(120, 25));
        fila1.add(dateFiltroFecha);

        // Fila 2: Datos Clínicos
        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        
        fila2.add(new JLabel("Enfermedad Vigilada:"));
        cmbFiltroEnfermedad = new JComboBox<>();
        cmbFiltroEnfermedad.addItem(null); 
        cmbFiltroEnfermedad.setPreferredSize(new Dimension(150, 25));
        cargarEnfermedadesCombo();
        fila2.add(cmbFiltroEnfermedad);

        fila2.add(new JLabel("Vacuna Aplicada:"));
        cmbFiltroVacuna = new JComboBox<>();
        cmbFiltroVacuna.addItem(null); 
        cmbFiltroVacuna.setPreferredSize(new Dimension(150, 25));
        cargarVacunasCombo();
        fila2.add(cmbFiltroVacuna);

        JButton btnBuscar = new JButton("Aplicar Filtros");
        btnBuscar.setBackground(new Color(100, 149, 237));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.addActionListener(e -> filtrarYListar());
        fila2.add(btnBuscar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        fila2.add(btnLimpiar);

        KeyAdapter enterListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) filtrarYListar();
            }
        };
        txtFiltroNombre.addKeyListener(enterListener);
        txtFiltroCedula.addKeyListener(enterListener);

        panelNorte.add(fila1);
        panelNorte.add(fila2);
        
        add(panelNorte, BorderLayout.NORTH);
    }

    private void crearTablaResultados() {
        String[] columnas = {
            "Fecha", "Cédula Paciente", "Nombre Paciente", 
            "Médico", "Diagnóstico", "Enfermedad Vig.", "Vacuna", "Peso (kg)"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setRowHeight(25);
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(80); 
        tablaHistorial.getColumnModel().getColumn(4).setPreferredWidth(200); 

        JScrollPane scroll = new JScrollPane(tablaHistorial);
        add(scroll, BorderLayout.CENTER);
    }

    private void crearBotonera() {
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnVerDetalle = new JButton("Ver Detalle Completo");
        btnVerDetalle.setBackground(new Color(60, 179, 113));
        btnVerDetalle.setForeground(Color.WHITE);
        btnVerDetalle.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Acción conectada al método optimizado
        btnVerDetalle.addActionListener(e -> verDetalleSeleccionado());
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        panelSur.add(btnVerDetalle);
        panelSur.add(btnCerrar);
        add(panelSur, BorderLayout.SOUTH);
    }

    private void cargarEnfermedadesCombo() {
        ArrayList<Enfermedad> lista = controller.getEnfermedadesVigiladas();
        if(lista != null) {
            for(Enfermedad e : lista) cmbFiltroEnfermedad.addItem(e);
        }
    }

    private void cargarVacunasCombo() {
        ArrayList<Vacuna> lista = controller.getInventarioVacunas();
        if(lista != null) {
            for(Vacuna v : lista) cmbFiltroVacuna.addItem(v);
        }
    }

    // ================================================================
    // LÓGICA OPTIMIZADA CON LISTA PARALELA
    // ================================================================
    
    private void filtrarYListar() {
        modeloTabla.setRowCount(0);
        listaFiltrada.clear(); // ¡Importante! Limpiar la lista paralela
        
        ArrayList<Consulta> todas = controller.getConsultas();
        if (todas == null) return;

        // Captura de Filtros
        String fNombre = txtFiltroNombre.getText().toLowerCase().trim();
        String fCedula = txtFiltroCedula.getText().trim();
        Date fFecha = dateFiltroFecha.getDate();
        Enfermedad fEnfermedad = (Enfermedad) cmbFiltroEnfermedad.getSelectedItem();
        Vacuna fVacuna = (Vacuna) cmbFiltroVacuna.getSelectedItem();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaStringFiltro = (fFecha != null) ? sdf.format(fFecha) : null;

        // Identificar rol y médico
        Medico medicoLogueado = null;
        if (usuarioActual.getRol() == RolUsuario.MEDICO && usuarioActual.getPersonaAsociada() instanceof Medico) {
            medicoLogueado = (Medico) usuarioActual.getPersonaAsociada();
        }
        boolean esAdmin = (usuarioActual.getRol() == RolUsuario.ADMINISTRADOR);

        // Bucle de filtrado
        for (Consulta c : todas) {
            // 1. VISIBILIDAD
            boolean visible = false;
            if (esAdmin) {
                visible = true;
            } else if (medicoLogueado != null) {
                if (c.esVisiblePara(medicoLogueado)) visible = true;
            } else {
                // Secretarias/Otros: solo públicas o vigiladas
                if (c.isEsPublica() || c.getEnfermedadVigilada() != null) visible = true;
            }

            if (!visible) continue; 

            // 2. FILTROS
            boolean coincide = true;

            if (!fNombre.isEmpty() && !c.getPaciente().getNombre().toLowerCase().contains(fNombre)) {
                coincide = false;
            }
            if (coincide && !fCedula.isEmpty() && !c.getPaciente().getCedula().startsWith(fCedula)) {
                coincide = false;
            }
            if (coincide && fechaStringFiltro != null) {
                String fechaCita = sdf.format(c.getFechaConsulta());
                if (!fechaCita.equals(fechaStringFiltro)) coincide = false;
            }
            if (coincide && fEnfermedad != null) {
                if (c.getEnfermedadVigilada() == null || !c.getEnfermedadVigilada().equals(fEnfermedad)) {
                    coincide = false;
                }
            }
            if (coincide && fVacuna != null) {
                if (c.getVacunaAplicada() == null || !c.getVacunaAplicada().equals(fVacuna)) {
                    coincide = false;
                }
            }

            // 3. AGREGAR (A TABLA Y A LISTA PARALELA)
            if (coincide) {
                listaFiltrada.add(c); // <--- Aquí guardamos el objeto real en el mismo orden que la tabla

                String enfStr = (c.getEnfermedadVigilada() != null) ? c.getEnfermedadVigilada().getNombre() : "-";
                String vacStr = (c.getVacunaAplicada() != null) ? c.getVacunaAplicada().getNombre() : "-";

                Object[] fila = {
                    sdf.format(c.getFechaConsulta()),
                    c.getPaciente().getCedula(),
                    c.getPaciente().getNombre(),
                    c.getMedico().getNombre(),
                    c.getDiagnostico(),
                    enfStr,
                    vacStr,
                    c.getPesoRegistrado()
                };
                modeloTabla.addRow(fila);
            }
        }
    }

    private void limpiarFiltros() {
        txtFiltroNombre.setText("");
        txtFiltroCedula.setText("");
        dateFiltroFecha.setDate(null);
        cmbFiltroEnfermedad.setSelectedIndex(0);
        cmbFiltroVacuna.setSelectedIndex(0);
        filtrarYListar();
    }

    // --- MÉTODO OPTIMIZADO PARA VER DETALLE ---
    private void verDetalleSeleccionado() {
        int fila = tablaHistorial.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para ver detalles.");
            return;
        }
        
        // Obtener el objeto DIRECTAMENTE desde la lista paralela
        // Como ambos se llenan en el mismo bucle 'for', los índices coinciden.
        Consulta consultaSeleccionada = listaFiltrada.get(fila);
        
        // Abrir la ventana de detalle que creamos antes
        VerDetalleConsultaDialog dialogo = new VerDetalleConsultaDialog(this, consultaSeleccionada);
        dialogo.setVisible(true);
    }
}