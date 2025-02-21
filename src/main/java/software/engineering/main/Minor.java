package software.engineering.main;

import java.net.URL;

public class Minor {
    private String name;
    private URL minorSheetURL;

    protected Minor(String name, URL minorSheet) {
    }

    protected String getName() {
        return name;
    }

    protected URL getMinorSheet() {
        return minorSheetURL;
    }
}
