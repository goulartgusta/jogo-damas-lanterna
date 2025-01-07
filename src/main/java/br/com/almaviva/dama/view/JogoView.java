package br.com.almaviva.dama.view;

import java.io.IOException;
import java.util.logging.Logger;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;

public class JogoView {

    private static final Logger LOG = Logger.getLogger(JogoView.class.getName());

    private Screen screen;
    private TextGraphics graphics;

    public JogoView() {
        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            this.screen = factory.createScreen();
            this.screen.startScreen();
            this.graphics = screen.newTextGraphics();
            LOG.info("LanternaUI inicializada com sucesso.");
        } catch (IOException e) {
            LOG.severe("Erro ao iniciar LanternaUI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void limparTela() {
            screen.clear();
    }

    public void refresh() {
        try {
            screen.refresh();
        } catch (IOException e) {
            LOG.warning("Erro ao dar refresh na tela: " + e.getMessage());
        }
    }

    public KeyStroke esperarTecla() {
        KeyStroke keyStroke = null;
        try {
            while (keyStroke == null) {
                keyStroke = screen.pollInput();
                if (keyStroke == null) {
                    Thread.sleep(50);
                }
            }
        } catch (InterruptedException | IOException e) {
            LOG.warning("Erro ao esperar tecla: " + e.getMessage());
        }
        return keyStroke;
    }

    public void desenharTexto(int x, int y, String texto) {
        graphics.putString(x, y, texto);
    }

    public void desenharTextoCentralizado(String texto, int y) {
        TerminalSize size = screen.getTerminalSize();
        int terminalWidth = size.getColumns();
        int x = (terminalWidth - texto.length()) / 2;
        graphics.putString(x, y, texto);
    }

    public void limparArea(int x, int y, int largura, int altura) {
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
        for (int row = 0; row < altura; row++) {
            graphics.putString(x, y + row, " ".repeat(largura));
        }
    }

    public int[] desenharTabuleiroCentralizado(Tabuleiro tabuleiro, int cursorLinha, int cursorColuna) {
        TerminalSize size = screen.getTerminalSize();
        int terminalWidth = size.getColumns();
        int terminalHeight = size.getRows();

        int boardWidth = Tabuleiro.TAMANHO_TABULEIRO * 2;
        int boardHeight = Tabuleiro.TAMANHO_TABULEIRO;

        int startX = Math.max((terminalWidth - boardWidth) / 2, 0);
        int startY = Math.max((terminalHeight - boardHeight) / 2, 0);

        for (int linha = 0; linha < Tabuleiro.TAMANHO_TABULEIRO; linha++) {
            for (int coluna = 0; coluna < Tabuleiro.TAMANHO_TABULEIRO; coluna++) {
                boolean casaEscura = ((linha + coluna) % 2 == 0);
                graphics.setBackgroundColor(casaEscura ? TextColor.ANSI.WHITE : TextColor.ANSI.BLACK);
                graphics.setForegroundColor(casaEscura ? TextColor.ANSI.BLACK : TextColor.ANSI.WHITE);

                if (linha == cursorLinha && coluna == cursorColuna) {
                    graphics.setBackgroundColor(TextColor.ANSI.BLUE);
                    graphics.setForegroundColor(TextColor.ANSI.YELLOW);
                }

                int x = startX + (coluna * 2);
                int y = startY + linha;
                graphics.putString(x, y, "  ");

                Peca peca = tabuleiro.getPeca(linha, coluna);
                if (peca != null) {
                    graphics.setForegroundColor(
                        linha == cursorLinha && coluna == cursorColuna ? TextColor.ANSI.YELLOW :
                        peca.getCor() == Cor.AZUL ? TextColor.ANSI.BLUE : TextColor.ANSI.RED
                    );
                    char simbolo = peca.ehDama() ? 'O' : 'o';
                    graphics.putString(x, y, simbolo + " ");
                }
            }
        }
        return new int[] { startX, startY, boardWidth, boardHeight };
    }

    public void desenharTelaComTabuleiro(Tabuleiro tabuleiro, int cursorLinha, int cursorColuna, String mensagem) {
        limparTela();
        int[] info = desenharTabuleiroCentralizado(tabuleiro, cursorLinha, cursorColuna);
        int startX = info[0];
        int startY = info[1];
        int boardWidth = info[2];

        if (mensagem != null && !mensagem.isEmpty()) {
            int avisoY = Math.max(startY - 2, 0);
            limparArea(startX, avisoY, boardWidth, 1);
            graphics.setForegroundColor(TextColor.ANSI.WHITE);
            graphics.setBackgroundColor(TextColor.ANSI.BLACK);
            desenharTextoCentralizado(mensagem, avisoY);
        }
        refresh();
    }

    public void exibirMensagemAbaixo(String mensagem, int posY, int largura) {
        limparArea(0, posY, largura, 2);
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        desenharTexto(0, posY, mensagem);
        refresh();
    }

    public void exibirMensagemAbaixoEEsperarTecla(String mensagem, int posY, int largura) {
        exibirMensagemAbaixo(mensagem, posY, largura);
        esperarTecla();
    }

    public void exibirMensagemCentralizada(String msg) {
        limparTela();
        desenharTextoCentralizado(msg, screen.getTerminalSize().getRows() / 2);
        refresh();
        esperarTecla();
    }

    public int[] selecionarPosicao(Tabuleiro tabuleiro, String mensagem) {
        int linha = 0;
        int coluna = 0;
        boolean continuar = true;

        do {
            desenharTelaComTabuleiro(tabuleiro, linha, coluna, mensagem);
            KeyStroke key = esperarTecla();

            switch (key.getKeyType()) {
                case ArrowUp -> linha = Math.max(linha - 1, 0);
                case ArrowDown -> linha = Math.min(linha + 1, Tabuleiro.TAMANHO_TABULEIRO - 1);
                case ArrowLeft -> coluna = Math.max(coluna - 1, 0);
                case ArrowRight -> coluna = Math.min(coluna + 1, Tabuleiro.TAMANHO_TABULEIRO - 1);
                case Enter -> continuar = false;
                case Escape -> { return null; }
                default -> LOG.fine("Tecla não mapeada.");
            }
        } while (continuar);

        return new int[] { linha, coluna };
    }

    public void finalizar() {
        try {
            screen.close();
            LOG.info("Screen finalizada com sucesso.");
        } catch (IOException e) {
            LOG.severe("Erro ao fechar screen: " + e.getMessage());
        }
    }
}
