package marustuff.textometer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/request")
@RequiredArgsConstructor
public class TextometerController {
    @NonNull
    @Autowired
    private final MeteringRepository repository;
    //@GetMapping("/{word}")
    @GetMapping("/test")
    //public String ServeMetering(@PathVariable String word){
    public String TestMetering(Model model){
        //Metering metering = new Metering();
        model.addAttribute("currentWords",repository.findAll());
        return "test";
    }

    @GetMapping("/{word}")
    public String ServeMetering(@PathVariable String word, Model model) {
        Metering metering = new Metering();
        if(repository.existsById(word)){
            //metering = repository.findById(word).get();
            model.addAttribute("currentWord",metering = repository.findById(word).get());

            return "request";
        }
        else{

            return "redirect:/poll/"+word;
        }

    }

    @GetMapping("/vs/{word}/{word2}")
    public String ServeVsMetering(@PathVariable("word") String word, @PathVariable("word2") String word2, Model model){
        if(repository.existsById(word)&&repository.existsById(word2)){
            return "test";
        }
         return "redirect:/poll/"+word+"/"+word2;
    }


}
