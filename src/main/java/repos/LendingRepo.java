package repos;

import models.Lending;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LendingRepo extends CrudRepository<Lending, Integer> {

}
