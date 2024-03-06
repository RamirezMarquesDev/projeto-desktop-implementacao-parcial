package com.ramirez.Teste.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExcluirDAO {

    public void excluirProduto(String codigo) {
        String sql = "DELETE FROM produtos WHERE codigodoproduto = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conexao.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, codigo);
            preparedStatement.executeUpdate();
            System.out.println("Produto excluído: Código = " + codigo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
