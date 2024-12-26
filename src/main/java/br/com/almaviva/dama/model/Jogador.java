package br.com.almaviva.dama.model;

public class Jogador {
    private String nome;
    private Cor cor;

    public Jogador(String nome, Cor cor) {
        this.nome = nome;
        this.cor = cor;
    }

    public String getNome() {
        return nome;
    }

    public Cor getCor() {
        return cor;
    }
}
