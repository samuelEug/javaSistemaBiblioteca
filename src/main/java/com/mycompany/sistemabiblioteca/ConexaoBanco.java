package com.mycompany.sistemabiblioteca;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;

import java.sql.ResultSet;

import java.sql.Statement;

import java.sql.PreparedStatement;

public class ConexaoBanco {

    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "samuel";

    public static Connection conexao() throws SQLException {
        try {
            // Verifique se o driver JDBC está carregado
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado.", e);
        }

    }
}
