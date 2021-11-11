package rozaryonov.shipping;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import rozaryonov.shipping.service.GuestService;

import javax.servlet.http.HttpSession;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GuestServiceTest {

    @Autowired
    GuestService guestService;

    @MockBean
    Model model;
    @MockBean
    Principal principal;


    @Test
    public void enterCabinetShouldReturnUserCabinetPage() {
        HttpSession session = Mockito.mock(HttpSession.class);
        when(principal.getName()).thenReturn("user");
        String actualPage = guestService.enterCabinet(model, principal, session);
        assertEquals("redirect:/auth_user/cabinet", actualPage);
    }
    @Test
    public void enterCabinetShouldReturnManagerCabinetPage() {
        HttpSession session = Mockito.mock(HttpSession.class);
        when(principal.getName()).thenReturn("admin");
        String actualPage = guestService.enterCabinet(model, principal, session);
        assertEquals("redirect:/manager/cabinet", actualPage);
    }

    @Test
    public void tariffShouldAdd3AttrsToSessionWhen () {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpSession mockSession = new MockHttpSession();

        guestService.tariffs(mockRequest, mockSession);
        assertNotNull(mockSession.getAttribute("pageTariffArchive"));
        assertNotNull(mockSession.getAttribute("tariffArchiveList"));
        assertEquals(1, mockSession.getAttribute("pageNum"));
    }



}
