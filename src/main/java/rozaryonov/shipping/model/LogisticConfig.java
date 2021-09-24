package rozaryonov.shipping.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class LogisticConfig {
	@Id
	private long id;
	private String name;
}
