package vistas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlador.ClinicaControladora;
import modelos.Paciente;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegClientePrt2 extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JRadioButton rdbtnNingunaA;
	private JRadioButton rdbtnAPolen;
	private JRadioButton rdbtnAAcaros;
	private JRadioButton rdbtnApelaje;
	private JRadioButton rdbtnAmani;
	private JRadioButton rdbtnAnueses;
	private JRadioButton rdbtnAabejas;
	private JRadioButton rdbtnNingunaE;
	private JRadioButton rdbtnEanemia;
	private JRadioButton rdbtnEhemofilia;
	private JRadioButton rdbtnEdistroMuscular;
	private JRadioButton rdbtnEtalasemia;
	private JRadioButton rdbtnEfibrosis;
	private JRadioButton rdbtnEhipercolesterol;
	private JComboBox cbxSangre;
	private JTextField txtAltura;
	private JTextField txtPeso;
	private JTextField txtalergiasExtra;
	private JTextField txtenfermedadesExtra;

	/**
	 * Launch the application.
	 */
	public static void main(Paciente p1) {
		try {
			RegClientePrt2 dialog = new RegClientePrt2(p1);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegClientePrt2(Paciente p1) {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);
			
			JLabel lblNewLabel = new JLabel("Tipo de Sangre:");
			lblNewLabel.setBounds(10, 11, 91, 14);
			panel.add(lblNewLabel);
			
			cbxSangre = new JComboBox();
			cbxSangre.setModel(new DefaultComboBoxModel (new String[] {"<Seleccione>", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-","Golden Blood"}));
			cbxSangre.setBounds(10, 30, 109, 20);
			panel.add(cbxSangre);
			
			JLabel lblNewLabel_1 = new JLabel("Altura:");
			lblNewLabel_1.setBounds(10, 79, 46, 14);
			panel.add(lblNewLabel_1);
			
			JLabel lblPeso = new JLabel("Peso:");
			lblPeso.setBounds(10, 147, 46, 14);
			panel.add(lblPeso);
			
			txtAltura = new JTextField();
			txtAltura.setBounds(10, 99, 55, 20);
			panel.add(txtAltura);
			txtAltura.setColumns(10);
			
			txtPeso = new JTextField();
			txtPeso.setBounds(10, 163, 55, 20);
			panel.add(txtPeso);
			txtPeso.setColumns(10);
			
			JLabel lblNewLabel_3 = new JLabel("Enfermedades Heredadas:");
			lblNewLabel_3.setBounds(276, 11, 138, 14);
			panel.add(lblNewLabel_3);
			
			JLabel lblNewLabel_4 = new JLabel("Alergias:");
			lblNewLabel_4.setBounds(123, 11, 127, 14);
			panel.add(lblNewLabel_4);
			
			rdbtnAPolen = new JRadioButton("Polen");
			rdbtnAPolen.setBounds(123, 52, 75, 23);
			panel.add(rdbtnAPolen);
			
			rdbtnAAcaros = new JRadioButton("Acaros");
			rdbtnAAcaros.setBounds(123, 75, 66, 23);
			panel.add(rdbtnAAcaros);
			
			rdbtnApelaje = new JRadioButton("Pelaje Animal");
			rdbtnApelaje.setBounds(123, 98, 109, 23);
			panel.add(rdbtnApelaje);
			
			rdbtnNingunaA = new JRadioButton("Ninguna");
			rdbtnNingunaA.setBounds(123, 29, 75, 23);
			panel.add(rdbtnNingunaA);
			
			rdbtnAmani = new JRadioButton("Mani");
			rdbtnAmani.setBounds(123, 120, 109, 23);
			panel.add(rdbtnAmani);
			
			rdbtnAnueses = new JRadioButton("Nueses");
			rdbtnAnueses.setBounds(123, 143, 109, 23);
			panel.add(rdbtnAnueses);
			
			rdbtnAabejas = new JRadioButton("Abejas");
			rdbtnAabejas.setBounds(123, 162, 109, 23);
			panel.add(rdbtnAabejas);
			
			txtalergiasExtra = new JTextField();
			txtalergiasExtra.setBounds(128, 187, 128, 20);
			txtalergiasExtra.setText("Extra...");
			panel.add(txtalergiasExtra);
			txtalergiasExtra.setColumns(10);
			
			rdbtnEfibrosis = new JRadioButton("Fibrosis");
			rdbtnEfibrosis.setBounds(276, 143, 109, 23);
			panel.add(rdbtnEfibrosis);
			
			rdbtnEanemia = new JRadioButton("Anemia");
			rdbtnEanemia.setBounds(276, 52, 109, 23);
			panel.add(rdbtnEanemia);
			
			rdbtnEhemofilia = new JRadioButton("Hemofilia");
			rdbtnEhemofilia.setBounds(276, 75, 109, 23);
			panel.add(rdbtnEhemofilia);
			
			rdbtnEdistroMuscular = new JRadioButton("Distrofia muscular");
			rdbtnEdistroMuscular.setBounds(276, 98, 138, 23);
			panel.add(rdbtnEdistroMuscular);
			
			rdbtnEtalasemia = new JRadioButton("Talasemia");
			rdbtnEtalasemia.setBounds(276, 120, 109, 23);
			panel.add(rdbtnEtalasemia);
			
			rdbtnEhipercolesterol = new JRadioButton("Hipercolesterolemia");
			rdbtnEhipercolesterol.setBounds(276, 162, 138, 23);
			panel.add(rdbtnEhipercolesterol);
			
			txtenfermedadesExtra = new JTextField();
			txtenfermedadesExtra.setBounds(276, 187, 128, 20);
			txtenfermedadesExtra.setText("Extra...");
			panel.add(txtenfermedadesExtra);
			txtenfermedadesExtra.setColumns(10);
			
			rdbtnNingunaE = new JRadioButton("Ninguna");
			rdbtnNingunaE.setBounds(276, 29, 109, 23);
			panel.add(rdbtnNingunaE);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton Registrar = new JButton("Registrar");
				Registrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						p1.setTipoSangre(cbxSangre.getSelectedObjects().toString());
						p1.setEstatura(Double.parseDouble(txtAltura.getText()));
						p1.setPeso(Double.parseDouble(txtPeso.getText()));
						if(rdbtnNingunaA.isSelected()) {
							p1.agregarAlergia(rdbtnNingunaA.getText().toString());
						}
						else if(rdbtnNingunaA.isSelected() == false){
							if(rdbtnAPolen.isSelected()) {
								p1.agregarAlergia(rdbtnAPolen.getText().toString());
							}
							if(rdbtnAAcaros.isSelected()) {
								p1.agregarAlergia(rdbtnAAcaros.getText().toString());
							}
							if(rdbtnApelaje.isSelected()) {
								p1.agregarAlergia(rdbtnApelaje.getText().toString());
							}
							if(rdbtnAmani.isSelected()) {
								p1.agregarAlergia(rdbtnAmani.getText().toString());
							}
							if(rdbtnAnueses.isSelected()) {
								p1.agregarAlergia(rdbtnAnueses.getText().toString());
							}
							if(rdbtnAabejas.isSelected()) {
								p1.agregarAlergia(rdbtnAabejas.getText().toString());
							}
							if(txtalergiasExtra.getText() != "Extra...") {
								p1.agregarAlergia(txtalergiasExtra.getText().toString());
							}
						}
						if(rdbtnNingunaE.isSelected()) {
							p1.agregarEnfermedad(rdbtnNingunaE.getText().toString());
						}
						else if(rdbtnNingunaE.isSelected() == false){
							if(rdbtnEanemia.isSelected()) {
								p1.agregarEnfermedad(rdbtnEanemia.getText().toString());								
							}
							if(rdbtnEhemofilia.isSelected()) {
								p1.agregarEnfermedad(rdbtnEhemofilia.getText().toString());								
							}
							if(rdbtnEdistroMuscular.isSelected()) {
								p1.agregarEnfermedad(rdbtnEdistroMuscular.getText().toString());								
							}
							if(rdbtnEtalasemia.isSelected()) {
								p1.agregarEnfermedad(rdbtnEtalasemia.getText().toString());								
							}
							if(rdbtnEfibrosis.isSelected()) {
								p1.agregarEnfermedad(rdbtnEfibrosis.getText().toString());								
							}
							if(rdbtnEhipercolesterol.isSelected()) {
								p1.agregarEnfermedad(rdbtnEhipercolesterol.getText().toString());								
							}
							if(txtenfermedadesExtra.getText() != "Extra...") {
								p1.agregarAlergia(txtenfermedadesExtra.getText().toString());
							}
						}
						
						ClinicaControladora.getInstance().registrarPaciente(p1);
					}
				});
				Registrar.setActionCommand("OK");
				buttonPane.add(Registrar);
				getRootPane().setDefaultButton(Registrar);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
}
