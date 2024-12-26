package br.com.almaviva.dama.model;

public class Peca {
    private Cor cor;
    private boolean dama;
    private int linha;
    private int coluna;

    public Peca(Cor cor, int linha, int coluna) {
        this.cor = cor;
        this.linha = linha;
        this.coluna = coluna;
        this.dama = false;
    }

    public Cor getCor() {
        return cor;
    }

    public boolean isDama() {
        return dama;
    }

    public void setDama(boolean dama) {
        this.dama = dama;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setPosicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }
}
