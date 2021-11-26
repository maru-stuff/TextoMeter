package marustuff.textometer.repository;

import marustuff.textometer.model.Metering;
import org.springframework.data.repository.CrudRepository;
// również adnotacja @Repository
public interface MeteringRepository extends CrudRepository<Metering,String> {
}
