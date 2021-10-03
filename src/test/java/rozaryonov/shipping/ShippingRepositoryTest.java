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
		ShippingStatus justCreated = shippingStatusRepository.findById(1L).orElse(null);
		Page<Shipping> ps = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(justCreated, PageRequest.of(0, 2));

		int zeroNumber = ps.getNumber();
		int pageTotal = ps.getTotalPages();
		boolean hasNext = ps.hasNext();

		List<Shipping> ls0 = ps.getContent();
		StringBuilder sb = new StringBuilder();
		for (Shipping s : ls0) {
			sb.append(s.getId()).append("; ").append(s.getFare()).append(s.getShippingStatus().getId()).append("\n");
		}
		
		String exp1 = 	"8; 1200.961\n" + 
						"1; 1345.961\n";
		assertEquals(exp1, sb.toString());
		
		assertEquals("2", "" + pageTotal);
		
		assertTrue(hasNext);
		
	}

	@Test
	void testFindByShippingStatus2ndPage() {
//		ShippingStatus justCreated = shippingStatusRepository.findById(1L).orElse(null);
		ShippingStatus justCreated = new ShippingStatus();
		justCreated.setId(1L);
		
		Page<Shipping> ps = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(justCreated, PageRequest.of(0, 2));

		int curNumber = ps.getNumber();
		int pageTotal = ps.getTotalPages();
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
		
		String exp1 = 	"3; 150.201\n" + 
						"7; 1100.961\n";
		assertEquals(exp1, sb.toString());
		
		assertEquals("1", "" + curNumber);
		
		assertFalse(hasNext);
		
	}

}
