package studentProject;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class StudentMain {

	public static final int INPUT = 1, UPDATE = 2, SEARHCH = 3, DELETE = 4, OUTPUT = 5, SORT = 6, STATS = 7, EXIT = 8;

	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws Exception {

		boolean mainFlag = false;
		int select = 0;

		while (!mainFlag) {
			switch (displayMenu(select)) {
			case INPUT:
				studentInput();
				break;
			case UPDATE:
				studentUpate();
				break;
			case SEARHCH:
				studentSearch();
				break;
			case DELETE:
				studentDelete();
				break;
			case OUTPUT:
				studentOutput();
				break;
			case SORT:
				studentSort();
				break;
			case STATS:
				studentStats();
				break;
			case EXIT:
				System.out.println("종료합니다.");
				mainFlag = true;
				break;
			default:
				System.out.println("잘못입력하였습니다.");
				break;
			}
		}
	}

	/** 1. 학생 정보 입력 */
	public static void studentInput() {
		try {
			boolean value = false;
			// 학년 (1~3학년 : 01 02 03) 반(1~9 : 01~09) 번호 (1~30 : 01~30)
			// 학번 패턴검색
			System.out.print("학년(01~03) 반(01~09) 번호(01~39)\n입력>>");
			String no = sc.nextLine();

			value = checkInputPattern(no, 1);
			if (!value) return;

			// 이름 패턴검색
			System.out.print("이름 입력 >>");
			String name = sc.nextLine();
			value = checkInputPattern(name, 2);
			if (!value) return;

			// 문자열 패턴검색
			System.out.print("성별 입력 (남자,여자) >>");
			String gender = sc.nextLine();
			value = checkInputPattern(gender, 3);
			if (!value)
				return;

			// kor (0~100)패턴검색
			System.out.print("국어점수 입력 (0~100) >>");
			int kor = sc.nextInt();
			value = checkInputPattern(String.valueOf(kor), 4);
			if (!value)
				return;

			// eng (0~100)패턴검색
			System.out.print("영어점수 입력 (0~100) >>");
			int eng = sc.nextInt();
			value = checkInputPattern(String.valueOf(eng), 4);
			if (!value)
				return;

			// math (0~100)패턴검색
			System.out.print("수학점수 입력 (0~100) >>");
			int math = sc.nextInt();
			value = checkInputPattern(String.valueOf(math), 4);
			if (!value)
				return;


			Student st = new Student(no, name, gender, kor, eng, math);

			DBConnection dbc = new DBConnection();
			dbc.connect();
			
			int insertReturnValue = dbc.insert(st);
			if (insertReturnValue == -1 || insertReturnValue == 0) {
				System.out.println("입력 실패입니다.");
			} else {
				System.out.println("입력 성공입니다.");
			}

			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		}
	}
	
	/** 2. 학생 점수 수정 */
	public static void studentUpate() {
		List<Student> list = new ArrayList<Student>();
		try {

			System.out.print("학생 번호 입력 >> ");
			String no = sc.nextLine();

			boolean value = checkInputPattern(no, 1);
			if (!value) return;
			
			DBConnection dbc = new DBConnection();

			dbc.connect();

			list = dbc.selectSearch(no,1);
			
			if (list.size() <= 0) {
				System.out.println("입력된 정보가 없습니다.");
			}

			for (Student student : list) {
				System.out.println(student);
			}
			
			Student imsiStudent = list.get(0);
			System.out.print("국어 점수 입력 >>" );
			int kor = sc.nextInt();
			value = checkInputPattern(String.valueOf(kor), 4);
			if (!value) return;
			imsiStudent.setKor(kor);
			
			System.out.print("영어 점수 입력 >>" );
			int eng = sc.nextInt();
			value = checkInputPattern(String.valueOf(eng), 4);
			if (!value) return;
			imsiStudent.setEng(eng);
			
			System.out.print("수학 점수 입력 >>" );
			int math = sc.nextInt();
			value = checkInputPattern(String.valueOf(math), 4);
			if (!value) return;
			imsiStudent.setMath(math);
			
			int returnUpdateValue = dbc.update(imsiStudent);
			if (returnUpdateValue == -1) {
				System.out.println("학생 수정 정보 없음");
				return;
			}
			System.out.println("학생 수정 완료하였습니다.");

			dbc.close();
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		}
	}
	
	/** 3. 학생 정보 검색 */
	public static void studentSearch() {
		List<Student> list = new ArrayList<Student>();
		try {

			System.out.print("학생 이름 입력 >> ");
			String name = sc.nextLine();
			boolean value = checkInputPattern(name, 2);
			if (!value) return;

			DBConnection dbc = new DBConnection();
			dbc.connect();

			list = dbc.selectSearch(name,2);
			
			if (list.size() <= 0) {
				System.out.println("검색한 학생의 정보가 없습니다.");
				return;
			}
			
			for (Student student : list) {
				System.out.println(student);
			}

			dbc.close();
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		}
	}

	/** 4. 학생 정보 삭제 */
	public static void studentDelete() {

		try {
			System.out.println("삭제할 학생 학번 입력 (010101)>> ");
			String no = sc.nextLine();
			boolean value = checkInputPattern(no, 1);
			if (!value)
				return;

			DBConnection dbc = new DBConnection();
			dbc.connect();

			int deleteReturnValue = dbc.delete(no);
			if (deleteReturnValue == -1) {
				System.out.println("삭제 실패입니다.");
			}
			if (deleteReturnValue == 0) {
				System.out.println("해당 학번의 정보가 없습니다.");
			} else {
				System.out.println(no + "학번의 정보를 삭제하였습니다.");
			}

			dbc.close();
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		}
	}

	/** 5. 학생 정보 전체 출력 */
	public static void studentOutput() {
		List<Student> list = new ArrayList<Student>();
		try {
			DBConnection dbc = new DBConnection();
			dbc.connect();
			
			System.out.println("1.전체출력 || 2.삭제정보 || 3.수정정보");
			int type = sc.nextInt();
			boolean value = checkInputPattern(String.valueOf(type), 5);
			if (!value) return;
			
			list = dbc.select(type);
			if (list.size() <= 0) {
				System.out.println("학생 정보가 없습니다.");
			}

			for (Student student : list) {
				if (type != 1) {
					System.out.println(student.toTriggerString());
				} else {
					System.out.println(student);					
				}
			}

			dbc.close();
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		} finally {
			sc.nextLine();
		}
	}

	/** 6. 학생 정보 정렬 : 학번, 이름, 총점 */
	public static void studentSort() {
		List<Student> list = new ArrayList<Student>();
		
		try {
			DBConnection dbc = new DBConnection();

			dbc.connect();

			System.out.print("정렬 방식을 선택하세요 (1.학번 || 2.이름 || 3.총점 >> ");
			int type = sc.nextInt();
			boolean value = checkInputPattern(String.valueOf(type), 5);
			if (!value) return;
	

			list = dbc.selectOrderBy(type);
			if (list.size() <= 0) {
				System.out.println("학생 정보가 없습니다.");
			}

			for (Student student : list) {
				System.out.println(student);
			}

			dbc.close();
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		} finally {
			sc.nextLine();
		}
	}
	
	/** 7. 학생 정보 통계 */
	public static void studentStats() {
		List<Student> list = new ArrayList<Student>();

		try {
			System.out.println("1.최고점수 || 2.최저점수");
			int type = sc.nextInt();
			
			boolean value = checkInputPattern(String.valueOf(type),6);
			if (!value) return;

			DBConnection dbc = new DBConnection();
			dbc.connect();

			list = dbc.selectMaxMin(type);
			
			if (list.size() <= 0) {
				System.out.println("검색한 학생의 정보가 없습니다.");
			}
			
			for (Student student : list) {
				System.out.println(student);
			}
			
			dbc.close();
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		} finally {
			sc.nextLine();
		}
	}
	
	/** 메뉴선택 */
	public static int displayMenu(int select) {

		try {
			System.out.println("〓".repeat(70));
			System.out.println("1.입력 || 2.수정 || 3.검색 || 4.삭제 || 5.출력 || 6.정렬 || 7.통계 || 8.종료");
			System.out.println("〓".repeat(70));
			select = sc.nextInt();

			String pattern = "^[1-9]*$"; // 숫자만
			boolean regex = Pattern.matches(pattern, String.valueOf(select));
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		} finally {
			sc.nextLine();
		}
		return select;
	}

	/** 패턴 검색 */
	public static boolean checkInputPattern(String data, int patternType) {
		String pattern = null;
		boolean regex = false;
		String message = null;

		switch (patternType) {
		case 1:
			pattern = "^0[1-3]0[1-9][0-3][0-9]$";
			message = "no 재입력 요망";
			break;
		case 2:
			pattern = "^[가-힝]{2,4}$";
			message = "name 재입력 요망";
			break;
		case 3:
			pattern = "^[남,여]자$";
			message = "gender 재입력 요망";
			break;
		case 4:
			pattern = "^[0-9]{1,3}$";
			message = "score 재입력 요망";
			break;
		case 5:
			pattern = "^[1-3]$";
			message = "정렬 타입 재입력 요망";
			break;
		case 6:
			pattern = "^[1-2]$";
			message = "통계 타입 재입력 요망";
			break;
		}

		regex = Pattern.matches(pattern, data);

		if (patternType == 4) {
			if (!regex || Integer.parseInt(data) < 0 || Integer.parseInt(data) > 100) {
				System.out.println(message);
				return false;
			}
		} else {
			if (!regex) {
				System.out.println(message);
				return false;
			}
		}
		return regex;
	}
}
