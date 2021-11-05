package rozaryonov.shipping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.model.ShippingStatus;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.repository.ShippingStatusRepository;

@SpringBootTest
class ShippingRepositoryTest {
	@Autowired
	private ShippingStatusRepository shippingStatusRepository;
	@Autowired
	private ShippingRepository shippingRepository;
	
	
	@Test
	void testFindByShippingStatus() {
		ShippingStatus justCreated = shippingStatusRepository.findById(AppConst.SHIPPING_STATUS_JUST_CREATED).orElse(null);
		Page<Shipping> ps = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(justCreated, PageRequest.of(0, 2));

		int pageTotal = ps.getTotalPages();
		boolean hasNext = ps.hasNext();

		List<Shipping> ls0 = ps.getContent();
		StringBuilder sb = new StringBuilder();
		for (Shipping s : ls0) {
			sb.append(s.getId()).append("; ").append(s.getFare()).append(s.getShippingStatus().getId()).append("\n");
		}
		
		String exp1 = 	"9; 424.801\n" +
				"10; 1078.701\n";
		assertEquals(exp1, sb.toString());
		
		assertEquals("3", "" + pageTotal);
		
		assertTrue(hasNext);
		
	}

	@Test
	void testFindByShippingStatus2ndPage() {
		ShippingStatus justCreated = new ShippingStatus();
		justCreated.setId(AppConst.SHIPPING_STATUS_JUST_CREATED);
		
		Page<Shipping> ps = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(justCreated, PageRequest.of(0, 2));

		int curNumber = ps.getNumber();
		boolean hasNext = ps.hasNext();

		Pageable pageable = null;
		if (ps.hasNext()) pageable = ps.nextPageable();
		
		ps=shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(justCreated, pageable);

		List<Shipping> ls1 = ps.getContent();
		StringBuilder sb = new StringBuilder();
		for (Shipping s : ls1) {
			sb.append(s.getId()).append("; ").append(s.getFare()).append(s.getShippingStatus().getId()).append("\n");
		}
		
		curNumber = ps.getNumber();
		hasNext = ps.hasNext();
		
		String exp1 = 	"11; 2131.201\n" +
				"12; 3796.801\n";
		assertEquals(exp1, sb.toString());
		
		assertEquals("1", "" + curNumber);
		
		assertTrue(hasNext);
		
	}

}
