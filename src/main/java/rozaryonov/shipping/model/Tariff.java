package rozaryonov.shipping.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;
@Data
@Entity
public class Tariff {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Timestamp creationTimestamp;
	@ManyToOne
	@JoinColumn(name = "logistic_config_id", foreignKey = @ForeignKey(name = "fk_tariff_logistic_config1"))
	private LogisticConfig logisticConfig;
	private int truckVelocity;
	private double density;
	private double paperwork;
	private int targetedReceipt;
	private int targetedDelivery;
	private double shippingRate;
	private double insuranceWorth;
	private double insuranceRate;
	

}
