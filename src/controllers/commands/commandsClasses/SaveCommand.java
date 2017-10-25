package controllers.commands.commandsClasses;

import controllers.commands.ICommand;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class SaveCommand implements ICommand {
    private String path;
    public SaveCommand (String path) {
        this.path = path;
    }
    @Override
    public void execute() {

    }
}
