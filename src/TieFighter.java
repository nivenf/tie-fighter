import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.*;

public class TieFighter extends Application {
	private ArrayList<Shape> nodes1;
	private ArrayList<Shape> nodes2;
	private ArrayList<Shape> nodes3;

	private int WINDOW_WIDTH;
	private int WINDOW_HEIGHT;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		WINDOW_HEIGHT = (int) screenSize.getHeight();
		WINDOW_WIDTH = (int) screenSize.getWidth();

		primaryStage.setTitle("Niven's Tie Fighter - Press H for hyperspace");
		primaryStage.setMaximized(true);

		nodes1 = new ArrayList<Shape>();
		nodes2 = new ArrayList<Shape>();
		nodes3 = new ArrayList<Shape>();

		Group root = new Group();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.BLACK);
		createStarfield(nodes1, nodes2, nodes3);

		root.getChildren().addAll(nodes1);
		root.getChildren().addAll(nodes2);
		root.getChildren().addAll(nodes3);

		Image cockpit = new Image(getClass().getResourceAsStream("/cockpit2.png"));
		ImageView cockpit_view = new ImageView(cockpit);
		cockpit_view.setX(-150);
		cockpit_view.setY(50);
		cockpit_view.setScaleX(1.4);
		cockpit_view.setScaleY(1.4);
		cockpit_view.setMouseTransparent(true);
		root.getChildren().add(cockpit_view);
		
		Image cockpit2 = new Image(getClass().getResourceAsStream("/cockpit3.png"));
		ImageView cockpit_view2 = new ImageView(cockpit2);
		cockpit_view2.setX(-150);
		cockpit_view2.setY(50);
		cockpit_view2.setScaleX(1.4);
		cockpit_view2.setScaleY(1.4);
		cockpit_view2.setMouseTransparent(true);
		root.getChildren().add(cockpit_view2);

		Rectangle fade = new Rectangle();
		fade.setFill(Color.BLACK);
		fade.setHeight(2000);
		fade.setWidth(2000);
		root.getChildren().add(fade);
		FadeTransition ft2 = new FadeTransition(Duration.millis(1000), fade);
		ft2.setDelay(Duration.millis(2000));
		ft2.setFromValue(1);
		ft2.setToValue(0);
		ft2.play();

		Font f = null;
		try {
			f = Font.loadFont(new FileInputStream(new File("resources/TIE-Wing.TTF")), 150);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		InnerShadow ds = new InnerShadow();
		ds.setOffsetX(4.0f);
		ds.setOffsetY(4.0f);
		Text happy = new Text(20, 900, "TIE FIGHTER");
		happy.setEffect(ds);
		happy.setFont(f);
		happy.setOpacity(0);
		happy.setFill(Color.YELLOW);
		root.getChildren().add(happy);
		FadeTransition ft = new FadeTransition(Duration.millis(3000), happy);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setCycleCount(2);
		ft.setAutoReverse(true);
		ft.play();

		Text inst = new Text(20, 900, "Click to shoot");
		inst.setEffect(ds);
		inst.setOpacity(0);
		inst.setFill(Color.GOLD);
		inst.setFont(f);
		root.getChildren().add(inst);
		ft = new FadeTransition(Duration.millis(3000), inst);
		ft.setDelay(Duration.millis(6000));
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setCycleCount(2);
		ft.setAutoReverse(true);
		ft.play();

		primaryStage.setScene(scene);
		primaryStage.show();

		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for (Shape c : nodes1) {
					c.setLayoutY(1 - (event.getY() * 0.005));
					c.setLayoutX(1 - (event.getX() * 0.005));
				}
				for (Shape c : nodes2) {
					c.setLayoutY(1 - (event.getY() * 0.01));
					c.setLayoutX(1 - (event.getX() * 0.01));
				}
				for (Shape c : nodes3) {
					c.setLayoutY(1 - (event.getY() * 0.02));
					c.setLayoutX(1 - (event.getX() * 0.02));
				}
				cockpit_view.setLayoutX(1 + (event.getX() * 0.03));
				cockpit_view.setLayoutY(1 + (event.getY() * 0.03));
				
				cockpit_view2.setLayoutX(1 + (event.getX() * 0.08));
				cockpit_view2.setLayoutY(1 + (event.getY() * 0.08));
			}
		});

		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Ellipse bullet = new Ellipse();
				bullet.setCenterX(500);
				bullet.setCenterY(1000);
				bullet.setRadiusX(200);
				bullet.setRadiusY(50);
				bullet.setFill(Color.LIMEGREEN);
				bullet.setStrokeWidth(5);
				double angle = Math.atan2(1000 - event.getY(), 500 - event.getX()) * 180 / Math.PI;
				bullet.setRotate(angle);

				FadeTransition ft = new FadeTransition(Duration.millis(800), bullet);
				ft.setFromValue(1);
				ft.setToValue(0);
				ft.play();

				ScaleTransition st = new ScaleTransition(Duration.millis(1000), bullet);
				st.setByX(-3);
				st.setByY(-3);
				st.play();

				TranslateTransition trans = new TranslateTransition();
				trans.setNode(bullet);
				trans.setByX(event.getX() - 500);
				trans.setByY(event.getY() - 1000);
				trans.play();
				trans.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						root.getChildren().remove(bullet);
					}
				});

				Ellipse bullet2 = new Ellipse();
				bullet2.setCenterX(1420);
				bullet2.setCenterY(1000);
				bullet2.setRadiusX(200);
				bullet2.setRadiusY(50);
				bullet2.setFill(Color.LIMEGREEN);
				bullet2.setStrokeWidth(5);
				double angle2 = Math.atan2(1000 - event.getY(), 1420 - event.getX()) * 180 / Math.PI;
				bullet2.setRotate(angle2);

				FadeTransition ft2 = new FadeTransition(Duration.millis(800), bullet2);
				ft2.setFromValue(1);
				ft2.setToValue(0);
				ft2.play();

				ScaleTransition st2 = new ScaleTransition(Duration.millis(1000), bullet2);
				st2.setByX(-3);
				st2.setByY(-3);
				st2.play();

				TranslateTransition trans2 = new TranslateTransition();
				trans2.setNode(bullet2);
				trans2.setByX(event.getX() - 1420);
				trans2.setByY(event.getY() - 1000);
				trans2.play();

				trans2.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						root.getChildren().remove(bullet2);
					}
				});

				Circle flash = new Circle();
				flash.setFill(Color.WHITE);
				flash.setRadius(1000);
				flash.setLayoutX(870);
				flash.setLayoutY(500);

				root.getChildren().add(bullet);
				root.getChildren().add(bullet2);
				root.getChildren().remove(happy);
				root.getChildren().remove(inst);
				root.getChildren().remove(cockpit_view);
				root.getChildren().add(cockpit_view);
				root.getChildren().remove(cockpit_view2);
				root.getChildren().add(cockpit_view2);
				root.getChildren().add(flash);

				FadeTransition ft3 = new FadeTransition(Duration.millis(200), flash);
				ft3.setFromValue(0.05);
				ft3.setToValue(0);
				ft3.play();
			}
		});

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.H) {
					MediaPlayer mediaPlayer = new MediaPlayer(
							new Media(this.getClass().getResource("hyperspace.mp4").toExternalForm()));
					mediaPlayer.setAutoPlay(true);
					MediaView mv = new MediaView(mediaPlayer);
					root.getChildren().removeAll(nodes1);
					root.getChildren().removeAll(nodes2);
					root.getChildren().removeAll(nodes3);
					root.getChildren().remove(cockpit_view);
					root.getChildren().remove(cockpit_view2);
					root.getChildren().add(mv);
					root.getChildren().add(cockpit_view);
					root.getChildren().add(cockpit_view2);

					mediaPlayer.setOnEndOfMedia(new Runnable() {
						@Override
						public void run() {
							Rectangle fade = new Rectangle();
							fade.setFill(Color.BLACK);
							fade.setHeight(2000);
							fade.setWidth(2000);
							root.getChildren().add(fade);
							root.getChildren().remove(mv);
							root.getChildren().addAll(nodes1);
							root.getChildren().addAll(nodes2);
							root.getChildren().addAll(nodes3);
							root.getChildren().remove(cockpit_view);
							root.getChildren().remove(cockpit_view2);
							randomplanet(root, cockpit_view, cockpit_view2);
						}
					});

				}
			}
		});
	}

	private void randomplanet(Group root, ImageView cockpit_view, ImageView cockpit_view2) {
		double d = Math.random();
		Font f = null;
		try {
			f = Font.loadFont(new FileInputStream(new File("resources/TIE-Wing.TTF")), 150);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		InnerShadow ds = new InnerShadow();
		ds.setOffsetX(4.0f);
		ds.setOffsetY(4.0f);
		
		if(d < 0.2) {
			Image planeti = new Image(getClass().getResourceAsStream("/planets/hoth.png"));
			ImageView planet = new ImageView(planeti);
			planet.setX(450);
			planet.setY(250);
			root.getChildren().add(planet);
			ScaleTransition st = new ScaleTransition(Duration.millis(100), planet);
			st.setByX(.3);
			st.setByY(.3);
			st.play();
			root.getChildren().remove(cockpit_view);
			root.getChildren().remove(cockpit_view2);
			root.getChildren().add(cockpit_view);
			root.getChildren().add(cockpit_view2);
			
			ScaleTransition st2 = new ScaleTransition(Duration.millis(50000), planet);
			st2.setDelay(Duration.millis(100));
			planet.setScaleX(1.3);
			planet.setScaleY(1.3);
			st2.setByX(.8);
			st2.setByY(.8);
			st2.play();
			
			Text info = new Text(20, 900, "HOTH");
			info.setEffect(ds);
			info.setOpacity(0);
			info.setFill(Color.GOLD);
			info.setFont(f);
			FadeTransition ft = new FadeTransition(Duration.millis(3000), info);
			ft.setFromValue(0);
			ft.setToValue(1);
			ft.setCycleCount(2);
			ft.setAutoReverse(true);
			ft.play();
			root.getChildren().add(info);
		}
		else if(d < 0.4) {
			Image planeti = new Image(getClass().getResourceAsStream("/planets/mustafar.png"));
			ImageView planet = new ImageView(planeti);
			planet.setX(950);
			planet.setY(150);
			root.getChildren().add(planet);
			ScaleTransition st = new ScaleTransition(Duration.millis(100), planet);
			st.setByX(.3);
			st.setByY(.3);
			st.play();
			
			ScaleTransition st2 = new ScaleTransition(Duration.millis(50000), planet);
			st2.setDelay(Duration.millis(100));
			planet.setScaleX(1.3);
			planet.setScaleY(1.3);
			st2.setByX(.8);
			st2.setByY(.8);
			st2.play();
			
			root.getChildren().remove(cockpit_view);
			root.getChildren().remove(cockpit_view2);
			root.getChildren().add(cockpit_view);
			root.getChildren().add(cockpit_view2);
			
			Text info = new Text(20, 900, "MUSTAFAR");
			info.setEffect(ds);
			info.setOpacity(0);
			info.setFill(Color.GOLD);
			info.setFont(f);
			FadeTransition ft = new FadeTransition(Duration.millis(3000), info);
			ft.setFromValue(0);
			ft.setToValue(1);
			ft.setCycleCount(2);
			ft.setAutoReverse(true);
			ft.play();
			root.getChildren().add(info);
		}
		else if(d < 0.6) {
			Image planeti = new Image(getClass().getResourceAsStream("/planets/naboo.png"));
			ImageView planet = new ImageView(planeti);
			planet.setX(650);
			planet.setY(150);
			root.getChildren().add(planet);
			ScaleTransition st = new ScaleTransition(Duration.millis(100), planet);
			st.setByX(.3);
			st.setByY(.3);
			st.play();
			
			ScaleTransition st2 = new ScaleTransition(Duration.millis(50000), planet);
			st2.setDelay(Duration.millis(100));
			planet.setScaleX(1.3);
			planet.setScaleY(1.3);
			st2.setByX(.8);
			st2.setByY(.8);
			st2.play();
			
			root.getChildren().remove(cockpit_view);
			root.getChildren().remove(cockpit_view2);
			root.getChildren().add(cockpit_view);
			root.getChildren().add(cockpit_view2);
			
			Text info = new Text(20, 900, "NABOO");
			info.setEffect(ds);
			info.setOpacity(0);
			info.setFill(Color.GOLD);
			info.setFont(f);
			FadeTransition ft = new FadeTransition(Duration.millis(3000), info);
			ft.setFromValue(0);
			ft.setToValue(1);
			ft.setCycleCount(2);
			ft.setAutoReverse(true);
			ft.play();
			root.getChildren().add(info);
		}
		else if(d < 0.8) {
			Image planeti = new Image(getClass().getResourceAsStream("/planets/tatooine.png"));
			ImageView planet = new ImageView(planeti);
			planet.setX(750);
			planet.setY(050);
			planet.setScaleX(0.2);
			planet.setScaleY(0.2);
			root.getChildren().add(planet);
			ScaleTransition st = new ScaleTransition(Duration.millis(100), planet);
			st.setByX(.3);
			st.setByY(.3);
			st.play();
			
			ScaleTransition st2 = new ScaleTransition(Duration.millis(50000), planet);
			st2.setDelay(Duration.millis(100));
			planet.setScaleX(0.5);
			planet.setScaleY(0.5);
			st2.setByX(.8);
			st2.setByY(.8);
			st2.play();
			
			root.getChildren().remove(cockpit_view);
			root.getChildren().remove(cockpit_view2);
			root.getChildren().add(cockpit_view);
			root.getChildren().add(cockpit_view2);
			
			Text info = new Text(20, 900, "TATOOINE");
			info.setEffect(ds);
			info.setOpacity(0);
			info.setFill(Color.GOLD);
			info.setFont(f);
			FadeTransition ft = new FadeTransition(Duration.millis(3000), info);
			ft.setFromValue(0);
			ft.setToValue(1);
			ft.setCycleCount(2);
			ft.setAutoReverse(true);
			ft.play();
			root.getChildren().add(info);
		}
		else {
			Image planeti = new Image(getClass().getResourceAsStream("/planets/kamino.png"));
			ImageView planet = new ImageView(planeti);
			planet.setX(950);
			planet.setY(100);
			root.getChildren().add(planet);
			ScaleTransition st = new ScaleTransition(Duration.millis(100), planet);
			st.setByX(.3);
			st.setByY(.3);
			st.play();
			
			ScaleTransition st2 = new ScaleTransition(Duration.millis(50000), planet);
			st2.setDelay(Duration.millis(100));
			planet.setScaleX(1.3);
			planet.setScaleY(1.3);
			st2.setByX(.8);
			st2.setByY(.8);
			st2.play();
			
			root.getChildren().remove(cockpit_view);
			root.getChildren().remove(cockpit_view2);
			root.getChildren().add(cockpit_view);
			root.getChildren().add(cockpit_view2);
			
			Text info = new Text(20, 900, "KAMINO");
			info.setEffect(ds);
			info.setOpacity(0);
			info.setFill(Color.GOLD);
			info.setFont(f);
			FadeTransition ft = new FadeTransition(Duration.millis(3000), info);
			ft.setFromValue(0);
			ft.setToValue(1);
			ft.setCycleCount(2);
			ft.setAutoReverse(true);
			ft.play();
			root.getChildren().add(info);
		}
	}

	private void createStarfield(ArrayList<Shape> nodes12, ArrayList<Shape> nodes22, ArrayList<Shape> nodes32) {
		Random rn = new Random();
		Circle star;

		for (int x = 0; x < 250; x++) {
			int pos_x = rn.nextInt(WINDOW_WIDTH) + 1;
			int pos_y = rn.nextInt(WINDOW_HEIGHT) + 1;
			star = new Circle();
			star.setCenterX(pos_x);
			star.setCenterY(pos_y);
			double d = Math.random();
			if (d < 0.3)
				star.setFill(Color.WHITE);
			else if (d < 0.6)
				star.setFill(Color.GRAY);
			else
				star.setFill(Color.LIGHTGRAY);
			star.setRadius(0.5);
			nodes1.add(star);
		}

		for (int x = 0; x < 500; x++) {
			int pos_x = rn.nextInt(WINDOW_WIDTH) + 1;
			int pos_y = rn.nextInt(WINDOW_HEIGHT) + 1;
			star = new Circle();
			star.setCenterX(pos_x);
			star.setCenterY(pos_y);
			double d = Math.random();
			if (d < 0.3)
				star.setFill(Color.WHITE);
			else if (d < 0.6)
				star.setFill(Color.GRAY);
			else
				star.setFill(Color.LIGHTGRAY);
			star.setRadius(0.5);
			nodes2.add(star);
		}

		for (int x = 0; x < 20; x++) {
			int pos_x = rn.nextInt(WINDOW_WIDTH) + 1;
			int pos_y = rn.nextInt(WINDOW_HEIGHT) + 1;
			star = new Circle();
			star.setCenterX(pos_x);
			star.setCenterY(pos_y);
			double d = Math.random();
			if (d < 0.3)
				star.setFill(Color.WHITE);
			else if (d < 0.6)
				star.setFill(Color.GRAY);
			else
				star.setFill(Color.LIGHTGRAY);
			star.setRadius(0.5);
			nodes3.add(star);
		}
	}
}