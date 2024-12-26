package br.com.almaviva.dama.service;

import java.util.List;
import java.util.logging.Logger;
import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;
import br.com.almaviva.dama.validacao.ValidacaoCaptura;

public class CapturaService {

	private static final Logger LOG = Logger.getLogger(CapturaService.class.getName());
	private Tabuleiro tabuleiro;

	public CapturaService(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
	}

	public boolean capturar(int[] origem, int[] destino, Cor corJogador) {
		LOG.info("Tentando capturar de (" + origem[0] + "," + origem[1] + ") para (" + destino[0] + "," + destino[1]
				+ "), cor=" + corJogador);
		int linhaPeca = origem[0];
		int colunaPeca = origem[1];
		int linhaDestino = destino[0];
		int colunaDestino = destino[1];

		if (!ValidacaoCaptura.podeCapturar(tabuleiro, linhaPeca, colunaPeca, linhaDestino, colunaDestino, corJogador)) {
			LOG.warning("Captura inválida segundo ValidacaoCaptura.");
			return false;
		}
		Peca peca = tabuleiro.getPeca(linhaPeca, colunaPeca);
		tabuleiro.setPeca(linhaPeca, colunaPeca, null);

		int meioLin = (linhaPeca + linhaDestino) / 2;
		int meioCol = (colunaPeca + colunaDestino) / 2;
		tabuleiro.setPeca(meioLin, meioCol, null);

		tabuleiro.setPeca(linhaDestino, colunaDestino, peca);

		promoverSeNecessario(peca, linhaDestino);
		LOG.info("Captura realizada com sucesso!");
		return true;
	}

	public void capturarmaisDeUm(int[] posicaoAtual, Cor corJogador) {
		LOG.info("Verificando capturas múltiplas a partir de (" + posicaoAtual[0] + "," + posicaoAtual[1] + "), cor="
				+ corJogador);
		while (true) {
			List<int[]> destinos = ValidacaoCaptura.getCapturasPossiveisDaPeca(tabuleiro, posicaoAtual[0],
					posicaoAtual[1], corJogador);
			if (destinos.isEmpty()) {
				LOG.fine("Nenhuma captura adicional encontrada.");
				break;
			}
			// Pega a primeira (sem Lei da Maioria)
			int[] destinoEscolhido = destinos.get(0);
			LOG.info("Captura múltipla - tentando destino (" + destinoEscolhido[0] + "," + destinoEscolhido[1] + ")");
			boolean ok = capturar(posicaoAtual, destinoEscolhido, corJogador);
			if (!ok) {
				LOG.warning("Falhou a captura múltipla. Interrompendo.");
				break;
			}
			posicaoAtual[0] = destinoEscolhido[0];
			posicaoAtual[1] = destinoEscolhido[1];
		}
	}

	public boolean existeCaptura(Cor corJogador) {
		boolean existe = ValidacaoCaptura.existeCaptura(tabuleiro, corJogador);
		LOG.fine("existeCaptura(" + corJogador + ") => " + existe);
		return existe;
	}

	public boolean podeCapturar(int lin, int col, Cor corJogador) {
		List<int[]> caps = ValidacaoCaptura.getCapturasPossiveisDaPeca(tabuleiro, lin, col, corJogador);
		boolean can = !caps.isEmpty();
		LOG.fine("podeCapturar(" + lin + "," + col + ", " + corJogador + ") => " + can);
		return can;
	}

	private void promoverSeNecessario(Peca peca, int linhaDestino) {
		if (peca.isDama())
			return;
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
