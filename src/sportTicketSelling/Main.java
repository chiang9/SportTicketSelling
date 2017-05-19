package sportTicketSelling;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Calendar;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	private static Stage primaryStage;
	private static Stage secondaryStage;
	private static ObservableList<Match> checkoutItems = FXCollections.observableArrayList();
	private static ObservableList<Athlete> athlistData = FXCollections.observableArrayList();
	private static ObservableList<Transaction> transDatatemp = FXCollections.observableArrayList();
	private static ArrayList<Integer> purchaseNum = new ArrayList<Integer>();

	static Connection con;

	private static String userID;
	private static String status;
	private static String point;
	private static ArrayList<Match> matchtable = new ArrayList<Match>();

	public Main() {

	}

	public static void createAlertBox(String title, String message) {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(300);
		window.setHeight(200);

		Label label = new Label();
		label.setText(message);
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> window.close());
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Sport Ticket Selling");
		showLoginView();
	}

	public static void initTable(ObservableList<Transaction> transData, ObservableList<Match> matchData) {
		try {
			// init transaction history
			matchtable.clear();

			Statement st = con.createStatement();
			ResultSet trans = st
					.executeQuery("select p.comf, tp.price, count(tp.ticket_id), m.match_id, m.mdate, m.location "
							+ "from payment p, ticketprice tp, ticketconfirmation tc, match m " + "where us_id = '"
							+ userID
							+ "' and p.payment_id = tp.payment_id and tc.ticket_id = tp.ticket_id and m.match_id = tc.match_id "
							+ "group by p.comf, tp.price, m.match_id, m.mdate, m.location");

			while (trans.next()) {
				String title = trans.getString(5) + ", match_id: " + trans.getString(4).trim() + ", in "
						+ trans.getString(6);
				transData.add(new Transaction(title, trans.getString(2), trans.getString(1), trans.getString(4),
						trans.getString(3)));
			}
			transDatatemp = transData;

			// init matches
			Statement st3 = con.createStatement();
			ResultSet sportrs = st3
					.executeQuery("select s.title, m.location, m.mdate, tp.price, m.match_id, m.ticketCount"
							+ " from sport s, match m, ticketconfirmation tc, ticketprice tp where "
							+ "m.match_id = tc.match_id and tc.ticket_id = tp.ticket_id and m.sport_id = s.sport_id and m.ticketcount > 0 "
							+ "group by s.title, m.location, m.mdate, tp.price, m.match_id, m.ticketcount");

			while (sportrs.next()) {
				String sport = sportrs.getString(1);
				String location = sportrs.getString(2);
				String date = sportrs.getString(3);
				String price = sportrs.getString(4);
				String matchid = sportrs.getString(5);
				int ticketcout = sportrs.getInt(6);

				Statement st4 = con.createStatement();
				ResultSet rs4 = st4.executeQuery("select a.name from match m, athlete a, athmatch am "
						+ "where m.match_id = am.match_id and am.ath_id = a.ath_id and m.match_id = '" + matchid + "'");
				String ath_rs = "";
				boolean first = true;
				while (rs4.next()) {
					if (first) {
						ath_rs = rs4.getString(1);
						first = false;
					} else {
						ath_rs += ", " + rs4.getString(1);
					}
				}
				
				if (ath_rs.length() > 1) {
				Match m = new Match(sport, ath_rs, location, date, price, matchid, ticketcout);
				matchData.add(m);
				matchtable.add(m);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void initProfile(Text usernameText, Text statusText, Text pointText, Text creditText,
			Button upgradeButton) {
		usernameText.setText(userID);
		String creditCd = "N/A";
		try {
			// display the status/points/crdcard info
			statusText.setText(status);
			pointText.setText(point);
			Statement pt = con.createStatement();
			ResultSet rs2 = pt.executeQuery("select creditcard from member where us_id = '" + userID + "'");
			while (rs2.next()) {
				creditCd = rs2.getString(1).trim();
			}
			if (!creditCd.isEmpty()) {
				creditText.setText(creditCd);
			}
			if (status.equals("Member")) {
				upgradeButton.setVisible(false);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void showLoginView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/LoginView.fxml"));
		AnchorPane loginLayout = loader.load();
		Scene loginScene = new Scene(loginLayout);
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}

	public static void showRegisterView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/RegisterUserView.fxml"));
		AnchorPane registerLayout = loader.load();
		Scene layoutScene = new Scene(registerLayout);
		primaryStage.setScene(layoutScene);

	}

	public static void showForgotPassView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/ForgotPassView.fxml"));
		AnchorPane profileLayout = loader.load();

		secondaryStage = new Stage();
		secondaryStage.setTitle("Forgot Password");
		secondaryStage.initModality(Modality.WINDOW_MODAL);
		secondaryStage.initOwner(primaryStage);
		Scene scene = new Scene(profileLayout);
		secondaryStage.setScene(scene);
		secondaryStage.show();
		// secondaryStage.showAndWait();
	}

	public static void showMainView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/MainView.fxml"));
		AnchorPane mainLayout = loader.load();

		Scene layoutScene = new Scene(mainLayout);
		primaryStage.setScene(layoutScene);
	}

	public static void showAdminView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/AdminView.fxml"));
		AnchorPane mainLayout = loader.load();

		Scene layoutScene = new Scene(mainLayout);
		primaryStage.setScene(layoutScene);
	}

	public static void showProfileView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/ProfileView.fxml"));
		AnchorPane profileLayout = loader.load();

		secondaryStage = new Stage();
		secondaryStage.setTitle("Profile");
		secondaryStage.initModality(Modality.WINDOW_MODAL);
		secondaryStage.initOwner(primaryStage);
		Scene scene = new Scene(profileLayout);
		secondaryStage.setScene(scene);
		secondaryStage.showAndWait();
	}

	public static void showUpgradeView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/UpgradeView.fxml"));
		AnchorPane upgradeLayout = loader.load();

		Scene scene = new Scene(upgradeLayout);
		secondaryStage.setScene(scene);
	}

	public static void showCheckOutView(ObservableList<Match> selectItems) throws IOException {
		purchaseNum.clear();
		if (selectItems.size() == 0) {
			createAlertBox("Alert", "Please select items");
			return;
		}
		checkoutItems = selectItems;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/CheckOutView.fxml"));
		AnchorPane checkoutLayout = loader.load();

		secondaryStage = new Stage();
		secondaryStage.setTitle("CheckOut");
		secondaryStage.initModality(Modality.WINDOW_MODAL);
		secondaryStage.initOwner(primaryStage);
		Scene scene = new Scene(checkoutLayout);
		secondaryStage.setScene(scene);
		secondaryStage.showAndWait();
	}

	private static void showPurchaseView() throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/PurchaseView.fxml"));
		AnchorPane Layout = loader.load();

		//secondaryStage = new Stage();
		secondaryStage.setTitle("Purchase");
		//secondaryStage.initModality(Modality.WINDOW_MODAL);
		//secondaryStage.initOwner(primaryStage);
		Scene scene = new Scene(Layout);
		secondaryStage.setScene(scene);
		//secondaryStage.showAndWait();
	}

	// show usernamewarning view
	public static boolean showUsernameWarningView(String username) throws IOException {
		// if (not valid email address)
		ArrayList<String> usernameList = new ArrayList<String>();
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(username);
		if (!m.matches()) {

			createAlertBox("Warning!", "This is not a valid email address");
			return false;
		}
		// if(already exist)
		try {
			Statement st;
			st = con.createStatement();
			ResultSet rs = st.executeQuery("select us_id from generaluser");
			while (rs.next()) {
				usernameList.add(rs.getString(1));
			}
			if (usernameList.contains(username)) {
				createAlertBox("Warning!", "That username already exist ");
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	// show invalid password warning view
	public static boolean showPasswordWarningView(String password) throws IOException {
		if (password.length() == 0) {
			createAlertBox("Warning!", "Password Field is empty");
			return false;
		} else
			return true;
		// check if password left blank or in invalid pattern
	}

	// show invalid password warning view
	public static boolean showSequrityQuestionWarningView(String forgot) throws IOException {
		if (forgot.length() == 0) {
			createAlertBox("Warning!", "the sequirty answer Field is empty");
			return false;
		} else
			return true;
	}


	public static void handleRegister(String username, String password, String forgot) throws SQLException{
		// register user into account
		try {
			if (showUsernameWarningView(username) && showPasswordWarningView(password)
					&& showSequrityQuestionWarningView(forgot)) {
				Statement statement = con.createStatement();
				statement.executeUpdate("INSERT INTO generaluser " + "VALUES ('" + username + "', '" + password + "', '"
						+ forgot + "')");
				createAlertBox("Welcome!", "Register Successfully!");
				showLoginView();
			} else {
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

		// 0: if the username and password does not match with db (successful
		// login), and set userID
		// 1: for the general user/member
		// 2: for the administrator
		public static int handleLogin(String username, String password) {
			ArrayList<String> usernameList = new ArrayList<String>();
			ArrayList<String> memberList = new ArrayList<String>();
			try {
				Statement st;
				st = con.createStatement();
				ResultSet rs = st.executeQuery("select us_id from generaluser");
				while (rs.next()) {
					usernameList.add(rs.getString(1).trim());
				}
				rs.close();
				st.close();
				
				if (usernameList.contains(username)) {
					Statement passcheck = con.createStatement();
					ResultSet rs1 = passcheck
							.executeQuery("select us_id, password from generaluser where us_id = '" + username + "'");
					while (rs1.next()) {
						if (password.equals(rs1.getString(2))) {
							// login success, check if he is an administrator and
							// decide his status
							//update the available ticket
							Statement st1;
							st1=con.createStatement();
							ResultSet rss= st1.executeQuery("select mdate ,match_id from match");
							while(rss.next()){
								String matchdate = rss.getString(1);
								String[] matchdatepart = matchdate.split("-");
								if(!isExpired(matchdatepart)){
									// donothin
								}
								else{
									Statement stt = con.createStatement();
									String id = rss.getString(2).trim();
									stt.executeUpdate("update match set ticketCount = 0 where match_id = '"+id+"'");
									stt.close();
								}
							}
							rss.close();
							st1.close();
							userID = rs1.getString(1);
							Statement statuscheck = con.createStatement();
							ResultSet rs3 = statuscheck.executeQuery("select us_id from member");
							while (rs3.next()) {
								memberList.add(rs3.getString(1));
							}
							rs3.close();
							statuscheck.close();
							
							if (memberList.contains(userID)) {
								status = "Member";
								Statement pnt = con.createStatement();
								ResultSet rs4 = pnt
										.executeQuery("select clubpoint, expirydate from member where us_id ='" + userID + "'");
								while (rs4.next()) {
									String[] expirydate = rs4.getString(2).split("-");
									//boolean expired = isExpired(expirydate);
									if (isExpired(expirydate)) {
										createAlertBox("Alert","Your account has been expired");
										status = "General User";
										point = "N/A";
										Statement st6 = con.createStatement();
										st6.executeUpdate("Delete from member where us_id = '"+userID+"'");
										return 1;
									}
									if (!rs4.getString(1).isEmpty()) {
										point = rs4.getString(1);
									}
								}
							} else {
								status = "General User";
								point = "N/A";
							}
							Statement admincheck = con.createStatement();
							ResultSet rs2 = admincheck.executeQuery("select us_id from administrator");
							ArrayList<String> adminList = new ArrayList<String>();
							while (rs2.next()) {
								adminList.add(rs2.getString(1));
							}

							if (adminList.contains(userID)) {
								return 2;
							} else {
								return 1;
							}
						}
					}
					passcheck.close();
					rs1.close();
				} else {
					return 0;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return 0;
			//return 1;

		}

    // check if something expires
	private static boolean isExpired(String[] expirydate) {
		String[] curtime = getCurrentTime().split("-");
		boolean expired = false;
		if (Integer.parseInt(expirydate[0]) < Integer.parseInt(curtime[0]) ||
                (Integer.parseInt(expirydate[0]) == Integer.parseInt(curtime[0]) && Integer.parseInt(expirydate[1]) < Integer.parseInt(curtime[1]))||
                (Integer.parseInt(expirydate[0]) == Integer.parseInt(curtime[0]) && Integer.parseInt(expirydate[1]) == Integer.parseInt(curtime[1]) && Integer.parseInt(expirydate[0]) == Integer.parseInt(curtime[0]) && Integer.parseInt(expirydate[2]) < Integer.parseInt(curtime[2]))) {
            expired = true;
        }
		return expired;
	}

	public static void handleRefund(ObservableList<Transaction> transData, int selectId) {
		Transaction s = transData.get(selectId);
		String comf = s.getComf();
		String tCount = s.getCount();
		String price = s.getPrice();
		String mid = s.getMatch();
		int paycount = 0;
		int total = Integer.parseInt(price)*Integer.parseInt(tCount)/5;
		// tickets purchased with points not allowed to refund
		if (comf.contains("p")) {
			createAlertBox("Alert", "Sorry, we cannot refund tickets purchased by points.");
		}
		else {
			try {
				Statement st = con.createStatement();
				ResultSet date = st.executeQuery(" select mdate from match where match_id = '" + mid + "'");
				while (date.next()) {
					String[] matchDate = date.getString(1).split("-");
					if(isExpired(matchDate)){
						createAlertBox("alert", "Sorry, we cannot refund tickets for expired matches.");
					}
					else{
						transData.remove(selectId);
						// get first tCount tids, set their associated payment_id to
						// null
						Statement st1 = con.createStatement();
						ResultSet tid = st1.executeQuery(" select tp.ticket_id from ticketprice tp, payment p, ticketconfirmation tc " +
								"where comf =  '" + comf + "' and tp.payment_id = p.payment_id and tc.match_id = '" + mid + "' and tp.ticket_id = tc.ticket_id");
						while (tid.next()) {
							Statement st3 = con.createStatement();
							st3.executeUpdate(
									"update ticketprice set payment_id = null where ticket_id = '" + tid.getString(1) + "'");
						}
						// update tCount
						Statement st2 = con.createStatement();
						st2.executeUpdate("update match set ticketCount = match.ticketCount + " + tCount
								+ " where match.match_id = '" + mid + "'");
						if(status.equals("Member")){
							Statement st3 = con.createStatement();
							st3.executeUpdate("update member set clubpoint = member.clubpoint - " + total
									+ " where us_id = '" + userID + "'");
							int currentPoint  = Integer.parseInt(point) - total;
							point = ""+currentPoint;
						}
						// delete payment if all associated tickets refunded;
						Statement st4 = con.createStatement();
						ResultSet payment = st4.executeQuery("select count(tp.payment_id) from ticketprice tp "
								+ "where tp.payment_id  = (select payment_id from payment where comf = '" + comf + "')");
						while (payment.next()) {
							paycount = Integer.parseInt(payment.getString(1));
						}
						if (paycount == 0) {
							Statement st5 = con.createStatement();
							st5.executeUpdate("delete from payment where comf = '" + comf + "'");
						}

						createAlertBox("alert", "Refund Successfully! Your clubpoints has been updated!");

					}
				}

			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public static void handleSearch(String input, ObservableList<Match> matchData) throws SQLException {
		String[] inputArray = input.split(",");
		ArrayList<String> matchidrs = new ArrayList<String>();
		ArrayList<Match> matchtablers = new ArrayList<Match>();
		Statement st3 = con.createStatement();

		String sql = "select m.match_id" + " from sport s, match m where " + "m.sport_id = s.sport_id and (";

		for (int i = 0; i < inputArray.length; i++) {
			String strim = inputArray[i].trim().toUpperCase();
			sql += "UPPER(m.location) like '%" + strim + "%' " + "or m.mdate like '%" + strim + "%' ";

			if (i == inputArray.length - 1) {
				sql += "or UPPER(s.title) like '%" + strim + "%')";
			} else {
				sql += "or UPPER(s.title) like '%" + strim + "%' or ";
			}
		}

		ResultSet searchrs = st3.executeQuery(sql);
		while (searchrs.next()) {
			matchidrs.add(searchrs.getString(1));
		}
		searchrs.close();
		st3.close();

		// handle athlete
		Statement st = con.createStatement();
		String sql2 = "select am.match_id from athlete a, athmatch am " + "where a.ath_id = am.ath_id and (";

		for (int i = 0; i < inputArray.length; i++) {
			String strim = inputArray[i].trim().toUpperCase();

			if (i == inputArray.length - 1) {
				sql2 += "UPPER(a.name) like '%" + strim + "%')";
			} else {
				sql2 += "UPPER(a.name) like '%" + strim + "%' or ";
			}
		}
		ResultSet searchathrs = st.executeQuery(sql2);
		while (searchathrs.next()) {
			if (!matchidrs.contains(searchathrs.getString(1))) {
				matchidrs.add(searchathrs.getString(1));
			}
		}
		searchathrs.close();
		st.close();
		
		
		for (Match m : matchtable) {
			if (matchidrs.contains(m.getId())) {
				matchtablers.add(m);
			}
		}

		matchData.setAll(matchtablers);

	}

	public static void handleCheckOut(GridPane itemGridPane) {
		for (int i = 0; i < checkoutItems.size(); i++) {
			Match m = checkoutItems.get(i);
			Label title = new Label(m.getSport() + ", " + m.getAthlete());
			Label price = new Label(m.getPrice());
			int tc = 0;
			try {
				Statement st = con.createStatement();
				ResultSet tcount = st
						.executeQuery("select ticketCount from match where match_id = '" + m.getId() + "'");
				while (tcount.next()) {
					tc = Integer.parseInt(tcount.getString(1));
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			ObservableList<Integer> numPicker = FXCollections.observableArrayList();
			for (int j = 1; j < tc + 1; j++) {
				numPicker.add(j);
			}
			Label remaining = new Label("" + tc);
			ChoiceBox<Integer> purchase = new ChoiceBox<Integer>(numPicker);
			itemGridPane.addRow(i + 1, title, price, remaining, purchase);
		}
	}

	// checkoutItems (global variable)
	public static void handlePurchase(GridPane itemGridPane) throws IOException {
		ArrayList<Integer> ticketNumbers = getTicketNumber(itemGridPane);
		purchaseNum = ticketNumbers;
		int total = 0;
		String pid = "";
		String comf = "";
		total = getTotalPrice(total, ticketNumbers);
		if (total <0) {
			createAlertBox("Alert","Please insert ticket number");
			return;
		} else if (total == 0) {
			return;
		}
		if (!status.equals("Member")) {
			showPurchaseView();
		} else {
			// add info into transaction table and close window

			
			int pointIncrement = total / 5;
			try {
				// update payment info for all checkoutitems in one purchase
				comf = getrandID("comf", "payment");
				pid = getrandID("payment_id", "payment");
				Statement st = con.createStatement();
				st.executeUpdate("insert into payment values ('" + pid + "', '" + userID + "', '" + getCurrentTime()
						+ "', '" + comf + "')");
				Statement st0 = con.createStatement();

				st0.executeUpdate("update member set clubpoint = member.clubpoint + " + pointIncrement
						+ " where us_id = '" + userID + "'");
				int currentPoint = Integer.parseInt(point) + pointIncrement;
				point = ""+currentPoint;
			} catch (Exception e) {
				System.out.println(e);
			}
			executePurchase(pid, comf, ticketNumbers);
			createAlertBox("Alert", "Successful Purchase of $" + total + '\n' + "Your confirmation id is: " + comf
					+ '\n' + "Earned " + pointIncrement + " clubpoints.");
		}
	}

	// point 1:5
	public static void handlePointPurchase(GridPane itemGridPane) {
		String pid = "";
		String comf = "";
		ArrayList<Integer> ticketNumbers = getTicketNumber(itemGridPane);
		if (!status.equals("Member")) {
			createAlertBox("Alert", "Sorry, only members can redeem points for tickets.");
		} else {
			// calculate total points needed for redeem
			int total = 0;
			int remainpnt = Integer.parseInt(point);
			total = getTotalPrice(total, ticketNumbers)*5;
			if (total < 0) {
				return;
			}
			if (total < remainpnt) {
				remainpnt = remainpnt - total;
				point = "" + remainpnt;
				try {
					// update payment info for all checkoutitems in one purchase
					comf = getrandID("comfp", "payment");
					pid = getrandID("payment_id", "payment");
					Statement st0 = con.createStatement();
					st0.executeUpdate("update member set clubpoint = '" + point + "' where us_id = '" + userID + "'");
					Statement st = con.createStatement();
					st.executeUpdate("insert into payment values ('" + pid + "', '" + userID + "', '" + getCurrentTime()
							+ "', '" + comf + "')");
				} catch (Exception e) {
					e.printStackTrace();
				}
				executePurchase(pid, comf, ticketNumbers);
				createAlertBox("Alert", "Successful Purchase, your confirmation id is: " + comf);
			} else {
				createAlertBox("Alert", "No enough points!");
			}
		}

	}

	// calculate total price of current payment
	private static int getTotalPrice(int total, ArrayList<Integer> ticketNumbers) {
		try {
			if (ticketNumbers.size() != checkoutItems.size()) {
				return -1;
			}
		for (int i = 0; i < checkoutItems.size(); i++) {
			int price = Integer.parseInt(checkoutItems.get(i).getPrice());
			total = total + price * ticketNumbers.get(i);
		}
		return total;
		} catch (Exception e) {
			createAlertBox("Alert","Please select ticket number");
			return 0;
		}
		
	}

	// for each checked out item, update ticket info
	private static void executePurchase(String pid, String comf, ArrayList<Integer> ticketNumbers) {
		for (int i = 0; i < checkoutItems.size(); i++) {
			String mid = checkoutItems.get(i).getId().trim();
			try {
				Statement st1 = con.createStatement();
				ResultSet tid = st1.executeQuery("select tp.ticket_id from ticketprice tp ,ticketconfirmation tc "
						+ "where tp.ticket_id = tc.ticket_id and tc.match_id = '" + mid
						+ "' and tp.payment_id is null and rownum <= " + ticketNumbers.get(i));
				while (tid.next()) {
					Statement st2 = con.createStatement();
					st2.executeUpdate("update ticketprice set payment_id = '" + pid + "' where ticket_id = '"
							+ tid.getString(1) + "'");
				}
				// ticket count update
				Statement st3 = con.createStatement();
				st3.executeUpdate("update match set ticketCount = match.ticketCount - " + ticketNumbers.get(i)
						+ " where match.match_id = '" + mid + "'");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// add each checkoutitem to transaction history
			String title = checkoutItems.get(i).getDate() + ", match_id: " + checkoutItems.get(i).getId().trim() + "in "
					+ checkoutItems.get(i).getLocation();
			transDatatemp
					.add(new Transaction(title, checkoutItems.get(i).getPrice(), comf, mid, "" + ticketNumbers.get(i)));
			closeSecondary();
		}
	}

	private static ArrayList<Integer> getTicketNumber(GridPane itemGridPane) {
		ArrayList<Integer> ticketNumbers = new ArrayList<Integer>();

		ObservableList<Node> childrens = itemGridPane.getChildren();
		for (Node node : childrens) {
			if (itemGridPane.getColumnIndex(node) != null && itemGridPane.getRowIndex(node) != null) {
				if (itemGridPane.getColumnIndex(node) == 3 && itemGridPane.getRowIndex(node) != 0) {
					ChoiceBox<Integer> ticketbox = (ChoiceBox<Integer>) node;
					ticketNumbers.add(ticketbox.getValue());
				}
			}
		}

		return ticketNumbers;
	}


	// true if success
	public static boolean handleForgotPassSubmit(String username, String ans, String newpass) {
		ArrayList<String> nametable = new ArrayList<String>();
		try {
			Statement st;
			st = con.createStatement();
			ResultSet usernamers = st.executeQuery("select us_id from generaluser");
			// !
			while (usernamers.next()) {
				nametable.add(usernamers.getString(1).trim());

			}

			if (nametable.contains(username)) {
				// username exist

				Statement st1;
				st1 = con.createStatement();
				ResultSet ansrs = st1.executeQuery("select forgot from generaluser where us_id = '" + username + "'");
				while (ansrs.next()) {
					String answer = ansrs.getString(1).trim();
					if (answer.equals(ans)) {

						Statement st2;
						st2 = con.createStatement();
						st2.executeUpdate(
								"Update generaluser set password = '" + newpass + "' where us_id = '" + username + "'");
						secondaryStage.close();
						return true;
					} else {
						return false;
					}
				}

			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	// 300 points for one ticket
	public static boolean handleUpgradeSubmit(String credit, String value, String date) {
		// check credit card

		try {
			if (credit.trim().length() != 16 || !isNumeric(credit)) {
				return false;
			}

			int pointadd = 0;
			int fee = 0;

			if (value.equals("$10, for 10 points")) {
				pointadd = 10;
				fee = 10;
			} else if (value.equals("$20, for 100 points")) {
				pointadd = 100;
				fee = 20;
			} else if (value.equals("$30, for 300 points")) {
				pointadd = 300;
				fee = 30;
			}

			Statement st1 = con.createStatement();
			st1.executeUpdate("insert into member values('" + userID + "'," + pointadd + " , '" + date + "'," + fee
					+ ", '" + credit + "')");
			createAlertBox("alert",
					"Congratulations! You are rewarded clubpoints for register for our membership!");
			status = "Member";
			point = Integer.toString(pointadd);
			closeSecondary();
			return true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return true;

	}

	public static void handlePurchaseComfirm(String text) {
		if (text.isEmpty()) {
			createAlertBox("alert", "Input cannot be empty.");
		}
		if (text.length() != 16 || text.matches(".*[a-zA-Z]+.*")) {
			createAlertBox("alert", "Invalid card number, please try again.");
		} else {
			closeSecondary();
			String pid = "";
			String comf = "";
			try {
				// update payment info for all checkoutitems in one purchase
				// he is not a member, so no point
				comf = getrandID("comf", "payment");
				pid = getrandID("payment_id", "payment");
				Statement st = con.createStatement();
				st.executeUpdate("insert into payment values ('" + pid + "', '" + userID + "', '" + getCurrentTime()
						+ "', '" + comf + "')");
			} catch (Exception e) {
				System.out.println(e);
			}
			executePurchase(pid, comf, purchaseNum);
			createAlertBox("Alert", "Successful Purchase, your confirmation id is: " + comf);
		}
	}

	public static void closeSecondary() {
		secondaryStage.close();
	}

	public static void main(String[] args) {
		try {
			// sql connection
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_w8f9", "a21752126");

			launch(args);
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Administrator view
	public static void handleAddAthlete(ObservableList<Athlete> athleteData, String name, String country, String sport,
			String age) {
		// insert into database
		try {
			int age1 = Integer.parseInt(age);
			String athleteID = getrandID("ath_id", "athlete");

			Statement st5;
			st5 = con.createStatement();
			ResultSet sportid = st5.executeQuery("select sport_id from Sport where title ='" + sport + "'");
			String s_id = null;
			while (sportid.next()) {
				s_id = sportid.getString(1);
			}

			Statement st3;
			st3 = con.createStatement();
			st3.executeUpdate("INSERT INTO Athlete " + "VALUES ('" + athleteID + "','" + name + "','" + age1 + "','"
					+ country + "','" + s_id + "')");
			Athlete a = new Athlete(athleteID, name, country, sport, age);

			athleteData.add(a);
			athlistData.add(a);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void handleRemoveAthlete(ObservableList<Athlete> athleteData, int selectId) {
		{
			try {
				Athlete athlete = athleteData.get(selectId);
				String id = athlete.getId();
				Statement st;
				st = con.createStatement();
				st.executeUpdate("DELETE FROM athlete WHERE ath_id = '" + id + "'");
				athleteData.remove(selectId);
				athlistData.remove(athlete);

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	public static void handleAddMatch(ObservableList<Match> matchData, String location, String date,
			ObservableList<Athlete> athselected, int ticketnum, String sport, String price, TableView<Match> rstable)
			throws SQLException, ParseException {

		// consider adding same match
		ArrayList<String> selectedathlist = new ArrayList<String>();
		for (Athlete a : athselected) {
			selectedathlist.add(a.getId().trim());
		}

		String sql = "select match_id from match where " + "UPPER(location) like '" + location.toUpperCase() + "' and "
				+ "mdate like '" + date + "'";

		// add athlete check
		for (int i = 0; i < selectedathlist.size(); i++) {
			sql += " and match_id in (select a.match_id from match m, athmatch a where m.match_id = a.match_id and a.ath_id = '"
					+ selectedathlist.get(i) + "')";
		}

		Statement duplicationcheck = con.createStatement();
		ResultSet checkrs = duplicationcheck.executeQuery(sql);

		while (checkrs.next()) {
			// possible duplicate, check price
			String mid = checkrs.getString(1);

			Statement pricecheck = con.createStatement();
			ResultSet pricers = pricecheck.executeQuery("select count(*) from ticketprice tp, ticketconfirmation tc "
					+ "where tp.ticket_id = tc.ticket_id and tc.match_id = '" + mid.trim() + "' and " + "tp.price = "
					+ price.trim());

			while (pricers.next()) {
				if (pricers.getInt(1) == 0) {
					// same information but different price
				} else {
					// exactly same match, so add ticket only
					int ticketcount = 0;
					ArrayList<String> tids = getticketidcust(ticketnum);
					for (Match m : matchData) {
						if (m.getId().trim().equals(mid.trim())) {
							ticketcount = m.getTicket() + ticketnum;
							m.setTicket(ticketcount);
							break;
						}
					}
					ticketGenerator(ticketnum, price, mid, tids);
					Statement updateticket = con.createStatement();
					updateticket.executeUpdate(
							"update match set ticketcount = " + ticketcount + " where " + "match_id = " + mid);
					rstable.refresh();
					return;
				}
			}
			pricers.close();
			pricecheck.close();

		}

		checkrs.close();
		duplicationcheck.close();

		String mid = getrandID("match_id", "match");

		// get sport_id
		String sid = null;
		Statement st2 = con.createStatement();
		ResultSet sidrs = st2.executeQuery("select sport_id from sport where title like '%" + sport + "%'");
		while (sidrs.next()) {
			sid = sidrs.getString(1);
		}

		sidrs.close();
		st2.close();

		// insert match
		Statement st3 = con.createStatement();
		st3.executeUpdate("INSERT INTO match " + "VALUES ('" + mid + "','" + location.trim() + "','" + date.trim()
				+ "','" + ticketnum + "','" + sid + "')");

		st3.close();

		// insert athmatch
		String athList = "";
		for (int i = 0; i < athselected.size(); i++) {
			Athlete a = athselected.get(i);
			if (i == athselected.size() - 1) {
				athList += a.getName();
			} else {
				athList += a.getName() + ", ";
			}
			String aid = a.getId();
			Statement st4 = con.createStatement();
			st4.executeUpdate("insert into athmatch values('" + mid + "','" + aid + "')");
			st4.close();
		}

		matchData.add(new Match(sport.trim(), athList, location.trim(), date.trim(), price, mid, ticketnum));

		// insert ticket
		ArrayList<String> tids = getticketidcust(ticketnum);
		ticketGenerator(ticketnum, price, mid, tids);

	}

	private static void ticketGenerator(int ticketnum, String price, String mid, ArrayList<String> tids)
			throws SQLException {
		String sqladd = "insert all ";

		for (int j = 0; j < ticketnum; j++) {
			String tid = tids.get(j).trim();
			sqladd += "into ticketconfirmation values('" + tid + "','" + mid + "') ";
			sqladd += "into ticketprice values('" + tid + "',''," + price + ") ";
		}

		sqladd += "select * from dual";
		Statement st6 = con.createStatement();
		int s = st6.executeUpdate(sqladd);

		Statement st8 = con.createStatement();
		st8.executeUpdate("update match set ticketcount = " + ticketnum + " where match_id = '" + mid + "'");

		st6.close();
		st8.close();
	}

	public static void handleRemoveMatch(ObservableList<Match> matchData, int selectId) {

		try {
			Match match = matchData.get(selectId);
			String id = match.getId();
			Statement st;
			st = con.createStatement();
			st.executeUpdate("DELETE FROM Match WHERE match_id = '" + id + "'");

			matchData.remove(selectId);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void handleAddSport(ObservableList<Sport> sportData, String title,
			ObservableList<String> sportCBoxData) {
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select count(*) from sport where UPPER(title) like '" + title.toUpperCase() + "'");
			while (rs.next()) {
				if (rs.getInt(1) != 0) {
					createAlertBox("Alert", "Sport is already in the table");
					return;
				}
			}

			String sportID = getrandID("sport_id", "sport");

			Statement st5 = con.createStatement();
			st5.executeUpdate("INSERT INTO sport " + "VALUES ('" + sportID + "','" + title + "')");
			sportData.add(new Sport(sportID, title));
			sportCBoxData.add(title);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// remove the select one from the db
	public static void handleRemoveSport(ObservableList<Sport> sportData, int selectId) {

		try {
			Sport sport = sportData.get(selectId);
			String id = sport.getId();
			Statement st;
			st = con.createStatement();
			st.executeUpdate("DELETE FROM sport WHERE sport_id = '" + id + "'");

			sportData.remove(selectId);
			

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void adminInit(ObservableList<Match> matchData, ObservableList<Athlete> athleteData,
			ObservableList<Sport> sportData, ObservableList<Athlete> athlistViewData,
			ObservableList<String> sportCBoxData) throws SQLException {
		// Data cleanse
		matchData.clear();
		athleteData.clear();
		sportData.clear();
		athlistViewData.clear();
		sportCBoxData.clear();
		
		athlistData = athlistViewData;
		ObservableList<Sport> tempsport = FXCollections.observableArrayList();
		ObservableList<Athlete> tempathlete = FXCollections.observableArrayList();

		// handle sport data
		Statement st = con.createStatement();
		ResultSet sportdatars = st.executeQuery("select * from sport");
		while (sportdatars.next()) {
			tempsport.add(new Sport(sportdatars.getString(1).trim(), sportdatars.getString(2).trim()));

		}
		sportData.setAll(tempsport);
		sportdatars.close();
		st.close();

		// handle athlete data
		Statement st2 = con.createStatement();
		ResultSet athletedatars = st2.executeQuery("select a.ath_id, a.name,a.country,s.title,a.age from "
				+ "athlete a, sport s where a.sport_id = s.sport_id");

		while (athletedatars.next()) {
			tempathlete.add(new Athlete(athletedatars.getString(1).trim(), athletedatars.getString(2),
					athletedatars.getString(3), athletedatars.getString(4), athletedatars.getString(5)));

		}
		athleteData.setAll(tempathlete);
		athletedatars.close();
		st2.close();

		// handle match data
		Statement st3 = con.createStatement();
		ResultSet sportrs = st3.executeQuery("select s.title, m.location, m.mdate, tp.price, m.match_id, m.ticketCount"
				+ " from sport s, match m, ticketconfirmation tc, ticketprice tp where "
				+ "m.match_id = tc.match_id and tc.ticket_id = tp.ticket_id and m.sport_id = s.sport_id "
				+ "group by s.title, m.location, m.mdate, tp.price, m.match_id, m.ticketCount");

		while (sportrs.next()) {
			String sport = sportrs.getString(1).trim();
			String location = sportrs.getString(2).trim();
			String date = sportrs.getString(3);
			String price = sportrs.getString(4);
			String matchid = sportrs.getString(5).trim();
			int ticketcout = sportrs.getInt(6);

			Statement st4 = con.createStatement();
			ResultSet rs4 = st4.executeQuery("select a.name from match m, athlete a, athmatch am "
					+ "where m.match_id = am.match_id and am.ath_id = a.ath_id and m.match_id = '" + matchid + "'");
			String ath_rs = "";
			boolean first = true;
			while (rs4.next()) {
				if (first) {
					ath_rs = rs4.getString(1);
					first = false;
				} else {
					ath_rs += ", " + rs4.getString(1);
				}
			}
			matchData.add(new Match(sport, ath_rs, location, date, price, matchid, ticketcout));
			rs4.close();
			st4.close();
		}

		sportrs.close();
		st3.close();

		// handle athlete list
		athlistViewData.setAll(tempathlete);

		// handle sport choice box
		for (Sport s : tempsport) {
			sportCBoxData.add(s.getTitle());
		}

	}

	// generate ID for all tables, for example, getrandID("payment_id",
	// "payment")
	// special case for comf in payment table, enter getrandID("comfp",
	// "payment") for point
	// getrandID("comf", "payment") for non point
	private static String getrandID(String idname, String tablename) throws SQLException {
		try {
			String id = "";
			String sql = "";
			String newid = "";

			// separate statement process to improve performance
			if (idname.equals("comfp")) {
				sql = "select comf from payment where comf like '%p%' order by comf";
				newid = "p00000";

				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);

				while (rs.next()) {
					String temp = rs.getString(1).substring(1, 6);
					int idtemp = Integer.parseInt(temp) + 1;
					id = String.valueOf(idtemp);

				}
				rs.close();
				st.close();

			} else if (idname.equals("comf")) {
				sql = "select comf from payment where comf not like '%p%' order by comf";
				newid = "000000";

				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);

				while (rs.next()) {
					int idtemp = rs.getInt(1) + 1;
					id = String.valueOf(idtemp);

				}
				rs.close();
				st.close();
			} else {
				sql = "select " + idname + " from " + tablename + " order by " + idname;
				newid = "000000";

				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);

				while (rs.next()) {
					int idtemp = rs.getInt(1) + 1;
					id = String.valueOf(idtemp);

				}
				rs.close();
				st.close();
			}

			int length = id.length();
			id = newid.substring(0, 6 - length) + id;

			return id.trim();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "000000";
	}

	private static String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);

	}

	public static String getExpiryTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		java.util.Date date = new java.util.Date();

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.YEAR, 1);

		return dateFormat.format(cal.getTime());

	}

	private static ArrayList<String> getticketidcust(int num) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		String id = "";
		String sql = "select ticket_id from ticketconfirmation order by ticket_id";
		String newid = "000000";
		int idtemp = 0;

		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			idtemp = rs.getInt(1) + 1;

		}

		for (int i = 0; i < num; i++) {
			id = String.valueOf(idtemp + i);
			int length = id.length();
			id = newid.substring(0, 6 - length) + id;
			result.add(id);
		}

		return result;

	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	public static void handlepriceSearch(int bound, ObservableList<Match> matchData) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<Match> resulttable = new ArrayList<Match>();
		String sql = "";
		if (bound == 50) {
			for (Match m: matchtable) {
				if (Integer.parseInt(m.getPrice()) < 50) {
					resulttable.add(m);
				}
			}
		} else if (bound == 75) {
			for (Match m: matchtable) {
				if (Integer.parseInt(m.getPrice()) > 50 && Integer.parseInt(m.getPrice()) < 200) {
					resulttable.add(m);
				}
			}
		} else {
			for (Match m: matchtable) {
				if (Integer.parseInt(m.getPrice()) > 200) {
					resulttable.add(m);
				}
		}
		}
		
		
		matchData.setAll(resulttable);
		
	}

	
	
	
	
}
