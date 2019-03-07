package learningmanagementsystem;

public class Command {
    /*
        Declare all the commands available in the program.
     */
    private enum CommandsChoices {
        add, update, delete, exit, menu, access
    }

    private enum TableNames {
        Courses, GradeItems
    }

    /**
     * Test whether the parameter is a command.
     * If it is, run appropriate action.
     *
     * @param choice a String.
     */
    public void accessCMDList(String choice) throws ExitProgramException {
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
                case access:
                    accessTable(choice);
            }
        } catch (ExitProgramException exit) {
            throw exit;
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("This command doesn't exist. Please try again.");
        }
    }

    /*
        Choose which table dashboard to initialize for the user.
     */
    private void accessTable(String choice) {
        TableNames name = TableNames.valueOf(choice);
        switch (name) {
            case Courses:
                new Courses();
                break;
            default:
                System.out.println("The table name is incorrect. Try again.");
        }
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
