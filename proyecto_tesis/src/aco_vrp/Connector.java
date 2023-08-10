package aco_vrp;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que se encarga de establecer la conexion con la base de datos. 
 * @author Teresa Becerril Torres
 * @version 1.0
 */
public class Connector {
	
	/* Conexion con la base de datos. */
	private Connection connection;
	/* Ruta de la base de datos. */
    private String route;
    /* Usuario que tiene acceso a la base de datos. */
    private String username;
    /* Contrasenia del usuario. */
    private String password;
	
    /**
     * Constructor que crea un objeto Connector para hacer la conexion a la 
     * base de datos. 
     */
    public Connector () {
    	route = "jdbc:mysql://localhost:3306/cities";
    	username = "proyecttesis";
    	password = "t3s1s";
    }
    
    /**
     * Metodo que establece la conexion con la base de datos.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    public void connect () throws SQLException {
    	if (connection == null || connection.isClosed()) {
    		try {
    			Class.forName("com.mysql.cj.jdbc.Driver");
    			connection = DriverManager.getConnection(route, username, password);
    		} catch (ClassNotFoundException e) {
    			throw new SQLException(e);
    		}
    	}
    }
    
    /**
     * Metodo que desconecta la conexion con la base de datos.
     * @throws SQLException si ocurre un error al cerrar la conexión con la 
     * base de datos.
     */
    public void disconnect () throws SQLException {
        if (connection != null && !connection.isClosed()) {
        	connection.close();
        }
    }
    
    /**
     * Metodo que regresa la conexion con la base de datos.
     * @return la conexion con la base de datos.
     */
    public Connection getConnection() {
    	return connection;
    }
}
