package com.chuidiang.descargador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Guarda y carga de fichero la configuracion del descargador.<br>
 * La configuracion consiste en una lista de extensiones y directorios
 * de descarga, de forma que cada extension de fichero tiene asignado
 * un directorio donde descargarlo.<br>
 * El fichero va en $(user.home)/.descargador/configuracion.txt, creando
 * el directorio si es necesario.<br>.
 * El fichero tiene lineas de texto. Cada linea es una extension
 * y un directorio, separados por un unico espacio.<br>
 * Dentro del codigo, la configuracion se representa como un Hashtable,
 * en el que las claves son String con la extension y los valores
 * son String con el path del directorio donde se descargaran los
 * ficheros con esa extension.<br>
 * 
 * @author chuidiang
 */
public class Configuracion {
	
	/**
	 * Carga la configuracion de un fichero por defecto.<br>
	 * 
	 * @return Un Hashtable con la configuracion.
	 */
	public static Hashtable<String, String> cargaConfiguracion () {
		Hashtable<String, String> configuracion = new Hashtable<String, String>();
		BufferedReader br=null;
		try {
			br = new BufferedReader(new FileReader(System.getProperty("user.home")+"/.descargador/configuracion.txt"));
			String linea = br.readLine();
			while (null != linea) {
				// En cada linea, el separador de la extension y el
				// directorio es un espacio.
				int espacio = linea.indexOf(" ");
				String extension = linea.substring(0,espacio);
				String directorio = linea.substring(espacio+1);
				configuracion.put(extension, directorio);
				linea=br.readLine();
			}
		} catch (FileNotFoundException e1) {
			System.out.println("No hay fichero de configuracion");
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			if (null!=br)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return configuracion;
	}

	/**
	 * Guarda la configuracion en el fichero por defecto.
	 * @param configuracion La configuracion a guardar.
	 */
	public static void salvaConfiguracion(Hashtable<String, String> configuracion) {
		File directorio = new File(System.getProperty("user.home")+"/.descargador");
		
		// Crea el directorio si no existe
		if (!directorio.exists())
			directorio.mkdir();
		
		// Escritura del fichero
		BufferedWriter fw=null;
		try {
			fw = new BufferedWriter(new FileWriter(new File(directorio,"configuracion.txt")));
			Enumeration<String> extensiones = configuracion.keys();
			while (extensiones.hasMoreElements()) {
				String extension = extensiones.nextElement();
				String directorioDescarga = configuracion.get(extension);
				fw.write(extension+" "+directorioDescarga);
				fw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fw)
					fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
