package com.scrapping.senates;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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


            for (Element card : senatorCards) {
                Senator senator = new Senator();

                try {

                    Element nameLink = card.selectFirst("a[href*=Member]");
                    Element nameStrong = card.selectFirst("strong.name");

                    senator.setUrl(nameLink.absUrl("href"));
                    senator.setName(nameStrong.text());
                    senator.setTitle("Senator");

                    Element obfuscatedEmailLink = card.selectFirst("a[href^='/cdn-cgi/l/email-protection']");

                    if (obfuscatedEmailLink != null) {
                        String obfuscatedHref = obfuscatedEmailLink.attr("href");

                        String obfuscatedEmailData = obfuscatedHref.substring(obfuscatedHref.lastIndexOf('#') + 1);

                        String deobfuscatedEmail = deobfuscateEmail(obfuscatedEmailData);
                        senator.setEmail(deobfuscatedEmail);

                    } else {
                        senator.setEmail("N/A");
                    }

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
            }

        } catch (IOException e) {
            System.err.println("Failed to connect or read from URL: " + e.getMessage());
            e.printStackTrace();
            return;
        }

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

    private static String deobfuscateEmail(String hexString) {
        try {

            int key = Integer.parseInt(hexString.substring(0, 2), 16);

            StringBuilder emailBuilder = new StringBuilder();

            for (int i = 2; i < hexString.length(); i += 2) {
                int charCode = Integer.parseInt(hexString.substring(i, i + 2), 16);

                int deobfuscatedCharCode = charCode ^ key;

                emailBuilder.append((char) deobfuscatedCharCode);
            }

            return emailBuilder.toString();
        } catch (Exception e) {
            System.err.println("Error deobfuscating email: " + e.getMessage());
            return "N/A";
        }
    }
}