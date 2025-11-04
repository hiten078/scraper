# Alaska Senate Web Scraper 🏛️
This is a Java project that scrapes the official Alaska State Legislature website to extract a clean, structured list of all current senators. The final data is exported into a senators.json file.

# Features
Scrapes the official senate roster (https://akleg.gov/senate.php).

Parses HTML using Jsoup for a lightweight and fast operation.

Successfully handles and decodes Cloudflare's email obfuscation using pure Java logic.


Exports all data into a "pretty-printed" senators.json file using Jackson.

# 🚀 Tech Stack
### Java: The core programming language.

### Maven: Used for project build and dependency management.

### Jsoup: For fetching and parsing the website's HTML.

### Jackson: For serializing the Java Senator objects into a JSON file.

# 🛠️ Project Journey & Technical Decisions
This project involved several key technical decisions to create an efficient and robust scraper. I read documentation for web scrapping in java and took help from the code provided in the documentation.

1. Key Decision: Jsoup vs. Selenium
   After initial research, I considered using Selenium, a powerful tool for browser automation. However, upon inspecting the target website, I determined it was almost entirely a static HTML page.

Using Selenium would be slow and resource-heavy for this task. I therefore chose Jsoup as the more lightweight, efficient, and appropriate tool for direct HTML parsing.

2. Data Modeling
   To keep the data organized, I created a Senator.java POJO. This class acts as a "blueprint" for a senator, defining and declaring the 8 fields we needed to fetch:

- `name`
- `title`
- `party`
- `position` (District)
- `address` (City)
- `phone`
- `email`
- `url`

3. The "Email" Problem: Overcoming Obfuscation
   The biggest technical challenge was that the email addresses were not present in the raw HTML. The site uses Cloudflare's email obfuscation to protect against scrapers. Instead of a mailto: link, the href attribute contained a scrambled hexadecimal string.

I had two paths forward:

#### Switch to Selenium: Selenium would run the site's JavaScript, which would de-obfuscate the email in the browser, making it easy to grab.

#### Reverse-Engineer the Logic: Stick with Jsoup, grab the obfuscated href string, and re-implement the de-obfuscation logic in Java.

I chose the second approach. It keeps the scraper lightweight, fast, and removes the need for a heavy Selenium dependency. I wrote a helper function that takes the hex string, uses the first byte as an XOR key, and decrypts the rest of the string to reveal the plain-text email address.

4. Final Output
   As the scraper loops through each senator's HTML "card," it populates a new Senator object. These objects are added to a List.

Finally, the Jackson library is used to serialize this List<Senator> into a well-formatted (pretty-printed) senators.json file in the project's root directory.

### TIME : The research, development, and debugging process (especially for the email obfuscation) took approximately 1.5-2 hours.

# ⚙️ How to Run
- `Clone this repository to your local machine.`

- `Ensure you have Java and Maven installed.`

- `Open a terminal and navigate to the project's root directory.`

- `Build the project and download dependencies by running:`

**Bash**
 ```
mvn clean install
```
Run the main application:

**Bash**
```
java -cp target/your-project-name.jar com.mycompany.senatescraper.Scraper
```
(Note: Replace your-project-name.jar and the package path with your project's actual names)

Once the program finishes, you will find a new senators.json file in the root folder.
