package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.*;

import static javafx.scene.media.MediaPlayer.INDEFINITE;


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
    AnimationTimer timer5;
    private Boolean btnClicked = false;
    private int moveCounter = 0;
    private int movesArray[] = new int[5];
    FileInputStream input3;
    FileInputStream input4;
    private int totalMove = 0;

    private Parent createBegin(Stage primaryStage) throws IOException {
        Pane root = new Pane();
        root.setPrefSize(320,320);
        root.setBackground(new Background(new BackgroundFill(Color.rgb(214, 214, 214), CornerRadii.EMPTY, Insets.EMPTY)));
        //Creating a Text object

        Button btn = new Button("Go to Story");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    primaryStage.setScene(new Scene(createStory(primaryStage)));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btn.setLayoutX(110);
        btn.setLayoutY(250);

        try {
            input3 = new FileInputStream("images/starter.gif");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }


        Label lb1 = new Label ("SPIDERMAN SAVING MR. STARK");
        root.getChildren().add(lb1);
        lb1.setTextFill(Color.RED);
        lb1.setTranslateX(50);
        lb1.setTranslateY(20);

        Label lb2 = new Label ("In this game, you will be helping to \nSpiderman for saving Mr. Stark. \nFor that, you need to solve the 5 puzzles,\nGood luck! ");
        root.getChildren().add(lb2);
        lb2.setTextFill(Color.BLACK);
        lb2.setTranslateX(20);
        lb2.setTranslateY(180);

        try {
            input4 = new FileInputStream("images/starter.jpg");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        Image jpg = new Image(input4);
        ImageView starterJPG = new ImageView(jpg);
        starterJPG.setFitHeight(124);
        starterJPG.setFitWidth(220);
        Label lb3 = new Label ("",starterJPG);
        root.getChildren().add(lb3);
        lb3.setTranslateX(40);
        lb3.setTranslateY(40);



        root.getChildren().add(btn);
        return root;
    }

    public void calculateMove(){
        for(int i=0; i<5; i++){
            totalMove = totalMove + movesArray[i];
        }
    }

    private Parent createStory(Stage primaryStage) throws IOException {
        Pane root = new Pane();
        root.setPrefSize(600,350);
        root.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, Insets.EMPTY)));
        //Creating a Text object
        Text text = new Text();
        Button btn = new Button("Start Game");
        Rectangle rec = new Rectangle(450,226);

        String path1 = "sounds/spiderman.mp3";

        Media media = new Media(new File(path1).toURI().toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        new Thread(String.valueOf(mediaPlayer)).start();
        int s = INDEFINITE;
        mediaPlayer.setCycleCount(s);
        mediaPlayer.play();
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setVolume(0.2);
        MediaView mediaView = new MediaView(mediaPlayer);

        FileInputStream input = new FileInputStream("images/story.png");
        Image image = new Image(input);
        ImagePattern pattern = new ImagePattern(image);
        rec.setFill(pattern);
        Path path = new Path();

        path.getElements().add(new MoveTo(300f, 450));
        path.getElements().add(new LineTo(300f, 200f));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(10000));
        pathTransition.setPath(path);
        pathTransition.setNode(rec);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(true);

        pathTransition.play();
      /*  pathTransition.statusProperty().addListener((obs, oldStatus, newStatus) -> {
            if (newStatus == Animation.Status.STOPPED) {
                level++;
            }
        });*/
        root.getChildren().add(rec);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    primaryStage.setScene(new Scene(createContent()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        text.setText("STORY OF SAVING MR. STARK");
        text.setX(200);
        text.setY(30);
        btn.setLayoutX(250);
        btn.setLayoutY(45);

        root.getChildren().add(text);
        root.getChildren().add(btn);
        root.getChildren().add(mediaView);
        return root;
    }

    private Parent createContent() throws IOException {
        System.out.println("level: " + level);
        fileReader();
        Pane root = new Pane();
        Text textArea = new Text();
        root.setPrefSize(320,400);
        root.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));

        Label lb1 = new Label ("Level: " + level + "\n" + "Move Counter: " + moveCounter);
        root.getChildren().add(lb1);
        lb1.setTextFill(Color.WHITE);
        lb1.setTranslateX(100);
        lb1.setTranslateY(340);



        Rectangle circle = new Rectangle(50,50);
        circle.setX(15);
        circle.setY(20);
        FileInputStream input2 = new FileInputStream("images/spider.png");
        Image image2 = new Image(input2);
        ImagePattern imgpat = new ImagePattern(image2);
        circle.setFill(imgpat);
        Group rot = new Group(circle);
        root.getChildren().add(rot);
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

                                 moveCounter++;
                                 lb1.setText("Level: " + level + "\n" + "Move Counter: " + moveCounter);
                                 System.out.println("moveCounter: "+moveCounter);
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
                            (grid[2][0].getTypeProp().equals("PipeStatic01") && grid[2][1].getTypeProp().equals("PipeHorizontal") && grid[2][2].getTypeProp().equals("PipeHorizontal")
                                    && grid[2][3].getTypeProp().equals("Pipe00"))){

                        Rectangle cPath = new Rectangle(50,50);
                        cPath.setX(15);
                        cPath.setY(20);
                        try {
                            input3 = new FileInputStream("images/spider.png");
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        Image image3 = new Image(input3);
                        ImagePattern imgpat2 = new ImagePattern(image3);
                        cPath.setFill(imgpat2);
                        Group rot2 = new Group(cPath);
                        root.getChildren().add(rot2);

                        circle.setVisible(false);
                        Path path = new Path();
                        if(level<4) {
                            ArcTo arcTo = new ArcTo();
                            arcTo.setX(80f);
                            arcTo.setY(280f);
                            arcTo.setRadiusX(40f);
                            arcTo.setRadiusY(40f);

                            path.getElements().add(new MoveTo(40f, 35f));
                            path.getElements().add(new LineTo(40f, 240f));
                            path.getElements().add(arcTo);
                            path.getElements().add(new LineTo(288f, 280f));
                        }else{
                            ArcTo arcTo2 = new ArcTo();
                            arcTo2.setX(80f);
                            arcTo2.setY(200f);
                            arcTo2.setRadiusX(40f);
                            arcTo2.setRadiusY(40f);

                            ArcTo arcTo3 = new ArcTo();
                            arcTo3.setX(280f);
                            arcTo3.setY(160f);
                            arcTo3.setRadiusX(40f);
                            arcTo3.setRadiusY(40f);

                            path.getElements().add(new MoveTo(40f, 35f));
                            path.getElements().add(new LineTo(40f, 160f));
                            path.getElements().add(arcTo2);
                            path.getElements().add(new LineTo(240f, 200f));
                            path.getElements().add(arcTo3);
                            path.getElements().add(new LineTo(280f, 115f));

                        }
                        PathTransition pathTransition = new PathTransition();
                        pathTransition.setDuration(Duration.millis(4000));
                        pathTransition.setPath(path);
                        pathTransition.setNode(cPath);
                       // pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
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
        rot.toFront();
        circle.toFront();

        return root;
    }

    private Parent createMiddle() throws IOException {
        Pane root = new Pane();
        root.setPrefSize(320,240);
        //Creating a Text object
        Text text = new Text();
        Button btn = new Button("Hell yeah!");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                btnClicked = true;
            }
        });

        btn.setLayoutX(110);
        btn.setLayoutY(200);

        try {
            input4 = new FileInputStream("images/nailed.jpg");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        Image jpg = new Image(input4);
        ImageView nailed = new ImageView(jpg);
        nailed.setFitHeight(240);
        nailed.setFitWidth(320);
        Label lb3 = new Label ("",nailed);
        root.getChildren().add(lb3);
        lb3.setTranslateX(0);
        lb3.setTranslateY(0);

        Label lb4 = new Label (level + "");
        root.getChildren().add(lb4);
        lb4.setTranslateX(233);
        lb4.setTranslateY(175);
        lb4.setTextFill(Color.WHITE);

        root.getChildren().add(btn);
        return root;
    }

    private Parent createFinish(Stage primaryStage) throws IOException {
        calculateMove();
        Pane root = new Pane();
        root.setPrefSize(533,320);
        //Creating a Text object
        Text text = new Text();

        try {
            input4 = new FileInputStream("images/finish.jpg");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        Image jpg = new Image(input4);
        ImageView finish = new ImageView(jpg);
        finish.setFitHeight(320);
        finish.setFitWidth(533);
        Label lb3 = new Label ("",finish);
        root.getChildren().add(lb3);
        lb3.setTranslateX(0);
        lb3.setTranslateY(0);




        Button btn = new Button("Exit");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                primaryStage.close();
            }
        });

        text.setText(String.valueOf(totalMove));
        text.setX(290);
        text.setY(260);
        text.setFill(Color.YELLOW);
        text.setStyle("font-size: 20px;");
        btn.setLayoutX(190);
        btn.setLayoutY(270);

        root.getChildren().add(text);
        root.getChildren().add(btn);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
            primaryStage.setTitle("Rolling Puzzle Game");
            primaryStage.setScene(new Scene(createBegin(primaryStage)));
            primaryStage.setResizable(false);

        primaryStage.show();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(level==2){
                    movesArray[level-2] = moveCounter;
                    moveCounter = 0;
                    try {
                        createMiddleScene(primaryStage);
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
                                    movesArray[level-2] = moveCounter;
                                    moveCounter = 0;
                                    try {
                                        createMiddleScene(primaryStage);
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
                                                movesArray[level-2] = moveCounter;
                                                moveCounter = 0;
                                                try {
                                                    createMiddleScene(primaryStage);
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
                                                            movesArray[level-2] = moveCounter;
                                                            moveCounter = 0;
                                                            try {
                                                                createMiddleScene(primaryStage);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            primaryStage.show();
                                                            timer4.stop();
                                                            timer5 = new AnimationTimer() {
                                                                @Override
                                                                public void handle(long now) {
                                                                    if(level==6){
                                                                        movesArray[level-2] = moveCounter;
                                                                        moveCounter = 0;
                                                                        try {
                                                                            createMiddleScene(primaryStage);
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        primaryStage.show();
                                                                        timer5.stop();
                                                                    }
                                                                }
                                                            };
                                                            timer5.start();
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

    public void createMiddleScene(Stage primaryStage) throws IOException {
        if(level<6){
            primaryStage.setScene(new Scene(createMiddle()));
        }else{
            primaryStage.setScene(new Scene(createFinish(primaryStage)));
        }
        timer1a2 = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(btnClicked == true){
                    try {
                        btnClicked = false;
                        primaryStage.setScene(new Scene(createContent()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    timer1a2.stop();
                }

            }
        };
        timer1a2.start();
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
