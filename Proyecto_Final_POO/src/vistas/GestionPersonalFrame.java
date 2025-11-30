package vistas;

import controlador.ClinicaControladora;
import modelos.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GestionPersonalFrame extends JFrame {

    private JTable tablaPersonal;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cmbFiltroTipo; // Para ver "Todos", "Medicos", "Secretarias"

    public GestionPersonalFrame() {
        setTitle("Gestión de Personal Médico y Administrativo");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- BARRA SUPERIOR (Filtro) ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Filtrar por:"));
        cmbFiltroTipo = new JComboBox<>(new String[]{"Todos", "Médicos", "Secretarias"});
        panelSuperior.add(cmbFiltroTipo);
        
        JButton btnFiltrar = new JButton("Actualizar Lista");
        btnFiltrar.addActionListener(e -> cargarTabla());
        panelSuperior.add(btnFiltrar);
        
        add(panelSuperior, BorderLayout.NORTH);

        // --- TABLA ---
        String[] columnas = {"Tipo", "Cédula", "Nombre", "Teléfono", "Dato Extra (Especialidad/Puesto)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaPersonal = new JTable(modeloTabla);
        add(new JScrollPane(tablaPersonal), BorderLayout.CENTER);

        // --- BOTONES ---
        JPanel panelBotones = new JPanel();
        JButton btnNuevo = new JButton("Registrar Nuevo Personal");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        JButton btnCerrar = new JButton("Cerrar");

        btnNuevo.setBackground(new Color(144, 238, 144));

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        // --- ACCIONES ---
        btnCerrar.addActionListener(e -> dispose());
        
        btnNuevo.addActionListener(e -> {
            // Abrir el diálogo de registro
            RegPersonalDialog dialogo = new RegPersonalDialog(this);
            dialogo.setVisible(true);
            cargarTabla(); // Refrescar al volver
        });

        // Cargar datos iniciales
        cargarTabla();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        ClinicaControladora control = ClinicaControladora.getInstance();
        String filtro = (String) cmbFiltroTipo.getSelectedItem();

        // 1. Cargar Médicos
        if (filtro.equals("Todos") || filtro.equals("Médicos")) {
            for (Medico m : control.getMedicos()) { 
                modeloTabla.addRow(new Object[]{
                    "MÉDICO", m.getCedula(), m.getNombre(), m.getTelefono(), m.getEspecialidad()
                });
            }
        }

        // 2. Cargar Secretarias
        if (filtro.equals("Todos") || filtro.equals("Secretarias")) {
            for (Secretaria s : control.getSecretarias()) { 
                modeloTabla.addRow(new Object[]{
                    "SECRETARIA", s.getCedula(), s.getNombre(), s.getTelefono(), "Administrativo"
                });
            }
        }
    }
}