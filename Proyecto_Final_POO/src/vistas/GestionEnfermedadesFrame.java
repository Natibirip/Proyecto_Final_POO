package vistas;

import controlador.ClinicaControladora;
import modelos.Enfermedad;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GestionEnfermedadesFrame extends JFrame {

    private JTable tablaEnfermedades;
    private DefaultTableModel modeloTabla;
    
    // Filtros
    private JTextField txtBuscar;
    private JCheckBox chkSoloVigiladas;
    private JCheckBox chkSoloActivas;

    // Lista paralela para manejar el filtrado
    private ArrayList<Enfermedad> listaFiltrada;

    public GestionEnfermedadesFrame() {
        listaFiltrada = new ArrayList<>();
        
        setTitle("Gestión de Enfermedades y Vigilancia Epidemiológica");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. PANEL SUPERIOR (TÍTULO Y FILTROS)
        crearPanelSuperior();

        // 2. PANEL CENTRAL (TABLA)
        crearTabla();

        // 3. PANEL INFERIOR (BOTONES)
        crearBotonera();

        // Carga inicial
        cargarTabla();
    }

    private void crearPanelSuperior() {
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(70, 130, 180)); // Azul acero
        panelNorte.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("CATÁLOGO DE ENFERMEDADES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelNorte.add(lblTitulo, BorderLayout.WEST);

        // Panel de Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelFiltros.setOpaque(false);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(Color.WHITE);
        lblBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        
        txtBuscar = new JTextField(20);
        txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { cargarTabla(); }
        });

        chkSoloVigiladas = new JCheckBox("Solo Vigiladas");
        chkSoloVigiladas.setOpaque(false);
        chkSoloVigiladas.setForeground(Color.WHITE);
        chkSoloVigiladas.setFont(new Font("Arial", Font.BOLD, 12));
        chkSoloVigiladas.addActionListener(e -> cargarTabla());

        chkSoloActivas = new JCheckBox("Solo Activas");
        chkSoloActivas.setOpaque(false);
        chkSoloActivas.setForeground(Color.WHITE);
        chkSoloActivas.setSelected(true); // Por defecto ver solo activas
        chkSoloActivas.addActionListener(e -> cargarTabla());

        panelFiltros.add(lblBuscar);
        panelFiltros.add(txtBuscar);
        panelFiltros.add(chkSoloVigiladas);
        panelFiltros.add(chkSoloActivas);

        panelNorte.add(panelFiltros, BorderLayout.EAST);
        add(panelNorte, BorderLayout.NORTH);
    }

    private void crearTabla() {
        String[] columnas = {"Código", "Nombre", "Vigilancia", "Descripción", "Estado"};
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tablaEnfermedades = new JTable(modeloTabla);
        tablaEnfermedades.setRowHeight(25);
        tablaEnfermedades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Renderizado personalizado para resaltar VIGILADAS
        tablaEnfermedades.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String vigilancia = table.getValueAt(row, 2).toString(); // Columna "Vigilancia"
                String estado = table.getValueAt(row, 4).toString();     // Columna "Estado"

                if (!isSelected) {
                    // Colores por defecto
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);

                    // 1. Si está inactiva -> Gris
                    if ("Inactiva".equals(estado)) {
                        c.setForeground(Color.GRAY);
                    } 
                    // 2. Si es VIGILADA -> Rojo y Negrita (Prioridad sobre gris)
                    else if ("SÍ - ALERTA".equals(vigilancia)) {
                        c.setForeground(new Color(178, 34, 34)); // Firebrick Red
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    }
                }
                return c;
            }
        });

        // Doble clic para editar
        tablaEnfermedades.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editarSeleccionada();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaEnfermedades);
        add(scroll, BorderLayout.CENTER);
    }

    private void crearBotonera() {
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        JButton btnNueva = new JButton("Nueva Enfermedad");
        JButton btnEditar = new JButton("Modificar Datos");
        JButton btnEstado = new JButton("Cambiar Estado");
        JButton btnCerrar = new JButton("Cerrar");

        configurarBoton(btnNueva, new Color(46, 139, 87)); // Verde
        configurarBoton(btnEditar, new Color(255, 140, 0)); // Naranja
        configurarBoton(btnEstado, Color.GRAY);
        configurarBoton(btnCerrar, new Color(178, 34, 34)); // Rojo

        btnNueva.addActionListener(e -> abrirDialogo(null));
        btnEditar.addActionListener(e -> editarSeleccionada());
        btnEstado.addActionListener(e -> cambiarEstado());
        btnCerrar.addActionListener(e -> dispose());

        panelSur.add(btnNueva);
        panelSur.add(btnEditar);
        panelSur.add(btnEstado);
        panelSur.add(btnCerrar);

        add(panelSur, BorderLayout.SOUTH);
    }

    private void configurarBoton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(200, 45));
    }

    // ==========================================
    // LÓGICA DE NEGOCIO
    // ==========================================

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        listaFiltrada.clear();
        
        String filtroTexto = txtBuscar.getText().toLowerCase().trim();
        boolean soloVigiladas = chkSoloVigiladas.isSelected();
        boolean soloActivas = chkSoloActivas.isSelected();

        // Usamos el método que agregamos al controlador en pasos anteriores
        ArrayList<Enfermedad> todas = ClinicaControladora.getInstance().getEnfermedadesVigiladas(); // Asumiendo que esta lista tiene TODAS

        if (todas != null) {
            for (Enfermedad e : todas) {
                boolean pasaFiltro = true;

                // 1. Filtro Texto
                if (!filtroTexto.isEmpty()) {
                    if (!e.getNombre().toLowerCase().contains(filtroTexto) && 
                        !e.getCodigo().toLowerCase().contains(filtroTexto)) {
                        pasaFiltro = false;
                    }
                }

                // 2. Filtro Vigilancia
                if (soloVigiladas && !e.isEsVigilada()) {
                    pasaFiltro = false;
                }

                // 3. Filtro Activa
                if (soloActivas && !e.isActiva()) {
                    pasaFiltro = false;
                }

                // AGREGAR SI PASA TODO
                if (pasaFiltro) {
                    listaFiltrada.add(e);
                    
                    modeloTabla.addRow(new Object[]{
                        e.getCodigo(),
                        e.getNombre(),
                        e.isEsVigilada() ? "SÍ - ALERTA" : "No",
                        e.getDescripcion(),
                        e.isActiva() ? "Activa" : "Inactiva"
                    });
                }
            }
        }
    }

    private void abrirDialogo(Enfermedad enfermedadEditar) {
        RegEnfermedadDialog dialog = new RegEnfermedadDialog(this, enfermedadEditar);
        dialog.setVisible(true);
        cargarTabla(); // Refrescar al volver
    }

    private void editarSeleccionada() {
        int fila = tablaEnfermedades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una enfermedad.");
            return;
        }
        Enfermedad e = listaFiltrada.get(fila);
        abrirDialogo(e);
    }

    private void cambiarEstado() {
        int fila = tablaEnfermedades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una enfermedad.");
            return;
        }
        
        Enfermedad e = listaFiltrada.get(fila);
        String nuevoEstado = e.isActiva() ? "DESACTIVAR" : "ACTIVAR";
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Seguro que desea " + nuevoEstado + " la enfermedad '" + e.getNombre() + "'?",
            "Cambiar Estado", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (e.isActiva()) e.desactivar(); else e.activar();
            
            ClinicaControladora.getInstance().guardarDatos();
            cargarTabla();
        }
    }

    // =================================================================
    // CLASE INTERNA: DIÁLOGO DE REGISTRO/EDICIÓN
    // =================================================================
    class RegEnfermedadDialog extends JDialog {
        
        private JTextField txtCodigo, txtNombre;
        private JTextArea txtDescripcion;
        private JCheckBox chkVigilancia;
        private Enfermedad enfermedadEdicion;

        public RegEnfermedadDialog(Frame parent, Enfermedad enfermedad) {
            super(parent, enfermedad == null ? "Nueva Enfermedad" : "Editar Enfermedad", true);
            this.enfermedadEdicion = enfermedad;
            setSize(450, 450);
            setLocationRelativeTo(parent);
            setLayout(null);
            setResizable(false);
            
            initUI();
            if (enfermedad != null) cargarDatos();
        }

        private void initUI() {
            int x = 30, y = 20, w = 370, h = 25, gap = 55;

            // Código
            add(lbl("Código (Único):", x, y));
            txtCodigo = new JTextField(); 
            txtCodigo.setBounds(x, y + 20, 150, h); 
            add(txtCodigo);

            y += gap;
            // Nombre
            add(lbl("Nombre de la Enfermedad:", x, y));
            txtNombre = new JTextField(); 
            txtNombre.setBounds(x, y + 20, w, h); 
            add(txtNombre);

            y += gap;
            // Vigilancia (DESTACADO)
            chkVigilancia = new JCheckBox("BAJO VIGILANCIA EPIDEMIOLÓGICA");
            chkVigilancia.setForeground(Color.RED);
            chkVigilancia.setFont(new Font("Arial", Font.BOLD, 12));
            chkVigilancia.setBounds(x, y + 10, w, h);
            add(chkVigilancia);
            
            JLabel lblNota = new JLabel("<html><small>* Las consultas con esta enfermedad serán públicas<br>automáticamente para todo el personal médico.</small></html>");
            lblNota.setBounds(x, y + 35, w, 30);
            lblNota.setForeground(Color.GRAY);
            add(lblNota);

            y += gap + 20;
            // Descripción
            add(lbl("Descripción / Protocolo:", x, y));
            txtDescripcion = new JTextArea();
            txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            txtDescripcion.setLineWrap(true);
            JScrollPane scroll = new JScrollPane(txtDescripcion);
            scroll.setBounds(x, y + 20, w, 80);
            add(scroll);

            // Botón Guardar
            JButton btnGuardar = new JButton("Guardar Datos");
            btnGuardar.setBackground(new Color(46, 139, 87));
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
            btnGuardar.setBounds(x, 350, w, 45);
            btnGuardar.addActionListener(e -> guardar());
            add(btnGuardar);
        }

        private JLabel lbl(String t, int x, int y) {
            JLabel l = new JLabel(t);
            l.setBounds(x, y, 200, 20);
            return l;
        }

        private void cargarDatos() {
            txtCodigo.setText(enfermedadEdicion.getCodigo());
            txtCodigo.setEditable(false); // ID no editable
            txtCodigo.setBackground(new Color(230, 230, 230));
            
            txtNombre.setText(enfermedadEdicion.getNombre());
            txtDescripcion.setText(enfermedadEdicion.getDescripcion());
            chkVigilancia.setSelected(enfermedadEdicion.isEsVigilada());
        }

        private void guardar() {
            String cod = txtCodigo.getText().trim();
            String nom = txtNombre.getText().trim();
            String desc = txtDescripcion.getText().trim();
            boolean vigilada = chkVigilancia.isSelected();

            if (cod.isEmpty() || nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código y Nombre son obligatorios.");
                return;
            }

            ClinicaControladora ctrl = ClinicaControladora.getInstance();

            if (enfermedadEdicion == null) {
                // NUEVA
                Enfermedad nueva = new Enfermedad(cod, nom, vigilada, desc);
                if (ctrl.agregarEnfermedad(nueva)) {
                    ctrl.guardarDatos();
                    JOptionPane.showMessageDialog(this, "Enfermedad registrada correctamente.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: El código ya existe.");
                }
            } else {
                // EDITAR
                enfermedadEdicion.setNombre(nom);
                enfermedadEdicion.setDescripcion(desc);
                enfermedadEdicion.setEsVigilada(vigilada);
                
                ctrl.guardarDatos();
                JOptionPane.showMessageDialog(this, "Datos actualizados.");
                dispose();
            }
        }
    }
}