package rozaryonov.shipping.dto;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
	private long id;
	@NotEmpty(message = "Login should not be empty")
	@Size(min = 3, max = 20, message = "Login should be between 3 and 20 characters")
	private String login;
	@NotEmpty(message = "Login should not be empty")
	@Size(min = 3, max = 20, message = "Password should be between 3 and 20 characters")
	private String password;
	private String email;
	@NotEmpty(message = "Login should not be empty")
	@Size(min = 3, max = 20, message = "Name should be between 3 and 20 characters")
	private String name;
	private String surname;
	private String patronomic;
	private String title;
	private long roleId;
	
	public PersonDto(String login, String password, String name) {
		this.login = login;
		this.password = password;
		this.name = name;
	}
	
}
