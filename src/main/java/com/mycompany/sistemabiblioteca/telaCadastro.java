package com.mycompany.sistemabiblioteca;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class telaCadastro extends JFrame {

    private JTextField campoUsuario;
    private JTextField campoEmail; // Novo campo para email
    private JPasswordField campoSenha;
    private JButton botaoCadastrar;

    public telaCadastro() {
        setTitle("Cadastro de Usuário");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campo de Usuário
        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(new JLabel("Usuário:"), gbc);

        campoUsuario = new JTextField(15);
        gbc.gridx = 1;
        painel.add(campoUsuario, gbc);

        // Campo de Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(new JLabel("Email:"), gbc);  // Label para email

        campoEmail = new JTextField(15);  // Campo para email
        gbc.gridx = 1;
        painel.add(campoEmail, gbc);

        // Campo de Senha
        gbc.gridx = 0;
        gbc.gridy = 2;
        painel.add(new JLabel("Senha:"), gbc);

        campoSenha = new JPasswordField(15);
        gbc.gridx = 1;
        painel.add(campoSenha, gbc);

        // Botão de Cadastrar
        botaoCadastrar = new JButton("Cadastrar");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        painel.add(botaoCadastrar, gbc);

        botaoCadastrar.addActionListener(e -> cadastrarUsuario());

        add(painel);
        setVisible(true);
    }

    private void cadastrarUsuario() {
        String usuario = campoUsuario.getText().trim();
        String email = campoEmail.getText().trim(); // Obtendo o email
        String senha = new String(campoSenha.getPassword()).trim();

        // Verificar se os campos estão vazios
        if (usuario.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Conectar ao banco de dados e salvar o novo usuário
        try (Connection conexao = ConexaoBanco.conexao()) {
            // Consulta SQL modificada para incluir o campo email e a permissão (default 'usuario')
            String query = "INSERT INTO usuarios (nome, email, senha, permissao) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(query);

            // Definir os parâmetros para a consulta
            stmt.setString(1, usuario);
            stmt.setString(2, email);  // Definindo o email
            stmt.setString(3, senha);  // Definindo a senha
            stmt.setString(4, "usuario");  // Definindo a permissão como "usuario" por padrão

            // Executar a consulta
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");

            // Fechar a tela de cadastro
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
