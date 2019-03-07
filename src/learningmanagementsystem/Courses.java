package learningmanagementsystem;

public class Courses implements Tables {

    @Override
    public void select() {

    }

    @Override
    public void add(String name, String id, String description, int profID){
        try {
            checkCourseName(name);
        } catch(IllegalArgumentException e){
            System.out.println("Please re enter");
        }
        try {
            checkDescription(description);
        } catch (IllegalArgumentException e){
            System.out.println("Please re enter");
        }
        try {
            checkProfID(profID);
        } catch (IllegalArgumentException e){
            System.out.println("Please re enter");
        }
        

    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    private boolean checkID(String id){

    }

    private boolean checkCourseName(String name) {
        if(name != null){
            return true;
        }
        return false;
    }

    private boolean checkDescription(String description) {
        if(description != null && description.length() > 150){
            throw new IllegalArgumentException("Sorry the description is too long!");
        }
        return true;
    }

    private boolean checkProfID(int profID){
        int length = Integer.toString(profID).length();
        if(length != 2){
            throw new IllegalArgumentException("Your professor id is invalid");
        }
        return true;
    }
}
