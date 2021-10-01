package rozaryonov.shipping.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;

@Data
@Entity(name = "invoice")
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "person_id", foreignKey = @ForeignKey (name = "fk_invoice_person1"))
	private Person person;
	@Column(name = "creation_datetime")
	private Timestamp creationDateTime;
	private BigDecimal sum;
	@ManyToOne
	@JoinColumn(name = "invoice_status_id", foreignKey = @ForeignKey (name = "fk_invoice_invoice_status1"))
	private InvoiceStatus invoiceStatus;
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }) //mappedBy = "id", 
	@JoinTable(
            name = "invoice_shippings",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "shipping_id")
			)
	private List<Shipping> shippings;
	
	
}
