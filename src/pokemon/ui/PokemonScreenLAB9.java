package pokemon.ui;

import java.io.File;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
//import pokemon.Cell;
//import pokemon.Empty;
//import pokemon.Map;
//import pokemon.Pokemon;
//import pokemon.Station;
//import pokemon.Wall;

public class PokemonScreenLAB9 extends Application {

	/**
	 * width of the window
	 */
	private static int W = 600;

	/**
	 * height of the window
	 */
	private static int H = 400;

	// this define the size of one CELL
	private static int STEP_SIZE = 40;

	// this are the urls of the images
	private static final String front = new File("icons/front.png").toURI().toString();
	private static final String back = new File("icons/back.png").toURI().toString();
	private static final String left = new File("icons/left.png").toURI().toString();
	private static final String right = new File("icons/right.png").toURI().toString();
	private static final String pikachuPath = new File("icons/pikachu.gif").toURI().toString();
	// pika image is in icons/25.png

	private ImageView avatar;
	private Image avatarImage;
	private ImageView pikachu;
	private Image pikachuImage;

	// these booleans correspond to the key pressed by the user
	boolean goUp, goDown, goRight, goLeft;

	// current position of the avatar
	double currentPosx = 0;
	double currentPosy = 0;

	// previous position of the avatar
	double previousPosx = 0;
	double previousPosy = 0;

	protected boolean stop = false;

	@Override
	public void start(Stage stage) throws Exception {

		// at the beginning lets set the image of the avatar front
		Random random = new Random();
		avatarImage = new Image(front);
		avatar = new ImageView(avatarImage);
		avatar.setFitHeight(STEP_SIZE);
		avatar.setFitWidth(STEP_SIZE);
		avatar.setPreserveRatio(true);
		currentPosx = random.nextInt(W / STEP_SIZE) * STEP_SIZE; // this should be a random position
		currentPosy = random.nextInt(H / STEP_SIZE) * STEP_SIZE; // this should be a random position
		previousPosx = currentPosx;
		previousPosy = currentPosy;

		pikachuImage = new Image(pikachuPath);
		pikachu = new ImageView(pikachuImage);
		pikachu.setFitHeight(STEP_SIZE);
		pikachu.setFitWidth(STEP_SIZE);
		pikachu.setPreserveRatio(true);
		pikachu.setVisible(false);

		Group mapGroup = new Group();
		avatar.relocate(currentPosx, currentPosy);
		pikachu.relocate(currentPosx, currentPosy);
		mapGroup.getChildren().add(avatar);
		mapGroup.getChildren().add(pikachu);

		// create scene with W and H and color of background
		Scene scene = new Scene(mapGroup, W, H, Color.SANDYBROWN);

		// add listener on key pressing
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				boolean isDirectionKeyCode = (event.getCode().equals(KeyCode.UP) || event.getCode().equals(KeyCode.DOWN)
						|| event.getCode().equals(KeyCode.LEFT) || event.getCode().equals(KeyCode.RIGHT));
				if (isDirectionKeyCode){
					if (previousPosx == currentPosx && previousPosy == currentPosy)
						pikachu.setVisible(true);
					previousPosx = currentPosx;
					previousPosy = currentPosy;
				}

				switch (event.getCode()) {
				case UP:
					goUp = true;
					avatar.setImage(new Image(back));
					break;
				case DOWN:
					goDown = true;
					avatar.setImage(new Image(front));
					break;
				case LEFT:
					goLeft = true;
					avatar.setImage(new Image(left));
					break;
				case RIGHT:
					goRight = true;
					avatar.setImage(new Image(right));
					break;
				default:
					break;
				}
			}
		});

		// add listener key released
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP:
					goUp = false;
					break;
				case DOWN:
					goDown = false;
					break;
				case LEFT:
					goLeft = false;
					break;
				case RIGHT:
					goRight = false;
					break;
				default:
					break;
				}
				stop = false;
			}
		});

		stage.setScene(scene);
		stage.show();

		// it will execute this periodically
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (stop)
					return;

				int dx = 0, dy = 0;

				if (goUp) {
					dy -= (STEP_SIZE);
				} else if (goDown) {
					dy += (STEP_SIZE);
				} else if (goRight) {
					dx += (STEP_SIZE);
				} else if (goLeft) {
					dx -= (STEP_SIZE);
				} else {
					// no key was pressed return
					return;
				}
				moveAvatarBy(dx, dy);
			}
		};
		// start the timer
		timer.start();
	}

	private void moveAvatarBy(int dx, int dy) {
		final double cx = avatar.getBoundsInLocal().getWidth() / 2;
		final double cy = avatar.getBoundsInLocal().getHeight() / 2;
		double x = cx + avatar.getLayoutX() + dx;
		double y = cy + avatar.getLayoutY() + dy;
		moveAvatar(x, y);
	}

	private void moveAvatar(double x, double y) {
		final double cx = avatar.getBoundsInLocal().getWidth() / 2;
		final double cy = avatar.getBoundsInLocal().getHeight() / 2;

		if (x - cx >= 0 && x + cx <= W && y - cy >= 0 && y + cy <= H) {
			// relocate ImageView avatar
			avatar.relocate(x - cx, y - cy);
			// update position
			currentPosx = x - cx;
			currentPosy = y - cy;

			//pikachu moves together
			pikachu.relocate(previousPosx, previousPosy);

			// I moved the avatar lets set stop at true and wait user release
			// the key :)
			stop = true;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
