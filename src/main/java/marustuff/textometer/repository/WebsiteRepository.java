package marustuff.textometer.repository;

import marustuff.textometer.model.Website;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//warto by tu zastosować adnotację repository
@Repository
public interface WebsiteRepository extends CrudRepository<Website,Long> {
}
