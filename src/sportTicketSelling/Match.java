package sportTicketSelling;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Match {
	private final SimpleStringProperty sSport;
	private final SimpleStringProperty sAthlete;
	private final SimpleStringProperty sLocation;
	private final SimpleStringProperty sDate;
	private final SimpleStringProperty sPrice;
	private final SimpleStringProperty matchId;
	private final SimpleIntegerProperty sTicket;



	public Match(String sSport, String sAthlete, String sLocation, String sDate, String sPrice, String matchId) {
		this.sSport = new SimpleStringProperty(sSport);
		this.sAthlete = new SimpleStringProperty(sAthlete);
		this.sLocation = new SimpleStringProperty(sLocation);
		this.sDate = new SimpleStringProperty(sDate);
		this.sPrice = new SimpleStringProperty(sPrice);
		this.matchId = new SimpleStringProperty(matchId);
		this.sTicket = new SimpleIntegerProperty(0);		// we don't use this in general user table

	}

	public Match(String sSport, String sAthlete, String sLocation, String sDate, String sPrice, String matchId, int sTicket) {
		this.sSport = new SimpleStringProperty(sSport);
		this.sAthlete = new SimpleStringProperty(sAthlete);
		this.sLocation = new SimpleStringProperty(sLocation);
		this.sDate = new SimpleStringProperty(sDate);
		this.sPrice = new SimpleStringProperty(sPrice);
		this.matchId = new SimpleStringProperty(matchId);
		this.sTicket = new SimpleIntegerProperty(sTicket);

	}

	public String getSport() {
		return sSport.get();
	}

	public void setSport(String st) {
		sSport.set(st);
	}

	public String getAthlete() {
		return sAthlete.get();
	}

	public void setAthlete(String st) {
		sAthlete.set(st);
	}

	public String getLocation() {
		return sLocation.get();
	}

	public void setLocation(String st) {
		sLocation.set(st);
	}

	public String getDate() {
		return sDate.get();
	}

	public void setDate(String st) {
		sDate.set(st);
	}

	public String getPrice() {
		return sPrice.get();
	}

	public void setPrice(String st) {
		sPrice.set(st);
	}

	public int getTicket() {
		return sTicket.get();
	}

	public void setTicket(int st) {
		sTicket.set(st);
	}

	public String getId() {
		return matchId.get();
	}

	public void setId(String st) {
		matchId.set(st);
	}


}
