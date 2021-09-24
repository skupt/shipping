package rozaryonov.shipping.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "properties")
public class Property {
	@Id
	String id;
	String value;
}
