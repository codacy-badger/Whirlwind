package logic;

import util.Utility;

import java.util.LinkedList;
import java.util.Queue;

public class Board {
    private Piece[][] board = null;
    private boolean winWhite = false;
    private boolean winBlack = false;
    private boolean[][] visited = null;
    private int FIRST_PIECE = 0;
    private int PLAYER_BLACK = 1;
    private int PLAYER_WHITE = 0;

    /**
     * Construtor vazio. Apenas cria um board [n]x[n] sem pe�as. Hard-coded para
     * criar [14]x[14]
     */
    Board() {
        int n = 14;
        int col;
        int row;
        board = new Piece[n][n];

        for (row = 0; row < board.length; row++)
            for (col = 0; col < board.length; col++)
                board[row][col] = new Piece(row, col);
    }

    /**
     * Construtor de um board quadrado. O board � preenchido pela fun��o
     * auxiliar FillWithPieces().
     *
     * @param n tamanho do lado do tabuleiro
     * @throws IndexOutOfBoundsException se o board tiver uma dimens�o muito pequena ou muito grande
     */
    Board(int n, int boardPicker) throws IndexOutOfBoundsException {
        if (n < 12)
            throw new IndexOutOfBoundsException("Board demasiado pequeno!");
        else if (n > 20)
            throw new IndexOutOfBoundsException("Board demasiado grande!");

        FIRST_PIECE = boardPicker;

        int row;
        int col;
        board = new Piece[n][n];

        for (row = 0; row < board.length; row++)
            for (col = 0; col < board.length; col++)
                board[row][col] = new Piece(row, col);

        fillWithPieces();
    }

    /**
     * @return the board
     */
    public Piece[][] getBoard() {
        return board;
    }

    /**
     * Devolve o tamanho do board
     *
     * @return tamanho do lado do tabuleiro
     */
    public int getSize() {
        return board.length;
    }

    /**
     * Preenche o tabuleiro com pe�as. O board � preenchido da seguinte maneira:
     * Coloca a primeira pe�a. Nessa linha coloca a cada 5 espa�os uma nova
     * pe�a, alternando o jogador, at� n�o ter 5 espa�os entre a pe�a e o fim do
     * tabuleiro. Depois para a pr�xima linha coloca a primeira pe�a duas
     * posi��es � direita da primeira pe�a, preenchendo antecipadamente as
     * posi��es antes dessa pe�a.
     */
    private void fillWithPieces() {
        int col;
        int row;
        int line_start_position = 1; // posi��o da primeira pe�a da linha
        int player = FIRST_PIECE;
        int col_position_checker;
        int aux_pc;
        int col_player_picker;
        int aux_pp;

        for (row = 0; row < board.length; row++) {
            col_player_picker = player;
            col_position_checker = line_start_position;

            // preencher o inicio da linha
            aux_pc = col_position_checker - 5;
            aux_pp = col_player_picker;
            while (aux_pc >= 0) {
                aux_pp ^= 1;
                board[row][aux_pc].setPlayer(aux_pp);
                aux_pc -= 5;
            }

            // preenchimento normal
            for (col = 0; col < board.length; col++) {
                if (col == col_position_checker) {
                    board[row][col].setPlayer(col_player_picker);
                    col_position_checker += 5;

                    col_player_picker ^= 1;
                }
            }

            // preparar a pr�xima linha
            player ^= 1;
            line_start_position += 2;
            if (line_start_position > 13) {
                line_start_position = 0;
                player ^= 1;
            }
        }
    }

    /**
     * Desenha o tabuleiro com as pe�as na consola. Primeiro desenha a linha de
     * caracteres que representam as poss�veis colunas. Depois para cada linha,
     * imprime o caracter que a representa, dependendo se � maior que 9 ou n�o,
     * e depois o conte�do da linha.
     */
    void display() {
        System.out.print("    ");
        Utility.printLineOfChar(board.length);
        System.out.println();
        System.out.print("   ");

        Utility.printDashedLine(board.length);

        for (int row = 0; row < board.length; row++) {
            if (row < 9)
                System.out.print(row + 1 + "  |");
            else
                System.out.print(row + 1 + " |");

            for (int col = 0; col < board.length; col++)
                System.out.print(board[row][col].getSymbol() + " ");

            System.out.println("|");
        }
        System.out.print("   ");

        Utility.printDashedLine(board.length);

        System.out.println();
    }

    /**
     * Calcula quantas pe�as de ambos os jogadores est�o no tabuleiro.
     *
     * @return n�mero de pe�as no tabuleiro
     */
    public int getNumPieces() {
        int numPieces = 0;
        for (Piece[] aBoard : board)
            for (int col = 0; col < board.length; col++)
                if (aBoard[col].getPlayer() != -1)
                    numPieces++;
        return numPieces;
    }

    /**
     * Verifica se a posi��o n�o tem nenhuma pe�a.
     *
     * @param row Linha da posi��o a verificar
     * @param col Coluna da posi��o a verificar
     * @return true se a posi��o ainda n�o tiver pe�a, false se j� tiver
     */
    Boolean checkFreePosition(int row, int col) {
        try {
            if (board[row][col].getPlayer() == -1) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            // System.out.println("Bad coords in checkFreePosition().");
        }
        return false;
    }

    /**
     * Compara o player dos argumentos com o player da pe�a.
     *
     * @param row    Linha da posi��o a verificar
     * @param col    Coluna da posi��o a verificar
     * @param player Jogador a comparar com o dono da pe�a
     * @return true se forem o mesmo player, false se n�o forem ou se a posi��o
     * n�o tiver pe�a
     */
    private Boolean checkOwner(int row, int col, int player) {
        try {
            return board[row][col].getPlayer() == player;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Bad coords in checkOwner().");
        }
        return false;
    }

    /**
     * Verifica se existem pe�as do player nas posi��es � volta de (row,col).
     *
     * @param p Pe�a que se vai verificar se tem pe�as � volta
     * @return true se exister pelo menos um movimento poss�vel, false se n�o
     */
    private Boolean checkHasPlayerNext(Piece p) {
        if ((p.getRow() + 1) < board.length && checkOwner(p.getRow() + 1, p.getCol(), p.getPlayer()))
            return true;
        if ((p.getRow() - 1) >= 0 && checkOwner(p.getRow() - 1, p.getCol(), p.getPlayer()))
            return true;
        if ((p.getCol() + 1) < board.length && checkOwner(p.getRow(), p.getCol() + 1, p.getPlayer()))
            return true;
        if ((p.getCol() - 1) >= 0 && checkOwner(p.getRow(), p.getCol() - 1, p.getPlayer()))
            return true;

        return false;
    }

    /**
     * Verifica se existem posi��es livres nas posi��es � volta de (row,col).
     *
     * @param p Pe�a que se vai verificar se tem posi��es livres � volta
     * @return true se exister pelo menos um movimento poss�vel, false se n�o
     */
    private Boolean checkHasEmptyNext(Piece p) {
        if ((p.getRow() + 1) < board.length && checkFreePosition(p.getRow() + 1, p.getCol()))
            return true;
        if ((p.getRow() - 1) >= 0 && checkFreePosition(p.getRow() - 1, p.getCol()))
            return true;
        if ((p.getCol() + 1) < board.length && checkFreePosition(p.getRow(), p.getCol() + 1))
            return true;
        if ((p.getCol() - 1) >= 0 && checkFreePosition(p.getRow(), p.getCol() - 1))
            return true;

        return false;
    }

    /**
     * Verifica se o movimento � v�lido. O movimento � v�lido quando a posi��o
     * n�o est� j� ocupada e h� uma pe�a do jogador numa casa adjacente �
     * desejada, quer na horizontal, quer na vertical.
     *
     * @param p Pe�a a ser verificada
     * @return true se for v�lido, false se n�o for v�lido
     */
    Boolean checkValidMove(Piece p) {
        if (!checkFreePosition(p.getRow(), p.getCol()))
            return false;

        if (checkHasPlayerNext(p))
            return true;

        System.out.println("Not valid. There isn't a player " + p.getPlayer() + " piece next to (" + (p.getRow() + 1)
                + "," + Utility.itoc(p.getCol()) + ").");
        return false;
    }

    /**
     * Retorna a pe�a na posi��o (row,col).
     *
     * @param row linha desejada
     * @param col coluna desejada
     * @return Piece na posi��o
     */
    Piece getPiece(int row, int col) {
        try {
            return board[row][col];
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Bad coords in getPiece().");
        }
        return null;
    }

    /**
     * Coloca uma pe�a do player na posi��o (row,col). N�o depende das regras do jogo, apenas tem que estar dentro do tabuleiro.
     *
     * @param p Pe�a a colocar
     * @return true se conseguiu colocar, false se n�o conseguiu
     */
    Boolean setPiece(Piece p) {
        try {
            if (checkValidMove(p)) {
                // System.out.println("Peca colocada em (" + (p.getRow()+1) +","
                // + Utility.itoc(p.getCol()) + ")");
                board[p.getRow()][p.getCol()].setPlayer(p.getPlayer());
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Bad coords in setPiece().");
        }
        return false;
    }

    /**
     * Retira a pe�a do tabuleiro. Na pr�tica apenas atribui � pe�a o jogador -1 que representa a aus�ncia de jogador
     *
     * @param row Linha da pe�a a remover
     * @param col Coluna da pe�a a remover
     */
    void removePiece(int row, int col) {
        try {
            board[row][col].resetPlayer();
            // System.out.println("Peca removida de (" + (row+1) +"," +
            // Utility.itoc(col) + ")");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Bad coords in removePiece().");
        }
    }

    /**
     * Coloca uma pe�a do player na posi��o (row,col). N�o depende das regras do jogo, apenas tem que ser uma posi��o livre.
     *
     * @param p Pe�a a colocar
     * @return true se conseguiu colocar, false se n�o conseguiu
     */
    Boolean setPieceAbs(Piece p) {
        try {
            if (checkFreePosition(p.getRow(), p.getCol())) {
                board[p.getRow()][p.getCol()].setPlayer(p.getPlayer());
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Bad coords in setPieceAbs().");
        }
        return false;
    }

    /**
     * Devolve todas as pe�as do player presentes no board.
     *
     * @param player Jogador de que se quer obter as pe�as
     * @return Queue<Piece> Fila de todas as pe�as do player
     */
    Queue<Piece> getPlayerPieces(int player) {
        Queue<Piece> player_pieces = new LinkedList<>();

        for (Piece[] col : board)
            for (Piece p : col)
                if (p.getPlayer() == player)
                    player_pieces.add(p);

        return player_pieces;
    }

    /**
     * Devolve todas as pe�as do player presentes no board que t�m posi��es livres ortogonalmente.
     *
     * @param player Jogador de que se quer obter as pe�as
     * @return Queue<Piece> Fila de todas as pe�as do player com posi��es livres
     */
    Queue<Piece> getPlayerPiecesWithPossibleMovements(int player) {
        Queue<Piece> player_pieces_with_movements = new LinkedList<>();
        Queue<Piece> player_pieces = getPlayerPieces(player);

        while (!player_pieces.isEmpty()) {
            Piece piece_to_check = player_pieces.remove();

            if (checkHasEmptyNext(piece_to_check)) {
                player_pieces_with_movements.add(piece_to_check);
            }
        }

        System.out.println("----------> --__-- -> " + player_pieces_with_movements.size());

        return player_pieces_with_movements;
    }

    /**
     * Verifica se o player Branco ganhou o jogo. Procura a primeira pe�a Branca
     * ao longo da linha 0 se n�o existir sabemos que � impossivel ter ganho, se
     * encontrar usa o {@link #auxwinnerWhite(int row, int col)
     * auxwinnerWhite} para percorrer todas os locais � volta terminando com
     * falso se n�o conseguir chegar ao outro extremo e mudando o estado do jogo
     * para vit�ria se pelo contr�rio atingiu o outro extremo.
     *
     * @return true se fez a linha, false se n�o
     */
    Boolean winnerWhite() {
        visited = new boolean[board.length][board.length];
        winWhite = false;

        for (int row = 0; row < board.length; row++)
            if (board[row][0].getPlayer() == PLAYER_WHITE && auxwinnerWhite(row, 0) && winWhite)
                return true;

        return false;
    }

    /**
     * Neste caso para o white processa a posi��o atual [row][col] isto �
     * termina com vit�ria se for o extremo certo associado ao jogador ,exemplo
     * especifico,�ltima coluna do tabuleiro, s� tenta processar o local do
     * tabuleiro se l� estiver uma pe�a do jogador e se ainda n�o tiver sido
     * visitado. As posi��es j� visitadas s�o guardadas em visited.
     *
     * @param row linha que est� a tratar neste momento
     * @param col coluna que est� a ser tratado neste momento
     * @return se � promising ou n�o atrav�s de bool, true se continuar, false se n�o for util continuar por este caminho
     */
    private Boolean auxwinnerWhite(int row, int col) {
        if (winWhite)
            return true;

        if (col == getSize() - 1 && board[row][col].getPlayer() == PLAYER_WHITE) {
            winWhite = true;
            return true;
        }

        if (board[row][col].getPlayer() == PLAYER_WHITE && !visited[row][col]) {
            visited[row][col] = true;

            // Verificar posi��es ortogonais
            if (auxwinnerWhiteOrtogonal(row, col) || auxwinnerWhiteDiagonal(row, col))
                return true;
        }

        return false;
    }

    private Boolean auxwinnerWhiteOrtogonal(int row, int col) {
        if (row + 1 < board.length && auxwinnerWhite(row + 1, col))
            return true;
        else if (col + 1 < board.length && auxwinnerWhite(row, col + 1))
            return true;
        else if (row - 1 >= 0 && auxwinnerWhite(row - 1, col))
            return true;
        else if (col - 1 >= 0 && auxwinnerWhite(row, col - 1))
            return true;

        return false;
    }

    private Boolean auxwinnerWhiteDiagonal(int row, int col) {
        if (row + 1 < board.length && col + 1 < board.length && auxwinnerWhite(row + 1, col + 1))
            return true;
        else if (row + 1 < board.length && col - 1 >= 0 && auxwinnerWhite(row + 1, col - 1))
            return true;
        else if (row - 1 >= 0 && col + 1 < board.length && auxwinnerWhite(row - 1, col + 1))
            return true;
        else if (row - 1 >= 0 && col - 1 >= 0 && auxwinnerWhite(row - 1, col - 1))
            return true;

        return false;
    }

    /**
     * Verifica se o player Preto ganhou o jogo. Procura a primeira pe�a Preta
     * ao longo da linha 0, isto � o topo do tabuleiro se n�o existir sabemos
     * que � impossivel ter ganho. Se encontrar usa o
     * {@link #auxwinnerBlack(int row, int col) auxwinnerBlack}
     * para percorrer todos os locais � volta, terminando com falso se n�o
     * conseguir chegar ao outro extremo e mudando o estado do jogo para vit�ria
     * se pelo contr�rio atingiu o outro extremo.
     *
     * @return true se fez a coluna, false se n�o
     */
    Boolean winnerBlack() {
        visited = new boolean[board.length][board.length];
        winBlack = false;

        for (int i = 0; i < board.length; i++)
            if (board[0][i].getPlayer() == PLAYER_BLACK && auxwinnerBlack(0, i) && winBlack) {
                return true;
            }

        return false;
    }

    /**
     * Neste caso para o Black processa a posi��o atual [row][col] isto �
     * termina com vit�ria se for o extremo certo associado ao jogador, exemplo
     * especifico, �ltima linha do tabuleiro, s� tenta processar o local do
     * tabuleiro se l� estiver uma pe�a do jogador e se ainda n�o tiver sido
     * visitado. As posi��es j� visitadas s�o guardadas em visited.
     *
     * @param row linha que est� a tratar neste momento
     * @param col coluna que est� a ser tratado neste momento
     * @return se � promising ou n�o atrav�s de bool, true se continuar, false se n�o for util continuar por este caminho
     */
    private Boolean auxwinnerBlack(int row, int col) {
        if (winBlack)
            return true;

        if (row == getSize() - 1 && board[row][col].getPlayer() == PLAYER_BLACK) {
            winBlack = true;
            return true;
        }

        if (board[row][col].getPlayer() == PLAYER_BLACK && !visited[row][col]) {
            visited[row][col] = true;

            if (auxwinnerBlackOrtogonal(row, col) || auxwinnerBlackDiagonal(row, col))
                return true;
        }

        return false;
    }

    private Boolean auxwinnerBlackOrtogonal(int row, int col) {
        if (row + 1 < board.length && auxwinnerBlack(row + 1, col))
            return true;
        else if (col + 1 < board.length && auxwinnerBlack(row, col + 1))
            return true;
        else if (row - 1 >= 0 && auxwinnerBlack(row - 1, col))
            return true;
        else if (col - 1 >= 0 && auxwinnerBlack(row, col - 1))
            return true;

        return false;
    }

    private Boolean auxwinnerBlackDiagonal(int row, int col) {
        if (row + 1 < board.length && col + 1 < board.length && auxwinnerBlack(row + 1, col + 1))
            return true;
        else if (row + 1 < board.length && col - 1 >= 0 && auxwinnerBlack(row + 1, col - 1))
            return true;
        else if (row - 1 >= 0 && col + 1 < board.length && auxwinnerBlack(row - 1, col + 1))
            return true;
        else if (row - 1 >= 0 && col - 1 >= 0 && auxwinnerBlack(row - 1, col - 1))
            return true;

        return false;
    }
}
