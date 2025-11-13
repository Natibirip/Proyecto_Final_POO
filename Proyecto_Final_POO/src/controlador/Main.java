package controlador;
import modelos.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws Exception {
        SistemaUsuarios sistema = new SistemaUsuarios();

        System.out.println("Usuarios iniciales:");
        for (Usuario u : sistema.getUsuarios()) {
            System.out.println(u);
        }

        // Simular login inicial sin persona asociada
        Usuario login = sistema.login("admin", "1234");
        if (login != null) {
            System.out.println("Login exitoso: " + login.getUsername());
            if (!login.tienePersonaAsociada()) {
                System.out.println("⚠ Usuario sin persona asociada. Se puede crear la persona ahora.");
                
                // Crear una persona y asociarla
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fecha = sdf.parse("01/01/1980");
                Administrador adminPersona = new Administrador("001-0000000-0", "Juan Pérez", "809-555-1111", fecha, 'M');
                login.setPersonaAsociada(adminPersona);

                System.out.println("Persona asociada creada: " + login.getPersonaAsociada().getNombre());
            }
        } else {
            System.out.println("Login fallido");
        }
    }
}
