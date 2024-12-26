package br.com.almaviva.dama.validacao;

import java.util.logging.Logger;
import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;

public class ValidacaoMovimento {

    private static final Logger LOG = Logger.getLogger(ValidacaoMovimento.class.getName());

    public static boolean podeMoverSimples(Tabuleiro tabuleiro, int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino, Cor corJogador) {
        if (!confirmarPecaDoJogador(tabuleiro, linhaOrigem, colunaOrigem, corJogador)) return false;
        if (!confirmarDestinoVazio(tabuleiro, linhaDestino, colunaDestino)) return false;

        return confirmarMovimentoPermitido(tabuleiro.getPeca(linhaOrigem, colunaOrigem), linhaOrigem, colunaOrigem, linhaDestino, colunaDestino);
    }

    public static boolean existeMovimentoSimples(Tabuleiro tabuleiro, Cor cor) {
        for (int linha = 0; linha < Tabuleiro.TAMANHO_TABULEIRO; linha++) {
            for (int coluna = 0; coluna < Tabuleiro.TAMANHO_TABULEIRO; coluna++) {
                Peca peca = tabuleiro.getPeca(linha, coluna);
                if (peca != null && peca.getCor() == cor && podeMoverAlgumaCasa(tabuleiro, linha, coluna, peca)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean podeMoverAlgumaCasa(Tabuleiro tabuleiro, int linha, int coluna, Peca peca) {
        for (int[] direcao : getDirecoesMovimento(peca)) {
            int novaLinha = linha + direcao[0];
            int novaColuna = coluna + direcao[1];
            if (confirmarPosicaoValida(novaLinha, novaColuna) && confirmarDestinoVazio(tabuleiro, novaLinha, novaColuna)) {
                return true;
            }
        }
        return false;
    }

    private static boolean confirmarPecaDoJogador(Tabuleiro tabuleiro, int linha, int coluna, Cor corJogador) {
        Peca peca = tabuleiro.getPeca(linha, coluna);
        if (peca == null || peca.getCor() != corJogador) {
            LOG.fine("A peça não pertence ao jogador.");
            return false;
        }
        return true;
    }

    private static boolean confirmarDestinoVazio(Tabuleiro tabuleiro, int linhaDestino, int colunaDestino) {
        if (tabuleiro.getPeca(linhaDestino, colunaDestino) != null) {
            LOG.fine("Destino não está vazio.");
            return false;
        }
        return true;
    }

    private static boolean confirmarMovimentoPermitido(Peca peca, int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino) {
        int deltaLinha = linhaDestino - linhaOrigem;
        int deltaColuna = colunaDestino - colunaOrigem;

        if (peca.isDama()) {
            return Math.abs(deltaLinha) == 1 && Math.abs(deltaColuna) == 1;
        }

        int direcaoPermitida = (peca.getCor() == Cor.AZUL) ? 1 : -1;
        if (deltaLinha == direcaoPermitida && Math.abs(deltaColuna) == 1) {
            return true;
        }

        LOG.fine("Movimento não permitido para a peça.");
        return false;
    }

    private static boolean confirmarPosicaoValida(int linha, int coluna) {
        return linha >= 0 && linha < Tabuleiro.TAMANHO_TABULEIRO && coluna >= 0 && coluna < Tabuleiro.TAMANHO_TABULEIRO;
    }

    private static int[][] getDirecoesMovimento(Peca peca) {
        if (peca.isDama()) {
            return new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        }
        int direcao = (peca.getCor() == Cor.AZUL) ? 1 : -1;
        return new int[][]{{direcao, 1}, {direcao, -1}};
    }
}
