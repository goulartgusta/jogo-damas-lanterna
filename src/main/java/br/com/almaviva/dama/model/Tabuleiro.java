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
        for (int lin = 0; lin < 3; lin++) {
            for (int col = 0; col < TAMANHO_TABULEIRO; col++) {
                if ((lin + col) % 2 == 0) {
                    casas[lin][col] = new Peca(Cor.AZUL, lin, col);
                }
            }
        }
        for (int lin = 5; lin < 8; lin++) {
            for (int col = 0; col < TAMANHO_TABULEIRO; col++) {
                if ((lin + col) % 2 == 0) {
                    casas[lin][col] = new Peca(Cor.VERMELHO, lin, col);
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
        for (int lin = 0; lin < TAMANHO_TABULEIRO; lin++) {
            for (int col = 0; col < TAMANHO_TABULEIRO; col++) {
                Peca p = casas[lin][col];
                if (p != null && p.getCor() == cor) {
                    contador++;
                }
            }
        }
        return contador;
    }
}
