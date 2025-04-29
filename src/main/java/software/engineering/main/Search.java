package software.engineering.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Search {

    protected ArrayList<Section> fullList;
    protected ArrayList<Section> lastSearch;
    protected ArrayList<Filter> filtersInUse;
    protected HashSet<String> dictionary;

    public Search(ArrayList<Section> list) {
        fullList = new ArrayList<>(list);
        lastSearch = new ArrayList<>(list);
        filtersInUse = new ArrayList<>();

        // create dictionary //TODO: possibly implement word frequency
        dictionary = new HashSet<String>();
        dictionary.add("and");
        dictionary.add("or");
        dictionary.add("not");
        for (int i = 0; i < fullList.size(); i++) {
            Section currentSection = fullList.get(i);
            String department = currentSection.getDepartment().toLowerCase();
            String courseCode = Integer.toString(currentSection.getCourseCode());
            String courseName = currentSection.getCourseName().toLowerCase();
            String professor = currentSection.getProfessor().toLowerCase();

            String[] nameWords = courseName.split(" ");
            String[] professorWords = professor.split(" ");

            dictionary.add(department);
            dictionary.add(courseCode);
            dictionary.addAll(Arrays.asList(nameWords));
            dictionary.addAll(Arrays.asList(professorWords));
        }

        /*for (int i = 0; i < dictionary.size(); i++) {
            System.out.println("dictionary[" + i + "] = " + dictionary.toArray()[i]);
        }*/
    }

    public ArrayList<Section> courseSearch(String text) {

        // handle empty search
        if (text.equals("")) {
            lastSearch = fullList;
            return fullList;
        }

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
            } else if (!searchArguments.get(i).contains("\"") && !searchArguments.get(i).contains("NOT") && !searchArguments.get(i).contains("OR")) { // TODO
                //System.out.println("searchArguments[" + i + "] = " + searchArguments.get(i));
                if (!dictionary.contains(searchArguments.get(i).toLowerCase())) {
                    String closestMatch = closestMatch(searchArguments.get(i).toLowerCase());
                    if (closestMatch != null) {
                        searchArguments.set(i, closestMatch);
                    }
                }
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

        lastSearch = returnList;
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

    public int levenshteinDistanceBad(String first, String second) {
        int distance = 0;

        if (first.length() > second.length()) {
            String temp = first;
            first = second;
            second = temp;
        }
        // Now first is guaranteed to be the shorter string

        /*if (second.length() - first.length() >= 5) {
            return second.length();
        }*/

        if (first.length() == 0) {
            return second.length();
        } else if (first.length() == 1) {
            if (second.contains(first)) {
                return second.length() - 1;
            }
            else {
                return second.length();
            }
        }

        if (first.charAt(0) == second.charAt(0)) {
            return levenshteinDistanceBad(first.substring(1), second.substring(1));
        }

        int distanceA = levenshteinDistanceBad(first.substring(1), second.substring(1));
        int distanceD = levenshteinDistanceBad(first.substring(0), second.substring(1));
        int distanceI = levenshteinDistanceBad(first.substring(1), second.substring(0));

        // a(bc | x(bc
        // (abc | x(abc
        // a(bc | (bc

        if (distanceA <= distanceD && distanceA <= distanceI) {
            distance = distanceA + 1;
        } else if (distanceD <= distanceA && distanceD <= distanceI) {
            distance = distanceD + 1;
        } else {
            distance = distanceI + 1;
        }

        return distance;
    }

    public int levenshteinDistance(String first, String second) {
        if (first.length() > second.length()) {
            String temp = first;
            first = second;
            second = temp;
        }

        int[][] matrix = new int[first.length() + 1][second.length() + 1];

        for (int i = 0; i < first.length() + 1; i++) {
            matrix[i][0] = i;
        }
        for (int i = 1; i < second.length() + 1; i++) {
            matrix[0][i] = i;
        }

        for (int i = 2; i < first.length() + second.length() + 1; i++) {

            int k = i-1;
            for (int j = 1; j < first.length() + 1; j++) {

                if (k <= 0) {
                    break;
                }
                else if (k <= second.length()) {
                    int valueA = matrix[j - 1][k - 1];
                    int valueB = matrix[j][k - 1];
                    int valueC = matrix[j - 1][k];

                    if (valueA <= valueB && valueA <= valueC) {
                        int matchingChar = 0;
                        if (first.charAt(j-1) != second.charAt(k-1)) {
                            matchingChar = 1;
                        }

                        matrix[j][k] = valueA + matchingChar;
                    } else if (valueB <= valueA && valueB <= valueC) {
                        matrix[j][k] = valueB + 1;
                    } else {
                        matrix[j][k] = valueC + 1;
                    }
                }

                k--;
            }
        }

        return matrix[first.length()][second.length()];
    }

    public String closestMatch(String text) {
        String closestMatch = text;
        int closestDistance = Integer.MAX_VALUE;

        for (String word : dictionary) {
            int distance = levenshteinDistance(text, word);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestMatch = word;
            }
        }

        return closestMatch;
    }

    public boolean containsText(Section section, String text) {
        String courseName = section.getCourseName();
        String professor = section.getProfessor();
        String department = section.getDepartment();
        int courseCode = section.getCourseCode();

        if (text.length() >= 1) {
            if (text.charAt(0) == '\"') {
                if (text.length() >= 2) {
                    text = text.substring(1, text.length() - 1);

                    if (courseName.contains(text) ||
                            professor.contains(text) ||
                            department.contains(text) ||
                            (text.contains(Integer.toString(courseCode)) && text.contains(department))
                    ) {
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                if (courseName.toLowerCase().contains(text) ||
                        professor.toLowerCase().contains(text) ||
                        department.toLowerCase().contains(text) ||
                        Integer.toString(courseCode).contains(text)

                ) {
                    return true;
                }
            }
        }
        return false;
    }
}
