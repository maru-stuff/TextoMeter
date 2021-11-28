package marustuff.textometer.service;

import lombok.RequiredArgsConstructor;
import marustuff.textometer.MalformedWebsiteException;
import marustuff.textometer.model.Metering;
import marustuff.textometer.model.Website;
import marustuff.textometer.repository.WebsiteRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;


@Service
public class WebScraperService {
    private final static String linesplitRegex = "[^A-ZÃƒâ€¦Ãƒâ€žÃƒâ€“a-zÃƒÂ¥ÃƒÂ¤ÃƒÂ¶]+";
    private final static String emptyElement = "";

    private final Metering metering = new Metering();

    public WebScraperService() {
    }

    public Metering getMetering(Website website, String word) throws MalformedWebsiteException {
        this.metering.setWord(word);
        this.metering.setScore(0);
        Document doc;

        try {
            try{
                doc = Jsoup.connect(website.getAddress()).get();
            } catch (MalformedURLException e){
                e.printStackTrace();
                System.out.println("MalformedURLException");
                throw new MalformedWebsiteException(website);
            }
            try {
                String text = doc.body().text();
                BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
                System.out.println(text);
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split(linesplitRegex);
                    for (String element : words) {
                        if (emptyElement.equals(element)) {
                            continue;
                        }
                        if (element.equals(word)) {
                            this.metering.incScore();
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                return this.metering;
            }


        } catch (IOException e) {
            System.out.println("IOException");
            // powinieneś obsłużyć ten wyjątek, sam wyjątek Exception jest najbardziej ogólnym rodzajem wyjątków, czy nie miałeś tam bardziej precyzyjnych wyjątków?
            // warto by było również użyć chociaż loggera, poczytaj o Slf4j
            e.printStackTrace();
        }
        return this.metering;
    }

}

// ogólnie pamiętaj o używaniu CTRL+L, poprawia formatowanie kodu :)