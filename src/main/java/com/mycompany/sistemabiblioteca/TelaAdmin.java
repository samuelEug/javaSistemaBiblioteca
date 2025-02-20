package com.mycompany.sistemabiblioteca;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class TelaAdmin extends JFrame {

    private JPanel painelDireito;
    private Connection conexao;

    public TelaAdmin(Connection conexao) {
        this.conexao = conexao;
        setTitle("Painel Admin");
        setSize(1600, 1020);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

       
        JPanel painelEsquerdo = new JPanel();
        painelEsquerdo.setLayout(new GridLayout(6, 1, 10, 10));
        painelEsquerdo.setPreferredSize(new Dimension(200, 0));


        JButton botaoLogoff = new JButton("Logoff");
        JButton botaoEditarUsuario = new JButton("Editar Usuário");
        JButton botaoVerLivros = new JButton("Ver Empréstimos");
        JButton botaoCadastrarLivro = new JButton("Cadastrar Livro");


        painelEsquerdo.add(botaoLogoff);
        painelEsquerdo.add(botaoEditarUsuario);
        painelEsquerdo.add(botaoVerLivros);
        painelEsquerdo.add(botaoCadastrarLivro);

     
        painelDireito = new JPanel(new BorderLayout());
        JLabel mensagemInicial = new JLabel("Bem-vindo, Admin!", SwingConstants.CENTER);
        mensagemInicial.setFont(new Font("Arial", Font.BOLD, 24));
        painelDireito.add(mensagemInicial, BorderLayout.CENTER);

   
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0);
        add(splitPane);

  
        botaoLogoff.addActionListener(e -> logoff());
        botaoEditarUsuario.addActionListener(e -> trocarTela(new TelaEditarUsuario()));
        botaoVerLivros.addActionListener(e -> trocarTela(new EmprestimoLivro()));
        botaoCadastrarLivro.addActionListener(e -> trocarTela(new CadastroLivros()));

        setVisible(true);
    }

    private void trocarTela(JPanel novaTela) {
        painelDireito.removeAll();
        painelDireito.add(novaTela, BorderLayout.CENTER);
        painelDireito.revalidate();
        painelDireito.repaint();
    }

    private void logoff() {
        dispose();
        new SistemaBiblioteca();
    }

    // Tela para editar usuários
    static class TelaEditarUsuario extends JPanel {
        private JTable tabelaUsuarios;

        public TelaEditarUsuario() {
            setLayout(new BorderLayout());

     
            Vector<String> colunas = new Vector<>();
            colunas.add("ID");
            colunas.add("Nome");
            colunas.add("Email");
            colunas.add("Permissões");
            colunas.add("Ação");

            Vector<Vector<Object>> dados = new Vector<>();

            try (Connection conexao = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/biblioteca", "postgres", "samuel")) {
                String sql = "SELECT id, nome, email, permissao FROM usuarios";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Vector<Object> linha = new Vector<>();
                    linha.add(rs.getInt("id"));
                    linha.add(rs.getString("nome"));
                    linha.add(rs.getString("email"));
                    linha.add(rs.getString("permissao"));
                    linha.add("Alterar | Excluir");
                    dados.add(linha);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            DefaultTableModel model = new DefaultTableModel(dados, colunas);
            tabelaUsuarios = new JTable(model);
            tabelaUsuarios.setRowHeight(40);

            tabelaUsuarios.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
            tabelaUsuarios.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

            JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
            add(scrollPane, BorderLayout.CENTER);
        }

        static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
            protected JButton button;
            private String label;
            private JTable table;
            private int row;

            public ButtonEditor(JCheckBox checkBox) {
                button = new JButton();
                button.setOpaque(true);
                button.addActionListener(e -> onButtonClick());
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                this.table = table;
                this.row = row;
                label = value == null ? "" : value.toString();
                button.setText(label);
                return button;
            }

            @Override
            public Object getCellEditorValue() {
                return label;
            }

            private void onButtonClick() {
                int userId = (int) table.getValueAt(row, 0);

                if (label.contains("Alterar")) {
                    new TelaAlterarUsuario(userId);
                } else if (label.contains("Excluir")) {
                    excluirUsuario(userId);
                }
            }

            private void excluirUsuario(int userId) {
                try (Connection conexao = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/biblioteca", "postgres", "samuel")) {
                    String sql = "DELETE FROM usuarios WHERE id = ?";
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setInt(1, userId);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Usuário excluído com sucesso!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        static class ButtonRenderer extends JButton implements TableCellRenderer {
            public ButtonRenderer() {
                setText("Ação");
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setText(value == null ? "" : value.toString());
                return this;
            }
        }
    }

static class TelaAlterarUsuario extends JFrame {
    private int userId;
    private JTextField campoNome;
    private JTextField campoEmail;
    private JComboBox<String> comboPermissao;

    public TelaAlterarUsuario(int userId) {
        this.userId = userId;

        setTitle("Alterar Usuário");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new GridLayout(5, 2, 10, 10)); // Ajuste no layout para 5 linhas

        campoNome = new JTextField();
        campoEmail = new JTextField();
        comboPermissao = new JComboBox<>(new String[]{"admin", "usuario"});

        // Buscar os dados do usuário para preencher os campos
        carregarDadosUsuario();

        // Botões
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCancelar = new JButton("Cancelar");
        JButton botaoDeletar = new JButton("Deletar Usuário");

        botaoSalvar.addActionListener(e -> salvarAlteracoes());
        botaoCancelar.addActionListener(e -> dispose()); // Fecha a janela sem salvar
        botaoDeletar.addActionListener(e -> deletarUsuario());

        // Adicionar componentes ao painel
        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("Email:"));
        painel.add(campoEmail);
        painel.add(new JLabel("Permissão:"));
        painel.add(comboPermissao);
        painel.add(botaoSalvar);
        painel.add(botaoCancelar);
        painel.add(new JLabel()); // Espaço vazio para layout
        painel.add(botaoDeletar);

        add(painel);
        setVisible(true);
    }


    private void carregarDadosUsuario() {
        try (Connection conexao = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/biblioteca", "postgres", "samuel")) {
            String sql = "SELECT nome, email, permissao FROM usuarios WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                campoNome.setText(rs.getString("nome"));
                campoEmail.setText(rs.getString("email"));
                comboPermissao.setSelectedItem(rs.getString("permissao"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void salvarAlteracoes() {
        try (Connection conexao = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/biblioteca", "postgres", "samuel")) {
            String sql = "UPDATE usuarios SET nome = ?, email = ?, permissao = ? WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, campoNome.getText());
            stmt.setString(2, campoEmail.getText());
            stmt.setString(3, (String) comboPermissao.getSelectedItem());
            stmt.setInt(4, userId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso!");
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar alterações.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    private void deletarUsuario() {
        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este usuário?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try (Connection conexao = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/biblioteca", "postgres", "samuel")) {
                String sql = "DELETE FROM usuarios WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Usuário deletado com sucesso!");
                dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao deletar o usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


    static class TelaVerLivros extends JPanel {
        public TelaVerLivros() {
            setLayout(new BorderLayout());
            add(new JLabel("Tela: Ver Todos os Emprestimos", SwingConstants.CENTER), BorderLayout.CENTER);
        }
    }

    static class TelaCadastrarLivro extends JPanel {
        public TelaCadastrarLivro() {
            setLayout(new BorderLayout());
            add(new JLabel("Tela: Cadastrar Livro", SwingConstants.CENTER), BorderLayout.CENTER);
        }
    }

    public static void main(String[] args) {
        new TelaAdmin(null);
    }
}
