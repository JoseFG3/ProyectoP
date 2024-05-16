package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.SessionManager;

public class TiendaController implements Initializable{

    @FXML
    private Button btnAnillo;

    @FXML
    private Button btnBaston;

    @FXML
    private Button btnChaleco;

    @FXML
    private Button btnPesa;

    @FXML
    private Button btnPila;

    @FXML
    private Button btnPluma;

    @FXML
    private Button btnPokedex;

    @FXML
    private Button btnReturn;

    @FXML
    void comprarAnillo(ActionEvent event) {

    }

    @FXML
    void comprarBaston(ActionEvent event) {

    }

    @FXML
    void comprarChaleco(ActionEvent event) {

    }

    @FXML
    void comprarPesa(ActionEvent event) {

    }

    @FXML
    void comprarPila(ActionEvent event) {

    }

    @FXML
    void comprarPluma(ActionEvent event) {

    }

    @FXML
    void comprarPokedex(ActionEvent event) {

    }
    @FXML
    private Label lblDinero;

    @FXML
    void irMenu(ActionEvent event) {
    	loadStage("../view/MENU-SCENE.fxml", event);
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		cargarDineroUsuarioDesdeBD();
		
	}
	
	private void cargarDineroUsuarioDesdeBD() {

        int idEntrenador = SessionManager.getEntrenador().getId_entrenador();
        Connection conexion = null;
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
            String sql = "SELECT POKEDOLLARS FROM entrenador WHERE id_entrenador =?";
            consulta = conexion.prepareStatement(sql);
            consulta.setInt(1,idEntrenador);
            resultado = consulta.executeQuery();
            // Verificar si se encontró el usuario y obtener el dinero
            if (resultado.next()) {
                int dineroUsuario = resultado.getInt("POKEDOLLARS");
                // Actualizar el label con el dinero del usuario
                lblDinero.setText(String.valueOf(dineroUsuario));
            } else {
                System.out.println("Usuario no encontrado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (consulta != null) {
                    consulta.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
