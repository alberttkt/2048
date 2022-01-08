package game;

import java.util.Scanner;

/**
 * @author Albert Troussard (330361)
 * @author Ménélik Nouvellon (328132)
 */
public class MainTerminal {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

            Board b1=Board.beginRandomBoard();
            while (!b1.isEnd()){
                System.out.println("score: "+b1.getScore());
                System.out.println(b1.getPieceAt(0)+"|"+b1.getPieceAt(1)+"|"+b1.getPieceAt(2)+"|"+b1.getPieceAt(3));
                System.out.println(b1.getPieceAt(4)+"|"+b1.getPieceAt(5)+"|"+b1.getPieceAt(6)+"|"+b1.getPieceAt(7));
                System.out.println(b1.getPieceAt(8)+"|"+b1.getPieceAt(9)+"|"+b1.getPieceAt(10)+"|"+b1.getPieceAt(11));
                System.out.println(b1.getPieceAt(12)+"|"+b1.getPieceAt(13)+"|"+b1.getPieceAt(14)+"|"+b1.getPieceAt(15));
                String s =scanner.next();
                switch (s){
                    case "w": b1=b1.play(Direction.UP);
                        break;
                    case "s": b1=b1.play(Direction.DOWN);
                        break;
                    case "a": b1=b1.play(Direction.LEFT);
                        break;
                    case "d": b1=b1.play(Direction.RIGHT);
                        break;

                }
            }




    }
}
