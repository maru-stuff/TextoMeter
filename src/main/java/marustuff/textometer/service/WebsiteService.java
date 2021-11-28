package marustuff.textometer.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
//import marustuff.textometer.Exceptions.WebsiteNullOrAddressEmpty;
import marustuff.textometer.model.Website;
import marustuff.textometer.repository.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WebsiteService {
    @NonNull
    @Autowired
    private final WebsiteRepository repository;

    private static final String listWebsitesReturn="list";
    private static final String listWebsitesRedirectReturn="redirect:/website/list";
    private static final String listWebsitesModelAttributeName= "currentWebsites";
    private static final String addWebsiteReturn="add";
    private static final String addWebsiteModelAttributeName="website";
    private static final String saveWebsiteReturn="redirect:/website/list";
    private static final String empty="";

    public String serveListWebsites(Model model) {
        model.addAttribute(listWebsitesModelAttributeName,repository.findAll());
        return listWebsitesReturn;
    }

    public String serveAddWebsite(Model model){
        Website website = new Website();
        model.addAttribute(addWebsiteModelAttributeName, website);
        return addWebsiteReturn;
    }

    public String serveSaveWebsite(Website website) throws ResponseStatusException {
        try {
            checkIfEmpty(website);
            repository.save(website);
            return saveWebsiteReturn;
        } catch (NullPointerException e) {
            return listWebsitesRedirectReturn;
        }

    }


    public void checkIfEmpty(Website website) throws NullPointerException{
        if (!(website != null && !website.getAddress().equals(null) && !website.getAddress().equals(empty))) {
            throw new NullPointerException("Website object is empty or address value empty");
        }
    }

    public void removeWebsite(Website website){
        System.out.println("removeWebsite :" + website);
        repository.deleteById(website.getId());
    }
}
