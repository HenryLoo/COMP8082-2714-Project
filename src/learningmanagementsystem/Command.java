package learningmanagementsystem;

public class Command {
    /*
        Declare all the commands available in the program.
     */
    private enum CommandsChoices {
        add, update, delete, exit, menu
    }

    /**
     * Test whether the parameter is a command.
     * If it is, run appropriate action.
     *
     * @param choice a String.
     * @retrun true if it's a command, else false.
     */
    public boolean accessCMDList(String choice) {
        try {
            CommandsChoices cmd = CommandsChoices.valueOf(choice);
            switch (cmd) {
                case add:
                    runCommand(cmd);
                    break;
                case update:
                    runCommand(cmd);
                    break;
                case delete:
                    runCommand(cmd);
                    break;
                case exit:
                    exitProgram();
                    break;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    // Holder for now
    public void runCommand(CommandsChoices cmd) {
        System.out.println("Please come back later");
    }


}
