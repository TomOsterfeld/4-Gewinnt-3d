package org.openjfx.connect_4.Grafik;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
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

        //PhongMaterial material = new PhongMaterial();
        //material.setDiffuseMap(new Image(getClass().getResourceAsStream("/img/texture.png")));
        //material.setDiffuseColor(red ? Color.RED : Color.YELLOW);

        PhongMaterial mat = new PhongMaterial(red ? Color.RED : Color.YELLOW);
        setMaterial(mat);
        translateYProperty().set(-100);
        setViewOrder(1);

        this.setEffect(GameEnvironment.getLighting());
        this.setMouseTransparent(true); // Andernfalls wird teilweise ein Klicken auf die seitlichen Buttons gestört
    }

    public void makeTransparent() {
        this.setDrawMode(DrawMode.LINE); // just draw the lines of the object
        double transparency = 0.0001;
        this.setMaterial(new PhongMaterial(red ? Color.rgb(255, 0 ,0, transparency) : Color.rgb(255, 255 ,0, transparency)));
        this.setViewOrder(-0.5);
        this.setCullFace(CullFace.NONE); // No culling is performed
        transparent = true;
    }

    public void makeUnTransparent() {
        this.setDrawMode(DrawMode.FILL);
        this.setMaterial(new PhongMaterial(red ? Color.rgb(255, 0 ,0, 1) : Color.rgb(255, 255 ,0, 1)));
        this.setViewOrder(0);
        this.setCullFace(CullFace.BACK);
        transparent = false;
    }

	public boolean getRed() {
		return red;
	}
}