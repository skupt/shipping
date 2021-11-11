package rozaryonov.shipping;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Role;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.repository.TariffRepository;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.service.GuestService;

import javax.servlet.http.HttpSession;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GuestServiceSimpleUnitTest {

    @Test
    public void enterCabinetShouldReturnManagerCabinetPage() {
        HttpSession session = Mockito.mock(HttpSession.class);
        Model model = Mockito.mock(Model.class);
        Principal principal = Mockito.mock(Principal.class);
        PersonRepository personRepository = Mockito.mock(PersonRepository.class);
        Person person = new Person(); person.setRole(Role.ROLE_MANAGER);
        Mockito.when(personRepository.findByLogin("admin")).thenReturn(java.util.Optional.of(person));
        GuestService guestService = new GuestService(null, null, personRepository);

        when(principal.getName()).thenReturn("admin");
        String actualPage = guestService.enterCabinet(model, principal, session);
        assertEquals("redirect:/manager/cabinet", actualPage);
    }

    @Test
    public void tariffShouldSet3NewAttrsWhenNOSuchAttrsInSession() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpSession mockSession = new MockHttpSession();
        TariffRepository tariffRepository = Mockito.mock(TariffRepository.class);
        Mockito.when(tariffRepository.findFilterSort(null, null, null, null)).thenReturn(new ArrayList<Tariff>());
        PageableFactory pageableFactory = Mockito.mock(PageableFactory.class);
        Page<Tariff, TariffRepository> pageTariffArchive = Mockito.mock(Page.class);
        Mockito.when(pageTariffArchive.nextPage()).thenReturn(new ArrayList<Tariff>());
        Mockito.when(pageableFactory.getPageableForTariffArchive(6,null,null)).thenReturn(pageTariffArchive);
        GuestService guestService = new GuestService(pageableFactory, null,null);

        guestService.tariffs(mockRequest, mockSession);

        assertEquals(ArrayList.class, ((ArrayList) mockSession.getAttribute("tariffArchiveList")).getClass());

    }

}
