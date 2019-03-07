package learningmanagementsystem;

public class Courses implements Tables {

    @Override
    public void select() {

    }

    @Override
    public void add(String name, String id, String description, int profID){
        if(checkCourseName(name) && checkProfID(profID) && )
        String command = "INSERT INTO courses VALUES (" + name + ", " + id
                + ", " + description + " ," + profID + ";";

    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    private boolean checkID(String id){
        boolean digitTest  = false;
        boolean alphaTest = false;
        for(int i = 0; i < 3; i++){
            if(id!= null && Character.isDigit(id.charAt(i))){
                digitTest = true;
            }
        }
        for(int i = 3; i < 6; i++){
            if(id!= null && Character.isAlphabetic(i)){
                alphaTest = true;
            }
        }
        if (digitTest && alphaTest && id.length() == 6 ){
            return true;
        }else {
            return false;

        }
    }

    private boolean checkCourseName(String name) {
        if(name != null){
            return true;
        }
        return false;
    }

    private boolean checkDescription(String description) {
        if(description != null && description.length() > 150){
            return false;
        }
        return true;
    }

    private boolean checkProfID(int profID){
        int length = Integer.toString(profID).length();
        if(length != 6){
            return false;
        }
        return true;
    }
}
