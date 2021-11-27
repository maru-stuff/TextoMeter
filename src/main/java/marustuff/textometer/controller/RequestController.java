package marustuff.textometer.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import marustuff.textometer.model.Metering;
import marustuff.textometer.repository.MeteringRepository;
import marustuff.textometer.model.Vs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {
    @NonNull
    @Autowired
    private final MeteringRepository repository;
    public final String submitWordReturn="redirect:/request/get/";
    public final String submitVSReturn="redirect:/request/getvs/";
    public final String testMeteringReturn="debugMeteringRepository";
    public final String serveMeteringModelAttributeName="currentWord";
    public final String serveMeteringReturn="request";
    public final String serveMeteringReturnIfPolling="redirect:/poll/";
    public final String serveVsMeteringModelAttributeName="currentVs";
    public final String serveVsMeteringReturnIfFound="vs";
    public final String serveVsMeteringReturnIfNotFound="redirect:/poll/";

    @PostMapping("/post")
    public String submitWord(@ModelAttribute Metering metering){
        return submitWordReturn+metering.getWord();
    }

    @PostMapping("/postvs")
    public String submitVS(@ModelAttribute Vs vs){
        return submitVSReturn+vs.getWord1()+"/"+vs.getWord2();
    }
    @GetMapping("/debug/metering")
    public String TestMetering(Model model){
        model.addAttribute("currentWords",repository.findAll());
        return testMeteringReturn;
    }

    @GetMapping("get/{word}")
    public String serveMetering(@PathVariable String word, Model model) {
        //findById zwraca Optional, czyli wartość tutaj może nie zostać zwrócona. Należy najpierw sprawdzić jej obecność poprzez isPresent() a dopiero potem ewentualnie ją pobierać get()
        if(repository.existsById(word)&&isItFresh(repository.findById(word).get())){
            //findById zwraca Optional, czyli wartość tutaj może nie zostać zwrócona. Należy najpierw sprawdzić jej obecność poprzez isPresent() a dopiero potem ewentualnie ją pobierać get()
            model.addAttribute(serveMeteringModelAttributeName,repository.findById(word).get());

            return serveMeteringReturn;
        }
        else{

            return serveMeteringReturnIfPolling+word;
        }

    }

    @GetMapping("getvs/{word}/{word2}")
    public String serveVsMetering(@PathVariable("word") String word, @PathVariable("word2") String word2, Model model){
        //findById zwraca Optional, czyli wartość tutaj może nie zostać zwrócona. Należy najpierw sprawdzić jej obecność poprzez isPresent() a dopiero potem ewentualnie ją pobierać get()
        if((repository.existsById(word)&&isItFresh(repository.findById(word).get()))&&(repository.existsById(word2)&&isItFresh(repository.findById(word2).get()))){
            //findById zwraca Optional, czyli wartość tutaj może nie zostać zwrócona. Należy najpierw sprawdzić jej obecność poprzez isPresent() a dopiero potem ewentualnie ją pobierać get()
            Vs vs = new Vs(repository.findById(word).get(),repository.findById(word2).get());
            model.addAttribute(serveVsMeteringModelAttributeName,vs);
            return serveVsMeteringReturnIfFound;
        }
         return serveVsMeteringReturnIfNotFound+word+"/"+word2;
    }

    private boolean isItFresh(Metering metering) {
        Instant fresh = Instant.now().minus(1, ChronoUnit.MINUTES);
        if (metering.getCreatedAt().toEpochMilli() > fresh.toEpochMilli()) {
            return true;
        } else {
            return false;
        }
    }
}
