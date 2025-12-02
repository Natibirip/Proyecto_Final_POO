package Soket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;





public class server extends Thread{

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(9911);
            System.out.println("Servidor iniciado en puerto 9911...");

            while (true) {
                Socket cliente = serverSocket.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                // Crear un hilo nuevo para cada cliente
                new Thread(new ManejadorCliente(cliente)).start();
            }

        } catch (IOException e) {
            System.out.println("Error al iniciar servidor: " + e);
        }
    }
}

class ManejadorCliente implements Runnable {

    private Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

            DataInputStream entrada = new DataInputStream(socket.getInputStream());

            // Archivo donde se guardará (UNO POR CONEXIÓN)
            File archivo = new File("empresa_respaldo.dat");
            FileOutputStream salida = new FileOutputStream(archivo);

            int unByte;

            while ((unByte = entrada.read()) != -1) {
                salida.write(unByte);
            }

            // Cerrar SOLO recursos del cliente
            salida.close();
            entrada.close();
            socket.close();

            System.out.println("Archivo recibido y cliente desconectado.");

        } catch (IOException e) {
            System.out.println("Cliente desconectado inesperadamente: " + e);
        }
    }
}