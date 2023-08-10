package aco_vrp;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import org.javatuples.Pair;

/**
 * Clase que se encarga de construir las listas de candidatos para todos 
 * los clientes. 
 * @author Teresa Becerril Torres
 * @version 1.0
 */
public class Candidates {

	/* Longitud de la lista de candidatos. */
	private int length;
	
	/**
	 * Constructor que crea un objeto Candidates.
	 * @param length la longitud de la lista de candidato.
	 */
	public Candidates (int length) {
		this.length = length;
	}
	
	/**
	 * Metodo que obtine y actualiza la lista de candidatos para cada uno de 
	 * los clientes.
	 * @param clients la lista de clientes.
	 */
	public void obtainCandidates (List<Client> clients) {
		Client depot = clients.get(0);
		for (Client client: clients) {
			List<Integer> candidates = searchCandidates(client, depot);
			client.setCandidates(candidates);
		}
	}
	
	/**
	 * Metodo que busca la lista de candidatos del cliente.
	 * @param client el cliente al que se le busca la lista de candidatos.
	 * @param depot el deposito.
	 * @return la lista de candidatos del cliente.
	 */
	public List<Integer> searchCandidates(Client client, Client depot) {
		List<Pair<Integer, Integer>> distances = client.getDistances();
		List<Integer> candidates = new ArrayList<Integer>();
		
		distances.sort(new Comparator<Pair<Integer, Integer>> () {
			@Override 
			public int compare (Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
				return p1.getValue1() - p2.getValue1();
			}
		});
		
		for (int i = 0; i < distances.size() ; i++) {
			int idClient = distances.get(i).getValue0();
			if (idClient != depot.getId()) {
				candidates.add(idClient);
			}
			if(candidates.size() == length) {
				break;
			}
		}
		
		return  candidates;
	}
}
