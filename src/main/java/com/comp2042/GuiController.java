package com.comp2042;

import com.comp2042.db.RecordManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import static com.comp2042.Config.*;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    private static final String MUSIC_FILE = "/music/musicnew.mp3";
    private static final String RECORD_NAME = "h_score";

    private final String[] backgroundClasses = {
            "background-forest",
            "background-desert",
            "background-snow",
            "background-space",
            "background-5",
            "background-6",
            "background-7",
            "background-8",
            "background-9",
    };
    @FXML
    public Slider difficultySlider;
    @FXML
    public Text difficultyText;
    @FXML
    public BorderPane gameBoard;
    @FXML
    private Pane rootPane;
    @FXML
    public Text scoreValue;
    @FXML
    public GridPane nextBrick;
    @FXML
    public ToggleButton pauseButton;
    @FXML
    public VBox helpBox;
    @FXML
    public ToggleButton musicToggleButton;
    @FXML
    public Slider volumeSlider;
    @FXML
    public Label volumeIconLabel;
    @FXML
    public HBox volumeControlBox;
    @FXML
    public ImageView volumeImageView;
    @FXML
    public Text highestScoreValue;
    private MediaPlayer mediaPlayer;
    private boolean isMusicPlaying = false;
    private double lastVolume = 50; // last volume used in player resume

    private final LongProperty highestScore = new SimpleLongProperty();
    private long updateTimestamp;

    private int currentBackgroundIndex = 0;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();
    private final IntegerProperty gameLevel = new SimpleIntegerProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)),
                                EventType.LEFT);
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)),
                                EventType.RIGHT);
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)),
                                EventType.ROTATE);
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }

                if (keyEvent.getCode() == KeyCode.P && isGameOver.getValue() != Boolean.FALSE) {
                    pauseButton.selectedProperty().setValue(!pauseButton.selectedProperty().getValue());
                }
            }
        });
        gameOverPanel.setVisible(false);

        pauseButton.selectedProperty().bindBidirectional(isPause);
        pauseButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.printf("oldVal %s: newVal: %s\n", oldValue, newValue);
                if (isGameOver.getValue() == Boolean.TRUE) {
                    return;
                }
                if (newValue) {
                    timeLine.pause();
                    pauseButton.setText("Resume");
                } else {
                    timeLine.play();
                    pauseButton.setText("Pause");
                }
            }
        });
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
        scoreValue.setEffect(reflection);

        // initialize media player
        initializeMusicPlayer();
        // initialize volume slider
        setupVolumeSlider();
        // hidden slider when game started
        setVolumeControlVisible(false);
        updateDifficultyText((int) difficultySlider.getValue());
        difficultySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateDifficultyText(newVal.intValue());
        });
    }
    private void updateDifficultyText(int level) {
        String text = "S";
        switch (level) {
            case GAME_LEVEL_MEDIUM:
                text = "M";
                gameLevel.setValue(GAME_LEVEL_MEDIUM);
                break;
            case GAME_LEVEL_FAST:
                text = "F";
                gameLevel.setValue(GAME_LEVEL_FAST);
                break;
            default:
                text = "S";
                gameLevel.setValue(GAME_LEVEL_SLOW);
                break;
        };

        //System.out.printf("updateDifficulty gameLevel: %d\n", gameLevel.getValue());
        difficultyText.setText(text);
    }

    private void initializeMusicPlayer() {
        try {
            String musicPath = getClass().getResource(MUSIC_FILE).toString();
            Media media = new Media(musicPath);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // player mode
            mediaPlayer.setVolume(0.5);
        } catch (Exception e) {
            System.err.printf("Can not load music file: %s, error: %s.", MUSIC_FILE, e.getMessage());
            musicToggleButton.setDisable(true);
        }
    }


    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        generatePreviewPanel(brick.getNextBrickData());
        System.out.printf("brick loc: x: %f, y: %f\n", brickPanel.getLayoutX(), brickPanel.getLayoutY());

//        timeLine = new Timeline(new KeyFrame(
//                Duration.millis(600),
//                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
//        ));
//        timeLine.setCycleCount(Timeline.INDEFINITE);
//        timeLine.play();
        initTimeline();

        loadHighScore();
        updateHighestScoreDisplay();
    }
    public void initTimeline() {
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        gameLevel.addListener((obs, oldLevel, newLevel) -> {
            double rate = switch (newLevel.intValue()) {
                case 1 -> 0.5;  // slow
                case 2 -> 1.0;  // medium
                case 3 -> 2.0;  // fast
                default -> 1.0;
            };
            timeLine.setRate(rate);
        });
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            case 8:
                returnPaint = Color.DEEPPINK;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }


    private void refreshBrick(ViewData brick, EventType eventType) {
        if (isPause.getValue() == Boolean.FALSE) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

            if (eventType == EventType.DOWN) {
                // System.out.printf("brick loc in refresh: x: %f, y: %f\n", brickPanel.getLayoutX(), brickPanel.getLayoutY());
            }

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }

            generatePreviewPanel(brick.getNextBrickData());
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData(), event.getEventType());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        scoreValue.textProperty().bind(integerProperty.asString());
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
        stopMusic();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }

    public void switchBackground(ActionEvent actionEvent) {
        // clear all the backgrounds
        for (String bgClass : backgroundClasses) {
            rootPane.getStyleClass().remove(bgClass);
        }

        currentBackgroundIndex = (currentBackgroundIndex + 1) % backgroundClasses.length;
        setBackgroundByClass(backgroundClasses[currentBackgroundIndex]);
    }

    private void setBackgroundByClass(String cssClass) {
        rootPane.getStyleClass().add(cssClass);
    }

    // 设置特定背景
    public void setBackground(String imagePath) {
        String imageUrl = getClass().getResource(imagePath).toExternalForm();

        // 为主 Pane 设置背景
        rootPane.setStyle("-fx-background-image: url('" + imageUrl + "'); " +
                "-fx-background-size: cover; " +
                "-fx-background-position: center; " +
                "-fx-background-repeat: no-repeat;");

        System.out.println("背景已切换到: " + imagePath);
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);

        if (isGameOver.getValue() == Boolean.TRUE) {
            initializeMusicPlayer();
        }
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }

    @FXML
    public void toggleMusic(ActionEvent actionEvent) {
        if (mediaPlayer == null || isGameOver.getValue() == Boolean.TRUE) return;

        if (isMusicPlaying) {
            mediaPlayer.pause();
            musicToggleButton.setText("Music Off");
            setVolumeControlVisible(false);
        } else {
            mediaPlayer.play();
            musicToggleButton.setText("Music On");
            if (volumeSlider.getValue() == 0) {
                volumeSlider.setValue(50);
            }
            setVolumeControlVisible(true);
        }
        isMusicPlaying = !isMusicPlaying;
    }

    private void stopMusic() {
        if (mediaPlayer != null && isMusicPlaying) {
            mediaPlayer.stop();
            isMusicPlaying = false;
            musicToggleButton.setSelected(false);
            musicToggleButton.setText("Music Off");

            setVolumeControlVisible(false);
        }
    }

    private void setupVolumeSlider() {
        // add listener for volume slider
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                double volume = newValue.doubleValue() / 100.0;
                mediaPlayer.setVolume(volume);
                updateVolumeIcon(newValue.doubleValue());
                if (volume > 0.0) {
                    lastVolume = volume;
                }
                System.out.printf("volume listener, old: %f, new %f, last: %f\n",
                        oldValue.doubleValue(), newValue.doubleValue(), lastVolume);
            }
        });

        // add event listener for icon label
        if (volumeIconLabel != null) {
            volumeIconLabel.setOnMouseClicked(event -> {
                toggleMute();
            });
        }
    }

    private void toggleMute() {
        System.out.printf("slider volume: %f, last val: %f\n", getVolume(), lastVolume);
        if (getVolume() > 0) {
            lastVolume = volumeSlider.getValue();
            setVolume(0.0);
        } else {
            volumeSlider.setValue(lastVolume > 0.0 ? lastVolume : 50.0);
        }
    }

    private void setVolumeControlVisible(boolean visible) {
        if (volumeControlBox != null) {
            volumeControlBox.setVisible(visible);
            volumeControlBox.setManaged(visible);
        }
    }

    private void updateVolumeIcon(double volume) {
        String icon = "/icons/volume.png";
        if (volume == 0) {
            icon = "/icons/no-sound.png";
        }

        // update icon
        if (volumeImageView != null && volumeIconLabel != null) {
            volumeImageView.setImage(new Image(icon));
            volumeIconLabel.setVisible(true);
            volumeIconLabel.setManaged(true);
        }
    }

    public void setVolume(double volume) {
        if (volume >= 0.0 && volume <= 100.0) {
            volumeSlider.setValue(volume);
        }
    }

    public double getVolume() {
        return volumeSlider.getValue();
    }

    private void generatePreviewPanel(int[][] nextBrickData) {
        nextBrick.getChildren().clear();
        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                setRectangleData(nextBrickData[i][j], rectangle);
                if (nextBrickData[i][j] != 0) {
                    nextBrick.add(rectangle, j, i);
                }
            }
        }
    }

    private void updateHighestScoreDisplay() {
        highestScoreValue.setText(String.valueOf(highestScore.getValue()));
    }

    private void loadHighScore() {
        long highScore = RecordManager.readLongFromFile(RECORD_NAME);

        highestScore.setValue(highScore);
        updateTimestamp = System.currentTimeMillis();
        System.out.printf("highestScore: %s, timestamp: %s\n", highScore, updateTimestamp);
    }

    public void tryUpdateHighestScore(Score score) {
        if (score.scoreProperty().getValue() <= highestScore.getValue()) {
            return;
        }

        // update the highest score
        highestScore.setValue(score.scoreProperty().getValue());
        updateHighestScoreDisplay();

        long curTime = System.currentTimeMillis();
        //if (curTime - updateTimestamp > 10 * 1000) {
            //System.out.printf("\nhighest score update time: %d ms, val: %d\n",
                    //curTime - updateTimestamp, highestScore.getValue());
            updateTimestamp = curTime;
            RecordManager.saveLongToFile(RECORD_NAME, highestScore.getValue());
        //}

    }
    public IntegerProperty getGameLevel() {
        return gameLevel;
    }
}
