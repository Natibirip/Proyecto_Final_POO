package vistas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import controlador.ClinicaControladora;
import modelos.Medico;
import modelos.Secretaria;

public class RegPersonalDialog extends JDialog {

    private JComboBox<String> cmbTipo;
    private JTextField txtNombre, txtCedula, txtTelefono;
    private JFormattedTextField txtFechaNac;
    private JComboBox<String> cmbSexo;
    
    private JPanel panelMedico;
    private JPanel panelSecretaria;
    
    private JComboBox<String> cmbEspecialidad;
    private JSpinner spinCitasDia;
    private JTextField txtHorario;
    
    private JButton btnGuardar, btnCancelar;

    public RegPersonalDialog(java.awt.Frame parent) {
        super(parent, "Registro de Personal", true);
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(null);
        setResizable(false);

        inicializarComponentes();
        
        cmbTipo.setSelectedIndex(0);
        actualizarVisibilidadPaneles();
    }

    private void inicializarComponentes() {
        // ... (Código de selección de tipo y datos personales igual que antes) ...
        JLabel lblTipo = new JLabel("Tipo de Personal:");
        lblTipo.setBounds(30, 20, 150, 20);
        add(lblTipo);

        cmbTipo = new JComboBox<>(new String[]{"Médico", "Secretaria"});
        cmbTipo.setBounds(150, 20, 200, 25);
        add(cmbTipo);
        
        cmbTipo.addActionListener(e -> actualizarVisibilidadPaneles());

        JPanel panelDatosPersonales = new JPanel();
        panelDatosPersonales.setLayout(null);
        panelDatosPersonales.setBorder(new TitledBorder("Datos Personales"));
        panelDatosPersonales.setBounds(20, 60, 440, 200);
        add(panelDatosPersonales);

        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setBounds(20, 30, 120, 20);
        panelDatosPersonales.add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(150, 30, 260, 25);
        panelDatosPersonales.add(txtNombre);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(20, 70, 120, 20);
        panelDatosPersonales.add(lblCedula);
        txtCedula = new JTextField();
        txtCedula.setBounds(150, 70, 150, 25);
        panelDatosPersonales.add(txtCedula);

        JLabel lblTel = new JLabel("Teléfono:");
        lblTel.setBounds(20, 110, 120, 20);
        panelDatosPersonales.add(lblTel);
        txtTelefono = new JTextField();
        txtTelefono.setBounds(150, 110, 150, 25);
        panelDatosPersonales.add(txtTelefono);

        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setBounds(20, 150, 50, 20);
        panelDatosPersonales.add(lblSexo);
        cmbSexo = new JComboBox<>(new String[]{"M", "F"});
        cmbSexo.setBounds(70, 150, 60, 25);
        panelDatosPersonales.add(cmbSexo);

        JLabel lblFecha = new JLabel("Fecha Nac (dd/mm/yyyy):");
        lblFecha.setBounds(150, 150, 160, 20);
        panelDatosPersonales.add(lblFecha);
        
        try {
            MaskFormatter formatoFecha = new MaskFormatter("##/##/####");
            formatoFecha.setPlaceholderCharacter('_');
            txtFechaNac = new JFormattedTextField(formatoFecha);
            txtFechaNac.setBounds(310, 150, 100, 25);
            panelDatosPersonales.add(txtFechaNac);
        } catch (Exception e) { e.printStackTrace(); }

        // ==========================================
        // PANEL MÉDICO
        // ==========================================
        panelMedico = new JPanel();
        panelMedico.setLayout(null);
        panelMedico.setBorder(new TitledBorder("Información Profesional (Médico)"));
        panelMedico.setBounds(20, 270, 440, 180);
        
        JLabel lblEsp = new JLabel("Especialidad:");
        lblEsp.setBounds(20, 30, 120, 20);
        panelMedico.add(lblEsp);

        cmbEspecialidad = new JComboBox<>();
        cmbEspecialidad.setBounds(150, 30, 260, 25);
        
        // --- MODIFICACIÓN 1: HACERLO EDITABLE ---
        cmbEspecialidad.setEditable(true); 
        // ----------------------------------------
        
        ClinicaControladora control = ClinicaControladora.getInstance();
        ArrayList<String> listaEsp = control.getListaEspecialidades();
        if (listaEsp != null) {
            for (int i = 0; i < listaEsp.size(); i++) {
                cmbEspecialidad.addItem(listaEsp.get(i));
            }
        }
        panelMedico.add(cmbEspecialidad);

        JLabel lblCitas = new JLabel("Citas máx. por día:");
        lblCitas.setBounds(20, 70, 120, 20);
        panelMedico.add(lblCitas);
        spinCitasDia = new JSpinner(new SpinnerNumberModel(10, 1, 50, 1));
        spinCitasDia.setBounds(150, 70, 60, 25);
        panelMedico.add(spinCitasDia);
        
        JLabel lblHorario = new JLabel("Horario Atención:");
        lblHorario.setBounds(20, 110, 120, 20);
        panelMedico.add(lblHorario);
        txtHorario = new JTextField("8:00 AM - 5:00 PM");
        txtHorario.setBounds(150, 110, 260, 25);
        panelMedico.add(txtHorario);
        
        add(panelMedico);

        // PANEL SECRETARIA
        panelSecretaria = new JPanel();
        panelSecretaria.setLayout(null);
        panelSecretaria.setBorder(new TitledBorder("Información Administrativa"));
        panelSecretaria.setBounds(20, 270, 440, 180);
        
        JLabel lblInfoSec = new JLabel("<html><body><center>Perfil Administrativo.<br>No se requieren datos clínicos adicionales.</center></body></html>");
        lblInfoSec.setBounds(50, 40, 340, 60);
        lblInfoSec.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfoSec.setForeground(Color.GRAY);
        lblInfoSec.setFont(new Font("Arial", Font.ITALIC, 12));
        panelSecretaria.add(lblInfoSec);
        
        add(panelSecretaria);

        // BOTONES
        btnGuardar = new JButton("Registrar Personal");
        btnGuardar.setBounds(100, 480, 160, 40);
        btnGuardar.setBackground(new Color(60, 179, 113));
        btnGuardar.setForeground(Color.WHITE);
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(280, 480, 120, 40);
        add(btnCancelar);

        btnGuardar.addActionListener(e -> guardarDatos());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void actualizarVisibilidadPaneles() {
        String seleccion = (String) cmbTipo.getSelectedItem();
        boolean esMedico = "Médico".equals(seleccion);
        panelMedico.setVisible(esMedico);
        panelSecretaria.setVisible(!esMedico);
        this.revalidate();
        this.repaint();
    }

    private void guardarDatos() {
        if (txtNombre.getText().isEmpty() || txtCedula.getText().isEmpty() || txtTelefono.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los datos personales.");
            return;
        }
        
        Date fechaNac = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            fechaNac = sdf.parse(txtFechaNac.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fecha inválida (dd/mm/yyyy)");
            return;
        }
        
        String cedula = txtCedula.getText();
        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        char sexo = cmbSexo.getSelectedItem().toString().charAt(0);
        String tipo = (String) cmbTipo.getSelectedItem();
        
        ClinicaControladora controller = ClinicaControladora.getInstance();
        
        if ("Médico".equals(tipo)) {
            // Obtener el texto (ya sea seleccionado o escrito)
            String especialidad = (String) cmbEspecialidad.getSelectedItem();
            
            if (especialidad == null || especialidad.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe indicar una especialidad.");
                return;
            }
            
            // --- MODIFICACIÓN 2: GUARDAR NUEVA ESPECIALIDAD EN LA CONTROLADORA ---
            // Esto registrará la especialidad en la lista global si no existe
            controller.agregarEspecialidad(especialidad); 
            // ---------------------------------------------------------------------

            Medico nuevoMedico = new Medico(cedula, nombre, telefono, fechaNac, sexo, especialidad);
            nuevoMedico.setCitasPorDia((int) spinCitasDia.getValue());
            nuevoMedico.setHorarioAtencion(txtHorario.getText());
            
            controller.registrarMedico(nuevoMedico);
            JOptionPane.showMessageDialog(this, "Médico registrado correctamente.");
            
        } else {
            Secretaria nuevaSecretaria = new Secretaria(cedula, nombre, telefono, fechaNac, sexo);
            controller.registrarSecretaria(nuevaSecretaria);
            JOptionPane.showMessageDialog(this, "Secretaria registrada correctamente.");
        }
        
        dispose();
    }
}