package software.engineering.main;

import java.util.ArrayList;

public class Schedule {
    private ArrayList<Section> classes;
    private ArrayList<Timeblock> blocks;

    protected Schedule() {
        classes = new ArrayList<Section>();
        blocks = new ArrayList<Timeblock>();
    }

    protected void addSection(Section section) {
        classes.add(section);
    }

    protected void addTimeblock(Timeblock block) {
        blocks.add(block);
    }

    protected void removeSection(Section section) {
        classes.remove(section);
    }

    protected void removeTimeblock(Timeblock block) {
        blocks.remove(block);
    }

    protected ArrayList<Object> getSchedule() {
        return null;
    }
}
