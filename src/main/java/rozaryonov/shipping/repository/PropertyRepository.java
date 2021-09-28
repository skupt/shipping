package rozaryonov.shipping.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.Property;

@Repository
public interface PropertyRepository extends CrudRepository<Property, String>{

}
