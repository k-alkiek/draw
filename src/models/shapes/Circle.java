package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 10/23/17.
 */
public class Circle extends AbstractShape {

    public Circle () {
        super();
        shapeProperties = new HashMap<>();
        shapeProperties.put("radius", 0.0);
        shapeProperties.put("borderWidth", 0.0);
    }

    @Override
    public void draw(GraphicsContext canvas) {
        canvas.setLineWidth(shapeProperties.get("borderWidth"));
        Double radius = shapeProperties.get("radius");
        canvas.setFill(fillColor);
        canvas.fillOval(centrePoint.getX() - radius,
                centrePoint.getY() - radius,
                radius * 2,
                radius * 2);
        canvas.setStroke(perimeterColor);
        canvas.strokeOval(centrePoint.getX() - radius,
                centrePoint.getY() - radius,
                radius * 2,
                radius * 2);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newCircle = new Circle();
        cloning(newCircle, this);
        return newCircle;
    }
}
