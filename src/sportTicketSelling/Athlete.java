package sportTicketSelling;

import javafx.beans.property.SimpleStringProperty;


public class Athlete {
	private final SimpleStringProperty aid;
	private final SimpleStringProperty aName;
	private final SimpleStringProperty aCountry;
	private final SimpleStringProperty aSport;
	private final SimpleStringProperty aAge;
	
	public Athlete(String aid, String aName, String aCountry, String aSport, String aAge) {
		this.aid = new SimpleStringProperty(aid);
		this.aName = new SimpleStringProperty(aName);
		this.aCountry = new SimpleStringProperty(aCountry);
		this.aSport = new SimpleStringProperty(aSport);
		this.aAge = new SimpleStringProperty(aAge);
	}
	
	
	public String getId() {
		return aid.get();
	}
	
	public void setId(String st) {
		aid.set(st);
	}
	
	public String getName() {
		return aName.get();
	}
	
	public void setName(String st) {
		aName.set(st);
	}
	
	public String getCountry() {
		return aCountry.get();
	}
	
	public void setCountry(String st) {
		aCountry.set(st);
	}
	
	public String getSport() {
		return aSport.get();
	}
	
	public void setSport(String st) {
		aSport.set(st);
	}
	
	public String getAge() {
		return aAge.get();
	}
	
	public void setAge(String st) {
		aAge.set(st);
	}

}
