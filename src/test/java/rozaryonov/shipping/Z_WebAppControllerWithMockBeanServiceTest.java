package rozaryonov.shipping;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import rozaryonov.shipping.controller.GuestController;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.repository.LocalityRepository;
import rozaryonov.shipping.service.GuestService;
import rozaryonov.shipping.service.PersonService;
import rozaryonov.shipping.service.ShippingService;
import rozaryonov.shipping.service.UserDetailsService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// ***************************
//APPLICATION FAILED TO START
//***************************

@WebMvcTest(GuestController.class)
public class Z_WebAppControllerWithMockBeanServiceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private GuestService guestService;
    @MockBean
    private PersonService personService;

    @Test
    public void shouldReturnNewPersonFormWhenBindingResultHasErrorsInUsingPersonService() throws Exception {
        PersonDto personDto = new PersonDto();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        BindingResult bindingResultTrue = Mockito.mock(BindingResult.class);
        when(bindingResultTrue.hasErrors()).thenReturn(Boolean.TRUE);
        when(personService.checkUserCreationForm(personDto, bindingResult)).thenReturn(bindingResultTrue);

        //todo how to pass personDto there? [createPerson(@ModelAttribute ("personDto") @Valid PersonDto personDto, BindingResult bindingResult)]
        this.mockMvc.perform(post("/persons/")
                        .requestAttr("personDto", personDto)
                        .requestAttr("bindingResult", bindingResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<form id=\\new_person_form\\")));
        //todo How to get authenticated if it is necessary?
        //todo how to perform post requests if csrf is not disabled?
    }
}
