package br.com.almaviva.dama.validacao;

import java.util.logging.Logger;
import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Jogador;
import br.com.almaviva.dama.model.Tabuleiro;
import br.com.almaviva.dama.service.CapturaService;
import br.com.almaviva.dama.service.MovimentoService;

public class ValidacaoJogo {
    private static final Logger LOG = Logger.getLogger(ValidacaoJogo.class.getName());

    private Tabuleiro tabuleiro;
    private MovimentoService movimentoService;
    private CapturaService capturaService;
    private int jogadasSemCaptura;
    private final int LIMITE_SEM_CAPTURA = 20;

    public ValidacaoJogo(Tabuleiro tabuleiro,
                         MovimentoService movimentoService,
                         CapturaService capturaService) {
        this.tabuleiro = tabuleiro;
        this.movimentoService = movimentoService;
        this.capturaService = capturaService;
        this.jogadasSemCaptura = 0;
    }

    public void resetarContagemSemCaptura() {
        this.jogadasSemCaptura = 0;
        LOG.fine("Contagem de jogadas sem captura foi resetada para 0.");
    }

    public void incrementarContagemSemCaptura() {
        this.jogadasSemCaptura++;
        LOG.fine("Incrementando jogadas sem captura. Valor atual: " + jogadasSemCaptura);
    }

    public boolean atingiuEmpatePorFaltaDeCaptura() {
        boolean res = (jogadasSemCaptura >= LIMITE_SEM_CAPTURA);
        if (res) {
            LOG.info("Atingiu limite de " + LIMITE_SEM_CAPTURA + " jogadas sem captura => Empate.");
        }
        return res;
    }

    public boolean podeJogar(Jogador jogador) {
        Cor cor = jogador.getCor();
        int numPecas = tabuleiro.contarPecas(cor);
        if (numPecas == 0) {
            LOG.fine("Jogador " + jogador.getNome() + " (" + cor + ") não tem peças.");
            return false;
        }
        // Se existe captura => tem jogada
        if (capturaService.existeCaptura(cor)) {
            LOG.fine("Jogador " + jogador.getNome() + " (" + cor + ") tem captura.");
            return true;
        }
        // Senão, vê se há movimento simples
        boolean movSimples = movimentoService.existeMovimentoSimples(cor);
        LOG.fine("Jogador " + jogador.getNome() + " (" + cor + ") => existeMovimentoSimples? " + movSimples);
        return movSimples;
    }

    public boolean semPecas(Jogador jogador) {
        boolean sem = (tabuleiro.contarPecas(jogador.getCor()) == 0);
        if (sem) {
            LOG.info("Jogador " + jogador.getNome() + " não tem mais peças!");
        }
        return sem;
    }
}
