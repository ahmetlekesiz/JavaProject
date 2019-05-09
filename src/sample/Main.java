package sample;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Main extends Application{

    public static ArrayList<String> level = new ArrayList<String>();
    public static ArrayList<Tile> tiles = new ArrayList<Tile>();
    public static Tile grid[][] = new Tile[4][4];
    int tileCounter = 0;
    String type;
    String prop;
    String imageName;
    double moveX, moveY;
    double releasedX, releasedY;
    int releasedItemI, releasedItemJ;
    double pressedX, pressedY;
    int pressedItemI, pressedItemJ;
    Tile tempObj = new Tile();
    double tempX, tempY;


    private Parent createContent() throws FileNotFoundException {
        Pane root = new Pane();
        root.setPrefSize(320,320);

        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                type = level.get(tileCounter);
                prop = level.get(tileCounter+1);
                imageName = "images/" + type + prop + ".png";
                FileInputStream input = new FileInputStream(imageName);
                Image image = new Image(input);
                ImagePattern image_pattern = new ImagePattern(image);
                Tile tile = new Tile(image_pattern, type, prop);
                tile.setTranslateX(j * 80);
                tile.setTranslateY(i * 80);
                root.getChildren().add(tile);
                tileCounter = tileCounter+2;
                tiles.add(tile);
                grid[i][j] = tile;

                tile.setOnMouseDragged(e ->{
                    if(grid[pressedItemI][pressedItemJ].getTypeProp().equals("EmptyFree") == false && grid[pressedItemI][pressedItemJ].getType().equals("End") == false && grid[pressedItemI][pressedItemJ].getType().equals("PipeStatic") == false && grid[pressedItemI][pressedItemJ].getType().equals("Starter") == false ) {
                        System.out.println(moveX);
                        tile.setTranslateX(e.getSceneX() - moveX);
                        tile.setTranslateY(e.getSceneY() - moveY);

                    }

                  //  System.out.println(tile.getTranslateX());
                });

                tile.setOnMouseReleased(e ->{
                   // System.out.println("x="+e.getSceneX());
                    //System.out.println("y="+e.getSceneY());
                    releasedX = e.getSceneX();
                    releasedY = e.getSceneY();
                    releasedItemJ = (int) (releasedX/80);
                    releasedItemI = (int) (releasedY/80);
                    System.out.println("released i => "+releasedItemI + "released j =>" + releasedItemJ);
                    System.out.println("pressed i => "+pressedItemI + "pressed j =>" + pressedItemJ);
                    if(grid[pressedItemI][pressedItemJ].getTypeProp().equals("EmptyFree") == false && grid[pressedItemI][pressedItemJ].getType().equals("End") == false && grid[pressedItemI][pressedItemJ].getType().equals("PipeStatic") == false && grid[pressedItemI][pressedItemJ].getType().equals("Starter") == false) {
                        if(releasedItemI - pressedItemI <= 1 && releasedItemI - pressedItemI >= -1 && releasedItemJ - pressedItemJ <= 1 && releasedItemJ - pressedItemJ >= -1 &&  grid[releasedItemI][releasedItemJ].getTypeProp().equals("EmptyFree") == true){
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

                    if(grid[1][0].getTypeProp().equals("PipeVertical") == true
                                && grid[2][0].getTypeProp().equals("PipeVertical") == true
                                    && grid[3][0].getTypeProp().equals("Pipe01") == true
                                        && grid[3][1].getTypeProp().equals("PipeHorizontal") == true
                                            && grid[1][0].getTypeProp().equals("PipeVertical") == true){
                        System.out.println("helal amk ilk level bitti");
                        final Circle cPath = new Circle (35, 35, 10);
                        cPath.setFill(Color.YELLOW);

                        Path path = new Path();

                        path.getElements().add(new MoveTo(35f,35f));
                        path.getElements().add(new LineTo(35f,240f));

                        PathTransition pathTransition = new PathTransition();
                        pathTransition.setDuration(Duration.millis(4000));
                        pathTransition.setPath(path);
                        pathTransition.setNode(cPath);
                        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        pathTransition.setCycleCount(Timeline.INDEFINITE);
                        pathTransition.setAutoReverse(true);
                        pathTransition.play();

                        Group group = new Group(cPath);
                        root.getChildren().add(cPath);

                    }

                });


                tile.setOnMousePressed(e->{
                    moveX = e.getX();
                    moveY = e.getY();
                   // System.out.println("x="+e.getSceneX());
                    //System.out.println("y="+e.getSceneY());
                    pressedX = e.getSceneX();
                    pressedY = e.getSceneY();
                    pressedItemJ = (int) (pressedX/80);
                    pressedItemI = (int) (pressedY/80);
                    tempX = grid[pressedItemI][pressedItemJ].getTranslateX();
                    tempY = grid[pressedItemI][pressedItemJ].getTranslateY();

                   // System.out.println("i => "+pressedItemI + " j =>" + pressedItemJ);


                });




            }
        }


      //  grid[1][3].setTranslateX(250);


        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("İmamSon");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
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
            //setAlignment(Pos.CENTER);
            getChildren().addAll(rec);
        }

        public Tile(){

        }

        public String getProp() {
            return prop;
        }

        public void setProp(String prop) {
            this.prop = prop;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeProp() {
            return typeProp;
        }

        public void setTypeProp(String typeProp) {
            this.typeProp = typeProp;
        }
    }

    public static void main(String[] args) throws IOException {
        String fileName = "levels/level1.txt";
        File file = new File(fileName);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String a[];

        String st;
        while ((st = br.readLine()) != null){
            a = st.split(",");
            for(int i=1; i<3; i++){
                level.add(a[i]);
            }


        }


        launch(args);

    }
}
