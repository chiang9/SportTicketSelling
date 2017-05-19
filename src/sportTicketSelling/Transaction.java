package sportTicketSelling;

import javafx.beans.property.SimpleStringProperty;

public class Transaction {
	private final SimpleStringProperty sTitle;
	private final SimpleStringProperty sPrice;
	private final SimpleStringProperty sComfID;
	private final SimpleStringProperty mid;
	private final SimpleStringProperty sCount;
	//private final SimpleStringProperty count;

	public Transaction(String sTitle, String sPrice, String sComfID, String mid, String sCount) {
		this.sTitle = new SimpleStringProperty(sTitle);
		this.sPrice = new SimpleStringProperty(sPrice);
		this.sComfID = new SimpleStringProperty(sComfID);
		this.mid = new SimpleStringProperty(mid);
		this.sCount = new SimpleStringProperty(sCount);
		//this.count = new SimpleStringProperty(count);
	}

	public String getTitle() {
		return sTitle.get();
	}

	public void setTitle(String st) {
		sTitle.set(st);
	}

	public String getPrice() {
		return sPrice.get();
	}

	public void setPrice(String st) {
		sPrice.set(st);
	}

	public String getComf() {
		return sComfID.get();
	}

	public void setComf(String st) {
		sComfID.set(st);
	}


	public String getMatch() {
		return mid.get();
	}

	public void setMatch(String st) { mid.set(st);}

	public String getCount() {
		return sCount.get();
	}

	public void setCount(String st) {
		sCount.set(st);
	}

}
