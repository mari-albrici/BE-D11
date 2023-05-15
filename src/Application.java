import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;

public class Application {
	
	static Connection connection = null;

	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost:5432/BE-D11-ES2?useSSL=false";
		String username = "postgres";
		String password = "1234";
		
		try {
			System.out.println("Connecting to Database...");
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Connected to database ✅");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		insertStudent("Marianna", "Albrici", "F", null, 7.9, 6.6, 10 );
		insertStudent("Elisa", "Alvaro", "F", null, 6.9, 5.6, 10 );
		
		deleteStudent(2);
		
		HashMap<String, Object> updateStudentData = new HashMap<>();
		updateStudentData.put("name", "Alice");
		
		updateStudent(4, updateStudentData);
		
		
		getBest();
		
		getVoteRange(1, 7);
		
	}
	
	public static void insertStudent(String name, String lastname, String gender, Date birthday, double avg, double min_vote, double max_vote) {
		String sqlInsert = "INSERT INTO public.school_students(name, lastname, gender, birthday, avg, min_vote, max_vote) VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		try {
			
			PreparedStatement stmnt = connection.prepareStatement(sqlInsert);
			stmnt.setString(1, name);
			stmnt.setString(2, lastname);
			stmnt.setString(3, gender);
			stmnt.setDate(4, birthday);
			stmnt.setDouble(5, avg);
			stmnt.setDouble(6, min_vote);
			stmnt.setDouble(7, max_vote);
			
			stmnt.execute();
			
			System.out.println("Studente inserito!");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateStudent(int id, HashMap<String, Object> student) {
		String sqlUpdate = "UPDATE public.school_students SET name=? WHERE id = ?;";
		
		try {
			PreparedStatement stmntUpdate = connection.prepareStatement(sqlUpdate);
			stmntUpdate.setInt(1, id);
			stmntUpdate.setObject(2, student);

			stmntUpdate.execute();
			System.out.println("STUDENTE MODIFICATO");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteStudent(int id) {
		String sqlDelete = "DELETE FROM public.school_students WHERE id = ?;";

		try {
			PreparedStatement stmntDelete = connection.prepareStatement(sqlDelete);
			stmntDelete.setInt(1, id);
			stmntDelete.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void getBest() {
		String sqlGetBest = "SELECT MAX(avg) AS avg FROM public.school_students;";
		
		try {
			Statement stmntGetBest = connection.createStatement();
			ResultSet bestAverage = stmntGetBest.executeQuery(sqlGetBest);
			
			while(bestAverage.next()) {
				System.out.println("| Average: " + bestAverage.getString("avg"));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void getVoteRange(int min, int max) {
		String sqlVoteRange = "SELECT * FROM public.school_students WHERE min_vote >= ? AND max_vote <= ?;";
		
		try {
			PreparedStatement stmntVoteRange = connection.prepareStatement(sqlVoteRange);
			
			stmntVoteRange.setInt(1, min);
			stmntVoteRange.setInt(2, max);
			
			ResultSet voteRange = stmntVoteRange.executeQuery();
			
			while(voteRange.next()) {
				System.out.println("Student in the range are: " + voteRange.getString("name"));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
