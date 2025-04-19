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

        ArrayList<String> searchArguments = new ArrayList<String>();
        ArrayList<ArrayList<String>> finalSearchArgumentSections = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> notFinalSearchArgumentSections = new ArrayList<ArrayList<String>>();

        // split text into separate arguments
        int ii = 0;
        int ci = 0;
        boolean shoudSetii = true;
        boolean inQuotes = false;
        char c;
        while (ci < text.length()) {
            c = text.charAt(ci);

            if (!inQuotes && Character.isWhitespace(c)) {
                String toAdd = text.substring(ii, ci);
                if (toAdd.trim() != "") {
                    if (!(toAdd.equals("AND") || toAdd.equals("OR") || toAdd.equals("NOT"))) {
                        toAdd = toAdd.toLowerCase();
                    }
                    searchArguments.add(toAdd);
                }
                shoudSetii = true;
            } else if (c == '\"') {
                if (!inQuotes) {
                    ii = ci;
                    inQuotes = true;
                    shoudSetii = false;
                } else {
                    String toAdd = text.substring(ii, ci);
                    ii = ci + 1;
                    searchArguments.add(toAdd);
                    inQuotes = false;
                    shoudSetii = true;
                }
            } else if (shoudSetii && !Character.isWhitespace(c)) {
                ii = ci;
                shoudSetii = false;
            }

            ci++;
        }
        if (ii < text.length() && !shoudSetii) {
            String toAdd = text.substring(ii, ci);
            if (toAdd.trim() != "") {
                if (!(toAdd.equals("AND") || toAdd.equals("OR") || toAdd.equals("NOT"))) {
                    toAdd = toAdd.toLowerCase();
                }
                searchArguments.add(toAdd);
            }
        }

        // remove AND operator (it is default behavior)
        for (int i = searchArguments.size() - 1; i >= 0; i--) {
            if (searchArguments.get(i).equals("AND")) {
                searchArguments.remove(i);
            }
        }

        // split search at OR operators and deal with NOT operators
        boolean newSection = true;
        for (int i = 0; i < searchArguments.size(); i++) {
            ArrayList toAddFSA = new ArrayList<String>();
            ArrayList toAddNotFSA = new ArrayList<String>();


            for (int j = i; j < searchArguments.size() + 1; j++) {
                if (j == searchArguments.size()) {
                    i = j;
                    break;
                }
                else if (searchArguments.get(j).equals("OR")) {
                    i = j;
                    break;
                } else if (searchArguments.get(j).equals("NOT")) {
                    if (j + 1 < searchArguments.size()) {
                        if (!(searchArguments.get(j+1).equals("OR") || searchArguments.get(j+1).equals("NOT"))) {
                            String argumentToAdd = searchArguments.get(j + 1);
                            toAddNotFSA.add(argumentToAdd);
                            j++;
                        }
                    }
                } else {
                    String argumentToAdd = searchArguments.get(j);
                    toAddFSA.add(argumentToAdd);
                }
            }

            if (toAddFSA.size() > 0 || toAddNotFSA.size() > 0) {
                finalSearchArgumentSections.add(toAddFSA);
                notFinalSearchArgumentSections.add(toAddNotFSA);
            }
        }

        // Print out the search arguments for debugging
        /*for (int i = 0; i < finalSearchArgumentSections.size(); i++) {
            ArrayList<String> currentSection = finalSearchArgumentSections.get(i);
            ArrayList<String> currentSectionN = notFinalSearchArgumentSections.get(i);

            for (int j = 0; j < currentSection.size(); j++) {
                System.out.println("argument[" + i + "]\t currentSection[" + j + "] = " + currentSection.get(j));
            }
            for (int j = 0; j < currentSectionN.size(); j++) {
                System.out.println("argument[" + i + "]\t notCurrentSection[" + j + "] = " + currentSectionN.get(j));
            }
            System.out.println("--------------------");
        }*/

        // compare arguments to sections and add sections to returnList
        for (int i = 0; i < finalSearchArgumentSections.size(); i++) {
            ArrayList<String> currentArgumentList = finalSearchArgumentSections.get(i);
            ArrayList<String> notCurrentArgumentList = notFinalSearchArgumentSections.get(i);

            for (int j = 0; j < fullList.size(); j++) {
                Section currentSection = fullList.get(j);

                int counter = 0;
                for (int k = 0; k < currentArgumentList.size(); k++) {
                    if (currentSection.containsText(currentArgumentList.get(k))) {
                        counter++;
                    }
                }
                for (int k = 0; k < notCurrentArgumentList.size(); k++) {
                    if (!currentSection.containsText(notCurrentArgumentList.get(k))) {
                        counter++;
                    }
                }
                // add course to returnList if it is not already there
                if (counter == currentArgumentList.size() + notCurrentArgumentList.size()) {
                    if (i == 0) { // should cause no errors; it implemented to improve speed
                        returnList.add(currentSection);
                    } else if (!returnList.contains(currentSection)) { // this could cause a slowdown in search speed
                        returnList.add(currentSection);
                    }
                }
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
