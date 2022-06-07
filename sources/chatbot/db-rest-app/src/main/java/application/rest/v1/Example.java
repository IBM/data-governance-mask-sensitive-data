package application.rest.v1;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

@Path("orders")
public class Example {

	private static Connection con;
	private static Properties env = new Properties();

	@GET
	@Path("dbconnection")
	@Produces(MediaType.TEXT_PLAIN)

	public void dbConnection() {

		
		try {
			InputStream inputStream = Example.class.getClassLoader().getResourceAsStream("/env.props");
			env.load(inputStream);
			String hostname = env.getProperty("HOSTNAME");
			String port = env.getProperty("PORT");
			String apikey = env.getProperty("API_KEY");
			String dbName = env.getProperty("DB_NAME");

			String url = "jdbc:db2://" + hostname + ":" + port + "/" + dbName + ":apiKey=" + apikey + ";securityMechanism=15;pluginName=IBMIAMauth;sslConnection=true;";
			System.out.println("url = " + url);

			// Load the driver
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			con = DriverManager.getConnection(url);
			con.setAutoCommit(false);
			System.out.println("DB Connection is set");
		} catch (Exception ex) {
			System.out.println("Exception: " + ex);
			System.err.println("Exception information");
		}
	}

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)

	public Response test() {
		List<String> list = new ArrayList<>();
		// return a simple list of strings
		list.add("Application up and running!!!");
		return Response.ok(list.toString()).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response orderDetails(Map<String, String> payload) {
		System.out.println("Orders POST");

		if (con != null) {
			if (payload.get("action").equalsIgnoreCase("validation")) {
				return validateEmail(payload.get("emailid"));
			} else {
				return getOrdersData(payload);
			}
		} else {
			System.out.println("Connection is null");
			return Response.serverError().build();
		}
	}

	private Response validateEmail(String emailid) {
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(emailid);
		if (matcher.matches()) {
			System.out.println("email valid");
			return Response.ok(new JSONObject().put("result", "valid").toString()).build();
		}
		System.out.println("email invalid");
		return Response.ok(new JSONObject().put("result", "invalid").toString()).build();
	}

	private Response getOrdersData(Map<String, String> payload) {
		Statement stmt;
		ResultSet rs;

		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM INSSCHEMA.CUSTOMER_ORDERS_VIEW WHERE E_MAIL = '" + payload.get("emailid")
					+ "'";
			System.out.println("query = " + query);
			// return Response.ok(new JSONObject().put("test", "test").toString()).build();
			rs = stmt.executeQuery(query);

			String displayData = "";
			if (payload.get("action").equalsIgnoreCase("history")) {
				displayData = "Following are your order history data: \n";
			}
			if (payload.get("action").equalsIgnoreCase("active")) {
				displayData = "Following are your active order data: \n";
			}

			int count = 0;
			while (rs.next()) {
				System.out.println("STATUS = " + rs.getString("STATUS"));
				if (payload.get("action").equalsIgnoreCase("history") && rs.getString("STATUS") == "Complete") {
					count++;
					displayData = displayData + "Order Id: " + rs.getString("ORDER_ID");
					displayData = displayData + "\nOrder Date: " + rs.getString("ORDER_DT");
					displayData = displayData + "\nItem: " + rs.getString("ITEM");
					displayData = displayData + "\nStatus: " + rs.getString("STATUS");
					displayData = displayData + "\nAmount: " + rs.getString("AMOUNT");
					displayData = displayData + "\nCredit Card No: " + rs.getString("CCNUM_FIRST12_DIGITS")
							+ rs.getString("CCNUM_LAST4_DIGITS");
					displayData = displayData + "\n\n";
				}
				if (payload.get("action").equalsIgnoreCase("active")
						&& rs.getString("STATUS").equalsIgnoreCase("Active")) {
					count++;
					displayData = displayData + "Order Id: " + rs.getString("ORDER_ID");
					displayData = displayData + "\nOrder Date: " + rs.getString("ORDER_DT");
					displayData = displayData + "\nItem: " + rs.getString("ITEM");
					displayData = displayData + "\nStatus: " + rs.getString("STATUS");
					displayData = displayData + "\nAmount: " + rs.getString("AMOUNT");
					displayData = displayData + "\nCredit Card No: " + rs.getString("CCNUM_FIRST12_DIGITS")
							+ rs.getString("CCNUM_LAST4_DIGITS");
					displayData = displayData + "\n\n";
				}
			}
			if (count == 0) {
				displayData = "No data found for the provided email id";
			}
			rs.close();

			JSONObject responseJSON = new JSONObject();
			// responseJSON.put("result", displayData).put("count", count);
			responseJSON.put("result", displayData);
			responseJSON.put("count", count);

			return Response.ok(responseJSON.toString()).build();

		} catch (Exception ex) {
			System.err.println("Exception information: " + ex.getMessage());
			return Response.serverError().build();
		}
	}

}
