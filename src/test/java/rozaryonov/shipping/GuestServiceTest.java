package rozaryonov.shipping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;
import rozaryonov.shipping.service.GuestService;

import javax.servlet.http.HttpSession;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GuestServiceTest {
    @Autowired
    GuestService guestService;

    @MockBean
    Model model;
    @MockBean
    Principal principal;
    @MockBean HttpSession session;


    @Test
    public void enterCabinetShouldReturnUserCabinetPage() {
        //Model model, Principal principal, HttpSession session
        when(principal.getName()).thenReturn("user");
        String actualPage = guestService.enterCabinet(model, principal, session);
        assertEquals("redirect:/auth_user/cabinet", actualPage);
    }
}
