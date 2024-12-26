package br.com.almaviva.dama;

import java.util.logging.Logger;
import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Jogador;
import br.com.almaviva.dama.service.JogoService;
import br.com.almaviva.dama.view.LanternaMenu;
import br.com.almaviva.dama.view.LanternaUI;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOG.info("Iniciando aplicação Damas...");
        LanternaUI ui = new LanternaUI();
        LanternaMenu menu = new LanternaMenu(ui);

        boolean continuar = true;
        while (continuar) {
            int opcao = menu.exibirMenuPrincipal();
            LOG.info("Opção selecionada no menu principal: " + opcao);
            switch (opcao) {
                case 1:
                    LOG.info("Escolhendo cor para Jogador 1");
                    String corEscolhida = menu.exibirMenuCorJogador1();
                    if (corEscolhida == null) {
                        LOG.warning("Jogador apertou ESC no menu de cor. Voltando ao menu principal.");
                        break;
                    }
                    Cor corJog1 = corEscolhida.equalsIgnoreCase("Azul") ? Cor.AZUL : Cor.VERMELHO;
                    Cor corJog2 = (corJog1 == Cor.AZUL) ? Cor.VERMELHO : Cor.AZUL;

                    Jogador jogador1 = new Jogador("Jogador 1", corJog1);
                    Jogador jogador2 = new Jogador("Jogador 2", corJog2);
                    LOG.info("Jogador 1: " + jogador1.getNome() + " - " + jogador1.getCor());
                    LOG.info("Jogador 2: " + jogador2.getNome() + " - " + jogador2.getCor());

                    JogoService jogo = new JogoService(ui, jogador1, jogador2);
                    jogo.iniciarJogo();
                    break;

                case 2: // Manual
                    LOG.info("Exibindo Manual simplificado");
                    ui.exibirMensagemCentralizada(
                            "Manual (Simplificado)\n\n"
                                    + "- As peças se movem em diagonal\n"
                                    + "- Captura (se existir) é obrigatória\n"
                                    + "- ESC a qualquer momento para voltar"
                    );
                    break;

                case 3:
                default:
                    LOG.info("Opção Sair. Encerrando aplicação.");
                    continuar = false;
                    break;
            }
        }

        ui.finalizar();
        LOG.info("Aplicação Damas finalizada.");
    }
}
