package vistas;

import controlador.ClinicaControladora;
import modelos.Vacuna;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GestionVacunasFrame extends JFrame {

    private JTable tablaVacunas;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    
    // Lista auxiliar para manejar el filtrado y obtener el objeto correcto al seleccionar
    private ArrayList<Vacuna> listaFiltrada;

    public GestionVacunasFrame() {
        listaFiltrada = new ArrayList<>();
        
        setTitle("Gestión de Inventario de Vacunas");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. PANEL SUPERIOR (Buscador y Título)
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(0, 102, 102)); // Verde azulado oscuro
        panelNorte.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("CONTROL DE STOCK E INVENTARIO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelNorte.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBuscar.setOpaque(false);
        
        JLabel lblBuscar = new JLabel("Filtrar (Nombre/Código):");
        lblBuscar.setForeground(Color.WHITE);
        lblBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Evento: Filtrar mientras se escribe
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cargarTabla();
            }
        });

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtBuscar);
        panelNorte.add(panelBuscar, BorderLayout.EAST);

        add(panelNorte, BorderLayout.NORTH);

        // 2. TABLA CENTRAL
        crearTabla();

        // 3. PANEL INFERIOR (Botones de Acción)
        crearBotonera();

        // Carga inicial de datos
        cargarTabla();
    }

    private void crearTabla() {
        String[] columnas = {"Código", "Nombre Vacuna", "Lote", "Descripción", "Stock Actual", "Estado"};
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tablaVacunas = new JTable(modeloTabla);
        tablaVacunas.setRowHeight(30);
        tablaVacunas.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaVacunas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaVacunas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Evento doble click para editar
        tablaVacunas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarSeleccionada();
                }
            }
        });

        // Renderizado condicional (Pintar rojo si hay poco stock)
        tablaVacunas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                try {
                    int stock = Integer.parseInt(table.getValueAt(row, 4).toString());
                    String estado = table.getValueAt(row, 5).toString();

                    if (!isSelected) {
                        if ("Inactiva".equals(estado)) {
                            c.setBackground(new Color(240, 240, 240)); 
                            c.setForeground(Color.GRAY);
                        } else if (stock <= 5) {
                            c.setBackground(new Color(255, 220, 220)); // Rojo suave (Alerta)
                            c.setForeground(Color.RED);
                        } else {
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                        }
                    }
                } catch (Exception e) {
                    // Ignorar errores de parseo
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tablaVacunas);
        add(scroll, BorderLayout.CENTER);
    }

    private void crearBotonera() {
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelSur.setBackground(new Color(230, 230, 230));

        JButton btnNueva = crearBoton("Nueva Vacuna", new Color(46, 139, 87)); // Verde
        JButton btnEditar = crearBoton("Modificar Datos", new Color(70, 130, 180)); // Azul
        JButton btnStock = crearBoton("📦 Entrada Stock", new Color(255, 140, 0)); // Naranja
        JButton btnEliminar = crearBoton("Eliminar Vacuna", new Color(178, 34, 34)); // Rojo
        JButton btnCerrar = crearBoton("Cerrar", Color.GRAY);

        btnNueva.addActionListener(e -> abrirDialogoVacuna(null));
        btnEditar.addActionListener(e -> editarSeleccionada());
        btnStock.addActionListener(e -> gestionarStock());
        btnEliminar.addActionListener(e -> eliminarSeleccionada());
        btnCerrar.addActionListener(e -> dispose());

        panelSur.add(btnNueva);
        panelSur.add(btnEditar);
        panelSur.add(btnStock);
        panelSur.add(btnEliminar);
        panelSur.add(btnCerrar);

        add(panelSur, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(170, 45));
        return btn;
    }

    // ==========================================
    // LÓGICA DE NEGOCIO
    // ==========================================

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        listaFiltrada.clear(); // Limpiar lista paralela
        
        String filtro = txtBuscar.getText().toLowerCase().trim();
        ArrayList<Vacuna> todas = ClinicaControladora.getInstance().getInventarioVacunas();

        if (todas != null) {
            for (Vacuna v : todas) {
                // Filtro por Nombre o Código
                if (filtro.isEmpty() || 
                    v.getNombre().toLowerCase().contains(filtro) || 
                    v.getCodigo().toLowerCase().contains(filtro)) {
                    
                    listaFiltrada.add(v); // Agregar a la lista sincronizada
                    
                    modeloTabla.addRow(new Object[]{
                        v.getCodigo(),
                        v.getNombre(),
                        v.getLote(),
                        v.getDescripcion(),
                        v.getCantidad(),
                        v.isActiva() ? "Disponible" : "Inactiva"
                    });
                }
            }
        }
    }

    private void abrirDialogoVacuna(Vacuna vacunaAEditar) {
        RegVacunaDialog dialog = new RegVacunaDialog(this, vacunaAEditar);
        dialog.setVisible(true);
        cargarTabla(); // Refrescar al volver
    }

    private void editarSeleccionada() {
        int fila = tablaVacunas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una vacuna para modificar.");
            return;
        }
        
        // Obtener el objeto real desde la lista filtrada
        Vacuna v = listaFiltrada.get(fila);
        abrirDialogoVacuna(v);
    }

    private void eliminarSeleccionada() {
        int fila = tablaVacunas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una vacuna para eliminar.");
            return;
        }

        Vacuna v = listaFiltrada.get(fila);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea ELIMINAR la vacuna '" + v.getNombre() + "'?\n" +
            "Esta acción borrará el registro del inventario permanentemente.",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            ClinicaControladora.getInstance().eliminarVacuna(v.getCodigo());
            ClinicaControladora.getInstance().guardarDatos(); // Asegurar persistencia
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Vacuna eliminada correctamente.");
        }
    }

    private void gestionarStock() {
        int fila = tablaVacunas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una vacuna para agregar stock.");
            return;
        }
        
        Vacuna v = listaFiltrada.get(fila);
        
        String input = JOptionPane.showInputDialog(this, 
            "Gestión de Stock para: " + v.getNombre() + "\n" +
            "Lote: " + v.getLote() + "\n" +
            "Stock Actual: " + v.getCantidad() + "\n\n" +
            "Ingrese cantidad a AGREGAR (Ej: 50):", 
            "Entrada de Almacén", JOptionPane.QUESTION_MESSAGE);
        
        try {
            if (input != null && !input.isEmpty()) {
                int cantidad = Integer.parseInt(input);
                if (cantidad > 0) {
                    v.agregarStock(cantidad);
                    ClinicaControladora.getInstance().guardarDatos();
                    cargarTabla();
                    JOptionPane.showMessageDialog(this, "Stock actualizado. Nuevo total: " + v.getCantidad());
                } else {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Ingrese un número entero válido.");
        }
    }

    // =================================================================
    // CLASE INTERNA: DIÁLOGO DE REGISTRO/EDICIÓN
    // =================================================================
    class RegVacunaDialog extends JDialog {
        
        private JTextField txtCodigo, txtNombre, txtLote, txtStock;
        private JTextArea txtDesc;
        private JCheckBox chkActiva;
        private Vacuna vacunaEdicion;

        public RegVacunaDialog(Frame parent, Vacuna vacuna) {
            super(parent, vacuna == null ? "Nueva Vacuna" : "Modificar Vacuna", true);
            this.vacunaEdicion = vacuna;
            setSize(420, 550);
            setLocationRelativeTo(parent);
            setLayout(null);
            setResizable(false);
            
            initUI();
            if(vacuna != null) cargarDatos();
        }

        private void initUI() {
            int x=30, y=20, w=340, h=25, gap=55;

            add(lbl("Código (Único):", x, y));
            txtCodigo = new JTextField(); txtCodigo.setBounds(x, y+20, w, h); add(txtCodigo);

            y+=gap;
            add(lbl("Nombre Comercial:", x, y));
            txtNombre = new JTextField(); txtNombre.setBounds(x, y+20, w, h); add(txtNombre);

            y+=gap;
            add(lbl("Lote de Fabricación:", x, y));
            txtLote = new JTextField(); txtLote.setBounds(x, y+20, w, h); add(txtLote);

            y+=gap;
            add(lbl("Stock Inicial:", x, y));
            txtStock = new JTextField("0"); txtStock.setBounds(x, y+20, 100, h); add(txtStock);
            
            chkActiva = new JCheckBox("Vacuna Disponible (Activa)");
            chkActiva.setSelected(true);
            chkActiva.setBounds(x+120, y+20, 200, h);
            add(chkActiva);

            y+=gap;
            add(lbl("Descripción / Notas:", x, y));
            txtDesc = new JTextArea(); txtDesc.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            txtDesc.setLineWrap(true);
            JScrollPane scroll = new JScrollPane(txtDesc);
            scroll.setBounds(x, y+20, w, 80); add(scroll);

            JButton btnGuardar = new JButton("Guardar Datos");
            btnGuardar.setBackground(new Color(46, 139, 87));
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
            btnGuardar.setBounds(x, 430, w, 45);
            btnGuardar.addActionListener(e -> guardar());
            add(btnGuardar);
        }

        private JLabel lbl(String t, int x, int y) {
            JLabel l = new JLabel(t); l.setBounds(x, y, 200, 20); return l;
        }

        private void cargarDatos() {
            txtCodigo.setText(vacunaEdicion.getCodigo());
            txtCodigo.setEditable(false); // No cambiar código al editar (Llave primaria)
            txtCodigo.setBackground(new Color(230, 230, 230));
            
            txtNombre.setText(vacunaEdicion.getNombre());
            txtLote.setText(vacunaEdicion.getLote());
            txtDesc.setText(vacunaEdicion.getDescripcion());
            txtStock.setText(String.valueOf(vacunaEdicion.getCantidad()));
            txtStock.setEditable(false); // Stock se gestiona con el botón de "Entrada"
            txtStock.setBackground(new Color(230, 230, 230));
            
            chkActiva.setSelected(vacunaEdicion.isActiva());
        }

        private void guardar() {
            String cod = txtCodigo.getText().trim();
            String nom = txtNombre.getText().trim();
            String lot = txtLote.getText().trim();
            String des = txtDesc.getText().trim();
            
            if (cod.isEmpty() || nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código y Nombre son obligatorios.");
                return;
            }

            ClinicaControladora ctrl = ClinicaControladora.getInstance();

            if (vacunaEdicion == null) {
                // MODO CREAR
                try {
                    int cant = Integer.parseInt(txtStock.getText());
                    Vacuna v = new Vacuna(cod, nom, des, lot, cant);
                    v.setActiva(chkActiva.isSelected());
                    
                    if(ctrl.agregarVacuna(v)) {
                        JOptionPane.showMessageDialog(this, "Vacuna registrada exitosamente.");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error: Ya existe una vacuna con ese código.");
                    }
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero.");
                }
            } else {
                // MODO MODIFICAR
                vacunaEdicion.setNombre(nom);
                vacunaEdicion.setLote(lot);
                vacunaEdicion.setDescripcion(des);
                vacunaEdicion.setActiva(chkActiva.isSelected());
                
                // Guardamos los cambios en el archivo
                ctrl.guardarDatos();
                
                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
                dispose();
            }
        }
    }
}