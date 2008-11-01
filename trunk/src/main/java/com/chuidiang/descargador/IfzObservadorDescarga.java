package com.chuidiang.descargador;

/**
 * Interface que deben implementar los obseravadores de la descarga.
 * 
 * @author chuidiang
 * 
 */
public interface IfzObservadorDescarga {
	/**
	 * Se ha anadido un fichero a la lista de descargas.
	 * 
	 * @param fichero
	 *            url del fichero que se ha anadido.
	 */
	public void anhadidoAListaDescargas(String fichero);

	/**
	 * Comienza la descarga de un fichero.
	 * 
	 * @param fichero
	 *            url del fichero cuya descarga comienza.
	 */
	public void comienzaDescarga(String fichero);

	/**
	 * Ha terminado la descarga del fichero.
	 * 
	 * @param fichero
	 *            url del fichero que se ha descargado.
	 * @param exito
	 *            <code>true</code> si ha habido exito en la descarga
	 * @param e
	 *            <code>Exception</code> en caso de fallo en la descarga
	 */
	public void descargaTerminada(String fichero, boolean exito, Exception e);
}
