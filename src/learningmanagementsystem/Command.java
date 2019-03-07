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
     * @param input a String.
     */
    public void accessCMDList(String input) throws ExitProgramException {
        String[] arguments = splitInput(input);
        try {
            CommandsChoices cmd = CommandsChoices.valueOf(arguments[0]);
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
                    accessTable(arguments[1]);
            }
        } catch (ExitProgramException exit) {
            throw exit;
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("The command syntax is incorrect. Please try again.");
        }
    }

    private String[] splitInput(String input) {
        return input.split(" ");
    }

    /*
        Choose which table dashboard to initialize for the user.
     */
    private void accessTable(String choice) {
        try {
            TableNames name = TableNames.valueOf(choice);
            switch (name) {
                case Courses:
                    new Courses();
                    break;
            }

        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("This table does not exist. Try again");
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
