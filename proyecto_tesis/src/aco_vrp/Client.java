package aco_vrp;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;

/**
 * Clase que representa un cliente. 
 * @author Teresa Becerril Torres
 * @version 1.0
 */
public class Client {

	/* ID del cliente. */
	private int id;
	/* Ciudad del cliente. */
	private String city;
	/* Estado del cliente. */
	private String state; 
	/* Demanda del cliente. */
	private int demand;
	/* Bandera que indica si el cliente es el deposito central. */
	private Boolean isDepot;
	/* Lista de candidatos del cliente. */
	private List<Integer> candidates;
	/* Lista de distancias del cliente con la siguiente forma [ID, distancia] */
	private List<Pair<Integer, Integer>> distances;
	
	/**
	 * Constructor que crea un objeto Client.
	 * @param id el ID del cliente.
	 * @param city la ciudad del cliente. 
	 * @param state el estado del cliente.
	 */
	public Client (int id, String city, String state) {
		this.id = id;
		this.city = city;
		this.state = state;
		demand = -1;
		isDepot = false;
		candidates = new ArrayList<Integer>();
		distances = new ArrayList<Pair<Integer, Integer>>();
	}
	
	/**
	 * Metodo que regresa el ID del cliente.
	 * @return el ID del cliente.
	 */
	public int getId () {
		return id;
	}
	
	/**
	 * Metodo que regresa la ciudad del cliente.
	 * @return la ciudad del cliente.
	 */
	public String getCity () {
		return city;
	}
	
	/**
	 * Metodo que regresa el estado del cliente.
	 * @return el estado del cliente.
	 */
	public String getState () {
		return state;
	}
	
	/**
	 * Metodo que regresa la demanda del cliente.
	 * @return la demanda del cliente.
	 */
	public int getDemand () {
		return demand;
	}
	
	/**
	 * Metodo que regresa si el cliente es el deposito central o no.
	 * @return true si el cliente es el deposito central, false en otro caso.
	 */
	public Boolean getIsDepot () {
		return isDepot;
	}
	
	/**
	 * Metodo que regresa la lista de candidatos del cliente.
	 * @return la lista de candidatos del cliente.
	 */
	public List<Integer> getCandidates () {
		return candidates;
	}
	
	/**
	 * Metodo que regresa la lista de distancias del cliente.
	 * @return la lista de distancias del cliente.
	 */
	public List<Pair<Integer, Integer>> getDistances () {
		return distances;
	}
	
	/**
	 * Metodo que actualiza la demanda del cliente.
	 * @param demand la nueva demanda del cliente.
	 */
	public void setDemand (int demand) {
		this.demand = demand;
	}
	
	/**
	 * Metodo que actualiza la bandera si el cliente es el deposito central 
	 * @param isDepot la bandera que indica que el cliente es el deposito 
	 * central.
	 */
	public void setIsDepot (Boolean isDepot) {
		this.isDepot = isDepot;
	}
	
	/**
	 * Metodo que actualiza la lista de candidatos del cliente.
	 * @param candidates la nueva lista de candidatos del cliente.
	 */
	public void setCandidates (List<Integer> candidates) {
		this.candidates = candidates;
	}
	
	/**
	 * Metodo que actualiza la lista de distancias del cliente.
	 * @param candidates la nueva lista de distancias del cliente.
	 */
	public void setDistances (List<Pair<Integer, Integer>> distances) {
		this.distances = distances;
	}
	
	/**
	 * Metodo que busca la distancia entre el cliente y el cliente 2. 
	 * @param idClient2 el id del cliente 2.
	 * @return la distancia entre el cliente y el client2.  
	 */
	public int searchDistance (int idClient2) {
		int distance = 0;
		
		for (Pair<Integer, Integer> aux : distances) {
			int idClientA = aux.getValue0();
			if (idClient2 == idClientA) {
				distance = aux.getValue1();
			}
		}
		
		return distance;
	}
}
