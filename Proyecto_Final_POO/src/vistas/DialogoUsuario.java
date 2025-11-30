package vistas;

import controlador.ClinicaControladora;
import modelos.*; // Importar tus modelos

import javax.swing.*;
import java.awt.*;

public class DialogoUsuario extends JDialog {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<RolUsuario> cmbRol;
    private JTextField txtCedulaVinculacion;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Usuario usuarioEdicion; // Si es null, es nuevo. Si no, es edición.
    private ClinicaControladora controller;

    public DialogoUsuario(Frame parent, Usuario usuario) {
        super(parent, true); // true = Modal (bloquea la ventana de atrás)
        this.usuarioEdicion = usuario;
        this.controller = ClinicaControladora.getInstance();

        setTitle(usuario == null ? "Nuevo Usuario" : "Editar Usuario");
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(null);

        inicializarComponentes();
        
        if (usuario != null) {
            cargarDatosExistentes();
        }
    }

    private void inicializarComponentes() {
        int y = 20;
        int labelX = 30;
        int fieldX = 150;
        int fieldW = 200;
        int h = 25;
        int gap = 40;

        // Username
        add(new JLabel("Username:")).setBounds(labelX, y, 100, h);
        txtUsername = new JTextField();
        txtUsername.setBounds(fieldX, y, fieldW, h);
        add(txtUsername);

        y += gap;
        // Password
        add(new JLabel("Password:")).setBounds(labelX, y, 100, h);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(fieldX, y, fieldW, h);
        add(txtPassword);

        y += gap;
        // Rol
        add(new JLabel("Rol:")).setBounds(labelX, y, 100, h);
        cmbRol = new JComboBox<>(RolUsuario.values());
        cmbRol.setBounds(fieldX, y, fieldW, h);
        add(cmbRol);

        y += gap;
        // Vinculación (Cédula)
        JLabel lblCedula = new JLabel("Cédula Persona:");
        lblCedula.setBounds(labelX, y, 100, h);
        add(lblCedula);
        
        txtCedulaVinculacion = new JTextField();
        txtCedulaVinculacion.setBounds(fieldX, y, fieldW, h);
        txtCedulaVinculacion.setToolTipText("Ingrese la cédula del Médico/Secretaria si existe");
        add(txtCedulaVinculacion);
        
        // Nota explicativa
        JLabel lblNota = new JLabel("(Opcional: Dejar vacío para usuario solo sistema)");
        lblNota.setFont(new Font("Arial", Font.PLAIN, 10));
        lblNota.setForeground(Color.GRAY);
        lblNota.setBounds(fieldX, y + 20, 220, 15);
        add(lblNota);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(80, 260, 100, 30);
        btnGuardar.addActionListener(e -> guardar());
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 260, 100, 30);
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);
    }

    private void cargarDatosExistentes() {
        txtUsername.setText(usuarioEdicion.getUsername());
        txtUsername.setEditable(false); // No permitimos cambiar el username (llave primaria lógica)
        txtPassword.setText(usuarioEdicion.getPassword());
        cmbRol.setSelectedItem(usuarioEdicion.getRol());
        
        if (usuarioEdicion.getPersonaAsociada() != null) {
            txtCedulaVinculacion.setText(usuarioEdicion.getPersonaAsociada().getCedula());
            txtCedulaVinculacion.setEditable(false); // Una vez vinculado, mejor no cambiarlo aquí para evitar errores
        }
    }

    private void guardar() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        RolUsuario rol = (RolUsuario) cmbRol.getSelectedItem();
        String cedula = txtCedulaVinculacion.getText().trim();

        // Validaciones básicas
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y Contraseña son obligatorios.");
            return;
        }

        // Lógica de Vinculación con Persona
        Persona personaEncontrada = null;
        if (!cedula.isEmpty()) {
            // Buscamos en Médicos
            personaEncontrada = controller.buscarMedicoPorCedula(cedula);
            
            // Si no es médico, buscamos en Pacientes (aunque es raro que paciente sea usuario, pero es Persona)
            if (personaEncontrada == null) {
                personaEncontrada = controller.buscarPacientePorCedula(cedula);
            }
            
            // Aquí podrías agregar búsqueda en lista de Secretarias/Administradores si las tienes separadas
            
            if (personaEncontrada == null) {
                int opt = JOptionPane.showConfirmDialog(this, 
                    "No se encontró ninguna persona con la cédula " + cedula + ".\n" +
                    "¿Desea crear el usuario SIN vincularlo a una persona real?",
                    "Persona no encontrada", JOptionPane.YES_NO_OPTION);
                
                if (opt == JOptionPane.NO_OPTION) {
                    return; // Cancelamos el guardado
                }
            }
        }

        if (usuarioEdicion == null) {
            // --- MODO CREAR NUEVO ---
            if (controller.existeUsuario(user)) {
                JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe.");
                return;
            }
            
            Usuario nuevo = new Usuario(user, pass, rol, personaEncontrada);
            controller.registrarUsuario(nuevo);
            JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.");
            
        } else {
            // --- MODO EDITAR ---
            usuarioEdicion.setPassword(pass);
            usuarioEdicion.setRol(rol);
            // Nota: No actualizamos la persona vinculada en edición simple para no romper integridad,
            // pero podrías hacerlo si lo necesitas.
            JOptionPane.showMessageDialog(this, "Usuario actualizado.");
        }
        
        dispose(); // Cerrar diálogo
    }
}