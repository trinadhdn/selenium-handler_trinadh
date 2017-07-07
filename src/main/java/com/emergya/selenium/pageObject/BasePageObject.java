package com.emergya.selenium.pageObject;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.emergya.selenium.drivers.EmergyaWebDriver;
import com.emergya.selenium.utils.Initialization;
import com.emergya.selenium.utils.PropertiesHandler;

/**
 * Class to group the common features of all PO's
 * 
 * @author Jose Antonio Sanchez <jasanchez@emergya.com>
 * @author Alejandro Gomez <agommor@gmail.com>
 * @author Ivan Bermudez <ibermudez@emergya.com>
 * @author Ivan Gomez <igomez@emergya.com>
 */
public abstract class BasePageObject {

    protected final EmergyaWebDriver driver;
    protected Initialization config = Initialization.getInstance();
    protected String className = "";

    static Logger log = Logger.getLogger(BasePageObject.class);

    public BasePageObject(EmergyaWebDriver driver) {
        this.driver = driver;
        this.className = getClass().getSimpleName();
    }

    /**
     * TIMEOUT for elements.
     */
    protected static final long TIMEOUT = 20; // Seconds

    /**
     * This method builds the file selector path for each Page Object:
     * @param key to be retrieved.
     * @return the file selectors path.
     */
    protected String getSelectorsFilePath(String key) {
        String filePath = "selectors" + File.separatorChar;
        String baseName = this.className.toLowerCase();
        // we have to check if the baseName is valid or not. If it's not valid, we will check in all the stack trace
        if (!existsKey(filePath + baseName + ".properties", key)) {
            boolean existsKey = false;
            Class<?> superClass = this.getClass().getSuperclass();

            while (!existsKey && !superClass.getSimpleName().equalsIgnoreCase("basepageobject")) {

                String proposedBaseName = superClass.getSimpleName().toLowerCase().replace(".java", "");
                existsKey = existsKey(filePath + proposedBaseName + ".properties", key);

                if (existsKey) {
                    baseName = proposedBaseName;
                } else {
                    superClass = superClass.getSuperclass();
                }
            }

        }
        filePath += baseName + ".properties";
        return filePath;
    }

    /**
     * It checks if exists a key in a file:
     * @param file.
     * @param key.
     * @return if exists or not.
     */
    private boolean existsKey(String file, String key) {
        boolean exists = false;
        PropertiesHandler handler = PropertiesHandler.getInstance();
        handler.load(file);
        exists = StringUtils.isNotBlank(handler.get(key));
        return exists;
    }

    /**
     * Checks that the PO is ready
     * 
     * @param pageObject
     *            page object to be used
     */
    public abstract boolean isReady();

    // **** ID methods section ****//
    /**
     * This method interacts with Selenium to retrieve the needed element. By ID
     * 
     * @param key of the item to be selected. In the related selector file should exists an entry with: key.type and key.id
     * @param timeOut limit for wait until appear.
     * @return the selected {@link Webelement} object
     */
    public WebElement getElementById(String key, long timeOut) {
        WebElement element = null;
        PropertiesHandler handler = PropertiesHandler.getInstance();
        handler.load(this.getSelectorsFilePath(key + ".id"));
        String type = handler.get(key + ".type"); // Could be null
        String id = handler.get(key + ".id"); // getElementByIdJustId

        if (type == null && StringUtils.isNotBlank(id)) {
            element = this.getElementByIdJustId(id, timeOut);
        } else {
            if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(id)) {
                element = this.getElementById(type, id, timeOut);
            } else {
                log.error("Trying to retrieve from " + this.getSelectorsFilePath(key + ".id")
                        + " file the item with the key " + key + " but " + key + ".type and/or " + key
                        + ".id are missing!");
            }
        }
        return element;
    }

    /**
     * This method interacts with Selenium to retrieve the needed element. By ID
     * This method uses the TIMEOUT constant.
     * 
     * @param key of the item to be selected. In the related selector file should exists an entry with: key.type and key.id
     * @return the selected {@link Webelement} object
     */
    public WebElement getElementById(String key) {
        return this.getElementById(key, TIMEOUT);
    }

    /**
     * This method checks if a {@link WebElement} is displayed and visible for selenium. By ID
     * 
     * @param key of the item to be selected. In the related selector file should exists an entry with: key.type and key.id
     * @param timeOut limit for wait until appear.
     * @return true if the element exists and it's visible.
     */
    public boolean isElementVisibleById(String key, long timeOut) {
        boolean showed = false;
        WebElement element = this.getElementById(key, timeOut);
        if (isVisible(element)) {
            showed = true;
        }
        return showed;
    }

    /**
     * This method checks if a {@link WebElement} is displayed and visible for selenium. By ID
     * This method uses the TIMEOUT constant.
     * 
     * @param key of the item to be selected. In the related selector file should exists an entry with: key.type and key.id
     * @return true if the element exists and it's visible.
     */
    public boolean isElementVisibleById(String key) {
        return this.isElementVisibleById(key, TIMEOUT);
    }

    // **** Name methods section ****//
    /**
     * This method interacts with Selenium to retrieve the needed element. By Name
     * 
     * @param key of the item to be selected. In the related selector file should exists an entry with: key.type and key.name
     * @param timeOut limit for wait until appear.
     * @return the selected {@link Webelement} object
     */
    public WebElement getElementByName(String key, long timeOut) {
        WebElement element = null;
        PropertiesHandler handler = PropertiesHandler.getInstance();
        handler.load(this.getSelectorsFilePath(key + ".name"));
        String type = handler.get(key + ".type"); // Could be null
        String name = handler.get(key + ".name"); // getElementByNameJustName

        if (type == null && StringUtils.isNotBlank(name)) {
            element = this.getElementByNameJustName(name, timeOut);
        } else {
            if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
                element = this.getElementByName(type, name, timeOut);
            } else {
                log.error("Trying to retrieve from " + this.getSelectorsFilePath(key + ".name")
                        + " file the item with the key " + key + " but " + key + ".type and/or " + key
                        + ".name are missing!");
            }
        }
        return element;
    }

    /**
     * This method interacts with Selenium to retrieve the needed element. By Name
     * This method uses the TIMEOUT constant.
     * 
     * @param key of the item to be selected. In the related selector file should exists an entry with: key.type and key.name
     * @return the selected {@link Webelement} object
     */
    public WebElement getElementByName(String key) {
        return this.getElementByName(key, TIMEOUT);
    }

    /**
     * This method checks if a {@link WebElement} is displayed and visible for selenium. By Name
     * 
     * @param key of the item to be selected. In the related selector file should exists an entry with: key.type and key.name
     * @param timeOut limit for wait until appear.
     * @return true if the element exists and it's visible.
     */
    public boolean isElementVisibleByName(String key, long timeOut) {
        boolean showed = false;
        WebElement element = this.getElementByName(key, timeOut);
        if (isVisible(element)) {
            showed = true;
        }
        return showed;
    }

    /**
     * This method checks if a {@link WebElement} is displayed and visible for selenium. By Name
     * This method uses the TIMEOUT constant.
     * 
     * @param key of the item to be selected. In the related selector file should exists an entry with: key.type and key.name
     * @return true if the element exists and it's visible.
     */
    public boolean isElementVisibleByName(String key) {
        return this.isElementVisibleByName(key, TIMEOUT);
    }

    // **** XPath methods section ****//
    /**
     * This method interacts with selenium to retrieve the needed element. By XPath
     * 
     * @param key of the item to be selected. In the related selector file should exists an entry with: key.xpath
     * @param timeOut limit for wait until appear.
     * @return the selected {@link WebElement} object.
     */
    public WebElement getElementByXPath(String key, long timeOut) {
        WebElement element = null;
        PropertiesHandler handler = PropertiesHandler.getInstance();
        handler.load(this.getSelectorsFilePath(key + ".xpath"));
        String xpath = handler.get(key + ".xpath");

        if (StringUtils.isNotBlank(xpath)) {
            element = this.getElementByXpath(xpath, timeOut);
        } else {
            log.error("Trying to retrieve from " + this.getSelectorsFilePath(key + ".xpath")
                    + " file the item with the key " + key + " but " + key + ".xpath is missing!");
        }
        return element;
    }

    /**
     * This method interacts with selenium to retrieve the needed element. By XPath
     * This method uses the TIMEOUT constant.
     * 
     * @param key of the item to be selected. In the related selector file should exists an entry with: key.xpath
     * @return the selected {@link WebElement} object.
     */
    public WebElement getElementByXPath(String key) {
        return this.getElementByXPath(key, TIMEOUT);
    }

    /**
     * This method interacts with selenium to retrieve the list of needed element. By xpath
     * 
     * @param key of the items to be selected. In the related selector file should exists an entry with: key.xpath
     * @param timeOut limit for wait until appear.
     * @return a List of the selected {@link WebElement} object
     */
    public List<WebElement> getElementsByXPath(String key, long timeOut) {
        List<WebElement> element = null;
        PropertiesHandler handler = PropertiesHandler.getInstance();
        handler.load(this.getSelectorsFilePath(key + ".xpath"));
        String xpath = handler.get(key + ".xpath");

        if (StringUtils.isNotBlank(xpath)) {
            element = this.getElementsByXpath(xpath, timeOut);
        } else {
            log.error("Trying to retrieve from " + this.getSelectorsFilePath(key + ".xpath")
                    + " file the item(s) with the key " + key + " but " + key + ".xpath is missing!");
        }
        return element;
    }

    /**
     * This method interacts with selenium to retrieve the list of needed element. By xpath
     * This method uses the TIMEOUT constant.
     * 
     * @param key of the items to be selected. In the related selector file should exists an entry with: key.xpath
     * @return a List of the selected {@link WebElement} object
     */
    public List<WebElement> getElementsByXPath(String key) {
        return this.getElementsByXPath(key, TIMEOUT);
    }

    /**
     * This method checks if a {@link WebElement} is displayed and visible for selenium. By xpath
     * 
     * @param xpath of the item to be selected. In the related selector file should exists an entry with and key.xpath
     * @param timeOut limit for wait until appear.
     * @return true if the element exists and it's visible
     */
    public boolean isElementVisibleByXPath(String key, long timeOut) {
        boolean showed = false;
        WebElement element = this.getElementByXPath(key, timeOut);
        if (element != null) {
            showed = true;
        }
        return showed;
    }

    /**
     * This method checks if a {@link WebElement} is displayed and visible for selenium. By xpath
     * This method uses the TIMEOUT constant.
     * 
     * @param xpath of the item to be selected. In the related selector file should exists an entry with and key.xpath
     * @return true if the element exists and it's visible
     */
    public boolean isElementVisibleByXPath(String key) {
        return this.isElementVisibleByXPath(key, TIMEOUT);
    }

    // **** Private methods section ****//
    /**
     * It checks if an WebElement is existing for selenium:
     * 
     * @param WebElement to be checked.
     * @return if exists the WebElement.
     */
    protected boolean exists(WebElement element) {
        boolean showed = false;
        if (element != null) {
            showed = true;
        }
        return showed;
    }

    /**
     * It checks if an WebElement is visible for selenium:
     * 
     * @param WebElement to be checked.
     * @return if is showed the WebElement.
     */
    protected boolean isVisible(WebElement element) {
        boolean showed = false;
        if (this.exists(element) && element.isDisplayed()) {
            showed = true;
        }
        return showed;
    }

    /**
     * This method interacts with selenium to retrieve the needed element by ID (just with ID):
     * 
     * @param id of the element to be retrieved.
     * @param timeOut limit for wait until appear.
     * @return the built id selector or null if it doesn't found it.
     */
    private WebElement getElementByIdJustId(String id, long timeOut) {
        this.driver.wait(By.id(id), timeOut);
        try {
            return this.driver.findElementById(id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method interacts with selenium to retrieve the needed element by ID:
     * 
     * @param type of the element to be retrieved.
     * @param id of the element to be retrieved.
     * @param timeOut limit for wait until appear.
     * @return the built id selector or null if it doesn't found it.
     */
    private WebElement getElementById(String type, String id, long timeOut) {
        this.driver.wait(By.id(this.buildIdSelector(type, id)), timeOut);
        try {
            return this.driver.findElementById(this.buildIdSelector(type, id));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method interacts with selenium to retrieve the needed element by name (just with name):
     * 
     * @param id of the element to be retrieved.
     * @param timeOut limit for wait until appear.
     * @return the built name selector or null if it doesn't found it.
     */
    private WebElement getElementByNameJustName(String name, long timeOut) {
        this.driver.wait(By.name(name), timeOut);
        try {
            return this.driver.findElementByName(name);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method interacts with selenium to retrieve the needed element by name:
     * 
     * @param type of the element to be retrieved.
     * @param name of the element to be retrieved.
     * @param timeOut limit for wait until appear.
     * @return the built name selector or null if it doesn't found it.
     */
    private WebElement getElementByName(String type, String name, long timeOut) {
        this.driver.wait(By.name(this.buildIdSelector(type, name)), timeOut);
        try {
            return this.driver.findElementByName(this.buildIdSelector(type, name));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method interacts with selenium to retrieve the needed element by xpath:
     * 
     * @param xpath of the element to be retrieved.
     * @param timeOut limit for wait until appear.
     * @return the built xpath selector or null if it doesn't found it.
     */
    private WebElement getElementByXpath(String xpath, long timeOut) {
        this.driver.wait(By.xpath(xpath), timeOut);
        try {
            List<WebElement> elements = this.driver.findElementsByXPath(xpath);
            if (elements != null && !elements.isEmpty()) {
                return elements.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method interacts with selenium to retrieve the needed elements by xpath:
     * 
     * @param xpath of the elements to be retrieved.
     * @param timeOut limit for wait until appear.
     * @return the built xpath list or null if it doesn't found it.
     */
    private List<WebElement> getElementsByXpath(String xpath, long timeOut) {
        this.driver.wait(By.xpath(xpath), timeOut);
        try {
            return this.driver.findElementsByXPath(xpath);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method builds the id selector using the UI logic
     * 
     * @param type
     *            of the element to be retrieved
     * @param id
     *            of the element to be retrieved
     * @return the built id selector
     */
    private String buildIdSelector(String type, String id) {
        String buttonSelector = type + "-" + id;
        return buttonSelector;
    }

    /**
     * To get the Xpath of a WebElement
     * @param key
     * @return String xpath
     */
    protected String getXPath(String key) {
        PropertiesHandler handler = PropertiesHandler.getInstance();
        handler.load(this.getSelectorsFilePath(key + ".xpath"));
        String xpath = handler.get(key + ".xpath");
        return xpath;
    }

    // ---------- WAIT FOR methods
    /**
     * Wait for element By ID, with time limit defined:
     * @param key of the element to search.
     * @param timeOut limit for searching.
     */
    protected void waitForById(String key, long timeOut) {
        PropertiesHandler handler = PropertiesHandler.getInstance();
        handler.load(this.getSelectorsFilePath(key + ".id"));
        // String type = handler.get(key + ".type"); // Could be null if the ID
        // is not own
        String id = handler.get(key + ".id"); // getElementByIdJustId

        // If the ID is found
        if (StringUtils.isNotBlank(id)) {
            this.driver.wait(By.xpath("//*[@id='" + id + "']"), timeOut);
        } else { // Else, the ID is not in .properties
            log.error("Trying to find from " + this.getSelectorsFilePath(key + ".id") + " file the item with the key "
                    + key + " but " + key + ".id is missing!");
        }
    }

    /**
     * Wait for element By ID, with time limit defined.
     * This method uses the TIMEOUT constant:
     * @param key of the element to search.
     */
    protected void waitForById(String key) {
        this.waitForById(key, TIMEOUT);
    }

    /**
     * Wait for element By Xpath, with time limit defined:
     * @param key of the element to search.
     * @param timeOut limit for searching.
     */
    protected void waitForByXPath(String key, long timeOut) {
        String xpath = this.getXPath(key);

        if (xpath != null && StringUtils.isNotBlank(xpath)) {
            this.driver.wait(By.xpath(xpath), timeOut);
        } else { // Else, the ID is not in .properties
            log.error("Trying to find from " + this.getSelectorsFilePath(key + ".xpath")
                    + " file the item with the key " + key + " but " + key + ".xpath is missing!");
        }
    }

    /**
     * Wait for element By Xpath, with time limit defined.
     * This method uses the TIMEOUT constant:
     * @param key of the element to search.
     */
    protected void waitForByXPath(String key) {
        this.waitForByXPath(key, TIMEOUT);
    }

    /**
     * Wait for element By WebElement:
     * @param element to search for.
     * @param timeOut limit for searching.
     */
    protected void waitForByElement(WebElement element, long timeOut) {
        long start = new Date().getTime();
        long end = start + (timeOut * 1000);
        long now = new Date().getTime();

        try {
            while (!element.isDisplayed() && now <= end) {
                now = new Date().getTime();
            }
        } catch (Exception ex) {
            // this try-catch is needed because if the element is hidden,
            // isDisplayed works fine but
            // if the element is removed from the DOM, we got a Selenium
            // Exception.
            log.error("Trying to find the WebElement but is missing!: " + ex.toString());
        }
    }

    /**
     * Wait for element By WebElement.
     * This method uses the TIMEOUT constant:
     * @param element to search for.
     */
    protected void waitForByElement(WebElement element) {
        this.waitForByElement(element, TIMEOUT);
    }

    // ---------- WAIT UNTIL DISAPPEAR methods
    /**
     * Wait until WebElement disappear:
     * @param element to wait until disappear.
     * @param timeOut limit for wait until disappear.
     */
    protected void waitUntilDisappearWebElement(WebElement element, long timeOut) {
        long start = new Date().getTime();
        long end = start + (timeOut * 1000);
        long now = new Date().getTime();

        try {
            do {
                now = new Date().getTime();
            } while (element.isDisplayed() && now <= end);
        } catch (Exception ex) {
            // this try-catch is needed because if the element is hidden,
            // isDisplayed works fine but
            // if the element is removed from the DOM, we got a Selenium
            // Exception.
            log.error("Trying to find the WebElement but is missing!: " + ex.toString());
        }
    }

    /**
     * Wait until WebElement disappear.
     * This method uses the TIMEOUT constant:
     * @param element to wait until disappear.
     */
    protected void waitUntilDisappearWebElement(WebElement element) {
        this.waitUntilDisappearWebElement(element, TIMEOUT);
    }

    /**
     * Wait until element disappear By Xpath:
     * @param key of the element to wait until disappear.
     * @param timeOut limit for wait until disappear.
     */
    protected void waitUntilDisappearByXPath(String key, long timeOut) {
        long start = new Date().getTime();
        long end = start + (timeOut * 1000);
        long now = new Date().getTime();
        List<WebElement> elements = null;

        do {
            elements = driver.findElements(By.xpath(this.getXPath(key)));
            now = new Date().getTime();
        } while (elements != null && !elements.isEmpty() && now <= end);
    }

    /**
     * Wait until element disappear By Xpath.
     * This method uses the TIMEOUT constant:
     * @param key of the element to wait until disappear.
     */
    protected void waitUntilDisappearByXPath(String key) {
        this.waitUntilDisappearByXPath(key, TIMEOUT);
    }

    /**
     * Wait until element disappear By ID:
     * @param key of the element to wait until disappear.
     * @param timeOut limit for wait until disappear.
     */
    protected void waitUntilDisappearByID(String key, long timeOut) {
        PropertiesHandler handler = PropertiesHandler.getInstance();
        handler.load(this.getSelectorsFilePath(key + ".id"));
        String id = handler.get(key + ".id");

        long start = new Date().getTime();
        long end = start + (timeOut * 1000);
        long now = new Date().getTime();
        List<WebElement> elements = null;

        do {
            elements = driver.findElements(By.id(id));
            now = new Date().getTime();
        } while (elements != null && !elements.isEmpty() && now <= end);
    }

    /**
     * Wait until element disappear By ID.
     * This method uses the TIMEOUT constant:
     * @param key of the element to wait until disappear.
     */
    protected void waitUntilDisappearByID(String key) {
        this.waitUntilDisappearByID(key, TIMEOUT);
    }

    /**
     * This function emulates a Scroll to see an element, using JavaScript.
     * @param element to search.
     */
    public void scrollTo(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            log.error("Cannot Scroll to the element, by JavaScript: " + e.toString());
        }
    }

    /**
     * This function emulates a Scroll Top action, using JavaScript.
     */
    public void scrollTop() {
        try {
            driver.executeJavaScript("window.scrollTo(0, 0)");
        } catch (Exception e) {
            log.error("Cannot ScrollTop the page, by JavaScript: " + e.toString());
        }
    }

    /**
     * This function emulates a Scroll Bottom action, using JavaScript.
     */
    public void scrollBottom() {
        try {
            driver.executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
        } catch (Exception e) {
            log.error("Cannot ScrollBottom the page, by JavaScript: " + e.toString());
        }
    }
}
