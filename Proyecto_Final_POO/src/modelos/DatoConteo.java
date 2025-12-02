package modelos;

public class DatoConteo {
    private String etiqueta;
    private int valor;

    public DatoConteo(String etiqueta, int valor) {
        this.etiqueta = etiqueta;
        this.valor = valor;
    }


    public void incrementar() { this.valor++; }


	public String getEtiqueta() {
		return etiqueta;
	}


	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}


	public int getValor() {
		return valor;
	}


	public void setValor(int valor) {
		this.valor = valor;
	}
}