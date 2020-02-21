package linktracker.model;

import java.util.List;

/**
 * Class that save the information of a web page
 */
public class WebPage {

    private String name;
    private String url;
    private List<String> links;

    /**
     * Constructor to create a new WebPage
     * @param name
     * @param url
     */
    public WebPage(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * Method to get the web's name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to change the value of the web's name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to get the web's url
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Method to change the value of the web's url
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Method to get the web's links
     * @return links
     */
    public List<String> getLinks() {
        return links;
    }

    /**
     * Method to change the value of the web's links
     * @param links
     */
    public void setLinks(List<String> links) {
        this.links = links;
    }

    /**
     * Method to show the web's name
     * @return name
     */
    @Override
    public String toString() {
        return name;
    }
}
