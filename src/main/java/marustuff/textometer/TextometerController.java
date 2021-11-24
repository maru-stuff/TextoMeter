package marustuff.textometer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String TestMetering(){
        Metering metering = new Metering();
        repository.findAll().forEach(System.out::println);

        return "test";
    }

    @GetMapping("/{word}")
    public String ServeMetering(@PathVariable String word) {
        Metering metering = new Metering();
        if(repository.existsById(word)){
            metering = repository.findById(word).get();

            System.out.println("inside Serve Metering");
            System.out.println("Word: "+metering.getWord()+" Score: " +metering.getScore());
            return "test";
        }
        else{

            return "redirect:/poll/"+word;
        }

    }


}
