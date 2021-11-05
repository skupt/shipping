package rozaryonov.shipping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rozaryonov.shipping.controller.AuthUserController;
import rozaryonov.shipping.controller.GuestController;
import rozaryonov.shipping.controller.ManagerController;
import rozaryonov.shipping.service.AuthUserService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WebAppTest {

    @Autowired
    private AuthUserController authUserController;
    @Autowired
    private GuestController guestController;
    @Autowired
    private ManagerController managerController;
    @Autowired
    private AuthUserService authUserService;

    @Test
    public void contextLoads() {
        assertNotNull(authUserController);
        assertNotNull(guestController);
        assertNotNull(managerController);
        assertNotNull(authUserService);
    }

}
