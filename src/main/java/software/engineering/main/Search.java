package software.engineering.main;
import java.util.ArrayList;

public class Search {

    protected ArrayList<Section> fullList;
    protected ArrayList<Section> lastSearch;
    protected ArrayList<Filter> filtersInUse;

    public Search(ArrayList<Section> list) {
        fullList = new ArrayList<>(list);
        lastSearch = new ArrayList<>();
        filtersInUse = new ArrayList<>();
    }

    public ArrayList<Section> courseSearch(String text) {
        ArrayList<Section> returnList = new ArrayList<Section>();

        for (int i = 0; i < fullList.size(); i++) {
            Section currentSection = fullList.get(i);

            if (
                    currentSection.getName().contains(text) ||
                            (
                                    text.contains(Integer.toString(currentSection.getCourseCode())) &&
                                            text.contains(currentSection.getDepartment())
                            )
            ) {
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

        lastSearch.clear();
        lastSearch.addAll(results);
        return results;
    }

    public void addFilter(Filter filter) {
        filtersInUse.add(filter);
    }
}
