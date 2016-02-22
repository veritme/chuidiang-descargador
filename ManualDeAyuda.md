#Manual de usuario del descargador de ficheros.

# Introduccion #

El descargador de ficheros permite darle la direccion de una pagina web y el buscara todos los ficheros a los que dicha pagina enlaca para descargarlos automaticamente. Puede ahorrarnos trabajo si queremos, por ejemplo, bajar todas las fotos a las que enlaza una pagina o todos los documentos.

# Ejecutar la aplicacion #

La aplicacion es un fichero descargador-1.0.0.jar. Para arrancarlo, basta hacer doble click sobre el o bien, desde linea de comandos, ejecutar

|| java -jar descargador-1.0.0.jar

# Configuracion #

Antes de descargar los primeros ficheros debemos decirle al descargador donde queremos que los deje. Para ello, en el menu configuracion seleccionamos la opcion _Directorios de descargas_. Aparecera una tabla con una unica fila vacia.

En la primera columna, dando doble click, debemos introducir una extension de fichero que nos queramos descargar, por ejemplo, _.doc_. En la segunda columna, el directorio en el que queremos que el descargador deje los ficheros de extension _.doc_, por ejemplo, _C:/descargas/documentos/_.

Al cerrar la ventana de configuracion, esta se guardar en _$(user.home)/.descargador/configuracion.txt_ y se leera automaticamente cada vez que arranquemos el descargador. En cualquier momento podemos modificarla abriendo nuevamente la ventana de _Directorios de descargas_

# Descargas #

Ahora solo nos queda empezar a descargar. Copiamos del navegador o escribimos en la caja de texto de la parte superior la direccion de la pagina web cuyos ficheros queremos descargarnos. Pulsamos el retorno de carro o bien el boton _descargar_ en la parte inferior.

En la lista central aparecera una lista de ficheros encontrados y que se van a descargar. Iran desapareciendo poco a poco, segun se vayan realizando las descargas.