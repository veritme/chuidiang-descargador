package com.chuidiang.descargador;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Ventana principal y main del descargador. Muestra una caja de texto en la que
 * poner la url de una pagina html. Pulsando <intro> en esa misma caja de texto
 * o bien el boton de descarga, comenzara el analisis de la pagina html y la
 * descarga de los ficheros enlazados desde esa pagina. En la lista central
 * apareceran todas las url de ficheros que se van a descargar.
 * 
 * @author chuidiang
 * 
 */
public class VentanaDescargador extends JFrame {
	/**
	 * Accion para empezar la descarga de una url.
	 * 
	 * @author chuidiang
	 * 
	 */
	private final class ActionDescarga implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// En un hilo se pasa la url de la caja de texto a la
			// clase de descargas.
			new Thread() {
				public void run() {
					des.descarga(tfUrl.getText());
				}
			}.start();
		}
	}

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1522605828120363835L;

	/**
	 * Crea y visualiza la ventana de descargas.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		VentanaDescargador v = new VentanaDescargador();
		v.pack();
		v.setVisible(true);
		v.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	/**
	 * Instancia de la accion para comenzar la descarga de una url.
	 */
	private ActionDescarga accion = new ActionDescarga();
	/**
	 * Boton para la descarga.
	 */
	private JButton b;
	/**
	 * Clase encargada de hacer las descargas.
	 */
	private Descargador des;
	/**
	 * Dialogo para el editor de la configuracion.
	 */
	private JDialog dialogo = null;

	/**
	 * Lista de url de ficheros que estan en la lista de descargas.
	 */
	private JList lista;

	/**
	 * Modelo de la lista que muestra los ficheros en la lista de descargas.
	 */
	private DefaultListModel m;

	/**
	 * Panerl para editar la configuracion.
	 */
	private PanelConfiguracion panel = null;

	/**
	 * Scrollpane de la lista de ficheros en la lista de descargas.
	 */
	JScrollPane sp;

	/**
	 * TextFiel para pedir la url de la pagina que contiene los enlaces a los
	 * ficheros de descargas.
	 */
	JTextField tfUrl = new JTextField(80);

	/**
	 * Construccion de todo.
	 */
	public VentanaDescargador() {
		super("Descargador");

		// Creacion del descargador,
		des = new Descargador();

		// Se le pasa la configuracion leida de fichero.
		des.setConfiguracion(Configuracion.cargaConfiguracion());

		// Observador para anadir y borrar ficheros descargados de la
		// lista de descargas visible al usuario.
		des.addObservador(new IfzObservadorDescarga() {

			@Override
			public void anhadidoAListaDescargas(final String fichero) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						m.addElement(fichero);
					}

				});
			}

			@Override
			public void comienzaDescarga(String fichero) {
				// No implementado.
			}

			@Override
			public void descargaTerminada(final String fichero, boolean exito,
					Exception e) {
				// Se ignora la excpecion. Debería mostrarse de alguna
				// manera.
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						m.removeElement(fichero);
					}

				});
			}

		});

		// Creacion de componentes visuales.
		initComponents();
	}

	private void initComponents() {
		b = new JButton("descargar");
		m = new DefaultListModel();
		lista = new JList(m);
		sp = new JScrollPane(lista);

		getContentPane().add(tfUrl, BorderLayout.NORTH);
		getContentPane().add(sp);
		getContentPane().add(b, BorderLayout.SOUTH);

		b.addActionListener(accion);
		tfUrl.addActionListener(accion);
		tfUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				((JTextField) arg0.getSource()).selectAll();
			}
		});

		JMenuBar menuBar = new JMenuBar();
		JMenu m = new JMenu("Configuración");
		JMenuItem extensiones = new JMenuItem(new AbstractAction(
				"Directorios por extensión") {
			private static final long serialVersionUID = 6831720696358185613L;

			@Override
			public void actionPerformed(ActionEvent e) {
				muestraVentanaConfiguracion();
			}

		});
		m.add(extensiones);
		menuBar.add(m);
		setJMenuBar(menuBar);
	}

	/**
	 * Se lee la configuracion del panel de descargas y se le pasa al
	 * descargador.
	 */
	protected void leeConfiguracion() {
		des.setConfiguracion(panel.getConfiguracion());
	}

	/**
	 * Muestra la ventana para editar la configuracion.
	 */
	private void muestraVentanaConfiguracion() {
		if (dialogo == null) {
			panel = new PanelConfiguracion();
			dialogo = new JDialog(VentanaDescargador.this, "Configuración");
			dialogo.add(panel);
			dialogo.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					leeConfiguracion();
				}

			});
		}
		panel.setConfiguracion(des.getConfiguracion());
		dialogo.pack();
		dialogo.setVisible(true);
	}
}
