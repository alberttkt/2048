package game;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;

import static javafx.beans.binding.Bindings.when;

/**
 * @author Albert Troussard (330361)
 */
public final class Main extends Application {
    private final static int PIECE_SIDE = 520/Board.BOARD_SIZE;
    private final static int INTER_PIECE_SPACE = 40/Board.BOARD_SIZE;
    private BoardBean boardBean = new BoardBean();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Board b = Board.beginRandomBoard();
        boardBean.setPuzzle(b);
       EventHandler<KeyEvent> keyListener = event -> {
           if(event.getCode() == KeyCode.UP ){
               boardBean.setPuzzle(boardBean.getBoard().play(Direction.UP));
           } else if(event.getCode() == KeyCode.DOWN) {
               boardBean.setPuzzle(boardBean.getBoard().play(Direction.DOWN));
           }else if(event.getCode() == KeyCode.LEFT) {
               boardBean.setPuzzle(boardBean.getBoard().play(Direction.LEFT));
           }else if(event.getCode() == KeyCode.RIGHT) {
               boardBean.setPuzzle(boardBean.getBoard().play(Direction.RIGHT));
           }
           event.consume();
       };

        List<Node> piecesNodes = new ArrayList<>();

        for (Piece piece : b.getAllPieces()) {
            Node node = nodeForPiece(piece);
            node.getStyleClass().add("client");
            piecesNodes.add(node);

        }
        layoutBoard(piecesNodes,boardBean.getBoard());
        Text scoretext= new Text();
        scoretext.setText("Score:");
        scoretext.setFont(Font.font(30));
        scoretext.setTranslateX(0);

        Text score = new Text();
        score.textProperty().bind(boardBean.scoreProperty().asString()
        );
        score.setFont(Font.font(30));
        score.setTranslateX(PIECE_SIDE -50);




        Text highScoretext= new Text();
        highScoretext.setText("High Score:");
        highScoretext.setFont(Font.font(30));
        highScoretext.setTranslateX(PIECE_SIDE + 25);

        Text highScore=new Text();
        boardBean.highScoreProperty().addListener((e,o,n)->{
            highScore.setText(n.toString());
        });
        highScore.setFont(Font.font(30));

        highScore.setTranslateX(PIECE_SIDE +190);


        Pane gamePane = new Pane();
        gamePane.getChildren().setAll(piecesNodes);
        double prefSide = PIECE_SIDE * Board.BOARD_SIZE + INTER_PIECE_SPACE * (Board.BOARD_SIZE + 1);
        gamePane.setPrefSize(prefSide, prefSide);

        Group group =new Group(scoretext,score,highScoretext,highScore);


        group.setTranslateX(PIECE_SIDE);
        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("server");
        resetButton.setTranslateX(260);
        resetButton.setTranslateY(-10);
        resetButton.setOnAction(e->{
            boardBean.setPuzzle(Board.beginRandomBoard());
        });
        resetButton.addEventHandler(KeyEvent.KEY_PRESSED,keyListener);

        BorderPane mainPane = new BorderPane(gamePane,group, null, resetButton, null);



        boardBean.boardProperty().addListener((p, o, n) -> {
            piecesNodes.clear();

            for (Piece piece : n.getAllPieces()) {

                Node node = nodeForPiece(piece);
                 node.getStyleClass().add("client");
               piecesNodes.add(node);
            }

            layoutBoard(piecesNodes, n);
            gamePane.getChildren().clear();
            gamePane.getChildren().setAll(piecesNodes);
        });

        Text gameOver = new Text();
        gameOver.setText("GAME OVER");
        gameOver.setFont(Font.font(100));
        gameOver.setTranslateY(250);
        gameOver.disableProperty().bind(boardBean.isEndedProperty());

        mainPane.getChildren().add(gameOver);
        gameOver.visibleProperty().set(false);


mainPane.getStylesheets().add("tooggleButtonStyle.css");
        Scene scene = new Scene(mainPane);
        scene.setFill(Color.LIGHTGRAY);
        scene.addEventHandler(KeyEvent.KEY_PRESSED,keyListener);
        scene.getStylesheets().addAll( "tooggleButtonStyle.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("2048");
        primaryStage.show();
        boardBean.isEndedProperty().addListener((e,o,n)->{
            if (n.booleanValue()==true){
              gameOver.visibleProperty().set(true);
            }else{
                gameOver.visibleProperty().set(false);

            }
        });
    }

    private void layoutBoard(List<Node> nodes, Board board) {
        //Iterator<Piece> pieceIt = board.getAllPieces().iterator();
        for (int r = 0; r < Board.BOARD_SIZE; ++r) {
            int y = INTER_PIECE_SPACE + r * (PIECE_SIDE + INTER_PIECE_SPACE);
            for (int c = 0; c < Board.BOARD_SIZE; ++c) {
                int x = INTER_PIECE_SPACE + c * (PIECE_SIDE + INTER_PIECE_SPACE);

                Node n = nodes.get(r*Board.BOARD_SIZE+c);
                if (n == null) continue;

                n.relocate(x, y);
            }
        }
    }

    private Node nodeForPiece(Piece piece) {
        Rectangle r = new Rectangle(PIECE_SIDE, PIECE_SIDE);
        r.setFill(Color.DEEPSKYBLUE);
        Text t = new Text(0, 0, ""+piece.toString()+"   ");
        switch (piece.getValue()) {
            case 0: r.setVisible(false);
            return r;

            case 2:
                r.setFill(Color.LIGHTSKYBLUE);
                t.setTranslateX(PIECE_SIDE/2-15);
                t.setTranslateY(PIECE_SIDE/2+15);
                break;
            case 4:
                r.setFill(Color.MEDIUMPURPLE);
                t.setTranslateX(PIECE_SIDE/2-15);
                t.setTranslateY(PIECE_SIDE/2+15);
                break;
            case 8:
                r.setFill(Color.LIGHTPINK);
                t.setTranslateX(PIECE_SIDE/2-15);
                t.setTranslateY(PIECE_SIDE/2+15);
                break;
            case 16:
                r.setFill(Color.RED);
                t.setTranslateX(PIECE_SIDE/2-35);
                t.setTranslateY(PIECE_SIDE/2+15);
                break;
            case 32:
                r.setFill(Color.ORANGE);
                t.setTranslateX(PIECE_SIDE/2-35);
                t.setTranslateY(PIECE_SIDE/2+15);
                break;
            case 64:
                r.setFill(Color.LIGHTYELLOW);
                t.setTranslateX(PIECE_SIDE/2-35);
                t.setTranslateY(PIECE_SIDE/2+15);
                break;
            case 128:
                r.setFill(Color.GREENYELLOW);
                t.setTranslateX(PIECE_SIDE/2-55);
                t.setTranslateY(PIECE_SIDE/2+15);
                break;
            case 256:
                r.setFill(Color.PALETURQUOISE);
                t.setTranslateX(PIECE_SIDE/2-55);
                t.setTranslateY(PIECE_SIDE/2+15);
                break;
            case 512:
                r.setFill(Color.LIGHTCYAN);
                t.setTranslateX(PIECE_SIDE/2-55);
                t.setTranslateY(PIECE_SIDE/2+15);
                break;
            case 1024:
                r.setFill(Color.INDIGO);
                t.setTranslateX(-6);
                t.setTranslateY(PIECE_SIDE/2+15);
                break;
            case 2048:
                r.setFill(Color.PURPLE);
                t.setTranslateX(-6);
                t.setTranslateY(PIECE_SIDE/2+10);
                break;
            case 4096:
                r.setFill(Color.AQUA);
                t.setTranslateX(-6);
                t.setTranslateY(PIECE_SIDE/2+10);
                break;
            case 8192:
                r.setFill(Color.BLACK);
                t.setFill(Color.SNOW);
                t.setTranslateX(-6);
                t.setTranslateY(PIECE_SIDE/2+10);
                break;


        }

        /*BoardBean.isSolvedProperty().addListener((p,o,n)->{
            if (n.booleanValue() ==true){
                r.setFill(Color.GREEN);
            }else{
                r.setFill(Color.DEEPSKYBLUE);
            }
        });*/
        t.setFont(Font.font(65));
Group g =new Group(r, t);
        //t.setTextOrigin(VPos.TOP);


        return g;
    }
}