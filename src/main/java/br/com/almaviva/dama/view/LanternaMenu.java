package br.com.almaviva.dama.view;

import java.util.logging.Logger;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class LanternaMenu {
    private static final Logger LOG = Logger.getLogger(LanternaMenu.class.getName());
    private LanternaUI ui;

    private String[] opcoesMenuPrincipal = { "Novo Jogo", "Manual", "Sair" };

    public LanternaMenu(LanternaUI ui) {
        this.ui = ui;
    }

    public int exibirMenuPrincipal() {
        int indice = 0;
        while (true) {
            ui.limparTela();
            ui.desenharTextoCentralizado("=== DAMAS ===", 2);

            for (int i = 0; i < opcoesMenuPrincipal.length; i++) {
                String prefix = (i == indice) ? "> " : "  ";
                String text = prefix + opcoesMenuPrincipal[i];
                int y = 5 + i;
                ui.desenharTextoCentralizado(text, y);
            }

            ui.refresh();
            KeyStroke key = ui.esperarTecla();
            if (key.getKeyType() == KeyType.ArrowUp && indice > 0) {
                indice--;
            } else if (key.getKeyType() == KeyType.ArrowDown && indice < opcoesMenuPrincipal.length - 1) {
                indice++;
            } else if (key.getKeyType() == KeyType.Enter) {
                LOG.info("MenuPrincipal: usuário escolheu indice=" + indice + " => " + opcoesMenuPrincipal[indice]);
                return indice + 1;
            } else if (key.getKeyType() == KeyType.Escape) {
                LOG.info("MenuPrincipal: usuário apertou ESC => sair (opcao=3).");
                return 3;
            }
        }
    }

    public String exibirMenuCorJogador1() {
        String[] opcoes = { "Azul", "Vermelho" };
        int indice = 0;
        while (true) {
            ui.limparTela();
            ui.desenharTextoCentralizado("Escolha a cor do Jogador 1:", 2);

            for (int i = 0; i < opcoes.length; i++) {
                String prefix = (i == indice) ? "> " : "  ";
                String text = prefix + opcoes[i];
                ui.desenharTextoCentralizado(text, 5 + i);
            }
            ui.refresh();

            KeyStroke key = ui.esperarTecla();
            if (key.getKeyType() == KeyType.ArrowUp && indice > 0) {
                indice--;
            } else if (key.getKeyType() == KeyType.ArrowDown && indice < opcoes.length - 1) {
                indice++;
            } else if (key.getKeyType() == KeyType.Enter) {
                LOG.info("MenuCorJogador1: usuário escolheu " + opcoes[indice]);
                return opcoes[indice];
            } else if (key.getKeyType() == KeyType.Escape) {
                LOG.info("MenuCorJogador1: usuário apertou ESC => null.");
                return null;
            }
        }
    }
}
