package game;

import java.util.*;
import java.util.function.ToDoubleBiFunction;

/**
 * @author Albert Troussard (330361)
 */
public class Board {
    public final static int BOARD_SIZE = 4;
    public final static Board EMPTY_BOARD = new Board(Collections.nCopies(BOARD_SIZE * BOARD_SIZE, Piece.EMPTY_PIECE), 0);
    private Integer score;
    private final List<Piece> pieces;
    private List<Integer> nonEmptyIndexs;
    private List<Integer> emptyIndexs;
    private int mergeScore;



    public Integer getScore() {
        return score;
    }
    public List<Piece> getAllPieces(){
        return List.copyOf(pieces);
    }

    public boolean isEnd() {
        return end;
    }
    public Piece getPieceAt(int i){
        return pieces.get(i);
    }

    private boolean end = false;

    public Board(List<Piece> pieces, Integer score) {
        if (pieces.size() != BOARD_SIZE * BOARD_SIZE) throw new IllegalArgumentException();
        this.pieces = List.copyOf(pieces);
        this.score = score;
        nonEmptyIndexs = new ArrayList<>();
        emptyIndexs = new ArrayList<>();
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            if (!piece.equals(Piece.EMPTY_PIECE)) nonEmptyIndexs.add(i);
            else emptyIndexs.add(i);
        }

    }

    public static Board beginRandomBoard(){
        return EMPTY_BOARD.addrandom().addrandom();
    }


    public Board play(Direction d){
        List<Direction> possibleMove = movePossible();
        /*if (possibleMove.size()==0) {
            end=true;
            return this;
        }*/
        if(!possibleMove.contains(d)) return this;

        Board interBoard =move(d);

        Board finalBoard= interBoard.addrandom();

        possibleMove = finalBoard.movePossible();

        if(possibleMove.size()==0){
           finalBoard.end=true;
        }

        return finalBoard;

    }

    private Board addrandom(){
        Random r = new Random();
        int random = r.nextInt(emptyIndexs.size());
        int ranValue = r.nextInt(4);
        List<Piece> copy=new ArrayList<>();
       pieces.forEach(p-> copy.add(Piece.copyOf(p)));
        copy.set( emptyIndexs.get(random),new Piece(ranValue==2?4:2));
        return new Board(copy,score);
    }




    // TODO Methode move
    private Board move(Direction direction) {
        List<Piece> newPieces;
        List<List<Piece>> groups = createGroups(direction);
        List<List<Piece>> newGroups = new ArrayList<>();
        groups.forEach(g -> newGroups.add(mergeGroup(g)));
        newPieces = fromGroupToList(newGroups, direction);
        int newScore = score + mergeScore;
        mergeScore = 0;
        return new Board(newPieces, newScore);
    }


    private List<Direction> movePossible() {
        List<Direction> directions = new ArrayList<>();
        Direction.all().forEach(d -> {
            if (!equals(move(d))) directions.add(d);
        });
        return directions;
    }


    private List<Piece> mergeGroup(List<Piece> group) {
        if (group.size() != BOARD_SIZE) throw new IllegalArgumentException();
        List<Piece> interList = new ArrayList<>();
        List<Piece> newPieces = new ArrayList<>();
        int mergeCount = 0;
        for (int i = 0; i < group.size(); i++) {
            Piece p = group.get(i);
            if (!p.equals(Piece.EMPTY_PIECE)) interList.add(Piece.copyOf(p));
        }

        for (int i = 0; i < interList.size() - 1; i++) {
            Piece pI = interList.get(i);
            Piece pI1 = interList.get(i + 1);
            if (pI.equals(pI1)) {
                Piece mergedPiece = pI.merge(pI1);
                newPieces.add(mergedPiece);
                mergeScore += mergedPiece.getValue();
                i++;
                mergeCount++;
            } else {
                newPieces.add(pI);
            }
        }
        if (newPieces.size() + mergeCount != interList.size()) newPieces.add(interList.get(interList.size() - 1));

        if (newPieces.size() != BOARD_SIZE) {
            int a = BOARD_SIZE - newPieces.size();
            for (int i = 0; i < a; i++) {
                newPieces.add(Piece.copyOf(Piece.EMPTY_PIECE));
            }
        }
        return newPieces;
    }


    private List<List<Piece>> createGroups(Direction direction) {
        List<List<Piece>> eleliste = new ArrayList<>();
        switch (direction) {
            case UP:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    List<Piece> sub = new ArrayList<>();
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        sub.add(pieces.get(i + BOARD_SIZE * j));
                    }
                    eleliste.add(sub);
                }
                break;
            case DOWN:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    List<Piece> sub = new ArrayList<>();
                    for (int j = BOARD_SIZE - 1; j >= 0; j--) {
                        sub.add(pieces.get(i + BOARD_SIZE * j));
                    }
                    eleliste.add(sub);
                }
                break;
            case LEFT:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    List<Piece> sub = new ArrayList<>();
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        sub.add(pieces.get(i * BOARD_SIZE + j));
                    }
                    eleliste.add(sub);
                }
                break;
            case RIGHT:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    List<Piece> sub = new ArrayList<>();
                    for (int j = BOARD_SIZE - 1; j >= 0; j--) {
                        sub.add(pieces.get(i * BOARD_SIZE + j));
                    }
                    eleliste.add(sub);
                }
                break;
        }
        return eleliste;
    }

    private List<Piece> fromGroupToList(List<List<Piece>> groups, Direction direction) {
        List<Piece> eleliste = new ArrayList<>();
        switch (direction) {
            case UP:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        eleliste.add(groups.get(j).get(i));
                    }
                }
                break;
            case DOWN:
                for (int i = BOARD_SIZE-1; i >=0; i--) {
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        eleliste.add(groups.get(j).get(i));
                    }
                }
                break;
            case LEFT:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        eleliste.add(groups.get(i).get(j));
                    }
                }
                break;
            case RIGHT:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    for (int j = BOARD_SIZE - 1; j >= 0; j--) {
                        eleliste.add(groups.get(i).get(j));
                    }
                }
                break;
        }
        return eleliste;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return pieces.equals(board.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces);
    }

    private Map<Piece, Boolean> mapcreator() {
        Map<Piece, Boolean> map = new HashMap<>();
        pieces.forEach(e -> map.put(e, false));
        return map;
    }

    private boolean contains(Piece p) {
        return pieces.contains(p);
    }
}
