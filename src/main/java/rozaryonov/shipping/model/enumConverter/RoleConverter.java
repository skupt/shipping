package rozaryonov.shipping.model.enumConverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import rozaryonov.shipping.model.Role;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

	@Override
	public String convertToDatabaseColumn(Role role) {
		if (role == null) return null;
		return role.toString();
	}

	@Override
	public Role convertToEntityAttribute(String id) {
		if (id == null) return null;
		
		return Role.valueOf(id);
	}

}
