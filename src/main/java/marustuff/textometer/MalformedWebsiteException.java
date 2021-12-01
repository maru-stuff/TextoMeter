package marustuff.textometer;

import marustuff.textometer.model.Website;

import java.net.MalformedURLException;

public class MalformedWebsiteException extends MalformedURLException {
    public Website website;

    public MalformedWebsiteException() {
        super();
    }

    public MalformedWebsiteException(Website website) {
        super();
        this.website = website;
    }
}
