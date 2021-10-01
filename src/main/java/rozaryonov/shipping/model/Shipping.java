package rozaryonov.shipping.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "shipping")
public class Shipping {
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Timestamp creationTimestamp;
	@ManyToOne
	@JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "fk_shipping_person1"))
	private Person person;
	private Timestamp downloadDatetime;
	@ManyToOne
	@JoinColumn(name = "load_locality_id", foreignKey = @ForeignKey(name = "fk_shipping_locality1"))
	private Locality loadLocality;
	private String shipper;
	private String downloadAddress;
	private String consignee;
	@ManyToOne
	@JoinColumn(name = "unload_locality_id", foreignKey = @ForeignKey(name = "fk_shipping_locality2"))
	private Locality unloadLocality;
	private String unloadAddress;
	private Timestamp unloadingDatetime;
	private double distance;
	private double weight;
	private double volume;
	private BigDecimal fare;
	@ManyToOne
	@JoinColumn(name = "shipping_status_id", foreignKey = @ForeignKey(name = "fk_shipping_shipping_status1"))
	private ShippingStatus shippingStatus;

}
