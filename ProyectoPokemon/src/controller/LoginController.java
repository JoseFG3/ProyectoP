package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import javax.swing.JOptionPane;
import bbdd.UserDAO;
import bbdd.Entrenador;
import util.SessionManager;

public class LoginController implements Initializable {

    private UserDAO prueba = new UserDAO();
    
    @FXML
    private Button btnSalir;
    
    @FXML
    private Button btnRegis;

    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private void eventKey(javafx.scene.input.KeyEvent event) {
        Object evt = event.getSource();

        if (evt.equals(txtUser)) {

            if (event.getCharacter().equals(" ")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No se admiten espacios en blanco.");
                alert.showAndWait();
                txtUser.setText("");
            }

        } else if (evt.equals(txtPassword)) {

            if (event.getCharacter().equals(" ")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No se admiten espacios en blanco.");
                alert.showAndWait();
                txtPassword.setText("");
            }

        }

    }

    @FXML
    private void eventAction(ActionEvent event) throws SQLException {

        Object evt = event.getSource();

        if (evt.equals(btnLogin)) {

            if (!txtUser.getText().isEmpty() && !txtPassword.getText().isEmpty()) {

                String user = txtUser.getText();
                String pass = txtPassword.getText();

                // Ahora el método login devuelve un objeto Entrenador
                Entrenador entrenador = prueba.login(user, pass);

                if (entrenador != null) { // Verifica si el inicio de sesión fue exitoso
                    JOptionPane.showMessageDialog(null, "Datos correctos puede ingresar al sistema");
                    // Guarda el objeto Entrenador en la sesión de usuario
                    SessionManager.setEntrenador(entrenador);

                    loadStage("../view/MENU-SCENE.fxml", event);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al iniciar sesión datos de acceso incorrectos",
                            "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error al iniciar sesión datos de acceso incorrectos",
                        "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            }
        }

    }
    
    @FXML
    private void eventRegis(ActionEvent event) {
    	
    	 Object evt = event.getSource();
    	 if(!txtUser.getText().isEmpty() && !txtPassword.getText().isEmpty()) {
    	 try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "")) {
             Statement instruccion = conn.createStatement();

             ResultSet resultado = instruccion.executeQuery("SELECT MAX(ID_ENTRENADOR) FROM entrenador");
             int ultimoID = 0;
             if (resultado.next()) {
                 ultimoID = resultado.getInt(1);
             }

             // Obtener los datos del Pokemon aleatorio
             int idEntrenador = ultimoID + 1;
             String nomEntrenador = txtUser.getText();
             String Pass = txtPassword.getText();
             int pokeDolares = 2000;
             

             String insertQuery = "INSERT INTO entrenador (ID_ENTRENADOR, NOM_ENTRENADOR, PASS, POKEDOLLARS) VALUES (?, ?, ?, ?)";
             PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
             insertStatement.setInt(1, idEntrenador);
             insertStatement.setString(2, nomEntrenador);
             insertStatement.setString(3, Pass);
             insertStatement.setInt(4, pokeDolares);
            

             int filasInsertadas = insertStatement.executeUpdate();
             if (filasInsertadas > 0) {
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("¡Usuario añadido con existo!");
                 alert.setHeaderText(null);
                 alert.setContentText("Se ha añadido el usuario: "+nomEntrenador+".");
                 alert.showAndWait();
             } 

         } catch (SQLException e) {
             System.out.println("Error de SQL: " + e.getMessage());
         }
    	 } else {
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Error");
             alert.setHeaderText(null);
             alert.setContentText("Ha ocurrido un error al insertar el usuario, por favor, asegúrate de rellenar todos los campos");
             alert.showAndWait();
         }
    }
    
    @FXML
    private void eventSalir(ActionEvent event) {
    	
    	 // Obtén la referencia de la ventana principal
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        // Cierra la ventana principal
        stage.close();
    	
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void loadStage(String url, Event event) {

        try {

            Object eventSource = event.getSource();
            Node sourceAsNode = (Node) eventSource;
            Scene oldScene = sourceAsNode.getScene();
            Window window = oldScene.getWindow();
            Stage stage = (Stage) window;
            stage.hide();

            Parent root = FXMLLoader.load(getClass().getResource(url));
            Scene scene = new Scene(root);

            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();

            newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
