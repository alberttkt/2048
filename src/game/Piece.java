package game;

import java.util.Objects;

/**
 * @author Albert Troussard (330361)
 */
public class Piece {
    private final int value;
    public final static Piece EMPTY_PIECE= new Piece(0);

    public Piece(int value) {
        if(value % 2 !=0) throw new IllegalArgumentException();
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public static Piece copyOf(Piece piece){
        return new Piece(piece.getValue());
    }

    public Piece merge(Piece p){
        if(!equals(p)) throw new IllegalArgumentException();
        return new Piece(value+p.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return value == piece.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
