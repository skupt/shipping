package rozaryonov.shipping.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Role;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long>{

}
