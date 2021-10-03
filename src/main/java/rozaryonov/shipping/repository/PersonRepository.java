package rozaryonov.shipping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Role;
import rozaryonov.shipping.model.SettlementType;
import rozaryonov.shipping.model.Settlements;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
	List<Person> findByRole(Role role);
}
