package software.engineering.main;

import java.util.ArrayList;

public class Search {

    protected ArrayList<Section> fullList;
    protected ArrayList<Section> lastSearch;
    protected ArrayList<Filter> filtersInUse;

    public Search(ArrayList<Section> list) {
        fullList = new ArrayList<>(list);
        lastSearch = new ArrayList<>(list);
        filtersInUse = new ArrayList<>();
    }

    public ArrayList<Section> courseSearch(String text) {
        ArrayList<Section> returnList = new ArrayList<Section>();
        String textInLower = text.toLowerCase();

        for (int i = 0; i < fullList.size(); i++) {
            Section currentSection = fullList.get(i);

            if (
                    currentSection.getName().toLowerCase().contains(textInLower) ||
                            currentSection.getProfessor().toLowerCase().contains(textInLower) ||
                            textInLower.contains(Integer.toString(currentSection.getCourseCode())) ||
                            textInLower.contains(currentSection.getProfessor()) ||
                            textInLower.contains(currentSection.getDepartment().toLowerCase())

            ) {
                returnList.add(fullList.get(i));
            }
        }

        return returnList;
    }

    public ArrayList<Section> courseSections(String department, int courseCode) {

        ArrayList<Section> returnList = new ArrayList<Section>();

        for (int i = 0; i < fullList.size(); i++) {
            Section currentSection = fullList.get(i);

            if (currentSection.getDepartment().equals(department) && currentSection.getCourseCode() == courseCode) {
                returnList.add(fullList.get(i));
            }
        }

        return returnList;
    }

    // I think this is done but I'm not sure - LegoBuilder
    public ArrayList<Section> applyFilters() {
        ArrayList<Section> results = new ArrayList<Section>();
        results.addAll(lastSearch);

        for (int i = 0; i < filtersInUse.size(); i++) {
            results = filtersInUse.get(i).filter(results);
        }

        setLastSearch(results);
        return results;
    }

    public void addFilter(Filter filter) {
        filtersInUse.add(filter);
    }

    public void clearFilters() {
        filtersInUse.clear();
        setLastSearch(fullList);
    }

    public ArrayList<Section> getLastSearch() {
        return lastSearch;
    }

    public void setLastSearch(ArrayList<Section> lastSearch) {
        this.lastSearch.clear();
        this.lastSearch.addAll(lastSearch);
    }
}
