package modelos;

import java.io.Serializable;

/**
 * define los roles disponibles en el sistema.
 */
public enum RolUsuario implements Serializable {
    ADMINISTRADOR("Administrador con todos los permisos"),
    SECRETARIA("Secretaria con permisos limitados"),
	MEDICO("medico con permisos limitados");
    private String descripcion;

    RolUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
