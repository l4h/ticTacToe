import java.util.Scanner;

public class TicTacToe {

    static int X = 7, Y = 7;
    static char[][] gameField = new char[X][Y];
    static int[] humanTurnsLogX = new int[X];           //count X in specific index
    static int[] humanTurnsLogY = new int[Y];           //count X in specific index
    static int[] compTurnsLogX = new int[X];
    static int[] compTurnsLogY = new int[Y];
    static int xMax = 0;                                //index on X for human
    static int yMax = 0;                                //index on Y for human
    static int xMaxComp = 0;
    static int yMaxComp = 0;
    static boolean[] xBlocked = new boolean[X];         //is line already xBlocked
    static boolean[] yBlocked = new boolean[Y];
    static int EXIT_STATUS;                             //0- continue game, 1 - win human,2- win comp, 3 - TurnExhausted

    static boolean human = true;
    static int diagonalWinHuman;
    static int diagonalWinComp;


    static final char EMPTY_CELL = '*';
    static final char X_SYMBOL = 'X';
    static final char O_SYMBOL = '0';
    static int totalTurns = X * Y;
    static int minTurnsForWinX;
    static int minTurnsForWinY;
    static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {

        startGame();

    }

    private static void startGame() {

        initGameResources();
        // default human=true
        do {

            doTurn();
            checkWin();
            human = !human;
        } while (totalTurns != 0 && EXIT_STATUS==0);
        switch (EXIT_STATUS){
            case 1:
                System.out.println("Поздравляю кожаный мешок в этот раз ты победил!");
                break;
            case 2:
                System.out.println("Skynet wins!!! whoooooohahahahah");
                break;
            case 3:
                System.out.println("Такие партии пустая трата времени");
        }

    }

    /**
     * for init array params 0,0
     *
     * @param human dfgdfg
     * @param x     dfgdfg
     * @param y     dfgdfg
     */
    private static void logTurns(boolean human, int x, int y) {
        if (human) {
            humanTurnsLogX[x] += 1;
            xMax = getIndexForMaxValue(humanTurnsLogX);
            humanTurnsLogY[y] += 1;
            yMax = getIndexForMaxValue(humanTurnsLogY);
            if(x==y || x+y==X-1){
                diagonalWinHuman++;
            }
        } else {
            compTurnsLogX[x] += 1;
            xMaxComp = getIndexForMaxValue(compTurnsLogX);
            compTurnsLogY[y] += 1;
            yMaxComp = getIndexForMaxValue(compTurnsLogY);
            if(x==y || x+y==X-1){
                diagonalWinComp++;
            }
        }


    }

    static int getIndexForMaxValue(int[] Logs) {
        int max = Logs[0];
        int index = 0;
        for (int i = 0; i <= Logs.length - 2; i++) {
            if (max < Logs[i + 1]) {
                max = Logs[i + 1];
                index = i + 1;
            }
        }
        return index;
    }

    //Метод обрабатывает не только победу но и другие причины завершения игры
    private static int checkWin() {
        if (human) {
            if (minTurnsForWinX == humanTurnsLogX[xMax] || minTurnsForWinY == humanTurnsLogY[yMax] || diagonalWinHuman ==X) {
                EXIT_STATUS = 1;
                return 1;


            } else if (totalTurns == 0) {
                EXIT_STATUS=3;
                return 3;
            } else {
                return 0;
            }
        } else {
            if (minTurnsForWinX == compTurnsLogX[xMaxComp] || minTurnsForWinY == compTurnsLogY[yMaxComp] || diagonalWinComp==X) {
                EXIT_STATUS=2;
                return 2;
            } else if (totalTurns == 0) {
                EXIT_STATUS=3;
                return 3;
            } else {
                return 0;
            }
        }

    }




    private static void doTurn() {
        int x = 0, y = 0;

        if (human) {
            System.out.println("Enter x & y throught SPACE (from 1 to " + X + " for X and from 1 to " + Y + " for Y):");//human turn
            do {
                if (sc.hasNextInt()) {
                    x = sc.nextInt() - 1;
                    sc.nextLine();
                    if (sc.hasNextInt()) y = sc.nextInt() - 1;

                }

            } while (!checkTurn(x, y));
            gameField[x][y] = X_SYMBOL;

            printGameField();
            totalTurns--;
            logTurns(true, x, y);
            //add checkWIN

        } else { // computer turn

            System.out.println("\nComputer turn\n");

            SetCell(xMax, yMax);
            printGameField();

        }
    }

    private static boolean isBlocked(int index, boolean xyBlocked[]) {
        return xyBlocked[index];
    }


    private static void SetCell(int x, int y) {

        if (!isBlocked(x, xBlocked)) {
            searchPositionToFill(x, y, 'X');
        } else if (!isBlocked(y, yBlocked)) {
            searchPositionToFill(x, y, 'Y');
        } else {
            FindVictoryPath();
        }

    }

    //Выполняем примитивную блокировку или просто ход
    private static boolean searchPositionToFill(int x, int y, char xy) {

        if (xy == 'Y') {
            for (int i = 0; i < gameField.length; i++) {
                if (gameField[i][y] == EMPTY_CELL) {
                    gameField[i][y] = O_SYMBOL;
                    yBlocked[y] = true;
                    totalTurns--;
                    return true;
                }
            }
            return false;
        } else if (xy == 'X') {
            for (int i = 0; i < gameField[y].length; i++) {
                if (gameField[x][i] == EMPTY_CELL) {
                    gameField[x][i] = O_SYMBOL;
                    xBlocked[x] = true;
                    totalTurns--;
                    return true;
                }
            }
            return false;
        } else {
            //предположим , что здесь происходит поиск хода для победы. По факту же ищем первую свободную яцейку
            for (int i = 0; i < gameField.length; i++) {
                for (int j = 0; j < gameField[yMax].length; j++) {
                    if (gameField[i][j] == EMPTY_CELL) {
                        gameField[i][j] = O_SYMBOL;
                        totalTurns--;
                        xBlocked[i] = true;
                        yBlocked[j] = true;
                        return true;
                    }
                }
            }
            return false;
        }
    }


    private static void FindVictoryPath() {
        if (compTurnsLogX[xMaxComp] > compTurnsLogY[yMaxComp]) {
            if (!searchPositionToFill(xMaxComp, yMaxComp, 'X')) {
                if (!searchPositionToFill(xMaxComp, yMaxComp, 'Y')) {
                    searchPositionToFill(xMaxComp, yMaxComp, 'A');
                }
            }
        } else {
            if (!searchPositionToFill(xMaxComp, yMaxComp, 'Y')) {
                if (!searchPositionToFill(xMaxComp, yMaxComp, 'X')) {
                    searchPositionToFill(xMaxComp, yMaxComp, 'A');
                }
            }
        }

    }



    private static boolean checkTurn(int x, int y) {
        if (x > gameField.length || x < 0 || y > gameField[X - 1].length || y < 0) {
            System.out.println("wrong coordinates. Try again");
            return false;
        } else {
            if (gameField[x][y] == '*') {
                return true;
            } else {
                System.out.println("Cell is already filled out\nTry other coordinates");
                return false;
            }
        }
    }

    private static void printGameField() {

        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                System.out.print(gameField[i][j] + " ");
            }
            System.out.println();
        }

    }

    private static void initGameResources() {
        minTurnsForWinY=X;
        minTurnsForWinX=Y;
        for (int i = 0; i < gameField.length; i++) {

            humanTurnsLogX[i] = 0;
            xBlocked[i] = false;
            if (i < humanTurnsLogY.length) {
                humanTurnsLogY[i] = 0;
                yBlocked[i] = false;
            }
            for (int j = 0; j < gameField[0].length; j++) {

                gameField[i][j] = '*';
            }
        }
    }
}
