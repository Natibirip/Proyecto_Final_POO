package vistas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import controlador.ClinicaControladora;
import modelos.Paciente;

public class RegClientePrt2 extends JDialog {

    private final JPanel contentPanel = new JPanel();
    
    // Objeto paciente en proceso (si viene de una cita)
    private Paciente pacienteEnProceso;

    // --- CAMPOS ---
    private JTextField txtNombre;
    private JTextField txtCedula;
    private JTextField txtTelefono;
    private JFormattedTextField txtFechaNac; 
    
    private JRadioButton rdbtnNingunaA, rdbtnAPolen, rdbtnAAcaros, rdbtnApelaje, rdbtnAmani, rdbtnAnueses, rdbtnAabejas;
    private JRadioButton rdbtnNingunaE, rdbtnEanemia, rdbtnEhemofilia, rdbtnEdistroMuscular, rdbtnEtalasemia, rdbtnEfibrosis, rdbtnEhipercolesterol;
    private JComboBox<String> cbxSangre;
    private JTextField txtAltura;
    private JTextField txtPeso;
    private JTextField txtalergiasExtra;
    private JTextField txtenfermedadesExtra;
    private JComboBox<String> boxSexo;

    /**
     * Constructor por defecto (para pruebas o registro manual)
     */
    public RegClientePrt2() {
        this(null); // Llama al constructor principal con null
    }

    /**
     * Constructor Principal (Integrado con Flujo de Citas)
     * @param pacientePrelleno Objeto con Nombre y Cédula ya cargados desde la Cita
     */
    public RegClientePrt2(Paciente pacientePrelleno) {
        this.pacienteEnProceso = pacientePrelleno;

        // Configuración Ventana
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, 1024, 720);
        setResizable(false);
        setTitle("Registro de Paciente");
        setModal(true); // Importante para que el flujo de Cita espere
        
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        inicializarComponentes();
        
        // --- PRE-LLENADO AUTOMÁTICO ---
        if (this.pacienteEnProceso != null) {
            if (this.pacienteEnProceso.getNombre() != null) {
                txtNombre.setText(this.pacienteEnProceso.getNombre());
            }
            if (this.pacienteEnProceso.getCedula() != null) {
                txtCedula.setText(this.pacienteEnProceso.getCedula());
                txtCedula.setEditable(false); // Bloquear cédula para consistencia con la cita
            }
        }
    }

    private void inicializarComponentes() {
        // --- DATOS PERSONALES ---
        JLabel lblDatos = new JLabel("--- DATOS PERSONALES ---");
        lblDatos.setBounds(10, 10, 200, 20);
        contentPanel.add(lblDatos);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setBounds(10, 40, 120, 14);
        contentPanel.add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(10, 60, 200, 20);
        contentPanel.add(txtNombre);
        
        // Cédula
        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(230, 40, 120, 14);
        contentPanel.add(lblCedula);
        txtCedula = new JTextField();
        txtCedula.setBounds(230, 60, 150, 20);
        txtCedula.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        contentPanel.add(txtCedula);

        // Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setBounds(400, 40, 120, 14);
        contentPanel.add(lblTelefono);
        txtTelefono = new JTextField();
        txtTelefono.setBounds(400, 60, 150, 20);
        txtTelefono.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        contentPanel.add(txtTelefono);

        // Fecha Nacimiento
        JLabel lblFecha = new JLabel("Fecha Nac. (dd/mm/yyyy):");
        lblFecha.setBounds(570, 40, 180, 14);
        contentPanel.add(lblFecha);
        try {
            MaskFormatter formatoFecha = new MaskFormatter("##/##/####");
            formatoFecha.setPlaceholderCharacter('_');
            txtFechaNac = new JFormattedTextField(formatoFecha);
            txtFechaNac.setBounds(570, 60, 100, 20);
            contentPanel.add(txtFechaNac);
        } catch (ParseException e) { e.printStackTrace(); }

        // Sexo
        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setBounds(755, 40, 100, 14);
        contentPanel.add(lblSexo);
        
        boxSexo = new JComboBox<>();
        boxSexo.setModel(new DefaultComboBoxModel<>(new String[] {"<seleccione>", "Masculino", "Femenino"}));
        boxSexo.setBounds(755, 60, 109, 22);
        contentPanel.add(boxSexo);

        // --- DATOS FISICOS ---
        int yOffset = 100;
        JLabel lblFisicos = new JLabel("--- DATOS MÉDICOS ---");
        lblFisicos.setBounds(10, yOffset, 200, 20);
        contentPanel.add(lblFisicos);

        JLabel lblSangre = new JLabel("Tipo de Sangre:");
        lblSangre.setBounds(10, 126, 120, 23);
        contentPanel.add(lblSangre);
        
        cbxSangre = new JComboBox<>();
        cbxSangre.setModel(new DefaultComboBoxModel<>(new String[] {"A+", "A-", "B+", "B-", "AB+","AB-", "O+", "O-"}));
        cbxSangre.setBounds(10, 150, 55, 22);
        contentPanel.add(cbxSangre);
        
        JLabel lblAltura = new JLabel("Altura (m):");
        lblAltura.setBounds(10, yOffset + 80, 80, 14);
        contentPanel.add(lblAltura);
        txtAltura = new JTextField();
        txtAltura.setBounds(10, yOffset + 100, 55, 20);
        contentPanel.add(txtAltura);
        
        JLabel lblPeso = new JLabel("Peso (Lb):");
        lblPeso.setBounds(80, yOffset + 80, 80, 14);
        contentPanel.add(lblPeso);
        txtPeso = new JTextField();
        txtPeso.setBounds(80, yOffset + 100, 55, 20);
        contentPanel.add(txtPeso);

        // --- ALERGIAS ---
        JLabel lblAlergias = new JLabel("Alergias:");
        lblAlergias.setBounds(200, yOffset + 30, 127, 14);
        contentPanel.add(lblAlergias);
        
        rdbtnNingunaA = new JRadioButton("Ninguna"); 
        rdbtnNingunaA.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(rdbtnNingunaA.isSelected()) {
                	rdbtnAPolen.setSelected(false);
                	rdbtnAAcaros.setSelected(false);
                	rdbtnApelaje.setSelected(false);
                	rdbtnAmani.setSelected(false);
                	rdbtnAnueses.setSelected(false);
                	
                	txtalergiasExtra.setText("Extra...");
                }
        	}
        });rdbtnNingunaA.setBounds(200, yOffset + 50, 100, 23); contentPanel.add(rdbtnNingunaA);
        
        rdbtnAPolen = new JRadioButton("Polen"); 
        rdbtnAPolen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		  desctivarBtnNingunaA();
        	}
        });rdbtnAPolen.setBounds(200, yOffset + 75, 100, 23); contentPanel.add(rdbtnAPolen);
        rdbtnAAcaros = new JRadioButton("Acaros"); 
        rdbtnAAcaros.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 desctivarBtnNingunaA();
        	}
        });rdbtnAAcaros.setBounds(200, yOffset + 100, 100, 23); contentPanel.add(rdbtnAAcaros);
        rdbtnApelaje = new JRadioButton("Pelaje"); 
        rdbtnApelaje.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 desctivarBtnNingunaA();
        	}
        });rdbtnApelaje.setBounds(200, yOffset + 125, 100, 23); contentPanel.add(rdbtnApelaje);
        rdbtnAmani = new JRadioButton("Mani"); 
        rdbtnAmani.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 desctivarBtnNingunaA();
        	}
        });rdbtnAmani.setBounds(200, yOffset + 150, 100, 23); contentPanel.add(rdbtnAmani);
        rdbtnAnueses = new JRadioButton("Nueces"); 
        rdbtnAnueses.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 desctivarBtnNingunaA();
        	}
        });rdbtnAnueses.setBounds(200, yOffset + 175, 100, 23); contentPanel.add(rdbtnAnueses);
        rdbtnAabejas = new JRadioButton("Abejas"); 
        rdbtnAabejas.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		desctivarBtnNingunaA();
        	}
        });rdbtnAabejas.setBounds(200, yOffset + 200, 100, 23); contentPanel.add(rdbtnAabejas);
        
        txtalergiasExtra = new JTextField("Extra...");
        txtalergiasExtra.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 if(txtalergiasExtra.getText() != "Extra...") {
        			 desctivarBtnNingunaA();
        		 }
        	}
        });
        txtalergiasExtra.setBounds(200, yOffset + 230, 128, 20);
        contentPanel.add(txtalergiasExtra);

        // --- ENFERMEDADES ---
        JLabel lblEnfermedades = new JLabel("Enfermedades Heredadas:");
        lblEnfermedades.setBounds(400, yOffset + 30, 180, 14);
        contentPanel.add(lblEnfermedades);
        
        rdbtnNingunaE = new JRadioButton("Ninguna"); 
        rdbtnNingunaE.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(rdbtnNingunaE.isSelected()) {
                	rdbtnEanemia.setSelected(false);
                	rdbtnEhemofilia.setSelected(false);
                	rdbtnEhemofilia.setSelected(false);
                	rdbtnEdistroMuscular.setSelected(false);
                	rdbtnEtalasemia.setSelected(false);
                	rdbtnEfibrosis.setSelected(false);
                	rdbtnEhipercolesterol.setSelected(false);
                	txtenfermedadesExtra.setText("Extra...");
                }
        	}
        });rdbtnNingunaE.setBounds(400, yOffset + 50, 109, 23); contentPanel.add(rdbtnNingunaE);
        
        rdbtnEanemia = new JRadioButton("Anemia"); 
        rdbtnEanemia.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		desctivarBtnNingunaE();
        	}
        });rdbtnEanemia.setBounds(400, yOffset + 75, 109, 23); contentPanel.add(rdbtnEanemia);
        rdbtnEhemofilia = new JRadioButton("Hemofilia"); 
        rdbtnEhemofilia.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		desctivarBtnNingunaE();
        	}
        });rdbtnEhemofilia.setBounds(400, yOffset + 100, 109, 23); contentPanel.add(rdbtnEhemofilia);
        rdbtnEdistroMuscular = new JRadioButton("Distrofia"); 
        rdbtnEdistroMuscular.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		desctivarBtnNingunaE();
        	}
        });rdbtnEdistroMuscular.setBounds(400, yOffset + 125, 138, 23); contentPanel.add(rdbtnEdistroMuscular);
        rdbtnEtalasemia = new JRadioButton("Talasemia"); 
        rdbtnEtalasemia.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		desctivarBtnNingunaE();
        	}
        });rdbtnEtalasemia.setBounds(400, yOffset + 150, 109, 23); contentPanel.add(rdbtnEtalasemia);
        rdbtnEfibrosis = new JRadioButton("Fibrosis"); 
        rdbtnEfibrosis.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		desctivarBtnNingunaE();
        	}
        });rdbtnEfibrosis.setBounds(400, yOffset + 175, 109, 23); contentPanel.add(rdbtnEfibrosis);
        rdbtnEhipercolesterol = new JRadioButton("Hipercolesterol"); 
        rdbtnEhipercolesterol.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		desctivarBtnNingunaE();
        	}
        });rdbtnEhipercolesterol.setBounds(400, yOffset + 200, 138, 23); contentPanel.add(rdbtnEhipercolesterol);
        
        txtenfermedadesExtra = new JTextField("Extra...");
        txtenfermedadesExtra.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		desctivarBtnNingunaE();
        	}
        });
        txtenfermedadesExtra.setBounds(400, yOffset + 230, 128, 20);
        contentPanel.add(txtenfermedadesExtra);

        // --- BOTONES ---
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton btnRegistrar = new JButton("Registrar Paciente");
        btnRegistrar.addActionListener(e -> registrar()); 
        buttonPane.add(btnRegistrar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);
    }
    
    private void registrar() {
        try {
            // 1. Validaciones básicas
        	if(rdbtnNingunaA.isSelected()) {
        		if(txtalergiasExtra.getText() != "Extra...") {
        			desctivarBtnNingunaA();
        		}
        	}
        	if(rdbtnNingunaE.isSelected()) {
        		if(txtenfermedadesExtra.getText() != "Extra...") {
        			desctivarBtnNingunaE();
        		}
        	}
            if (txtNombre.getText().isEmpty() || txtCedula.getText().isEmpty() || txtTelefono.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete Nombre, Cédula y Teléfono");
                return;
            }
            
            if (boxSexo.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un sexo válido.");
                return;
            }

            // 2. Parseo de Fecha
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            Date fechaNac = sdf.parse(txtFechaNac.getText());
            
            // 3. Obtener Sexo
            char sexo = boxSexo.getSelectedItem().toString().charAt(0); // 'M' o 'F'

            // 4. CREAR O ACTUALIZAR OBJETO PACIENTE
            Paciente p;
            if (this.pacienteEnProceso != null) {
                p = this.pacienteEnProceso; // Usar el que venía de la cita
            } else {
                p = new Paciente(); // Crear uno nuevo si fue manual
            }
            
            p.setNombre(txtNombre.getText());
            p.setCedula(txtCedula.getText());
            p.setTelefono(txtTelefono.getText());
            p.setFechaNacimiento(fechaNac);
            p.setSexo(sexo);
            p.setFechaRegistro(new Date());

            // 5. Datos Físicos
            p.setTipoSangre(cbxSangre.getSelectedItem().toString());
            try {
                if(!txtAltura.getText().isEmpty()) p.setEstatura(Double.parseDouble(txtAltura.getText()));
                if(!txtPeso.getText().isEmpty()) p.setPeso(Double.parseDouble(txtPeso.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Altura y Peso deben ser numéricos.");
                return;
            }

            // 6. Alergias
            if(!rdbtnNingunaA.isSelected()){
                if(rdbtnAPolen.isSelected()) p.agregarAlergia("Polen");
                if(rdbtnAAcaros.isSelected()) p.agregarAlergia("Acaros");
                if(rdbtnApelaje.isSelected()) p.agregarAlergia("Pelaje Animal");
                if(rdbtnAmani.isSelected()) p.agregarAlergia("Mani");
                if(rdbtnAnueses.isSelected()) p.agregarAlergia("Nueces");
                if(rdbtnAabejas.isSelected()) p.agregarAlergia("Abejas");
                String extra = txtalergiasExtra.getText();
                if(!extra.equals("Extra...") && !extra.isEmpty()) p.agregarAlergia(extra);
            } else {
                p.agregarAlergia("Ninguna");
            }

            // 7. Enfermedades
            if(!rdbtnNingunaE.isSelected()){
                if(rdbtnEanemia.isSelected()) p.agregarEnfermedad("Anemia");
                if(rdbtnEhemofilia.isSelected()) p.agregarEnfermedad("Hemofilia");
                if(rdbtnEdistroMuscular.isSelected()) p.agregarEnfermedad("Distrofia");
                if(rdbtnEtalasemia.isSelected()) p.agregarEnfermedad("Talasemia");
                if(rdbtnEfibrosis.isSelected()) p.agregarEnfermedad("Fibrosis");
                if(rdbtnEhipercolesterol.isSelected()) p.agregarEnfermedad("Hipercolesterol");
                String extra = txtenfermedadesExtra.getText();
                if(!extra.equals("Extra...") && !extra.isEmpty()) p.agregarEnfermedad(extra);
            } else {
                p.agregarEnfermedad("Ninguna");
            }

            // 8. GUARDAR
            ClinicaControladora.getInstance().registrarPaciente(p);
            JOptionPane.showMessageDialog(this, "Paciente Registrado Exitosamente");
            dispose();

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Error en fecha. Use formato dd/mm/yyyy");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
     void desctivarBtnNingunaA(){
    	if(rdbtnNingunaA.isSelected()) {
    		rdbtnNingunaA.setSelected(false);
    	}
    }
     void desctivarBtnNingunaE(){
     	if(rdbtnNingunaE.isSelected()) {
     		rdbtnNingunaE.setSelected(false);
     	}
     }
}