package br.com.almaviva.dama.service;

import java.util.logging.Logger;
import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;
import br.com.almaviva.dama.validacao.ValidacaoMovimento;

public class MovimentoService {
	private static final Logger LOG = Logger.getLogger(MovimentoService.class.getName());

	private Tabuleiro tabuleiro;

	public MovimentoService(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
	}

	public boolean executarMovimento(int[] origem, int[] destino, Cor corJogador) {
		int linhaOrigem = origem[0];
		int colunaOrigem = origem[1];
		int linhaDestino = destino[0];
		int colunaDestino = destino[1];

		if (!ValidacaoMovimento.podeMoverSimples(tabuleiro, linhaOrigem, colunaOrigem, linhaDestino, colunaDestino,
				corJogador)) {
			LOG.warning("Movimento inválido segundo ValidacaoMovimento.");
			return false;
		}

		Peca peca = tabuleiro.getPeca(linhaOrigem, colunaOrigem);
		tabuleiro.setPeca(linhaOrigem, colunaOrigem, null);
		tabuleiro.setPeca(linhaDestino, colunaDestino, peca);

		promoverSeNecessario(peca, linhaDestino);
		LOG.info("Movimento simples realizado com sucesso!");
		return true;
	}

	public boolean existeMovimentoSimples(Cor cor) {
		return ValidacaoMovimento.existeMovimentoSimples(tabuleiro, cor);
	}

	private void promoverSeNecessario(Peca peca, int linhaDestino) {
		if (peca.ehDama())
			return;
		if (peca.getCor() == Cor.AZUL && linhaDestino == Tabuleiro.TAMANHO_TABULEIRO - 1) {
			peca.setDama(true);
			LOG.info("Peça Azul promovida a Dama (movimento simples).");
		}
		if (peca.getCor() == Cor.VERMELHO && linhaDestino == 0) {
			peca.setDama(true);
			LOG.info("Peça Vermelha promovida a Dama (movimento simples).");
		}
	}
}
