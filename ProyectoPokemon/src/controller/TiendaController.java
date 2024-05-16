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
import javafx.scene.control.Alert;
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
    private Button btnPokeball;
 
    @FXML
    private Button btnReturn;
    private int dineroUsuario;
    int idEntrenador = SessionManager.getEntrenador().getId_entrenador();
    
    @FXML
    void comprarAnillo(ActionEvent event) {
    	comprarObjeto("Anillo", 2000, 6);
 
    }
 
    @FXML
    void comprarBaston(ActionEvent event) {
    	comprarObjeto("Baston", 450, 4);
    }
 
    @FXML
    void comprarChaleco(ActionEvent event) {
    	comprarObjeto("Chaleco", 400, 3);
    }
 
 
    
    @FXML
    void comprarPesa(ActionEvent event) {
    	comprarObjeto("Pesas", 500, 1);
 
    }
 
    @FXML
    void comprarPila(ActionEvent event) {
    	comprarObjeto("Pila", 350, 5);
 
    }
 
    @FXML
    void comprarPluma(ActionEvent event) {
    	comprarObjeto("Pluma", 300, 2);
    }
 
    @FXML
    void comprarPokeball(ActionEvent event) {
    	comprarObjeto("Pokeball", 50, 7);
    }
    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void comprarObjeto(String nombreObjeto, int precio, int idObjeto) {
 
        try {
            // Conexión a la base de datos
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
            System.out.println("Conexión a la base de datos establecida.");
 
            // Verificar el valor de idEntrenador
            System.out.println("idEntrenadorSesion: " + idEntrenador);
 
            // Obtener el saldo actual del usuario desde la base de datos
            PreparedStatement consultaSaldoActual = conexion.prepareStatement("SELECT POKEDOLLARS FROM entrenador WHERE id_entrenador = ?");
            consultaSaldoActual.setInt(1, idEntrenador);
            ResultSet resultadoSaldo = consultaSaldoActual.executeQuery();
 
            if (resultadoSaldo.next()) {
                dineroUsuario = resultadoSaldo.getInt("POKEDOLLARS");
                System.out.println("Saldo actual del usuario: " + dineroUsuario);
            } else {
                System.out.println("No se encontró el usuario con id: " + idEntrenador);
                mostrarMensaje("Error", "No se encontró el usuario.");
                return; // Salir del método si no se encontró el usuario
            }
            resultadoSaldo.close();
            consultaSaldoActual.close();
 
            // Verificar si el usuario tiene suficiente dinero
            System.out.println("Verificando si el usuario tiene suficiente dinero...");
            if (dineroUsuario >= precio) {
                // Deducción del precio del objeto al saldo del usuario
                dineroUsuario -= precio;
                System.out.println("Nuevo saldo después de la compra: " + dineroUsuario);
 
 
                // Verificar si ya existe el objeto en la mochila del usuario
                PreparedStatement consultaExistenciaObjeto = conexion.prepareStatement("SELECT num_objeto FROM mochila WHERE id_entrenador = ? AND id_objeto = ?");
                consultaExistenciaObjeto.setInt(1, idEntrenador);
                consultaExistenciaObjeto.setInt(2, idObjeto);
                ResultSet resultadoExistenciaObjeto = consultaExistenciaObjeto.executeQuery();
 
                if (resultadoExistenciaObjeto.next()) {
                    // Si existe, actualizar el num_objeto
                    int numObjetoActual = resultadoExistenciaObjeto.getInt("num_objeto");
                    int nuevoNumObjeto = numObjetoActual + 1;
                    PreparedStatement actualizarObjeto = conexion.prepareStatement("UPDATE mochila SET num_objeto = ? WHERE id_entrenador = ? AND id_objeto = ?");
                    actualizarObjeto.setInt(1, nuevoNumObjeto);
                    actualizarObjeto.setInt(2, idEntrenador);
                    actualizarObjeto.setInt(3, idObjeto); 
                    actualizarObjeto.executeUpdate();
                    actualizarObjeto.close();
                    System.out.println("num_objeto actualizado para el número de "+nombreObjeto+" existentes en la mochila del usuario.");
                } else {
                    // Si no existe, insertar las pesas en la mochila del usuario
                    PreparedStatement consultaMaxNumObjeto = conexion.prepareStatement("SELECT COALESCE(MAX(num_objeto), 0) AS max_num_objeto FROM mochila WHERE id_entrenador = ?");
                    consultaMaxNumObjeto.setInt(1, 1);
                    ResultSet resultadoMaxNumObjeto = consultaMaxNumObjeto.executeQuery();
 
                    int nuevoNumObjeto = 1; // Valor por defecto si no hay registros previos
                    if (resultadoMaxNumObjeto.next()) {
                        nuevoNumObjeto = resultadoMaxNumObjeto.getInt("max_num_objeto") + 1;
                    }
                    resultadoMaxNumObjeto.close();
                    consultaMaxNumObjeto.close();
 
                    System.out.println("Nuevo num_objeto: " + nuevoNumObjeto);
 
                    PreparedStatement consultaMochila = conexion.prepareStatement("INSERT INTO mochila (id_entrenador, id_objeto, num_objeto) VALUES (?, ?, ?)");
                    consultaMochila.setInt(1, idEntrenador);
                    consultaMochila.setInt(2, idObjeto);
                    consultaMochila.setInt(3, nuevoNumObjeto);
                    consultaMochila.executeUpdate();
                    consultaMochila.close();
                    System.out.println(nombreObjeto+" insertado en la mochila del usuario.");
                }
                resultadoExistenciaObjeto.close();
                consultaExistenciaObjeto.close();;
 
                // Actualizar el saldo del usuario en la tabla entrenador
                PreparedStatement actualizarSaldo = conexion.prepareStatement("UPDATE entrenador SET POKEDOLLARS = ? WHERE id_entrenador = ?");
                actualizarSaldo.setInt(1, dineroUsuario);
                actualizarSaldo.setInt(2, idEntrenador);
                actualizarSaldo.executeUpdate();
                actualizarSaldo.close();
                System.out.println("Saldo del usuario actualizado en la base de datos.");
 
                // Actualizar el label del dinero del usuario en la interfaz gráfica
                lblDinero.setText(String.valueOf(dineroUsuario));
 
                // Mostrar mensaje de compra exitosa
                mostrarMensaje("Compra realizada", "Has comprado "+nombreObjeto+" correctamente.");
            } else {
                System.out.println("Saldo insuficiente: " + dineroUsuario);
                mostrarMensaje("Error", "No tienes suficiente dinero para comprar "+nombreObjeto+".");
            }
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensaje("Error", "Ocurrió un error al realizar la compra.");
        }
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