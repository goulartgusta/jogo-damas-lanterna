package br.com.almaviva.dama.service;

import java.util.logging.Logger;
import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Jogador;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;
import br.com.almaviva.dama.view.LanternaUI;
import br.com.almaviva.dama.validacao.ValidacaoJogo;

public class JogoService {

	private static final Logger LOG = Logger.getLogger(JogoService.class.getName());

	private LanternaUI ui;
	private Tabuleiro tabuleiro;
	private Jogador jogador1;
	private Jogador jogador2;
	private Jogador jogadorAtual;
	private MovimentoService movimentoService;
	private CapturaService capturaService;
	private ValidacaoJogo validacaoJogo;

	public JogoService(LanternaUI ui, Jogador j1, Jogador j2) {
		LOG.info("Criando JogoService...");
		this.ui = ui;
		this.jogador1 = j1;
		this.jogador2 = j2;
		this.jogadorAtual = j1;
		this.tabuleiro = new Tabuleiro();

		this.movimentoService = new MovimentoService(tabuleiro);
		this.capturaService = new CapturaService(tabuleiro);

		this.validacaoJogo = new ValidacaoJogo(tabuleiro, movimentoService, capturaService);
		LOG.info("JogoService criado com sucesso.");
	}

	public void iniciarJogo() {
		LOG.info("Iniciando loop de jogo...");
		boolean fim = false;

		while (!fim) {
			// Verifica se jogadorAtual ficou sem peças
			if (validacaoJogo.semPecas(jogadorAtual)) {
				Jogador vencedor = (jogadorAtual == jogador1) ? jogador2 : jogador1;
				ui.exibirMensagemCentralizada(
					"Parabéns, " + vencedor.getNome() + "! Você venceu (oponente sem peças).");
				LOG.info("Encerrando jogo: " + jogadorAtual.getNome() + " ficou sem peças.");
				break;
			}

			// Se jogadorAtual não pode mover => fim
			if (!validacaoJogo.podeJogar(jogadorAtual)) {
				ui.exibirMensagemCentralizada(
					"O jogador " + jogadorAtual.getNome() + " não pode mover.\nFim de jogo!");
				LOG.info("Encerrando jogo: " + jogadorAtual.getNome() + " não pode jogar.");
				break;
			}

			// Se 20 jogadas sem captura => empate
			if (validacaoJogo.atingiuEmpatePorFaltaDeCaptura()) {
				ui.exibirMensagemCentralizada("Empate! 20 jogadas sem captura.");
				LOG.info("Empate por 20 jogadas sem captura.");
				break;
			}

			boolean desistiu = executarTurno(jogadorAtual);
			if (desistiu) {
				LOG.info("Jogador " + jogadorAtual.getNome() + " desistiu (ESC). Encerrando jogo.");
				fim = true;
			} else {
				jogadorAtual = (jogadorAtual == jogador1) ? jogador2 : jogador1;
			}
		}

		ui.exibirMensagemCentralizada("Jogo encerrado! (Pressione uma tecla)");
		ui.finalizar();
		LOG.info("Loop de jogo finalizado.");
	}

	private boolean executarTurno(Jogador jog) {
		LOG.info("Executando turno de " + jog.getNome() + " (" + jog.getCor() + ")...");
		Cor cor = jog.getCor();
		String msg = "Vez de " + jog.getNome() + " (" + cor + ")";
		boolean temCaptura = capturaService.existeCaptura(cor);

		while (true) {
			int[] origem = ui.selecionarPosicao(tabuleiro, msg + (temCaptura ? "\n(Captura Obrigatória!)" : ""));
			if (origem == null) {
				LOG.info("Jogador " + jog.getNome() + " apertou ESC na seleção de origem.");
				return true; // ESC => desistiu
			}

			Peca peca = tabuleiro.getPeca(origem[0], origem[1]);
			if (peca == null) {
				ui.exibirMensagemCentralizada("Não há peça nessa posição.");
				LOG.fine("Jogador selecionou casa vazia. Pedindo nova seleção.");
				continue;
			}
			if (peca.getCor() != cor) {
				ui.exibirMensagemCentralizada("Essa peça não é sua!");
				LOG.fine("Jogador selecionou peça de outra cor. Pedindo nova seleção.");
				continue;
			}

			if (temCaptura && !capturaService.podeCapturar(origem[0], origem[1], cor)) {
				ui.exibirMensagemCentralizada("Captura obrigatória! Escolha peça que possa capturar.");
				LOG.fine("Peça selecionada não pode capturar. Pedindo nova seleção.");
				continue;
			}

			int[] destino = ui.selecionarPosicao(tabuleiro, "Selecione o destino");
			if (destino == null) {
				LOG.info("Jogador " + jog.getNome() + " apertou ESC na seleção de destino.");
				continue; 
			}

			if (temCaptura) {
				LOG.info("Tentando captura...");
				boolean capturou = capturaService.capturar(origem, destino, cor);
				if (!capturou) {
					ui.exibirMensagemCentralizada("Captura inválida!");
					LOG.warning("Captura retornou falso. Pedindo nova jogada.");
					continue;
				}
				validacaoJogo.resetarContagemSemCaptura();

				capturaService.capturarmaisDeUm(destino, cor);
				return false; 
			} else {
				LOG.info("Tentando movimento simples...");
				boolean moveu = movimentoService.executarMovimento(origem, destino, cor);
				if (!moveu) {
					ui.exibirMensagemCentralizada("Movimento inválido!");
					LOG.warning("Movimento retornou falso. Pedindo nova jogada.");
					continue;
				}
				validacaoJogo.incrementarContagemSemCaptura();
				return false;
			}
		}
	}
}
