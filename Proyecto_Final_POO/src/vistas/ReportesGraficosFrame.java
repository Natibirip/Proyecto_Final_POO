package vistas;

import controlador.ClinicaControladora;
import modelos.*;

// Imports de JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReportesGraficosFrame extends JFrame {

    private ClinicaControladora controller;

    public ReportesGraficosFrame() {
        this.controller = ClinicaControladora.getInstance();

        setTitle("Tablero de Inteligencia de Negocios (BI) - Clínica");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. TÍTULO
        JLabel lblTitulo = new JLabel("ESTADÍSTICAS Y TOMA DE DECISIONES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 2. PANEL PRINCIPAL CON SCROLL (Para evitar deformación)
        // Usamos un GridLayout de 2 columnas y filas dinámicas
        JPanel panelGrid = new JPanel(new GridLayout(0, 2, 20, 20)); 
        panelGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelGrid.setBackground(Color.WHITE);

        // --- AGREGAR GRÁFICOS AL GRID ---
        
        // Gráfico A: Epidemiología
        panelGrid.add(crearPanelEnfermedades());
        
        // Gráfico B: Estado de Citas (Embudo)
        panelGrid.add(crearPanelEstadoCitas());
        
        // Gráfico C: Productividad
        panelGrid.add(crearPanelProductividad());
        
        // Gráfico D: Demografía (Nuevo)
        panelGrid.add(crearPanelDemografia());
        
        // Gráfico E: Inventario Vacunas (Nuevo)
        panelGrid.add(crearPanelInventario());

        // Metemos el Grid en un ScrollPane para que si la pantalla es chica,
        // los gráficos mantengan su tamaño y no se aplasten.
        JScrollPane scroll = new JScrollPane(panelGrid);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // 3. BOTÓN CERRAR
        JButton btnCerrar = new JButton("Cerrar Tablero");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.addActionListener(e -> dispose());
        add(btnCerrar, BorderLayout.SOUTH);
    }

    // =========================================================
    // 1. EPIDEMIOLOGÍA (Barras Verticales)
    // Insight: ¿Hay brotes epidemiológicos?
    // =========================================================
    private JPanel crearPanelEnfermedades() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<Consulta> consultas = controller.getConsultas();
        ArrayList<DatoConteo> conteos = new ArrayList<>();

        if (consultas != null) {
            for (Consulta c : consultas) {
                Enfermedad enf = c.getEnfermedadVigilada();
                if (enf != null) {
                    contarDato(conteos, enf.getNombre());
                }
            }
        }

        for (DatoConteo d : conteos) {
            dataset.addValue(d.getValor(), "Casos", d.getEtiqueta());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Vigilancia Epidemiológica", "Enfermedad", "Total Casos",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        configurarBarRenderer(chart); // Aplica números visibles
        return encapsularEnPanel(chart, "Detecta brotes infecciosos activos.");
    }

    // =========================================================
    // 2. ESTADO DE CITAS (Pastel)
    // Insight: ¿Estamos perdiendo pacientes por cancelaciones?
    // =========================================================
    private JPanel crearPanelEstadoCitas() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        ArrayList<Cita> citas = controller.getCitas();
        ArrayList<DatoConteo> conteos = new ArrayList<>();

        if (citas != null) {
            for (Cita c : citas) {
                contarDato(conteos, c.getEstado().toString());
            }
        }

        for (DatoConteo d : conteos) {
            dataset.setValue(d.getEtiqueta(), d.getValor());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Eficiencia de Agenda", dataset, true, true, false);

        // Configurar etiquetas del Pastel (Valor y Porcentaje)
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%")));
        plot.setSimpleLabels(true); // Evita que las etiquetas se superpongan feo
        
        return encapsularEnPanel(chart, "Analiza el % de cancelaciones.");
    }

    // =========================================================
    // 3. PRODUCTIVIDAD MÉDICA (Barras Horizontales)
    // Insight: ¿Está balanceada la carga laboral?
    // =========================================================
    private JPanel crearPanelProductividad() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<Consulta> consultas = controller.getConsultas();
        ArrayList<DatoConteo> conteos = new ArrayList<>();

        if (consultas != null) {
            for (Consulta c : consultas) {
                String nombreMedico = "Dr. " + c.getMedico().getNombre();
                contarDato(conteos, nombreMedico);
            }
        }

        for (DatoConteo d : conteos) {
            dataset.addValue(d.getValor(), "Consultas", d.getEtiqueta());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Productividad por Médico", "Total Atenciones", "Médico",
                dataset, PlotOrientation.HORIZONTAL, false, true, false);

        configurarBarRenderer(chart);
        return encapsularEnPanel(chart, "Evalúa la carga de trabajo por doctor.");
    }

    // =========================================================
    // 4. DEMOGRAFÍA (Barras por Grupos de Edad) - NUEVO
    // Insight: ¿Nuestra clínica es pediátrica o geriátrica?
    // =========================================================
    private JPanel crearPanelDemografia() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<Paciente> pacientes = controller.getPacientes();
        
        // Buckets manuales
        int ninos = 0;   // 0-12
        int jovenes = 0; // 13-29
        int adultos = 0; // 30-59
        int mayores = 0; // 60+

        if (pacientes != null) {
            for (Paciente p : pacientes) {
                int edad = p.getEdad();
                if (edad <= 12) ninos++;
                else if (edad <= 29) jovenes++;
                else if (edad <= 59) adultos++;
                else mayores++;
            }
        }

        dataset.addValue(ninos, "Pacientes", "Niños (0-12)");
        dataset.addValue(jovenes, "Pacientes", "Jóvenes (13-29)");
        dataset.addValue(adultos, "Pacientes", "Adultos (30-59)");
        dataset.addValue(mayores, "Pacientes", "Adultos Mayores (60+)");

        JFreeChart chart = ChartFactory.createBarChart(
                "Demografía de Pacientes", "Rango de Edad", "Cantidad",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        configurarBarRenderer(chart);
        
        // Cambiar color de barras para diferenciarlo
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(255, 165, 0)); // Naranja

        return encapsularEnPanel(chart, "Define qué especialidades reforzar.");
    }

    // =========================================================
    // 5. INVENTARIO VACUNAS (Barras) - NUEVO
    // Insight: ¿Qué vacunas se están agotando?
    // =========================================================
    private JPanel crearPanelInventario() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<Vacuna> vacunas = controller.getInventarioVacunas();

        if (vacunas != null) {
            for (Vacuna v : vacunas) {
                if (v.isActiva()) {
                    dataset.addValue(v.getCantidad(), "Stock", v.getNombre());
                }
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Niveles de Stock de Vacunas", "Vacuna", "Dosis Disponibles",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        configurarBarRenderer(chart);
        
        // Poner una línea roja de "Peligro" visualmente o cambiar color si es bajo
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(34, 139, 34)); // Verde bosque

        return encapsularEnPanel(chart, "Monitoreo de insumos críticos.");
    }

    // =========================================================
    // MÉTODOS AUXILIARES DE DISEÑO
    // =========================================================

    /**
     * Aplica etiquetas numéricas visibles sobre las barras.
     */
    private void configurarBarRenderer(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        
        // Generador de etiquetas estándar (Muestra el valor numérico)
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelFont(new Font("SansSerif", Font.BOLD, 12));
        
        // Asegurar que el eje Y solo muestre enteros
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    }

    /**
     * Envuelve el gráfico en un panel con borde y descripción para que se vea profesional
     * y no se deforme (el ChartPanel mantiene ratio).
     */
    private JPanel encapsularEnPanel(JFreeChart chart, String descripcion) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        container.setBackground(Color.WHITE);

        // ChartPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300)); // Tamaño base para evitar "streched"
        container.add(chartPanel, BorderLayout.CENTER);

        // Label de Insight
        JLabel lblInfo = new JLabel("💡 " + descripcion);
        lblInfo.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblInfo.setForeground(Color.DARK_GRAY);
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfo.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        container.add(lblInfo, BorderLayout.SOUTH);

        return container;
    }

    // Utilería para contar sin Maps
    private void contarDato(ArrayList<DatoConteo> lista, String etiqueta) {
        boolean encontrado = false;
        for (int i = 0; i < lista.size(); i++) {
            DatoConteo d = lista.get(i);
            if (d.getEtiqueta().equals(etiqueta)) {
                d.incrementar();
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            lista.add(new DatoConteo(etiqueta, 1));
        }
    }
}