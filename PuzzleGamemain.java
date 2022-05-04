import java.util.ArrayList;

public class Main
{
    public static void main(String[] args) 
	{
        
        PuzzleGame b = new PuzzleGame();

        ArrayList<PuzzleGame.Scope> initial = new ArrayList<PuzzleGame.Scope>();

        initial.add(new PuzzleGame.Scope(0,0));
        initial.add(new PuzzleGame.Scope(1,0));
        initial.add(new PuzzleGame.Scope(1,1));
        initial.add(new PuzzleGame.Scope(2,0));
        initial.add(new PuzzleGame.Scope(2,1));

        initial.forEach(begin -> {
            System.out.println("\n--- " + begin + " ---");
            b.initialize(5, begin);
            ArrayList<PuzzleGame.Movement> bs = b.answerway();
            System.out.println();
            b.getLoc(b.game);
            bs.forEach(step -> {
                System.out.println("\n" + step + "\n");
                b.step(step);
                b.getLoc(b.game);
            });
        });
    }
}