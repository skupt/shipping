package rozaryonov.shipping.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;

@Data
@Entity
public class InvoiceShipping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "invoice_id", foreignKey = @ForeignKey (name = "fk_invoice_shippings_invoice1"))
	private Invoice invoice;
	@ManyToOne
	@JoinColumn(name = "shipping_id", foreignKey  = @ForeignKey (name = "fk_invoice_shippings_shipping1"))
	private Shipping shipping;
	private BigDecimal sum;

}
