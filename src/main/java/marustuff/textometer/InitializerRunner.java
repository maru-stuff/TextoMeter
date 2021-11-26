package marustuff.textometer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import marustuff.textometer.model.Website;
import marustuff.textometer.repository.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class InitializerRunner implements CommandLineRunner {
    @NonNull
    @Autowired
    private final WebsiteRepository repository;

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Website(1L,"https://wp.pl/"));
        repository.save(new Website(2L,"https://www.onet.pl/"));
        repository.save(new Website(3L,"https://www.o2.pl/"));
    }
}
