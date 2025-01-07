package br.com.almaviva.dama.model;

import java.util.logging.Logger;

public class Tabuleiro {
    public static final int TAMANHO_TABULEIRO = 8;
    private static final Logger LOG = Logger.getLogger(Tabuleiro.class.getName());

    private Peca[][] casas;

    public Tabuleiro() {
        casas = new Peca[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        inicializarTabuleiro();
    }

    private void inicializarTabuleiro() {
        LOG.info("Inicializando tabuleiro com peças AZUL nas linhas 0..2 e VERMELHO nas linhas 5..7...");
        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < TAMANHO_TABULEIRO; coluna++) {
                if ((linha + coluna) % 2 == 0) {
                    casas[linha][coluna] = new Peca(Cor.AZUL, linha, coluna);
                }
            }
        }
        
        for (int linha = 5; linha < 8; linha++) {
            for (int coluna = 0; coluna < TAMANHO_TABULEIRO; coluna++) {
                if ((linha + coluna) % 2 == 0) {
                    casas[linha][coluna] = new Peca(Cor.VERMELHO, linha, coluna);
                }
            }
        }
    }

    public Peca getPeca(int linha, int coluna) {
        if (linha < 0 || linha >= TAMANHO_TABULEIRO || coluna < 0 || coluna >= TAMANHO_TABULEIRO) {
            return null;
        }
        return casas[linha][coluna];
    }

    public void setPeca(int linha, int coluna, Peca peca) {
        if (linha < 0 || linha >= TAMANHO_TABULEIRO || coluna < 0 || coluna >= TAMANHO_TABULEIRO) {
            return;
        }
        casas[linha][coluna] = peca;
        if (peca != null) {
            peca.setPosicao(linha, coluna);
        }
    }

    public int contarPecas(Cor cor) {
        int contador = 0;
        for (int linha = 0; linha < TAMANHO_TABULEIRO; linha++) {
            for (int coluna = 0; coluna < TAMANHO_TABULEIRO; coluna++) {
                Peca peca = casas[linha][coluna];
                if (peca != null && peca.getCor() == cor) {
                    contador++;
                }
            }
        }
        return contador;
    }
}
