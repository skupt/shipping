package rozaryonov.shipping.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.LogisticNetElement;

@Repository
public interface LogisticNetElementRepository extends CrudRepository<LogisticNetElement, Long>,
        CustomLogisticNetElementRepository {

}
