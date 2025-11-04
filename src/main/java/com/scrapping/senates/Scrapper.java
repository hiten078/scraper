package com.scrapping.senates;

// Jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// Jackson imports
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

// Java utilities
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//public class Scrapper {

//    public static void main(String[] args) {
//        final String URL = "https://akleg.gov/senate.php";
//        List<Senator> senators = new ArrayList<>();
//
//        System.out.println("Connecting to " + URL + "...");
//
//        try {
//            Document doc = Jsoup.connect(URL).get();
//            System.out.println("Successfully connected. Parsing document...");
//
//            // 3. Select all senator "cards"
//            // *** UPDATED SELECTOR (FIX 1) ***
//            // This now *only* selects <li> tags inside the visible "tab1-2" div.
//            // This will fix the 44-card issue AND the 4 NullPointer errors.
//            Elements senatorCards = doc.select("div#tab1-2 ul.item > li");
//            System.out.println("Found " + senatorCards.size() + " senator cards.");
//
//            // 4. Loop through each card
//            for (Element card : senatorCards) {
//                Senator senator = new Senator();
//
//                // 5. Use a try-catch block for resilience
//                try {
//                    // --- Extract Data using new structure ---
//
//                    // Get Name, URL, and Title
//                    Element nameLink = card.selectFirst("a[href*=Member]");
//                    Element nameStrong = card.selectFirst("strong.name");
//
//                    senator.setUrl(nameLink.absUrl("href"));
//                    senator.setName(nameStrong.text());
//                    senator.setTitle("Senator");
//
//                    // Get Email
//                    // *** UPDATED SELECTOR (FIX 2) ***
//                    // This is a more specific path to the email link.
//                    // --- Get Email (Robust Method) ---
//// We will loop through all <dd> tags, as the email link is in one
//// without a matching <dt> tag.
//
//                    String email= "NA" ; // Set a default value
//                    Elements allDdTags = card.select("dl > dd"); // Get all <dd> tags inside the <dl>
//
//                    for (Element dd : allDdTags) {
//                        Element emailLink = dd.selectFirst("a[href*=mailto:]");
//                        if (emailLink != null) {
//                            // We found the mailto link!
////                            email = emailLink.attr("href").replace("mailto:", "");
//                            email = emailLink.toString();
//                            senator.setEmail(emailLink.absUrl("href"));
//                            System.out.println("EMAILfound" + emailLink);
//
//                            break; // Stop looping, we're done.
//                        }
//                    }
////                    senator.setEmail(email);
//
//
//                    // --- Use the new <dl> list structure ---
//                    senator.setParty(getDdTextByDt(card, "Party:"));
//                    senator.setPosition(getDdTextByDt(card, "District:"));
//                    senator.setPhone(getDdTextByDt(card, "Phone:"));
//                    senator.setAddress(getDdTextByDt(card, "City:"));
//
//                    senators.add(senator);
//                    System.out.println("Successfully parsed: " + senator.getName());
//
//                } catch (Exception e) {
//                    System.err.println("Error parsing one card. Skipping. Error: " + e.getMessage());
//                    e.printStackTrace(); // Print the full error to help debug
//                }
//            } // End of loop
//
//        } catch (IOException e) {
//            System.err.println("Failed to connect or read from URL: " + e.getMessage());
//            e.printStackTrace();
//            return;
//        }
//
//        // 6. Write the data to a JSON file
//        if (senators.isEmpty()) {
//            System.out.println("No senators were parsed. Exiting.");
//            return;
//        }
//
//        System.out.println("\nParsing complete. Writing " + senators.size() + " senators to JSON file...");
//
//        File outputFile = new File("senators.json");
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//        try {
//            mapper.writeValue(outputFile, senators);
//            System.out.println("Successfully wrote data to: " + outputFile.getAbsolutePath());
//        } catch (IOException e) {
//            System.err.println("Error writing JSON file: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * A helper method to find a <dt> tag by its text, then return the
//     * text of the immediately following <dd> tag.
//     */
//    private static String getDdTextByDt(Element card, String dtText) {
//        Element dt = card.selectFirst("dt:contains(" + dtText + ")");
//        if (dt != null) {
//            Element dd = dt.nextElementSibling();
//            if (dd != null && dd.tagName().equals("dd")) {
//                return dd.text();
//            }
//        }
//        return "N/A";
//    }
//public static void main(String[] args) {
//    final String URL = "https://akleg.gov/senate.php";
//    List<Senator> senators = new ArrayList<>();
//
//    System.out.println("Connecting to " + URL + "...");
//
//    try {
//        Document doc = Jsoup.connect(URL).get();
//        System.out.println("Successfully connected. Parsing document...");
//
//        // This selector is correct:
//        Elements senatorCards = doc.select("div#tab1-2 ul.item > li");
//        System.out.println("Found " + senatorCards.size() + " senator cards.");
//
//        // Loop through each card
//        for (Element card : senatorCards) {
//            Senator senator = new Senator();
//
//            // Use a try-catch block for resilience
//            try {
//                // Get Name, URL, and Title
//                Element nameLink = card.selectFirst("a[href*=Member]");
//                Element nameStrong = card.selectFirst("strong.name");
//
//                senator.setUrl(nameLink.absUrl("href"));
//                senator.setName(nameStrong.text());
//                senator.setTitle("Senator");
//
//                // --- NEW EMAIL DEBUGGING BLOCK ---
//                String email = "N/A"; // Set default
//                Elements ddTags = card.select("dl dd"); // Get all <dd> tags
//
//                for (Element dd : ddTags) {
//                    // Look for a link inside this <dd>
//                    Element emailLink = dd.selectFirst("a[href^=mailto:]");
//                    if (emailLink != null) {
//                        // Found it!
//                        email = emailLink.attr("href").replace("mailto:", "");
//                        break; // Stop looping
//                    }
//                }
//
//                senator.setEmail(email);
//
//                // *** THE NEW DEBUGGING LINE ***
//                if (email.equals("N/A")) {
//                    System.err.println("!!! FAILED TO FIND EMAIL for " + senator.getName());
//                    System.err.println("--- CARD HTML ---");
//                    System.err.println(card.html()); // Print the card's inner HTML
//                    System.err.println("-----------------");
//                }
//                // --- END NEW DEBUGGING BLOCK ---
//
//                // --- Use the <dl> list structure ---
//                senator.setParty(getDdTextByDt(card, "Party:"));
//                senator.setPosition(getDdTextByDt(card, "District:"));
//                senator.setPhone(getDdTextByDt(card, "Phone:"));
//                senator.setAddress(getDdTextByDt(card, "City:"));
//
//                senators.add(senator);
//                // Don't print "Successfully parsed" yet
//
//            } catch (Exception e) {
//                System.err.println("Error parsing one card. Skipping. Error: " + e.getMessage());
//                e.printStackTrace(); // Print the full error to help debug
//            }
//        } // End of loop
//
//    } catch (IOException e) {
//        System.err.println("Failed to connect or read from URL: " + e.getMessage());
//        e.printStackTrace();
//        return; // Exit if we can't connect
//    }
//
//    // 6. Write the data to a JSON file
//    if (senators.isEmpty()) {
//        System.out.println("No senators were parsed. Exiting.");
//        return;
//    }
//
//    System.out.println("\nParsing complete. Writing " + senators.size() + " senators to JSON file...");
//
//    // Print final list for verification
//    for (Senator s : senators) {
//        System.out.println("  -> Parsed: " + s.getName() + " | Email: " + s.getEmail());
//    }
//
//    File outputFile = new File("senators.json");
//    ObjectMapper mapper = new ObjectMapper();
//    mapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//    try {
//        mapper.writeValue(outputFile, senators);
//        System.out.println("Successfully wrote data to: " + outputFile.getAbsolutePath());
//    } catch (IOException e) {
//        System.err.println("Error writing JSON file: " + e.getMessage());
//        e.printStackTrace();
//    }
//}
//
//    // This helper method is unchanged and should be below your main method
//    private static String getDdTextByDt(Element card, String dtText) {
//        Element dt = card.selectFirst("dt:contains(" + dtText + ")");
//        if (dt != null) {
//            Element dd = dt.nextElementSibling();
//            if (dd != null && dd.tagName().equals("dd")) {
//                return dd.text();
//            }
//        }
//        return "N/A";
//    }

//} // <-- Make sure this matches your package name!

// Jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// Jackson imports
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

// Java utilities
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scrapper {

    public static void main(String[] args) {
        final String URL = "https://akleg.gov/senate.php";
        List<Senator> senators = new ArrayList<>();

        System.out.println("Connecting to " + URL + "...");

        try {
            Document doc = Jsoup.connect(URL).get();
            System.out.println("Successfully connected. Parsing document...");

            Elements senatorCards = doc.select("div#tab1-2 ul.item > li");
            System.out.println("Found " + senatorCards.size() + " senator cards.");

            // Loop through each card
            for (Element card : senatorCards) {
                Senator senator = new Senator();

                try {
                    // Get Name, URL, and Title
                    Element nameLink = card.selectFirst("a[href*=Member]");
                    Element nameStrong = card.selectFirst("strong.name");

                    senator.setUrl(nameLink.absUrl("href"));
                    senator.setName(nameStrong.text());
                    senator.setTitle("Senator");

                    // --- NEW EMAIL LOGIC ---
                    // This time, we find the "email-protection" link
                    Element obfuscatedEmailLink = card.selectFirst("a[href^='/cdn-cgi/l/email-protection']");

                    if (obfuscatedEmailLink != null) {
                        // We found the link. Get the 'href' attribute's value.
                        String obfuscatedHref = obfuscatedEmailLink.attr("href");

                        // Grab the hex string (the part after the #)
                        String obfuscatedEmailData = obfuscatedHref.substring(obfuscatedHref.lastIndexOf('#') + 1);

                        // Deobfuscate it!
                        String deobfuscatedEmail = deobfuscateEmail(obfuscatedEmailData);
                        senator.setEmail(deobfuscatedEmail);

                    } else {
                        senator.setEmail("N/A");
                    }
                    // --- END NEW EMAIL LOGIC ---

                    // --- Get other data (this was already working) ---
                    senator.setParty(getDdTextByDt(card, "Party:"));
                    senator.setPosition(getDdTextByDt(card, "District:"));
                    senator.setPhone(getDdTextByDt(card, "Phone:"));
                    senator.setAddress(getDdTextByDt(card, "City:"));

                    senators.add(senator);
                    System.out.println("Successfully parsed: " + senator.getName() + " | Email: " + senator.getEmail());

                } catch (Exception e) {
                    System.err.println("Error parsing one card. Skipping. Error: " + e.getMessage());
                    e.printStackTrace();
                }
            } // End of loop

        } catch (IOException e) {
            System.err.println("Failed to connect or read from URL: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 6. Write the data to a JSON file
        if (senators.isEmpty()) {
            System.out.println("No senators were parsed. Exiting.");
            return;
        }

        System.out.println("\nParsing complete. Writing " + senators.size() + " senators to JSON file...");

        File outputFile = new File("senators.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(outputFile, senators);
            System.out.println("Successfully wrote data to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * A helper method to find a <dt> tag by its text, then return the
     * text of the immediately following <dd> tag.
     */
    private static String getDdTextByDt(Element card, String dtText) {
        Element dt = card.selectFirst("dt:contains(" + dtText + ")");
        if (dt != null) {
            Element dd = dt.nextElementSibling();
            if (dd != null && dd.tagName().equals("dd")) {
                return dd.text();
            }
        }
        return "N/A";
    }

    /**
     * De-obfuscates a Cloudflare-protected email address.
     * @param hexString The hexadecimal string from the href attribute (without the #).
     * @return The de-obfuscated email address.
     */
    private static String deobfuscateEmail(String hexString) {
        try {
            // This is the core logic of the Cloudflare deobfuscator
            // 1. Get the key (the first two hex chars, or 1 byte)
            int key = Integer.parseInt(hexString.substring(0, 2), 16);

            // 2. Build the email string
            StringBuilder emailBuilder = new StringBuilder();

            // 3. Loop through the rest of the string, 2 chars at a time
            for (int i = 2; i < hexString.length(); i += 2) {
                // Get the current hex char pair
                int charCode = Integer.parseInt(hexString.substring(i, i + 2), 16);

                // 4. XOR the char code with the key
                int deobfuscatedCharCode = charCode ^ key;

                // 5. Append the resulting character
                emailBuilder.append((char) deobfuscatedCharCode);
            }

            return emailBuilder.toString();
        } catch (Exception e) {
            // In case of any error (e.g., bad hex string)
            System.err.println("Error deobfuscating email: " + e.getMessage());
            return "N/A";
        }
    }
}