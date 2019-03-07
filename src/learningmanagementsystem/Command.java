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
    public boolean accessCMDList(String choice) throws ExitProgramException {
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
                default:
                    System.out.println("This command doesn't exist. Please try again.");
            }
        } catch (ExitProgramException exit) {
            throw exit;
        }


        return true;
    }

    // Holder for now
    public void runCommand(CommandsChoices cmd) {
        System.out.println("Please come back later");
    }

    /**
     * Tell session that we want to exit the program.
     */
    protected void exitProgram() throws ExitProgramException {
        throw new ExitProgramException("Exiting Program...");
    }
}
