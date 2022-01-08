package game;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Albert Troussard (330361)
 * @author Ménélik Nouvellon (328132)
 */
public class BoardBean{
    private ObjectProperty<Board> board;


    private IntegerProperty highScore;
    private BooleanProperty isEnded;
    private IntegerBinding score;
    private ObservableList<Piece> pieces;

    public int getHighScore() {
        return highScore.get();
    }

    public IntegerProperty highScoreProperty() {
        return highScore;
    }
    public boolean isEnded() {
        return isEnded.get();
    }

    public BooleanProperty isEndedProperty() {
        return isEnded;
    }
    public ObservableList<Piece> getPieces() {
        return pieces;
    }



    public IntegerBinding scoreProperty() {
        return score;
    }



    public BoardBean() {
        highScore=new SimpleIntegerProperty(0);
        this.board = new SimpleObjectProperty<>();
        this.score = Bindings.createIntegerBinding(
                () -> board.get().getScore(),board
        );


        this.pieces = new SimpleListProperty<>(FXCollections.observableArrayList());
        isEnded=new SimpleBooleanProperty();

    }

    public Board getBoard() {
        return board.get();
    }

    public ObjectProperty<Board> boardProperty() {
        return board;
    }

    public void setPuzzle(Board newBoard) {
        board.set(newBoard);
        pieces.setAll(newBoard.getAllPieces());
        this.isEnded.set(newBoard.isEnd());
        if(newBoard.getScore() > highScore.get()){
          highScore.set(newBoard.getScore());
        }
    }


}
