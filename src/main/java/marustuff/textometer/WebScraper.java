package marustuff.textometer;

import marustuff.textometer.model.Metering;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


//To wygląda na serwis. Czy na pewno chciałeś z robić z tego zwykłą klasę?
public class WebScraper {
    //Metering jest niezmienne? Czy może być użyte wiele razy z takim samym kontekstem?
    private Metering metering = new Metering();

    public WebScraper(){
    }

    //To konstruktor, czy brak modyfikatora dostępu(domyślny to package private) jest tutaj celowy?
    WebScraper(String website, String word){
        this.metering=this.getMetering(website,word);
    }
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
                // to co zawarłeś w " " lepiej byłoby wynieść do statycznej wartości, zwiększy czytelność
                String[] words = line.split("[^A-ZÃƒâ€¦Ãƒâ€žÃƒâ€“a-zÃƒÂ¥ÃƒÂ¤ÃƒÂ¶]+");
                for(String element : words){
                    // tak samo z "" <-- co to dokładnie jest? lepiej wynieść to jako stałą, będziesz mógł spokojnie nadać jej nazwę, co zwiększy czytelność
                    if("".equals(element)){
                        continue;
                    }
                    if(element.equals(word)){
                        this.metering.incScore();
                    }
                }
            }

        } catch (Exception e) {
            // powinieneś obsłużyć ten wyjątek, sam wyjątek Exception jest najbardziej ogólnym rodzajem wyjątków, czy nie miałeś tam bardziej precyzyjnych wyjątków?
            // warto by było również użyć chociaż loggera, poczytaj o Slf4j
            e.printStackTrace();
        }
        return this.metering;
    }

}

// ogólnie pamiętaj o używaniu CTRL+L, poprawia formatowanie kodu :)