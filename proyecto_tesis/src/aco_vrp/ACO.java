package aco_vrp;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una colonia de hormigas.
 * @author Teresa Becerril Torres
 * @version 1.0
 */
public class ACO {

	/* Velocidad de evaporacion de las feromonas. */
	private double alpha;
	/* Cantidad inicial de feromonas. */
	private double tau0;
	/* Hormigas de la colonia. */
	private List<Ant> ants;
	/* Mejores rutas encontradas por la colonia. */
	private List<List<Client>> bestRoutes; // Una sola ruta para cuando sea multiple la estrategia
	/* Matriz de feromonas. */
	private double[][] pheromones;
	/* Matriz de clientes visitados. */
	private Boolean[][] visitClient;
	/* Lista de Clientes. */
	private List<Client> clients;
	
	/**
	 * Constructor que crea un objeto ACO.
	 * @param m el numero de hormigas en la colonia.
	 * @param Q la capacidad de carga de las hormigas.
	 * @param alpha la Velocidad de evaporación de las feromonas.
	 * @param tau0 la cantidad inicial de feromonas.
	 * @param clients la lista de clientes.
	 */
	public ACO (int m, int Q, double alpha, double tau0, List<Client> clients) {
		this.alpha = alpha;
		this.tau0 = tau0;
		this.clients = clients;
		bestRoutes = new ArrayList<List<Client>>();
		createAnts(m, Q, clients.get(0));
		addPheromones(clients);
	}
	
	/**
	 * Metodo que crea las hormigas de la colonia.
	 * @param m el numero de hormigas que se van a crear.
	 * @param Q la capacidad de carga de las hormigas.
	 * @param depot el deposito en el que se van a encontrar las hormigas.
	 */
	public void createAnts (int m, int Q, Client depot) {
		ants = new ArrayList<Ant>();
		
		for(int i = 0; i < m; i++) {
			Ant ant = new Ant(i, Q, depot);
			ants.add(ant);
		}
	}
	
	/**
	 * Metodo que agrega la cantidad inicial de feromonas a la matriz.
	 * @param clients la lista de clientes.
	 */
	public void addPheromones (List<Client> clients) {
		int n = clients.size();
		pheromones = new double[n][n];
		visitClient = new Boolean[n][n];
		for(int i = 0; i < pheromones.length; i++){
			for(int j = 0; j < pheromones.length; j++){
				if(i != j){
					pheromones[i][j] = tau0;
		        }
		    }
		}
		
		for(int i = 0; i < visitClient.length; i++){
			for(int j = 0; j < visitClient.length; j++){
				if(i != j){
					visitClient[i][j] = false;
		        }
		    }
		}
	}
	
	/**
	 * Metodo que regresa la lista de hormigas de la colonia. 
	 * @return la lista de hormigas de la colonia. 
	 */
	public List<Ant> getAnts () {
	    return ants;
	}
	
	/**
	 * Metodo que regresa la lista de las mejores rutas que ha generado la colonia.
	 * @return la lista de las mejores rutas que ha generado la colonia.
	 */
	public List<List<Client>> getBestRoutes () {
		return bestRoutes;
	}
	
	/**
	 * Metodo que regresa la matriz de feromonas de la colonia.
	 * @return la matriz de feromonas de la colonia.
	 */
	public double[][] getPheromones () {
		return pheromones;
	}
	
	/**
	 * Metodo que regresa la matriz que indica si han sido visitados los clientes.
	 * @return  la matriz que indica si han sido visitados los clientes.
	 */
	public Boolean[][] getVisitClient () {
		return visitClient;
	}
	
	/**
	 * Metodo que actualiza la lista de hormigas de la colonia. 
	 * @param ants la nueva lista de hormigas de la colonia. 
	 */
	public void setAnts (List<Ant> ants) {
	    this.ants = ants;
	}
	
	/**
	 * Metodo que actualiza la lista de las mejores rutas que ha generado la colonia.
	 * @param bestRoutes la nueva lista de las mejores rutas que ha generado la colonia.
	 */
	public void setBestRoutes (List<List<Client>> bestRoutes) {
		this.bestRoutes = bestRoutes;
	}
	
	/**
	 * Metodo que actualiza la matriz de feromonas de la colonia.
	 * @param pheromones la nueva matriz de feromonas de la colonia.
	 */
	public void getPheromones (double[][] pheromones) {
		this.pheromones = pheromones;
	}
	
	/**
	 * Metodo que actualiza la matriz que indica si han sido visitados los clientes.
	 * @param visitClient la nueva matriz que indica si han sido visitados los clientes.
	 */
	public void setVisitClient (List<Client> route) {
		for (int i = 0; i < route.size(); i++){
			if (i < route.size() - 1) {
				Client c1 = route.get(i);
				Client c2 = route.get(i + 1);
				int indexC1 = getIndexClient(c1);
				int indexC2 = getIndexClient(c2);
				Boolean isVisited = visitClient[indexC1][indexC2];
				if (!isVisited) {
					visitClient[indexC1][indexC2] = true;
					visitClient[indexC2][indexC1] = true;
				}
			}
		}
	}
	
	/**
	 * Metodo que busca el indice del cliente en la lista de clientes.
	 * @param client el cliente del que se quiere saber su indice.
	 * @return el indice del cliente en la lista de clientes.
	 */
	public int getIndexClient(Client client) {
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
	 * Metodo que realiza la actualizacion local de las feromonas.
	 */
	public void updateLP () {
		for (int i = 0; i < pheromones.length; i++){
			for (int j = 0; j < pheromones.length; j++){
				if (i != j){
					Boolean isVisited = visitClient[i][j];
					if (isVisited) {
						double tauIJ = pheromones[i][j];
						double newTauIJ = (1 - alpha) * tauIJ + (alpha * tau0);
						pheromones[i][j] = newTauIJ;
					}
		        }
		    }
		}
	}
	
	/**
	 * Metodo que realiza la actualizacion global de las feromonas.
	 * @param bestRoute la mejor ruta encontrada por las hormigas.
	 * @param distance la distancia de la mejor ruta.
	 */
	public void updateGP (List<Client> bestRoute, int distance) {
		for (int i = 0; i < bestRoute.size(); i++){
			if (i < bestRoute.size() - 1) {
				Client client1 = bestRoute.get(i);
				Client client2 = bestRoute.get(i + 1);
				int indexC1 = getIndexClient(client1);
				int indexC2 = getIndexClient(client2);
				double tauIJ = pheromones[indexC1][indexC2];
				double newTauIJ = (1 - alpha) * tauIJ + (alpha * (Math.pow(distance, -1)));
				pheromones[indexC1][indexC2] = newTauIJ;
				pheromones[indexC2][indexC1] = newTauIJ;
			}
		}
	}
	
	/**
	 * Metodo que realiza la evaporación externa de las feromonas.
	 * @param delta la cantidad de feromonas que se va a evaporar.
	 */
	public void evaporateP (double delta) {
		for (int i = 0; i < pheromones.length; i++){
			for (int j = 0; j < pheromones.length; j++){
				if (i != j){
					pheromones[i][j] += delta;
		        }
		    }
		}
	}
	
}
