import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final char DOT_HUMAN = 'X';                      // Фишка игрока
    private static final char DOT_AI = '0';                         // Фишка компьютера
    private static final char DOT_EMPTY = '.';                      // Пустая ячейка
    private static final int WIN_COUNT = 4;                         // Выигрышная комбинация
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static char[][] field;                                  // Игровое поле
    private static int fieldSizeX;                                  // Размерность игрового поля по горизонтали
    private static int fieldSizeY;                                  // Размерность игрового поля по вертикали

    /**
     * Главный метод, точка входа в программу
     * @param args
     */
    public static void main(String[] args) {
        while (true) {
            initField();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, WIN_COUNT, "Победил человек!"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, WIN_COUNT, "Победил компьютер"))
                    break;
            }
            System.out.println("Желаете сыграть ещё раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y")) {
                break;
            }
        }
    }

    /**
     * Инициализация игрового поля
     */
    public static void initField() {
        fieldSizeY = 5;
        fieldSizeX =5;

        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    static void printField() {
        // Заголовок игрового поля
        System.out.print("+-");
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print((i + 1) + "-");
        }
        System.out.println();

        // Игровое поле
        for (int y = 0; y < fieldSizeY; y++) {
            System.out.print((y + 1) + "|");
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(field[y][x] + "|");
            }
            System.out.println();
        }

        // Нижняя часть игрового поля
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print("--");
        }
        System.out.println("--");
    }

    /**
     * Ход игрока (человека)
     */
    static void humanTurn() {
        int x;
        int y;

        do {
            System.out.println("Введите координаты X и Y (от 1 до 3)\nчерез пробел: ");
            x = scanner.nextInt() -1;
            y = scanner.nextInt() -1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));

        field[y][x] = DOT_HUMAN;
    }
    
    /**
     * Ход игрока (компьютера)
     */
    public static void aiTurn() {

        //  Проверка - победит ли игрок-компьютер в текущем ходе (при выигрышной комбинации WIN_COUNT)?
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == DOT_EMPTY) {
                    field[y][x] = DOT_AI;
                    if (checkWin(DOT_AI, WIN_COUNT)) return;
                    else field[y][x] = DOT_EMPTY;
                }
            }
        }

        // Проверка - победит ли игрок-человек в текущем ходе (при выигрышной комбинации WIN_COUNT - 1)?
        boolean f = checkWin(DOT_HUMAN, WIN_COUNT - 1);
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == DOT_EMPTY) {
                    field[y][x] = DOT_HUMAN;
                    if (checkWin(DOT_HUMAN, WIN_COUNT - (f ? 0 : 1))) {
                        field[y][x] = DOT_AI;
                        return;
                    } else {
                        field[y][x] = DOT_EMPTY;
                    }
                }
            }
        }

        // Если никто не победит, то компюютер ставит фишку в любую пустую ячейку игрового поля
        int x;
        int y;

        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellEmpty(x, y));

        field[y][x] = DOT_AI;
    }

    /**
     * Метод для проверки, является ли ячейка игрового поля пустой
     * @param x координата ячейки
     * @param y координата ячейки
     * @return результат проверки
     */
    static boolean isCellEmpty(int x, int y) {
        return (field[y][x] == DOT_EMPTY);
    }

    /**
     * Метод для проверки, является ли ячейка игрового поля доступной
     * @param x координата ячейки
     * @param y координата ячейки
     * @return результат проверки
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Метод проверки состояния игры
     * @param dot фишка игрока (человек/компьютер)
     * @param win выигрышная комбинация
     * @param s победный сообщение
     * @return результат проверки игры
     */
    static boolean checkGameState(char dot, int win, String s) {
        if (checkWin(dot, win)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false;
    }

    /**
     * Метод для проверки на ничью
     * @return результат проверки
     */
    static boolean checkDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

    /**
     * Метод для проверки победы игрока (человек/компьютер)
     * @param dot фишка игрока (человек/компюютер)
     * @param winCount количество фишек для победы
     * @return признак победы
     */
    static boolean checkWin(char dot, int winCount) {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == dot) {
                    if (checkXY(y, x, 1, winCount) ||
                        checkXY(y, x, -1, winCount) ||
                        checkDiagonal(y, x, -1, winCount) ||
                        checkDiagonal(y, x, 1, winCount))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Метод для проверки победы игрока (человек/компьютер)
     *      по горизонтали вправо/по вертикали вниз
     * @param x начальная координата фишки
     * @param y начальная координата фишки
     * @param dir направление проверки (-1 => по горизонтали вправо, 1 => по вертикали вниз)
     * @param win выигрышная комбинация
     * @return результат проверки
     */
    static boolean checkXY(int x, int y, int dir, int win) {
        char c = field[x][y];

        for (int i = 1; i < win; i++) {
            if (dir > 0 && (!isCellValid(x + i, y) || c != field[x + i][y])) return false;
            else if (dir < 0 && (!isCellValid(x, y + i) || c != field[x][y + i])) return false;
        }
        return true;
    }

    /**
     * Метод для проверки победы игрока (человек/компьютер)
     *      по диагонали вверх вправо/ вниз вправо
     * @param x начальная координата фишки
     * @param y начальная координата фишки
     * @param dir направление проверки (-1 => по диагонали вверх вправо, 1 => по диагонали вниз вправо)
     * @param win выигрышная комбинация
     * @return результат проверки
     */
    static boolean checkDiagonal(int x, int y, int dir, int win) {
        char c = field[x][y];

        for (int i = 1; i < win; i++) {
            if (!isCellValid(x + i, y + i*dir) || c != field[x + i][y + i*dir]) return false;
        }
        return true;
    }
}