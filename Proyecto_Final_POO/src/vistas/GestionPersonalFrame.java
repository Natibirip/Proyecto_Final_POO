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
    private JComboBox<String> cmbFiltroTipo; // Variable de clase

    public GestionPersonalFrame() {
        setTitle("Gestión de Personal Médico y Administrativo");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // --- BARRA SUPERIOR (Filtro) ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Filtrar por:"));
        
        // CORRECCIÓN 1: Inicializamos la variable de clase, NO creamos una nueva
        cmbFiltroTipo = new JComboBox<>();
        // Aseguramos que los textos coincidan con los IF de abajo ("Médicos", "Secretarias")
        cmbFiltroTipo.setModel(new DefaultComboBoxModel<>(new String[] {"Todos", "Médicos", "Secretarias"}));
        
        JButton btnFiltrar = new JButton("Actualizar Lista");
        btnFiltrar.addActionListener(e -> cargarTabla());
        
        panelSuperior.add(cmbFiltroTipo);
        panelSuperior.add(btnFiltrar);
        
        getContentPane().add(panelSuperior, BorderLayout.NORTH);

        // --- TABLA ---
        String[] columnas = {"Tipo", "Cédula", "Nombre", "Teléfono", "Dato Extra (Especialidad/Puesto)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaPersonal = new JTable(modeloTabla);
        getContentPane().add(new JScrollPane(tablaPersonal), BorderLayout.CENTER);

        // --- BOTONES ---
        JPanel panelBotones = new JPanel();
        JButton btnNuevo = new JButton("Registrar Nuevo Personal");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        JButton btnCerrar = new JButton("Cerrar");

        btnNuevo.setBackground(new Color(144, 238, 144));

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        // --- ACCIONES ---
        btnCerrar.addActionListener(e -> dispose());
        
        btnNuevo.addActionListener(e -> {
            RegPersonalDialog dialogo = new RegPersonalDialog(this);
            dialogo.setVisible(true);
            cargarTabla(); 
        });

        cargarTabla();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0); // Limpiar la tabla
        ClinicaControladora control = ClinicaControladora.getInstance();
        
        // Validación de seguridad para el filtro
        String filtro = (String) cmbFiltroTipo.getSelectedItem();
        if (filtro == null) filtro = "Todos";

      

        // 1. Cargar Médicos
        if (filtro.equals("Todos") || filtro.equals("Médicos")) {
            ArrayList<Medico> listaMedicos = control.getMedicos();
            // Solo entramos al bucle si la lista NO es null y NO está vacía
            if (listaMedicos != null && !listaMedicos.isEmpty()) {
                for (Medico m : listaMedicos) { 
                    modeloTabla.addRow(new Object[]{
                        "MÉDICO", m.getCedula(), m.getNombre(), m.getTelefono(), m.getEspecialidad()
                    });
                }
            }
        }

        // 2. Cargar Secretarias
        if (filtro.equals("Todos") || filtro.equals("Secretarias")) {
            ArrayList<Secretaria> listaSecretarias = control.getSecretarias();
            // Solo entramos al bucle si la lista NO es null y NO está vacía
            if (listaSecretarias != null && !listaSecretarias.isEmpty()) {
                for (Secretaria s : listaSecretarias) { 
                    modeloTabla.addRow(new Object[]{
                        "SECRETARIA", s.getCedula(), s.getNombre(), s.getTelefono(), "Administrativo"
                    });
                }
            }
        }
    }
}