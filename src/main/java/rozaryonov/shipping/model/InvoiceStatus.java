package rozaryonov.shipping.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class InvoiceStatus {
	@Id
	private long id;
	private String name;
}
