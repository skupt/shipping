package rozaryonov.shipping.repository.page;

public abstract class ReportRow {
	String index;
	double value;
	
	public ReportRow() {}
	
	public ReportRow(String index, double value) {
		super();
		this.index = index;
		this.value = value;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return index + "\t" + value;
	} 
	
}
