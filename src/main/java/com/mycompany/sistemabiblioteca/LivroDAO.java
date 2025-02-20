package com.mycompany.sistemabiblioteca;

/**
 *
 * @author 7bluz
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LivroDAO {
    // Método para inserir um novo livro
    public boolean inserirLivro(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, quantidade_paginas, sinopse) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.conexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, livro.getTitulo());
            pstmt.setString(2, livro.getAutor());
            pstmt.setInt(3, livro.getQuantidadePaginas());
            pstmt.setString(4, livro.getSinopse());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar um livro por ID
    public Livro buscarLivroPorId(int id) {
        String sql = "SELECT * FROM livros WHERE id = ?";
        Livro livro = null;

        try (Connection conn = ConexaoBanco.conexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setQuantidadePaginas(rs.getInt("quantidade_paginas"));
                livro.setSinopse(rs.getString("sinopse"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livro;
    }
}
