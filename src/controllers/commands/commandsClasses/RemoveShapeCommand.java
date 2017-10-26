package controllers.commands.commandsClasses;

import controllers.commands.ICommand;
import models.interfaces.Shape;

import java.util.List;
import java.util.Stack;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class RemoveShapeCommand implements ICommand {
    private List<Shape> shapes;
    private Shape shape;
    private int index;
    public RemoveShapeCommand(List<Shape> shapes,
                              Shape shape) {
        this.shapes = shapes;
        this.shape = shape;
        index = shapes.indexOf(shape);
    }
    @Override
    public void execute() {
        shapes.remove(index);
    }

    @Override
    public void unexecute() {
        shapes.add(shape);
    }
}
