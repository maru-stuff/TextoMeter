package marustuff.textometer.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import marustuff.textometer.WebScraper;
import marustuff.textometer.model.Metering;
import marustuff.textometer.repository.MeteringRepository;
import marustuff.textometer.model.Website;
import marustuff.textometer.repository.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@RequestMapping("/poll")
@Controller
@RequiredArgsConstructor
public class PollingController {
    @NonNull
    @Autowired
    private final MeteringRepository repository;
    private final WebsiteRepository websiteRepository;
    private WebScraper we = new WebScraper();

    @GetMapping("/{word}")
    public String pollMeterings(@PathVariable String word){
        pollMetering(word);
        return "redirect:/request/get/"+word;
    }
    @GetMapping("/{word}/{word2}")
    public String pollVs(@PathVariable("word") String word, @PathVariable("word2") String word2){
        pollMetering(word);
        pollMetering(word2);
        return "redirect:/request/getvs/"+word+"/"+word2;
    }

    private void pollMetering(String word){
        System.out.println(word);
        try{
            repository.deleteById(word);
        } catch (Exception e){
            Metering empty = new Metering(word);
            repository.save(empty);
        }
        Metering sum = new Metering(word);
        for(Website website:websiteRepository.findAll()){
            sum.AddMetering(we.getMetering(website.getAddress(),word));
        }

        System.out.println(sum.getScore() + " " + sum.getWord());
        repository.save(sum);
    }
}
