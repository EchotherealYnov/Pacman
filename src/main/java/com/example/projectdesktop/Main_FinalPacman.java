package com.example.projectdesktop;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class Main_FinalPacman extends Application {

    static final int WIDTH = 1000;
    static final int HEIGHT = 800;

    static int scoreint = 0;

    List<Circle> listGhost;
    Circle pacman;
    List<Circle> listPoint;
    Text score;

    Button buttonPause;
    Scene sceneGame;
    Group groupGame;
    Stage primaryStage;
    Scene sceneMenu;
    Timeline tl;
    boolean gamePaused = true;
    Button buttonGoToHighScorePage;
    Scene sceneHighScorePage;
    Group groupHighScorePage;
    Button buttonStart;
    Button buttonReturnToMenu;
    Text highscores;
    TextArea saisiePseudo;
    String pseudoPlayer;

    HashMap<String, Integer> highScores;
    TableView tableHighScore;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Pacman");

        //SCENE MENU DEFINITION
        Group groupMenu = new Group();
        buttonStart = new Button("Start The GAME!");
        buttonGoToHighScorePage = new Button("Go to highscore!");
        saisiePseudo = new TextArea();
        saisiePseudo.setPrefHeight(100);
        saisiePseudo.setPrefWidth(100);

        groupMenu.setLayoutX(WIDTH/2);
        groupMenu.setLayoutY(HEIGHT/2);
        VBox vbox = new VBox(buttonStart, buttonGoToHighScorePage, saisiePseudo);
        groupMenu.getChildren().add(vbox);


        sceneMenu = new Scene(groupMenu, WIDTH, HEIGHT, Color.BLACK);
        primaryStage.setScene(sceneMenu);
        //END SCENE MENU DEFINITION

        //SCENE GAME DEFINITION
        Rectangle rContour = new Rectangle(0,0 ,WIDTH,HEIGHT);
        rContour.setFill(Color.TRANSPARENT);
        rContour.setStroke(Color.BLUE);
        rContour.setStrokeWidth(10);

        groupGame = initializeGroupGame();
        sceneGame = new Scene(groupGame, WIDTH, HEIGHT, Color.BLACK);
        //primaryStage.setScene(sceneGame);

        tl = new Timeline(new KeyFrame(Duration.millis(250), e -> run()));
        tl.setCycleCount(Timeline.INDEFINITE);

        handleGameEvent();
       /* groupMenu.getChildren().add(buttonStart);
        groupMenu.getChildren().add(buttonGoToHighScorePage);
        */

        //HighScorePage
        buttonReturnToMenu = new Button("Return to menu!");
        highscores = new Text();
        //getInfoFromFile(highscores);
        highscores.setX(200);
        highscores.setY(200);
        highscores.setFill(Color.WHITE);
        groupHighScorePage = new Group();
        groupHighScorePage.getChildren().add(buttonReturnToMenu);
        groupHighScorePage.getChildren().add(highscores);
        tableHighScore = new TableView();
        TableColumn col1 = new TableColumn();
        col1.setText("COL1");

        // a reprendre
        TableColumn<String, String> pseudoColumn = new TableColumn<>("Pseudo");
        pseudoColumn.setCellValueFactory(new PropertyValueFactory<>("Pseudo"));
        TableColumn<String, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("Score"));

        tableHighScore.getItems().add(0, "pseudo");
        tableHighScore.getItems().add(1, 34);

        PropertyValueFactory factory = new PropertyValueFactory<>("firstName");
        tableHighScore.getItems().add("string");

        TableColumn col2 = new TableColumn();
        tableHighScore.getColumns().add(col1);
        tableHighScore.getColumns().add(col2);

        groupHighScorePage.getChildren().add(tableHighScore);
        //   VBox vboxButtonMenu = new VBox(buttonReturnToMenu, buttonGoToHighScorePage);
        sceneHighScorePage = new Scene(groupHighScorePage, WIDTH, HEIGHT, Color.BLACK);

        primaryStage.show();

        buttonPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(sceneMenu);
            }
        });

        buttonStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(sceneGame);
                gamePaused = false;
                pseudoPlayer = saisiePseudo.getText();
                tl.play();
            }
        });

        buttonGoToHighScorePage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dataInHashMap();
                primaryStage.setScene(sceneHighScorePage);
            }
        });

        buttonReturnToMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(sceneMenu);
            }
        });
    }

    private void handleGameEvent() {
        sceneGame.setOnKeyPressed((KeyEvent event) -> {
            if (event.getText().isEmpty())
                return;
            char keyEntered = event.getText().toUpperCase().charAt(0);
            //System.out.println(keyEntered);

            boolean isMouvOk = !gamePaused;
            switch (keyEntered){
                case 'Z' :
                    //isMouvOk = true;
                    for (Node node : groupGame.getChildren()) {
                        if( node instanceof Rectangle){
                            Rectangle r = ((Rectangle) node);
                            if((pacman.getCenterX()>= r.getX() && pacman.getCenterX()<=r.getX()+r.getWidth())){
                                if(pacman.getCenterY()-pacman.getRadius() <= r.getY() + r.getHeight() && pacman.getCenterY()>=r.getY()){
                                    isMouvOk = false;
                                }
                            }
                        }
                    }
                    if(isMouvOk){
                        if (pacman.getCenterY() <= 0) {
                            pacman.setCenterY(HEIGHT + pacman.getRadius());
                        }

                        isNextPositionAPoint(groupGame, listPoint, pacman, score);
                        pacman.setCenterY(pacman.getCenterY() - pacman.getRadius());
                    }
                    break;
                case 'S' :
                    //isMouvOk = true;
                    for (Node node : groupGame.getChildren()) {
                        if( node instanceof Rectangle){
                            Rectangle r = ((Rectangle) node);
                            if((pacman.getCenterX()>= r.getX() && pacman.getCenterX()<=r.getX()+r.getWidth())){
                                if(pacman.getCenterY() <= r.getY() + r.getHeight() &&
                                        pacman.getCenterY()+ pacman.getRadius()>=r.getY()){
                                    System.out.println("Bam");
                                    isMouvOk = false;
                                }
                            }
                        }
                    }
                    if(isMouvOk) {
                        if (pacman.getCenterY() >= HEIGHT) {
                            pacman.setCenterY(0 - pacman.getRadius());
                        }
                        isNextPositionAPoint(groupGame, listPoint, pacman, score);
                        pacman.setCenterY(pacman.getCenterY() + pacman.getRadius());
                    }
                    break;
                case 'Q' :
                    //isMouvOk = true;
                    for (Node node : groupGame.getChildren()) {
                        if( node instanceof Rectangle){
                            Rectangle r = ((Rectangle) node);
                            if(pacman.getCenterY() <= r.getY() + r.getHeight() &&
                                    pacman.getCenterY()>=r.getY()){
                                if((pacman.getCenterX()>= r.getX() && pacman.getCenterX()-pacman.getRadius()<=r.getX()+r.getWidth())){
                                    isMouvOk = false;
                                }
                            }
                        }
                    }
                    if(isMouvOk) {
                        if (pacman.getCenterX() <= 0) {
                            pacman.setCenterX(WIDTH + pacman.getRadius());
                        }
                        isNextPositionAPoint(groupGame, listPoint, pacman, score);
                        pacman.setCenterX(pacman.getCenterX() - pacman.getRadius());
                    }
                    break;
                case 'D' :
                    //isMouvOk = true;
                    for (Node node : groupGame.getChildren()) {
                        if( node instanceof Rectangle){
                            Rectangle r = ((Rectangle) node);
                            if(pacman.getCenterY() <= r.getY() + r.getHeight() &&
                                    pacman.getCenterY()>=r.getY()){
                                if((pacman.getCenterX()+pacman.getRadius()>= r.getX() && pacman.getCenterX()-pacman.getRadius()<=r.getX())){
                                    isMouvOk = false;
                                }
                            }
                        }
                    }
                    if(isMouvOk) {
                        if (pacman.getCenterX() >= WIDTH) {
                            pacman.setCenterX(0 - pacman.getRadius());
                        }
                        isNextPositionAPoint(groupGame, listPoint, pacman, score);
                        pacman.setCenterX(pacman.getCenterX() + pacman.getRadius());
                    }
                    break;
                case 'X' :
                    primaryStage.setScene(sceneMenu);
                    break;
                case 'P':
                    if(tl.getStatus() == Animation.Status.RUNNING){
                        tl.pause();
                        gamePaused = true;
                    }
                    else if(tl.getStatus() == Animation.Status.PAUSED){
                        tl.play();
                        gamePaused = false;
                    }

            }
        });
    }

    private Group initializeGroupGame() {
        Group group = new Group();
        group.getChildren().add(createObstacleOnScene(100,100, 100, 100));
        group.getChildren().add(createObstacleOnScene(800,100, 100, 100));
        group.getChildren().add(createObstacleOnScene(100,600, 100, 100));
        group.getChildren().add(createObstacleOnScene(800,600, 100, 100));
        group.getChildren().add(createObstacleOnScene(400, 300, 200, 200));

        buttonPause = new Button("Pause The GAME!");
        group.getChildren().add(buttonPause);

        score = new Text(WIDTH - 50,25, String.valueOf(scoreint));
        score.setFill(Color.WHITE);
        group.getChildren().add(score);

        pacman = new Circle(WIDTH/2,50, 25, Color.YELLOW);
        group.getChildren().add(pacman);

        Circle ghost1 = new Circle(700, 700, 25, Color.RED);
        Circle ghost2 = new Circle(700, 700, 25, Color.RED);
        Circle ghost3 = new Circle(700, 700, 25, Color.RED);
        Circle ghost4 = new Circle(700, 700, 25, Color.RED);
        Circle ghost5 = new Circle(700, 700, 25, Color.RED);

        listGhost = new ArrayList<Circle>();
        listGhost.add(ghost1);
        listGhost.add(ghost2);
        listGhost.add(ghost3);
        listGhost.add(ghost4);
        listGhost.add(ghost5);

        group.getChildren().add(ghost1);
        group.getChildren().add(ghost2);
        group.getChildren().add(ghost3);
        group.getChildren().add(ghost4);
        group.getChildren().add(ghost5);

        listPoint = new ArrayList<Circle>();
        for(int i = 50; i < HEIGHT; i = i + 50){
            for(int j = 50; j < WIDTH; j = j + 50){
                boolean createOk = true;

                for (Node node : group.getChildren()) {
                    if (node instanceof Rectangle) {
                        Rectangle r = ((Rectangle) node);
                        if (j > r.getX() -1 && j < r.getX()+r.getWidth()+1){
                            if  ( i>r.getY()-1 && i<r.getY()+r.getHeight()+1){
                                createOk = false;
                            }
                        }
                    }
                }
                if(createOk){
                    Circle point = new Circle(j, i, 10, Color.GAINSBORO);
                    listPoint.add(point);
                }
            }
        }

        for (Circle p : listPoint) {
            group.getChildren().add(p);
        }
        return group;
    }

    private void run() {
        Random r = new Random();
        for (Circle ghost:listGhost ) {
            boolean chaseOn = false;
            if((Math.abs(ghost.getCenterX() - pacman.getCenterX()) +
                    Math.abs(ghost.getCenterY() - pacman.getCenterY())) < 500) {
                chaseOn = true;
            }

            if(chaseOn){
                double difX = ghost.getCenterX() - pacman.getCenterX();
                double difY = ghost.getCenterY() - pacman.getCenterY();
                if(Math.abs(difX) < Math.abs(difY)){
                    if(difY>0)  ghost.setCenterY(ghost.getCenterY()-25);
                    else ghost.setCenterY(ghost.getCenterY()+25);
                }
                else {
                    if(difX>0)  ghost.setCenterX(ghost.getCenterX()-25);
                    else ghost.setCenterX(ghost.getCenterX()+25);
                }
            }
            else {
                switch (r.nextInt(4)) {
                    case 0:
                        if (ghost.getCenterX() + 25 < WIDTH) {
                            ghost.setCenterX(ghost.getCenterX() + 25);
                        }
                        break;
                    case 1:
                        if (ghost.getCenterX() - 25 > 0) {
                            ghost.setCenterX(ghost.getCenterX() - 25);
                        }
                        break;
                    case 2:
                        if (ghost.getCenterY() - 25 > 0) {
                            ghost.setCenterY(ghost.getCenterY() - 25);
                        }
                        break;
                    case 3:
                        if (ghost.getCenterY() + 25 < HEIGHT) {
                            ghost.setCenterY(ghost.getCenterY() + 25);
                        }
                        break;
                }
            }
            if(ghost.getCenterX()==pacman.getCenterX() && ghost.getCenterY()==pacman.getCenterY()){
                tl.pause();
                gamePaused = true;
                highscores.setText(pseudoPlayer + " " + scoreint);
                primaryStage.setScene(sceneHighScorePage);

                System.out.println("Game Over!");
                //System.exit(0);
            }
        }
    }

    private void isNextPositionAPoint(Group group, List<Circle> listPoint, Circle pacman, Text score) {
        Circle pointTempToRemove = null;
        for (Circle point : listPoint) {
            if(point.getCenterX() == pacman.getCenterX() && point.getCenterY() == pacman.getCenterY()){
                pointTempToRemove = point;
                group.getChildren().remove(point);
                scoreint++;
                score.setText(String.valueOf(scoreint));
            }
        }
        if(pointTempToRemove!=null) {
            listPoint.remove(pointTempToRemove);
        }
    }

    public Rectangle createObstacleOnScene(int x, int y, int width, int heigth){
        Rectangle r = new Rectangle(x, y, width, heigth);
        r.setFill(Color.BLUE);
        return r;
    }

    public boolean isMouvementAllowed(Group group, Circle pacman){
        for (Node node : group.getChildren()) {
            // node.getLayoutY()
        }

        return false;
    }

    /*public void writeHighScoreToFile(){
        JSONObject employeeDetails = new JSONObject();
        employeeDetails.put("firstName", "Lokesh");
        employeeDetails.put("lastName", "Gupta");
        employeeDetails.put("website", "howtodoinjava.com");
        JSONObject employeeObject = new JSONObject();
        employeeObject.put("employee", employeeDetails);
        //Add employees to list
        JSONArray employeeList = new JSONArray();
        employeeList.add(employeeObject);
        employeeList.add(employeeObject2);
        //Write JSON file
        try (FileWriter file = new FileWriter("./highscores.json")) {
            file.write(employeeList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getInfoFromFile(Text highscores) {
        try {
            FileReader fl = new FileReader("./dataHighScore.txt");
            BufferedReader reader = new BufferedReader(fl);
            while(reader.readLine()!=null) {
                highscores.setText(reader.readLine());
            }
            reader.close();
        } catch (IOException e) {
        }
    }*/

    public void dataInHashMap(){
        highScores = new HashMap<String, Integer>();
        highScores.put("Joueur1", 50);
        highScores.put("Joueur2", 40);
        highScores.put("Joueur3", 60);

    }

    public static void main(String[] args) {
        launch(args);
    }

}