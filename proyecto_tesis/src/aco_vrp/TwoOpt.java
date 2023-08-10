package aco_vrp;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que mejora las rutas utilizando la heuristica 2-opt. 
 * @author Teresa Becerril Torres
 * @version 1.0
 **/
public class TwoOpt {
	
	/* Instancia de la clase VRP*/
	private VRP vrp;
	
	/**
	 * Constructor que crea un objeto TwoOpt.
	 * @param vrp la instancia de la clase VRP.
	 */
	public TwoOpt (VRP vrp) {
		this.vrp = vrp;
	}
	
	
	/**
	 * Metodo que mejora una ruta utilizando la heuristica 2-opt
	 * @param bestRoute la ruta que se va a mejorar.
	 * @param bestDistance la distancia de la ruta que se va a mejorar. 
	 * @return la ruta mejorada.
	 */
	public List<Client> improveRoute (List<Client> route, int distance) {
		List<Client> bestRoute = new ArrayList<Client>();
		int bestDistance;
        int improve = 1;
		
		while (improve != 0) {
			
			improve = 0;
			
			for (int i = 1; i < route.size(); i++) {
				for (int j = i + 1; j < route.size() - 1; j++) {
					bestRoute = swap(route, i, j);
					bestDistance = vrp.calculateDistance(bestRoute);
					
					if (bestDistance < distance) {
						route = bestRoute;
						distance = bestDistance;
						improve++;
					}
				}
			}
		}
		
		return route;
	}
	
	/**
	 * Metodo que realiza un intercambio 2-opt valido entre el cliente 1 y cliente 2.
	 * @param route la ruta a la cual se le va aplicar el intercambio.
	 * @param i el indice del cliente 1 en la ruta.
	 * @param j el indice del cliente 2 en la ruta.
	 * @return
	 */
	public List<Client> swap (List<Client> route, int i, int j) {
		List<Client> newRoute = new ArrayList<Client>();
		int r = 0;
		
		for (int k = 0; k <= i - 1; k++) {
			newRoute.add(route.get(k));
		}
		
		for (int k = i; k <= j; k++) {
			newRoute.add(route.get(j - r));
			r++;
		}
		
		for (int k = j + 1; k < route.size(); k++) {
			newRoute.add(route.get(k));
		}
		
		return newRoute;
	}
}
