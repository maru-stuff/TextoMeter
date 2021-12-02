package marustuff.textometer.service;

import lombok.RequiredArgsConstructor;
import marustuff.textometer.EmptyWebsiteRepositoryException;
import marustuff.textometer.MalformedWebsiteException;
import marustuff.textometer.model.Metering;
import marustuff.textometer.model.MeteringComparison;
import marustuff.textometer.model.Website;
import marustuff.textometer.repository.MeteringRepository;
import marustuff.textometer.repository.WebsiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {
    public static final String errorEmptyWordView = "errorEmptyWord";
    private static final long timeFromCreationOfObjectInMinutes = 1;
    private static final String submitVSReturn = "redirect:/request/getvs?word=";
    private static final String submitVsSeparator = "&word2=";
    private static final String submitWordReturn = "redirect:/request/get?word=";
    private static final String serveMeteringReturn = "request";
    private static final String serveMeteringModelAttributeName = "currentWord";
    private static final String serveVsMeteringReturnFound = "vs";
    private static final String serveVsMeteringModelAttributeName = "currentVs";
    private static final String testMeteringView = "debugMeteringRepository";
    private static final String testMeteringViewAttributeName = "currentWords";
    private static final String standardErrorView = "error";
    private static final String empty = "";
    private static final String errorEmptyWordLogging = "One or both of words were empty.";
    private static final String errorMalformedWebsiteAddressLogging = "Website address provided was malformed, website address: ";
    private static final String errorEmptyWebsiteBodyLogging = "Scraped website body is empty, website address: ";
    private static final String errorEmptyWebisteRepositoryView = "errorEmptyWebsiteRepository";
    @Autowired
    private final MeteringRepository meteringRepository;
    @Autowired
    private final WebScraperService webScraperService;
    @Autowired
    private final WebsiteRepository websiteRepository;
    @Autowired
    private final WebsiteService websiteService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Metering createMetering(String word) throws EmptyWebsiteRepositoryException {
        Metering sum = new Metering(word);
        for (Website website : websiteService.findAll()) {
            try {
                sum.AddMetering(webScraperService.getMetering(website, word));
            } catch (MalformedWebsiteException e) {
                websiteService.removeWebsite(e.website);
                logger.error(errorMalformedWebsiteAddressLogging + website.getAddress());
                break;
            } catch (IOException e) {
                logger.error(errorEmptyWebsiteBodyLogging + website.getAddress());
            }
        }
        return sum;
    }

    public Iterable<Metering> findAll() {

        return meteringRepository.findAll();
    }


    public void saveMeteringToRepository(Metering metering) {
        try {
            meteringRepository.deleteById(metering.getWord());
        } catch (Exception exception) {
            meteringRepository.save(metering);
        }
        meteringRepository.save(metering);
    }

    public Metering pollMetering(String word) throws EmptyWebsiteRepositoryException {
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
        model.addAttribute(testMeteringViewAttributeName, meteringRepository.findAll());
        return testMeteringView;
    }

    public String submitComparisonView(MeteringComparison meteringComparison) {
        if (meteringComparison.getWord1().equals(empty) || meteringComparison.getWord2().equals(empty)) {
            logger.error(errorEmptyWordLogging);
            return errorEmptyWordView;
        } else {
            return submitVSReturn + meteringComparison.getWord1() + submitVsSeparator + meteringComparison.getWord2();
        }
    }


    public String submitMeteringView(Metering metering) {
        if (metering.getWord().equals(empty)) {
            logger.error(errorEmptyWordLogging);
            return errorEmptyWordView;
        } else {
            return submitWordReturn + metering.getWord();
        }
    }

    public String getMeteringView(String word, Model model) {
        try {
            Metering metering = pollMetering(word);
            model.addAttribute(serveMeteringModelAttributeName, metering);
            return serveMeteringReturn;
        } catch (EmptyWebsiteRepositoryException e) {
            return errorEmptyWebisteRepositoryView;
        }

    }


    public String getComparisonMeteringView(String word, String word2, Model model) {
        try {
            MeteringComparison meteringComparison = new MeteringComparison(pollMetering(word), pollMetering(word2));
            model.addAttribute(serveVsMeteringModelAttributeName, meteringComparison);
            return serveVsMeteringReturnFound;
        }catch(EmptyWebsiteRepositoryException e){
            return errorEmptyWebisteRepositoryView;
        }
    }

    public Metering findByWord(String word) {
        try{
            return pollMetering(word);
        }catch(EmptyWebsiteRepositoryException e){
            return null;
        }
    }
}
