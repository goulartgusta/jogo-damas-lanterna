package br.com.almaviva.dama.validacao;

import java.util.logging.Logger;
import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;

public class ValidacaoMovimento {

    private static final Logger LOG = Logger.getLogger(ValidacaoMovimento.class.getName());

    public static boolean podeMoverSimples(
            Tabuleiro tabuleiro,
            int linOrigem, int colOrigem,
            int linDestino, int colDestino,
            Cor corJogador
    ) {
        Peca p = tabuleiro.getPeca(linOrigem, colOrigem);
        if (p == null) {
            LOG.fine("podeMoverSimples => Peca inexistente na origem.");
            return false;
        }
        if (p.getCor() != corJogador) {
            LOG.fine("podeMoverSimples => Cor da peça não bate com corJogador.");
            return false;
        }

        if (tabuleiro.getPeca(linDestino, colDestino) != null) {
            LOG.fine("podeMoverSimples => Destino não está vazio.");
            return false;
        }

        int deltaLinha = linDestino - linOrigem;
        int deltaCol = colDestino - colOrigem;

        if (!p.isDama()) {
            if (p.getCor() == Cor.AZUL) {
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
        for (int lin = 0; lin < Tabuleiro.TAMANHO_TABULEIRO; lin++) {
            for (int col = 0; col < Tabuleiro.TAMANHO_TABULEIRO; col++) {
                Peca p = tabuleiro.getPeca(lin, col);
                if (p != null && p.getCor() == cor) {
                    if (podeMoverAlgumaCasa(tabuleiro, lin, col, p)) {
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
