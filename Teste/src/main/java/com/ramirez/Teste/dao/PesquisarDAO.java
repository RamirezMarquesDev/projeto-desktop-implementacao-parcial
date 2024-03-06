package com.ramirez.Teste.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PesquisarDAO {

        public List<Produto> pesquisarProdutosPorNome(String termo) {
            List<Produto> resultados = new ArrayList<>();

            String sql = "SELECT codigodoproduto, nome, descricao, precocusto, precovenda, quantidade FROM produtos WHERE nome LIKE ?";

            try (Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement preparedStatement = conexao.prepareStatement(sql)
            ) {
                preparedStatement.setString(1, "%" + termo + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String codigo = resultSet.getString("codigodoproduto");
                        String nome = resultSet.getString("nome");
                        String descricao = resultSet.getString("descricao");
                        double precoCusto = resultSet.getDouble("precocusto");
                        double precoVenda = resultSet.getDouble("precovenda");
                        int quantidade = resultSet.getInt("quantidade");

                        Produto produto = new Produto(codigo, nome, descricao, precoCusto, precoVenda, quantidade);
                        resultados.add(produto);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return resultados;
        }
    }

