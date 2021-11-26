package marustuff.textometer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import marustuff.textometer.model.Website;
import marustuff.textometer.repository.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class InitializerRunner implements CommandLineRunner {
//po co ten non null?
    @NonNull
    @Autowired
    private final WebsiteRepository repository;

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Website(1L,"https://hackaday.com/blog/"));
        repository.save(new Website(2L,"https://news.ycombinator.com/"));
        repository.save(new Website(3L,"https://www.vice.com/en"));
    }
}
