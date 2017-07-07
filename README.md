# Selenium WebDriver Handler

##Description
It allows your Selenium projects abstracting the WebDriver logic for ease of use. Currently supports IE, Firefox and Chrome WebDriver but is extensible to others browsers. You can find an example of use of _qa-selenium-handler_ in this project https://github.com/Emergya/qa-emergya-quickstart

##Adding dependencies
 1. Add the repository in your ```pom.xml``` file:

   ```xml
  	<!-- GtHub Selenium WebDriver Handler Repository -->
	<repository>
		<id>selenium-handler</id>
		<name>Selenium WebDriver Handler</name>
		<url>https://raw.github.com/Emergya/qa-selenium-handler/mvn-repo</url>
	</repository>
    ```
 2. Adding the following maven dependency in your ```pom.xml``` file, make sure the version is the last one published:


    ```xml
	<dependency>
		<groupId>com.emergya</groupId>
		<artifactId>selenium-handler</artifactId>
		<version>0.1.6</version>
	</dependency>
    ```
