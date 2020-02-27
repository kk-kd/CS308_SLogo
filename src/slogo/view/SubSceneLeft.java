package slogo.view;

import java.util.EnumMap;

import java.util.Queue;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import slogo.controller.listings.MovingObjectProperties;

public class SubSceneLeft extends SubScene {

  private static final int INITIAL_TURTLE_X = 280;
  private static final int INITIAL_TURTLE_Y = 230;
  private final int TURTLE_SIZE = 60; // turtle is 60 px x 60 px

  private ImageView turtle = new ImageView(new Image("file:resources/defaultTurtle.png"));
  private Rectangle rect;
  private Slider slider;
  private TranslateTransition trans;
  private double initialX;
  private double initialY;
  private TextField tf;

  private Path path;
  private Queue<EnumMap<MovingObjectProperties, Object>> queue;


  public SubSceneLeft() {
    root = new Group();
    vBox = new VBox();
    vBox.getStyleClass().add("leftvbox");
    root.getChildren().add(vBox);
    createRectangle();
    createSlider();
    path = new Path();
    root.getChildren().addAll(path);
    root.getChildren().add(createTurtle());
    initialX = 0;
    initialY = 0;

//    TranslateTransition t1 = moveTurtle(50, 50, 135, 5);
//    TranslateTransition t2 = moveTurtle(0, -50, 0, 5);
//    Animation path_animation = clipAnimation(path, 2);
//    path_animation.play();
//    SequentialTransition s = new SequentialTransition(t1, t2);
//    s.play();
  }

  private Animation clipAnimation(Path path, int numSteps) {
    final Pane clip = new Pane();
    path.clipProperty().set(clip);

    final Circle pen = new Circle(0, 0, 2);

    ChangeListener pen_Listener = new ChangeListener() {
      @Override
      public void changed(ObservableValue observableValue, Object o1, Object o2) {
        Circle clip_eraser = new Circle(pen.getTranslateX(), pen.getTranslateY(), pen.getRadius());
        clip.getChildren().add(clip_eraser);
      }
    };

    pen.translateXProperty().addListener(pen_Listener);
    pen.translateYProperty().addListener(pen_Listener);
    //pen.rotateProperty().addListener(pen_Listener);
    PathTransition pathTransition = new PathTransition(Duration.seconds(5 * (numSteps + 1)), path,
        pen);
    pathTransition.setOnFinished(t -> {
      path.setClip(null);
      clip.getChildren().clear();
    });

    return pathTransition;
  }

  private TranslateTransition moveTurtle(double xFinal, double yFinal, double heading,
      int duration) {
//    path.getElements().addAll(
//        new MoveTo(INITIAL_TURTLE_X + 30, INITIAL_TURTLE_Y + 30),
//        new LineTo(INITIAL_TURTLE_X + 50 + 30, INITIAL_TURTLE_Y + 50 + 30),
//        new MoveTo(280 + 50 + 30, 230 + 50 + 30),
//        new LineTo(280 + 50 + 30, 230 + 50 + 30 - 50)
//
//    );
//    path.setFill(null);
//    path.setStroke(Color.BLACK);
//    path.setStrokeWidth(2);
    turtle.setRotate(heading);
    trans = new TranslateTransition(Duration.seconds(duration),
        turtle); // slider.getValue() for Duration
    trans.setFromX(initialX);
    trans.setFromY(initialY);

    trans.setToX(xFinal);
    trans.setToY(yFinal);

    initialX = xFinal;
    initialY = yFinal;

    return trans;
  }

  private ImageView createTurtle() {
    turtle.setX(INITIAL_TURTLE_X);
    turtle.setY(INITIAL_TURTLE_Y);
    return turtle;
  }

  public void setTurtle(Image newTurtle) {
    turtle.setImage(newTurtle);
  }

  private void createRectangle() {
    rect = new Rectangle(ViewScreen.STAGE_WIDTH / 2, ViewScreen.STAGE_HEIGHT / 2,
        SubSceneRight.INITIAL_BACKGROUND_COLOR);
    rect.getStyleClass().add("rectangle");
    vBox.getChildren().add(rect);
  }

  private void createSlider() {
    slider = new Slider(1, 10, 5.5);
    vBox.getChildren().add(slider);
  }

  @Override
  public void update(Queue<EnumMap<MovingObjectProperties, Object>> queue) {
    this.queue = queue;
    recurse();
  }

  private void recurse() {
    if (!queue.isEmpty()) {
      tf.setEditable(false); // .setVisible() will alternatively make it go away
      EnumMap<MovingObjectProperties, Object> movements = queue.remove();
      TranslateTransition t1 = moveTurtle(-1 * (Double) movements.get(MovingObjectProperties.X),
          -1 * (Double) movements.get(MovingObjectProperties.Y),
          (Double) movements.get(MovingObjectProperties.HEADING) - 90, 2);
      t1.play();

      t1.setOnFinished(event -> {

        if (!queue.isEmpty()) {

          EnumMap<MovingObjectProperties, Object> movements1 = queue.remove();
          TranslateTransition t2 = moveTurtle(
              -1 * (Double) movements1.get(MovingObjectProperties.X),
              -1 * (Double) movements1.get(MovingObjectProperties.Y),
              (Double) movements1.get(MovingObjectProperties.HEADING) - 90, 2);
          t2.play();
          t2.setOnFinished(event1 -> recurse());
        }else{
          tf.setEditable(true);
        }
       // tf.setVisible(true);
      });
    }else{
    tf.setEditable(true);
    }
  }

  public void setRectangleColor(Color color) {
    rect.setFill(color);
  }

  public void listenToDisableTextField(TextField tf){
      this.tf = tf;
  }
}
