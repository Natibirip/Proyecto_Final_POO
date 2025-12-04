package vistas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableModel;
import controlador.ClinicaControladora;
import modelos.Enfermedad;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.awt.Dimension;
import java.awt.Toolkit;

public class RegEnfermedades extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTable tableEnfermedades;
    private DefaultTableModel modelEnfermedades;
    private JCheckBox chkVigilada;

    public static void main(String[] args) {
        try {
            RegEnfermedades frame = new RegEnfermedades();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RegEnfermedades() {
        setTitle("Registro de Enfermedades");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.7);
        int height = (int) (screenSize.getHeight() * 0.7);
        
        setBounds(100, 100, 1342, 492);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblCodigo = new JLabel("Codigo:");
        lblCodigo.setBounds(10, 11, 60, 14);
        contentPane.add(lblCodigo);
        
        txtCodigo = new JTextField();
        txtCodigo.setBounds(80, 8, 120, 25);
        contentPane.add(txtCodigo);
        txtCodigo.setColumns(10);
        
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(210, 11, 60, 14);
        contentPane.add(lblNombre);
        
        txtNombre = new JTextField();
        txtNombre.setBounds(280, 8, 200, 25);
        contentPane.add(txtNombre);
        txtNombre.setColumns(10);
        
        JLabel lblDescripcion = new JLabel("Descripcion:");
        lblDescripcion.setBounds(490, 11, 80, 14);
        contentPane.add(lblDescripcion);
        
        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(570, 8, width - 600, 25);
        contentPane.add(txtDescripcion);
        txtDescripcion.setColumns(10);
        
        chkVigilada = new JCheckBox("Enfermedad Vigilada");
        chkVigilada.setBounds(10, 40, 150, 25);
        contentPane.add(chkVigilada);
        
        JButton btnAgregar = new JButton("Agregar Enfermedad");
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarEnfermedad();
            }
        });
        btnAgregar.setBounds(170, 40, 150, 30);
        contentPane.add(btnAgregar);
        
        JButton btnEliminar = new JButton("Eliminar Enfermedad");
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarEnfermedad();
            }
        });
        btnEliminar.setBounds(330, 40, 150, 30);
        contentPane.add(btnEliminar);
        
        JButton btnVigiladas = new JButton("Ver Enfermedades Vigiladas");
        btnVigiladas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarEnfermedadesVigiladas();
            }
        });
        btnVigiladas.setBounds(490, 40, 200, 30);
        contentPane.add(btnVigiladas);
        
        JButton btnTodas = new JButton("Ver Todas");
        btnTodas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarEnfermedades();
            }
        });
        btnTodas.setBounds(700, 40, 120, 30);
        contentPane.add(btnTodas);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 86, width - 40, height - 150);
        contentPane.add(scrollPane);
        
        modelEnfermedades = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Codigo", "Nombre", "Descripcion", "Vigilada", "Estado"}
        ) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableEnfermedades = new JTable(modelEnfermedades);
        scrollPane.setViewportView(tableEnfermedades);
        
        cargarEnfermedades();
    }
    
    private void agregarEnfermedad() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        boolean esVigilada = chkVigilada.isSelected();
        
        if (codigo.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Codigo y nombre son obligatorios");
            return;
        }
        
        Enfermedad enfermedad = new Enfermedad(codigo, nombre, esVigilada, descripcion);
        boolean exito = ClinicaControladora.getInstance().agregarEnfermedad(enfermedad);
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "Enfermedad agregada exitosamente");
            limpiarCampos();
            cargarEnfermedades();
        } else {
            JOptionPane.showMessageDialog(this, "Error: La enfermedad ya existe");
        }
    }
    
    private void eliminarEnfermedad() {
        int fila = tableEnfermedades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una enfermedad para eliminar");
            return;
        }
        
        String codigo = (String) modelEnfermedades.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Esta seguro de eliminar la enfermedad " + codigo, 
            "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean exito = ClinicaControladora.getInstance().eliminarEnfermedad(codigo);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Enfermedad eliminada exitosamente");
                cargarEnfermedades();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la enfermedad");
            }
        }
    }
    
    private void mostrarEnfermedadesVigiladas() {
        modelEnfermedades.setRowCount(0);
        List<Enfermedad> enfermedades = ClinicaControladora.getInstance().obtenerEnfermedadesVigiladas();
        for (Enfermedad enfermedad : enfermedades) {
            modelEnfermedades.addRow(new Object[]{
                enfermedad.getCodigo(),
                enfermedad.getNombre(),
                enfermedad.getDescripcion(),
                enfermedad.isEsVigilada() ? "Si" : "No",
                enfermedad.isActiva() ? "Activa" : "Inactiva"
            });
        }
        JOptionPane.showMessageDialog(this, "Mostrando enfermedades bajo vigilancia");
    }
    
    private void cargarEnfermedades() {
        modelEnfermedades.setRowCount(0);
        List<Enfermedad> enfermedades = ClinicaControladora.getInstance().obtenerEnfermedades();
        for (Enfermedad enfermedad : enfermedades) {
            modelEnfermedades.addRow(new Object[]{
                enfermedad.getCodigo(),
                enfermedad.getNombre(),
                enfermedad.getDescripcion(),
                enfermedad.isEsVigilada() ? "Si" : "No",
                enfermedad.isActiva() ? "Activa" : "Inactiva"
            });
        }
    }
    
    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        chkVigilada.setSelected(false);
    }
}