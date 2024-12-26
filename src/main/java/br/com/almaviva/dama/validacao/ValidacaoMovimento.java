package br.com.almaviva.dama.validacao;

import java.util.logging.Logger;
import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;

public class ValidacaoMovimento {

    private static final Logger LOG = Logger.getLogger(ValidacaoMovimento.class.getName());

    public static boolean podeMoverSimples(
            Tabuleiro tabuleiro,
            int linhaOrigem, int colunaOrigem,
            int linhaDestino, int colunaDestino,
            Cor corJogador
    ) {
        Peca peca = tabuleiro.getPeca(linhaOrigem, colunaOrigem);
        if (peca == null) {
            LOG.fine("podeMoverSimples => Peca inexistente na origem.");
            return false;
        }
        if (peca.getCor() != corJogador) {
            LOG.fine("podeMoverSimples => Cor da peça não bate com corJogador.");
            return false;
        }

        if (tabuleiro.getPeca(linhaDestino, colunaDestino) != null) {
            LOG.fine("podeMoverSimples => Destino não está vazio.");
            return false;
        }

        int deltaLinha = linhaDestino - linhaOrigem;
        int deltaCol = colunaDestino - colunaOrigem;

        if (!peca.isDama()) {
            if (peca.getCor() == Cor.AZUL) {
                if (deltaLinha != +1) {
                    LOG.fine("podeMoverSimples => Peça Azul, deltaLinha != +1.");
                    return false;
                }
            } else {
                if (deltaLinha != -1) {
                    LOG.fine("podeMoverSimples => Peça Vermelha, deltaLinha != -1.");
                    return false;
                }
            }
            if (Math.abs(deltaCol) != 1) {
                LOG.fine("podeMoverSimples => deltaCol != 1.");
                return false;
            }
        } else {
            // Dama => ±1 diagonal
            if (Math.abs(deltaLinha) != 1 || Math.abs(deltaCol) != 1) {
                LOG.fine("podeMoverSimples => Dama mas deltaLinha/deltaCol != 1.");
                return false;
            }
        }

        return true;
    }

    public static boolean existeMovimentoSimples(Tabuleiro tabuleiro, Cor cor) {
        for (int linha = 0; linha < Tabuleiro.TAMANHO_TABULEIRO; linha++) {
            for (int coluna = 0; coluna < Tabuleiro.TAMANHO_TABULEIRO; coluna++) {
                Peca peca = tabuleiro.getPeca(linha, coluna);
                if (peca != null && peca.getCor() == cor) {
                    if (podeMoverAlgumaCasa(tabuleiro, linha, coluna, peca)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean podeMoverAlgumaCasa(Tabuleiro tabuleiro, int lin, int col, Peca p) {
        if (!p.isDama()) {
            int novaLinha = (p.getCor() == Cor.AZUL) ? lin + 1 : lin - 1;
            for (int dcol = -1; dcol <= 1; dcol += 2) {
                int novaCol = col + dcol;
                if (posValida(novaLinha, novaCol)) {
                    if (tabuleiro.getPeca(novaLinha, novaCol) == null) {
                        return true;
                    }
                }
            }
        } else {
            int[][] dir = { {1,1},{1,-1},{-1,1},{-1,-1} };
            for (int[] d : dir) {
                int nl = lin + d[0];
                int nc = col + d[1];
                if (posValida(nl, nc)) {
                    if (tabuleiro.getPeca(nl, nc) == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean posValida(int l, int c) {
        return (l >= 0 && l < Tabuleiro.TAMANHO_TABULEIRO && c >= 0 && c < Tabuleiro.TAMANHO_TABULEIRO);
    }
}
