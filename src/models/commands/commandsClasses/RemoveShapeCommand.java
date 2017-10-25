package models.commands.commandsClasses;

import models.commands.ICommand;
import models.interfaces.Shape;

import java.util.List;
import java.util.Stack;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class RemoveShapeCommand implements ICommand {
    private List<Shape> shapes;
    private Stack<Shape> currentShapes;
    private Shape shape;
    public RemoveShapeCommand(List<Shape> shapes,
                              Stack<Shape> currentShapes,
                              Shape shape) {
        this.shapes = shapes;
        this.currentShapes = currentShapes;
        this.shape = shape;
    }
    @Override
    public void execute() {
        currentShapes.push(shape);
        shapes.remove(shapes.indexOf(shape));
    }
}
