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
    private static final String errorWebsiteEmptyOrMalformedView = "errorWebsiteEmptyOrMalformed";
    private static final String listWebsitesReturn = "list";
    private static final String listWebsitesModelAttributeName = "currentWebsites";
    private static final String addWebsiteReturn = "add";
    private static final String addWebsiteModelAttributeName = "website";
    private static final String saveWebsiteReturn = "redirect:/website/list";
    private static final String httpProtocol = "http://";
    private static final String httpsProtocol = "https://";
    private static final String errorMalformedWebsiteAddressLogging = "Website address provided was malformed, website address: ";

    @NonNull
    @Autowired
    private final WebsiteRepository websiteRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String serveListWebsites(Model model) {
        model.addAttribute(listWebsitesModelAttributeName, websiteRepository.findAll());
        return listWebsitesReturn;
    }

    public String serveAddWebsite(Model model) {
        Website website = new Website();
        model.addAttribute(addWebsiteModelAttributeName, website);
        return addWebsiteReturn;
    }

    public String serveSaveWebsite(Website website) {
        try {
            saveWebsite(website);
            return saveWebsiteReturn;
        } catch (MalformedWebsiteException e) {
            logger.error(errorMalformedWebsiteAddressLogging + website.getAddress());
            return errorWebsiteEmptyOrMalformedView;
        }
    }

    public void removeWebsite(Website website) {
        websiteRepository.deleteById(website.getId());
    }

    public String removeWebsite(Long id) {
        if(websiteRepository.existsById(id)) {
            websiteRepository.deleteById(id);
            return "200 OK";
        } else {
            return "404 not found";
    }

}

    public void checkUrl(Website website) throws MalformedURLException {
        if (!website.getAddress().startsWith(httpProtocol) && !website.getAddress().startsWith(httpsProtocol)) {
            throw new MalformedURLException();
        } else {
            Jsoup.connect(website.getAddress());
        }
    }

    public Iterable<Website> findAll() throws  EmptyWebsiteRepositoryException{
            isRepositoryEmpty();
            return websiteRepository.findAll();
    }

    private void isRepositoryEmpty() throws EmptyWebsiteRepositoryException {
        if(websiteRepository.count()==0){
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
            logger.error(errorMalformedWebsiteAddressLogging + website.getAddress());
        }
    }

}
