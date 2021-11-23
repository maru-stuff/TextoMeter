package marustuff.textometer;

import lombok.NonNull;
import org.springframework.stereotype.Controller;

@Controller
public class TextometerController {
    @NonNull
    private final MeteringRepository repository;
}
