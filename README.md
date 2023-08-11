# Colonia de hormigas para el Problema Clásico de Rutas de Vehículos

**Universidad Nacional Autónoma de México, Facultad de Ciencias**

Proyecto de Tesis para obtener el Título de Licenciado en Ciencias de la Computación

Presenta Teresa Becerril Torres

Bajo dirección de la Dra. María de Luz Gasca Soto

## Detalles de implementación

* **Lenguaje de programación:** Java 17.0.5
* **IDE:** Eclipse 2022-09 (4.25.0)
* **Gestor de base de datos:** MySQL 8.0.31
* **Gestor de proyectos:** Apache Maven 4.0.0

Bibliotecas que se utilizaron:

* mysql-connector-j-8.0.31
* javatuples-1.2

## Detalles de ejecución

El programa se encuentra dentro de la carpeta **proyecto_tesis**, el cual se debe de importar a Eclipse para que pueda ser compilado y ejecutado.

* En la carpeta **files** se hallan los siguientes archivos:
  - `Configurations.txt` - Los valores de configuración con los que trabaja el programa.
  - `E15.txt` - Los IDs con las demandas de los clientes y del depósito del ejemplar de 15 ciudades.
  - `E20.txt` - Los IDs con las demandas de los clientes y del depósito del ejemplar de 20 ciudades.
  - `E30.txt` - Los IDs con las demandas de los clientes y del depósito del ejemplar de 30 ciudades.
  - `Orders.txt` - El ejemplar con el que trabaja el programa, este archivo se debe editar si se desea cambiar el ejemplar con el que el programa va a trabajar. 

* En la carpeta **sql** se encuentra la base de datos `cities.sql`, la cual contiene la información de las ciudades y la distancia entre cada par de ciudades.

Para poder ejecutar el programa se le debe pasar alguno de los siguientes argumentos:

* `-s 1` - Colonia de hormigas única.
* `-s 2` - Colonia de hormigas única y heurística 2-opt.
* `-s 3 -c n` - Colonia de hormigas única y listas de candidatos, donde n es la longitud de las listas de candidatos.
* `-s 4` - Múltiples colonias de hormigas.
* `-s 5` - Múltiples colonias de hormigas y heurística 2-opt.
* `-s 6 -c n` - Múltiples colonias de hormigas y listas de candidatos, donde n es la longitud de las listas de candidatos.

Los pasos a seguir para ejecutar el programa son:

1. Abrir Eclipse IDE y elegir el directorio de trabajo.
2. Importar proyecto_tesis, en caso de que ya se haya importado el proyecto saltar este paso.
   - Seleccionar Import... del menú File.
   - Elegir Existing Projects intoWorkspace y presionar Next.
   - Buscar el directorio **Programa_Tesis** y seleccionar el proyecto **proyecto_tesis**. Presionar Finish para importar el proyecto.
3. Seleccionar **proyecto_tesis**, abrir el archivo `files/Orders.txt` e ingresar el ejemplar con el que se desea trabajar.
4. Elegir Run Configurations... del menú Run Solution.
5. Seleccionar la pestaña Arguments y escribir los argumentos con los que se quiere ejecutar el progama en la sección Program arguments.
6. Presionar Run y esperar a que se imprima la mejor solución encontrada para el ejemplar.
