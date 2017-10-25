package models.commands.commandsClasses;

import models.commands.ICommand;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class LoadCommand implements ICommand {
    private String path;
    public LoadCommand (String path) {
        this.path = path;
    }
    @Override
    public void execute() {

    }
}
