package rozaryonov.shipping.dto;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
@Data
public class ShippingResultFormParametersDto {
    private long departureId;
    private long arrivalId;
    private int length;
    private int width;
    private int height;
    private double weight;
    private String locStrStr;

    public ShippingResultFormParametersDto(HttpServletRequest request) {
        departureId = Long.parseLong(request.getParameter("departure"));
        arrivalId = Long.parseLong(request.getParameter("arrival"));
        length = Integer.parseInt(request.getParameter("length"));
        width = Integer.parseInt(request.getParameter("width"));
        height = Integer.parseInt(request.getParameter("height"));
        weight = Integer.parseInt(request.getParameter("weight"));
        locStrStr = request.getLocale().toString();
    }
}
