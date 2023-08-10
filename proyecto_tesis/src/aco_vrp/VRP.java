package aco_vrp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.javatuples.Pair;

/**
 * Clase que utiliza la metaheurística ACO y las estrategias 
 * de mejora para solucionar el VRP.
 * @author Teresa Becerril Torres
 * @version 1.0
 */
public class VRP {
	
	
	/* Numero de vehiculos. */
	private int m;
	/* Capacidad de carga de los vehiculos. */ 
	private int Q;
	/* Velocidad de evaporacion de las feromonas. */
	private double alpha;
	/* Importancia de la distancia en comparacion con la cantidad de feromonas. */
	private double beta;
	/* Cantidad inicial de feromonas. */
	private double tau0;
	/* Probabilidad de seleccionar una ruta corta. */
	private double q0;
	/* Probabilidad de seleccionar una ruta corta con niveles altos de feromonas*/
	private int q;
	/* Lista de clientes del ejemplar. */
	private List<Client> clients;
	/* Deposito del ejemplar. */
	private Client depot;
	/* Colonia de hormigas. */
	private ACO aco;
	/* Colonias de hormigas. */
	private List<ACO> colonies;
	/* Generador de numeros aleatorios. */
	private Random random;
	/* Lista que nos indica cuantas veces fueron visitados los clientes. */
	private List<Integer> visits;
	
	/**
	 * Constructor que crea un objeto VRP.
	 * @param clients la lista de clientes del ejemplar.
	 * @param depot el deposito del ejemplar.
	 */
	public VRP (List<Client> clients, Client depot) {
		this.clients = clients;
		this.depot = depot;
		readConfigurations();
		q = 0; 
		aco = new ACO(m, Q, alpha, tau0, clients);
		random = new Random();
		setUpVisits();
	}
	
	/**
	 * Metodo que inicializa los parametros con los valores del archivo.
	 */
	public void readConfigurations () { 
		File file = new File("files/Configurations.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
			String line = reader.readLine();
			
			while (line != null) {
				String[] aux = line.split("="); 
				
				if (aux[0] != null && aux[1] != null)  {
					String parameter = aux[0].replaceAll("\\s","");
					String valor = aux[1].replaceAll("\\s", "");
					
					switch(parameter) {
						case "m":
							try {
		              			m = Integer.parseInt(valor);
		              		} catch (Exception e) {
		              			System.err.println("\n No se proporciono un numero de " + 
		              		                       "vehiculos valido. \n");
		                        System.exit(0);
		              		}
							break;
						case "Q":
							try {
		              			Q = Integer.parseInt(valor);
		              		} catch (Exception e) {
		              			System.err.println("\n No se proporciono una capacidad de " + 
		              		                       "carga valida. \n");
		                        System.exit(0);
		              		}
							break;
						case "alpha":
							try {
		              			alpha = Double.parseDouble(valor);
		              		} catch (Exception e) {
		              			System.err.println("\n No se proporciono una velocidad de " + 
		              		                       "evaporacion de feromonas valida. \n");
		                        System.exit(0);
		              		}
							break;
						case "beta":
							try {
		              			beta = Double.parseDouble(valor);
		              		} catch (Exception e) {
		              			System.err.println("\n No se proporciono una importancia de " + 
		              		                       "la distancia en comparacion con la cantidad " + 
		              					           "de feromonas valida. \n");
		                        System.exit(0);
		              		}
							break;
						case "tau0":
							try {
		              			tau0 = Double.parseDouble(valor);
		              		} catch (Exception e) {
		              			System.err.println("\n No se proporciono una cantidad inicial " + 
		              		                       "de feromonas valida. \n");
		                        System.exit(0);
		              		}
							break;
						case "q0":
							try {
		              			q0 = Double.parseDouble(valor);
		              		} catch (Exception e) {
		              			System.err.println("\n No se proporciono una probabilidad de " + 
		              		                       "seleccionar una ruta corta valida. \n");
		                        System.exit(0);
		              		}
							break;
						default:
							System.err.println("\n Se ingreso un parametro que " + 
       		                       "no es valido. \n");
							System.exit(0);
					}
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
	 * Metodo que inicializa la lista que nos indica cuantas veces fueron visitados
	 * los clientes.
	 */
	public void setUpVisits () {
		visits = new ArrayList<Integer>();
		
		for (int i = 0; i < clients.size(); i++) {
			visits.add(0);
		}
	}
	
	private class EvaporateDaemon extends Thread {
		
		/* Colonia de hormigas. */
		private ACO aco;
		/* Cantidad de feromonas que se va a evaporar constantemente */
		private double delta;
		/* Bandera para detener el hilo. */
		private Boolean stopDeamon;
		
		/**
		 * Constructor que crea un EvaporateDaemon.
		 * @param aco la colonia de hormigas.
		 */
		public EvaporateDaemon (ACO aco, double delta) {
			this.aco = aco;
			this.delta = delta;
			stopDeamon = true;
		}
		
		/**
		 * Metodo que corre el hilo.
		 */
		public void run () {
			try{     
				while(stopDeamon) {
					aco.evaporateP(delta);
				}
            } catch (Exception e) {
                System.err.println(e.toString());
            }
		}
		
		public void stopDaemon () {
			stopDeamon = false;
		}
	}
	
	/**
	 * Metodo que busca un cliente en la lista de clientes.
	 * @param idClient el id del cliente que se va a buscar.
	 * @return el cliente que se busca, null en otro caso.
	 */
	public Client searchClient (int idClient) {
		Client client = null;
		
		for (Client c : clients) {
			int id = c.getId();
			if (idClient == id) {
				client = c;
			}
		}
		
		return client;
	}
	
	/**
	 * Metodo que busca el indice del cliente en la lista de clientes.
	 * @param client el cliente del que se quiere saber su indice.
	 * @return el indice del cliente en la lista de clientes.
	 */
	public int searchIndexClient(Client client) {
		int idClient = client.getId();
		int indexC = -1;
		
		for (int i = 0; i < clients.size(); i ++) {
			Client c = clients.get(i);
			int idC = c.getId();
			if (idClient == idC) {
				indexC = i;
			}
		}
		
		return indexC;
	}
	
	/**
	 * Metodo que calcula la distancia de la ruta.
	 * @param rute la ruta a la cual se le va a calcular la distancia.
	 * @return la distancia de la ruta.
	 */
	public int calculateDistance (List<Client> route) { 
		int distanceR = 0;
		
		for (int i = 0; i < route.size(); i++) {
			if (i < route.size() - 1) {
				Client client1 = route.get(i);
				int idClient2 = route.get(i + 1).getId(); 
				distanceR += client1.searchDistance(idClient2);
			}
		}
		
		return distanceR;
	}
	
	/**
	 * Metodo que calcula la demanda total atendida por una ruta.
	 * @param route la ruta a la cual se le va a calcular la demanda total atendida.
	 * @return la demanda total atendida por una ruta.
	 */
	public int calculateDemand (List<Client> route) {
		int demandT = 0;
		
		for (int i = 0; i < route.size(); i++) {
			Client client = route.get(i);
			int demand = client.getDemand();
			demandT += demand;
		}
		
		return demandT;
	}
	
	/**
	 * Metodo que regresa la lista con los IDs de los clientes ordenados de acuerdo a su 
	 * distancia con el cliente.
	 * @param client el cliente del que queremos obtener las distancias ordenas. 
	 * @return la lista con los IDs de los clientes ordenados de acuerdo a su 
	 * distancia con el cliente.
	 */
	public List<Integer> getOrderedDist (Client client) {
		List<Pair<Integer, Integer>> distances = client.getDistances();
		List<Integer> idDistances  = new ArrayList<Integer>();
		
		for (int i = 0; i < distances.size(); i++) {
			Pair<Integer, Integer> distance = distances.get(i);
			int idClient = distance.getValue0();
			idDistances.add(idClient);
		}
		
		return idDistances;
	}
	
	/**
	 * Metodo que selecciona al siguiente cliente a visitar utilizando la lista de 
	 * candidatos del cliente actual de la hormiga.
	 * @param ant la hormiga que va a seleccionar al siguiente cliente.
	 * @return el siguiente cliente a visitar. 
	 */
	public Client selectNextClient (Ant ant) {
		List<Client> memory = ant.getMemory();
		int capacity = ant.getCapacity();
		Client currentClient = ant.getCurrentClient();
		List<Integer> candidates = currentClient.getCandidates();
		Client nextClient = null;
		
		for (int i = 0; i < candidates.size(); i++) {
			int idClient = candidates.get(i);
			Client client = searchClient(idClient);
			int index = searchIndexClient(client);
			int demand = client.getDemand();
			int numVisits = visits.get(index);
			
			if (numVisits == 0) {
				if (!memory.contains(client)) {
					if (demand <= capacity) {
						nextClient = client;
						break;
					}
				}
			}
		}
		
		
		
		return nextClient;
	}
	
	/**
	 * Metodo que selecciona al siguiente cliente a visitar utilizando la lista de 
	 * candidatos del deposito de la hormiga.
	 * @param ant la hormiga que va a seleccionar al siguiente cliente.
	 * @return el siguiente cliente a visitar. 
	 */
	public Client selectNextCD (Ant ant) {
		int numVisits = visits.get(0);
		List<Integer> candidates = depot.getCandidates();	
		Client nextClient = null;
		
		if (numVisits == 0) {
			int indexLC = random.nextInt(candidates.size());
			int idClient = candidates.get(indexLC);
			nextClient = searchClient(idClient);
		} else {
			for (int i = 0; i < candidates.size(); i++) {
				int idClient = candidates.get(i);
				Client client = searchClient(idClient);
				int index = searchIndexClient(client);
				int numVC = visits.get(index);
				
				if (numVC == 0) {
					nextClient = client;
					break;
				}
			}
		}
		
		if (nextClient == null) {
			List<Integer> idDistances = getOrderedDist(depot);
			
			for (int i = 0; i < idDistances.size(); i++) {
				int idClient = idDistances.get(i);
				Client client = searchClient(idClient);
				int index = searchIndexClient(client);
				int numVC = visits.get(index);
				
				if (numVC == 0) {
					nextClient = client;
					break;
				}
			}
		}
		
		return nextClient;
	}
	
	/**
	 * Metodo que obtiene la distancia mínima recorrida por alguna ruta encontrada.
	 * @param routes las rutas encontradas.
	 * @param maxDemand la maxima demanda atendida por alguna ruta encontrada.
	 * @return la distancia mínima recorrida por alguna ruta encontrada.
	 */ 
	public int obtainBestDistance (List<List<Client>> routes, int maxDemand) {
		int bestDistance = 0;
		
		for (int i = 0; i < routes.size(); i++) {
			List<Client> route = routes.get(i);
			int demand = calculateDemand(route);
			int distance = calculateDistance(route);
			if (demand == maxDemand) {
				if (bestDistance == 0) {
					bestDistance = distance;
				} else if (distance < bestDistance) {
					bestDistance = distance;
				}
			}
		}
		
		return bestDistance;
	}
	
	
	/**
	 * Metodo que obtiene la mejor ruta encontrada por las hormigas de la colonia.
	 * @param ants la lista de hormigas de la colonia. 
	 * @return la mejor ruta encontrada por las hormigas de la colonia.
	 */
	public List<Client> obtainBestRoute (List<Ant> ants) {
		List<List<Client>> routes = new ArrayList<List<Client>>();
		List<Client> bestRoute = null; 
		int maxDemand = 0;
		
		for (int i = 0; i < ants.size(); i++) {
			Ant ant = ants.get(i);
			List<Client> memory = ant.getMemory();
			int demand = calculateDemand(memory);
			
			if (!routes.contains(memory)) {
				routes.add(memory);
				
				if (demand > maxDemand) {
					maxDemand = demand;
				}
			}
		}
		
		int bestDistance = obtainBestDistance(routes, maxDemand);
		
		for (int i = 0; i < routes.size(); i++) {
			List<Client> route = routes.get(i);
			int demand = calculateDemand(route);
			int distance = calculateDistance(route);
			
			if (demand == maxDemand &&distance == bestDistance) {
					bestRoute = route;
			}
		}
		
		return bestRoute;
	}
	
	/**
	 * Metodo que actualiza el numero de veces que el cliente ha sido visitado.
	 * @param route la ruta que ha sido visitada.
	 */
	public void updateVisits (List<Client> route) {
		for (int i = 0; i < route.size(); i++) {
			Client client = route.get(i);
			int index = searchIndexClient(client);
			int numVisits = visits.get(index);
			visits.remove(index);
			visits.add(index, numVisits + 1);
		}
	}
	
	/**
	 * Metodo que busca la mejor ruta utilizando las listas de candidatos de los clientes.
	 * @return la mejor ruta encontrada.
	 */
	public List<Client> selectClientCL (ACO colony) {
		List<Ant> ants = colony.getAnts();
		Boolean finishC = false; 
		
		while (!finishC) {
			for (int i = 0; i < ants.size(); i++) {
				Ant ant = ants.get(i);
				Client currentClient = ant.getCurrentClient();
				Boolean finishA = ant.getFinish();
				
				if (!finishA) {
					Client client = null;
						if (currentClient.getId() == depot.getId()) {
							client = selectNextCD(ant);
						} else {
							client = selectNextClient(ant);
							
						}
						
						
						if (client != null) {
							ant.addCurrentClient(client);
							int capacity = ant.getCapacity();
								
							if (capacity == 0) {
								ant.addCurrentClient(depot);
								ant.setFinish(true);
							}
						} else {
							ant.addCurrentClient(depot);
							ant.setFinish(true);
						}
				}
			}
			
			Boolean aux = true;
			
			for (int i = 0; i < ants.size(); i++) {
				Ant ant = ants.get(i);
				Boolean finishA = ant.getFinish();
				aux = aux && finishA;
			}
			
			finishC = aux;
		}
		
		List<Client> route = obtainBestRoute(ants);
		updateVisits(route);
		return route;
	}
	
	/**
	 * Metodo que resetea a las hormigas de una colonia.
	 */
	public void resetAnts (ACO colony) {
		List<Ant> ants = colony.getAnts();
		
		for (int i = 0; i < ants.size(); i++) {
			Ant ant = ants.get(i);
			ant.setCapacity(Q);
			List<Client> newMemory = new ArrayList<Client>();
			newMemory.add(depot);
			ant.setMemory(newMemory);
			ant.setFinish(false);
		}
		
		colony.setAnts(ants);
	}
	
	/**
	 * Metodo que busca las mejores rutas para satisfacer la demanda total de 
	 * los clientes utilizando listas de candidatos.
	 * @param lengthLC la longitud de las listas de candidatos.
	 */
	public void acovrplc (ACO colony, int lengthLC) {
		Candidates candidates = new Candidates(lengthLC);
		candidates.obtainCandidates(clients);
		List<List<Client>> bestRoutes = aco.getBestRoutes();
		Boolean finishC = false; 
		while(!finishC) {
			List<Client> route = selectClientCL(aco);
			bestRoutes.add(route);
			resetAnts(colony);
			Boolean aux = true;
			
			for (int i = 0; i < visits.size(); i++) {
				int numVists = visits.get(i);
				Boolean isVisited = false;
				if (numVists != 0) {
					isVisited = true;
				}
				aux = aux && isVisited;
			}
			
			finishC = aux;
		}
		
		aco.setBestRoutes(bestRoutes);
	}
	
	/**
	 * Método que selecciona al siguiente cliente j a visitar favoreciendo
	 * rutas cortas.
	 * @param ant la hormiga que va a seleccionar al siguiente cliente.
	 * @return el siguiente cliente j a visitar.
	 */
	public Client selectClientSR (ACO colony, Ant ant) {
		double[][] pheromones = colony.getPheromones();
		List<Client> memory = ant.getMemory();
		int capacity = ant.getCapacity();
		Client clientI = ant.getCurrentClient();
		int i = searchIndexClient(clientI);
		Client clientJ = null;
		double argMax = 0;
		
		for (int u = 0; u < clients.size(); u++) {
			Client clientU = clients.get(u);
			int idClientU = clientU.getId();
			int demandU = clientU.getDemand();
			int indexU = searchIndexClient(clientU);
			int numVisits = visits.get(indexU);
			
			if (clientU.getId() != depot.getId()) {
				if (numVisits == 0) {
					if (!memory.contains(clientU)) {
						double tauIU = pheromones[i][u];
						int distanceIU = clientI.searchDistance(idClientU);
						double etaIU = Math.pow(distanceIU, -1);
						double aux = tauIU * Math.pow(etaIU, beta);
						if (aux > argMax) {
							if (demandU <= capacity) {
								argMax = aux;
								clientJ = clientU;
							}
						}
					}
				}
			}
		}
		
		return clientJ;
	}
	
	/**
	 * Método auxiliar que calcula la suma del producto de la acumulación de feromonas por
	 * la inversa de la distancia de todos los clientes que no pertenecen a la memoria 
	 * de la hormiga.
	 * @param ant la hormiga que va a seleccionar al siguiente cliente.
	 * @return la suma del producto de la acumulación de feromonas por la inversa de la distancia
	 * de todos los clientes que no pertenecen a la memoria de la hormiga.
	 */
	public double calculateSum (Ant ant, ACO colony) {
		double[][] pheromones = colony.getPheromones();
		List<Client> memory = ant.getMemory();
		Client clientI = ant.getCurrentClient();
		int indexI = searchIndexClient(clientI);
		double sum = 0;
		
		for (int u = 0; u < clients.size(); u ++) {
			Client clientU = clients.get(u);
			int idClientU = clientU.getId();
			int indexU = searchIndexClient(clientU);
			
			if (clientU.getId() != depot.getId()) {
				if (!memory.contains(clientU)) {
					double tauIU = pheromones[indexI][indexU];
					int distanceIU = clientI.searchDistance(idClientU);
					double etaIU = Math.pow(distanceIU, -1);
					double aux = tauIU * Math.pow(etaIU, beta);
					sum += aux;
				}
			}
		}
		
		return sum;
	}
	
	/**
	 * Método que calcula la probabilidad establecida por la inversa de la distancia
	 * y la acumulación de feromonas.
	 * @param ant la hormiga que va a seleccionar el siguiente cliente.
	 * @param clientJ el cliente J, el proximo cliente a visitar.
	 * @return la probabilidad establecida por la inversa de la distancia
	 * y la acumulación de feromonas.
	 */
	public double calulatePIJ (ACO colony, Ant ant, Client clientJ) {
		double[][] pheromones = colony.getPheromones();
		List<Client> memory = ant.getMemory();
		Client clientI = ant.getCurrentClient();
		int indexI = searchIndexClient(clientI);
		int indexJ = searchIndexClient(clientJ);
		int numVisits = visits.get(indexJ);
		int idClientJ = clientJ.getId();
		double sum = calculateSum(ant, colony);
		double pIJ = 0;
		
		if (numVisits == 0) {
			if (!memory.contains(clientJ)) {
				double tauIJ = pheromones[indexI][indexJ];
				int distanceIJ = clientI.searchDistance(idClientJ);
				double etaIJ = Math.pow(distanceIJ, -1);
				double aux = tauIJ * Math.pow(etaIJ, beta);
				pIJ = aux / sum;
			}
		}
		
		return pIJ;
	}
	
	/**
	 * Metodo que selecciona al siguiente cliente j a visitar favoreciendo 
	 * rutas cortas con altos niveles de feromonas.
	 * @param ant la hormiga que va a seleccionar al siguiente cliente.
	 * @return el siguiente cliente a visitar.
	 */
	public Client selectClientHLP (ACO colony, Ant ant) {
		int capacity = ant.getCapacity();
		Client clientJ = null;
		
		for (int j = 0; j < clients.size(); j ++) {
			Client client = clients.get(j);
			int demand = client.getDemand();
			if (client.getId() != depot.getId()) {
				double pIJ = calulatePIJ (colony, ant, client);
			
				if (pIJ != 0) {
					if (demand <= capacity) {
						clientJ = client;
						break;
					}
				}
			
			}
		}
		
		return clientJ;
	}
	
	/**
	 * Metodo que selecciona al siguiente cliente a visitar.
	 * @param ant la hormiga que va a seleccionar al siguiente cliente.
	 * @return el siguiente cliente a visitar.
	 */
	public Client selectClientJ (ACO colony, Ant ant) {
		Client clientJ = null;
		q = random.nextInt(2);
		
		if (q <= q0) {
			clientJ = selectClientSR(colony, ant);
		} else {
			clientJ = selectClientHLP(colony, ant);
		}
		
		return clientJ;
	}
	
	/**
	 * Metodo que busca la mejor ruta encontrada por las hormigas.
	 * @return la mejor ruta encontrada.
	 */
	public List<Client> selectClientVRP (ACO colony) {
		List<Ant> ants = colony.getAnts();
		Boolean finishC = false;
		
		while (!finishC) {
			for (int i = 0; i < ants.size(); i++) {
				Ant ant = ants.get(i);
				Boolean finishA = ant.getFinish();
				
				if(!finishA) {
					Client client = selectClientJ(colony, ant);
					
					if (client != null) {
						ant.addCurrentClient(client);
						int capacity = ant.getCapacity();
						
						if (capacity == 0) {
							ant.addCurrentClient(depot);
							ant.setFinish(true);
						}
					} else {
						ant.addCurrentClient(depot);
						ant.setFinish(true);
					}
				}
			}
			
			Boolean aux = true;
			
			for (int i = 0; i < ants.size(); i++) {
				Ant ant = ants.get(i);
				Boolean finishA = ant.getFinish();
				aux = aux && finishA;
			}
			
			finishC = aux;
		}
		
		for (Ant ant : ants) {
			List<Client> route = ant.getMemory();
			colony.setVisitClient(route);
			colony.updateLP();
		}
		
		List<Client> bestRoute = obtainBestRoute(ants);
		updateVisits(bestRoute);
		return bestRoute;
	}
	
	/**
	 * Metodo que busca las mejores rutas para satisfacer la demanda total de 
	 * los clientes.
	 */
	public void acovrp (ACO colony) {
		EvaporateDaemon evaporate = new EvaporateDaemon(colony, 0.2);
		List<List<Client>> bestRoutes = colony.getBestRoutes();
		Boolean finishC = false;
		evaporate.setDaemon(true);
		evaporate.start();
		
		while (!finishC) {
			List<Client> bestRoute = selectClientVRP(colony);
			int distance = calculateDistance(bestRoute);
			bestRoutes.add(bestRoute);
			colony.updateGP(bestRoute, distance);
			resetAnts(colony);
			
			Boolean aux = true;
			
			for (int i = 0; i < visits.size(); i++) {
				int numVisits = visits.get(i);
				Boolean isVisited = false;
				
				if (numVisits != 0) {
					isVisited = true;
				}
				
				aux = aux && isVisited;
			}
			
			finishC = aux;
		}
		
		evaporate.stopDaemon();
		colony.setBestRoutes(bestRoutes);
	}
	
	/**
	 * Metodo que mejora las rutas encontradas utilizando la heuristica 2-opt.
	 * @param routes las rutas que se van a mejorar.
	 * @param twoOpt la instancia de la clase TwoOpt.
	 * @return las rutas mejoradas.
	 */
	public List<List<Client>> applyTwoOpt (ACO colony, TwoOpt twoOpt) {
		List<List<Client>> routes = colony.getBestRoutes();
		for (int i = 0; i < routes.size(); i++) {
			List<Client> route = routes.get(i);
			int distance = calculateDistance(route);
			List<Client> bestRoute = twoOpt.improveRoute(route, distance);
			int bestDistance = calculateDistance(bestRoute);
			if (bestDistance != 0) {
				routes.remove(i);
				routes.add(i, bestRoute);
			}
		}
		
		return routes;
	}
	
	/**
	 * Metodo que busca las mejores rutas para satisfacer la demanda total de 
	 * los clientes utilizando la heuristica 2-opt.
	 */
	public void acovrpTwoOpt (ACO colony) {
		acovrp(colony);
		TwoOpt twoOpt = new TwoOpt(this);
		List<List<Client>> bestRoutes = applyTwoOpt(colony, twoOpt);
		colony.setBestRoutes(bestRoutes);
	}
	
	/**
	 * Metodo que construye las colonias de hormigas que se van a utilizar.
	 */
	public void createColonies() {
		colonies = new ArrayList<ACO>();
		for (int i = 0; i < m; i++) {
			ACO colonyI = new ACO(m, Q, alpha, tau0, clients);
			colonies.add(colonyI);
		}
	}
	
	/**
	 * Metodo que calcula la distancia total que recorre la colonia de hormiga.
	 * @param colony la colonia de hormiga que recorre las rutas.
	 * @return la distancia total que recorre la colonia de hormiga. 
	 */
	public int calculateTotalDistance (ACO colony) {
		int totalDistance = 0;
		List<List<Client>> bestRoutes = colony.getBestRoutes();
		
		for (int i = 0; i < bestRoutes.size(); i++) {
			List<Client> br = bestRoutes.get(i);
			int distance = calculateDistance(br);
			totalDistance += distance;
		}
		
		return totalDistance;
	}
	
	/**
	 * Metodo que obtine las mejores rutas.
	 * @return la colonia que encontro las mejores rutas.
	 */
	public ACO obtainBestSolution() {
		int bestDistance = 0;
		ACO bestColony = null;
		
		for (int i = 0; i < colonies.size(); i++) {
			ACO colony = colonies.get(i);
			int totalDistance = calculateTotalDistance(colony);
			
			if (i == 0) {
				bestDistance = totalDistance;
				bestColony = colony;
			} else if (totalDistance < bestDistance) {
				bestDistance = totalDistance;
				bestColony = colony;
			}
		}
		
		return bestColony;
	}
	
	/**
	 * Metodo que busca las mejores rutas para satisfacer la demanda total de 
	 * los clientes utilizando multiples colonias de hormigas.
	 */
	public void macovrp () {
		createColonies();
		
		for (int i = 0; i < colonies.size(); i++) {
			ACO colony = colonies.get(i);
			acovrp(colony);
			visits = new ArrayList<Integer>();
			setUpVisits();
		}
	}
	
	/**
	 * Metodo que busca las mejores rutas para satisfacer la demanda total de 
	 * los clientes utilizando multiples colonias de hormigas y la heuristica 2-opt.
	 */
	public void macovrpTwoOpt () {
		macovrp();
		TwoOpt twoOpt = new TwoOpt(this);
		
		for (int i = 0; i < colonies.size(); i++) {
			ACO colony = colonies.get(i);
			List<List<Client>> bestRoutes = applyTwoOpt(colony, twoOpt);
			colony.setBestRoutes(bestRoutes);
		}
	}
	
	public void vrp (int strategy, int lengthLC) {
		switch(strategy) {
			case 1:
				System.out.println("Colonia de hormigas unica \n"); 
				acovrp(aco);
				convertRoutes(aco);
				break;
			case 2:
				System.out.println("Colonia de hormigas unica y Heuristica 2-Opt \n"); 
				acovrpTwoOpt(aco);
				convertRoutes(aco);
				break;
			case 3:
				if (lengthLC != 0) { 
					System.out.println("Colonia de hormigas unica y Listas de Candidatos \n"); 
					acovrplc(aco, lengthLC);
					convertRoutes(aco);
				} else {
          			System.err.println("\n No se proporciono una longitud " + 
          		                       "valida para la lista de candidatos. \n");
                    System.exit(0);
          		}
				break;
			case 4:
				System.out.println("Multiples Colonias de Hormigas \n");
				macovrp(); 
				ACO bestColony1 = obtainBestSolution();
				convertRoutes(bestColony1);
				break;
			case 5:
				System.out.println("Multiples Colonias de Hormigas y Heuristica 2-Opt \n");
				macovrpTwoOpt();
				ACO bestColony2 = obtainBestSolution();
				convertRoutes(bestColony2);
				break;
			case 6:
				if (lengthLC != 0) { 
					System.out.println("ACO-VRP y Listas de Candidatos \n"); 
					acovrplc(aco, lengthLC);
					convertRoutes(aco);
				} else {
          			System.err.println("\n No se proporciono una longitud " + 
          		                       "valida para la lista de candidatos. \n");
                    System.exit(0);
          		}
				break;
		}
	}
	
	/**
	 * Metodo que convierte las rutas encontradas en String.
	 * @return las rutas encontradas en String.
	 */
	public void convertRoutes (ACO colony) {
		List<List<Client>> bestRoutes = colony.getBestRoutes();
		int totalDistance = 0;
		int totalDemand = 0;
		String r = "Mejores rutas encontradas: \n\n";
		
		for (int i = 0; i < bestRoutes.size(); i++) {
			List<Client> route = bestRoutes.get(i);
			int distance = calculateDistance(route);
			int demand = calculateDemand(route);
			totalDistance += distance;
			totalDemand += demand;
			r += "Vehículo " + (i + 1) + ": \n";
			r += "Ruta: ";
			
			for (int j = 0; j < route.size(); j++) {
				Client c = route.get(j);
				if (j == route.size() - 1) {
					r +=  c.getId() + "] \n";
				} else {
					if (j == 0) {
						r += "[" + c.getId() + ", ";
					} else {
						r +=  c.getId() + ", ";
					}
				}
			}
			
			r += "Demanda atendida: " + demand + "\n";
			r += "Distancia recorrida: " + distance + "\n\n";
		}
		
		r += "Distancia total recorrida: " + totalDistance + "\n";
		r += "Demanda total atendida: " + totalDemand + "\n";
		
		System.out.println(r);
	}
}
