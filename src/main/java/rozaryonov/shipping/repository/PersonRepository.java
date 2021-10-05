package rozaryonov.shipping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Role;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
	List<Person> findByRole(Role role);
	Optional<Person> findByLogin(String login);
}
