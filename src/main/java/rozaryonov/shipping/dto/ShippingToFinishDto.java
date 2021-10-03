package rozaryonov.shipping.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ShippingToFinishDto {
	
	@NotNull
	private Long shippingId;
	@NotEmpty
	private String unloadDate;
}
