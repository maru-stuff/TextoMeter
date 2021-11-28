package marustuff.textometer.service;

import lombok.RequiredArgsConstructor;
import marustuff.textometer.MalformedWebsiteException;
import marustuff.textometer.model.Metering;
import marustuff.textometer.model.MeteringComparison;
import marustuff.textometer.model.Website;
import marustuff.textometer.repository.MeteringRepository;
import marustuff.textometer.repository.WebsiteRepository;
import net.bytebuddy.pool.TypePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.net.MalformedURLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeteringService {
    public static final long timeFromCreationOfObjectInMinutes = 1;
    private static final String submitVSReturn = "redirect:/request/getvs/";
    private static final String submitWordReturn = "redirect:/request/get/";
    private static final String serveMeteringReturn = "request";
    private static final String serveMeteringModelAttributeName = "currentWord";
    private static final String serveVsMeteringReturnIfFound = "vs";
    private static final String serveVsMeteringModelAttributeName = "currentVs";
    private static final String testMeteringView = "debugMeteringRepository";

    @Autowired
    private final MeteringRepository meteringRepository;
    @Autowired
    private final WebScraperService webScraperService;
    @Autowired
    private final WebsiteRepository websiteRepository;
    @Autowired
    private final WebsiteService websiteService;

    public Metering createMetering(String word) {
        Metering sum = new Metering(word);
        for (Website website : websiteRepository.findAll()) {
            try{
                sum.AddMetering(webScraperService.getMetering(website, word));
            } catch (MalformedWebsiteException e){
                System.out.println("create metering, malformed website exception");
                websiteService.removeWebsite(e.website);
                break;


            }
        }
        return sum;
    }

    public void saveMeteringToRepository(Metering metering) {
        try {
            meteringRepository.deleteById(metering.getWord());
        } catch (Exception exception) {
            meteringRepository.save(metering);
        }
        meteringRepository.save(metering);
    }

    public Metering pollMetering(String word) {
        Metering metering;
        Optional<Metering> meteringOptional = meteringRepository.findById(word);
        if (meteringOptional.isPresent()) {
            metering = meteringOptional.get();
            if (!isFresh(metering)) {
                metering = createMetering(word);
                saveMeteringToRepository(metering);
            }
        } else {
            metering = createMetering(word);
            saveMeteringToRepository(metering);
        }
        return metering;
    }


    private boolean isFresh(Metering metering) {
        Instant fresh = Instant.now().minus(timeFromCreationOfObjectInMinutes, ChronoUnit.MINUTES);
        return (metering.getCreatedAt().toEpochMilli() > fresh.toEpochMilli());
    }

    public String debugMetering(Model model) {
        model.addAttribute("currentWords", meteringRepository.findAll());
        return testMeteringView;
    }

    public String submitComparisonView(MeteringComparison vs) {
        return submitVSReturn + vs.getWord1() + "/" + vs.getWord2();

    }


    public String submitMeteringView(Metering metering) {
        return submitWordReturn + metering.getWord();
    }

    public String serveMeteringView(String word, Model model) {
        Metering metering = pollMetering(word);
        model.addAttribute(serveMeteringModelAttributeName, metering);
        return serveMeteringReturn;
    }


    public String serveComparisionMeteringView(String word, String word2, Model model) {
        MeteringComparison meteringComparison = new MeteringComparison(pollMetering(word), pollMetering(word2));
        model.addAttribute(serveVsMeteringModelAttributeName, meteringComparison);
        return serveVsMeteringReturnIfFound;
    }

}
