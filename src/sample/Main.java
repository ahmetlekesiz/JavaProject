package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.*;


public class Main extends Application{

    private static String levels[] =  new String[32];
    private static Tile grid[][] = new Tile[4][4];
    private int tileCounter = 0;
    private String type;
    private String prop;
    private String imageName;
    private double moveX, moveY;
    private double releasedX, releasedY;
    private int releasedItemI, releasedItemJ;
    private double pressedX, pressedY;
    private int pressedItemI, pressedItemJ;
    private Tile tempObj = new Tile();
    private double tempX, tempY;
    private static int level = 1;
    AnimationTimer timer;
    AnimationTimer timer2;
    AnimationTimer timer3;
    AnimationTimer timer4;
    AnimationTimer timer1a2;
    Boolean btnClicked = false;

    private Parent createContent() throws IOException {
        System.out.println("level: " + level);
        fileReader();
        System.out.println();
        Pane root = new Pane();
        root.setPrefSize(320,320);
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                type = levels[tileCounter];
                prop = levels[tileCounter+1];
                imageName = "images/" + type + prop + ".png";
                FileInputStream input = new FileInputStream(imageName);
                Image image = new Image(input);
                ImagePattern image_pattern = new ImagePattern(image);
                Tile tile = new Tile(image_pattern, type, prop);
                tile.setTranslateX(j * 80);
                tile.setTranslateY(i * 80);
                root.getChildren().add(tile);
                tileCounter = tileCounter+2;
                grid[i][j] = tile;

                tile.setOnMouseDragged(e ->{
                    if(!grid[pressedItemI][pressedItemJ].getTypeProp().equals("EmptyFree") && !grid[pressedItemI][pressedItemJ].getType().equals("End") && !grid[pressedItemI][pressedItemJ].getType().equals("PipeStatic") && !grid[pressedItemI][pressedItemJ].getType().equals("Starter")) {
                        System.out.println(moveX);
                        tile.toFront();
                        tile.setTranslateX(e.getSceneX() - moveX);
                        tile.setTranslateY(e.getSceneY() - moveY);
                    }
                });

                tile.setOnMouseReleased(e ->{
                    releasedX = e.getSceneX();
                    releasedY = e.getSceneY();
                    releasedItemJ = (int) (releasedX/80);
                    releasedItemI = (int) (releasedY/80);
                    System.out.println("released i => "+releasedItemI + "released j =>" + releasedItemJ);
                    System.out.println("pressed i => "+pressedItemI + "pressed j =>" + pressedItemJ);
                    if(!grid[pressedItemI][pressedItemJ].getTypeProp().equals("EmptyFree") && !grid[pressedItemI][pressedItemJ].getType().equals("End") && !grid[pressedItemI][pressedItemJ].getType().equals("PipeStatic") && !grid[pressedItemI][pressedItemJ].getType().equals("Starter")) {
                        if(releasedItemI - pressedItemI <= 1 && releasedItemI - pressedItemI >= -1 && releasedItemJ - pressedItemJ <= 1 && releasedItemJ - pressedItemJ >= -1 && grid[releasedItemI][releasedItemJ].getTypeProp().equals("EmptyFree")){
                             if(pressedItemI == releasedItemI || pressedItemJ == releasedItemJ){
                                 grid[pressedItemI][pressedItemJ].setTranslateX(grid[releasedItemI][releasedItemJ].getTranslateX());
                                 grid[pressedItemI][pressedItemJ].setTranslateY(grid[releasedItemI][releasedItemJ].getTranslateY());
                                 grid[releasedItemI][releasedItemJ].setTranslateX(tempX);
                                 grid[releasedItemI][releasedItemJ].setTranslateY(tempY);

                                 tempObj = grid[releasedItemI][releasedItemJ];
                                 grid[releasedItemI][releasedItemJ] = grid[pressedItemI][pressedItemJ];
                                 grid[pressedItemI][pressedItemJ] = tempObj;
                             }else{
                                 tile.setTranslateX(tempX);
                                 tile.setTranslateY(tempY);
                             }

                        }else{
                            tile.setTranslateX(tempX);
                            tile.setTranslateY(tempY);
                        }

                    }

                    if((grid[1][0].getTypeProp().equals("PipeVertical") && grid[2][0].getTypeProp().equals("PipeVertical")
                                    && grid[3][0].getTypeProp().equals("Pipe01")
                                        && grid[3][1].getTypeProp().equals("PipeHorizontal")
                                            && grid[1][0].getTypeProp().equals("PipeVertical")) ||
                            (grid[2][0].getTypeProp().equals("Pipe01")
                            && grid[2][1].getTypeProp().equals("PipeHorizontal")
                            && grid[2][2].getTypeProp().equals("PipeHorizontal")
                            && grid[2][3].getTypeProp().equals("Pipe00")) ||
                                    (grid[2][0].getTypeProp().equals("PipeStatic01")
                                    && grid[2][1].getTypeProp().equals("PipeHorizontal")
                                    && grid[2][2].getTypeProp().equals("PipeHorizontal")
                                    && grid[2][3].getTypeProp().equals("Pipe00"))){
                        final Circle cPath = new Circle (35, 35, 10);
                        cPath.setFill(Color.YELLOW);

                        Path path = new Path();
                        if(level<4) {
                            path.getElements().add(new MoveTo(35f, 35f));
                            path.getElements().add(new LineTo(35f, 280f));
                            path.getElements().add(new LineTo(300f, 280f));
                        }else{
                            path.getElements().add(new MoveTo(35f, 35f));
                            path.getElements().add(new LineTo(35f, 180f));
                            path.getElements().add(new LineTo(300f, 180f));
                            path.getElements().add(new LineTo(300f, 105f));

                        }
                        PathTransition pathTransition = new PathTransition();
                        pathTransition.setDuration(Duration.millis(4000));
                        pathTransition.setPath(path);
                        pathTransition.setNode(cPath);
                        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        pathTransition.setCycleCount(1);
                        pathTransition.setAutoReverse(true);

                        pathTransition.play();
                        pathTransition.statusProperty().addListener((obs, oldStatus, newStatus) -> {
                            if (newStatus == Animation.Status.STOPPED) {
                                level++;
                            }
                        });
                        root.getChildren().add(cPath);

                    }
                });



                tile.setOnMousePressed(e->{
                    moveX = e.getX();
                    moveY = e.getY();
                    pressedX = e.getSceneX();
                    pressedY = e.getSceneY();
                    pressedItemJ = (int) (pressedX/80);
                    pressedItemI = (int) (pressedY/80);
                    tempX = grid[pressedItemI][pressedItemJ].getTranslateX();
                    tempY = grid[pressedItemI][pressedItemJ].getTranslateY();
                });
            }
        }
        tileCounter = 0;

        return root;
    }

    private Parent createMiddle() throws IOException {
        Pane root = new Pane();
        root.setPrefSize(320,320);
        //Creating a Text object
        Text text = new Text();
        Button btn = new Button("click me!");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                btnClicked = true;
            }
        });
        //Setting the text to be added.
        text.setText("Hello how are you");

        //setting the position of the text
        text.setX(50);
        text.setY(50);
        root.getChildren().add(text);
        root.getChildren().add(btn);
        return root;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
            primaryStage.setTitle("Rolling Puzzle Game");
            primaryStage.setScene(new Scene(createContent()));
            primaryStage.setResizable(false);
            primaryStage.show();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(level==2){
                    try {
                        primaryStage.setScene(new Scene(createMiddle()));
                        timer1a2 = new AnimationTimer() {
                            @Override
                            public void handle(long now) {
                                if(btnClicked == true){
                                    try {
                                        primaryStage.setScene(new Scene(createContent()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    timer1a2.stop();
                                }

                            }
                        };
                        timer1a2.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    primaryStage.show();
                    timer.stop();
                       //level 3
                        timer2 = new AnimationTimer() {
                            @Override
                            public void handle(long now) {
                                if(level==3){
                                    try {
                                        primaryStage.setScene(new Scene(createContent()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    primaryStage.show();
                                    timer2.stop();
                                    //level 4
                                    timer3 = new AnimationTimer() {
                                        @Override
                                        public void handle(long now) {
                                            if(level==4){
                                                try {
                                                    primaryStage.setScene(new Scene(createContent()));
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                primaryStage.show();
                                                timer3.stop();
                                                //level 5
                                                timer4 = new AnimationTimer() {
                                                    @Override
                                                    public void handle(long now) {
                                                        if(level==5){
                                                            try {
                                                                primaryStage.setScene(new Scene(createContent()));
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            primaryStage.show();
                                                            timer4.stop();
                                                        }
                                                    }
                                                };
                                                timer4.start();

                                            }

                                        }
                                    };
                                    timer3.start();
                                }

                            }
                        };
                        timer2.start();
                }

            }
        };
        timer.start();
    }

    public void fileReader() throws IOException {
        String fileName = "levels/level"+ level + ".txt";
        File file = new File(fileName);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String a[];

        String st;
        int counter=0;
        while ((st = br.readLine()) != null){
            a = st.split(",");
            for(int i=1; i<3; i++){
                levels[counter] = a[i];
                counter++;
            }
        }
    }

    private class Tile extends StackPane{
        private String type;
        private String prop;
        private String typeProp;

        public Tile(ImagePattern pattern, String type, String prop){
            this.type = type;
            this.prop = prop;
            typeProp = type + prop;
            Rectangle rec = new Rectangle(80,80);
            rec.setFill(null);
            rec.setStroke(Color.BLACK);
            rec.setFill(pattern);
            setAlignment(Pos.CENTER);
            getChildren().addAll(rec);
        }

        public Tile(){}

        public String getType() {
            return type;
        }

        public String getTypeProp() {
            return typeProp;
        }
    }

    public static void main(String[] args) throws IOException {
            launch(args);
    }
}
