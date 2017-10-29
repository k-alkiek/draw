package controllers.commands.commandsClasses;

import controllers.commands.ICommand;
import models.interfaces.Shape;

import java.util.ArrayList;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class RemoveShapeCommand implements ICommand {
    private ArrayList<Shape> shapes;
    private Shape shape;
    public RemoveShapeCommand(Shape shape, ArrayList<Shape> shapes) {
        this.shapes = shapes;
        this.shape = shape;
    }
    @Override
    public void execute() {
        shapes.remove(shapes.indexOf(shape));
    }

    @Override
    public void unexecute() {
        shapes.add(shape);
    }
}
