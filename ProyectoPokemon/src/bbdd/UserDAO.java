package bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UserDAO {
    
    // Método para verificar las credenciales del usuario y devolver un objeto Entrenador si las credenciales son correctas
    public Entrenador login(String username, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement pst;
        ResultSet rs;
        Entrenador entrenador = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();

            if (connection != null) {
                String sql = "SELECT * FROM entrenador WHERE BINARY NOM_ENTRENADOR=? AND PASS=?";
                pst = connection.prepareStatement(sql);
                pst.setString(1, username);
                pst.setString(2, password);

                rs = pst.executeQuery();

                if (rs.next()) {
                    // Crear un nuevo objeto Entrenador con los datos recuperados de la base de datos
                    entrenador = new Entrenador(rs.getString("NOM_ENTRENADOR"), rs.getString("PASS"), rs.getInt("POKEDOLLARS"), rs.getInt("ID_ENTRENADOR"));
                }
            } else {
                JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Hubo un error de ejecución, posibles errores:\n" + ex.getMessage());
        } finally {
            try {
                if (connection != null) {
                    ConnectionPool.getInstance().closeConnection(connection);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return entrenador; // Devuelve el objeto Entrenador recuperado de la base de datos
    }
}
