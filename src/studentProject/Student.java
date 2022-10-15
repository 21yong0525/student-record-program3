package studentProject;

import java.io.Serializable;

public class Student implements Comparable<Student>, Serializable {
	
	private static final byte SUB_NO = 3;
	
	private String no;
	private String name;
	private String gender;
	private int kor;
	private int eng;
	private int math;
	private int total;
	private double avg;
	private String grade;
	private int rate;
	private String date;
	
	public Student(String no, String name, String gender, int kor, int eng, int math, int total, double avg,
			String grade, String date) {
		this(no,name,gender,kor,eng,math);
		this.total = total;
		this.avg = avg;
		this.grade = grade;
		this.date = date;
	}

	public Student(String no, String name, String gender, int kor, int eng, int math, int total, double avg,
			String grade, int rate) {
		this(no,name,gender,kor,eng,math);
		this.total = total;
		this.avg = avg;
		this.grade = grade;
		this.rate = rate;
	}
	
	public Student(String no, String name, String gender , int kor, int eng, int math) {
		this.no = no;
		this.name = name;
		this.gender = gender;
		this.kor = kor;
		this.eng = eng;
		this.math = math;
	}

	public String getNo() {return no;}
	public void setNo(String no) {this.no = no;}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public String getGender() {return gender;}
	public void setGender(String gender) {this.gender = gender;}
	
	public int getKor() {return kor;}
	public void setKor(int kor) {this.kor = kor;}

	public int getEng() {return eng;}
	public void setEng(int eng) {this.eng = eng;}

	public int getMath() {return math;}
	public void setMath(int math) {this.math = math;}
	
	public int getTotal() {return total;}
	public void setTotal(int total) {this.total = total;}

	public double getAvg() {return avg;}
	public void setAvg(double avg) {this.avg = avg;}

	public String getGrade() {return grade;}
	public void setGrade(String grade) {this.grade = grade;}

	public int getRate() {return rate;}
	public void setRate(int rate) {this.rate = rate;}
		
	public String getDate() {return date;}
	public void setDate(String date) {this.date = date;}
	
	@Override
	public int hashCode() {
		return this.no.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Student)) return false;
		return this.no.equals(((Student)obj).no);
	}
	
	@Override
	public int compareTo(Student student) {
		return this.total - student.total;
	}
	
	public String toTriggerString() {
		System.out.println("〓".repeat(70));
		System.out.println("학생 정보\n번호\t이름\t국어\t영어\t수학\t총합\t평균\t등급\t등록날짜");
		return no + "\t" + name + "\t" + kor + "\t" + eng +"\t"+ math + "\t" + total + "\t" + avg + "\t" + grade + "\t" +  date;
	}
	
	
	@Override
	public String toString() {
		System.out.println("〓".repeat(70));
		System.out.println("학생 정보\n번호\t이름\t국어\t영어\t수학\t총합\t평균\t등급\t등수");
		return no + "\t" + name + "\t" + kor + "\t" + eng +"\t"+ math + "\t" + total + "\t" + avg + "\t" + grade + "\t" + rate;
	}
}
