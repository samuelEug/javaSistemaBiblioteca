package com.mycompany.sistemabiblioteca;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BancoDeDados {
    
        public static Connection conectar() {
        try {
            // Substitua pelos detalhes corretos do seu banco
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/nome_do_banco", "usuario", "senha");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para cadastrar um usuário na tabela 'usuarios'
    public static boolean cadastrarUsuario(String nome, String email, String senha, String permissao) {
        String URL = "jdbc:postgresql://localhost:5432/biblioteca";
        String USER = "postgres"; // usuário
        String PASSWORD = "samuel"; // senha
        Connection conn = null;
        boolean sucesso = false;

        try {
            // Estabelecendo a conexão
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn != null) {
                // Preparando a query de inserção
                String INSERIR_USUARIO = "INSERT INTO usuarios (nome, email, senha, permissao) VALUES (?, ?, ?, ?)";
                try {
                    PreparedStatement pstmt = conn.prepareStatement(INSERIR_USUARIO);
                    pstmt.setString(1, nome);  // Campo 'usuario'
                    pstmt.setString(2, email); // Campo 'email'
                    pstmt.setString(3, senha);    // Campo 'senha'
                    pstmt.setString(4, permissao); // Campo 'permissao'
                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        sucesso = true;
                        System.out.println("Usuário cadastrado com sucesso!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Falha ao conectar ao banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Deu Algum Problema");
            e.printStackTrace();
        } finally {
            try {
                // Fechar a conexão se ela foi aberta
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Deu Algum Problema para Fechar conexao");
                ex.printStackTrace();
            }
        }
        
        return sucesso;
    }
    
    public static boolean verificarLogin(String nome, String senha) {
        Connection conexao = conectar();
        if (conexao == null) {
            return false;
        }

        String sql = "SELECT * FROM usuarios WHERE nome = ? AND senha = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, senha);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;  // Login bem-sucedido
            } else {
                return false; // Login falhou
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean cadastrarLivro(String nomeLivro, String autor, int paginas, String sinopse) {
        // Usando o método 'conectar()' para estabelecer a conexão
        Connection conexao = conectar();
        
        if (conexao == null) {
            System.out.println("Erro ao conectar ao banco de dados.");
            return false;
        }

        // Consulta SQL para inserir um novo livro
        String sql = "INSERT INTO livros (nome, autor, paginas, sinopse) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, nomeLivro);
            ps.setString(2, autor);
            ps.setInt(3, paginas);
            ps.setString(4, sinopse);

            // Executa a inserção no banco de dados
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                System.out.println("Livro cadastrado com sucesso!");
                return true; // Cadastro bem-sucedido
            } else {
                System.out.println("Erro ao cadastrar o livro.");
                return false; // Falha no cadastro
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                // Fechar a conexão após o uso
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para buscar livro por nome
    public static Livro buscarLivroPorNome(String nomeLivro) {
        Connection conexao = conectar();

        if (conexao == null) {
            System.out.println("Erro ao conectar ao banco de dados.");
            return null;
        }

        // Consulta SQL para buscar livro pelo nome
        String sql = "SELECT * FROM livros WHERE nome = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, nomeLivro);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Criar um objeto Livro com os dados retornados
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setQuantidadePaginas(rs.getInt("quantidade de paginas"));
                livro.setSinopse(rs.getString("sinopse"));
                return livro;
            } else {
                return null; // Livro não encontrado
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


