package marustuff.textometer.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import marustuff.textometer.EmptyWebsiteRepositoryException;
import marustuff.textometer.MalformedWebsiteException;
import marustuff.textometer.model.Website;
import marustuff.textometer.repository.WebsiteRepository;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.net.MalformedURLException;

@Service
@RequiredArgsConstructor
public class WebsiteService {
    private static final String ERROR_WEBSITE_EMPTY_OR_MALFORMED_VIEW = "errorWebsiteEmptyOrMalformed";
    private static final String LIST_WEBSITES_RETURN = "list";
    private static final String LIST_WEBSITES_MODEL_ATTRIBUTE_NAME = "currentWebsites";
    private static final String ADD_WEBSITE_RETURN = "add";
    private static final String ADD_WEBSITE_MODEL_ATTRIBUTE_NAME = "website";
    private static final String SAVE_WEBSITE_RETURN = "redirect:/website/list";
    private static final String HTTP_PROTOCOL_STRING = "http://";
    private static final String HTTPS_PROTOCOL_STRING = "https://";
    private static final String ERROR_MALFORMED_WEBSITE_ADDRESS_LOGGING = "Website address provided was malformed, website address: ";

    @NonNull
    @Autowired
    private final WebsiteRepository websiteRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String serveListWebsites(Model model) {
        model.addAttribute(LIST_WEBSITES_MODEL_ATTRIBUTE_NAME, websiteRepository.findAll());
        return LIST_WEBSITES_RETURN;
    }

    public String serveAddWebsite(Model model) {
        Website website = new Website();
        model.addAttribute(ADD_WEBSITE_MODEL_ATTRIBUTE_NAME, website);
        return ADD_WEBSITE_RETURN;
    }

    public String serveSaveWebsite(Website website) {
        try {
            saveWebsite(website);
            return SAVE_WEBSITE_RETURN;
        } catch (MalformedWebsiteException e) {
            logger.error(ERROR_MALFORMED_WEBSITE_ADDRESS_LOGGING + website.getAddress());
            return ERROR_WEBSITE_EMPTY_OR_MALFORMED_VIEW;
        }
    }

    public void removeWebsite(Website website) {
        websiteRepository.deleteById(website.getId());
    }

    public String removeWebsite(Long id) {
        if (websiteRepository.existsById(id)) {
            websiteRepository.deleteById(id);
            return "200 OK";
        } else {
            return "404 not found";
        }

    }

    public void checkUrl(Website website) throws MalformedURLException {
        if (!website.getAddress().startsWith(HTTP_PROTOCOL_STRING) && !website.getAddress().startsWith(HTTPS_PROTOCOL_STRING)) {
            throw new MalformedURLException();
        } else {
            Jsoup.connect(website.getAddress());
        }
    }

    public Iterable<Website> findAll() throws EmptyWebsiteRepositoryException {
        isRepositoryEmpty();
        return websiteRepository.findAll();
    }

    private void isRepositoryEmpty() throws EmptyWebsiteRepositoryException {
        if (websiteRepository.count() == 0) {
            throw new EmptyWebsiteRepositoryException();
        }
    }


    public Website apiSaveWebsite(Website website) {
        website.setId(null);
        try {
            saveWebsite(website);
            return website;
        } catch (MalformedWebsiteException e) {
            return null;
        }
    }

    private void saveWebsite(Website website) throws MalformedWebsiteException {
        try {
            checkUrl(website);
            websiteRepository.save(website);
        } catch (MalformedURLException e) {
            logger.error(ERROR_MALFORMED_WEBSITE_ADDRESS_LOGGING + website.getAddress());
        }
    }

}
