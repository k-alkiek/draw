package controllers.commands.commandsClasses;

import controllers.commands.ICommand;
import models.interfaces.Shape;

import java.util.List;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class AddShapeCommand implements ICommand {
    private List<Shape> shapes;
    private Shape shape;
    public AddShapeCommand(List<Shape> shapes, Shape shape) {
        this.shapes = shapes;
        this.shape = shape;
    }
    @Override
    public void execute() {
        shapes.add(shape);
    }
}
