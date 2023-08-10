package aco_vrp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;


/**
 * Clase que se conecta con la base de datos y obtiene los datos de los 
 * clientes y las distancias entre cada par de clientes. 
 * @author Teresa Becerril Torres
 * @version 1.0
 */
public class SearchBD {
	
	/* Conexion con la base de datos. */
	private Connection con;
	/* Conector de la base de datos. */
	private Connector connector;
	
	/**
	 * Constructor que crea un objeto SearchBD.
	 */
	public SearchBD () {
		connector = new Connector();
	}
	 
	/**
	 * Metodo que obtiene un cliente a partir del id.
	 * @param idCity el id del cliente
	 * @return el cliente que tiene ese id. 
	 * @throws SQLException si ocurre un error al acceder a la base de datos.
	 */
	public Client getClient (int idCity) throws SQLException {
		Client client = null;
		String sql = "SELECT * FROM cities WHERE id_city = ?";
		connector.connect();
		con = connector.getConnection();
		PreparedStatement statement = con.prepareStatement(sql);
		statement.setInt(1, idCity);
		ResultSet res = statement.executeQuery();
		if(res.next()) {
			client = new Client(res.getInt("id_city"), res.getString("city"), res.getString("state"));
		}
		res.close();
		connector.disconnect();
		return client;
	}
	
	/**
	 * Metodo que regresa la lista de clientes que su id pertenece a la lista IDs. 
	 * @param IDs la lista con los IDs de los clientes.
	 * @return la lista de clientes que su id pertenece a la lista IDs.
	 * @throws SQLException si ocurre un error al acceder a la base de datos.
	 */
	public List<Client> getClients (List<Integer> IDs) throws SQLException {
		List<Client> clients = new ArrayList<Client>();
		String sql = "SELECT * FROM cities WHERE id_city = ?";
		connector.connect();
		con = connector.getConnection();
		for (Integer id: IDs) {
			Client client = null;
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet res = statement.executeQuery();
			if(res.next()) {
				client = new Client(res.getInt("id_city"), res.getString("city"), res.getString("state"));
			}
			clients.add(client);
		}
		connector.disconnect();
		return clients;
	}
	
	/**
	 * Metodo que obtiene la distancia entre el cliente 1 y el cliente 2.
	 * @param id_city1 el id del cliente 1.
	 * @param id_city2 el id del cliente 2.
	 * @return la distancia entre el cliente 1 y el cliente 2.
	 * @throws SQLException si ocurre un error al acceder a la base de datos.
	 */
	public Integer getDistance (int idCity1, int idCity2) throws SQLException {
		int distance = -1;
		String sql = "SELECT * FROM distances WHERE id_city1 = ? AND id_city2 = ?";
		connector.connect();
		con = connector.getConnection();
		PreparedStatement statement = con.prepareStatement(sql);
		statement.setInt(1, idCity1);
		statement.setInt(2, idCity2);
		ResultSet res = statement.executeQuery();
		if(res.next()) {
			distance = res.getInt("distance");
		}
		res.close();
		connector.disconnect();
		return distance;
	}
	
	/**
	 * Método que regresa la lista de tuplas con el id del cliente n y la distancia entre 
	 * el cliente 1 y el cliente n.
	 * @param id_city el id del cliente 1. 
	 * @param IDs la lista con los IDs de los clientes n.
	 * @return la lista de tuplas con el id del cliente n y la distancia entre 
	 * el cliente 1 y el cliente n.
	 * @throws SQLException si ocurre un error al acceder a la base de datos.
	 */
	public List<Pair<Integer, Integer>> getDistances (int idCity, List<Integer> IDs) throws SQLException {
		List<Pair<Integer, Integer>> distances = new ArrayList<Pair<Integer, Integer>>();
		String sql = "SELECT * FROM distances WHERE id_city1 = ? and id_city2 = ?";
		connector.connect();
		con = connector.getConnection();
		for (Integer id: IDs) {
			if (idCity != id) {
				Pair<Integer, Integer> distance = null;
				PreparedStatement statement = con.prepareStatement(sql);
				statement.setInt(1, idCity);
				statement.setInt(2, id);
				ResultSet res = statement.executeQuery();
				if(res.next()) {
					distance = new Pair<Integer, Integer>(id, res.getInt("distance"));
				}
				distances.add(distance);
			}
		}
		connector.disconnect();
		return distances;
	}
}
