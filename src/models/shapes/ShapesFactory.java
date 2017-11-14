package models.shapes;

import models.interfaces.IShapesFactory;
import models.interfaces.Shape;

/**
 * Created by khaledabdelfattah on 11/11/17.
 */
public class ShapesFactory implements IShapesFactory {
    public ShapesFactory () {}
    @Override
    public Shape createShape(String type) {
        Shape newShape = null;
        if (type.equals("models.shapes.Circle") || type.equals("Circle")) {
            newShape = new Circle();
        } else if (type.equals("models.shapes.Line") || type.equals("Line")) {
            newShape = new Line();
        } else if (type.equals("models.shapes.Rectangle") || type.equals("Rectangle")) {
            newShape = new Rectangle();
        } else if (type.equals("models.shapes.Ellipse") || type.equals("Ellipse")) {
            newShape = new Ellipse();
        } else if (type.equals("models.shapes.RoundRectangle") || type.equals("RoundRectangle")) {
            newShape = new RoundRectangle();
        } else if (type.equals("models.shapes.Triangle") || type.equals("Triangle")) {
            newShape = new Triangle();
        } else if (type.equals("models.shapes.Square") || type.equals("Square")){
            newShape = new Square();
        } else if (type.equals("models.shapes.Polygon") || type.equals("Polygon")) {
            newShape = new Polygon();
        }
        return newShape;
    }
}
