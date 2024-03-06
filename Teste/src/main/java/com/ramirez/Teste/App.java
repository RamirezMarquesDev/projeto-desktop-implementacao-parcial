package com.ramirez.Teste;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

import com.ramirez.Teste.dao.ConnectionFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("TelaLogin"), 736, 552);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.show();
    }
    
    //tela 2
    public static void startThirdStage() throws IOException {
    	//Teste conecxão DAO
    	try (Connection connection = ConnectionFactory.getConnection()) {
            System.out.println("Conexão bem-sucedida!");
        Stage thirdStage = new Stage();
        Scene thirdScene = new Scene(loadFXML("TelaCadastroProdutos"), 800, 600);
     
        thirdStage.setScene(thirdScene);
        thirdStage.setResizable(false);
        thirdStage.setMaximized(false); 
        thirdStage.show();
    	} catch (SQLException e) {
            System.err.println("Erro de Conexão: " + e.getMessage());
        }    	
    }
    
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
    	Locale.setDefault(new Locale("pt", "BR"));
        launch();
    }

}
