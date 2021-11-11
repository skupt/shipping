package rozaryonov.shipping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GuestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("admin")
    public void enterCabinetShouldReturnManagerCabinetPage() throws Exception {
        this.mockMvc.perform(get("/authorized_zone_redirection"))
                .andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/cabinet"));
    }

    @Test
    @WithUserDetails("user")
    public void enterCabinetShouldReturnAuthUserCabinetPage() throws Exception {
        this.mockMvc.perform(get("/authorized_zone_redirection"))
                .andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth_user/cabinet"));
    }



    @Test
    public void shouldReturnIndexPage() throws Exception {
        this.mockMvc.perform(get("/").header("Accept-Language", "en_US")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("MDS: Modern Delivery Service")));
    }

    @Test
    public void shouldReturnTariffPage() throws Exception {
        String expected = "\t<tr >\n" +
                "\t\t<td>2021-01-30 03:20:37.0</td>\n" +
                "\t\t<td>1</td>\n" +
                "\t\t<td>50</td>\n" +
                "\t\t<td>0.5</td>\n" +
                "\t\t<td>40.0</td>\n" +
                "\t\t<td>50</td>\n" +
                "\t\t<td>50</td>\n" +
                "\t\t<td>0.3</td>\n" +
                "\t\t<td>50.0</td>\n" +
                "\t\t<td>0.02</td>\n" +
                "\t</tr>\n" +
                "\n" +
                "\t<tr >\n" +
                "\t\t<td>2021-02-20 03:20:37.0</td>\n" +
                "\t\t<td>1</td>\n" +
                "\t\t<td>50</td>\n" +
                "\t\t<td>0.5</td>\n" +
                "\t\t<td>45.0</td>\n" +
                "\t\t<td>90</td>\n" +
                "\t\t<td>66</td>\n" +
                "\t\t<td>0.4</td>\n" +
                "\t\t<td>50.0</td>\n" +
                "\t\t<td>0.02</td>\n" +
                "\t</tr>\n" +
                "\n" +
                "\t<tr >\n" +
                "\t\t<td>2021-03-30 04:20:37.0</td>\n" +
                "\t\t<td>1</td>\n" +
                "\t\t<td>50</td>\n" +
                "\t\t<td>0.5</td>\n" +
                "\t\t<td>45.0</td>\n" +
                "\t\t<td>90</td>\n" +
                "\t\t<td>66</td>\n" +
                "\t\t<td>0.5</td>\n" +
                "\t\t<td>50.0</td>\n" +
                "\t\t<td>0.02</td>\n" +
                "\t</tr>\n" +
                "\n" +
                "\t<tr >\n" +
                "\t\t<td>2021-04-30 04:20:37.0</td>\n" +
                "\t\t<td>1</td>\n" +
                "\t\t<td>50</td>\n" +
                "\t\t<td>0.5</td>\n" +
                "\t\t<td>45.0</td>\n" +
                "\t\t<td>90</td>\n" +
                "\t\t<td>66</td>\n" +
                "\t\t<td>0.6</td>\n" +
                "\t\t<td>50.0</td>\n" +
                "\t\t<td>0.02</td>\n" +
                "\t</tr>\n" +
                "\n" +
                "\t<tr >\n" +
                "\t\t<td>2021-05-30 04:20:37.0</td>\n" +
                "\t\t<td>1</td>\n" +
                "\t\t<td>55</td>\n" +
                "\t\t<td>0.5</td>\n" +
                "\t\t<td>45.0</td>\n" +
                "\t\t<td>80</td>\n" +
                "\t\t<td>66</td>\n" +
                "\t\t<td>0.7</td>\n" +
                "\t\t<td>50.0</td>\n" +
                "\t\t<td>0.02</td>\n" +
                "\t</tr>\n";
        this.mockMvc.perform(get("/tariffs").header("Accept-Language", "en_US")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expected)));
    }

    @Test
    public void shouldReturnCalculationStartPage() throws Exception {
        this.mockMvc.perform(get("/shippings/calculation_start_form").header("Accept-Language", "en_US")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("calculation_start_form")));
    }

    @Test
    public void shouldReturnCalculationResultPage() throws Exception {
        this.mockMvc.perform(get("/shippings/calculation_result_form")
                        .header("Accept-Language", "en_US")
                        .param("departure", "5")
                        .param("arrival", "7")
                        .param("length", "20")
                        .param("width", "20")
                        .param("height", "20")
                        .param("weight", "20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("calculation_result_form")));
    }
}