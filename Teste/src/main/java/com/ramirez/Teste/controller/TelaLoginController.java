package com.ramirez.Teste.controller;

import java.io.IOException;

import com.ramirez.Teste.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class TelaLoginController {

	private String senhaOriginal;

	@FXML
	private Button btnentrar;

	@FXML
	private CheckBox checkbox;

	@FXML
	private Label labelsenha;

	@FXML
	private Label labelusuario;

	@FXML
	private ImageView logoview;

	@FXML
	private Pane pane;

	@FXML
	private Label passError;

	@FXML
	private PasswordField txtsenha;

	@FXML
	private TextField txtusuario;

	@FXML
	private Label userEror;

	@FXML
	public void initialize() {
		
		senhaOriginal = "ramirez123";
		
		// Configurar um ouvinte para a propriedade "selected" do CheckBox
		checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				senhaOriginal = txtsenha.getText();
				txtsenha.setPromptText(senhaOriginal);
				txtsenha.setText("");
			} else {
				txtsenha.setText(senhaOriginal);
				txtsenha.setPromptText("");
			}
		});

		// validar o campo de senha
		txtsenha.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.trim().isEmpty()) {
				// O texto da senha é válido
				passError.setText(""); // Limpar mensagem de erro, se houver
			} else {
				// O texto da senha não é válido
				if (!checkbox.isSelected()) {
					passError.setText("Senha não pode ser vazia.");
				}
			}
		});

		// validar o campo de usuário
		txtusuario.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.trim().isEmpty()) {
				// O texto do usuário é válido
				userEror.setText(""); // Limpar mensagem de erro, se houver
			} else {
				// O texto do usuário não é válido
				userEror.setText("Usuário não pode ser vazio.");
			}
		});

		// Adicionar ação ao botão "Entrar"
		btnentrar.setOnAction(event -> {
			try {
				handleBtnEntrar();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		// aceitar enter
		txtsenha.setOnKeyPressed(event -> {
		    if (event.getCode().equals(KeyCode.ENTER)) {
		        try {
		            handleBtnEntrar();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		});
	}

	private void handleBtnEntrar() throws IOException {
	    // Verificar se os campos são válidos antes de prosseguir
	    if (isInputValid()) {
	        // Checar as credenciais
	        String usuario = txtusuario.getText().trim();
	        String senhaDigitada = txtsenha.getText().trim();

	        // Comparar a senha digitada com a senha original, levando em consideração o estado do CheckBox
	        boolean senhasIguais = checkbox.isSelected() ? true : senhaDigitada.equals(senhaOriginal);

	        if (usuario.equals("ramirezmarques") && senhasIguais) {
	            // Credenciais corretas, navegar para a próxima tela
	        	App.setRoot("TelaPrincipal");
	        } else {
	            // Credenciais incorretas, exibir mensagem de erro
	            userEror.setText("Usuário ou senha incorretos.");
	            passError.setText("Usuário ou senha incorretos.");
	        }
	    }
	}

	private boolean isInputValid() {
		// Verificar se ambos os campos são válidos
		boolean usuarioValido = txtusuario.getText() != null && !txtusuario.getText().trim().isEmpty();
		boolean senhaValida = txtsenha.getText() != null && !txtsenha.getText().trim().isEmpty();

		// Exibir mensagens de erro, se necessário
		userEror.setText(usuarioValido ? "" : "O usuário não pode ser vazio.");
		passError.setText(senhaValida ? "" : "A senha não pode ser vazia.");

		return usuarioValido && (senhaValida || checkbox.isSelected());
	}
}
