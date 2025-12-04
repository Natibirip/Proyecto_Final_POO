package vistas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlador.ClinicaControladora;
import modelos.Usuario;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class LoginScreen extends JFrame {

	private JPanel contentPane;
	private JTextField usuariotxt;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen frame = new LoginScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Acceso al Sistema Clínico");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(224, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel usuariolbl = new JLabel("Usuario:");
		usuariolbl.setBounds(28, 60, 56, 16);
		contentPane.add(usuariolbl);
		
		JLabel passwordlbl = new JLabel("Contrase\u00F1a:");
		passwordlbl.setBounds(28, 89, 77, 16);
		contentPane.add(passwordlbl);
		
		usuariotxt = new JTextField();
		usuariotxt.setBounds(125, 57, 116, 22);
		contentPane.add(usuariotxt);
		usuariotxt.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(125, 86, 116, 22);
		passwordField.setEchoChar('*');
		contentPane.add(passwordField);
		
		JCheckBox checkpass = new JCheckBox("Mostrar contrase\u00F1a");
		checkpass.setBackground(new Color(224, 255, 255));
		checkpass.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Si está marcado, quitamos el carácter de ocultación (ponemos 0)
                    passwordField.setEchoChar((char) 0);
                } else {
                    // Si no está marcado, volvemos a poner el asterisco
                    passwordField.setEchoChar('*');
                }
            }
        });
		checkpass.setBounds(254, 85, 170, 25);
		contentPane.add(checkpass);
		
		JButton ingresarbtn = new JButton("Ingresar");
		ingresarbtn.setBackground(new Color(152, 251, 152));
		ingresarbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				procesarLogin();
			}
		});
		ingresarbtn.setBounds(323, 215, 97, 25);
		contentPane.add(ingresarbtn);
		
		JLabel lblNewLabel = new JLabel("Iniciar Sesi\u00F3n");
		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblNewLabel.setBounds(125, 13, 159, 16);
		contentPane.add(lblNewLabel);
		
		JLabel lblSistemaClinico = new JLabel("Sistema Clinico");
		lblSistemaClinico.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblSistemaClinico.setBounds(12, 219, 159, 16);
		contentPane.add(lblSistemaClinico);
	}
	
	private void procesarLogin() {
        String user = usuariotxt.getText();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese usuario y contraseña", 
                                          "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Llamamos al CONTROLADOR PRINCIPAL (Singleton)
        ClinicaControladora controller = ClinicaControladora.getInstance();
        Usuario usuarioLogueado = controller.hacerLogin(user, pass);

        if (usuarioLogueado != null) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido " + usuarioLogueado.getUsername() + "!");
            
            // AQUÍ ABRIRÍAS LA VENTANA PRINCIPAL DEL SISTEMA
            // Ejemplo:
            
            Principal ventanaPrincipal = new Principal(usuarioLogueado);
            ventanaPrincipal.setVisible(true);
            
            // Cerramos la ventana de login
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", 
                                          "Error de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}
