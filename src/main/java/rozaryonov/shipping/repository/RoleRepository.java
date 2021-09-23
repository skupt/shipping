package rozaryonov.shipping.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{

}
