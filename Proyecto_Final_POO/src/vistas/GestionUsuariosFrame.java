package vistas;

import controlador.ClinicaControladora;
import modelos.Persona;
import modelos.RolUsuario;
import modelos.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GestionUsuariosFrame extends JFrame {

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private ClinicaControladora controller;

    public GestionUsuariosFrame() {
        this.controller = ClinicaControladora.getInstance(); // Singleton
        
        setTitle("Administración de Usuarios y Roles");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // No cierra la app, solo esta ventana
        getContentPane().setLayout(new BorderLayout());

        // 1. Panel Superior (Título)
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(70, 130, 180));
        JLabel lblTitulo = new JLabel("GESTIÓN DE USUARIOS DEL SISTEMA");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelTitulo.add(lblTitulo);
        getContentPane().add(panelTitulo, BorderLayout.NORTH);

        // 2. Tabla (Centro)
        inicializarTabla();
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // 3. Panel de Botones (Inferior)
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton btnNuevo = new JButton("Nuevo Usuario");
        btnNuevo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		abrirDialogoUsuario(null);
        	}
        });
        JButton btnEditar = new JButton("Editar Seleccionado");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrar = new JButton("Cerrar");

        // Estilos básicos
        btnNuevo.setBackground(new Color(144, 238, 144)); // Verde claro
        btnEditar.setBackground(new Color(255, 228, 181)); // Naranja claro

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);
        
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        // 4. Cargar datos iniciales
        cargarDatosEnTabla();

        // ================= EVENTOS =================
        
        btnCerrar.addActionListener(e -> dispose());

        //btnNuevo.addActionListener(e -> abrirDialogoUsuario(null)); // null indica modo CREAR

        btnEditar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila >= 0) {
                // Obtenemos el username de la columna 0
                String username = (String) modeloTabla.getValueAt(fila, 0);
                Usuario u = buscarUsuarioLocalmente(username);
                if (u != null) {
                   abrirDialogoUsuario(u); // pasamos el objeto para modo EDITAR
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario de la lista.");
            }
        });
        
        btnEliminar.addActionListener(e -> eliminarUsuario());
    }

    private void inicializarTabla() {
        // Columnas: Username, Rol, Nombre Real (Persona), Cédula
        String[] columnas = {"Username", "Rol", "Nombre Persona", "Cédula Vinculada"};
        
        // Hacemos que las celdas no sean editables directamente
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.setRowHeight(25);
    }

    private void cargarDatosEnTabla() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        ArrayList<Usuario> lista = controller.getUsuarios();

        for (int i = 0; i < lista.size(); i++) {
            Usuario u = lista.get(i);
            
            String nombrePersona = "--- (Sin vincular)";
            String cedulaPersona = "N/A";
            
            // Verificar si tiene persona asociada
            if (u.getPersonaAsociada() != null) {
                nombrePersona = u.getPersonaAsociada().getNombre();
                cedulaPersona = u.getPersonaAsociada().getCedula();
            }

            Object[] fila = {
                u.getUsername(),
                u.getRol().toString(),
                nombrePersona,
                cedulaPersona
            };
            modeloTabla.addRow(fila);
        }
    }

    // Método auxiliar porque el Controller quizás solo tiene login, no búsqueda pública
    private Usuario buscarUsuarioLocalmente(String username) {
        ArrayList<Usuario> lista = controller.getUsuarios();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getUsername().equals(username)) {
                return lista.get(i);
            }
        }
        return null;
    }

    private void eliminarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
            return;
        }

        String username = (String) modeloTabla.getValueAt(fila, 0);
        
        // Evitar que se elimine a sí mismo (opcional, pero recomendado)
        // if (username.equals(UsuarioActual.getUsername())) ...
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Seguro que desea eliminar al usuario '" + username + "'?", 
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Usuario u = buscarUsuarioLocalmente(username);
            if (u != null) {
                controller.getUsuarios().remove(u);
                cargarDatosEnTabla(); // Refrescar
                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
            }
        }
    }

    // =================================================================
    // SUB-CLASE O MÉTODO PARA ABRIR EL FORMULARIO (JDialog)
    // =================================================================
    

    private void abrirDialogoUsuario(Usuario usuarioAEditar) {
        DialogoUsuario dialogo = new DialogoUsuario(this, usuarioAEditar);
        dialogo.setVisible(true);
        
        // Cuando el diálogo se cierre, recargamos la tabla por si hubo cambios
        cargarDatosEnTabla();
    }

}