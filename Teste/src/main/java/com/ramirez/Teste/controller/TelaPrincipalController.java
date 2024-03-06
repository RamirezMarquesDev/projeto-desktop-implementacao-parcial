package com.ramirez.Teste.controller;

import java.io.IOException;

import com.ramirez.Teste.App;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;

public class TelaPrincipalController {

    @FXML
    private Pane pane2;

    @FXML
    private Menu menuProdutos;

    @FXML
    private MenuItem VideoAgradecimentos;

    @FXML
    private MenuItem cadastrarProdutos;

    @FXML
    private MenuBar menuBarTelaPrin;

    @FXML
    private ImageView planofundo;

    @FXML
    private Pane paneVideo;

    @FXML
    private MediaView video;

    public void initialize() {
        
        cadastrarProdutos.setOnAction(event -> cadastrarProdutosAction());
        //proximo passo
        //VideoAgradecimentos.setOnAction(event -> reproduzirAgradecimentos());
    }

    private void cadastrarProdutosAction() {
        //"Cadastrar Produtos" é selecionado
        System.out.println("Cadastrar Produtos foi selecionado!");

        try {
            // Chamar o método startThirdStage da classe App
            App.startThirdStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
