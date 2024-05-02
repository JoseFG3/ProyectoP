package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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


public class LoginController implements Initializable {

    private UserDAO prueba = new UserDAO();
    
    @FXML
    private TextField txtUser;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Button btnLogin;
    
    @FXML
    private void eventKey(KeyEvent event){
        
        Object evt = event.getSource();
        
        if(evt.equals(txtUser)){
            
            if(event.getCharacter().equals(" ")){
            	Alert alert = new Alert(Alert.AlertType.ERROR, "No se admiten espacios en blanco.");
                alert.showAndWait();
                txtUser.setText("");
            }
        
        }else if(evt.equals(txtPassword)){

            if(event.getCharacter().equals(" ")){
            	Alert alert = new Alert(Alert.AlertType.ERROR, "No se admiten espacios en blanco.");
                alert.showAndWait();
                txtPassword.setText("");
            }            
        
        }
        
        
        
    }
    
    @FXML
    private void eventAction(ActionEvent event) throws SQLException{
        
        Object evt = event.getSource();

        if(evt.equals(btnLogin)){
                                
            if(!txtUser.getText().isEmpty() && !txtPassword.getText().isEmpty()){
            
                String user = txtUser.getText();
                String pass = txtPassword.getText();
                
                int state = prueba.login(user, pass);
                
                if(state!=-1){

                    if(state == 1){

                        JOptionPane.showMessageDialog(null, "Datos correctos puede ingresar al sistema");
                        
                        loadStage("../view/MENU SCENE.fxml", event);

                    }else{
                        JOptionPane.showMessageDialog(null, "Error al iniciar sesión datos de acceso incorrectos", 
                                                            "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);                
                    }

                }                

            
            }else{            
                    JOptionPane.showMessageDialog(null, "Error al iniciar sesión datos de acceso incorrectos", 
                                                        "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);                                
            }
        
        }
    
    }
    
    
    
       @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
   private void loadStage(String url, Event event){
            
        try {  
            
            
            Object eventSource = event.getSource(); 
            Node sourceAsNode = (Node) eventSource ;
            Scene oldScene = sourceAsNode.getScene();
            Window window = oldScene.getWindow();
            Stage stage = (Stage) window ;
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