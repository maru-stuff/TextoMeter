package marustuff.textometer.repository;

import marustuff.textometer.model.Website;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteRepository extends CrudRepository<Website,Long> {
}
