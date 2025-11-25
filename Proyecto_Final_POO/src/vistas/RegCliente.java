package vistas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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


import modelos.Paciente;


import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class RegCliente extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtCedula;
	private JTextField txtNombre;
	private JTextField txtFecha;
	private JTextField txtTelefono;
	private static Date fechaN = new Date();
	private static Date fechaToday = new Date();
	private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	private JRadioButton rdbtnMujer;
	private JRadioButton rdbtnHombre;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegCliente dialog = new RegCliente();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegCliente() {
		setTitle("Registrar Clientes");
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
			lblNewLabel_1.setBounds(10, 77, 46, 14);
			panel.add(lblNewLabel_1);
			
			JLabel lblNewLabel_2 = new JLabel("Sexo:");
			lblNewLabel_2.setBounds(10, 136, 46, 14);
			panel.add(lblNewLabel_2);
			
			JLabel lblNewLabel_3 = new JLabel("Fecha de nacimiento:");
			lblNewLabel_3.setBounds(207, 11, 173, 14);
			panel.add(lblNewLabel_3);
			
			txtCedula = new JTextField();
			txtCedula.setBounds(10, 36, 132, 20);
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
			txtNombre.setBounds(10, 105, 148, 20);
			panel.add(txtNombre);
			txtNombre.setColumns(10);
			
			txtFecha = new JTextField();
			txtFecha.setBounds(207, 36, 86, 20);
			String FechaFormat = formato.format(fechaN);
			txtFecha.setText(FechaFormat);
			panel.add(txtFecha);
			txtFecha.setColumns(10);
			
			JLabel lblNewLabel_4 = new JLabel("Telefono:");
			lblNewLabel_4.setBounds(207, 77, 86, 14);
			panel.add(lblNewLabel_4);
			
			txtTelefono = new JTextField();
			txtTelefono.setBounds(207, 105, 86, 20);
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
			rdbtnHombre.setBounds(10, 157, 80, 23);
			
			panel.add(rdbtnHombre);
			
			rdbtnMujer = new JRadioButton("Mujer");
			rdbtnMujer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(rdbtnMujer.isSelected()) {
						rdbtnHombre.setSelected(false);
					}
				}
			});
			
			rdbtnMujer.setBounds(92, 157, 86, 23);
			panel.add(rdbtnMujer);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnRegistrar = new JButton("Continuar");
				btnRegistrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						try {
				            
				            Date fechaNaci = formato.parse(txtFecha.getText());
				          
				           
				            char sexo;
				            if(rdbtnHombre.isSelected()) {
				            	sexo = 'M';				           
				            	Paciente p1 = new  Paciente(txtCedula.getText(), txtNombre.getText(), txtTelefono.getText(), fechaNaci, sexo, fechaToday, null, null, null, false);	
				            	RegClientePrt2 regpar2 = new RegClientePrt2(p1);
				            }
				            else if(rdbtnMujer.isSelected()) {
				            	sexo = 'F';
				            	Paciente p1 = new  Paciente(txtCedula.getText(), txtNombre.getText(), txtTelefono.getText(), fechaNaci, sexo, fechaToday, null, null, null, false);		
				            	RegClientePrt2 regpar2 = new RegClientePrt2(p1);
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
