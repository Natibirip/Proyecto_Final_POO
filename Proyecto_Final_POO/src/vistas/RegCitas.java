package vistas;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;

import controlador.ClinicaControladora;
import modelos.Medico;
import modelos.Paciente;
import modelos.Cita;

import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class RegCitas extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtCedulaPaciente;
    private JComboBox<String> cmbEspecialidad;
    private JList<String> listaMedicos;
    private DefaultListModel<String> modelMedicos;
    private JTextField txtFecha;
    private JTextField txtHora;
    
    private Paciente pacienteSeleccionado;
    private Medico medicoSeleccionado;

    public static void main(String[] args) {
        try {
            RegCitas dialog = new RegCitas();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RegCitas() {
        setTitle("Registrar Cita");
        setBounds(100, 100, 500, 400);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblCedula = new JLabel("Cedula Paciente:");
        lblCedula.setBounds(10, 11, 120, 14);
        contentPane.add(lblCedula);
        
        txtCedulaPaciente = new JTextField();
        txtCedulaPaciente.setBounds(140, 8, 120, 25);
        contentPane.add(txtCedulaPaciente);
        txtCedulaPaciente.setColumns(10);
        
        JButton btnBuscarPaciente = new JButton("Buscar");
        btnBuscarPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarPaciente();
            }
        });
        btnBuscarPaciente.setBounds(270, 8, 80, 25);
        contentPane.add(btnBuscarPaciente);
        
        JButton btnNuevoPaciente = new JButton("Nuevo");
        btnNuevoPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegCliente regCliente = new RegCliente();
                regCliente.setVisible(true);
            }
        });
        btnNuevoPaciente.setBounds(360, 8, 80, 25);
        contentPane.add(btnNuevoPaciente);
        
        JLabel lblEspecialidad = new JLabel("Especialidad:");
        lblEspecialidad.setBounds(10, 45, 80, 14);
        contentPane.add(lblEspecialidad);
        
        cmbEspecialidad = new JComboBox<>();
        cmbEspecialidad.setBounds(100, 42, 150, 25);
        cmbEspecialidad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarMedicosPorEspecialidad();
            }
        });
        contentPane.add(cmbEspecialidad);
        
        JLabel lblMedicos = new JLabel("Medicos:");
        lblMedicos.setBounds(10, 80, 150, 14);
        contentPane.add(lblMedicos);
        
        modelMedicos = new DefaultListModel<>();
        listaMedicos = new JList<>(modelMedicos);
        listaMedicos.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarMedico();
                }
            }
        });
        
        JScrollPane scrollMedicos = new JScrollPane(listaMedicos);
        scrollMedicos.setBounds(10, 100, 350, 120);
        contentPane.add(scrollMedicos);
        
        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(10, 230, 120, 14);
        contentPane.add(lblFecha);
        
        txtFecha = new JTextField();
        txtFecha.setBounds(140, 227, 100, 25);
        contentPane.add(txtFecha);
        txtFecha.setColumns(10);
        
        JLabel lblHora = new JLabel("Hora:");
        lblHora.setBounds(10, 265, 120, 14);
        contentPane.add(lblHora);
        
        txtHora = new JTextField();
        txtHora.setBounds(140, 262, 100, 25);
        contentPane.add(txtHora);
        txtHora.setColumns(10);
        
        JButton btnRegistrar = new JButton("Registrar Cita");
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarCita();
            }
        });
        btnRegistrar.setBounds(10, 300, 120, 30);
        contentPane.add(btnRegistrar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnCancelar.setBounds(140, 300, 100, 30);
        contentPane.add(btnCancelar);
        
        cargarEspecialidades();
        cargarMedicos();
    }
    
    private void buscarPaciente() {
        String cedula = txtCedulaPaciente.getText().trim();
        
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cedula");
            return;
        }
        
        pacienteSeleccionado = ClinicaControladora.getInstance().buscarPacientePorCedula(cedula);
        
        if (pacienteSeleccionado != null) {
            JOptionPane.showMessageDialog(this, "Paciente: " + pacienteSeleccionado.getNombre());
        } else {
            int opcion = JOptionPane.showConfirmDialog(this, 
                "Paciente no existe. ¿Registrar nuevo?", 
                "Paciente no encontrado", JOptionPane.YES_NO_OPTION);
            
            if (opcion == JOptionPane.YES_OPTION) {
                RegCliente regCliente = new RegCliente();
                regCliente.setVisible(true);
            }
        }
    }
    
    private void cargarEspecialidades() {
        cmbEspecialidad.removeAllItems();
        cmbEspecialidad.addItem("Todas");
        
        List<Medico> medicos = ClinicaControladora.getInstance().getMedicos();
        for (Medico medico : medicos) {
            if (medico.isActivo()) {
                String especialidad = medico.getEspecialidad();
                boolean existe = false;
                for (int i = 0; i < cmbEspecialidad.getItemCount(); i++) {
                    if (cmbEspecialidad.getItemAt(i).equals(especialidad)) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    cmbEspecialidad.addItem(especialidad);
                }
            }
        }
    }
    
    private void cargarMedicos() {
        cargarMedicosPorEspecialidad();
    }
    
    private void cargarMedicosPorEspecialidad() {
        modelMedicos.clear();
        
        String especialidadSeleccionada = (String) cmbEspecialidad.getSelectedItem();
        List<Medico> medicos = ClinicaControladora.getInstance().getMedicos();
        
        for (Medico medico : medicos) {
            if (medico.isActivo()) {
                if (especialidadSeleccionada.equals("Todas") || 
                    medico.getEspecialidad().equals(especialidadSeleccionada)) {
                    
                    String infoMedico = String.format("Dr. %s - %s",
                        medico.getNombre(),
                        medico.getEspecialidad());
                    
                    modelMedicos.addElement(infoMedico);
                }
            }
        }
    }
    
    private void seleccionarMedico() {
        int indiceSeleccionado = listaMedicos.getSelectedIndex();
        if (indiceSeleccionado != -1) {
            String especialidadSeleccionada = (String) cmbEspecialidad.getSelectedItem();
            List<Medico> medicos = ClinicaControladora.getInstance().getMedicos();
            
            int contador = 0;
            for (Medico medico : medicos) {
                if (medico.isActivo()) {
                    if (especialidadSeleccionada.equals("Todas") || 
                        medico.getEspecialidad().equals(especialidadSeleccionada)) {
                        
                        if (contador == indiceSeleccionado) {
                            medicoSeleccionado = medico;
                            break;
                        }
                        contador++;
                    }
                }
            }
        }
    }
    
    private void registrarCita() {
        if (pacienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Busque un paciente primero");
            return;
        }
        
        if (medicoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un medico");
            return;
        }
        
        String fechaStr = txtFecha.getText().trim();
        String hora = txtHora.getText().trim();
        
        if (fechaStr.isEmpty() || hora.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete fecha y hora");
            return;
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = sdf.parse(fechaStr);
            
            Cita nuevaCita = new Cita(pacienteSeleccionado, medicoSeleccionado, fecha, hora);
            
            JOptionPane.showMessageDialog(this, 
                "Cita registrada:\n" +
                "Paciente: " + pacienteSeleccionado.getNombre() + "\n" +
                "Medico: " + medicoSeleccionado.getNombre() + "\n" +
                "Fecha: " + fechaStr + " " + hora);
            
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en fecha. Use dd/mm/aaaa");
        }
    }
}