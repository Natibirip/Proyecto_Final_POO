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
import javax.swing.table.DefaultTableModel;
import controlador.ClinicaControladora;
import modelos.Vacuna;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.awt.Dimension;
import java.awt.Toolkit;

public class RegVacunas extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTable tableVacunas;
    private DefaultTableModel modelVacunas;

    public static void main(String[] args) {
        try {
            RegVacunas frame = new RegVacunas();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RegVacunas() {
        setTitle("Registro de Vacunas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.7);
        int height = (int) (screenSize.getHeight() * 0.7);
        
        setBounds(100, 100, 1350, 616);
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
        
        JButton btnAgregar = new JButton("Agregar Vacuna");
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarVacuna();
            }
        });
        btnAgregar.setBounds(10, 45, 140, 30);
        contentPane.add(btnAgregar);
        
        JButton btnEliminar = new JButton("Eliminar Vacuna");
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarVacuna();
            }
        });
        btnEliminar.setBounds(160, 45, 140, 30);
        contentPane.add(btnEliminar);
        
        JButton btnModificar = new JButton("Modificar Vacuna");
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificarVacuna();
            }
        });
        btnModificar.setBounds(310, 45, 140, 30);
        contentPane.add(btnModificar);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 86, width - 40, height - 150);
        contentPane.add(scrollPane);
        
        modelVacunas = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Codigo", "Nombre", "Descripcion", "Estado"}
        ) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableVacunas = new JTable(modelVacunas);
        scrollPane.setViewportView(tableVacunas);
        
        cargarVacunas();
    }
    
    private void agregarVacuna() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        
        if (codigo.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Codigo y nombre son obligatorios");
            return;
        }
        
        Vacuna vacuna = new Vacuna(codigo, nombre, descripcion);
        boolean exito = ClinicaControladora.getInstance().agregarVacuna(vacuna);
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "Vacuna agregada exitosamente");
            limpiarCampos();
            cargarVacunas();
        } else {
            JOptionPane.showMessageDialog(this, "Error: La vacuna ya existe");
        }
    }
    
    private void eliminarVacuna() {
        int fila = tableVacunas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una vacuna para eliminar");
            return;
        }
        
        String codigo = (String) modelVacunas.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Esta seguro de eliminar la vacuna " + codigo, 
            "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean exito = ClinicaControladora.getInstance().eliminarVacuna(codigo);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Vacuna eliminada exitosamente");
                cargarVacunas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la vacuna");
            }
        }
    }
    
    private void modificarVacuna() {
        int fila = tableVacunas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una vacuna para modificar");
            return;
        }
        
        String codigoOriginal = (String) modelVacunas.getValueAt(fila, 0);
        String nuevoCodigo = txtCodigo.getText().trim();
        String nuevoNombre = txtNombre.getText().trim();
        String nuevaDescripcion = txtDescripcion.getText().trim();
        
        if (nuevoCodigo.isEmpty() || nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Codigo y nombre son obligatorios");
            return;
        }
        
        Vacuna vacunaModificada = new Vacuna(nuevoCodigo, nuevoNombre, nuevaDescripcion);
        boolean exito = ClinicaControladora.getInstance().modificarVacuna(codigoOriginal, vacunaModificada);
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "Vacuna modificada exitosamente");
            limpiarCampos();
            cargarVacunas();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar la vacuna");
        }
    }
    
    private void cargarVacunas() {
        modelVacunas.setRowCount(0);
        List<Vacuna> vacunas = ClinicaControladora.getInstance().obtenerVacunas();
        for (Vacuna vacuna : vacunas) {
            modelVacunas.addRow(new Object[]{
                vacuna.getCodigo(),
                vacuna.getNombre(),
                vacuna.getDescripcion(),
                vacuna.isActiva() ? "Activa" : "Inactiva"
            });
        }
    }
    
    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
    }
}