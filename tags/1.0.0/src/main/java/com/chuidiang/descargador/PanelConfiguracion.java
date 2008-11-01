package com.chuidiang.descargador;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * Panel para editar la configuracion de las descargas.<br>
 * Basicamente es una tabla con dos columnas. En la primera columna van las
 * extensiones de los ficheros que se quieren descargar. En la segunda el path
 * para la descarga de cada fichero segun su extension. Se edita haciendo doble
 * click en las celdas. La ultima fila esta vacia para anadir nuevas
 * extensiones. Si borramos una extension, desaparace la fila. Si se pone un
 * path de descarga que no existe, se da opcion a crearlo automaticamente.
 * 
 * @author chuidiang
 * 
 */
public class PanelConfiguracion extends JPanel {
	/**
	 * serial uid
	 */
	private static final long serialVersionUID = 6104320242014260144L;

	/**
	 * Modelo de datos con dos columnas: la extension y el directorio donde
	 * descargar los ficheros con esa extension.
	 */
	private DefaultTableModel modelo;

	/**
	 * Crea el panel.
	 */
	public PanelConfiguracion() {
		// Creacion de la tabla y su modelo de datos.
		modelo = new DefaultTableModel(new String[] { "Extensiones",
				"Directorio descargas" }, 1);
		JTable tabla = new JTable(modelo);
		JScrollPane sp = new JScrollPane(tabla);
		add(sp);

		// Suscripcion a cambios en los datos, para anadir o borrar
		// nuevas extensiones con sus directorios.
		modelo.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent eventoTableModel) {
				switch (eventoTableModel.getType()) {
				case TableModelEvent.UPDATE:
					updateFila(eventoTableModel);
					break;
				}
			}

		});
	}

	/**
	 * Devuelve un Hashtable en el que las claves son las extensiones de los
	 * ficheros y los valores son los directorios donde descargar los ficheros
	 * con dicha extension.
	 * 
	 * @return El Hashtable.
	 */
	public Hashtable<String, String> getConfiguracion() {
		Hashtable<String, String> configuracion = new Hashtable<String, String>();

		// Se recorren todas las filas, menos la ultima que siempre
		// es una fila vacia.
		for (int i = 0; i < modelo.getRowCount() - 1; i++) {

			// La extension, se le anade punto delante si no lo tiene.
			String extension = (String) modelo.getValueAt(i, 0);
			if (!extension.startsWith("."))
				extension = "." + extension;

			// El directorio. Se cambian los \ por / y se anade una /
			// al final si no la tiene.
			String directorio = (String) modelo.getValueAt(i, 1);
			directorio = directorio.replaceAll("\\\\", "/");
			if (!directorio.endsWith("/"))
				directorio = directorio + "/";

			// Se mete extension/directorio en el Hashtable
			configuracion.put(extension, directorio);
		}

		// Se guarda en fichero.
		Configuracion.salvaConfiguracion(configuracion);

		return configuracion;
	}

	/**
	 * Recibe las parejas extension/directorio y las muestra en la tabla,
	 * borrando previamente los datos que hubiera en la tabla.
	 * 
	 * @param configuracion
	 */
	public void setConfiguracion(Hashtable<String, String> configuracion) {

		// borrado de la tabla.
		while (modelo.getRowCount() > 0)
			modelo.removeRow(0);

		// Se muestra en la tabla las parejas extension/directorio.
		if (null != configuracion) {
			Enumeration<String> extensiones = configuracion.keys();
			while (extensiones.hasMoreElements()) {
				String extension = extensiones.nextElement();
				String directorio = configuracion.get(extension);
				modelo.addRow(new Object[] { extension, directorio });
			}
		}

		// Se anade fila vacia, para poder insertar en ella una nueva
		// pareja extension/directorio.
		modelo.addRow(new Object[] { "", "" });
	}

	/**
	 * Trata los cambios en el contenido de la tabla.
	 * 
	 * @param eventoTableModel
	 */
	protected void updateFila(TableModelEvent eventoTableModel) {

		int filaCambiada = eventoTableModel.getFirstRow();
		int columnaCambiada = eventoTableModel.getColumn();

		// Si cambia la columna de la extension
		if (0 == columnaCambiada) {

			// Si el usuario ha borrado la extension, borramos la fila que
			// contenia dicha extension.
			if ("".equals(modelo.getValueAt(filaCambiada, columnaCambiada))) {
				modelo.removeRow(filaCambiada);
				return;
			}

		}

		// Si cambia la columna del directorio
		if (1 == columnaCambiada) {
			String directorio = (String) modelo.getValueAt(filaCambiada,
					columnaCambiada);

			// Se comprueba si existe el directorio y se pide
			// confirmacion para crearlo si no existe.
			File dir = new File(directorio);
			if (!dir.exists()) {
				int seleccion = JOptionPane.showConfirmDialog(this,
						"<html>El directorio " + directorio
								+ " no existe<br>¿Lo creamos? </html>",
						"No existe directorio", JOptionPane.YES_NO_OPTION);
				if (JOptionPane.YES_OPTION == seleccion) {
					dir.mkdirs();
				}
			}
		}

		// Si la fila que cambia es la ultima, se anade una nueva
		// fila en blanco para poder seguir insertando parejas
		// extension/directorio
		if (filaCambiada == modelo.getRowCount() - 1) {
			modelo.addRow(new Object[] { "", "" });
		}

	}

}
