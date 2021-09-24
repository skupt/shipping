package rozaryonov.shipping.model;

//import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
//import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.ForeignKey;

import lombok.Data;

@Data
@Entity
@Table(name = "logistic_net")
//@IdClass(LogisticNetPK.class)
public class LogisticNetElement {
	@Id
	private long id;
	@ManyToOne
	@JoinColumn (name = "logistic_config_id", foreignKey = @ForeignKey (name = "fk_logistic_net_logistic_config1"))
	private LogisticConfig logisticConfig;
	@ManyToOne
	@JoinColumn (name = "city_id", foreignKey = @ForeignKey (name = "fk_logistic_net_locality1"))
	private Locality city;
	@ManyToOne
	@JoinColumn (name = "neighbor_id", foreignKey = @ForeignKey (name = "fk_logistic_net_locality2"))
	private Locality neighbor;
	private double distance;

	/*
	public LogisticNetPK getId() {
		return new LogisticNetPK(
				logisticConfig,
				city,
				neighbor
		);
	}

	public void setId(LogisticNetPK id) {
		this.logisticConfig = id.getLogisticConfig();
		this.city = id.getCity();
		this.neighbor = id.getNeighbor();
	}
	*/
	
}
