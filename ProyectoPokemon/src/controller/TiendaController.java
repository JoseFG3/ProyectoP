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
    	int precioAnillo = 2000;

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
            if (dineroUsuario >= precioAnillo) {
                // Deducción del precio de la pesa del saldo del usuario
                dineroUsuario -= precioAnillo;
                System.out.println("Nuevo saldo después de la compra: " + dineroUsuario);


                // Verificar si ya existe la pesa en la mochila del usuario
                PreparedStatement consultaExistenciaObjeto = conexion.prepareStatement("SELECT num_objeto FROM mochila WHERE id_entrenador = ? AND id_objeto = ?");
                consultaExistenciaObjeto.setInt(1, idEntrenador);
                consultaExistenciaObjeto.setInt(2, 6); // Suponiendo que el ID del anillo es 6
                ResultSet resultadoExistenciaObjeto = consultaExistenciaObjeto.executeQuery();

                if (resultadoExistenciaObjeto.next()) {
                    // Si existe, actualizar el num_objeto
                    int numObjetoActual = resultadoExistenciaObjeto.getInt("num_objeto");
                    int nuevoNumObjeto = numObjetoActual + 1;
                    PreparedStatement actualizarObjeto = conexion.prepareStatement("UPDATE mochila SET num_objeto = ? WHERE id_entrenador = ? AND id_objeto = ?");
                    actualizarObjeto.setInt(1, nuevoNumObjeto);
                    actualizarObjeto.setInt(2, idEntrenador);
                    actualizarObjeto.setInt(3, 6); // Suponiendo que el ID del anillo es 6
                    actualizarObjeto.executeUpdate();
                    actualizarObjeto.close();
                    System.out.println("num_objeto actualizado para los anillos existentes en la mochila del usuario.");
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
                    consultaMochila.setInt(2, 6); // Suponiendo que el ID del anillo es 6
                    consultaMochila.setInt(3, nuevoNumObjeto);
                    consultaMochila.executeUpdate();
                    consultaMochila.close();
                    System.out.println("Anillo insertado en la mochila del usuario.");
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
                mostrarMensaje("Compra realizada", "Has comprado un anillo correctamente.");
            } else {
                System.out.println("Saldo insuficiente: " + dineroUsuario);
                mostrarMensaje("Error", "No tienes suficiente dinero para comprar un anillo.");
            }
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensaje("Error", "Ocurrió un error al realizar la compra.");
        }


    }

    @FXML
    void comprarBaston(ActionEvent event) {
    	int precioBaston = 450;

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
            if (dineroUsuario >= precioBaston) {
                // Deducción del precio del baston del saldo del usuario
                dineroUsuario -= precioBaston;
                System.out.println("Nuevo saldo después de la compra: " + dineroUsuario);


                // Verificar si ya existe el baston en la mochila del usuario
                PreparedStatement consultaExistenciaObjeto = conexion.prepareStatement("SELECT num_objeto FROM mochila WHERE id_entrenador = ? AND id_objeto = ?");
                consultaExistenciaObjeto.setInt(1, idEntrenador);
                consultaExistenciaObjeto.setInt(2, 4); // Suponiendo que el ID del baston es 4
                ResultSet resultadoExistenciaObjeto = consultaExistenciaObjeto.executeQuery();

                if (resultadoExistenciaObjeto.next()) {
                    // Si existe, actualizar el num_objeto
                    int numObjetoActual = resultadoExistenciaObjeto.getInt("num_objeto");
                    int nuevoNumObjeto = numObjetoActual + 1;
                    PreparedStatement actualizarObjeto = conexion.prepareStatement("UPDATE mochila SET num_objeto = ? WHERE id_entrenador = ? AND id_objeto = ?");
                    actualizarObjeto.setInt(1, nuevoNumObjeto);
                    actualizarObjeto.setInt(2, idEntrenador);
                    actualizarObjeto.setInt(3, 4); // Suponiendo que el ID del baston es 4
                    actualizarObjeto.executeUpdate();
                    actualizarObjeto.close();
                    System.out.println("num_objeto actualizado para los bastones existentes en la mochila del usuario.");
                } else {
                    // Si no existe, insertar el baston en la mochila del usuario
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
                    consultaMochila.setInt(2, 4); // Suponiendo que el ID del baston es 4
                    consultaMochila.setInt(3, nuevoNumObjeto);
                    consultaMochila.executeUpdate();
                    consultaMochila.close();
                    System.out.println("Baston insertado en la mochila del usuario.");
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
                mostrarMensaje("Compra realizada", "Has comprado un baston correctamente.");
            } else {
                System.out.println("Saldo insuficiente: " + dineroUsuario);
                mostrarMensaje("Error", "No tienes suficiente dinero para comprar un baston.");
            }
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensaje("Error", "Ocurrió un error al realizar la compra.");
        }


    }

    @FXML
    void comprarChaleco(ActionEvent event) {
        int precioChaleco = 400;

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
            if (dineroUsuario >= precioChaleco) {
                // Deducción del precio del chaleco del saldo del usuario
                dineroUsuario -= precioChaleco;
                System.out.println("Nuevo saldo después de la compra: " + dineroUsuario);


                // Verificar si ya existe el chaleco en la mochila del usuario
                PreparedStatement consultaExistenciaObjeto = conexion.prepareStatement("SELECT num_objeto FROM mochila WHERE id_entrenador = ? AND id_objeto = ?");
                consultaExistenciaObjeto.setInt(1, idEntrenador);
                consultaExistenciaObjeto.setInt(2, 3); // Suponiendo que el ID del chaleco es 3
                ResultSet resultadoExistenciaObjeto = consultaExistenciaObjeto.executeQuery();

                if (resultadoExistenciaObjeto.next()) {
                    // Si existe, actualizar el num_objeto
                    int numObjetoActual = resultadoExistenciaObjeto.getInt("num_objeto");
                    int nuevoNumObjeto = numObjetoActual + 1;
                    PreparedStatement actualizarObjeto = conexion.prepareStatement("UPDATE mochila SET num_objeto = ? WHERE id_entrenador = ? AND id_objeto = ?");
                    actualizarObjeto.setInt(1, nuevoNumObjeto);
                    actualizarObjeto.setInt(2, idEntrenador);
                    actualizarObjeto.setInt(3, 3); // Suponiendo que el ID del chaleco es 3
                    actualizarObjeto.executeUpdate();
                    actualizarObjeto.close();
                    System.out.println("num_objeto actualizado para el chaleco existente en la mochila del usuario.");
                } else {
                    // Si no existe, insertar el chaleco en la mochila del usuario
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
                    consultaMochila.setInt(2, 3); // Suponiendo que el ID del chaleco es 3
                    consultaMochila.setInt(3, nuevoNumObjeto);
                    consultaMochila.executeUpdate();
                    consultaMochila.close();
                    System.out.println("Chaleco insertado en la mochila del usuario.");
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
                mostrarMensaje("Compra realizada", "Has comprado un chaleco correctamente.");
            } else {
                System.out.println("Saldo insuficiente: " + dineroUsuario);
                mostrarMensaje("Error", "No tienes suficiente dinero para comprar un chaleco.");
            }
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensaje("Error", "Ocurrió un error al realizar la compra.");
        }
    }


    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    @FXML
    void comprarPesa(ActionEvent event) {
    	int precioPesa = 500;

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
            if (dineroUsuario >= precioPesa) {
                // Deducción del precio de la pesa del saldo del usuario
                dineroUsuario -= precioPesa;
                System.out.println("Nuevo saldo después de la compra: " + dineroUsuario);


                // Verificar si ya existe la pesa en la mochila del usuario
                PreparedStatement consultaExistenciaObjeto = conexion.prepareStatement("SELECT num_objeto FROM mochila WHERE id_entrenador = ? AND id_objeto = ?");
                consultaExistenciaObjeto.setInt(1, idEntrenador);
                consultaExistenciaObjeto.setInt(2, 1); // Suponiendo que el ID de la pesa es 1
                ResultSet resultadoExistenciaObjeto = consultaExistenciaObjeto.executeQuery();

                if (resultadoExistenciaObjeto.next()) {
                    // Si existe, actualizar el num_objeto
                    int numObjetoActual = resultadoExistenciaObjeto.getInt("num_objeto");
                    int nuevoNumObjeto = numObjetoActual + 1;
                    PreparedStatement actualizarObjeto = conexion.prepareStatement("UPDATE mochila SET num_objeto = ? WHERE id_entrenador = ? AND id_objeto = ?");
                    actualizarObjeto.setInt(1, nuevoNumObjeto);
                    actualizarObjeto.setInt(2, idEntrenador);
                    actualizarObjeto.setInt(3, 1); // Suponiendo que el ID de la pesa es 1
                    actualizarObjeto.executeUpdate();
                    actualizarObjeto.close();
                    System.out.println("num_objeto actualizado para las pesas existentes en la mochila del usuario.");
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
                    consultaMochila.setInt(2, 1); // Suponiendo que el ID de las pesas es 1
                    consultaMochila.setInt(3, nuevoNumObjeto);
                    consultaMochila.executeUpdate();
                    consultaMochila.close();
                    System.out.println("Pesas insertadas en la mochila del usuario.");
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
                mostrarMensaje("Compra realizada", "Has comprado unas pesas correctamente.");
            } else {
                System.out.println("Saldo insuficiente: " + dineroUsuario);
                mostrarMensaje("Error", "No tienes suficiente dinero para comprar unas pesas.");
            }
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensaje("Error", "Ocurrió un error al realizar la compra.");
        }

    }

    @FXML
    void comprarPila(ActionEvent event) {
    	int precioPila = 350;

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
            if (dineroUsuario >= precioPila) {
                // Deducción del precio de la pila del saldo del usuario
                dineroUsuario -= precioPila;
                System.out.println("Nuevo saldo después de la compra: " + dineroUsuario);


                // Verificar si ya existe la pila en la mochila del usuario
                PreparedStatement consultaExistenciaObjeto = conexion.prepareStatement("SELECT num_objeto FROM mochila WHERE id_entrenador = ? AND id_objeto = ?");
                consultaExistenciaObjeto.setInt(1, idEntrenador);
                consultaExistenciaObjeto.setInt(2, 5); // Suponiendo que el ID de la pila es 1
                ResultSet resultadoExistenciaObjeto = consultaExistenciaObjeto.executeQuery();

                if (resultadoExistenciaObjeto.next()) {
                    // Si existe, actualizar el num_objeto
                    int numObjetoActual = resultadoExistenciaObjeto.getInt("num_objeto");
                    int nuevoNumObjeto = numObjetoActual + 1;
                    PreparedStatement actualizarObjeto = conexion.prepareStatement("UPDATE mochila SET num_objeto = ? WHERE id_entrenador = ? AND id_objeto = ?");
                    actualizarObjeto.setInt(1, nuevoNumObjeto);
                    actualizarObjeto.setInt(2, idEntrenador);
                    actualizarObjeto.setInt(3, 5); // Suponiendo que el ID de la pila es 5
                    actualizarObjeto.executeUpdate();
                    actualizarObjeto.close();
                    System.out.println("num_objeto actualizado para las pilas existentes en la mochila del usuario.");
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
                    consultaMochila.setInt(2, 5); // Suponiendo que el ID de la pila es 5
                    consultaMochila.setInt(3, nuevoNumObjeto);
                    consultaMochila.executeUpdate();
                    consultaMochila.close();
                    System.out.println("Pilas insertadas en la mochila del usuario.");
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
                mostrarMensaje("Compra realizada", "Has comprado unas pilas correctamente.");
            } else {
                System.out.println("Saldo insuficiente: " + dineroUsuario);
                mostrarMensaje("Error", "No tienes suficiente dinero para comprar unas pilas.");
            }
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensaje("Error", "Ocurrió un error al realizar la compra.");
        }


    }

    @FXML
    void comprarPluma(ActionEvent event) {
    	int precioPluma = 300;

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
            if (dineroUsuario >= precioPluma) {
                // Deducción del precio de la pluma del saldo del usuario
                dineroUsuario -= precioPluma;
                System.out.println("Nuevo saldo después de la compra: " + dineroUsuario);


                // Verificar si ya existe la pluma en la mochila del usuario
                PreparedStatement consultaExistenciaObjeto = conexion.prepareStatement("SELECT num_objeto FROM mochila WHERE id_entrenador = ? AND id_objeto = ?");
                consultaExistenciaObjeto.setInt(1, idEntrenador);
                consultaExistenciaObjeto.setInt(2, 2); // Suponiendo que el ID de la pluma es 2
                ResultSet resultadoExistenciaObjeto = consultaExistenciaObjeto.executeQuery();

                if (resultadoExistenciaObjeto.next()) {
                    // Si existe, actualizar el num_objeto
                    int numObjetoActual = resultadoExistenciaObjeto.getInt("num_objeto");
                    int nuevoNumObjeto = numObjetoActual + 1;
                    PreparedStatement actualizarObjeto = conexion.prepareStatement("UPDATE mochila SET num_objeto = ? WHERE id_entrenador = ? AND id_objeto = ?");
                    actualizarObjeto.setInt(1, nuevoNumObjeto);
                    actualizarObjeto.setInt(2, idEntrenador);
                    actualizarObjeto.setInt(3, 2); // Suponiendo que el ID de la pluma es 2
                    actualizarObjeto.executeUpdate();
                    actualizarObjeto.close();
                    System.out.println("num_objeto actualizado para las plumas existentes en la mochila del usuario.");
                } else {
                    // Si no existe, insertar la pluma en la mochila del usuario
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
                    consultaMochila.setInt(2, 2); // Suponiendo que el ID de la pluma es 2
                    consultaMochila.setInt(3, nuevoNumObjeto);
                    consultaMochila.executeUpdate();
                    consultaMochila.close();
                    System.out.println("Pluma insertada en la mochila del usuario.");
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
                mostrarMensaje("Compra realizada", "Has comprado una pluma correctamente.");
            } else {
                System.out.println("Saldo insuficiente: " + dineroUsuario);
                mostrarMensaje("Error", "No tienes suficiente dinero para comprar una pluma.");
            }
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensaje("Error", "Ocurrió un error al realizar la compra.");
        }


    }

    @FXML
    void comprarPokeball(ActionEvent event) {
    	int precioPokeball = 50;

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
            if (dineroUsuario >= precioPokeball) {
                // Deducción del precio de la pokeball del saldo del usuario
                dineroUsuario -= precioPokeball;
                System.out.println("Nuevo saldo después de la compra: " + dineroUsuario);


                // Verificar si ya existe la pokeball en la mochila del usuario
                PreparedStatement consultaExistenciaObjeto = conexion.prepareStatement("SELECT num_objeto FROM mochila WHERE id_entrenador = ? AND id_objeto = ?");
                consultaExistenciaObjeto.setInt(1, idEntrenador);
                consultaExistenciaObjeto.setInt(2, 7); // Suponiendo que el ID de la pokeball es 7
                ResultSet resultadoExistenciaObjeto = consultaExistenciaObjeto.executeQuery();

                if (resultadoExistenciaObjeto.next()) {
                    // Si existe, actualizar el num_objeto
                    int numObjetoActual = resultadoExistenciaObjeto.getInt("num_objeto");
                    int nuevoNumObjeto = numObjetoActual + 1;
                    PreparedStatement actualizarObjeto = conexion.prepareStatement("UPDATE mochila SET num_objeto = ? WHERE id_entrenador = ? AND id_objeto = ?");
                    actualizarObjeto.setInt(1, nuevoNumObjeto);
                    actualizarObjeto.setInt(2, idEntrenador);
                    actualizarObjeto.setInt(3, 7); // Suponiendo que el ID de la pokeball es 7
                    actualizarObjeto.executeUpdate();
                    actualizarObjeto.close();
                    System.out.println("num_objeto actualizado para las pokeballs existentes en la mochila del usuario.");
                } else {
                    // Si no existe, insertar la pokeball en la mochila del usuario
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
                    consultaMochila.setInt(2, 7); // Suponiendo que el ID de la pokeball es 7
                    consultaMochila.setInt(3, nuevoNumObjeto);
                    consultaMochila.executeUpdate();
                    consultaMochila.close();
                    System.out.println("Pokeball insertada en la mochila del usuario.");
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
                mostrarMensaje("Compra realizada", "Has comprado una pokeball correctamente.");
            } else {
                System.out.println("Saldo insuficiente: " + dineroUsuario);
                mostrarMensaje("Error", "No tienes suficiente dinero para comprar una pokeball.");
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
