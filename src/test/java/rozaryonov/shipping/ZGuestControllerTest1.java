package rozaryonov.shipping;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.BindingResult;
import rozaryonov.shipping.controller.GuestController;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.service.PersonService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ZGuestControllerTest1 {
    @Autowired
    private GuestController guestController;

    @MockBean
    private PersonService personService;

    @Test
    public void shouldReturnNewPersonFormWhenBindingResultHasErrorsInUsingPersonService() throws Exception {
        PersonDto personDto = new PersonDto();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        BindingResult bindingResultTrue = Mockito.mock(BindingResult.class);
        when(bindingResultTrue.hasErrors()).thenReturn(Boolean.TRUE);
        when(personService.checkUserCreationForm(personDto, bindingResult)).thenReturn(bindingResultTrue);

        String actual = guestController.createPerson(personDto, bindingResultTrue);
        assertEquals("/person/new_person_form", actual);
    }

}
