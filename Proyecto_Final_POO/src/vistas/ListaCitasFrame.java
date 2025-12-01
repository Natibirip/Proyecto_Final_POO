package vistas;

import controlador.ClinicaControladora;
import modelos.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ListaCitasFrame extends JFrame {

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private Usuario usuarioActual; // Para saber quién está logueado
    private ArrayList<Cita> listaCitasFiltrada; // Lista auxiliar para mapear la tabla con los objetos

    public ListaCitasFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        this.listaCitasFiltrada = new ArrayList<>();

        setTitle("Gestión de Citas - Usuario: " + usuario.getUsername());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. TÍTULO
        JLabel lblTitulo = new JLabel("AGENDA DE CITAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 2. TABLA
        crearTabla();

        // 3. BOTONES
        crearBotonera();

        // 4. CARGAR DATOS
        cargarCitas();
    }

    private void crearTabla() {
        // Columnas
        String[] columnas = {"Fecha", "Hora", "Paciente", "Cédula", "Médico Asignado", "Estado"};
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
        };

        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setRowHeight(25);
        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scroll = new JScrollPane(tablaCitas);
        add(scroll, BorderLayout.CENTER);
    }

    private void crearBotonera() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnRefrescar = new JButton("Actualizar Lista");
        JButton btnAtender = new JButton("Atender Cita (Consulta)");
        JButton btnCancelar = new JButton("Cancelar Cita");
        JButton btnCerrar = new JButton("Cerrar");

        // Estilos
        btnAtender.setBackground(new Color(60, 179, 113)); // Verde
        btnAtender.setForeground(Color.WHITE);
        btnAtender.setFont(new Font("Arial", Font.BOLD, 12));
        
        btnCancelar.setBackground(new Color(205, 92, 92)); // Rojo suave
        btnCancelar.setForeground(Color.WHITE);

        // Solo el médico puede "Atender" clínicamente
        if (usuarioActual.getRol() != RolUsuario.MEDICO) {
            btnAtender.setEnabled(false);
            btnAtender.setToolTipText("Solo los médicos pueden iniciar consultas.");
        }

        // Acciones
        btnCerrar.addActionListener(e -> dispose());
        btnRefrescar.addActionListener(e -> cargarCitas());
        btnCancelar.addActionListener(e -> cancelarCitaSeleccionada());
        btnAtender.addActionListener(e -> atenderCitaSeleccionada());

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAtender);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarCitas() {
        modeloTabla.setRowCount(0); // Limpiar tabla visual
        listaCitasFiltrada.clear(); // Limpiar lista auxiliar
        
        ClinicaControladora controller = ClinicaControladora.getInstance();
        ArrayList<Cita> todas = controller.getCitas();
        
        if (todas == null) return;

        for (Cita c : todas) {
            boolean agregar = false;

            // LÓGICA DE FILTRADO SEGÚN ROL
            if (usuarioActual.getRol() == RolUsuario.MEDICO) {
                // Si soy médico, verifico si la cita es conmigo
                // Nota: El usuario debe tener una persona asociada que sea Medico
                if (usuarioActual.getPersonaAsociada() != null && 
                    c.getMedico().getCedula().equals(usuarioActual.getPersonaAsociada().getCedula())) {
                    agregar = true;
                }
            } else {
                // Admin y Secretaria ven todo
                agregar = true;
            }

            if (agregar) {
                listaCitasFiltrada.add(c); // Guardamos referencia para usarla luego
                Object[] fila = {
                    c.getFechaFormateada(),
                    c.getHora(),
                    c.getNombrePersona(), // Usamos el nombre crudo de la cita
                    c.getCedulaPersona(),
                    c.getNombreMedico(),
                    c.getEstado()
                };
                modeloTabla.addRow(fila);
            }
        }
    }

    // =================================================================
    // LÓGICA CORE: FLUJO ATENDER -> REGISTRAR -> CONSULTAR
    // =================================================================
    private void atenderCitaSeleccionada() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita para atender.");
            return;
        }

        // 1. Obtener la cita real desde nuestra lista filtrada
        Cita cita = listaCitasFiltrada.get(fila);

        // Validar estado
        if (cita.getEstado() == EstadoCita.CANCELADA || cita.getEstado() == EstadoCita.REALIZADA) {
            JOptionPane.showMessageDialog(this, "Esta cita ya fue procesada o cancelada.");
            return;
        }

        ClinicaControladora controller = ClinicaControladora.getInstance();
        String cedulaPaciente = cita.getCedulaPersona();
        String nombrePaciente = cita.getNombrePersona();

        // 2. Buscar si el paciente existe
        Paciente pacienteReal = controller.buscarPacientePorCedula(cedulaPaciente);

        // 3. CASO: PACIENTE NO EXISTE
        if (pacienteReal == null) {
            int respuesta = JOptionPane.showConfirmDialog(this, 
                "El paciente " + nombrePaciente + " (Cédula: " + cedulaPaciente + ") no está registrado.\n" +
                "Es necesario registrarlo para iniciar la consulta.\n\n" +
                "¿Desea abrir el registro ahora?",
                "Paciente No Encontrado", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                // A. Crear objeto temporal para pre-llenar el formulario
                // Usamos el constructor vacío y setters para evitar problemas
                Paciente preDatos = new Paciente();
                preDatos.setNombre(nombrePaciente);
                preDatos.setCedula(cedulaPaciente);

                // B. Abrir ventana de Registro (MODAL es clave aquí)
                // Se detendrá la ejecución de este método hasta que se cierre RegClientePrt2
                RegClientePrt2 ventanaReg = new RegClientePrt2(preDatos);
                ventanaReg.setModal(true); 
                ventanaReg.setVisible(true);

                // C. Al volver, buscamos de nuevo
                pacienteReal = controller.buscarPacientePorCedula(cedulaPaciente);
            } else {
                return; // Canceló el proceso
            }
        }

        // 4. CASO: PACIENTE YA EXISTE (O SE ACABA DE CREAR)
        if (pacienteReal != null) {
            // A. Asociar el paciente real a la cita
            cita.setPacienteAsociado(pacienteReal);
            
            // B. Iniciar Consulta
            RealizarConsultaFrame ventanaConsulta = new RealizarConsultaFrame(cita);
            ventanaConsulta.setVisible(true);
            
            // C. (Opcional) Cerrar esta lista o actualizarla
            // dispose(); 
            // O solo marcamos como "En Proceso" si tuvieras ese estado
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se pudo verificar el registro del paciente.");
        }
    }

    private void cancelarCitaSeleccionada() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita.");
            return;
        }
        
        Cita c = listaCitasFiltrada.get(fila);
        if (c.cancelar()) {
            ClinicaControladora.getInstance().guardarDatos();
            cargarCitas();
            JOptionPane.showMessageDialog(this, "Cita cancelada.");
        } else {
            JOptionPane.showMessageDialog(this, "No se puede cancelar esta cita (Ya pasó o ya fue realizada).");
        }
    }
}