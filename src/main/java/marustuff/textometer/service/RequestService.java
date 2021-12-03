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
    private static final String ERROR_EMPTY_WORD_VIEW = "errorEmptyWord";
    private static final long TIME_FROM_CREATION_OF_OBJECT_IN_MINUTES = 1;
    private static final String SUBMIT_VS_RETURN = "redirect:/request/getvs?word=";
    private static final String SUBMIT_VS_SEPARATOR = "&word2=";
    private static final String SUBMIT_WORD_RETURN = "redirect:/request/get?word=";
    private static final String SERVE_METERING_RETURN = "request";
    private static final String SERVE_METERING_MODEL_ATTRIBUTE_NAME = "currentWord";
    private static final String SERVE_VS_METERING_RETURN_FOUND = "vs";
    private static final String SERVE_VS_METERING_MODEL_ATTRIBUTE_NAME = "currentVs";
    private static final String TEST_METERING_VIEW = "debugMeteringRepository";
    private static final String TEST_METERING_VIEW_ATTRIBUTE_NAME = "currentWords";
    private static final String STANDARD_ERROR_VIEW = "error";
    private static final String EMPTY = "";
    private static final String ERROR_EMPTY_WORD_LOGGING = "One or both of words were empty.";
    private static final String ERROR_MALFORMED_WEBSITE_ADDRESS_LOGGING = "Website address provided was malformed, website address: ";
    private static final String ERROR_EMPTY_WEBSITE_BODY_LOGGING = "Scraped website body is empty, website address: ";
    private static final String ERROR_EMPTY_WEBSITE_REPOSITORY_VIEW = "errorEmptyWebsiteRepository";
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
                logger.error(ERROR_MALFORMED_WEBSITE_ADDRESS_LOGGING + website.getAddress());
                break;
            } catch (IOException e) {
                logger.error(ERROR_EMPTY_WEBSITE_BODY_LOGGING + website.getAddress());
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
        Instant fresh = Instant.now().minus(TIME_FROM_CREATION_OF_OBJECT_IN_MINUTES, ChronoUnit.MINUTES);
        return (metering.getCreatedAt().toEpochMilli() > fresh.toEpochMilli());
    }

    public String debugMetering(Model model) {
        model.addAttribute(TEST_METERING_VIEW_ATTRIBUTE_NAME, meteringRepository.findAll());
        return TEST_METERING_VIEW;
    }

    public String submitComparisonView(MeteringComparison meteringComparison) {
        if (meteringComparison.getWord1().equals(EMPTY) || meteringComparison.getWord2().equals(EMPTY)) {
            logger.error(ERROR_EMPTY_WORD_LOGGING);
            return ERROR_EMPTY_WORD_VIEW;
        } else {
            return SUBMIT_VS_RETURN + meteringComparison.getWord1() + SUBMIT_VS_SEPARATOR + meteringComparison.getWord2();
        }
    }


    public String submitMeteringView(Metering metering) {
        if (metering.getWord().equals(EMPTY)) {
            logger.error(ERROR_EMPTY_WORD_LOGGING);
            return ERROR_EMPTY_WORD_VIEW;
        } else {
            return SUBMIT_WORD_RETURN + metering.getWord();
        }
    }

    public String getMeteringView(String word, Model model) {
        try {
            Metering metering = pollMetering(word);
            model.addAttribute(SERVE_METERING_MODEL_ATTRIBUTE_NAME, metering);
            return SERVE_METERING_RETURN;
        } catch (EmptyWebsiteRepositoryException e) {
            return ERROR_EMPTY_WEBSITE_REPOSITORY_VIEW;
        }

    }


    public String getComparisonMeteringView(String word, String word2, Model model) {
        try {
            MeteringComparison meteringComparison = new MeteringComparison(pollMetering(word), pollMetering(word2));
            model.addAttribute(SERVE_VS_METERING_MODEL_ATTRIBUTE_NAME, meteringComparison);
            return SERVE_VS_METERING_RETURN_FOUND;
        }catch(EmptyWebsiteRepositoryException e){
            return ERROR_EMPTY_WEBSITE_REPOSITORY_VIEW;
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
