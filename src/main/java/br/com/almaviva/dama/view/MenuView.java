package br.com.almaviva.dama.view;

import java.util.logging.Logger;

import com.googlecode.lanterna.input.KeyStroke;

public class MenuView {
    private static final Logger LOG = Logger.getLogger(MenuView.class.getName());
    private final JogoView view;

    private final String[] opcoesMenuPrincipal = { "Novo Jogo", "Manual", "Sair" };

    public MenuView(JogoView view) {
        this.view = view;
    }

    public int exibirMenuPrincipal() {
        int indice = 0;
        boolean continuar = true;

        while (continuar) {
            view.limparTela();
            desenharTitulo(); 

            for (int i = 0; i < opcoesMenuPrincipal.length; i++) {
                String prefix = (i == indice) ? "> " : "  ";
                String text = prefix + opcoesMenuPrincipal[i];
                int y = 10 + i; 
                view.desenharTextoCentralizado(text, y);
            }

            view.refresh();
            KeyStroke key = view.esperarTecla();

            switch (key.getKeyType()) {
                case ArrowUp:
                    if (indice > 0) indice--;
                    break;
                case ArrowDown:
                    if (indice < opcoesMenuPrincipal.length - 1) indice++;
                    break;
                case Enter:
                    LOG.info("MenuPrincipal: usuário escolheu indice=" + indice + " => " + opcoesMenuPrincipal[indice]);
                    return indice + 1;
                case Escape:
                    LOG.info("MenuPrincipal: usuário apertou ESC => sair (opcao=3).");
                    return 3;
                default:
                    LOG.fine("Tecla não mapeada: " + key.getKeyType());
                    break;
            }
        }
        return 3; 
    }

    public String exibirMenuCorJogador1() {
        String[] opcoes = { "Azul", "Vermelho" };
        int indice = 0;
        boolean continuar = true;

        while (continuar) {
            view.limparTela();
            desenharEscolha(); 
            view.desenharTextoCentralizado("Faça sua escolha, Jogador 1:", 20);

            for (int i = 0; i < opcoes.length; i++) {
                String prefix = (i == indice) ? "> " : "  ";
                String text = prefix + opcoes[i];
                int y = 22 + i;
                view.desenharTextoCentralizado(text, y);
            }

            view.refresh();
            KeyStroke key = view.esperarTecla();

            switch (key.getKeyType()) {
                case ArrowUp:
                    if (indice > 0) indice--;
                    break;
                case ArrowDown:
                    if (indice < opcoes.length - 1) indice++;
                    break;
                case Enter:
                    LOG.info("MenuCorJogador1: usuário escolheu " + opcoes[indice]);
                    return opcoes[indice];
                case Escape:
                    LOG.info("MenuCorJogador1: usuário apertou ESC => null.");
                    return null;
                default:
                    LOG.fine("Tecla não mapeada: " + key.getKeyType());
                    break;
            }
        }
        return null; 
    }

    private void desenharTitulo() {
        String[] asciiTitle = {
            "██████╗  █████╗ ███╗   ███╗ █████╗ ███████╗"
            , "██╔══██╗██╔══██╗████╗ ████║██╔══██╗██╔════╝"
            , "██║  ██║███████║██╔████╔██║███████║███████╗"
            , "██║  ██║██╔══██║██║╚██╔╝██║██╔══██║╚════██║"
            , "██████╔╝██║  ██║██║ ╚═╝ ██║██║  ██║███████║"
            , "╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝"
            , "                                           "
        };
        int startY = 2; 
        for (int i = 0; i < asciiTitle.length; i++) {
            view.desenharTextoCentralizado(asciiTitle[i], startY + i);
        }
    }
    private void desenharEscolha() {
        String[] asciiArt = {"⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠉⠉⠉⠀⠀⠉⠙⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠛⠛⠉⠉⠉⠉⠙⠛⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠉⠀⠰⣏⣤⣤⣆⣠⣀⣀⣀⣀⡉⠛⣿⣿⣿⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⠋⠀⢲⢢⢤⣀⡀⠀⠀⠀⠀⠀⠀⠈⢻⣿⣿⣿⣿⣿⡟⠀⠀⠀⠀⠘⣿⣾⡝⠻⢷⠿⠟⡿⠋⠀⠘⣿⣿⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⠟⠋⠀⠠⣔⣮⣭⠉⠙⠉⢁⡥⠞⠀⠀⠀⠀⠈⣿⣿⣿⣿⣿⡇⠂⠀⠀⠒⢄⠈⠉⠿⣧⡄⠲⠟⣿⣶⣤⡄⠈⠻⣿⣿⣿⣿\n"
        		, "⣿⣿⣋⣥⣤⣲⣼⣽⣿⣿⠗⣦⣄⡴⠋⠔⠁⠀⠀⠀⠀⠀⢸⣿⣿⣿⣿⣿⡤⠀⠀⠀⠀⠉⠈⢀⠹⠿⡟⠒⠛⢿⣿⣾⣤⠄⠙⢿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⣿⣿⠏⠉⠀⠀⠀⡰⠀⢩⠂⠀⣆⠀⠀⣸⣿⣿⣿⣿⣿⣷⡤⠀⠒⠢⡄⢄⠀⠡⣄⠑⣤⡄⠀⠙⢻⣿⣿⣿⣾⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⣿⣃⡀⠈⣷⡆⢠⣅⣶⠀⢤⣿⠁⠀⣸⣿⣿⣿⣿⣿⣿⣿⣿⣷⡀⠀⠹⣀⠈⠻⣿⣽⡛⢿⣧⠀⠀⠻⣿⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⠇⠀⠀⣽⡟⢠⣿⡿⠀⠀⡼⠤⢄⣰⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡄⠀⠙⣆⠁⠨⠿⣋⠀⠙⢦⠀⠀⢹⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⠠⡀⠀⣿⠃⠾⡿⠃⠀⢠⠃⠀⢨⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣆⢀⣹⡄⠀⠀⠹⡄⠀⠈⢆⠀⢨⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⣧⣄⣤⠃⠀⢸⠁⠀⠀⣾⣀⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣤⣴⣧⣤⣤⣾⣷⣾⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣦⣼⣷⣦⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
        		, "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
        };

        int startY = 3; 
        for (int i = 0; i < asciiArt.length; i++) {
            view.desenharTextoCentralizado(asciiArt[i], startY + i);
        }
    }
}
