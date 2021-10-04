package rozaryonov.shipping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import rozaryonov.shipping.repository.page.DayReportRepo;
import rozaryonov.shipping.repository.page.DirectionReportRepo;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.repository.reportable.DayReport;
import rozaryonov.shipping.repository.reportable.DirectionReport;

@SpringBootTest
class DirectionReportRepoTest {
	@Autowired
	private PageableFactory pageableFactory;
	
	
	@Test
	void testNextPage() {
		Page<DirectionReport, DirectionReportRepo> page = pageableFactory.getPageableForManagerDirectionReport(100);
		List<DirectionReport> lr = page.nextPage();
		
		StringBuilder sb = new StringBuilder();
		for (DirectionReport r : lr) {
			sb.append(r.toString()).append("\n");
		}
		
		String expected = "Луцк -> Запорожье	400.0\n" + 
				"Львов -> Житомир	4993.84\n" + 
				"Львов -> Кропивницкий	462.0\n" + 
				"Ровно -> Днепр	150.2\n" + 
				"Ужгород -> Черкассы	378.0\n";
		
		assertEquals(expected, sb.toString());
		
	}

	@Test
	void testOther() {
		Page<DayReport, DayReportRepo> page = pageableFactory.getPageableForManagerDayReport(100);
		page.nextPage();
		int curPage = page.getCurPageNum();
		int pageTotal = page.getTotalPages();
		
		assertEquals(1, curPage);
		assertEquals(1, pageTotal);
		
		
	}
	
	
	


}
