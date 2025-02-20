package com.mycompany.sistemabiblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CadastroLivros extends JPanel {

    private JTextField campoTituloCadastro;
    private JTextField campoAutorCadastro;
    private JTextField campoAnoPublicacaoCadastro;
    private JTextField campoGeneroCadastro;
    private JTextField campoNumeroDisponiveisCadastro;
    private JTextArea campoSinopseCadastro;
    private JButton botaoCadastrar;
    private JTable tabelaLivros;

    public CadastroLivros() {
        setLayout(new BorderLayout());

        // Painel de Cadastro de Livro
        JPanel painelCadastro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campo Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        painelCadastro.add(new JLabel("Título:"), gbc);
        campoTituloCadastro = new JTextField(15);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelCadastro.add(campoTituloCadastro, gbc);

        // Campo Autor
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        painelCadastro.add(new JLabel("Autor:"), gbc);
        campoAutorCadastro = new JTextField(15);
        gbc.gridx = 1;
        painelCadastro.add(campoAutorCadastro, gbc);

        // Campo Ano de Publicação
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        painelCadastro.add(new JLabel("Ano de Publicação:"), gbc);
        campoAnoPublicacaoCadastro = new JTextField(5);
        gbc.gridx = 1;
        painelCadastro.add(campoAnoPublicacaoCadastro, gbc);

        campoAnoPublicacaoCadastro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c)) {
                    evt.consume();
                }
            }
        });

        // Campo Sinopse
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        painelCadastro.add(new JLabel("Sinopse:"), gbc);
        campoSinopseCadastro = new JTextArea(5, 15);
        JScrollPane scrollSinopse = new JScrollPane(campoSinopseCadastro);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painelCadastro.add(scrollSinopse, gbc);

        // Campo Gênero
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        painelCadastro.add(new JLabel("Gênero:"), gbc);
        campoGeneroCadastro = new JTextField(15);
        gbc.gridx = 1;
        painelCadastro.add(campoGeneroCadastro, gbc);

        // Campo Número de Disponíveis
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        painelCadastro.add(new JLabel("Número de Disponíveis:"), gbc);
        campoNumeroDisponiveisCadastro = new JTextField(5);
        gbc.gridx = 1;
        painelCadastro.add(campoNumeroDisponiveisCadastro, gbc);

        campoNumeroDisponiveisCadastro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c)) {
                    evt.consume();
                }
            }
        });

        // Botão Cadastrar
        botaoCadastrar = new JButton("Cadastrar Livro");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelCadastro.add(botaoCadastrar, gbc);

        botaoCadastrar.addActionListener(e -> cadastrarLivro());

        // Adiciona painel de cadastro ao painel principal
        add(painelCadastro, BorderLayout.WEST);

        // Painel de Livros
        JPanel painelListaLivros = new JPanel(new BorderLayout());

        // Tabela de Livros
        tabelaLivros = new JTable();
        JScrollPane scrollTabela = new JScrollPane(tabelaLivros);
        painelListaLivros.add(scrollTabela, BorderLayout.CENTER);

        carregarLivros();

        // Adiciona painel de livros à direita
        add(painelListaLivros, BorderLayout.CENTER);
    }

    private void cadastrarLivro() {
        String titulo = campoTituloCadastro.getText().trim();
        String autor = campoAutorCadastro.getText().trim();
        String anoPublicacao = campoAnoPublicacaoCadastro.getText().trim();
        String sinopse = campoSinopseCadastro.getText().trim();
        String genero = campoGeneroCadastro.getText().trim();
        String numeroDisponiveis = campoNumeroDisponiveisCadastro.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty() || anoPublicacao.isEmpty() || sinopse.isEmpty() || genero.isEmpty() || numeroDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int anoPublicacaoInt = Integer.parseInt(anoPublicacao);
            int numeroDisponiveisInt = Integer.parseInt(numeroDisponiveis);

            try (Connection conexao = ConexaoBanco.conexao()) {
                if (conexao == null) {
                    JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String query = "INSERT INTO livros (titulo, autor, ano_publicacao, sinopse, genero, numero_disponiveis) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(query);
                stmt.setString(1, titulo);
                stmt.setString(2, autor);
                stmt.setInt(3, anoPublicacaoInt);
                stmt.setString(4, sinopse);
                stmt.setString(5, genero);
                stmt.setInt(6, numeroDisponiveisInt);

                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
                    carregarLivros();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao cadastrar livro. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

                campoTituloCadastro.setText("");
                campoAutorCadastro.setText("");
                campoAnoPublicacaoCadastro.setText("");
                campoSinopseCadastro.setText("");
                campoGeneroCadastro.setText("");
                campoNumeroDisponiveisCadastro.setText("");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar livro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um ano válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    void carregarLivros() {
        try (Connection conexao = ConexaoBanco.conexao()) {
            String query = "SELECT * FROM livros";
            PreparedStatement stmt = conexao.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Título", "Autor", "Ano", "Disponíveis", "Excluir"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 5; // Apenas a coluna "Excluir" será editável
                }
            };

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getInt("ano_publicacao"),
                    rs.getInt("numero_disponiveis"),
                    "Excluir"
                });
            }

            tabelaLivros.setModel(modelo);

            tabelaLivros.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
            tabelaLivros.getColumn("Excluir").setCellEditor(new ButtonEditor(new JCheckBox()));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cadastro de Livros");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.add(new CadastroLivros());
        frame.setVisible(true);
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setText("Excluir");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private int currentRow;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton("Excluir");
        button.addActionListener(e -> excluirLivro());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.currentRow = row;
        return button;
    }

    private void excluirLivro() {
        int id = (int) table.getValueAt(currentRow, 0);
        int confirm = JOptionPane.showConfirmDialog(button, "Tem certeza que deseja excluir o livro ID: " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conexao = ConexaoBanco.conexao()) {
                String query = "DELETE FROM livros WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(query);
                stmt.setInt(1, id);

                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    JOptionPane.showMessageDialog(button, "Livro excluído com sucesso!");
                    ((DefaultTableModel) table.getModel()).removeRow(currentRow);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(button, "Erro ao excluir livro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        fireEditingStopped();
    }
}
