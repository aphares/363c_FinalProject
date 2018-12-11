package cs363c;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * This is an application which, upon succesful login, queries the tweets database.
 * @author Andrew Phares
 * @author Marc Isaac
 */
public class Tester {
	
	/**
	 * This script is the same for both questions 1 and 2, and is migrated from the sample
	 * classes given during class. It draws a box on the screen into which the user can input 
	 * his/her login information.
	 * @return the login which the user has provided
	 */
	public static String[] loginDialog() {
		String result[] = new String[2];
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();
		cs.fill = GridBagConstraints.HORIZONTAL;
		JLabel lbUsername = new JLabel("Username: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(lbUsername, cs);
		JTextField tfUsername = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		panel.add(tfUsername, cs);
		JLabel lbPassword = new JLabel("Password: ");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(lbPassword, cs);
		JPasswordField pfPassword = new JPasswordField(20);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		panel.add(pfPassword, cs);
		panel.setBorder(new LineBorder(Color.GRAY));
		String[] options = new String[] { "OK", "Cancel" };
		int ioption = JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.OK_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (ioption == 0) // pressing OK button
		{
			result[0] = tfUsername.getText();
			result[1] = new String(pfPassword.getPassword());
		}
		return result;
	}

	/**
	 * This method calls to the external SQL server and return the data from the
	 * chosen queries
	 * 
	 * @param stmt object that will call to SQL server
	 * @param sqlQuery the query to execute
	 * @throws SQLException this will be thrown if SQL syntax is incorrect or if some other issue
	 * arises when using the stmt object
	 */
	private static void runQuery(Statement stmt, String sqlQuery) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsMetaData;
		String toShow;
		rs = stmt.executeQuery(sqlQuery);
		rsMetaData = rs.getMetaData();
		System.out.println(sqlQuery);
		toShow = "";
		while (rs.next()) {
			for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
				toShow += rs.getString(i + 1) + ", ";
			}
			toShow += "\n";
		}
		if (toShow == "" ) {
			toShow = "No results found matching your criteria.";
		}
		JOptionPane.showMessageDialog(null, toShow);
	}
	
	/**
	 * This execute the java window requiring a login, and then upon successful
	 * login will allow the user to execute various SQL queries based on an input
	 * between 1-10
	 * @param args main method
	 */
	public static void main(String[] args) {
		
		String result[] = loginDialog();
		String dbServer = "jdbc:mysql://localhost:3306/tweets_database?useSSL=false";
		String userName = result[0];
		String password = result[1];
		

		
		Connection con = null;
		Statement stmt;
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(dbServer, userName, password);
			stmt = con.createStatement();
			String sqlQuery = "";
			String option = "";
			String instruction = "Enter 1: Input a value of k (0-100). Shows k hashtags which appeared in the greatest number of states: the number of those states, which states it appeared in,"
					+ "and the hashtag." + "\n"
					+ "Enter 2: Input a month, year, hashtag name, state name, and k (0-100). Shows the number of tweets and the users' screen names and categories. \n"
					+ "Enter 3: Input a month, year, any number of state names, and k (0-100). Shows distinct hashtags that appeared in at least one of the states within the given date.\n"
					+ "Enter 4: Input a month, year, category, and k(0-100). Shows the names, states, and urls used by by k users, sorted by descending order of tweet posted time."
					+ "Enter 5: Input a month, year and k (0-100). Shows the screen name, category of the user and the tweet text, retweet count, and url used by the user for that month and year. \n"
					+ "Enter 6: Input a category, month, year, and k(0-100). Shows the user names of users mentioned in tweets of users of the specified input. \n"
					+ "Enter 7: Input a category, month, year, and k(0-100). Shows most-used hashtags among all the users of the specified input, as well as the tweet text, uername, and the mentioned user. \n"
					+ "Enter 8: Input two categories for two different users, month, year, and k (0-100). Shows tweets and the user names of any users who mention each other in the given categories. \n"
					+ "Enter 9: Input a sub-category, month, year, and k(0-100). Shows the tweet text, user name, and user mentioned for the input specified. \n"
					+ "Enter 10: Insert information of a new user into the database."
					+ "Enter anything else to exit.";
			while (true) {
				option = JOptionPane.showInputDialog(instruction);
				
				//Q3
				if (option.equals("1")) {
					option = JOptionPane.showInputDialog("Enter the value for k (0-100):");
					int selectedK = Integer.parseInt(option);
					if (selectedK > 100 || selectedK < 0) {
						JOptionPane.showMessageDialog(null,"INVALID K");
						break;
					}
					sqlQuery = "SELECT COUNT(distinct ofstate) as c, u.ofstate, h.hastagname \r\n" + 
							"FROM tweet t RIGHT JOIN `user` u ON t.posted_user = u.screen_name RIGHT JOIN tagged h ON t.tid = h.tid\r\n" + 
							"GROUP BY h.hastagname ORDER BY c desc\r\n" + 
							"LIMIT " + selectedK + ";";
					runQuery(stmt, sqlQuery);
					
				//Q7
				} else if (option.equalsIgnoreCase("2")) {
					option = JOptionPane.showInputDialog("Enter the value for k (0-100):");
					int selectedK = Integer.parseInt(option);
					if (selectedK > 100 || selectedK < 0) {
						JOptionPane.showMessageDialog(null,"INVALID K");
						break;
					}
					option = JOptionPane.showInputDialog("Enter the four digit year (e.g. 2016). Any other input will return nothing.");
					String year = option;
					option = JOptionPane.showInputDialog("Enter the two digit month (e.g. 01-12). Any other input will return nothing.");
					String month = option;
					option = JOptionPane.showInputDialog("Enter the hashtag.");
					String hashtag = option;
					option = JOptionPane.showInputDialog("Enter the state name.");
					String statename = option;
					sqlQuery = "SELECT COUNT(hastagname),u.screen_name,u.category \r\n" + 
							"FROM tweet t RIGHT JOIN `user` u ON t.posted_user = u.screen_name RIGHT JOIN tagged h ON t.tid = h.tid\r\n" + 
							"WHERE (" + getDate(year,month) + ") AND h.hastagname=\"" + hashtag + "\" AND u.ofstate = \"" + statename + "\" LIMIT " + selectedK + ";";
					runQuery(stmt, sqlQuery);

				//Q10
				} else if (option.equalsIgnoreCase("3")) {
					option = JOptionPane.showInputDialog("Enter the value for k (0-100):");
					int selectedK = Integer.parseInt(option);
					if (selectedK > 100 || selectedK < 0) {
						JOptionPane.showMessageDialog(null,"INVALID K");
						break;
					}
					option = JOptionPane.showInputDialog("Enter the four digit year (e.g. 2016). Any other input will return nothing.");
					String year = option;
					option = JOptionPane.showInputDialog("Enter the two digit month (e.g. 01-12). Any other input will return nothing.");
					String month = option;
					option = JOptionPane.showInputDialog("Enter the number of state names to input (any int above 0). Keep in mind that in the database states are both listed in their full names and abbreviations.");
					int stateint = Math.abs(Integer.parseInt(option));
					String statelist = "";
					while (stateint > 1) {
					option = JOptionPane.showInputDialog("Enter the state name.");
					statelist += "u.ofstate = \"" + option + "\" OR ";
					stateint--;
					}
					option = JOptionPane.showInputDialog("Enter the state name.");
					statelist += "u.ofstate = \"" + option + "\"";
					sqlQuery = "SELECT h.hastagname, u.ofstate \r\n" + 
							"FROM tweet t RIGHT JOIN `user` u ON t.posted_user = u.screen_name RIGHT JOIN tagged h ON t.tid = h.tid\r\n" + 
							"WHERE (" + getDate(year,month) + ") AND ("  + statelist + ") GROUP BY h.hastagname ORDER BY h.hastagname ASC LIMIT " + selectedK + ";";
					runQuery(stmt, sqlQuery);	
					
				//Q15
				} else if (option.equalsIgnoreCase("4")) {
					option = JOptionPane.showInputDialog("Enter the value for k (0-100):");
					int selectedK = Integer.parseInt(option);
					if (selectedK > 100 || selectedK < 0) {
						JOptionPane.showMessageDialog(null,"INVALID K");
						break;
					}
					option = JOptionPane.showInputDialog("Enter the four digit year (e.g. 2016). Any other input will return nothing.");
					String year = option;
					option = JOptionPane.showInputDialog("Enter the two digit month (e.g. 01-12). Any other input will return nothing.");
					String month = option;
					option = JOptionPane.showInputDialog("Enter the category.");
					String category = option;
					sqlQuery = "SELECT u.screen_name,u.ofstate,r.url \r\n" + 
							"FROM tweet t RIGHT JOIN `user` u ON t.posted_user = u.screen_name RIGHT JOIN url_used r ON t.tid = r.tid\r\n" + 
							"WHERE (" + getDate(year,month) + ") AND u.category=\"" + category + "\" ORDER BY t.posted DESC LIMIT " + selectedK + ";";
					runQuery(stmt, sqlQuery);	
					
				
				//Q16
				} else if (option.equalsIgnoreCase("5")) {
					option = JOptionPane.showInputDialog("Enter the value for k (0-100):");
					int selectedK = Integer.parseInt(option);
					if (selectedK > 100 || selectedK < 0) {
						JOptionPane.showMessageDialog(null,"INVALID K");
						break;
					}
					option = JOptionPane.showInputDialog("Enter the four digit year (e.g. 2016). Any other input will return nothing.");
					String year = option;
					option = JOptionPane.showInputDialog("Enter the two digit month (e.g. 01-12). Any other input will return nothing.");
					String month = option;
					sqlQuery = "" +  
							"SELECT u.`name`, u.category, t.textbody, t.retweet_count,t.posted,r.url from tweet t \r\n" + 
							"RIGHT JOIN `user` u ON t.posted_user = u.screen_name \r\n" + 
							"RIGHT JOIN url_used r ON t.tid = r.tid\r\n" + 
							"WHERE\r\n" + 
							getDate(year,month) +
							"ORDER BY t.retweet_count desc LIMIT " + selectedK + ";";
					runQuery(stmt, sqlQuery);
				} else if (option.equals("6")) {
					option = JOptionPane.showInputDialog("Enter the value for k (0-100):");
					int selectedK = Integer.parseInt(option);
					if (selectedK > 100 || selectedK < 0) {
						JOptionPane.showMessageDialog(null, "INVALID K");
						break;
					}
					option = JOptionPane.showInputDialog("Enter the four digit year (e.g. 2016).");
					String year = option;
					option = JOptionPane.showInputDialog("Enter the two digit month (e.g. 01-12).");
					String month = option;
					option = JOptionPane.showInputDialog("Enter the category. ");
					String category = option;
					sqlQuery = "" + 
							"SELECT m.screen_name from mentioned m RIGHT JOIN tweet t ON m.tid = t.tid RIGHT JOIN user u ON m.screen_name = u.screen_name WHERE "
							+ getDate(year, month) + " AND (u.category = \"" + category + "\") LIMIT " + selectedK + ";";
					runQuery(stmt, sqlQuery);
				} else if (option.equals("7")) {
					option = JOptionPane.showInputDialog("Enter the value for k (0-100):");
					int selectedK = Integer.parseInt(option);
					if (selectedK > 100 || selectedK < 0) {
						JOptionPane.showMessageDialog(null, "INVALID K");
						break;
					}
					option = JOptionPane.showInputDialog("Enter the four digit year (e.g. 2016).");
					String year = option;
					option = JOptionPane.showInputDialog("Enter the two digit month (e.g. 01-12).");
					String month = option;
					option = JOptionPane.showInputDialog("Enter the category. ");
					String category = option;
					// need list of states
					sqlQuery = "" + 
							"SELECT count(t.tid) from tweet t RIGHT JOIN tagged h ON t.tid = h.tid WHERE " + getDate(year, month) + "GROUP BY h.hastagname ORDER BY count(t.tid) DESC LIMIT " + selectedK + ";";
					runQuery(stmt, sqlQuery);
				} else if (option.equals("8")) {
					option = JOptionPane.showInputDialog("Enter the value for k (0-100):");
					int selectedK = Integer.parseInt(option);
					if (selectedK > 100 || selectedK < 0) {
						JOptionPane.showMessageDialog(null, "INVALID K");
						break;
					}
					option = JOptionPane.showInputDialog("Enter the four digit year (e.g. 2016). ");
					String year = option;
					option = JOptionPane.showInputDialog("Enter the two digit month (e.g. 01-12).");
					String month = option;
					option = JOptionPane.showInputDialog("Enter the category of the mentioned user. ");
					String catMen = option;
					option = JOptionPane.showInputDialog("Enter the category of the posting user. ");
					String catPost = option;

					sqlQuery =  "SELECT t.textbody, u1.screen_name,u2.screen_name\r\n" + 
								"from tweet t RIGHT JOIN mentioned m ON t.tid = m.tid RIGHT JOIN user u1 ON m.screen_name = u1.screen_name RIGHT JOIN user u2 ON t.posted_user = u2.screen_name\r\n" + 
								"WHERE u2.category = \"" + catPost + "\" AND u2.category = \"" + catMen + "\" AND (" + getDate(year, month)  + ") LIMIT " + selectedK + ";";
					runQuery(stmt, sqlQuery);
				} else if (option.equals("9")) {
					option = JOptionPane.showInputDialog("Enter the value for k (0-100):");
					int selectedK = Integer.parseInt(option);
					if (selectedK > 100 || selectedK < 0) {
						JOptionPane.showMessageDialog(null, "INVALID K");
						break;
					}
					option = JOptionPane.showInputDialog("Enter the four digit year (e.g. 2016). ");
					String year = option;
					option = JOptionPane.showInputDialog("Enter the two digit month (e.g. 01-12).");
					String month = option;
					option = JOptionPane.showInputDialog("Enter the sub-category. ");
					String subCat = option;
					sqlQuery = "" + 
							"SELECT t.textbody, t.posted_user, m.screen_name from tweet t RIGHT JOIN mentioned m ON t.tid"
							+ " = m.tid RIGHT JOIN user u ON m.screen_name = u.screen_name WHERE (u.sub_category = \"" + subCat + "\") AND (" + getDate(year, month) + ") LIMIT " + selectedK + ";";
					runQuery(stmt, sqlQuery);
				} else if (option.equals("10")) {
					option = JOptionPane.showInputDialog("Enter the screen_name of the user. ");
					String screen = option;
					option = JOptionPane.showInputDialog("Enter the name of the user. ");
					String name = option;
					option = JOptionPane.showInputDialog("Enter the sub-category of the user. ");
					String subCat = option;
					option = JOptionPane.showInputDialog("Enter the category of the user. ");
					String cat = option;
					option = JOptionPane.showInputDialog("Enter the ofstate. ");
					String ofstate = option;
					option = JOptionPane.showInputDialog("Enter the number of followers. ");
					int numFol = Integer.parseInt(option);
					option = JOptionPane.showInputDialog("Enter the number of people following. ");
					int numFoling = Integer.parseInt(option);
					
					User u = new User();
					u.setScreenname(screen);
					u.setName(name);
					u.setSubcat(subCat);
					u.setCat(cat);
					u.setState(ofstate);
					u.setFollowers(numFol);
					u.setFollowing(numFoling);
					
					Configuration config = new Configuration().configure().addAnnotatedClass(User.class);
				    ServiceRegistry reg = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
				    SessionFactory sf = config.buildSessionFactory(reg);
				    Session session = sf.openSession();
				    
				    Transaction tx = session.beginTransaction();
				    session.saveOrUpdate(u);

				    tx.commit();
				    session.close();

//					
//					sqlQuery = "" + 
//							"INSERT INTO user (screen_name, name, sub_category, category, ofstate, numFollowers, numFollowing) VALUES "
//							+ "(\"" + screen + "\", \"" + name + "\", \"" + subCat + "\", \"" + cat + "\", \"" + ofstate + "\", \"" + numFol + "\", \"" + numFoling + "\");"; 
//					runQuery(stmt, sqlQuery);
				}
				else {
					break;
				}
			}
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Program terminates due to errors");
			e.printStackTrace();
		}
	}
	/** Constructs 31 conditions in the SQL query for finding the date.
	 * @param year the year 
	 * @param month the month
	 * @return the string side of this query.
	 */
	public static String getDate(String year, String month) {
		return  "t.posted = \"" + year + "-" + month  + "-01 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-02 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-03 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-04 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-05 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-06 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-07 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-08 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-09 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-10 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-11 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-12 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-13 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-14 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-15 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-16 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-17 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-18 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-19 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-20 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-21 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-22 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-23 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-24 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-25 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-26 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-27 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-28 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-29 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-30 00:00:00\" or\r\n" + 
				"t.posted = \"" + year + "-" + month  + "-31 00:00:00\" \r\n";
	}
	
}