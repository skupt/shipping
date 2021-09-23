package rozaryonov.shipping.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.ForeignKey;

import lombok.Data;

@Data
@Entity
public class Settlements {
	@Id
	private long id;
	private LocalDateTime creationDatetime;
	@ManyToOne
	@JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "fk_settlements_person1"))
	private Person person;
	@ManyToOne
	@JoinColumn(name = "settlment_type_id",  foreignKey = @ForeignKey(name = "fk_settlements_settlment_type1"))
	private SettlementType settlementType;
	private BigDecimal amount;
	
}
