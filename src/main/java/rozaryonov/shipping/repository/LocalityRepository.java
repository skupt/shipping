package rozaryonov.shipping.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.Locality;

@Repository
public interface LocalityRepository extends CrudRepository<Locality, Long>{

}
