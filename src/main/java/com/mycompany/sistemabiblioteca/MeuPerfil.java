package com.mycompany.sistemabiblioteca;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MeuPerfil extends JPanel {

    private JTextField campoNomePerfil;
    private JTextField campoEmailPerfil;
    private JTextField campoCelularPerfil;
    private JTextField campoCpfPerfil;
    private JButton botaoSalvarPerfil;

    // Campos de edição
    private JTextField campoNomeEditar;
    private JTextField campoCelularEditar;
    private JTextField campoEmailEditar;
    private JTextField campoCpfEditar;

    // Painel de informações (referência para atualizar dinamicamente)
    private JPanel painelInformacoes;

    private String nomeUsuario; 

    public MeuPerfil(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;

        setLayout(new BorderLayout());

        // Painel com informações do usuário
        painelInformacoes = new JPanel(new GridLayout(4, 2, 10, 10));
        painelInformacoes.setBorder(BorderFactory.createTitledBorder("Informações do Usuário"));

        painelInformacoes.add(new JLabel("Nome:", JLabel.RIGHT));
        JLabel labelNome = new JLabel(nomeUsuario);
        labelNome.setFont(new Font("Arial", Font.BOLD, 14));
        painelInformacoes.add(labelNome);

        painelInformacoes.add(new JLabel("Celular:", JLabel.RIGHT));
        JLabel labelCelular = new JLabel("****-****");
        labelCelular.setFont(new Font("Arial", Font.BOLD, 14));
        painelInformacoes.add(labelCelular);

        painelInformacoes.add(new JLabel("E-mail:", JLabel.RIGHT));
        JLabel labelEmail = new JLabel("exemplo@dominio.com");
        labelEmail.setFont(new Font("Arial", Font.BOLD, 14));
        painelInformacoes.add(labelEmail);

        painelInformacoes.add(new JLabel("CPF:", JLabel.RIGHT));
        JLabel labelCpf = new JLabel("000.000.000-00");
        labelCpf.setFont(new Font("Arial", Font.BOLD, 14));
        painelInformacoes.add(labelCpf);

        // Painel para formulário de edição
        JPanel painelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Alterar Dados"));

        // Campos de edição
        painelFormulario.add(new JLabel("Nome:"));
        campoNomeEditar = new JTextField(20);
        campoNomeEditar.setText(nomeUsuario);
        painelFormulario.add(campoNomeEditar);

        painelFormulario.add(new JLabel("Celular:"));
        campoCelularEditar = new JTextField(15);
        painelFormulario.add(campoCelularEditar);

        painelFormulario.add(new JLabel("E-mail:"));
        campoEmailEditar = new JTextField(20);
        painelFormulario.add(campoEmailEditar);

        painelFormulario.add(new JLabel("CPF:"));
        campoCpfEditar = new JTextField(15);
        painelFormulario.add(campoCpfEditar);

        botaoSalvarPerfil = new JButton("Salvar Alterações");
        painelFormulario.add(new JLabel(""));
        painelFormulario.add(botaoSalvarPerfil);

        // Configura evento do botão para salvar alterações
        botaoSalvarPerfil.addActionListener(e -> salvarPerfil());

        // Divisão horizontal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelInformacoes, painelFormulario);
        splitPane.setDividerLocation(500);

        add(splitPane, BorderLayout.CENTER);
    }

    // Atualiza o painel de informações com os novos dados do usuário
    private void atualizarPainelInformacoes(String nome, String celular, String email, String cpf) {
        painelInformacoes.removeAll();

        painelInformacoes.add(new JLabel("Nome:", JLabel.RIGHT));
        JLabel labelNome = new JLabel(nome);
        labelNome.setFont(new Font("Arial", Font.BOLD, 14));
        painelInformacoes.add(labelNome);

        painelInformacoes.add(new JLabel("Celular:", JLabel.RIGHT));
        JLabel labelCelular = new JLabel(celular);
        labelCelular.setFont(new Font("Arial", Font.BOLD, 14));
        painelInformacoes.add(labelCelular);

        painelInformacoes.add(new JLabel("E-mail:", JLabel.RIGHT));
        JLabel labelEmail = new JLabel(email);
        labelEmail.setFont(new Font("Arial", Font.BOLD, 14));
        painelInformacoes.add(labelEmail);

        painelInformacoes.add(new JLabel("CPF:", JLabel.RIGHT));
        JLabel labelCpf = new JLabel(cpf);
        labelCpf.setFont(new Font("Arial", Font.BOLD, 14));
        painelInformacoes.add(labelCpf);

        painelInformacoes.revalidate();
        painelInformacoes.repaint();
    }

    // Método para salvar alterações no banco de dados
    public void salvarPerfil() {
        try {
            String novoNome = campoNomeEditar.getText();
            String novoCelular = campoCelularEditar.getText();
            String novoEmail = campoEmailEditar.getText();
            String novoCpf = campoCpfEditar.getText();

            Connection conexao = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/biblioteca", "postgres", "samuel"
            );

            String query = "UPDATE usuarios SET nome = ?, celular = ?, email = ?, cpf = ? WHERE nome = ?";
            PreparedStatement stmt = conexao.prepareStatement(query);
            stmt.setString(1, novoNome);
            stmt.setString(2, novoCelular);
            stmt.setString(3, novoEmail);
            stmt.setString(4, novoCpf);
            stmt.setString(5, nomeUsuario);

            int linhasAfetadas = stmt.executeUpdate();
            stmt.close();
            conexao.close();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarPainelInformacoes(novoNome, novoCelular, novoEmail, novoCpf);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar o perfil.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar o perfil.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
