package rozaryonov.shipping.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;

@Data
@Entity
public class UserDetails {
	//@OneToOne
	//@JoinColumn(name = "person_id") 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long person_id;
	private BigDecimal balance;
	
}
