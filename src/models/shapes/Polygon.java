package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 11/14/17.
 */
public class Polygon extends AbstractShape {
    private double[] xPoints, yPoints;
    private int n;
    public Polygon() {
        super();
        shapeProperties = new HashMap<>();
        shapeProperties.put("borderWidth", 0.0);
        for (int i = 0; i < n; i ++) {
            shapeProperties.put("x" + i, 0.0);
            shapeProperties.put("y" + i, 0.0);
        }
    }

    public Polygon (int n, double[] xPoints, double[] yPoints) {
        super();
        this.n = n;
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        shapeProperties = new HashMap<>();
        shapeProperties.put("borderWidth", 0.0);
        for (int i = 0; i < n; i ++) {
            shapeProperties.put("x" + i, 0.0);
            shapeProperties.put("y" + i, 0.0);
        }
    }
    @Override
    public void draw(GraphicsContext canvas) {
        canvas.setLineWidth(shapeProperties.get("borderWidth"));
        canvas.setFill(fillColor);
        canvas.fillPolygon(xPoints, yPoints, n);
        canvas.setStroke(perimeterColor);
        canvas.strokePolygon(xPoints, yPoints, n);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newPolygon = new Polygon();
        cloning(newPolygon, this);
        return newPolygon;
    }
}
