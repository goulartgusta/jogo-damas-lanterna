package br.com.almaviva.dama;

import java.util.logging.Logger;

import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Jogador;
import br.com.almaviva.dama.service.JogoService;
import br.com.almaviva.dama.view.JogoView;
import br.com.almaviva.dama.view.MenuView;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOG.info("Iniciando aplicação Damas...");
        JogoView viewJogo = new JogoView();
        MenuView viewMenu = new MenuView(viewJogo);

        boolean continuar = true;
        while (continuar) {
        	int opcao = viewMenu.exibirMenuPrincipal();
        	switch (opcao) {
                case 1:
                    LOG.info("Escolhendo cor para Jogador 1");
                    String corEscolhida = viewMenu.exibirMenuCorJogador1();
                    if (corEscolhida == null) {
                        LOG.warning("Jogador apertou ESC. Voltando ao menu principal.");
                        break;
                    }
                    Cor corJog1 = corEscolhida.equalsIgnoreCase("Azul") ? Cor.AZUL : Cor.VERMELHO;
                    Cor corJog2 = (corJog1 == Cor.AZUL) ? Cor.VERMELHO : Cor.AZUL;

                    Jogador jogador1 = new Jogador("Jogador 1", corJog1);
                    Jogador jogador2 = new Jogador("Jogador 2", corJog2);
                    LOG.info("Jogador 1: " + jogador1.getNome() + " - " + jogador1.getCor());
                    LOG.info("Jogador 2: " + jogador2.getNome() + " - " + jogador2.getCor());

                    JogoService jogo = new JogoService(viewJogo, jogador1, jogador2);
                    jogo.iniciarJogo();
                    break;

                case 2: 
                    exibirManual(viewJogo);
                    break;

                case 3:
                default:
                    LOG.info("Opção Sair. Encerrando aplicação.");
                    continuar = false;
                    break;
            }
        }

        viewJogo.finalizar();
        LOG.info("Aplicação Damas finalizada.");
    }

    private static void exibirManual(JogoView viewJogo) {
        String[] manual = {
            "Manual",
            "- As peças se movem somente em diagonal",
            "- Sempre que existir uma possibilidade de captura de uma ou mais uma peças, deve ser obrigatória",
            "- Para virar dama, deve chegar a outra extremidade do campo",
            "- Aperte 'ESC' a qualquer momento para voltar"
        };

        viewJogo.limparTela();

        int linhaAtual = 5; 
        for (String linha : manual) {
            viewJogo.desenharTextoCentralizado(linha, linhaAtual);
            linhaAtual += 2; 
        }

        viewJogo.refresh();
        viewJogo.esperarTecla(); 
    }
}
