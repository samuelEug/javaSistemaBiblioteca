package com.mycompany.sistemabiblioteca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class EmprestimoLivro extends JPanel {

    private JTextField campoNomeCliente;
    private JTextField campoCelularCliente;
    private JTextField campoEmailCliente;
    private JTextField campoCpfCliente;
    private JComboBox<String> comboBoxLivros;
    private JButton botaoEmprestimo;
    private JButton botaoVerEmprestimos;
    private JTable tabelaLivros;
    private DefaultTableModel modeloTabelaLivros;
    private JTable tabelaEmprestimos;
    private DefaultTableModel modeloTabelaEmprestimos;

    public EmprestimoLivro() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Configuração do painel de empréstimo
        JPanel painelEmprestimo = new JPanel();
        painelEmprestimo.setLayout(new GridBagLayout());
        GridBagConstraints gbcEmprestimo = new GridBagConstraints();
        gbcEmprestimo.insets = new Insets(10, 10, 10, 10);

        // Nome do Cliente
        gbcEmprestimo.gridx = 0;
        gbcEmprestimo.gridy = 0;
        gbcEmprestimo.anchor = GridBagConstraints.EAST;
        painelEmprestimo.add(new JLabel("Nome do Cliente:"), gbcEmprestimo);
        campoNomeCliente = new JTextField(20);
        gbcEmprestimo.gridx = 1;
        painelEmprestimo.add(campoNomeCliente, gbcEmprestimo);

        // Celular do Cliente
        gbcEmprestimo.gridx = 0;
        gbcEmprestimo.gridy = 1;
        gbcEmprestimo.anchor = GridBagConstraints.EAST;
        painelEmprestimo.add(new JLabel("Celular:"), gbcEmprestimo);
        campoCelularCliente = new JTextField(15);
        gbcEmprestimo.gridx = 1;
        painelEmprestimo.add(campoCelularCliente, gbcEmprestimo);

        // Email do Cliente
        gbcEmprestimo.gridx = 0;
        gbcEmprestimo.gridy = 2;
        gbcEmprestimo.anchor = GridBagConstraints.EAST;
        painelEmprestimo.add(new JLabel("E-mail:"), gbcEmprestimo);
        campoEmailCliente = new JTextField(20);
        gbcEmprestimo.gridx = 1;
        painelEmprestimo.add(campoEmailCliente, gbcEmprestimo);

        // CPF do Cliente
        gbcEmprestimo.gridx = 0;
        gbcEmprestimo.gridy = 3;
        gbcEmprestimo.anchor = GridBagConstraints.EAST;
        painelEmprestimo.add(new JLabel("CPF:"), gbcEmprestimo);
        campoCpfCliente = new JTextField(15);
        gbcEmprestimo.gridx = 1;
        painelEmprestimo.add(campoCpfCliente, gbcEmprestimo);

        // ComboBox para selecionar o livro
        gbcEmprestimo.gridx = 0;
        gbcEmprestimo.gridy = 4;
        gbcEmprestimo.anchor = GridBagConstraints.EAST;
        painelEmprestimo.add(new JLabel("Selecionar Livro:"), gbcEmprestimo);
        comboBoxLivros = new JComboBox<>();
        gbcEmprestimo.gridx = 1;
        painelEmprestimo.add(comboBoxLivros, gbcEmprestimo);

        // Botão Emprestar
        botaoEmprestimo = new JButton("Realizar Empréstimo");
        gbcEmprestimo.gridx = 0;
        gbcEmprestimo.gridy = 5;
        gbcEmprestimo.gridwidth = 2;
        gbcEmprestimo.fill = GridBagConstraints.HORIZONTAL;
        painelEmprestimo.add(botaoEmprestimo, gbcEmprestimo);

        // Adicionar botão para ver empréstimos
        botaoVerEmprestimos = new JButton("Ver Empréstimos");
        gbcEmprestimo.gridx = 0;
        gbcEmprestimo.gridy = 6;
        gbcEmprestimo.gridwidth = 2;
        gbcEmprestimo.fill = GridBagConstraints.HORIZONTAL;
        painelEmprestimo.add(botaoVerEmprestimos, gbcEmprestimo);

        // Dividir o painel em duas partes
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(painelEmprestimo, gbc);

        // Configuração da tabela de livros
        JPanel painelLivros = new JPanel();
        painelLivros.setLayout(new BorderLayout());

        modeloTabelaLivros = new DefaultTableModel(new Object[]{"ID", "Título", "Autor", "Ano", "Disponíveis"}, 0);
        tabelaLivros = new JTable(modeloTabelaLivros);
        JScrollPane scrollLivros = new JScrollPane(tabelaLivros);
        painelLivros.add(scrollLivros, BorderLayout.CENTER);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(painelLivros, gbc);

        // Carregar livros ao iniciar o painel
        carregarLivros();

        // Ação ao clicar no botão "Ver Empréstimos"
        botaoVerEmprestimos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirEmprestimos();
            }
        });

        // Ação ao clicar no botão de empréstimo
        botaoEmprestimo.addActionListener(e -> realizarEmprestimo());
    }

    // Método para carregar livros disponíveis no JComboBox e na tabela
    private void carregarLivros() {
        try (Connection conexao = ConexaoBanco.conexao()) {
            if (conexao == null) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT id, titulo, autor, ano_publicacao, numero_disponiveis FROM livros WHERE numero_disponiveis > 0";
            PreparedStatement stmt = conexao.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Limpar a ComboBox e a tabela
            comboBoxLivros.removeAllItems();
            modeloTabelaLivros.setRowCount(0);

            // Adicionar os livros à ComboBox e à tabela
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                int ano = rs.getInt("ano_publicacao");
                int disponiveis = rs.getInt("numero_disponiveis");

                comboBoxLivros.addItem(titulo + " (ID: " + id + ")");
                modeloTabelaLivros.addRow(new Object[]{id, titulo, autor, ano, disponiveis});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para realizar o empréstimo
private void realizarEmprestimo() {
    String nomeCliente = campoNomeCliente.getText().trim();
    String celularCliente = campoCelularCliente.getText().trim();
    String emailCliente = campoEmailCliente.getText().trim();
    String cpfCliente = campoCpfCliente.getText().trim();
    String livroSelecionado = (String) comboBoxLivros.getSelectedItem();

    // Validação de campos obrigatórios
    if (nomeCliente.isEmpty() || celularCliente.isEmpty() || emailCliente.isEmpty() || cpfCliente.isEmpty() || livroSelecionado == null || livroSelecionado.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Extrair o ID do livro selecionado
    int idLivro = -1;
    try {
        String[] livroParts = livroSelecionado.split(" \\(ID: ");
        idLivro = Integer.parseInt(livroParts[1].replace(")", ""));
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao processar a seleção do livro.", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Realizar a inserção do empréstimo no banco de dados
    try (Connection conexao = ConexaoBanco.conexao()) {
        if (conexao == null) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Preparar query para inserir o empréstimo
        String queryEmprestimo = "INSERT INTO emprestimo (nome_cliente, celular_cliente, email_cliente, cpf_cliente, id_livro) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conexao.prepareStatement(queryEmprestimo);
        stmt.setString(1, nomeCliente);
        stmt.setString(2, celularCliente);
        stmt.setString(3, emailCliente);
        stmt.setString(4, cpfCliente);
        stmt.setInt(5, idLivro);

        int linhasAfetadas = stmt.executeUpdate();

        if (linhasAfetadas > 0) {
            // Atualizar o número de livros disponíveis na tabela "livros"
            String queryAtualizarLivros = "UPDATE livros SET numero_disponiveis = numero_disponiveis - 1 WHERE id = ?";
            PreparedStatement stmtAtualizarLivros = conexao.prepareStatement(queryAtualizarLivros);
            stmtAtualizarLivros.setInt(1, idLivro);
            stmtAtualizarLivros.executeUpdate();

            JOptionPane.showMessageDialog(this, "Empréstimo realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarLivros(); // Recarregar livros disponíveis
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao realizar o empréstimo.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Erro ao realizar empréstimo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}


    // Método para exibir os empréstimos
    private void exibirEmprestimos() {
        JFrame emprestimosFrame = new JFrame("Empréstimos");
        modeloTabelaEmprestimos = new DefaultTableModel(new Object[]{"ID", "Nome Cliente", "Livro Emprestado", "Data Empréstimo"}, 0);
        tabelaEmprestimos = new JTable(modeloTabelaEmprestimos);
        JScrollPane scrollPane = new JScrollPane(tabelaEmprestimos);
        emprestimosFrame.add(scrollPane, BorderLayout.CENTER);

        try (Connection conexao = ConexaoBanco.conexao()) {
            if (conexao == null) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT e.id, e.nome_cliente, l.titulo, e.data_emprestimo FROM emprestimo e JOIN livros l ON e.id_livro = l.id";
            PreparedStatement stmt = conexao.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomeCliente = rs.getString("nome_cliente");
                String livroEmprestado = rs.getString("titulo");
                Date dataEmprestimo = rs.getDate("data_emprestimo");

                modeloTabelaEmprestimos.addRow(new Object[]{id, nomeCliente, livroEmprestado, dataEmprestimo});
            }

            emprestimosFrame.setSize(600, 400);
            emprestimosFrame.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar empréstimos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
