package vistas;

import controlador.ClinicaControladora;
import modelos.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BuscarPacientesFrame extends JFrame {

    private JTextField txtBuscar;
    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;
    private ArrayList<Paciente> listaFiltrada; // Para mantener referencia a los objetos actuales en la tabla

    public BuscarPacientesFrame() {
        setTitle("Directorio de Pacientes - Búsqueda y Edición");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla Completa
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        listaFiltrada = new ArrayList<>();

        // 1. PANEL SUPERIOR (BUSCADOR)
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelNorte.setBackground(new Color(70, 130, 180)); // Azul acero

        JLabel lblBuscar = new JLabel("Buscar (Nombre o Cédula):");
        lblBuscar.setForeground(Color.WHITE);
        lblBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        
        txtBuscar = new JTextField(30);
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Evento: Buscar mientras escribe
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarPacientes();
            }
        });

        JButton btnLimpiar = new JButton("Limpiar Filtro");
        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            filtrarPacientes();
        });

        panelNorte.add(lblBuscar);
        panelNorte.add(txtBuscar);
        panelNorte.add(btnLimpiar);
        
        add(panelNorte, BorderLayout.NORTH);

        // 2. PANEL CENTRAL (TABLA)
        String[] columnas = {"Cédula", "Nombre", "Teléfono", "Edad", "Sangre", "Peso (kg)", "Altura (m)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura, la edición es por botón
            }
        };

        tablaPacientes = new JTable(modeloTabla);
        tablaPacientes.setRowHeight(25);
        tablaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPacientes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Doble clic para editar rápido
        tablaPacientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirEditor();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tablaPacientes);
        add(scroll, BorderLayout.CENTER);

        // 3. PANEL INFERIOR (BOTONES)
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnEditar = new JButton("Modificar Datos del Paciente");
        btnEditar.setBackground(new Color(255, 140, 0)); // Naranja
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEditar.setPreferredSize(new Dimension(250, 40));
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setPreferredSize(new Dimension(100, 40));

        btnEditar.addActionListener(e -> abrirEditor());
        btnCerrar.addActionListener(e -> dispose());

        panelSur.add(btnEditar);
        panelSur.add(btnCerrar);
        
        add(panelSur, BorderLayout.SOUTH);

        // Carga inicial
        filtrarPacientes();
    }

    private void filtrarPacientes() {
        modeloTabla.setRowCount(0);
        listaFiltrada.clear();
        
        String texto = txtBuscar.getText().toLowerCase().trim();
        ArrayList<Paciente> todos = ClinicaControladora.getInstance().getPacientes();

        if (todos != null) {
            for (Paciente p : todos) {
                boolean coincide = false;
                // Si no hay texto, mostramos todos
                if (texto.isEmpty()) {
                    coincide = true;
                } else {
                    // Buscar por nombre O cédula
                    if (p.getNombre().toLowerCase().contains(texto) || 
                        p.getCedula().contains(texto)) {
                        coincide = true;
                    }
                }

                if (coincide) {
                    listaFiltrada.add(p); // Guardamos referencia para saber cuál editar luego
                    modeloTabla.addRow(new Object[]{
                        p.getCedula(),
                        p.getNombre(),
                        p.getTelefono(),
                        p.getEdad(),
                        p.getTipoSangre(),
                        p.getPeso(),
                        p.getEstatura()
                    });
                }
            }
        }
    }

    private void abrirEditor() {
        int fila = tablaPacientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente de la lista para modificar.");
            return;
        }

        // Obtenemos el objeto real desde nuestra lista sincronizada
        Paciente pacienteSeleccionado = listaFiltrada.get(fila);

        // Abrimos el diálogo modal de edición
        DialogoEditarPaciente dialogo = new DialogoEditarPaciente(this, pacienteSeleccionado);
        dialogo.setVisible(true);

        // Al cerrar el diálogo, refrescamos la tabla para ver los cambios
        filtrarPacientes();
    }

    // =========================================================================
    // CLASE INTERNA: DIÁLOGO DE EDICIÓN (Para mantener todo en un archivo)
    // =========================================================================
    class DialogoEditarPaciente extends JDialog {
        
        private Paciente paciente;
        private JTextField txtTelefono, txtPeso, txtEstatura;

        public DialogoEditarPaciente(JFrame parent, Paciente paciente) {
            super(parent, "Modificar Paciente: " + paciente.getNombre(), true);
            this.paciente = paciente;
            setSize(400, 450);
            setLocationRelativeTo(parent);
            setLayout(null);
            setResizable(false);

            inicializarFormulario();
        }

        private void inicializarFormulario() {
            int y = 20;
            int h = 25;
            int gap = 45;

            // --- CAMPOS NO EDITABLES (Solo lectura) ---
            
            add(crearLabel("Nombre Completo:", 20, y));
            JTextField txtNombre = new JTextField(paciente.getNombre());
            txtNombre.setBounds(150, y, 200, h);
            txtNombre.setEditable(false); // BLOQUEADO
            txtNombre.setBackground(new Color(230, 230, 230));
            add(txtNombre);

            y += gap;
            add(crearLabel("Cédula:", 20, y));
            JTextField txtCedula = new JTextField(paciente.getCedula());
            txtCedula.setBounds(150, y, 200, h);
            txtCedula.setEditable(false); // BLOQUEADO
            txtCedula.setBackground(new Color(230, 230, 230));
            add(txtCedula);

            y += gap;
            add(crearLabel("Fecha Nac.:", 20, y));
            // Asumiendo que tienes un método getFechaNacimiento o similar
            // Si no tienes formateador a mano, usa toString simple
            String fechaStr = (paciente.getFechaNacimiento() != null) ? paciente.getFechaNacimiento().toString() : "N/A";
            try { 
                // Intento simple de formato si es posible
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                fechaStr = sdf.format(paciente.getFechaNacimiento());
            } catch(Exception e) {}
            
            JTextField txtFecha = new JTextField(fechaStr);
            txtFecha.setBounds(150, y, 200, h);
            txtFecha.setEditable(false); // BLOQUEADO
            txtFecha.setBackground(new Color(230, 230, 230));
            add(txtFecha);

            // --- SEPARADOR ---
            y += gap;
            JSeparator sep = new JSeparator();
            sep.setBounds(20, y, 340, 5);
            add(sep);
            y += 15;

            JLabel lblTituloEdit = new JLabel("Datos Editables");
            lblTituloEdit.setFont(new Font("Arial", Font.BOLD, 12));
            lblTituloEdit.setForeground(Color.BLUE);
            lblTituloEdit.setBounds(20, y - 15, 150, 20);
            add(lblTituloEdit);

            // --- CAMPOS EDITABLES ---

            // Teléfono
            add(crearLabel("Teléfono:", 20, y));
            txtTelefono = new JTextField(paciente.getTelefono());
            txtTelefono.setBounds(150, y, 200, h);
            add(txtTelefono);

            y += gap;
            // Peso
            add(crearLabel("Peso (kg):", 20, y));
            txtPeso = new JTextField(String.valueOf(paciente.getPeso()));
            txtPeso.setBounds(150, y, 100, h);
            add(txtPeso);

            y += gap;
            // Estatura
            add(crearLabel("Estatura (m):", 20, y));
            txtEstatura = new JTextField(String.valueOf(paciente.getEstatura()));
            txtEstatura.setBounds(150, y, 100, h);
            add(txtEstatura);

            // --- BOTONES ---
            y += gap + 10;
            JButton btnGuardar = new JButton("Guardar Cambios");
            btnGuardar.setBackground(new Color(60, 179, 113));
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setBounds(50, y, 140, 35);
            
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setBounds(210, y, 100, 35);

            btnGuardar.addActionListener(e -> guardarCambios());
            btnCancelar.addActionListener(e -> dispose());

            add(btnGuardar);
            add(btnCancelar);
        }

        private JLabel crearLabel(String texto, int x, int y) {
            JLabel lbl = new JLabel(texto);
            lbl.setBounds(x, y, 120, 25);
            return lbl;
        }

        private void guardarCambios() {
            try {
                String nuevoTel = txtTelefono.getText().trim();
                double nuevoPeso = Double.parseDouble(txtPeso.getText().trim());
                double nuevaEstatura = Double.parseDouble(txtEstatura.getText().trim());

                if (nuevoTel.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El teléfono no puede estar vacío.");
                    return;
                }

                // Actualizar Objeto
                paciente.setTelefono(nuevoTel);
                paciente.setPeso(nuevoPeso);
                paciente.setEstatura(nuevaEstatura);

                // Persistir cambios
                ClinicaControladora.getInstance().guardarDatos();

                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
                dispose();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error: Peso y Estatura deben ser números válidos (use punto decimal).");
            }
        }
    }
}