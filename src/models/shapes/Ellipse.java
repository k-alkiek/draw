package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 11/11/17.
 */
public class Ellipse extends AbstractShape implements Serializable {

    public Ellipse () {
        super();
        shapeProperties = new HashMap<>();
        shapeProperties.put("height", 0.0);
        shapeProperties.put("width", 0.0);
        shapeProperties.put("borderWidth", 0.0);
    }
    @Override
    public void draw(GraphicsContext canvas) {
        canvas.setLineWidth(shapeProperties.get("borderWidth"));
        Double height = shapeProperties.get("height"),
        width = shapeProperties.get("width");

        canvas.setFill(fillColor);
        canvas.fillOval(centrePoint.getX() - width,
                centrePoint.getY() - height,
                width * 2,
                height * 2);
        canvas.setStroke(perimeterColor);
        canvas.strokeOval(centrePoint.getX() - width,
                centrePoint.getY() - height,
                width * 2,
                height * 2);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newEllipse = new Ellipse();
        cloning(newEllipse, this);
        return newEllipse;
    }
}
