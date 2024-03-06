package com.ramirez.Teste.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditarDAO {

	public void atualizarProduto(Produto produto) {
		String sql = "UPDATE produtos SET nome = ?, descricao = ?, precocusto = ?, precovenda = ?, quantidade = ? WHERE codigodoproduto = ?";

		try (Connection conexao = ConnectionFactory.getConnection();
				PreparedStatement preparedStatement = conexao.prepareStatement(sql)) {
			conexao.setAutoCommit(false);

			preparedStatement.setString(1, produto.getNome());
			preparedStatement.setString(2, produto.getDescricao());
			preparedStatement.setDouble(3, produto.getPrecoCusto());
			preparedStatement.setDouble(4, produto.getPrecoVenda());
			preparedStatement.setInt(5, produto.getQuantidade());
			preparedStatement.setString(6, produto.getCodigo());

			preparedStatement.executeUpdate();

			conexao.commit();
			conexao.setAutoCommit(true);

			System.out.println("Produto atualizado no banco de dados: " + produto);
		} catch (SQLException e) {
			System.err.println("Erro ao executar a atualização no banco de dados.");
			e.printStackTrace();
		}
	}
}
