package com.ramirez.Teste.controller;

import java.util.List;
import java.util.Optional;

import com.ramirez.Teste.dao.EditarDAO;
import com.ramirez.Teste.dao.ExcluirDAO;
import com.ramirez.Teste.dao.PesquisarDAO;
import com.ramirez.Teste.dao.Produto;
import com.ramirez.Teste.dao.ProdutoInsertDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class TelaCadastroProdutosController {

	@FXML
	private Label desclabel;

	@FXML
	private Label codlabel;

	@FXML
	private Label labelretornoPesquisa;

	@FXML
	private Label titulolabel;

	@FXML
	private Label codigoerrorlabel;

	@FXML
	private Label descerrolabel;

	@FXML
	private Label nomeerrolabel;

	@FXML
	private Label precoCerrolabel;

	@FXML
	private Label precoVerrolabel;

	@FXML
	private Label qtderrolabel;

	@FXML
	private Label quantidadelabel;

	@FXML
	private Label nomeplabel;

	@FXML
	private Label precovlabel;

	@FXML
	private Label pesquisalabel;

	@FXML
	private Label precoclabel;

	@FXML
	private TextField quantidadetxtf;

	@FXML
	private TextField codtxtf;

	@FXML
	private TextField desctxtf;

	@FXML
	private TextField nometxtf;

	@FXML
	private TextField pesqtxtf;

	@FXML
	private TextField precoctxtf;

	@FXML
	private TextField precovtxtf;

	@FXML
	private Button editarbotao;

	@FXML
	private Button excluirbotao;

	@FXML
	private Button pesqbotao;

	@FXML
	private Button salvarbotao;

	@FXML
	private Pane paneCadastrarPeoduto;

	@FXML
	private ListView<Produto> listapesquisaview;

	private ObservableList<Produto> resultadosObservableList = FXCollections.observableArrayList();

	private Produto produtoEmEdicao;

	private final int MAX_LENGTH = 5;
	private final String FORMATO_DOUBLE = "(\\d{0,5}([\\.,]\\d{0,2})?)?";

	@FXML
	private void initialize() {

		// Desabilitar os botões de editar e excluir
		editarbotao.setDisable(true);
		excluirbotao.setDisable(true);
		//método initialize
		salvarbotao.setOnAction(event -> salvarProduto());
		pesqbotao.setOnAction(event -> pesquisarProduto());
		// Habilitar os botões de editar e excluir
		editarbotao.setOnAction(event -> editarProduto());
		excluirbotao.setOnAction(event -> excluirProduto());
		configurarFiltroTecla();
		// limitar campos
		configurarLimitesCamposTexto();

		// aqui eu arrumo o problema do campo com . ou ,
		configurarCampoDouble(precoctxtf);
		configurarCampoDouble(precovtxtf);

		// clicar na lista
		listapesquisaview.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				editarProdutoSelecionado();
				editarbotao.setDisable(false);
				excluirbotao.setDisable(false);
			}
		});
		// Definir uma fábrica de células personalizada para a ListView
		listapesquisaview.setCellFactory(param -> new ListCell<Produto>() {
			@Override
			protected void updateItem(Produto produto, boolean empty) {
				super.updateItem(produto, empty);
				if (empty || produto == null) {
					setText(null);
				} else {
					setText(String.format(
							"Código: %s, Nome: %s, Descrição: %s, Custo: %.2f, Valor de Venda: %.2f, Quantidade: %d",
							produto.getCodigo(), produto.getNome(), produto.getDescricao(), produto.getPrecoCusto(),
							produto.getPrecoVenda(), produto.getQuantidade()));
				}
			}
		});
		// Configurar limite de caracteres e permitir apenas números para quantidadetxtf
		quantidadetxtf.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				// Se o novo valor não contiver apenas dígitos, substituir pelo valor anterior
				quantidadetxtf.setText(oldValue);
			}

			// Verificar o comprimento do texto
			if (newValue.length() > MAX_LENGTH) {
				quantidadetxtf.setText(oldValue);
				// Exibir mensagem de erro
				qtderrolabel.setText("Tamanho máximo permitido: " + MAX_LENGTH + " caracteres");
			} else {
				// Limpar a mensagem de erro
				qtderrolabel.setText("");
			}
		});
	}

	// problema do campo com virgula e ponto
	private void configurarCampoDouble(TextField textField) {
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches(FORMATO_DOUBLE)) {
				textField.setText(oldValue);
			}
		});
	}

	// aqui cofigura esc
	private void configurarFiltroTecla() {
		paneCadastrarPeoduto.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				editarbotao.setDisable(true);
				excluirbotao.setDisable(true);
				// Chame o método para limpar os campos
				limparCamposEdicao();
			}
		});
	}

	private void editarProdutoSelecionado() {
		Produto produtoSelecionado = listapesquisaview.getSelectionModel().getSelectedItem();
		if (produtoSelecionado != null) {
			// Preencher os campos de edição com os dados do item selecionado
			codtxtf.setText(produtoSelecionado.getCodigo());
			nometxtf.setText(produtoSelecionado.getNome());
			desctxtf.setText(produtoSelecionado.getDescricao());
			precoctxtf.setText(String.valueOf(produtoSelecionado.getPrecoCusto()));
			precovtxtf.setText(String.valueOf(produtoSelecionado.getPrecoVenda()));
			quantidadetxtf.setText(String.valueOf(produtoSelecionado.getQuantidade()));

			// Armazenar o produto em edição
			produtoEmEdicao = produtoSelecionado;

			// Desabilitar edição do campo de código
			codtxtf.setEditable(false);

			// Desabilitar os botões apropriados
			salvarbotao.setDisable(true);
			// excluirbotao.setDisable(true);
			pesqbotao.setDisable(false);
		}
	}

	private void editarProduto() {
		if (produtoEmEdicao != null) {

			codigoerrorlabel.setText("");
			descerrolabel.setText("");
			nomeerrolabel.setText("");
			precoCerrolabel.setText("");
			precoVerrolabel.setText("");
			qtderrolabel.setText("");

			// Verificar campos vazios antes de obter os dados
			if (nomeEdicaoVazio()) {
				nomeerrolabel.setText("! Campo não pode ser vazio.");
				System.err.println("Nome de edição vazio. Preencha o campo.");
				return;
			}

			if (descricaoEdicaoVazia()) {
				descerrolabel.setText("! Campo não pode ser vazio.");
				System.err.println("Descrição de edição vazia. Preencha o campo.");
				return;
			}

			if (precoCustoEdicaoVazio()) {
				precoCerrolabel.setText("! Campo não pode ser vazio.");
				System.err.println("Preço de custo de edição vazio. Preencha o campo.");
				return;
			}

			if (precoVendaEdicaoVazio()) {
				precoVerrolabel.setText("! Campo não pode ser vazio.");
				System.err.println("Preço de venda de edição vazio. Preencha o campo.");
				return;
			}

			if (quantidadeEdicaoVazia()) {
				qtderrolabel.setText("! Campo não pode ser vazio.");
				System.err.println("Quantidade de edição vazia. Preencha o campo.");
				return;
			}
			// Obter os dados dos campos de edição
			String codigo = codtxtf.getText();
			String nome = nometxtf.getText();
			String descricao = desctxtf.getText();
			double precoCusto = converterParaDouble(precoctxtf.getText());
			double precoVenda = converterParaDouble(precovtxtf.getText());
			int quantidade = Integer.parseInt(quantidadetxtf.getText());

			// Atualizar os campos do produto em edição
			produtoEmEdicao.setCodigo(codigo);
			produtoEmEdicao.setNome(nome);
			produtoEmEdicao.setDescricao(descricao);
			produtoEmEdicao.setPrecoCusto(precoCusto);
			produtoEmEdicao.setPrecoVenda(precoVenda);
			produtoEmEdicao.setQuantidade(quantidade);

			// Limpar os campos de edição
			limparCamposEdicao();

			// Chamar método na classe EditarDAO para atualizar no banco de dados
			EditarDAO editarDAO = new EditarDAO();
			editarDAO.atualizarProduto(produtoEmEdicao);

			// Exibir as informações na interface
			exibirInformacoes(produtoEmEdicao);

			// Limpar a referência ao produto em edição
			produtoEmEdicao = null;

			// Desabilitar os botões apropriados
			salvarbotao.setDisable(true);
			// excluirbotao.setDisable(false);
			pesqbotao.setDisable(false);
			// Desabilitar edição do campo de código
			codtxtf.setEditable(true);

			// Limpar os campos de edição
			limparCamposEdicao();
			salvarbotao.setDisable(false);
			pesqbotao.setDisable(false);
			excluirbotao.setDisable(true);
			editarbotao.setDisable(true);
		}
	}

	private boolean nomeEdicaoVazio() {
		return nometxtf.getText().isEmpty();
	}

	private boolean descricaoEdicaoVazia() {
		return desctxtf.getText().isEmpty();
	}

	private boolean precoCustoEdicaoVazio() {
		return precoctxtf.getText().isEmpty();
	}

	private boolean precoVendaEdicaoVazio() {
		return precovtxtf.getText().isEmpty();
	}

	private boolean quantidadeEdicaoVazia() {
		return quantidadetxtf.getText().isEmpty();
	}

	private void exibirInformacoes(Produto produto) {
		codtxtf.setText(produto.getCodigo());
		nometxtf.setText(produto.getNome());
		desctxtf.setText(produto.getDescricao());
		precoctxtf.setText(String.valueOf(produto.getPrecoCusto()));
		precovtxtf.setText(String.valueOf(produto.getPrecoVenda()));
		quantidadetxtf.setText(String.valueOf(produto.getQuantidade()));
	}

	private void limparCamposEdicao() {
		// Limpar os campos de edição
		codtxtf.clear();
		nometxtf.clear();
		desctxtf.clear();
		precoctxtf.clear();
		precovtxtf.clear();
		quantidadetxtf.clear();
		pesqtxtf.clear();
		resultadosObservableList.clear();
	}

	private void excluirProduto() {
		// Criar um layout GridPane para a caixa de diálogo
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		// Adicionar elementos à GridPane
		gridPane.add(new Label("Deseja realmente excluir esse produto?"), 0, 0);
		gridPane.add(new Label("Essa ação é irreversível."), 0, 1);

		// Criar caixa de diálogo de confirmação com a GridPane personalizada
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmação de Exclusão");
		alert.getDialogPane().setContent(gridPane);

		// Adicionar botões à caixa de diálogo
		ButtonType confirmarButton = new ButtonType("Confirmar");
		ButtonType cancelarButton = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(confirmarButton, cancelarButton);

		// Aguardar resposta do usuário
		Optional<ButtonType> result = alert.showAndWait();

		// Se o usuário confirmar, excluir o produto
		if (result.isPresent() && result.get() == confirmarButton) {
			ExcluirDAO excluirDAO = new ExcluirDAO();
			excluirDAO.excluirProduto(produtoEmEdicao.getCodigo());

			// Limpar os campos de edição
			limparCamposEdicao();

			// Desabilitar os botões apropriados
			salvarbotao.setDisable(false);
			excluirbotao.setDisable(true);
			pesqbotao.setDisable(false);
			editarbotao.setDisable(true);
		} else {
			// Caso o usuário clique em Cancelar, limpar os campos
			limparCamposEdicao();
			salvarbotao.setDisable(false);
			excluirbotao.setDisable(true);
			pesqbotao.setDisable(false);
			editarbotao.setDisable(true);
		}
	}

	@FXML
	private void pesquisarProduto() {
		// obter o nome para pesquisa
		String nomePesquisa = pesqtxtf.getText();

		PesquisarDAO pesquisaDAO = new PesquisarDAO();
		List<Produto> resultados = pesquisaDAO.pesquisarProdutosPorNome(nomePesquisa);

		// Limpar a lista antes de adicionar novos resultados
		resultadosObservableList.clear();

		// Adicionar os resultados à lista observável
		resultadosObservableList.addAll(resultados);

		// Definir a lista observável como a fonte de itens da tabela
		listapesquisaview.setItems(resultadosObservableList);

		// Exibir os resultados na lista
		exibirResultadosNaLista(resultados);
	}

	private void exibirResultadosNaLista(List<Produto> resultados) {
		// Limpar a lista
		listapesquisaview.getItems().clear();

		// Adicionar os resultados à lista
		resultadosObservableList.addAll(resultados);

		// Definir a lista observável como a fonte de itens da lista
		listapesquisaview.setItems(resultadosObservableList);
	}

	@FXML
	private void salvarProduto() {
		// Limpar mensagens de erro anteriores
		codigoerrorlabel.setText("");
		descerrolabel.setText("");
		nomeerrolabel.setText("");
		precoCerrolabel.setText("");
		precoVerrolabel.setText("");
		qtderrolabel.setText("");

		// Verificar campos vazios
		if (nometxtf.getText().isEmpty()) {
			nomeerrolabel.setText("! Campo não pode ser vazio.");
			return;
		}
		if (desctxtf.getText().isEmpty()) {
			descerrolabel.setText("! Campo não pode ser vazio.");
			return;
		}
		if (codtxtf.getText().isEmpty()) {
			codigoerrorlabel.setText("! Campo não pode ser vazio.");
			return;
		}
		if (precoctxtf.getText().isEmpty()) {
			precoCerrolabel.setText("! Campo não pode ser vazio.");
			return;
		}
		if (precovtxtf.getText().isEmpty()) {
			precoVerrolabel.setText("! Campo não pode ser vazio.");
			return;
		}
		if (quantidadetxtf.getText().isEmpty()) {
			qtderrolabel.setText("! Campo não pode ser vazio.");
			return;
		}

		// Obter valores dos campos
		String codigoProduto = codtxtf.getText();
		String nomeProduto = nometxtf.getText();
		String descricaoProduto = desctxtf.getText();
		double precoCusto = converterParaDouble(precoctxtf.getText());
		double precoVenda = converterParaDouble(precovtxtf.getText());
		int quantidade = Integer.parseInt(quantidadetxtf.getText());

		// Chamar método para inserir o produto
		ProdutoInsertDAO produtoDAO = new ProdutoInsertDAO();
		boolean sucesso = produtoDAO.adicionarProduto(codigoProduto, nomeProduto, descricaoProduto, precoCusto,
				precoVenda, quantidade);

		if (sucesso) {
			System.out.println("Produto adicionado com sucesso!");
			// Limpar os campos após o sucesso
			codtxtf.clear();
			nometxtf.clear();
			desctxtf.clear();
			precoctxtf.clear();
			precovtxtf.clear();
			quantidadetxtf.clear();
		} else {
			System.err.println("Falha ao adicionar o produto!");
		}
	}

	private double converterParaDouble(String valor) {
		// Substituir ',' por '.' e converter para double
		return Double.parseDouble(valor.replace(',', '.'));
	}

	private void configurarLimitesCamposTexto() {
		// Configurar limite de caracteres para o campo codtxtf
		codtxtf.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 20) {
				codtxtf.setText(oldValue);
				codigoerrorlabel.setText("Tamanho máximo permitido: 20 caracteres");
			} else {
				codigoerrorlabel.setText(""); // Limpar a mensagem de erro
			}
		});

		// Configurar limite de caracteres para o campo nometxtf
		nometxtf.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 50) {
				nometxtf.setText(oldValue);
				nomeerrolabel.setText("Tamanho máximo permitido: 50 caracteres");
			} else {
				nomeerrolabel.setText(""); // Limpar a mensagem de erro
			}
		});
	}
}
