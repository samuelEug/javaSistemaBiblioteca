package com.mycompany.sistemabiblioteca;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SistemaBiblioteca extends JFrame {

    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JButton botaoLogin;

    public SistemaBiblioteca() {
        // Configurações da janela de login
        setTitle("Login");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        JLabel logoLabel = new JLabel("LOGO");
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painel.add(logoLabel, gbc);

   
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(new JLabel("Usuário:"), gbc);

        campoUsuario = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        painel.add(campoUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painel.add(new JLabel("Senha:"), gbc);

        campoSenha = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        painel.add(campoSenha, gbc);

        // Adicionar botão de login
        botaoLogin = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(botaoLogin, gbc);

        JButton botaoCadastro = new JButton("Não possui conta?");
        gbc.gridy = 4;
        painel.add(botaoCadastro, gbc);

        botaoCadastro.addActionListener(e -> abrirTelaCadastro());


        add(painel, BorderLayout.CENTER);


        botaoLogin.addActionListener(e -> validarLogin());

        // Exibe a janela
        setVisible(true);
    }

    private void abrirTelaCadastro() {
        new telaCadastro(); 
    }

    private void validarLogin() {
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword()).trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Consulta ao banco de dados para verificar credenciais
        String sql = "SELECT permissao FROM usuarios WHERE nome = ? AND senha = ?";

        try (Connection conn = ConexaoBanco.conexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario);
            pstmt.setString(2, senha); // 

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String permissao = rs.getString("permissao");
                JOptionPane.showMessageDialog(this, "Login bem-sucedido!");

           
                if ("admin".equalsIgnoreCase(permissao)) {
                    new TelaAdmin(conn); // 
                } else {
                   
                    new TelaPrincipalUsuario(usuario); 
                }

                dispose(); 

            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new SistemaBiblioteca();
    }
}
