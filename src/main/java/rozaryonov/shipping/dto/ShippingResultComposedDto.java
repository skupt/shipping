package rozaryonov.shipping.dto;

import lombok.Data;
@Data
public class ShippingResultComposedDto {
    private ShippingResultPriceAttributesDto shippingResultPriceAttributesDto;
    private ShippingResultFormParametersDto shippingResultFormParametersDto;

    public ShippingResultComposedDto(ShippingResultFormParametersDto shippingResultFormParametersDto,
                                     ShippingResultPriceAttributesDto shippingResultPriceAttributesDto) {
        this.shippingResultFormParametersDto = shippingResultFormParametersDto;
        this.shippingResultPriceAttributesDto = shippingResultPriceAttributesDto;
    }
}
