package Soket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

import controlador.ClinicaControladora;

public class ClienteRespalda {

    public static void enviarRespaldo() {
        try {
            File f = new File("clinica_datos.dat");

            // Si no existe, lo crea vacío
            if (!f.exists()) {
                FileOutputStream fos = new FileOutputStream(f);
                fos.close();
                System.out.println("Archivo empresa.dat creado.");
            }

            // Ahora sí lo lee
            FileInputStream fis = new FileInputStream(f);

            Socket socket = new Socket("localhost", 9911);
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            
            ClinicaControladora.getInstance().guardarDatos();
            int b;
            while ((b = fis.read()) != -1) {
                salida.write(b);
            }

            fis.close();
            salida.close();
            socket.close();

            System.out.println("Respaldo enviado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}