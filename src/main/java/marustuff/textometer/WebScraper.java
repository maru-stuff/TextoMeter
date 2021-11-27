package marustuff.textometer;

import marustuff.textometer.model.Metering;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@Service
public class WebScraper {
    private final String linesplitRegex="[^A-ZÃƒâ€¦Ãƒâ€žÃƒâ€“a-zÃƒÂ¥ÃƒÂ¤ÃƒÂ¶]+";
    private final String emptyElement="";
    //Metering jest niezmienne? Czy może być użyte wiele razy z takim samym kontekstem?
    private Metering metering = new Metering();

    public WebScraper(){
    }

    //To konstruktor, czy brak modyfikatora dostępu(domyślny to package private) jest tutaj celowy?
    /*WebScraper(String website, String word){
        this.metering=this.getMetering(website,word);
    }*/
    public Metering getMetering(String website,String word) {
        this.metering.setWord(word);
        this.metering.setScore(0);
        try {
            Document doc = Jsoup.connect(website).get();
            // czy body() zawsze jest not null? Masz tutaj ryzyko null pointer exception,, należałoby to sprawdzić
            String text=doc.body().text();
            // nie agreguj tak new :P brzydko wygląda i utrudnia czytelnosc, lepiej zrobic z nich lokalne zmienne, ktore przy okazji opiszą co tam się dzieje
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
            String line;
            while((line = reader.readLine()) != null){
                String[] words = line.split(linesplitRegex);
                for(String element : words){
                    if(emptyElement.equals(element)){
                        continue;
                    }
                    if(element.equals(word)){
                        this.metering.incScore();
                    }
                }
            }

        } catch (IOException e) {
            // powinieneś obsłużyć ten wyjątek, sam wyjątek Exception jest najbardziej ogólnym rodzajem wyjątków, czy nie miałeś tam bardziej precyzyjnych wyjątków?
            // warto by było również użyć chociaż loggera, poczytaj o Slf4j
            e.printStackTrace();
        }
        return this.metering;
    }

}

// ogólnie pamiętaj o używaniu CTRL+L, poprawia formatowanie kodu :)