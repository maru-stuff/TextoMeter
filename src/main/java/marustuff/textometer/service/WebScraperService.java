package marustuff.textometer.service;

import marustuff.textometer.MalformedWebsiteException;
import marustuff.textometer.model.Metering;
import marustuff.textometer.model.Website;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;


@Service
public class WebScraperService {
    private final static String linesplitRegex = "[^A-ZÃƒâ€¦Ãƒâ€žÃƒâ€“a-zÃƒÂ¥ÃƒÂ¤ÃƒÂ¶]+";
    private final static String emptyElement = "";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public WebScraperService() {
    }

    public Metering getMetering(Website website, String word) throws IOException {
        Metering metering = new Metering();
        metering.setWord(word);
        metering.setScore(0);

        Document doc;

        try {
            doc = Jsoup.connect(website.getAddress()).get();
            if (doc.body().text().equals(null)) {
                throw new IOException();
            }
            String text = doc.body().text();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(linesplitRegex);
                for (String element : words) {
                    if (emptyElement.equals(element)) {
                    } else {
                        if (element.equals(word)) {
                            metering.incScore();
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Website has not responded properly " + website.getAddress());
        }
        return metering;
    }

}

// ogólnie pamiętaj o używaniu CTRL+L, poprawia formatowanie kodu :)