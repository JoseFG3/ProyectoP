package bbdd;

import java.sql.Connection;
import java.sql.SQLException;
import bbdd.ConnectionPool;

/**
 *
 * @author JorgeLPR
 */
public class testDBConnectionPool {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {

            Connection c =  ConnectionPool.getInstance().getConnection();
            if(c!=null){
                System.out.println("conectado ");
                ConnectionPool.getInstance().closeConnection(c);
            }else{
                System.out.println("No conectado");                
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}