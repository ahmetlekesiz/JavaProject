package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Main extends Application{

    public static ArrayList<String> level = new ArrayList<String>();
    int tileCounter = 0;
    String type;
    String prop;
    String imageName;
    double moveX, moveY;


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
                Tile tile = new Tile(image_pattern);
                tile.setTranslateX(j * 80);
                tile.setTranslateY(i * 80);
                root.getChildren().add(tile);
                tileCounter = tileCounter+2;



                tile.setOnMouseDragged(e ->{
                    tile.setTranslateX(e.getSceneX() - moveX);
                    tile.setTranslateY(e.getSceneY() - moveY);

                });

                tile.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> System.out.println(event.getSceneX()));


                tile.setOnMousePressed(e->{
                    moveX = e.getX();
                    moveY = e.getY();
                });




            }
        }

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
        public Tile(ImagePattern pattern){
            Rectangle rec = new Rectangle(80,80);
            rec.setFill(null);
            rec.setStroke(Color.BLACK);
            rec.setFill(pattern);
          //  setAlignment(Pos.CENTER);
           getChildren().addAll(rec);
        }
    }

    public static void main(String[] args) throws IOException {
        String fileName = "levels/level3.txt";
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
