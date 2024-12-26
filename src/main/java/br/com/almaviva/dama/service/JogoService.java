package br.com.almaviva.dama.service;

import java.util.logging.Logger;
import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Jogador;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;
import br.com.almaviva.dama.validacao.ValidacaoJogo;
import br.com.almaviva.dama.view.JogoView;

public class JogoService {

    private static final Logger LOG = Logger.getLogger(JogoService.class.getName());

    private final JogoView view;
    private final Tabuleiro tabuleiro;
    private final Jogador jogador1;
    private final Jogador jogador2;
    private Jogador jogadorAtual;
    private final MovimentoService movimentoService;
    private final CapturaService capturaService;
    private final ValidacaoJogo validacaoJogo;

    public JogoService(JogoView view, Jogador jogador1, Jogador jogador2) {
        LOG.info("Criando JogoService...");
        this.view = view;
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        this.jogadorAtual = jogador1;
        this.tabuleiro = new Tabuleiro();

        this.movimentoService = new MovimentoService(tabuleiro);
        this.capturaService = new CapturaService(tabuleiro);
        this.validacaoJogo = new ValidacaoJogo(tabuleiro, movimentoService, capturaService);

        LOG.info("Jogo criado com sucesso.");
    }

    public void iniciarJogo() {
        LOG.info("Iniciando jogo...");
        while (!verificarFimDeJogo()) {
            if (executarTurno()) {
                LOG.info("Jogador " + jogadorAtual.getNome() + " desistiu. Encerrando o jogo.");
                break;
            }
            alternarJogador();
        }
        encerrarJogo();
    }

    private boolean verificarFimDeJogo() {
        if (validacaoJogo.jogadorSemPecas(jogadorAtual)) {
            exibirMensagemVitoria();
            return true;
        }
        if (!validacaoJogo.podeJogar(jogadorAtual)) {
            exibirMensagemSemMovimentos();
            return true;
        }
        if (validacaoJogo.atingiuEmpatePorFaltaDeCaptura()) {
            exibirMensagemEmpate();
            return true;
        }
        return false;
    }

    private void exibirMensagemVitoria() {
        Jogador vencedor = (jogadorAtual == jogador1) ? jogador2 : jogador1;
        view.exibirMensagemCentralizada("Parabéns, " + vencedor.getNome() + "! Você venceu.");
        LOG.info("Fim de jogo: " + vencedor.getNome() + " venceu.");
    }

    private void exibirMensagemSemMovimentos() {
        view.exibirMensagemCentralizada("O jogador " + jogadorAtual.getNome() + " não pode mover. Fim de jogo!");
        LOG.info("Fim de jogo: " + jogadorAtual.getNome() + " não pode jogar.");
    }

    private void exibirMensagemEmpate() {
        view.exibirMensagemCentralizada("Empate! 20 jogadas sem captura.");
        LOG.info("Fim de jogo: empate por 20 jogadas sem captura.");
    }

    private void alternarJogador() {
        jogadorAtual = (jogadorAtual == jogador1) ? jogador2 : jogador1;
    }

    private void encerrarJogo() {
        view.exibirMensagemCentralizada("Jogo encerrado! (Pressione uma tecla)");
        view.finalizar();
        LOG.info("Jogo finalizado.");
    }

    private boolean executarTurno() {
        LOG.info("Turno de " + jogadorAtual.getNome() + " (" + jogadorAtual.getCor() + ")");
        Cor cor = jogadorAtual.getCor();
        String mensagem = "Vez de " + jogadorAtual.getNome() + " (" + cor + ")";
        boolean temCaptura = capturaService.existeCaptura(cor);

        int[] origem = selecionarOrigem(cor, mensagem, temCaptura);
        if (origem == null) {
            return true; 
        }

        int[] destino = selecionarDestino();
        if (destino == null) {
            return false;
        }

        return processarMovimentoOuCaptura(origem, destino, temCaptura, cor);
    }

    private int[] selecionarOrigem(Cor cor, String mensagem, boolean temCaptura) {
        String mensagemCompleta = mensagem + (temCaptura ? "\n(Captura obrigatória!)" : "");
        int[] origem;

        do {
            origem = view.selecionarPosicao(tabuleiro, mensagemCompleta);
            if (origem == null) {
                LOG.info("Jogador " + jogadorAtual.getNome() + " apertou ESC na seleção de origem.");
                return null;
            }
        } while (!validarSelecaoDeOrigem(origem, cor));

        return origem;
    }

    private int[] selecionarDestino() {
        return view.selecionarPosicao(tabuleiro, "Selecione o destino");
    }

    private boolean validarSelecaoDeOrigem(int[] origem, Cor cor) {
        Peca peca = tabuleiro.getPeca(origem[0], origem[1]);
        if (peca == null) {
            view.exibirMensagemCentralizada("Não há peça nessa posição.");
            return false;
        }
        if (peca.getCor() != cor) {
            view.exibirMensagemCentralizada("Essa peça não é sua!");
            return false;
        }
        if (capturaService.existeCaptura(cor) && !capturaService.podeCapturar(origem[0], origem[1], cor)) {
            view.exibirMensagemCentralizada("Captura obrigatória! Escolha uma peça que possa capturar.");
            return false;
        }
        return true;
    }

    private boolean processarMovimentoOuCaptura(int[] origem, int[] destino, boolean temCaptura, Cor cor) {
        if (temCaptura) {
            return processarCaptura(origem, destino, cor);
        } else {
            return processarMovimento(origem, destino, cor);
        }
    }

    private boolean processarCaptura(int[] origem, int[] destino, Cor cor) {
        if (!capturaService.capturar(origem, destino, cor)) {
            view.exibirMensagemCentralizada("Captura inválida!");
            return false;
        }
        validacaoJogo.resetarContagemSemCaptura();
        capturaService.capturarmaisDeUm(destino, cor);
        return true;
    }

    private boolean processarMovimento(int[] origem, int[] destino, Cor cor) {
        if (!movimentoService.executarMovimento(origem, destino, cor)) {
            view.exibirMensagemCentralizada("Movimento inválido!");
            return false;
        }
        validacaoJogo.incrementarContagemSemCaptura();
        return true;
    }
}
