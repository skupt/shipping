package rozaryonov.shipping;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.service.PersonService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//todo why 403?
@SpringBootTest
@AutoConfigureMockMvc
public class ZGuestControllerTest2 {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        PersonDto personDto = new PersonDto();
        personDto.setLogin("login5");
        personDto.setPassword("login5");
        personDto.setName("login5");

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        BindingResult bindingResultTrue = Mockito.mock(BindingResult.class);
        when(bindingResultTrue.hasErrors()).thenReturn(Boolean.TRUE);
        when(personService.checkUserCreationForm(personDto, bindingResult)).thenReturn(bindingResultTrue);  //todo after calling it here will be NullPointerException later in row 67

        this.mockMvc.perform(post("/persons/")
                        .requestAttr("personDto", personDto)
                        .requestAttr("bindingResult", bindingResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<form id=\\new_person_form\\")));
    }

}
