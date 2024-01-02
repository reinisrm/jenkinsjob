package repos;

import models.Lending;
import org.springframework.data.repository.CrudRepository;

public interface LendingRepo extends CrudRepository<Lending, Integer> {

}
