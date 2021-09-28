package rozaryonov.shipping.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
	@NotEmpty(message = "Login should not be empty")
	@Size(min = 3, max = 20, message = "Login should be between 3 and 20 characters")
	private String login;
	@Size(min = 3, max = 20, message = "Password should be between 3 and 20 characters")
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
