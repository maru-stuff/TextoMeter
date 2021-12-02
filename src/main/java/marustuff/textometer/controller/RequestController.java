package marustuff.textometer.controller;

import lombok.RequiredArgsConstructor;
import marustuff.textometer.model.Metering;
import marustuff.textometer.model.MeteringComparison;
import marustuff.textometer.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

    @Autowired
    private final RequestService requestService;

    @PostMapping("/post")
    public String submitWord(@ModelAttribute Metering metering) {
        return requestService.submitMeteringView(metering);
    }

    @PostMapping("/postvs")
    public String submitComparison(@ModelAttribute MeteringComparison vs) {
        return requestService.submitComparisonView(vs);
    }

    @GetMapping("/debug/metering")
    public String TestMetering(Model model) {
        return requestService.debugMetering(model);
    }

    @GetMapping("/get")
    public String serveMetering(@RequestParam("word") String word, Model model) {
        return requestService.getMeteringView(word, model);
    }

    @GetMapping("/getvs")
    public String serveVsMetering(@RequestParam("word") String word, @RequestParam("word2") String word2, Model model) {
        return requestService.getComparisonMeteringView(word, word2, model);

    }
}
