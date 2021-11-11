package rozaryonov.shipping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import rozaryonov.shipping.model.SettlementType;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.repository.SettlementsRepository;
import rozaryonov.shipping.repository.SettlementsTypeRepository;

@SpringBootTest
class SettlementsRepositoryTest {
	@Autowired
	private SettlementsTypeRepository settlementsTypeRepository;
	@Autowired
	private SettlementsRepository settlementRepository;
	
	@Test
	void testFindBySettlementsType() {
		SettlementType st = settlementsTypeRepository.findById(1L).orElse(null);
		List<Settlements> ls = settlementRepository.findBySettlementType(st);
		StringBuilder sb = new StringBuilder();
		for (Settlements s : ls) {
			sb
			
			.append(s.getId())
			.append(" ").append(s.getPerson().getLogin())
			.append(" ").append(s.getSettlementType().getName())
			.append(" ").append(s.getAmount().toString())
			.append("\n");
		}
		String expected = "1 vasya payment 20000.00\n" +
				"2 petya payment 20000.00\n" +
				"3 user payment 5000.00\n" +
				"4 6543 payment 6000.00\n" +
				"5 vasya payment 2500.00\n" +
				"6 petya payment 3400.00\n" +
				"7 user payment 1300.00\n";
		
		
		assertEquals(expected, sb.toString());
	}

	@Test
	void calcPersonBalanceTest() {
		BigDecimal b1 = settlementRepository.calcPersonBalance(1L);
		assertEquals("22500.00", b1.toString());

		BigDecimal b2 = settlementRepository.calcPersonBalance(2L);
		assertEquals("23249.80", b2.toString());

		
	}
}
