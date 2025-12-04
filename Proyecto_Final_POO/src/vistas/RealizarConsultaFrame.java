package vistas;

import controlador.ClinicaControladora;
import modelos.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class RealizarConsultaFrame extends JFrame {

    // Datos de contexto
    private Cita citaActual;
    private Paciente paciente;
    private Medico medico;
    private ClinicaControladora controller;

    // Componentes Consulta Generales
    private JTextArea txtSintomas;
    private JTextArea txtDiagnostico;
    private JTextArea txtTratamiento;
    private JTextArea txtExamenes; 
    private JSpinner spinLicencia; 
    
    private JComboBox<Enfermedad> cmbEnfermedadVigilada;
    private JCheckBox chkVaResumen;
    private JCheckBox chkHacerPublica; // NUEVO: Checkbox manual
    
    // Signos Vitales (Editables)
    private JTextField txtPesoEdit;    
    private JTextField txtAlturaEdit;  

    // Vacunas (Solo una)
    private JComboBox<Vacuna> cmbVacunaUnica; // NUEVO: Selector único

    // Historial
    private JTable tablaHistorial;
    private DefaultTableModel modeloHistorial;

    public RealizarConsultaFrame(Cita cita) {
        this.citaActual = cita;
        // IMPORTANTE: Aquí obtenemos el paciente que asociamos en la ventana anterior
        this.paciente = cita.getPaciente(); // getPacienteAsociado() en tu clase Cita
        this.medico = cita.getMedico();
        this.controller = ClinicaControladora.getInstance();

        setTitle("Consulta Médica - Dr. " + medico.getNombre() + " atendiendo a " + paciente.getNombre());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 1. PANEL SUPERIOR
        crearPanelInfoPaciente();

        // 2. PANEL IZQUIERDO (Historial actualizado)
        crearPanelHistorial();

        // 3. PANEL CENTRAL
        crearPanelDatosClinicos();

        // 4. PANEL DERECHO (Vacunas Simplificadas + Privacidad)
        crearPanelAcciones();

        // 5. BOTONES
        crearBotonera();
    }

    private void crearPanelInfoPaciente() {
        JPanel panelInfo = new JPanel(new GridLayout(2, 4, 10, 5));
        panelInfo.setBorder(new TitledBorder("Información y Signos Vitales (Actualizar si es necesario)"));
        panelInfo.setBackground(new Color(230, 240, 255));

        panelInfo.add(new JLabel("Nombre: " + paciente.getNombre()));
        panelInfo.add(new JLabel("Cédula: " + paciente.getCedula()));
        panelInfo.add(new JLabel("Edad: " + paciente.getEdad() + " años"));
        
        // Manejo seguro de alergias null
        String textoAlergias = "Ninguna";
        if (paciente.getAlergias() != null && !paciente.getAlergias().isEmpty()) {
            textoAlergias = paciente.getAlergias().toString();
        }
        JLabel lblAlergias = new JLabel("Alergias: " + textoAlergias);
        lblAlergias.setForeground(Color.RED);
        panelInfo.add(lblAlergias);

        // Editables
        JPanel panelPeso = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelPeso.setOpaque(false);
        panelPeso.add(new JLabel("Peso (kg): "));
        txtPesoEdit = new JTextField(String.valueOf(paciente.getPeso()), 5);
        panelPeso.add(txtPesoEdit);
        panelInfo.add(panelPeso);

        JPanel panelAltura = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelAltura.setOpaque(false);
        panelAltura.add(new JLabel("Altura (m): "));
        txtAlturaEdit = new JTextField(String.valueOf(paciente.getEstatura()), 5);
        panelAltura.add(txtAlturaEdit);
        panelInfo.add(panelAltura);
        
        panelInfo.add(new JLabel("Tipo Sangre: " + paciente.getTipoSangre()));
        panelInfo.add(new JLabel("")); 

        add(panelInfo, BorderLayout.NORTH);
    }

    private void crearPanelHistorial() {
        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.setBorder(new TitledBorder("Historial Médico"));
        panelHistorial.setPreferredSize(new Dimension(320, 0)); 

        // --- ACTUALIZACIÓN: COLUMNAS SOLICITADAS ---
        String[] cols = {"Fecha", "Peso (kg)", "Diagnóstico"};
        
        modeloHistorial = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaHistorial = new JTable(modeloHistorial);
        
        // Ajustar anchos de columna
        tablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(70); // Fecha
        tablaHistorial.getColumnModel().getColumn(1).setPreferredWidth(60); // Peso
        tablaHistorial.getColumnModel().getColumn(2).setPreferredWidth(150); // Diagnóstico
        
        cargarHistorialFiltrado();

        panelHistorial.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        add(panelHistorial, BorderLayout.WEST);
    }

    private void cargarHistorialFiltrado() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<Consulta> todas = controller.getConsultas();
        
        // Limpiar tabla por si acaso
        modeloHistorial.setRowCount(0);

        if (todas != null) {
            for (Consulta c : todas) {
                // 1. Filtro por paciente
                if (c.getPaciente().getCedula().equals(paciente.getCedula())) {
                    
                    // 2. Filtro de Visibilidad (Lógica actualizada en clase Consulta)
                    if (c.esVisiblePara(this.medico)) {
                        modeloHistorial.addRow(new Object[]{
                            sdf.format(c.getFechaConsulta()),
                            c.getPesoRegistrado(), // Muestra el peso histórico
                            c.getDiagnostico()
                        });
                    }
                }
            }
        }
    }

    private void crearPanelDatosClinicos() {
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(new TitledBorder("Registro Clínico Actual"));

        panelCentral.add(new JLabel("1. Síntomas / Motivo Consulta:"));
        txtSintomas = new JTextArea(3, 20);
        txtSintomas.setLineWrap(true);
        panelCentral.add(new JScrollPane(txtSintomas));

        panelCentral.add(Box.createVerticalStrut(10));

        panelCentral.add(new JLabel("2. Diagnóstico Médico:"));
        txtDiagnostico = new JTextArea(3, 20);
        txtDiagnostico.setLineWrap(true);
        panelCentral.add(new JScrollPane(txtDiagnostico));

        panelCentral.add(Box.createVerticalStrut(10));

        panelCentral.add(new JLabel("3. Tratamiento / Receta:"));
        txtTratamiento = new JTextArea(4, 20);
        txtTratamiento.setLineWrap(true);
        panelCentral.add(new JScrollPane(txtTratamiento));
        
        panelCentral.add(Box.createVerticalStrut(10));
        
        panelCentral.add(new JLabel("4. Órdenes de Laboratorio / Exámenes:"));
        txtExamenes = new JTextArea(2, 20);
        txtExamenes.setLineWrap(true);
        txtExamenes.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelCentral.add(new JScrollPane(txtExamenes));
        
        panelCentral.add(Box.createVerticalStrut(10));

        JPanel panelLicencia = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLicencia.add(new JLabel("5. Días de Reposo (Licencia Médica):"));
        spinLicencia = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
        panelLicencia.add(spinLicencia);
        panelCentral.add(panelLicencia);

        add(panelCentral, BorderLayout.CENTER);
    }

    private void crearPanelAcciones() {
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBorder(new TitledBorder("Acciones y Privacidad"));
        panelDerecho.setPreferredSize(new Dimension(260, 0));

        // 1. VIGILANCIA
        panelDerecho.add(new JLabel("Enfermedad Vigilada (Pública):"));
        cmbEnfermedadVigilada = new JComboBox<>();
        cmbEnfermedadVigilada.addItem(null); // Ninguna
        
        ArrayList<Enfermedad> enfermedades = controller.getEnfermedadesVigiladas();
        if (enfermedades != null) {
            for (Enfermedad e : enfermedades) {
                if (e.isActiva()) cmbEnfermedadVigilada.addItem(e); 
            }
        }
        panelDerecho.add(cmbEnfermedadVigilada);
        panelDerecho.add(new JLabel("<html><font size='2' color='gray'>*Seleccionar una enfermedad hace<br>la consulta pública automáticamente.</font></html>"));

        panelDerecho.add(Box.createVerticalStrut(20));

        // 2. VACUNA (SOLO UNA)
        panelDerecho.add(new JLabel("Aplicar Vacuna (Solo 1):"));
        cmbVacunaUnica = new JComboBox<>();
        cmbVacunaUnica.addItem(null); // Opción "Ninguna" o "No aplicar"
        
        ArrayList<Vacuna> vacunas = controller.getInventarioVacunas();
        if (vacunas != null) {
            for (Vacuna v : vacunas) {
                cmbVacunaUnica.addItem(v);
            }
        }
        panelDerecho.add(cmbVacunaUnica);
        
        panelDerecho.add(Box.createVerticalStrut(30));
        
        // 3. OPCIONES DE VISIBILIDAD
        panelDerecho.add(new JLabel("--- Opciones de Registro ---"));
        
        chkVaResumen = new JCheckBox("Marcar para Resumen Clínico");
        panelDerecho.add(chkVaResumen);
        
        // Checkbox "Hacer Pública"
        chkHacerPublica = new JCheckBox("Hacer consulta Pública");
        chkHacerPublica.setToolTipText("Permite que otros médicos vean esta consulta aunque no haya enfermedad vigilada.");
        panelDerecho.add(chkHacerPublica);

        // 4. IMPRIMIR
        panelDerecho.add(Box.createVerticalGlue()); 
        JButton btnImprimir = new JButton("🖨️ Generar Receta (PDF/Txt)");
        btnImprimir.setBackground(new Color(255, 228, 196));
        btnImprimir.setToolTipText("Guarda un archivo de texto con las indicaciones");
        btnImprimir.addActionListener(e -> imprimirReceta());
        panelDerecho.add(btnImprimir);
        
        add(panelDerecho, BorderLayout.EAST);
    }

    private void crearBotonera() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnFinalizar = new JButton("FINALIZAR CONSULTA");
        btnFinalizar.setBackground(new Color(0, 100, 0));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnCancelar = new JButton("Cancelar");

        btnFinalizar.addActionListener(e -> finalizarConsulta());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnFinalizar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // --- LÓGICA DE NEGOCIO ---

    private void finalizarConsulta() {
        if (txtSintomas.getText().trim().isEmpty() || txtDiagnostico.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar síntomas y diagnóstico.");
            return;
        }

        double nuevoPeso = paciente.getPeso();
        double nuevaAltura = paciente.getEstatura();

        // 1. ACTUALIZAR SIGNOS VITALES
        try {
            nuevoPeso = Double.parseDouble(txtPesoEdit.getText());
            nuevaAltura = Double.parseDouble(txtAlturaEdit.getText());
            paciente.setPeso(nuevoPeso); // Actualiza al paciente actual
            paciente.setEstatura(nuevaAltura);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en Peso o Altura. Use formato numérico.");
            return;
        }

        // 2. DATOS EXTRA
        String tratamientoFinal = txtTratamiento.getText();
        String examenes = txtExamenes.getText().trim();
        int diasLicencia = (int) spinLicencia.getValue();

        if (!examenes.isEmpty()) tratamientoFinal += "\n\n[ÓRDENES DE LAB]: " + examenes;
        if (diasLicencia > 0) tratamientoFinal += "\n\n[LICENCIA MÉDICA]: " + diasLicencia + " días.";

        // 3. RECOPILAR DATOS DE LA GUI
        String id = UUID.randomUUID().toString();
        Enfermedad enfVigilada = (Enfermedad) cmbEnfermedadVigilada.getSelectedItem();
        boolean esResumen = chkVaResumen.isSelected();
        boolean esPublica = chkHacerPublica.isSelected();
        Vacuna vacunaSel = (Vacuna) cmbVacunaUnica.getSelectedItem(); // Puede ser null

        // 4. CREAR CONSULTA (Con el nuevo constructor actualizado)
        Consulta nueva = new Consulta(
            id,
            paciente,
            medico,
            new Date(),
            txtSintomas.getText(),
            txtDiagnostico.getText(),
            tratamientoFinal,
            nuevoPeso, // Guardamos el peso histórico (snapshot)
            enfVigilada,
            esResumen,
            esPublica, // Nuevo atributo
            vacunaSel, // Una sola vacuna (o null)
            citaActual
        );

        // 5. GUARDAR Y ACTUALIZAR
        controller.registrarConsulta(nueva);
        paciente.agregarConsulta(nueva);
        
        // Si se aplicó una vacuna, la marcamos en el paciente
        if (vacunaSel != null) {
            paciente.actualizarVacuna(vacunaSel, true);
        }
        
        // 6. ACTUALIZAR ESTADO DE LA CITA
        citaActual.setEstado(EstadoCita.REALIZADA); // Marcamos la cita como completada
        
        // Guardar cambios generales
        controller.guardarDatos(); 

        JOptionPane.showMessageDialog(this, "Consulta registrada y finalizada correctamente.");
        dispose();
    }

    private void imprimirReceta() {
        String nombreArchivo = "Receta_" + paciente.getCedula() + "_" + System.currentTimeMillis() + ".txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(nombreArchivo))) {
            out.println("====== CENTRO MÉDICO ======");
            out.println("Dr. " + medico.getNombre() + " | " + medico.getEspecialidad());
            out.println("Fecha: " + new Date());
            out.println("--------------------------------");
            out.println("PACIENTE: " + paciente.getNombre());
            out.println("PESO ACTUAL: " + txtPesoEdit.getText() + "kg"); 
            out.println("--------------------------------");
            out.println("\nDIAGNÓSTICO:");
            out.println(txtDiagnostico.getText());
            out.println("\nINDICACIONES:");
            out.println(txtTratamiento.getText());
            
            if (!txtExamenes.getText().isEmpty()) {
                out.println("\nEXÁMENES SOLICITADOS:");
                out.println(txtExamenes.getText());
            }
            
            int dias = (int) spinLicencia.getValue();
            if (dias > 0) {
                out.println("\n*** LICENCIA MÉDICA OTORGADA: " + dias + " DÍAS ***");
            }
            
            out.println("\n\n__________________________");
            out.println("Firma del Médico");
            
            JOptionPane.showMessageDialog(this, "Receta guardada en: " + nombreArchivo);
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al imprimir receta.");
        }
    }
}