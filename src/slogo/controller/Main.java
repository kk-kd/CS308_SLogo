package slogo.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import slogo.view.ViewScreen;


/**
 *
 */
public class Main extends Application {

    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private Controller controller;
    private ViewScreen viewScreen;

    /**
     * Start the program.
     */
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        viewScreen = new ViewScreen(primaryStage);
        controller = new Controller(viewScreen);
        setTiming();
    }

    private void setTiming() {
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void step(double secondDelay) {
        String inputString = viewScreen.getInputString();
        if (inputString != null) {
            System.out.println(inputString);
        }
        controller.update();
    }
}
