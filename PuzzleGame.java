import java.util.ArrayList;

import static java.lang.Math.*;

public class PuzzleGame {
    boolean[][] game;
    int pegsLeft;
    int atpos;

    public static char Boltochar(Boolean b) {
        return !b ? '.' : 'x';
    }

    public void getloc(boolean[][] game) {
        for (int i = 0; i < atpos; i++) {
            System.out.print("  ");
            for (int k = 0; k < (atpos - i - 1); k++) {
                System.out.print(" ");
            }
            for (int j = 0; j <= i; j++) {
                System.out.print(PuzzleGame.Boltochar(game[i][j]));
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public void initialization(int n, Scope h) {
        atpos = n;
        game = new boolean[n][n];
        pegsLeft = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                game[i][j] = true;
                pegsLeft++;
            }
        }

        game[h.x][h.y] = false;

    }

    public boolean step(Movement m) {
        if (!m.getValidity(this)) {
            System.out.println("Invalid step.");
            return false;
        }

        game[m.start.x][m.start.y] = false;

        game[m.end.x][m.end.y] = true;

        game[(m.start.x + m.end.x) / 2][(m.start.y + m.end.y) / 2] = false;

        pegsLeft--;

        return true;
    }

    public void backstep(Movement m) {

        game[m.start.x][m.start.y] = true;

        game[m.end.x][m.end.y] = false;

        game[(m.start.x + m.end.x) / 2][(m.start.y + m.end.y) / 2] = true;

        pegsLeft++;
    }

    public ArrayList<Movement> loctonext(Scope start) {
        ArrayList<Movement> tentative = new ArrayList<Movement>();

        if (!start.isScopeValid(atpos)) {
            return tentative;
        }

        if (!game[start.x][start.y]) {
            return tentative;
        }

        Movement valid = new Movement(start, new Scope(start.x - 2, start.y));

        if (valid.getValidity(this)) {
            tentative.add(valid);
        }

        valid = new Movement(start, new Scope(start.x - 2, start.y - 2));

        if (valid.getValidity(this)) {
            tentative.add(valid);
        }

        valid = new Movement(start, new Scope(start.x, start.y - 2));

        if (valid.getValidity(this)) {
            tentative.add(valid);
        }

        valid = new Movement(start, new Scope(start.x, start.y + 2));

        if (valid.getValidity(this)) {
            tentative.add(valid);
        }

        valid = new Movement(start, new Scope(start.x + 2, start.y));

        if (valid.getValidity(this)) {
            tentative.add(valid);
        }

        valid = new Movement(start, new Scope(start.x + 2, start.y + 2));

        if (valid.getValidity(this)) {
            tentative.add(valid);
        }

        return tentative;
    }

    public ArrayList<Movement> validSteps() {
        Scope tmpScope;
        ArrayList<Movement> scopeMoves;
        ArrayList<Movement> tmpMoves = new ArrayList<Movement>();

        for (int i = 0; i < atpos; i++) {
            for (int j = 0; j <= i; j++) {
                tmpScope = new Scope(i, j);
                scopeMoves = this.loctonext(tmpScope);
                tmpMoves.addAll(scopeMoves);
            }
        }

        return tmpMoves;
    }

    public ArrayList<Movement> answerway() {
        ArrayList<Movement> p = new ArrayList<Movement>();
        ArrayList<Movement> Movements = this.validSteps();

        if (Movements.isEmpty()) {
            return p;
        }

        for (int i = 0; i < Movements.size(); i++) {
            this.step(Movements.get(i));

            // Win condition
            if (pegsLeft == 1) {
                p.add(Movements.get(i));
                this.backstep(Movements.get(i));

                return p;
            }

            // Recurse
            ArrayList<Movement> movePath = this.answerway();

            if (movePath.size() + 1 > p.size()) {
                p.clear();
                p.add(Movements.get(i));
                p.addAll(movePath);
            }

            this.backstep(Movements.get(i));
        }

        return p;
    }

    public static class Scope {
        int x, y;

        public Scope(int r, int c) {
            this.x = r;
            this.y = c;
        }

        public String toString() {
            return "[" + x + "," + y + "]";
        }

        public boolean isScopeValid(int game_size) {
            return (x >= 0) && (x < game_size) && (y >= 0) && (y <= x);
        }
    }

    public static class Movement {
        Scope start;
        Scope end;

        public Movement(Scope start, Scope end) {
            this.start = start;
            this.end = end;
        }

        public String toString() {
            return "from " + start.toString() + " to " + end.toString();
        }

        public boolean getValidity(PuzzleGame game) {

            if (!start.isScopeValid(game.atpos) || !end.isScopeValid(game.atpos))
                return false;
            if (!game.game[start.x][start.y] || game.game[end.x][end.y]) {
                return false;
            }

            int rowj = abs(start.x - end.x);
            int colj = abs(start.y - end.y);

            if (rowj == 0) {
                if (colj != 2) {
                    return false;
                }
            } else if (rowj == 2) {
                if (colj != 0 && colj != 2) {
                    return false;
                }
            } else {
                return false;
            }

            return game.game[(start.x + end.x) / 2][(start.y + end.y) / 2];
        }
    }
}