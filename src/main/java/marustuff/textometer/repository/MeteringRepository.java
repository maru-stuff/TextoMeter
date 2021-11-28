package marustuff.textometer.repository;

import marustuff.textometer.model.Metering;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeteringRepository extends CrudRepository<Metering, String> {
}
