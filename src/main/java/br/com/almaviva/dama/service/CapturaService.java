package br.com.almaviva.dama.service;

import java.util.List;
import java.util.logging.Logger;

import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;
import br.com.almaviva.dama.validacao.ValidacaoCaptura;

public class CapturaService {

    private static final Logger LOG = Logger.getLogger(CapturaService.class.getName());
    private final Tabuleiro tabuleiro;

    public CapturaService(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    public boolean capturar(int[] origem, int[] destino, Cor corJogador) {
        int linhaPeca = origem[0];
        int colunaPeca = origem[1];
        int linhaDestino = destino[0];
        int colunaDestino = destino[1];

        if (!ValidacaoCaptura.podeCapturar(tabuleiro, linhaPeca, colunaPeca, linhaDestino, colunaDestino, corJogador)) {
            LOG.warning("Captura inválida segundo ValidacaoCaptura.");
            return false;
        }

        executarCaptura(linhaPeca, colunaPeca, linhaDestino, colunaDestino);

        Peca peca = tabuleiro.getPeca(linhaDestino, colunaDestino);
        promoverSeNecessario(peca, linhaDestino);
        LOG.info("Captura realizada com sucesso!");
        return true;
    }

    public void capturarmaisDeUm(int[] posicaoAtual, Cor corJogador) {
        boolean capturou = true;
        while (capturou) {
            List<int[]> destinos = ValidacaoCaptura.getCapturasPossiveisDaPeca(
                    tabuleiro, posicaoAtual[0], posicaoAtual[1], corJogador);

            if (destinos.isEmpty()) {
                LOG.fine("Nenhuma captura adicional encontrada.");
                capturou = false;
            } else {
                int[] destinoEscolhido = destinos.get(0);
                capturou = capturar(posicaoAtual, destinoEscolhido, corJogador);
                if (capturou) {
                    posicaoAtual[0] = destinoEscolhido[0];
                    posicaoAtual[1] = destinoEscolhido[1];
                } else {
                    LOG.warning("Falhou a captura múltipla. Interrompendo.");
                }
            }
        }
    }

    public boolean existeCaptura(Cor corJogador) {
        return ValidacaoCaptura.existeCaptura(tabuleiro, corJogador);
    }

    public boolean podeCapturar(int linha, int coluna, Cor corJogador) {
        List<int[]> caps = ValidacaoCaptura.getCapturasPossiveisDaPeca(tabuleiro, linha, coluna, corJogador);
        return !caps.isEmpty();
    }

    private void executarCaptura(int linhaPeca, int colunaPeca, int linhaDestino, int colunaDestino) {
        Peca peca = tabuleiro.getPeca(linhaPeca, colunaPeca);
        tabuleiro.setPeca(linhaPeca, colunaPeca, null);

        int meioLin = (linhaPeca + linhaDestino) / 2;
        int meioCol = (colunaPeca + colunaDestino) / 2;
        tabuleiro.setPeca(meioLin, meioCol, null);

        tabuleiro.setPeca(linhaDestino, colunaDestino, peca);
    }

    private void promoverSeNecessario(Peca peca, int linhaDestino) {
        if (peca.isDama()) {
            return;
        }
        if (peca.getCor() == Cor.AZUL && linhaDestino == Tabuleiro.TAMANHO_TABULEIRO - 1) {
            peca.setDama(true);
            LOG.info("Peça Azul promovida a Dama.");
        }
        if (peca.getCor() == Cor.VERMELHO && linhaDestino == 0) {
            peca.setDama(true);
            LOG.info("Peça Vermelha promovida a Dama.");
        }
    }
}
