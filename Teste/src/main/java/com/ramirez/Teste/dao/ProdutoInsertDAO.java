package com.ramirez.Teste.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProdutoInsertDAO {
		
    public boolean adicionarProduto(String codigodoproduto, String nome, String descricao, double precoCusto, double precoVenda, int quantidade) {
        String sql = "INSERT INTO produtos (codigodoproduto, nome, descricao, precocusto, precovenda, quantidade) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conexao.prepareStatement(sql)
        ) {
            // Verificar campos vazios
            if (codigodoproduto.isEmpty() || nome.isEmpty() || descricao.isEmpty()) {
                return false;
            }

            preparedStatement.setString(1, codigodoproduto);
            preparedStatement.setString(2, nome);
            preparedStatement.setString(3, descricao);
            preparedStatement.setDouble(4, precoCusto);
            preparedStatement.setDouble(5, precoVenda);
            preparedStatement.setInt(6, quantidade);

            int linhasAfetadas = preparedStatement.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


