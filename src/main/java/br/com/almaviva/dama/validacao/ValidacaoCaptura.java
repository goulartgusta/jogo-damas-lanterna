package br.com.almaviva.dama.validacao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;

public class ValidacaoCaptura {

    private static final Logger LOG = Logger.getLogger(ValidacaoCaptura.class.getName());

    public static boolean podeCapturar(Tabuleiro tabuleiro, int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino, Cor corJogador) {
        if (!confirmarPecaJogador(tabuleiro, linhaOrigem, colunaOrigem, corJogador)) return false;
        if (!confirmarDestinoValido(tabuleiro, linhaDestino, colunaDestino)) return false;
        if (!confirmarMovimentoCapturaValido(tabuleiro, linhaOrigem, colunaOrigem, linhaDestino, colunaDestino)) return false;

        return confirmarCapturaValida(tabuleiro, linhaOrigem, colunaOrigem, linhaDestino, colunaDestino);
    }

    public static boolean existeCaptura(Tabuleiro tabuleiro, Cor cor) {
        for (int linha = 0; linha < Tabuleiro.TAMANHO_TABULEIRO; linha++) {
            for (int coluna = 0; coluna < Tabuleiro.TAMANHO_TABULEIRO; coluna++) {
                Peca peca = tabuleiro.getPeca(linha, coluna);
                if (peca != null && peca.getCor() == cor && !getCapturasPossiveisDaPeca(tabuleiro, linha, coluna, cor).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<int[]> getCapturasPossiveisDaPeca(Tabuleiro tabuleiro, int linha, int coluna, Cor cor) {
        List<int[]> capturas = new ArrayList<>();
        if (!confirmarPecaJogador(tabuleiro, linha, coluna, cor)) return capturas;

        for (int[] direcao : getDirecoesCaptura()) {
            int novaLinha = linha + direcao[0];
            int novaColuna = coluna + direcao[1];
            if (podeCapturar(tabuleiro, linha, coluna, novaLinha, novaColuna, cor)) {
                capturas.add(new int[]{novaLinha, novaColuna});
            }
        }
        return capturas;
    }

    private static boolean confirmarPecaJogador(Tabuleiro tabuleiro, int linha, int coluna, Cor corJogador) {
        Peca peca = tabuleiro.getPeca(linha, coluna);
        if (peca == null || peca.getCor() != corJogador) {
            LOG.fine("A peça na posição não pertence ao jogador.");
            return false;
        }
        return true;
    }

    private static boolean confirmarDestinoValido(Tabuleiro tabuleiro, int linhaDestino, int colunaDestino) {
        if (!confirmarPosicaoDentroTabuleiro(linhaDestino, colunaDestino)) {
            LOG.fine("Destino fora do tabuleiro.");
            return false;
        }
        if (tabuleiro.getPeca(linhaDestino, colunaDestino) != null) {
            LOG.fine("Destino já ocupado.");
            return false;
        }
        return true;
    }

    private static boolean confirmarMovimentoCapturaValido(Tabuleiro tabuleiro, int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino) {
        Peca peca = tabuleiro.getPeca(linhaOrigem, colunaOrigem);
        int deltaLinha = linhaDestino - linhaOrigem;
        int deltaColuna = colunaDestino - colunaOrigem;

        if (peca.ehDama()) {
            if (Math.abs(deltaLinha) != 2 || Math.abs(deltaColuna) != 2) {
                LOG.fine("Movimento de captura inválido para Dama.");
                return false;
            }
        } else {
            int direcaoPermitida = (peca.getCor() == Cor.AZUL) ? 2 : -2;
            if (deltaLinha != direcaoPermitida || Math.abs(deltaColuna) != 2) {
                LOG.fine("Movimento de captura inválido para peça normal.");
                return false;
            }
        }
        return true;
    }

    private static boolean confirmarCapturaValida(Tabuleiro tabuleiro, int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino) {
        int meioLinha = (linhaOrigem + linhaDestino) / 2;
        int meioColuna = (colunaOrigem + colunaDestino) / 2;

        if (!confirmarPosicaoDentroTabuleiro(meioLinha, meioColuna)) {
            LOG.fine("Peça intermediária fora do tabuleiro.");
            return false;
        }

        Peca pecaIntermediaria = tabuleiro.getPeca(meioLinha, meioColuna);
        Peca pecaOrigem = tabuleiro.getPeca(linhaOrigem, colunaOrigem);

        if (pecaIntermediaria == null || pecaIntermediaria.getCor() == pecaOrigem.getCor()) {
            LOG.fine("Peça intermediária inválida para captura.");
            return false;
        }
        return true;
    }

    private static boolean confirmarPosicaoDentroTabuleiro(int linha, int coluna) {
        return linha >= 0 && linha < Tabuleiro.TAMANHO_TABULEIRO && coluna >= 0 && coluna < Tabuleiro.TAMANHO_TABULEIRO;
    }

    private static int[][] getDirecoesCaptura() {
        return new int[][]{{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
    }
}
