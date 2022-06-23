package org.openjfx.connect_4.Grafik;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.openjfx.connect_4.Logik.GameClock;
import org.openjfx.connect_4.Logik.Move;

import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * 
 * @author Tom
 *
 */
public class GameEnvironment {	
	private final int x, y, z;
	private final int winningLength;
	private final int gameLength = 10; // in minutes
	
	public static int TILE_SIZE = 90;
	
	private AnchorPane globalRoot;
	private Scene scene;
	
	private Camera camera_3D;
	private SubScene scene_3D;
	private SmartGroup content;
	
	private Group restartGroup;
	private Group pauseGroup;
	
	private Label redClock, yellowClock;
	private GameClock redTimer, yellowTimer;
	
    private Color color;
    private Color transparentColor;
    private Color darkTransparentColor;
    
    private Token[][][] grid;
	
    private final static Lighting lighting = new Lighting();
	
    private ReadOnlyDoubleProperty widthProperty, heightProperty;
	private boolean redturn;
	
	private Consumer<Move> placeTokenHandler;
	private Consumer<Boolean> winEvent;
	
	public GameEnvironment(int x, int y, int z, int winningLength, Consumer<Move> placeTokenHandler) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.winningLength = winningLength;
		this.placeTokenHandler = placeTokenHandler;
		init();
	}
	
	private void init() {
		redturn = true;
			
		camera_3D = createCamera();
		content = createContent();
		 
		scene_3D = new SubScene(content, 1500, 1500, true, SceneAntialiasing.BALANCED);
		scene_3D.setFill(Color.valueOf("lightskyblue"));
		scene_3D.setCamera(camera_3D);
		 
		globalRoot = new AnchorPane();
		globalRoot.getChildren().add(scene_3D);
		globalRoot.getChildren().add(createTokenSymbolYellow());
		globalRoot.getChildren().add(createTokenSymbolRed());
		globalRoot.getChildren().add(createGewinnScreen());
		globalRoot.getChildren().add(createPause());
		globalRoot.getChildren().add(createGameClocks());
		 
		scene = new Scene(globalRoot);
		 
		scene_3D.widthProperty().bind(scene.widthProperty());
		scene_3D.heightProperty().bind(scene.heightProperty());
		
		Light.Distant _light = new Light.Distant();
		_light.setAzimuth(00.0);
		_light.setElevation(65.0);
	
		lighting.setLight(_light);
		lighting.setSurfaceScale(3.0);
		
		grid = new Token[x][y][z];
		
		createKeyBindings();		 
	}
	
	
	private void createKeyBindings() {
		Camera camera = scene_3D.getCamera();
		
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    camera_3D.translateZProperty().set(camera_3D.getTranslateZ() + 100);
                    break;
                case S:
                    camera_3D.translateZProperty().set(camera_3D.getTranslateZ() - 100);
                    break;
                case Q:
                    content.rotate(3, Rotate.X_AXIS);
                    break;
                case E:
                    content.rotate(-3, Rotate.X_AXIS);
                    break;
                case NUMPAD6: case D:
                    content.rotate(3, Rotate.Y_AXIS);
                    break;
                case NUMPAD4: case A:
                    content.rotate(-3, Rotate.Y_AXIS);
                    break;
                case UP:
                    camera_3D.setRotate(camera_3D.getRotate() + 1);
                    break;
                case DOWN:
                    camera_3D.setRotate(camera_3D.getRotate() - 1);
                    break;
                case ESCAPE:
                    pauseanimation();
                    break;
            }
        });
	}
	
    public void placeToken(Token token, int x, int y, int z) {    	
        int corner_x = (int) (-(this.x * TILE_SIZE) / 2.0 + (x + 0.5) * TILE_SIZE);
        int corner_y = (int) (-(this.y * TILE_SIZE) / 2.0 + (y + 0.5) * TILE_SIZE); 

        color = !redturn  ? Color.rgb(200, 0, 0, 0.9) : Color.rgb(200, 200, 50, 0.9);
        transparentColor = !redturn ? Color.rgb(200, 0, 0, 0.3) : Color.rgb(200, 200, 50, 0.3);
        darkTransparentColor = !redturn ? Color.rgb(100, 0, 0, 0.9) : Color.rgb(100, 100, 25, 0.9);

        grid[x][y][z] = token;
        content.getChildren().add(token);

        // Startposition
        token.translateXProperty().set(corner_x);
        token.translateZProperty().set(corner_y);
        token.translateYProperty().set(-(this.z + 1) * TILE_SIZE);

        TranslateTransition animation = new TranslateTransition();
        animation.setNode(token);
        animation.setToY(-(z + 1) * TILE_SIZE); // Zielposition
        animation.setOnFinished(e -> {});
        animation.play();
        
        token.setPickOnBounds(false);
        token.mouseTransparentProperty().set(true);
        
        if(redturn) {
        	redTimer.stop();
        	yellowTimer.start();
        } else {
        	yellowTimer.stop();
        	redTimer.start();
        }
        
        redturn = !redturn;
    }
    
    public void markTokens(List<int[]> tokens) {
    	tokens.forEach(point3d -> {
    		int x = point3d[0];
    		int y = point3d[1];
    		int z = point3d[2];
    		if(x < 0 || x >= this.x || y < 0 || y >= this.y || z < 0 || y >= this.z || grid[x][y][z] == null)
    			return;
    		grid[x][y][z].makeTransparent();
    	});
    }
	
	private SmartGroup createContent() {
        SmartGroup root = new SmartGroup();

        Consumer<Node> addToGroup = root.getChildren()::addAll;
        
        color = redturn  ? Color.rgb(200, 0, 0, 0.9) : Color.rgb(200, 200, 50, 0.9);
        transparentColor = redturn ? Color.rgb(200, 0, 0, 0.3) : Color.rgb(200, 200, 50, 0.3);
        darkTransparentColor = redturn ? Color.rgb(100, 0, 0, 0.9) : Color.rgb(100, 100, 25, 0.9);
        
        addToGroup.accept(createGrid());
        createBoxes().forEach(addToGroup);
        //createLight().forEach(addToGroup);
        
        return root;
	}
	
	private Box createGrid() {
        Box grid = new Box((x + 2) * TILE_SIZE, TILE_SIZE, (y + 2) * TILE_SIZE);
        //grid.translateYProperty().set(HEIGHT / 2.0 - TILE_SIZE / 2.0);
        //grid.translateYProperty().set(HEIGHT);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/ConcreteTexture.jpg"))));
        grid.setMaterial(material);
        grid.setMouseTransparent(true); // Andernfalls wird teilweise ein Klicken auf die seitlichen Buttons gestört

        return grid;
	}
	
    private Circle createTokenSymbolYellow() {
        Circle yellowToken = new Circle();
        yellowToken.setFill(Color.YELLOW);
        yellowToken.setRadius(20);
        yellowToken.setTranslateX(20 + yellowToken.getRadius());
        yellowToken.setTranslateY(yellowToken.getRadius() + 15);
        yellowToken.setEffect(getLighting());
        return yellowToken;
    }
    
    private Circle createTokenSymbolRed() {
        Circle redToken = new Circle();
        redToken.setFill(Color.RED);
        redToken.setRadius(20);
        redToken.setTranslateX(-20 - redToken.getRadius());
        redToken.setTranslateY(redToken.getRadius() + 15);
        redToken.translateXProperty()
        	.bind(SceneController.getStage().widthProperty().subtract(20 + 2 * redToken.getRadius())); // x Position muss sich an breite des Panes anpassen
        redToken.setEffect(getLighting());
        return redToken;
    }
    
    private List<Node> createBoxes() {
        List<Node> boxes = new ArrayList<>();
        
        int corner_x = (int) (-(x * TILE_SIZE) / 2.0 - TILE_SIZE);
        int corner_y = (int) (-(y * TILE_SIZE) / 2.0 - 0.5 * TILE_SIZE);

        for(int i = 1; i <= x; i++) {
            for(int j = 1; j <= y; j++) {
            	int x = corner_x + i * TILE_SIZE, y = corner_y + j * TILE_SIZE, radius = TILE_SIZE / 2;
            	Color fill = (i + j) % 2 == 0 ? Color.BLACK : Color.WHITE;
            	
                Cylinder cylinder = new Cylinder(radius, z * TILE_SIZE + 50);
                cylinder.translateXProperty().set(x + 0.5 * TILE_SIZE);
                cylinder.translateZProperty().set(y); // y und z sind hier vertauscht
                cylinder.translateYProperty().set(-cylinder.getHeight() / 2 - 50);
                Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
                rect.translateXProperty().set(x);
                rect.translateZProperty().set(y);
                rect.setOpacity(0.7);
                rect.setEffect(lighting);
                rect.setRotationAxis(new Point3D(1, 0, 0));
                rect.setRotate(90);
                rect.translateYProperty().set(-(z + 1) * TILE_SIZE - 50);
                rect.setFill(fill);

                Rectangle rect2 = new Rectangle(TILE_SIZE, TILE_SIZE);
                rect2.translateXProperty().set(x);
                rect2.translateZProperty().set(y);
                rect2.setRotationAxis(new Point3D(1, 0, 0));
                rect2.setRotate(90);
                rect2.translateYProperty().set(-TILE_SIZE - 1);
                rect2.setFill(fill);
                rect2.setViewOrder(2);
                rect2.setEffect(lighting);

                cylinder.setDrawMode(DrawMode.FILL);
                cylinder.setCullFace(CullFace.BACK);
                cylinder.setVisible(false);
                cylinder.setMouseTransparent(true);
                //cylinder.setViewOrder(2);

                final int COLUMN_X = i, COLUMN_Z = j;

                EventHandler<Event> onMouseEntered = event -> {
                    cylinder.setMaterial(new PhongMaterial(transparentColor));
                    cylinder.setVisible(true);
                    if(((COLUMN_X + COLUMN_Z) % 2) == 0) {
                        rect2.setFill(darkTransparentColor);
                        rect.setFill(darkTransparentColor);
                    } else {
                        rect.setFill(color);
                        rect2.setFill(color);
                    }
                };

                EventHandler<Event> onMouseExited = e -> {
                    cylinder.setMaterial(new PhongMaterial(Color.TRANSPARENT));
                    cylinder.setVisible(false);
                    final Color PAINT = (COLUMN_X + COLUMN_Z) % 2 == 0 ? Color.BLACK : Color.WHITE;
                    rect.setFill(PAINT);
                    rect2.setFill(PAINT);
                };

                EventHandler<Event> onMouseClicked = event -> {
                    placeTokenHandler.accept(new Move(COLUMN_X - 1, COLUMN_Z - 1));
                };

                rect.setOnMouseEntered(onMouseEntered);
                rect.setOnMouseExited(onMouseExited);
                rect.setOnMouseClicked(onMouseClicked);

                boxes.add(rect);
                boxes.add(cylinder);
                boxes.add(rect2);
            }
        }
        return boxes;
    }
	
    private Camera createCamera() {
        Camera camera = new PerspectiveCamera(true);
        camera.translateXProperty().set(0);
        camera.translateYProperty().set(-650);
        camera.translateZProperty().set(-1280);
        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(-18);

        camera.setNearClip(1);
        camera.setFarClip(10000);

        return camera;
    }
    
    private List<Node> createLight() {
    	PointLight light = new PointLight();
    	light.setColor(Color.WHITE);
    	light.translateYProperty().set(-z * TILE_SIZE);
    	light.translateXProperty().set(TILE_SIZE * x);
    	
    	PointLight light1 = new PointLight();
    	light1.setColor(Color.WHITE);
    	light1.translateYProperty().set(-z * TILE_SIZE);
    	light1.translateZProperty().set(TILE_SIZE * y);
    	
    	PointLight light2 = new PointLight();
    	light2.setColor(Color.WHITE);
    	light2.translateYProperty().set(-z * TILE_SIZE);
    	light2.translateXProperty().set(-TILE_SIZE * x);
    	
    	PointLight light3 = new PointLight();
    	light3.setColor(Color.WHITE);
    	light3.translateYProperty().set(-z * TILE_SIZE);
    	light3.translateZProperty().set(-TILE_SIZE * y);
    	return List.of(light, light1, light2, light3);
    }
 
	
	/**
	 * @author: Endrit
	 * @return
	 */
    private Group createGameClocks() {
    	int minutes = 1;
    	
    	Group createGameClocks = new Group();
    	
    	redTimer = new GameClock(minutes * 60);
		yellowTimer = new GameClock(minutes * 60);
    	
    	redClock = new Label(redTimer.toString());
		redClock.setTextFill(Color.RED);
		redClock.translateXProperty().bind(SceneController.getStage().widthProperty().subtract(180));
	    redClock.setTranslateY(25);
	    redClock.setFont(new Font("Calibri",20));
    		
    	
    	yellowClock = new Label(yellowTimer.toString());
    	yellowClock.setTextFill(Color.YELLOW);
		yellowClock.setTranslateX(115);
		yellowClock.setTranslateY(25);
		yellowClock.setFont(new Font("Calibri",20));
		
		// Tom
		redTimer.setAction(() -> {
    		Platform.runLater(() -> {
    			redClock.setText(redTimer.toString());
    		});
    	});
		
		yellowTimer.setAction(() -> {
    		Platform.runLater(() -> {
    			yellowClock.setText(yellowTimer.toString());
    		});
    	});
		
		redTimer.setOnZero(() -> {
			System.out.println("Zeit abgelaufen");
    		Platform.runLater(() -> {
    			winEvent.accept(false);
    		});
    	});
		
		yellowTimer.setOnZero(() -> {
			System.out.println("Zeit abgelaufen");
    		Platform.runLater(() -> {
    			winEvent.accept(true);
    		});
    	});
    		
    	// nicht mehr Tom
  
    	createGameClocks.getChildren().addAll(redClock, yellowClock);  	 	
    	createGameClocks.setVisible(true);
    	return createGameClocks;
    }
    
    public void markWinningRows(Move move, int move_z) {
		try {
			Thread.currentThread().sleep(100); // leichte Verzögerung
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
		int move_x = move.getX(), move_y = move.getY();
    	
    	boolean redTurn = grid[move_x][move_y][move_z].getRed();
    	
    	List<Token> winningRows = new ArrayList<>(4);
    			
    	//grid[move.getX()][move.getY()][move_z].getRed();
    	
		for(int deltaX = -1; deltaX <= 1; deltaX++) {
			for(int deltaY = -1; deltaY <= 1; deltaY++) {
				for(int deltaZ = -1; deltaZ <= 1; deltaZ++) {
					if(deltaX == 0 && deltaY == 0 && deltaZ == 0) break; // at least one index needs to change
					
					int leftBoarder = winningLength - 1;
					int rightBoarder = winningLength - 1;
					
					if(deltaX != 0) {
						if(deltaX == 1) {
							leftBoarder = Math.min(leftBoarder, move_x);
							rightBoarder = Math.min(rightBoarder, this.x - move_x - 1);
						} else {
							leftBoarder = Math.min(leftBoarder, this.x - move_x - 1);
							rightBoarder = Math.min(rightBoarder, move_x);
						}
					}
					
					if(deltaY != 0) {
						if(deltaY == 1) {
							leftBoarder = Math.min(leftBoarder, move_y);
							rightBoarder = Math.min(rightBoarder, this.y - move_y - 1);
						} else {
							leftBoarder = Math.min(leftBoarder, this.y - move_y - 1);
							rightBoarder = Math.min(rightBoarder, move_y);
						}
					}
					
					if(deltaZ != 0) {
						if(deltaZ == 1) {
							leftBoarder = Math.min(leftBoarder, move_z);
							rightBoarder = Math.min(rightBoarder, this.z - move_z - 1);
						} else {
							leftBoarder = Math.min(leftBoarder, this.z - move_z - 1);
							rightBoarder = Math.min(rightBoarder, move_z);
						}
					}
					
					int rowLength = rightBoarder + leftBoarder + 1;
					
					if(rowLength < winningLength) continue; // row not long enough
					
					int _x = move_x - deltaX * leftBoarder; // Koordinaten des ersten Tokens einer Reihe
					int _y = move_y - deltaY * leftBoarder;
					int _z = move_z - deltaZ * leftBoarder;
					
					int counter = 0;
					
					for(int i = 0; i < rowLength; _x += deltaX, _y += deltaY, _z += deltaZ, i++) {
						if(grid[_x][_y][_z] != null && grid[_x][_y][_z].getRed() == redTurn) {
							counter++;
						} else {
							if(counter >= winningLength) {
								int __x = _x - deltaX, __y = _y - deltaY, __z = _z - deltaZ;
								for(; counter-- > 0; __x -= deltaX, __y -= deltaY, __z -= deltaZ) {
									if(grid[__x][__y][__z] != null) {
										winningRows.add(grid[__x][__y][__z]);
									}
								}
							}
							
							counter = 0;
						}
					}
					
					if(counter >= winningLength) {
						System.out.println(counter);
						int __x = _x - deltaX, __y = _y - deltaY, __z = _z - deltaZ;
						for(; counter-- > 0; __x -= deltaX, __y -= deltaY, __z -= deltaZ) {
							if(grid[__x][__y][__z] != null) {
								winningRows.add(grid[__x][__y][__z]);
							}
						}
					}
				}
			}
		}
		
		List<Token> allTokens = new ArrayList<>();
		
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				for(int k = 0; k < z; k++) {
					if(grid[i][j][k] != null) {
						allTokens.add(grid[i][j][k]);
					}
				}
			}
		}
		
		// mache alle Tokens bis auf die, der Gewinnreihe, transparent
		winningRows.forEach(winningToken -> allTokens.remove(winningToken));
		allTokens.forEach(Token::makeTransparent);
    }
    
    public void gewinnAnimation(String ergebnis) {
    	Label gewinner = (Label) restartGroup.getChildren().get(1);
    	gewinner.setText(ergebnis);
    	restartGroup.setVisible(!restartGroup.isVisible());	
    	//pauseGroup.setVisible(!pauseGroup.isVisible());	
    	if(restartGroup.isVisible()) {
    		scene_3D.setOpacity(0.5);
    	} else {
    		scene_3D.setOpacity(1.0);
    	}
    }

    private void pauseanimation() {
    	restartGroup.setVisible(false);
    	pauseGroup.setVisible(!pauseGroup.isVisible());
    	if(pauseGroup.isVisible()) {
    		scene_3D.setOpacity(0.2);
    	}else {
    		scene_3D.setOpacity(1.0);
    	}
    }

    private Group createGewinnScreen() {
		restartGroup = new Group();
		
		String restart = "restart";
		String homescreenstring = "homescreen";
		
		Button button = new Button();
		button.setText(restart);
		button.setFont(new Font("Calibri",30));
		button.setTranslateX(90);   //hat am längsten gedauert das rasuzufinden, angeschaut wie es bei gelben token ist
		button.translateYProperty().bind(SceneController.getStage().heightProperty().divide(2).subtract(50));
		
		Label label = new Label("Rot gewinnt"); //Fehler: falsches importiert, L�sung bereits gefunden
		label.setFont(new Font("Calibri",50));
		label.translateXProperty().bind(SceneController.getStage().widthProperty().divide(2).subtract(120));
		label.translateYProperty().bind(SceneController.getStage().heightProperty().divide(2).subtract(50));
		
		Label label2 = new Label("Gelb gewinnt");
		label2.setFont(new Font ("Calibri", 50));
		label2.translateXProperty().bind(SceneController.getStage().widthProperty().divide(2).subtract(120));
		label.translateYProperty().bind(SceneController.getStage().heightProperty().divide(2).subtract(50));
		
		//.translateXProperty()
    	//.bind(SceneController.getStage().widthProperty().subtract(20 + 2 * redToken.getRadius()))   kopiert von tom als hilfe für mich
		
		Button homescreen = new Button();
		homescreen.setText(homescreenstring);
		homescreen.setFont(new Font("Calibri",30));
		homescreen.translateXProperty().bind(SceneController.getStage().widthProperty().subtract(196.36+90)); //problem: relation rauszufinden
		//lösung: messen mit geodreieck und ausrechnen
		homescreen.translateYProperty().bind(SceneController.getStage().heightProperty().divide(2).subtract(50));
		
		button.setOnAction(action -> {
			System.out.println("Hier muss eine reset operation eingefügt werden");
		});	
		homescreen.setOnAction(action -> {
			SceneController.switchScene("START_MENU");
		});
			
		
		
		restartGroup.getChildren().addAll(button, label,homescreen);
		restartGroup.setVisible(false);
		
		return restartGroup;
	}

    private Group createPause() {
		pauseGroup = new Group();
		
		Button pause = new Button();
		String pauseString = "Pause";
		pause.setText(pauseString);
		pause.setFont(new Font("Calibri", 30));
		pause.setTranslateX(90);
		pause.translateYProperty().bind(SceneController.getStage().heightProperty().divide(2).subtract(50));
		
		Button homescreen = new Button();
		String homescreenstring = "Homescreen";
		homescreen.setText(homescreenstring);
		homescreen.setFont(new Font("Calibri",30));
		homescreen.translateXProperty().bind(SceneController.getStage().widthProperty().subtract(196.36+90));
		homescreen.translateYProperty().bind(SceneController.getStage().heightProperty().divide(2).subtract(50));
		
		homescreen.setOnAction(action -> {
			SceneController.switchScene("START_MENU");
		});
		
		pause.setOnAction(action -> {
			System.out.println("Aktion muss noch eingefügt werden");
		});
			
	
		pauseGroup.getChildren().addAll(pause,homescreen);
		pauseGroup.setVisible(false);
		return pauseGroup;
	}
    
    public void setWinEvent(Consumer<Boolean> winEvent) {
    	this.winEvent = winEvent;
    }
	
    public Scene getScene() {
		return scene;
	}

	public static Lighting getLighting() {
		return lighting;
	}

	class SmartGroup extends Group {
        Rotate rotation;
        Transform transformation = new Rotate();

        void rotate(int angle, Point3D axis) {
        	rotation = new Rotate(angle, axis);
        	transformation = transformation.createConcatenation(rotation);
            this.getTransforms().clear();
            this.getTransforms().add(transformation);
        }
    }
}
