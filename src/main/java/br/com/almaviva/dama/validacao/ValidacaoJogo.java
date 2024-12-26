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
    }

    public boolean atingiuEmpatePorFaltaDeCaptura() {
        return jogadasSemCaptura >= LIMITE_SEM_CAPTURA;
    }

    public boolean podeJogar(Jogador jogador) {
        Cor cor = jogador.getCor();
        int numPecas = tabuleiro.contarPecas(cor);
        if (numPecas == 0) {
            return false;
        }
        if (capturaService.existeCaptura(cor)) {
            return true;
        }
        
        return movimentoService.existeMovimentoSimples(cor);
    }

    public boolean jogadorSemPecas(Jogador jogador) {
        return tabuleiro.contarPecas(jogador.getCor()) == 0;
    }
}
