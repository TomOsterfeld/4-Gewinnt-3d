package org.openjfx.connect_4.Grafik;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;

/**
 * 
 * @author Tom
 *
 */
public class Token extends Sphere {
	private final boolean red;
	public boolean transparent = false;

	public Token(boolean red, int radius) {
		super(radius);
		this.red = red;

		// PhongMaterial material = new PhongMaterial();
		// material.setDiffuseMap(new
		// Image(getClass().getResourceAsStream("/img/texture.png")));
		// material.setDiffuseColor(red ? Color.RED : Color.YELLOW);

		PhongMaterial material = new PhongMaterial(red ? Color.RED : Color.YELLOW);
		setMaterial(material);
		translateYProperty().set(-100);
		setViewOrder(1);

		this.setEffect(GameEnvironment.getLighting());
		this.setMouseTransparent(true); // Andernfalls wird teilweise ein Klicken auf die seitlichen Buttons gestört
	}

	public void darker() {
		PhongMaterial material = new PhongMaterial(red ? Color.RED.darker() : Color.YELLOW.darker());
		setMaterial(material);
	}

	public void makeTransparent() {
		this.setDrawMode(DrawMode.LINE); // Nur "Skelett" der Kugel
		double transparency = .7;
		this.setMaterial(
				new PhongMaterial(red ? Color.rgb(255, 0, 0, transparency) : Color.rgb(255, 255, 0, transparency)));
		transparent = true;
	}

	public void makeUnTransparent() {
		this.setDrawMode(DrawMode.FILL); // durchgängige Oberfläche
		this.setMaterial(new PhongMaterial(red ? Color.rgb(255, 0, 0, 1) : Color.rgb(255, 255, 0, 1)));
		transparent = false;
	}

	public boolean getRed() {
		return red;
	}
}