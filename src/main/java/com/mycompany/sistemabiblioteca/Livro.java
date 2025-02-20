package com.mycompany.sistemabiblioteca;

/**
 *
 * @author 7bluz
 */
public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private int quantidadePaginas;
    private String sinopse;

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
  
    public String getTitulo() {
        return titulo;
    }
  
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
  
    public String getAutor() {
        return autor;
    }
  
    public void setAutor(String autor) {
        this.autor = autor;
    }
  
    public int getQuantidadePaginas() {
        return quantidadePaginas;
    }
  
    public void setQuantidadePaginas(int quantidadePaginas) {
        this.quantidadePaginas = quantidadePaginas;
    }
  
    public String getSinopse() {
        return sinopse;
    }
  
    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }
}
