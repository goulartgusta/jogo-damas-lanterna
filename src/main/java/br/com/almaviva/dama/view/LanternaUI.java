package br.com.almaviva.dama.view;

import java.io.IOException;
import java.util.logging.Logger;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import br.com.almaviva.dama.model.Cor;
import br.com.almaviva.dama.model.Peca;
import br.com.almaviva.dama.model.Tabuleiro;

public class LanternaUI {

    private static final Logger LOG = Logger.getLogger(LanternaUI.class.getName());

    private Screen screen;
    private TextGraphics graphics;

    public LanternaUI() {
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
        try {
            screen.clear();
        } catch (Exception e) {
            LOG.warning("Erro ao limpar tela: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void refresh() {
        try {
            screen.refresh();
        } catch (IOException e) {
            LOG.warning("Erro ao dar refresh na tela: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public KeyStroke esperarTecla() {
        while (true) {
            try {
                KeyStroke keyStroke = screen.pollInput();
                if (keyStroke != null) {
                    LOG.fine("Tecla pressionada: " + keyStroke);
                    return keyStroke;
                }
                Thread.sleep(50);
            } catch (InterruptedException | IOException e) {
                LOG.warning("Erro ao esperar tecla: " + e.getMessage());
                e.printStackTrace();
            }
        }
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
            String espacos = " ".repeat(largura);
            desenharTexto(x, y + row, espacos);
        }
    }

    public int[] desenharTabuleiroCentralizado(Tabuleiro tabuleiro, int cursorLinha, int cursorColuna) {
        TerminalSize size = screen.getTerminalSize();
        int terminalWidth = size.getColumns();
        int terminalHeight = size.getRows();

        int boardWidth = Tabuleiro.TAMANHO_TABULEIRO * 2;
        int boardHeight = Tabuleiro.TAMANHO_TABULEIRO;

        int startX = (terminalWidth - boardWidth) / 2;
        int startY = (terminalHeight - boardHeight) / 2;
        if (startX < 0) startX = 0;
        if (startY < 0) startY = 0;

        for (int linha = 0; linha < Tabuleiro.TAMANHO_TABULEIRO; linha++) {
            for (int coluna = 0; coluna < Tabuleiro.TAMANHO_TABULEIRO; coluna++) {
                boolean casaEscura = ((linha + coluna) % 2 == 0);
                if (casaEscura) {
                    graphics.setBackgroundColor(TextColor.ANSI.WHITE);
                    graphics.setForegroundColor(TextColor.ANSI.BLACK);
                } else {
                    graphics.setBackgroundColor(TextColor.ANSI.BLACK);
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);
                }

                if (linha == cursorLinha && coluna == cursorColuna) {
                    graphics.setBackgroundColor(TextColor.ANSI.BLUE);
                    graphics.setForegroundColor(TextColor.ANSI.YELLOW);
                }
                int x = startX + (coluna * 2);
                int y = startY + linha;
                graphics.putString(x, y, "  ");

                Peca peca = tabuleiro.getPeca(linha, coluna);
                if (peca != null) {
                    if (linha == cursorLinha && coluna == cursorColuna) {
                        graphics.setForegroundColor(TextColor.ANSI.YELLOW);
                    } else {
                        if (peca.getCor() == Cor.AZUL) {
                            graphics.setForegroundColor(TextColor.ANSI.BLUE);
                        } else {
                            graphics.setForegroundColor(TextColor.ANSI.RED);
                        }
                    }
                    char simbolo = peca.isDama() ? 'O' : 'o';
                    graphics.putString(x, y, simbolo + " ");
                }
            }
        }
        return new int[] { startX, startY, boardWidth, boardHeight };
    }

    public void desenharTelaComTabuleiro(
            Tabuleiro tabuleiro, 
            int cursorLinha, 
            int cursorColuna, 
            String mensagem) {

        limparTela();
        int[] info = desenharTabuleiroCentralizado(tabuleiro, cursorLinha, cursorColuna);
        int startX = info[0];
        int startY = info[1];
        int boardWidth = info[2];
        int boardHeight = info[3];

        if (mensagem != null && !mensagem.isEmpty()) {
            int avisoY = startY - 2;
            if (avisoY < 0) avisoY = 0;
            limparArea(startX, avisoY, boardWidth, 1);
            graphics.setForegroundColor(TextColor.ANSI.WHITE);
            graphics.setBackgroundColor(TextColor.ANSI.BLACK);
            desenharTextoCentralizado(mensagem, avisoY);
        }
        refresh();
    }

    public void exibirMensagemAbaixo(String mensagem, int posY, int largura) {
        int posX = 0;
        limparArea(posX, posY, largura, 2);
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        desenharTexto(posX, posY, mensagem);
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

        while (true) {
            desenharTelaComTabuleiro(tabuleiro, linha, coluna, mensagem);
            KeyStroke key = esperarTecla();
            if (key.getKeyType() == KeyType.Escape) {
                return null;
            }
            switch (key.getKeyType()) {
                case ArrowUp:
                    if (linha > 0) linha--;
                    break;
                case ArrowDown:
                    if (linha < Tabuleiro.TAMANHO_TABULEIRO - 1) linha++;
                    break;
                case ArrowLeft:
                    if (coluna > 0) coluna--;
                    break;
                case ArrowRight:
                    if (coluna < Tabuleiro.TAMANHO_TABULEIRO - 1) coluna++;
                    break;
                case Enter:
                    return new int[] { linha, coluna };
            }
        }
    }

    public void finalizar() {
        try {
            screen.close();
            LOG.info("Screen finalizada com sucesso.");
        } catch (IOException e) {
            LOG.severe("Erro ao fechar screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
