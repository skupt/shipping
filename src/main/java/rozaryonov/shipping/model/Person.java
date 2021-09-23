package rozaryonov.shipping.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Person {
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue
	private long id;
	private String login;
	private String password;
	private String email;
	private String name;
	private String surname;
	private String patronomic;
	private String title;
	@ManyToOne
	@JoinColumn(name = "role_id",
			foreignKey = @ForeignKey(name = "fk_person_role1"))
	private Role role;
	
}
