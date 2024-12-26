package br.com.almaviva.dama.validacao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;

public class ValidacaoCaptura {

    private static final Logger LOG = Logger.getLogger(ValidacaoCaptura.class.getName());

    public static boolean podeCapturar(
        Tabuleiro tabuleiro,
        int linhaOrigem, int colunaOrigem,
        int linhaDestino, int colunaDestino,
        Cor corJogador
    ) {
        Peca peca = tabuleiro.getPeca(linhaOrigem, colunaOrigem);
        if (peca == null) {
            LOG.fine("podeCapturar => sem peça na origem.");
            return false;
        }
        if (peca.getCor() != corJogador) {
            LOG.fine("podeCapturar => cor da peça != corJogador.");
            return false;
        }

        if (!posicaoValida(linhaDestino, colunaDestino)) {
            LOG.fine("podeCapturar => destino fora do tabuleiro.");
            return false;
        }
        if (tabuleiro.getPeca(linhaDestino, colunaDestino) != null) {
            LOG.fine("podeCapturar => destino não vazio.");
            return false;
        }

        int deltaLinha = linhaDestino - linhaOrigem;
        int deltaColuna = colunaDestino - colunaOrigem;

        if (!peca.isDama()) {
            if (peca.getCor() == Cor.AZUL) {
                if (deltaLinha != +2) {
                    LOG.fine("podeCapturar => peça Azul mas deltaLinha != +2.");
                    return false;
                }
            } else {
                if (deltaLinha != -2) {
                    LOG.fine("podeCapturar => peça Vermelha mas deltaLinha != -2.");
                    return false;
                }
            }
            if (Math.abs(deltaColuna) != 2) {
                LOG.fine("podeCapturar => deltaCol != ±2.");
                return false;
            }
        } else {
            // Dama => ±2
            if (Math.abs(deltaLinha) != 2 || Math.abs(deltaColuna) != 2) {
                LOG.fine("podeCapturar => Dama mas deltaLinha/Col != ±2.");
                return false;
            }
        }

        int meioLin = (linhaOrigem + linhaDestino) / 2;
        int meioCol = (colunaOrigem + colunaDestino) / 2;
        if (!posicaoValida(meioLin, meioCol)) {
            LOG.fine("podeCapturar => casa do meio fora do tabuleiro.");
            return false;
        }
        Peca pInimiga = tabuleiro.getPeca(meioLin, meioCol);
        if (pInimiga == null) {
            LOG.fine("podeCapturar => sem peça no meio para capturar.");
            return false;
        }
        if (pInimiga.getCor() == peca.getCor()) {
            LOG.fine("podeCapturar => a peça do meio é da mesma cor (não é inimigo).");
            return false;
        }

        return true;
    }

    public static boolean existeCaptura(Tabuleiro tabuleiro, Cor cor) {
        for (int linha = 0; linha < Tabuleiro.TAMANHO_TABULEIRO; linha++) {
            for (int coluna = 0; coluna < Tabuleiro.TAMANHO_TABULEIRO; coluna++) {
                Peca peca = tabuleiro.getPeca(linha, coluna);
                if (peca != null && peca.getCor() == cor) {
                    List<int[]> capt = getCapturasPossiveisDaPeca(tabuleiro, linha, coluna, cor);
                    if (!capt.isEmpty()) {
                        LOG.fine("existeCaptura => encontrado ao menos uma captura para cor " + cor);
                        return true;
                    }
                }
            }
        }
        LOG.fine("existeCaptura => nenhuma captura para cor " + cor);
        return false;
    }

    public static List<int[]> getCapturasPossiveisDaPeca(
            Tabuleiro tabuleiro, int linha, int coluna, Cor cor
    ) {
        List<int[]> lista = new ArrayList<>();
        Peca peca = tabuleiro.getPeca(linha, coluna);
        if (peca == null || peca.getCor() != cor) return lista;

        int[][] direcoes = {{-2,-2}, {-2,2}, {2,-2}, {2,2}};
        for (int[] d : direcoes) {
            int nl = linha + d[0];
            int nc = coluna + d[1];
            if (podeCapturar(tabuleiro, linha, coluna, nl, nc, cor)) {
                lista.add(new int[]{ nl, nc });
            }
        }
        return lista;
    }

    private static boolean posicaoValida(int l, int c) {
        return (l >= 0 && l < Tabuleiro.TAMANHO_TABULEIRO 
                && c >= 0 && c < Tabuleiro.TAMANHO_TABULEIRO);
    }
}
