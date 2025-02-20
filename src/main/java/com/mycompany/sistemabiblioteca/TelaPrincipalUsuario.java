package com.mycompany.sistemabiblioteca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaPrincipalUsuario extends JFrame {

    private EmprestimoLivro painelEmprestimoLivro;
    private MeuPerfil painelMeuPerfil;

    public TelaPrincipalUsuario(String nomeUsuario) {
        setTitle("Bem-vindo, " + nomeUsuario);
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        painelEmprestimoLivro = new EmprestimoLivro();
        painelMeuPerfil = new MeuPerfil(nomeUsuario);

      
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu"); 
        JMenuItem itemEmprestimo = new JMenuItem("Empréstimo de Livros");
        JMenuItem itemPerfil = new JMenuItem("Meu Perfil");
        JMenuItem itemLogoff = new JMenuItem("Logoff");

        itemEmprestimo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
       
                setContentPane(painelEmprestimoLivro);
                revalidate();
                repaint();
            }
        });

        // Ação do item de Perfil
        itemPerfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             
                setContentPane(painelMeuPerfil);
                revalidate();
                repaint();
            }
        });

// Ação do item de Logoff
itemLogoff.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
       
        SistemaBiblioteca telaLogin = new SistemaBiblioteca(); 
        telaLogin.setVisible(true); 
        dispose(); // 
    }
});
        menu.add(itemEmprestimo);
        menu.add(itemPerfil);
        menu.add(itemLogoff);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Configurando o layout inicial para exibir o painel de Cadastro de Livros
        setContentPane(painelEmprestimoLivro);

        setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            SistemaBiblioteca telaLogin = new SistemaBiblioteca(); 
            telaLogin.setVisible(true);
        });
    }
}
 