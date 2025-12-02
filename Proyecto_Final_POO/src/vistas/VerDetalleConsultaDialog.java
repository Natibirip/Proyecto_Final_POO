package vistas;

import modelos.Consulta;
import modelos.Enfermedad;
import modelos.Vacuna;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class VerDetalleConsultaDialog extends JDialog {

    private Consulta consulta;
    private final Color COLOR_FONDO = new Color(245, 245, 245);

    public VerDetalleConsultaDialog(Frame parent, Consulta consulta) {
        super(parent, "Detalle de Consulta - ID: " + consulta.getId(), true);
        this.consulta = consulta;
        
        setSize(700, 750);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        // 1. TÍTULO SUPERIOR
        JLabel lblTitulo = new JLabel("INFORME DE CONSULTA MÉDICA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 2. PANEL CENTRAL CON SCROLL
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JScrollPane scrollPrincipal = new JScrollPane(panelContenido);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPrincipal, BorderLayout.CENTER);

        // --- SECCIÓN A: DATOS GENERALES ---
        panelContenido.add(crearPanelDatosGenerales());
        panelContenido.add(Box.createVerticalStrut(15));

        // --- SECCIÓN B: EVALUACIÓN CLÍNICA ---
        panelContenido.add(crearPanelClinico());
        panelContenido.add(Box.createVerticalStrut(15));

        // --- SECCIÓN C: VIGILANCIA Y VACUNAS ---
        panelContenido.add(crearPanelAdicionales());

        // 3. BOTONES INFERIORES
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnExportar = new JButton("Exportar a Archivo");
        btnExportar.setBackground(new Color(100, 149, 237));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.addActionListener(e -> exportarReporte());

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnExportar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelDatosGenerales() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(new TitledBorder("Datos Generales"));
        panel.setBackground(COLOR_FONDO);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fechaStr = sdf.format(consulta.getFechaConsulta());

        panel.add(new JLabel("Fecha y Hora:"));
        panel.add(new JLabel(fechaStr));

        panel.add(new JLabel("Paciente:"));
        panel.add(new JLabel(consulta.getPaciente().getNombre() + " (Ced: " + consulta.getPaciente().getCedula() + ")"));

        panel.add(new JLabel("Médico Tratante:"));
        panel.add(new JLabel(consulta.getMedico().getNombre() + " (" + consulta.getMedico().getEspecialidad() + ")"));

        panel.add(new JLabel("Peso Registrado:"));
        panel.add(new JLabel(consulta.getPesoRegistrado() + " kg"));

        return panel;
    }

    private JPanel crearPanelClinico() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Evaluación Clínica"));
        panel.setBackground(Color.WHITE);

        // Síntomas
        panel.add(crearLabelTitulo("SÍNTOMAS / MOTIVO:"));
        JTextArea txtSintomas = crearTextAreaLectura(consulta.getSintomas());
        panel.add(txtSintomas);
        panel.add(Box.createVerticalStrut(10));

        // Diagnóstico
        panel.add(crearLabelTitulo("DIAGNÓSTICO:"));
        JTextArea txtDiag = crearTextAreaLectura(consulta.getDiagnostico());
        txtDiag.setForeground(new Color(0, 0, 139)); // Azul oscuro
        panel.add(txtDiag);
        panel.add(Box.createVerticalStrut(10));

        // Tratamiento
        panel.add(crearLabelTitulo("TRATAMIENTO E INDICACIONES:"));
        JTextArea txtTrat = crearTextAreaLectura(consulta.getTratamiento());
        panel.add(txtTrat);

        return panel;
    }

    private JPanel crearPanelAdicionales() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(new TitledBorder("Información Adicional y Epidemiológica"));
        
        // Enfermedad
        Enfermedad enf = consulta.getEnfermedadVigilada();
        String enfStr = (enf != null) ? enf.getNombre() + " (Vigilada)" : "Ninguna";
        
        // Vacuna
        Vacuna vac = consulta.getVacunaAplicada();
        String vacStr = (vac != null) ? vac.getNombre() + " (Lote: " + vac.getLote() + ")" : "Ninguna";

        // Visibilidad
        String visibilidad = consulta.isEsPublica() ? "PÚBLICA" : "PRIVADA (Solo médico tratante)";
        if(enf != null) visibilidad += " (Pública por Vigilancia)";

        panel.add(new JLabel("Enfermedad Reportada:"));
        JLabel lblEnf = new JLabel(enfStr);
        if(enf != null) lblEnf.setForeground(Color.RED);
        panel.add(lblEnf);

        panel.add(new JLabel("Vacuna Aplicada:"));
        JLabel lblVac = new JLabel(vacStr);
        if(vac != null) lblVac.setForeground(new Color(0, 100, 0)); // Verde
        panel.add(lblVac);

        panel.add(new JLabel("Visibilidad del Registro:"));
        panel.add(new JLabel(visibilidad));

        return panel;
    }

    // --- MÉTODOS AUXILIARES UI ---

    private JLabel crearLabelTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextArea crearTextAreaLectura(String texto) {
        JTextArea area = new JTextArea(texto);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false); // SOLO LECTURA
        area.setBackground(new Color(250, 250, 250));
        area.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        return area;
    }

    // --- LÓGICA DE EXPORTACIÓN ---

    private void exportarReporte() {
        String nombreArchivo = "Reporte_Consulta_" + consulta.getId() + ".txt";
        
        try (PrintWriter out = new PrintWriter(new FileWriter(nombreArchivo))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            out.println("==========================================");
            out.println("       REPORTE DE CONSULTA MÉDICA         ");
            out.println("==========================================");
            out.println("ID Consulta: " + consulta.getId());
            out.println("Fecha: " + sdf.format(consulta.getFechaConsulta()));
            out.println("------------------------------------------");
            out.println("PACIENTE: " + consulta.getPaciente().getNombre());
            out.println("CÉDULA:   " + consulta.getPaciente().getCedula());
            out.println("PESO:     " + consulta.getPesoRegistrado() + " kg");
            out.println("------------------------------------------");
            out.println("MÉDICO:   " + consulta.getMedico().getNombreConTitulo());
            out.println("------------------------------------------");
            out.println("\n[SÍNTOMAS]");
            out.println(consulta.getSintomas());
            out.println("\n[DIAGNÓSTICO]");
            out.println(consulta.getDiagnostico());
            out.println("\n[TRATAMIENTO / INDICACIONES]");
            out.println(consulta.getTratamiento());
            out.println("\n------------------------------------------");
            
            if(consulta.getEnfermedadVigilada() != null) {
                out.println("VIGILANCIA: " + consulta.getEnfermedadVigilada().getNombre());
            }
            if(consulta.getVacunaAplicada() != null) {
                out.println("VACUNA APLICADA: " + consulta.getVacunaAplicada().getNombre());
            }
            
            out.println("\n\nFirma del Médico");
            out.println("_________________________");

            JOptionPane.showMessageDialog(this, "Reporte exportado correctamente:\n" + nombreArchivo);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + e.getMessage());
        }
    }
}