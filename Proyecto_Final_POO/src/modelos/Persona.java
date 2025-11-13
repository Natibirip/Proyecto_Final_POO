package modelos;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

/**
 * Clase abstracta que representa a una persona en el sistema.
 * Sirve como clase base para Paciente y Medico.
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public abstract class Persona implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private String cedula;
    private String nombre;
    private String telefono;
    private Date fechaNacimiento;
    private char sexo; // 'M' o 'F'
    private String rutaFoto;
    
    /**
     * Constructor completo de Persona
     * 
     * @param cedula Cédula de identidad (único)
     * @param nombre Nombre completo de la persona
     * @param telefono Número de teléfono de contacto
     * @param fechaNacimiento Fecha de nacimiento
     * @param sexo Sexo ('M' para masculino, 'F' para femenino)
     */
    public Persona(String cedula, String nombre, String telefono, 
                   Date fechaNacimiento, char sexo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = Character.toUpperCase(sexo);
        this.rutaFoto = null; // Se asigna después si se sube una foto
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public char getSexo() {
        return sexo;
    }
    
    public void setSexo(char sexo) {
        this.sexo = Character.toUpperCase(sexo);
    }
    
    public String getRutaFoto() {
        return rutaFoto;
    }
    
    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }
    
    // ========== MÉTODOS ESPECÍFICOS ==========
    
    /**
     * Calcula la edad de la persona basándose en su fecha de nacimiento
     * 
     * @return Edad en años
     */
    public int getEdad() {
        if (fechaNacimiento == null) {
            return 0;
        }
        
        Calendar nacimiento = Calendar.getInstance();
        nacimiento.setTime(fechaNacimiento);
        
        Calendar hoy = Calendar.getInstance();
        
        int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
        
        // Ajustar si aún no ha cumplido años este año
        if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }
        
        return edad;
    }
    
    /**
     * Verifica si la persona tiene una foto asignada
     * 
     * @return true si tiene foto, false en caso contrario
     */
    public boolean tieneFoto() {
        return rutaFoto != null && !rutaFoto.isEmpty();
    }
    
}