package aco_vrp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;


/**
 * Clase principal para encontrar las soluciones.
 * @author Teresa Becerril Torres
 * @version 1.0
 */
public class Solution {
	
	/* IDs de los clientes del ejemplar. */
	private static List<Integer> idClients;
	/* Demandas de los clientes del ejemplar. */
	private static List<Integer> demands;
	/* Lista de clientes del ejemplar. */
	private static List<Client> clients;
	/* Deposito del ejemplar. */
	private static Client depot;
	
	/**
	 * Metodo que obtiene los IDs y las demandas de los clientes del archivo.
	 * @param file el archivo que contiene los IDs y las demandas de los clientes.
	 */
	private static void readOrders (File file) { 
		idClients = new ArrayList<Integer>();
		demands = new ArrayList<Integer>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
			String line = reader.readLine();
			
			while (line != null) {
				String[] order = line.split("-"); 
				
				if (order[0] != null && order[1] != null)  {
					String idCS = order[0].replaceAll("\\s","");
					int idClient = Integer.parseInt(idCS);
					idClients.add(idClient);
					
					String demandS = order[1].replaceAll("\\s","");
					int demand = Integer.parseInt(demandS);
					demands.add(demand);
				}
				
				line = reader.readLine();
			}
			
			if (reader != null) {
				reader.close();
			}
		} catch (IOException e) {
			System.err.println("Error al leer el archivo.");
			System.exit(0);
		}
	}
	
	/**
	 * Metodo que obtiene los clientes de la base de datos.
	 */
	private static void obtainClients () {
		clients = new ArrayList<Client>();
		File file = new File("files/Orders.txt");
		readOrders(file);
		
		try {
			SearchBD search = new SearchBD();
			clients = search.getClients(idClients);
			
			for (int i = 0; i < clients.size(); i++) {
				Client client = clients.get(i);
				int idClient = client.getId();
				int demand = demands.get(i);
				List<Pair<Integer, Integer>> distances = search.getDistances(idClient, idClients);
				client.setDistances(distances);
				client.setDemand(demand);
				if (demand == 0) {
					depot = client;
					Boolean isDepot = true;
					client.setIsDepot(isDepot);
					clients.remove(i);
					clients.add(0, client);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al acceder a la base de datos.");
			System.exit(0);
		}
	}
	
	/**
	 * Metodo main de la clase principal.
	 * @param args los argumentos que se le pasan en la terminal.
	 */
	public static void main(String args[]){
		int strategy = 1;
		int lengthLC = 0;
		
		for (int i = 0; i < args.length; i++) {
              String arguments = args[i];
              switch (arguments) {
              	case "-s":
              		try {
              			strategy = Integer.parseInt(args[i + 1]);
              			i++;
              		} catch (Exception e) {
              			System.err.println("\n No se proporciono un numero de " + 
              		                       "estrategia valido. \n");
                        System.exit(0);
              		}
              		break;
              	case "-c":
              		try {
              			lengthLC = Integer.parseInt(args[i + 1]);
              			i++;
              		} catch (Exception e) {
              			System.err.println("\n No se proporciono una longitud " + 
              		                       "valida para la lista de candidatos. \n");
                        System.exit(0);
              		}
              		break;
              	}
		}
		
		obtainClients();
		VRP v = new VRP(clients, depot);
		v.vrp(strategy, lengthLC);
	}
}
