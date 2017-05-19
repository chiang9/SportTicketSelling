package sportTicketSelling;

import javafx.beans.property.SimpleStringProperty;

public class Sport {
	private final SimpleStringProperty sid;
	private final SimpleStringProperty sTitle;
	
	public Sport(String sid, String sTitle) {
		this.sid = new SimpleStringProperty(sid);
		this.sTitle = new SimpleStringProperty(sTitle);
	}
	
	
	public String getId() {
		return sid.get();
	}
	
	public void setId(String st) {
		sid.set(st);
	}
	
	public String getTitle() {
		return sTitle.get();
	}
	
	public void setTitle(String st) {
		sTitle.set(st);
	}
	
	
	
	
}
