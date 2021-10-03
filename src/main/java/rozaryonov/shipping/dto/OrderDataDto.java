package rozaryonov.shipping.dto;

import java.sql.Timestamp;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import rozaryonov.shipping.model.Locality;

@Data
@NoArgsConstructor
public class OrderDataDto {
	//@FutureOrPresent(message= "Date may be present or future")
	private String downloadDatetime;
	@NotEmpty(message="Field should not be empty")
	@Size (min=5, max=45, message="Field may contain from 5 to 45 characters")
	private String shipper;
	@NotEmpty(message="Field should not be empty")
	@Size (min=5, max=45, message="Field may contain from 5 to 45 characters")
	private String downloadAddress;
	@NotEmpty(message="Field should not be empty")
	@Size (min=5, max=45, message="Field may contain from 5 to 45 characters")
	private String consignee;
	@NotEmpty(message="Field should not be empty")
	@Size (min=5, max=45, message="Field may contain from 5 to 45 characters")
	private String unloadAddress;

}
