package rozaryonov.shipping.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity

public class Locality {
	@Id
	private long id;
	private String name;
}
