package rozaryonov.shipping.repository.page;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;


/**
 * 
 * @author Vitaly Rozaryonv
 * 
 * Example of use:
 * 
 * Connection cn = ConnectionPool.getConnection();
 * EntityRepo = new EntityRepo(cn);
 * Page<Entity, EntityRepo> page = new Page(repo, comparator)
 * // OR
 * Page<Entity, EntityRepo> page = new Page(repo, after, before, rowsOnPage, predicate, comparator)
 * page.init();
 * cn.close();
 * // use on jsp
 * List<Entity> entityList = page.nextPage();
 * entityList = page.prevPage();
 * 
 * @param <T> class of entities
 * @param <D> class of Dao
 */

public class Page <T, R extends Pageable<T>>{
	//state for constructor
	private Timestamp after = Timestamp.valueOf(LocalDateTime.of(1970, 1, 1, 0, 0));
	private Timestamp before = Timestamp.valueOf(LocalDateTime.of(3000, 1, 1, 0, 0));
	private int rowsOnPage = 5;
	private Pageable<T> repo;
	private Comparator<T> comparator;
	private Predicate<T> predicat = (e)-> true;
	//state for init()
	private int curPageNum = 0;
	private List<T> daoList = null;
	private int totalRows = 1;
	
	@SuppressWarnings("unused")
	private Page() {}
	
	public Page(Pageable<T> repo, Comparator<T> c) {
		this.repo=repo;
		this.comparator=c;
	}
	
	
	public Page(Pageable<T> repo, LocalDateTime after, LocalDateTime before, int rowsOnPage, Predicate p, Comparator c) {
		this.repo=repo;
		this.after = Timestamp.valueOf(after);
		this.before = Timestamp.valueOf(before);
		this.rowsOnPage = rowsOnPage;
		this.comparator=c;
		this.predicat=p;
		
	}
	
	public void init() {
		daoList = repo.findFilterSort(after, before, predicat, comparator);
		totalRows = daoList.size();
		
	}
	public List<T> nextPage() {
		if (daoList==null) return new ArrayList<T>();
		if (daoList.size()==0) return daoList;
		if (curPageNum*rowsOnPage < totalRows) curPageNum += 1; 
		int begin = (curPageNum-1)*rowsOnPage;
		int end = curPageNum*rowsOnPage;
		if (end > totalRows) end = totalRows;
		return daoList.subList(begin, end);
	}

	public List<T> prevPage() {
		if (daoList==null) return new ArrayList<T>();
		if (daoList.size()==0) return daoList;
		if (curPageNum>1) curPageNum -= 1; 
		int begin = (curPageNum-1)*rowsOnPage;
		int end = curPageNum*rowsOnPage;
		if (end > totalRows) end = totalRows;
		return daoList.subList(begin, end);
	}

	public Timestamp getAfter() {
		return after;
	}

	public void setAfter(Timestamp after) {
		this.after = after;
	}

	public Timestamp getBefore() {
		return before;
	}

	public void setBefore(Timestamp before) {
		this.before = before;
	}

	public int getRowsOnPage() {
		return rowsOnPage;
	}

	public void setRowsOnPage(int rowsOnPage) {
		this.rowsOnPage = rowsOnPage;
	}

	public Pageable<T> getDao() {
		return repo;
	}

	public void setDao(Pageable<T> dao) {
		this.repo = dao;
	}

	public Comparator<T> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	public Predicate<T> getPredicat() {
		return predicat;
	}

	public void setPredicat(Predicate<T> predicat) {
		this.predicat = predicat;
	}

	public int getCurPageNum() {
		return curPageNum;
	}

}
