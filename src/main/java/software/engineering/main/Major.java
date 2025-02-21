package software.engineering.main;

import java.net.URL;

public class Major {
    private String name;
    private URL majorSheetURL;

    protected Major(String name, URL minorSheet) {
    }

    protected String getName() {
        return name;
    }

    protected URL getMajorSheetURL() {
        return majorSheetURL;
    }
}
