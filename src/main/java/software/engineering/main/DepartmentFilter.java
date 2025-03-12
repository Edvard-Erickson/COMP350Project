package software.engineering.main;

import java.util.ArrayList;

public class DepartmentFilter extends Filter {

    protected String department;

    public DepartmentFilter(String department) {
        super();
    }

    @Override
    public ArrayList<Section> filter(ArrayList<Section> sectionList) {
        ArrayList<Section> returnList = new ArrayList<Section>();

        for (int i = 0; i < sectionList.size(); i++) {
            if (sectionList.get(i).getDepartment() == department) {
                returnList.add(sectionList.get(i));
            }
        }

        return returnList;
    }
}
