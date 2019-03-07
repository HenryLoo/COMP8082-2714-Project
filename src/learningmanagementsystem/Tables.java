package learningmanagementsystem;

public interface Tables {
    void add(String name, String id, String description, int profID);
    void update();
    void delete();
    void select(); // might change to display
}
