package marustuff.textometer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/poll")
@Component
@RequiredArgsConstructor
public class MeteringPoller {
    @NonNull
    @Autowired
    private final MeteringRepository repository;
    private WebScraperEndpoint we = new WebScraperEndpoint();
    private ArrayList<String> websites =new ArrayList<String>();
    {
        websites.add("https://wp.pl/");
        websites.add("https://www.onet.pl/");
        websites.add("https://www.o2.pl/");
    }


    @GetMapping("/{word}")
    public String pollMeterings(@PathVariable String word){
        System.out.println(word);
        try{
            repository.deleteById(word);
        } catch (Exception e){
            Metering empty = new Metering(word,0);
            repository.save(empty);
        }
        Metering sum = new Metering(word);
        for(String website:websites){
            sum.AddMetering(we.getMetering(website,word));
        }

        System.out.println(sum.getScore() + " " + sum.getWord());
        repository.save(sum);
        return "test";
    }
}
