package bbdd;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;



public class UserDAO {
	
	public int login (String user, String password) throws SQLException {
		
		Connection connection = null;
		PreparedStatement pst;
		ResultSet rs;
		int state = -1;
		
		try {
			
			connection = ConnectionPool.getInstance().getConnection();
			
			if(connection!=null) {
				
				String sql = "SELECT NOM_ENTRENADOR, PASS FROM entrenador WHERE BINARY NOM_ENTRENADOR=? AND pass=?";
				
				pst = connection.prepareStatement(sql);
				pst.setString(1, user);
				pst.setString(2, password);
				
				rs = pst.executeQuery();
				
				if(rs.next()) {
					state=-1;
				}else {
					state=0;
				}
			}else {
				JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
		} catch(HeadlessException | SQLException ex) {
			JOptionPane.showMessageDialog(null, "Hubo un error de ejecución, posibles errores:\n"
											+ ex.getMessage());
		}finally {
			
			try {
				if(connection !=null) {
					ConnectionPool.getInstance().closeConnection(connection);
				}
			} catch(SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		
		return state;
	}
}

