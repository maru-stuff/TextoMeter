package marustuff.textometer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static java.nio.file.Files.newBufferedReader;


public class WebScraperEndpoint {
    private Metering metering = new Metering();

    WebScraperEndpoint(){
        metering.setWord("");
        metering.setScore(0);
        System.out.println("dbg Obiekt utworzono");
    }

    WebScraperEndpoint(String website,String word){
        this.metering=this.getMetering(website,word);
    }

    public Metering getMetering(String website,String word) {
        this.metering.setWord(word);
        this.metering.setScore(0);
        try {
            Document doc = Jsoup.connect(website).get();
            String text=doc.body().text();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
            String line;
            while((line = reader.readLine()) != null){
               String[] words = line.split("[^A-ZÃƒâ€¦Ãƒâ€žÃƒâ€“a-zÃƒÂ¥ÃƒÂ¤ÃƒÂ¶]+");
               for(String element : words){
                   if("".equals(element)){
                       continue;
                   }
                   if(element.equals(word)){
                       this.metering.incScore();
                   }
               }

            }



            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.metering;
    }

    }

