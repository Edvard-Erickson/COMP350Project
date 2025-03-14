package software.engineering.main;

import java.util.ArrayList;

public class ProfessorFilter extends Filter {

    protected String professor;

    public ProfessorFilter(String professor) {
        super();
    }

    @Override
    public ArrayList<Section> filter(ArrayList<Section> sectionList) {
        ArrayList<Section> returnList = new ArrayList<Section>();

        for (int i = 0; i < sectionList.size(); i++) {
            if (sectionList.get(i).getProfessor() == professor) {
                returnList.add(sectionList.get(i));
            }
        }

        return returnList;
    }

}
