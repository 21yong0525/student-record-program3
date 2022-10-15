package studentProject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class DBConnection {
	private Connection connection = null;

	/** connection */
	public void connect() {

		Properties properties = new Properties();

		try {
			FileInputStream fis = new FileInputStream("C:/Java_Test/studentProject/src/studentProject/db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Fproperties.load error" + e.getMessage());
		}

		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("userid"), properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("connerction error" + e.getMessage());
		}

	}

	/** insert statement */
	public int insert(Student st) {
		PreparedStatement ps = null;

		 String insertQuery = "call procedure_insert_student(?, ?, ?, ?, ?, ?)";
		int inserReturnValue = -1;

		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, st.getNo());
			ps.setString(2, st.getName());
			ps.setString(3, st.getGender());
			ps.setInt(4, st.getKor());
			ps.setInt(5, st.getEng());
			ps.setInt(6, st.getMath());
			inserReturnValue = ps.executeUpdate();
		} catch (SQLException e) {
			if(e.getMessage().equals("Duplicate entry '"+st.getNo()+"' for key 'PRIMARY'")) {
				System.out.println("동일한 학번이 존재합니다.");
			} else {
				System.out.println("SQLException error"+e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (ps != null) ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return inserReturnValue;
	}

	/** update statement */
	public int update(Student st) {
		PreparedStatement ps = null;
		String updateQuery = "call procedure_update_student(?, ?, ?, ?)";
		int updateReturnValue = -1;

		try {
			ps = connection.prepareStatement(updateQuery);
			ps.setString(1, st.getNo());
			ps.setInt(2, st.getKor());
			ps.setInt(3, st.getEng());
			ps.setInt(4, st.getMath());
			updateReturnValue = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException error" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return updateReturnValue;
	}

	/** select name search statement */
	public List<Student> selectSearch(String data, int type) {
		PreparedStatement ps = null;

		ResultSet rs = null;
		List<Student> list = new ArrayList<Student>();
		String selectSearchQuery = "select * from student where ";

		try {
			switch (type) {
			case 1:
				selectSearchQuery += "no like ? ";
				break;
			case 2:
				selectSearchQuery += "name like ? ";
				break;
			default:
				System.out.println("잘못 입력하였습니다.");
				return list;
			}

			ps = connection.prepareStatement(selectSearchQuery);

			String namePattern = "%" + data + "%";
			ps.setString(1, namePattern);
			rs = ps.executeQuery();

			if (!(rs != null || rs.isBeforeFirst())) return list;
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");

				list.add(new Student(no, name, gender, kor, eng, math, total, avg, grade, rate));
			}

		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (ps != null) ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return list;
	}

	/** delete statement */
	public int delete(String no) {
		PreparedStatement ps = null;
		String dleleteQuery = "delete from student where no = ?";
		int deleteReturnValue = -1;

		try {
			ps = connection.prepareStatement(dleleteQuery);
			ps.setString(1, no);
			deleteReturnValue = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException error" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (ps != null) ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return deleteReturnValue;
	}

	/** select statement */
	public List<Student> select(int type) {
		Statement st = null;
		String selectQuery = "select * from ";
		ResultSet rs = null;
		List<Student> list = new ArrayList<Student>();

		try {
			st = connection.prepareStatement(selectQuery);
			switch (type) {
				case 1: selectQuery += "student"; break;
				case 2: selectQuery += "deletestudent"; break;
				case 3: selectQuery += "updatestudent"; break;
			}

			rs = st.executeQuery(selectQuery);

			if (!(rs != null || rs.isBeforeFirst())) return list;
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");
				
				if (type != 1) {
					String date = rs.getString("date");
					list.add(new Student(no, name, gender, kor, eng, math, total, avg, grade, date));
				} else {
					list.add(new Student(no, name, gender, kor, eng, math, total, avg, grade, rate));
				}

			}

		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return list;
	}

	/** select order by statement */
	public List<Student> selectOrderBy(int type) {
		PreparedStatement ps = null;
		String selectOrderByQuery = "select * from student order by ";
		ResultSet rs = null;
		List<Student> list = new ArrayList<Student>();

		try {
			switch (type) {
				case 1: selectOrderByQuery += "no asc"; break;
				case 2: selectOrderByQuery += "name asc"; break;
				case 3: selectOrderByQuery += "total desc"; break;
				default : System.out.println("정렬 타입 오류"); return list;
			}
			ps = connection.prepareStatement(selectOrderByQuery);

			rs = ps.executeQuery(selectOrderByQuery);

			if (!(rs != null || rs.isBeforeFirst())) return list;

			int rank = 0;
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");
				
				if (type == 3) rate = ++rank;
				list.add(new Student(no, name, gender, kor, eng, math, total, avg, grade, rate));
			}

		} catch (Exception e) {
			System.out.println("select error" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return list;
	}
	
	/** selectMaxMin statement */
	public List<Student> selectMaxMin(int type) {
		Statement st = null;
		String selectMaxMinQuery = "select * from student where total = (select ";
		ResultSet rs = null;
		List<Student> list = new ArrayList<Student>();

		try {
			st = connection.createStatement();
			
			switch (type) {
				case 1: selectMaxMinQuery += "MAX(total) from student)"; break;
				case 2: selectMaxMinQuery += "MIN(total) from student)"; break;
				default : System.out.println("통계 타입 오류"); return list;
			}

			rs = st.executeQuery(selectMaxMinQuery);

			if (!(rs != null || rs.isBeforeFirst())) return list;
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");
				
				list.add(new Student(no, name, gender, kor, eng, math, total, avg, grade, rate));
			}

		} catch (Exception e) {
			System.out.println("select error" + e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return list;
	}
	
	/** Connection close */
	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("Connection close error" + e.getMessage());
		}
	}
}
