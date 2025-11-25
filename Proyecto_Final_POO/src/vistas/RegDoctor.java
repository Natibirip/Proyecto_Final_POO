package vistas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controlador.ClinicaControladora;
import modelos.Medico;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class RegDoctor extends JDialog {

	private static final long serialVersionUID = 1L; 
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtCedula;
	private JTextField txtNombre;
	private JTextField txtFecha;
	private JTextField txtTelefono;
	private static Date fechaN = new Date();
	private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	private JRadioButton rdbtnMujer;
	private JRadioButton rdbtnHombre;
	private JTextField txtEspecialidad;
	private JSpinner spCitas;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegDoctor dialog = new RegDoctor();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegDoctor() {
		setTitle("Registrar Doctor");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);
			
			JLabel lblNewLabel = new JLabel("Cedula:");
			lblNewLabel.setBounds(10, 11, 46, 14);
			panel.add(lblNewLabel);
			
			JLabel lblNewLabel_1 = new JLabel("Nombre:");
			lblNewLabel_1.setBounds(10, 55, 95, 14);
			panel.add(lblNewLabel_1);
			
			JLabel lblNewLabel_2 = new JLabel("Sexo:");
			lblNewLabel_2.setBounds(10, 99, 46, 14);
			panel.add(lblNewLabel_2);
			
			JLabel lblNewLabel_3 = new JLabel("Fecha de nacimiento:");
			lblNewLabel_3.setBounds(207, 11, 173, 14);
			panel.add(lblNewLabel_3);
			
			txtCedula = new JTextField();
			txtCedula.setBounds(10, 24, 148, 20);
			txtCedula.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char letter = e.getKeyChar();
					if(!Character.isDigit(letter)) {
						e.consume();
					}
				}
			});
			panel.add(txtCedula);
			txtCedula.setColumns(10);
			
			txtNombre = new JTextField();
			txtNombre.setBounds(10, 68, 148, 20);
			panel.add(txtNombre);
			txtNombre.setColumns(10);
			
			txtFecha = new JTextField();
			txtFecha.setBounds(207, 24, 86, 20);
			String FechaFormat = formato.format(fechaN);
			txtFecha.setText(FechaFormat);
			panel.add(txtFecha);
			txtFecha.setColumns(10);
			
			JLabel lblNewLabel_4 = new JLabel("Telefono:");
			lblNewLabel_4.setBounds(207, 55, 86, 14);
			panel.add(lblNewLabel_4);
			
			txtTelefono = new JTextField();
			txtTelefono.setBounds(207, 68, 86, 20);
			txtTelefono.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char letter = e.getKeyChar();
					if(!Character.isDigit(letter)) {
						e.consume();
					}
				}
			});
			panel.add(txtTelefono);
			txtTelefono.setColumns(10);
			
			rdbtnHombre = new JRadioButton("Hombre");
			rdbtnHombre.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(rdbtnHombre.isSelected()) {
						rdbtnMujer.setSelected(false);
					}
				}
			});
			rdbtnHombre.setBounds(10, 120, 80, 23);
			
			panel.add(rdbtnHombre);
			
			rdbtnMujer = new JRadioButton("Mujer");
			rdbtnMujer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(rdbtnMujer.isSelected()) {
						rdbtnHombre.setSelected(false);
					}
				}
			});
			
			rdbtnMujer.setBounds(92, 120, 86, 23);
			panel.add(rdbtnMujer);
			
			JLabel lblNewLabel_5 = new JLabel("Especialidad:");
			lblNewLabel_5.setBounds(207, 99, 80, 14);
			panel.add(lblNewLabel_5);
			
			txtEspecialidad = new JTextField();
			txtEspecialidad.setBounds(207, 131, 107, 20);
			panel.add(txtEspecialidad);
			txtEspecialidad.setColumns(10);
			
			JLabel lblNewLabel_6 = new JLabel("Maximo de Citas por dia:");
			lblNewLabel_6.setBounds(10, 160, 148, 14);
			panel.add(lblNewLabel_6);
			
			spCitas = new JSpinner();
			spCitas.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
			spCitas.setBounds(10, 185, 29, 20);
			panel.add(spCitas);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnRegistrar = new JButton("Registrar");
				btnRegistrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						try {
				            
				            Date fechaNaci = formato.parse(txtFecha.getText());
				           
				            char sexo;
				            if(rdbtnHombre.isSelected()) {
				            	sexo = 'M';
				            	Medico med = new Medico(txtCedula.getText(), txtNombre.getText(), txtTelefono.getText(), fechaNaci, sexo, txtEspecialidad.getText());
				            	
				            	int citas = (int) spCitas.getValue();
				            	med.setCitasPorDia(citas);
				            	ClinicaControladora.getInstance().registrarMedico(med);
				            	
				            }
				            else if(rdbtnMujer.isSelected()) {
				            	sexo = 'F';
				            	Medico med = new Medico(txtCedula.getText(), txtNombre.getText(), txtTelefono.getText(), fechaNaci, sexo, txtEspecialidad.getText());
				            	
				            	int citas = (int) spCitas.getValue();
				            	med.setCitasPorDia(citas);
				            	ClinicaControladora.getInstance().registrarMedico(med);
				            	
				            }
				            
				        } 
						catch (Exception e) {
				            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use dd/MM/yyyy");
				        }
						
					}
				});
				btnRegistrar.setActionCommand("OK");
				buttonPane.add(btnRegistrar);
				getRootPane().setDefaultButton(btnRegistrar);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
