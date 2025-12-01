package vistas;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.toedter.calendar.JDateChooser; // IMPORTANTE: Librería JCalendar

import controlador.ClinicaControladora;
import modelos.Cita;
import modelos.EstadoCita;
import modelos.Medico;

public class RegCita extends JDialog {

    // Componentes Paciente
    private JTextField txtNombre;
    private JTextField txtCedula;

    // Componentes Selección Médico
    private JComboBox<String> cmbFiltroEspecialidad;
    private JList<Medico> listaMedicos;
    private DefaultListModel<Medico> modeloListaMedicos;
    private JLabel lblInfoMedico; 

    // Componentes Fecha/Hora
    private JDateChooser dateChooser; // <--- CAMBIO: JDateChooser
    private JSpinner spinHora;

    public RegCita(java.awt.Frame parent) {
        super(parent, "Agendar Nueva Cita - Sistema Clínico", true);
        setSize(1100, 750); 
        setLocationRelativeTo(parent);
        getContentPane().setLayout(null);
        setResizable(false);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // TÍTULO PRINCIPAL
        JLabel lblTitulo = new JLabel("GESTIÓN DE NUEVA CITA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(0, 10, 1100, 40);
        getContentPane().add(lblTitulo);

        // =======================================================
        // PANEL IZQUIERDO: PACIENTE Y FECHA (0, 60, 500, 600)
        // =======================================================
        JPanel panelIzq = new JPanel();
        panelIzq.setLayout(null);
        panelIzq.setBounds(20, 60, 500, 470);
        getContentPane().add(panelIzq);

        // --- SUB-PANEL: DATOS DEL PACIENTE ---
        JPanel panelPaciente = new JPanel();
        panelPaciente.setLayout(null);
        panelPaciente.setBorder(new TitledBorder("1. Datos del Paciente"));
        panelPaciente.setBounds(0, 0, 500, 200);
        panelIzq.add(panelPaciente);

        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setBounds(20, 40, 150, 25);
        panelPaciente.add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(180, 40, 280, 25);
        panelPaciente.add(txtNombre);

        JLabel lblCedula = new JLabel("Cédula de Identidad:");
        lblCedula.setBounds(20, 90, 150, 25);
        panelPaciente.add(lblCedula);
        txtCedula = new JTextField();
        txtCedula.setBounds(180, 90, 280, 25);
        panelPaciente.add(txtCedula);

        // --- SUB-PANEL: FECHA Y HORA ---
        JPanel panelFecha = new JPanel();
        panelFecha.setLayout(null);
        panelFecha.setBorder(new TitledBorder("3. Fecha y Hora de la Cita"));
        panelFecha.setBounds(0, 220, 500, 250);
        panelIzq.add(panelFecha);

        // 1. Selector de FECHA (JDateChooser)
        JLabel lblDia = new JLabel("Seleccione Fecha:");
        lblDia.setBounds(20, 40, 150, 25);
        panelFecha.add(lblDia);

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy"); // Formato visual
        dateChooser.setDate(new Date()); // Fecha actual por defecto
        dateChooser.setMinSelectableDate(new Date()); // No permitir fechas pasadas en el calendario visual
        dateChooser.setBounds(180, 40, 200, 30);
        panelFecha.add(dateChooser);

        // 2. Selector de HORA (JSpinner)
        JLabel lblHora = new JLabel("Seleccione Hora:");
        lblHora.setBounds(20, 100, 150, 25);
        panelFecha.add(lblHora);

        SpinnerDateModel modelHora = new SpinnerDateModel();
        spinHora = new JSpinner(modelHora);
        JSpinner.DateEditor editorHora = new JSpinner.DateEditor(spinHora, "HH:mm");
        spinHora.setEditor(editorHora);
        
        // Fijamos hora por defecto 8:00 AM
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        spinHora.setValue(cal.getTime());
        
        spinHora.setBounds(180, 100, 200, 30);
        panelFecha.add(spinHora);
        
        JLabel lblNota = new JLabel("<html><small>* El sistema verificará automáticamente la disponibilidad<br>del médico para el día seleccionado.</small></html>");
        lblNota.setBounds(180, 140, 300, 40);
        lblNota.setForeground(Color.GRAY);
        panelFecha.add(lblNota);


        // =======================================================
        // PANEL DERECHO: SELECCIÓN DE MÉDICO (540, 60, 520, 600)
        // =======================================================
        JPanel panelDer = new JPanel();
        panelDer.setLayout(null);
        panelDer.setBorder(new TitledBorder("2. Selección del Médico"));
        panelDer.setBounds(540, 60, 520, 470);
        getContentPane().add(panelDer);

        // Filtro por Especialidad
        JLabel lblFiltro = new JLabel("Filtrar por Especialidad:");
        lblFiltro.setBounds(20, 30, 150, 25);
        panelDer.add(lblFiltro);

        cmbFiltroEspecialidad = new JComboBox<>();
        cmbFiltroEspecialidad.setBounds(180, 30, 300, 25);
        cargarEspecialidadesEnCombo();
        panelDer.add(cmbFiltroEspecialidad);

        // Lista de Médicos
        JLabel lblLista = new JLabel("Seleccione un Médico de la lista:");
        lblLista.setBounds(20, 70, 300, 20);
        panelDer.add(lblLista);

        modeloListaMedicos = new DefaultListModel<>();
        listaMedicos = new JList<>(modeloListaMedicos);
        listaMedicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollMedicos = new JScrollPane(listaMedicos);
        scrollMedicos.setBounds(20, 100, 480, 250);
        panelDer.add(scrollMedicos);

        // Información detallada del médico seleccionado
        lblInfoMedico = new JLabel("Seleccione un médico para ver su disponibilidad...");
        lblInfoMedico.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblInfoMedico.setVerticalAlignment(SwingConstants.TOP);
        lblInfoMedico.setBounds(20, 360, 480, 90);
        panelDer.add(lblInfoMedico);

        // =======================================================
        // BOTONES INFERIORES
        // =======================================================
        JButton btnAgendar = new JButton("CONFIRMAR CITA");
        btnAgendar.setBackground(new Color(0, 128, 0)); // Verde oscuro
        btnAgendar.setForeground(Color.WHITE);
        btnAgendar.setFont(new Font("Arial", Font.BOLD, 16));
        btnAgendar.setBounds(350, 620, 200, 50);
        getContentPane().add(btnAgendar);

        JButton btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBackground(new Color(178, 34, 34)); // Rojo
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCancelar.setBounds(570, 620, 200, 50);
        getContentPane().add(btnCancelar);

        // =======================================================
        // EVENTOS
        // =======================================================
        
        cmbFiltroEspecialidad.addActionListener(e -> filtrarMedicos());

        listaMedicos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostrarInfoMedico();
                }
            }
        });

        btnAgendar.addActionListener(e -> agendarCita());
        btnCancelar.addActionListener(e -> dispose());

        // Carga inicial
        filtrarMedicos();
    }

    // --- MÉTODOS DE CARGA DE DATOS ---

    private void cargarEspecialidadesEnCombo() {
        ClinicaControladora controller = ClinicaControladora.getInstance();
        ArrayList<String> especialidades = controller.getListaEspecialidades();
        
        cmbFiltroEspecialidad.addItem("Todas");
        
        if (especialidades != null) {
            for (String esp : especialidades) {
                cmbFiltroEspecialidad.addItem(esp);
            }
        }
    }

    private void filtrarMedicos() {
        modeloListaMedicos.clear();
        String filtro = (String) cmbFiltroEspecialidad.getSelectedItem();
        
        ClinicaControladora controller = ClinicaControladora.getInstance();
        ArrayList<Medico> todos = controller.getMedicos();
        
        if (todos != null) {
            for (Medico m : todos) {
                if (m.isActivo()) {
                    if ("Todas".equals(filtro) || m.getEspecialidad().equals(filtro)) {
                        modeloListaMedicos.addElement(m);
                    }
                }
            }
        }
    }

    private void mostrarInfoMedico() {
        Medico m = listaMedicos.getSelectedValue();
        if (m != null) {
            String info = "<html><body style='padding:10px'>" +
                    "<b>Dr/a:</b> " + m.getNombre() + "<br>" +
                    "<b>Especialidad:</b> " + m.getEspecialidad() + "<br>" +
                    "<b>Horario:</b> " + m.getHorarioAtencion() + "<br>" +
                    "<b>Cupo Diario:</b> " + m.getCitasPorDia() + " pacientes máx." +
                    "</body></html>";
            lblInfoMedico.setText(info);
        } else {
            lblInfoMedico.setText("Seleccione un médico...");
        }
    }

    // --- LÓGICA DE AGENDAMIENTO Y VALIDACIÓN ---

    private void agendarCita() {
        // 1. Validar Campos de Texto
        if (txtNombre.getText().trim().isEmpty() || txtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre y cédula del paciente.");
            return;
        }

        // 2. Validar Selección Médico
        Medico medicoSeleccionado = listaMedicos.getSelectedValue();
        if (medicoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un médico de la lista.");
            return;
        }

        // 3. Obtener Fecha del DateChooser y Hora del Spinner
        Date fechaParte = dateChooser.getDate(); // <-- CAMBIO AQUÍ: Obtenemos del JDateChooser
        
        if (fechaParte == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha válida.");
            return;
        }

        Date horaParte = (Date) spinHora.getValue();
        
        // 4. Combinar Fecha y Hora
        Calendar calFecha = Calendar.getInstance();
        calFecha.setTime(fechaParte);
        
        Calendar calHora = Calendar.getInstance();
        calHora.setTime(horaParte);
        
        calFecha.set(Calendar.HOUR_OF_DAY, calHora.get(Calendar.HOUR_OF_DAY));
        calFecha.set(Calendar.MINUTE, calHora.get(Calendar.MINUTE));
        
        Date fechaFinal = calFecha.getTime();

        // 5. Validar que no sea en el pasado
        // Truco: Comparar con "ahora" menos unos segundos para evitar problemas de clic rápido
        if (fechaFinal.before(new Date(System.currentTimeMillis() - 60000))) {
            JOptionPane.showMessageDialog(this, "Error: No puede agendar citas en el pasado.");
            return;
        }

        // 6. VALIDAR DISPONIBILIDAD (CUPO)
        if (!hayCupoDisponible(medicoSeleccionado, fechaFinal)) {
            return; 
        }

        // 7. CREAR CITA
        SimpleDateFormat sdfHora = new SimpleDateFormat("hh:mm a");
        String horaString = sdfHora.format(fechaFinal);

        Cita nuevaCita = new Cita(
            txtNombre.getText().trim(),
            txtCedula.getText().trim(),
            medicoSeleccionado,
            fechaFinal, 
            horaString  
        );

        // 8. GUARDAR
        ClinicaControladora.getInstance().registrarCita(nuevaCita);
        
        JOptionPane.showMessageDialog(this, "¡Cita Confirmada!\nMédico: " + medicoSeleccionado.getNombre() +
                "\nFecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fechaFinal));
        dispose();
    }

    private boolean hayCupoDisponible(Medico medico, Date fechaCita) {
        ClinicaControladora controller = ClinicaControladora.getInstance();
        ArrayList<Cita> citas = controller.getCitas();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String diaNuevo = sdf.format(fechaCita);
        
        int contador = 0;
        
        if (citas != null) {
            for (Cita c : citas) {
                if (c.getMedico().getCedula().equals(medico.getCedula())) {
                    String diaExistente = sdf.format(c.getFecha());
                    if (diaNuevo.equals(diaExistente)) {
                        if (c.getEstado() != EstadoCita.CANCELADA) {
                            contador++;
                        }
                    }
                }
            }
        }
        
        if (contador >= medico.getCitasPorDia()) {
            JOptionPane.showMessageDialog(this, 
                "AGENDA LLENA para este día.\n" +
                "El Dr. " + medico.getNombre() + " ya tiene " + contador + 
                " citas (Máximo: " + medico.getCitasPorDia() + ").\n" +
                "Por favor seleccione otra fecha.", 
                "Sin Disponibilidad", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
}