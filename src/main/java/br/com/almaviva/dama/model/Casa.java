package br.com.almaviva.dama.model;

public class Casa {
    private int linha;
    private int coluna;

    public Casa(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public String getCoordenada() {
        char letraColuna = (char)('A' + this.coluna);
        int linhaTabuleiro = 8 - this.linha;
        return "" + letraColuna + linhaTabuleiro;
    }
}
