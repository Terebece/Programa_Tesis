package aco_vrp;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una hormiga. 
 * @author Teresa Becerril Torres
 * @version 1.0
 */
public class Ant {
	
	/* ID de la hormiga. */
	private int id;
	/* Capacidad de carga de la hormiga. */
	private int capacity;
	/* Cliente actual en el que se encuentra la hormiga. */
	private Client currentClient;
	/* Memoria de la hormiga. */
	private List<Client> memory;
	/* Bandera que indica si la hormiga ya termino su ruta. */
	private Boolean finish;
	
	/**
	 * Constructor que crea un objeto Ant.
	 * @param id el id de la hormiga.
	 * @param capacity la capacidad de carga de la hormiga.
	 */
	public Ant(int id, int capacity, Client currentClient) {
		this.id = id;
		this.capacity = capacity;
		this.currentClient = currentClient;
		memory = new ArrayList<Client>();
		memory.add(currentClient);
		finish = false;
	}
	
	/**
	 * Metodo que regresa el ID de la hormiga.
	 * @return el ID de la hormiga.
	 */
	public int getId () {
		return id;
	}
	
	/**
	 * Metodo que regresa la capacidad de la hormiga.
	 * @return la capacidad de la hormiga.
	 */
	public int getCapacity () {
		return capacity;
	}
	
	/**
	 * Metodo que regresa el cliente actual de la hormiga.
	 * @return el cliente actual de la hormiga.
	 */
	public Client getCurrentClient () {
		return currentClient;
	}
	
	/**
	 * Metodo que regresa la memoria de la hormiga.
	 * @return la memoria de la hormiga.
	 */
	public List<Client> getMemory () {
		return memory;
	}
	
	/**
	 * Metodo que indica si la hormiga ya completó su ruta.
	 * @return true si la hormiga ya termino su ruta, false en otro caso.
	 */
	public Boolean getFinish () {
		return finish;
	}
	
	/**
	 * Metodo que regresa la capacidad de la hormiga.
	 * @return la capacidad de la hormiga.
	 */
	public void setCapacity (int capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * Metodo que actualiza la memoria de la hormiga.
	 * @param finish la nueva memoria de la hormiga.
	 */
	public void setMemory (List<Client> memory) {
		this.memory = memory;
	}
	
	/**
	 * Metodo que actualiza la bandera que indica si la hormiga ya completó su ruta.
	 * @param finish la nueva bandera que indica si la hormiga ya completó su ruta.
	 */
	public void setFinish (Boolean finish) {
		this.finish = finish;
	}
	
	/**
	 * Metodo que agrega el cliente actual de la hormiga.
	 * @param currentClient el cliente actual de la hormiga.
	 */
	public void addCurrentClient (Client currentClient) {
		this.currentClient = currentClient;
		memory.add(currentClient);
		int demand = currentClient.getDemand();
		capacity -= demand;
	}
}
