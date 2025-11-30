package vistas;

import controlador.ClinicaControladora;
import modelos.*;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class RegPersonalDialog extends JDialog {

    private JTextField txtNombre, txtCedula, txtTelefono;
    private JComboBox<String> cmbTipoPersona;
    private JTextField txtEspecialidad; // Exclusivo para médicos
    private JTextField txtRegistroMedico; // Exclusivo para médicos
    private JLabel lblEspecialidad, lblRegMedico; // Etiquetas para ocultar/mostrar

    public RegPersonalDialog(Window owner) {
        super(owner, "Registro de Personal", ModalityType.APPLICATION_MODAL);
        setSize(400, 450);
        setLocationRelativeTo(owner);
        setLayout(null);

        int x = 30, y = 20, w = 300, h = 25, gap = 40;

        // 1. Selector de TIPO
        add(new JLabel("Tipo de Personal:")).setBounds(x, y, 200, h);
        cmbTipoPersona = new JComboBox<>();
        cmbTipoPersona.setBounds(x, y + 20, w, h);
        add(cmbTipoPersona);

        y += 60;

        // 2. Datos Comunes (Persona)
        add(new JLabel("Nombre Completo:")).setBounds(x, y, 200, h);
        txtNombre = new JTextField();
        txtNombre.setBounds(x, y + 20, w, h);
        add(txtNombre);

        y += 50;
        add(new JLabel("Cédula:")).setBounds(x, y, 200, h);
        txtCedula = new JTextField();
        txtCedula.setBounds(x, y + 20, w, h);
        add(txtCedula);

        y += 50;
        add(new JLabel("Teléfono:")).setBounds(x, y, 200, h);
        txtTelefono = new JTextField();
        txtTelefono.setBounds(x, y + 20, w, h);
        add(txtTelefono);

        y += 50;

        // 3. Datos Exclusivos de MÉDICO
        lblEspecialidad = new JLabel("Especialidad:");
        lblEspecialidad.setBounds(x, y, 200, h);
        add(lblEspecialidad);

        txtEspecialidad = new JTextField();
        txtEspecialidad.setBounds(x, y + 20, w, h);
        add(txtEspecialidad);

        y += 50;
        lblRegMedico = new JLabel("No. Licencia Médica:");
        lblRegMedico.setBounds(x, y, 200, h);
        add(lblRegMedico);
        
        txtRegistroMedico = new JTextField();
        txtRegistroMedico.setBounds(x, y + 20, w, h);
        add(txtRegistroMedico);

        // Botón Guardar
        JButton btnGuardar = new JButton("Registrar Personal");
        btnGuardar.setBounds(x, 350, w, 40);
        btnGuardar.setBackground(new Color(100, 149, 237));
        btnGuardar.setForeground(Color.WHITE);
        add(btnGuardar);

        // --- EVENTOS ---

        // Evento: Ocultar/Mostrar campos según selección
        cmbTipoPersona.addActionListener(e -> {
            boolean esMedico = cmbTipoPersona.getSelectedItem().equals("MÉDICO");
            mostrarCamposMedico(esMedico);
        });

        // Evento: Guardar
        btnGuardar.addActionListener(e -> guardar());

        // Estado inicial
        mostrarCamposMedico(true);
    }

    private void mostrarCamposMedico(boolean visible) {
        lblEspecialidad.setVisible(visible);
        txtEspecialidad.setVisible(visible);
        lblRegMedico.setVisible(visible);
        txtRegistroMedico.setVisible(visible);
    }

    private void guardar() {
        String tipo = (String) cmbTipoPersona.getSelectedItem();
        String nombre = txtNombre.getText();
        String cedula = txtCedula.getText();
        String telefono = txtTelefono.getText();

        if (nombre.isEmpty() || cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los datos obligatorios.");
            return;
        }

        ClinicaControladora control = ClinicaControladora.getInstance();

        if (tipo.equals("MÉDICO")) {
            String especialidad = txtEspecialidad.getText();
            String licencia = txtRegistroMedico.getText();
            
            // Crear objeto Médico
            // Asumiendo constructor: Medico(cedula, nombre, telefono, fechaNac, sexo, especialidad, licencia)
            // Usamos fecha y sexo por defecto o agrégalos al form si quieres
            Medico nuevoMedico = new Medico(cedula, nombre, telefono, new Date(), 'M', especialidad, licencia);
            
            control.registrarMedico(nuevoMedico);
            JOptionPane.showMessageDialog(this, "Médico registrado con éxito.");
        
        } else {
            // Crear objeto Secretaria
            Secretaria nuevaSecretaria = new Secretaria(cedula, nombre, telefono, new Date(), 'F', "Mañana");
            control.registrarSecretaria(nuevaSecretaria); // Necesitas crear este método en Controller si no existe
            JOptionPane.showMessageDialog(this, "Secretaria registrada con éxito.");
        }

        dispose(); // Cerrar ventana
    }
}