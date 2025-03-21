package software.engineering.main;

import java.util.ArrayList;

public class DepartmentFilter extends Filter {

    protected String department;

    public DepartmentFilter(String department) {
        super();
        this.department = department.toLowerCase();
    }

    @Override
    public ArrayList<Section> filter(ArrayList<Section> sectionList) {
        ArrayList<Section> returnList = new ArrayList<Section>();

        for (int i = 0; i < sectionList.size(); i++) {
            if (sectionList.get(i).getDepartment().toLowerCase().equals(department)) {
                returnList.add(sectionList.get(i));
            }
        }

        return returnList;
    }
}
