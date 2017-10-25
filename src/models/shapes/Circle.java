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
        shapeProperties.put("raduis", 0.0);
    }

    @Override
    public void draw(GraphicsContext canvas) {
        canvas.setFill(fillColor);
        canvas.fillOval(centrePoint.getX(),
                centrePoint.getY(),
                shapeProperties.get("raduis"),
                shapeProperties.get("raduis"));
        canvas.setStroke(perimeterColor);
        canvas.strokeOval(centrePoint.getX(),
                centrePoint.getY(),
                shapeProperties.get("raduis"),
                shapeProperties.get("raduis"));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newCircle = new Circle();
        cloning(newCircle, this);
        return newCircle;
    }
}
