/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss2_lab6_th1;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import static java.sql.DriverManager.getConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author GIANG
 */
public class Main {

    private static final String SV_URL = "jdbc:mysql://localhost:3306/?&serverTimezone=UTC";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/javaqlsv?&serverTimezone=UTC";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "";

    /**
     * @param args the command line arguments
     */
    static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs = null;

    public static void main(String[] args) throws SQLException {

        Main main = new Main();
        Scanner scan = new Scanner(System.in);
        try {
            //ket noi toi csdl
            main.connectServer();
            //kiem tra db ton tai
            String dbName = "javaqlsv";
            boolean checkDatabaseExists = main.checkDatabaseExists(dbName);
            if (!checkDatabaseExists) {

                main.createNewDatabase(dbName);
            }
            main.connectDatabase();
            do {
                System.out.println("1.Hien thi thong tin ");
                System.out.println("2. Them  thong tin ");
                System.out.println("3. Sua thong tin ");
                System.out.println("4. Xoa thong tin bang Id");
                System.out.println("5. Thoat");
                System.out.println("Nhap lua chon cua ban");
                int choice = 0;
                do {
                    try {
                        choice = Integer.parseInt(scan.nextLine());
                        break;
                    } catch (NumberFormatException e) {
                    }
                } while (true);
                switch (choice) {
                    case 1:
                        main.displayData();
                        break;
                    case 2:
                        main.addNewStudent(scan);
                        break;

                    case 3:
                        main.updateStudent(scan);
                        break;
                    case 4:
                        main.lastIndex();
                        break;
                    case 5:

                        System.exit(0);
                    default:
                        System.err.println("Please enter a number 1-5!");
                }
            } while (true);

        } finally {
            main.disconectDB();
        }

    }

    public void connectServer() {
        try {
            System.out.println("Dang ket noi den server ..............");
            conn = getConnection(SV_URL, USER_NAME, PASSWORD);
            System.out.println("Ket noi thanh cong server");
        } catch (SQLException ex) {
            try {
                Thread.sleep(500);
                System.err.println("Khong the ket noi den server! Vui long thu lai");
                System.exit(0);
                //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Connect to Database
     */
    public void connectDatabase() {
        try {
            System.out.println("Dang ket noi den database ..............");
            conn = getConnection(DB_URL, USER_NAME, PASSWORD);
            System.out.println("Ket noi thanh cong database");
        } catch (SQLException ex) {
            try {
                Thread.sleep(500);
                System.err.println("Khong the ket noi den server! Vui long thu lai");
                System.exit(0);
                //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    /**
     * Close Connection , Statement,Result set
     */
    public void disconectDB() {
        try {
            conn.close();
            stmt.close();
            rs.close();
            System.out.println("Da dong ket noi database! ");
        } catch (Exception e) {
        }
    }

    public void displayListTable() throws SQLException {
        //  String tableNamePattern  = "%_Assessment_" + session + "_" + year;
        DatabaseMetaData dbMeta = conn.getMetaData();
        String[] types = {"TABLE"};
        rs = dbMeta.getTables(null, null, "%", types);
        while (rs.next()) {
            String tableCatalog = rs.getString(1);
            String tableSchema = rs.getString(2);
            String tableName = rs.getString(3);
            System.out.printf("%s - %s - %s%n",
                    tableCatalog, tableSchema, tableName);
            //System.out.println(rs.getString("TABLE_NAME"));
        }
    }

    /**
     * Check database exists
     *
     * @param dbName
     * @return
     * @throws SQLException
     */
    public boolean checkDatabaseExists(String dbName) throws SQLException {
        ResultSet resultSet = conn.getMetaData().getCatalogs();

        //iterate each catalog in the ResultSet
        while (resultSet.next()) {

            // Get the database name, which is at position 1
            String databaseName = resultSet.getString(1);
            if (databaseName.equals(dbName)) {

                return true;
            }
        }
        return false;
    }

    /**
     * Create new database
     *
     * @param dbName
     * @throws java.sql.SQLException
     */
    public void createNewDatabase(String dbName) throws SQLException {
        stmt = conn.createStatement();

        int myResult = stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
        if (myResult < 1) {
            System.err.println("Khong the tao csdl! Vui long kiem tra lai");
        } else {
            System.out.println("Tao moi csdl " + dbName + " thanh cong");
        }
    }

    /**
     * @Display in table tblsinhvien
     *
     * @Case 1
     *
     * @throws SQLException
     */
    public void displayData() throws SQLException {
        stmt = conn.createStatement();
        rs = stmt.executeQuery("select * from tblsinhvien");
        while (rs.next()) {
            if (rs.isFirst()) {
                System.out.printf("%-5s %-10s %-20s %-20s %-20s %-10s \n", "Stt", "Ma SV", "Ten", "Dia chi", "Phone", "Gioi tinh");
                System.out.printf("%-5d %-10s %-20s %-20s %-20s %-10s\n", +rs.getInt("id"), rs.getString("rollNumber"), rs.getNString("name"), rs.getNString("address"), rs.getString("phoneNumber"), (rs.getInt("gender") == 1) ? "Nam" : "Nu");
            } else {
                System.out.printf("%-5d %-10s %-20s %-20s %-20s %-10s \n", +rs.getInt("id"), rs.getString("rollNumber"), rs.getNString("name"), rs.getNString("address"), rs.getString("phoneNumber"), (rs.getInt("gender") == 1) ? "Nam" : "Nu");
            }
        }
    }

    /**
     * @param scan
     * @throws java.sql.SQLException
     * @ Case 2
     */
    public void addNewStudent(Scanner scan) throws SQLException {
        stmt = conn.createStatement();
        System.out.println("Nhap ma sv:");
        String regexRoll = "^C[0-9]{1,14}$";
        Pattern patternRoll = Pattern.compile(regexRoll);
        String rollNumber, studentName, address, phone;
        int gender;
        do {

            rollNumber = scan.nextLine().trim();
            if (rollNumber == null) {
                System.err.println("Ma sinh vien khong duoc de trong!");
            } else {
                Matcher mathRoll = patternRoll.matcher(rollNumber);
                if (mathRoll.matches()) {

                    break;
                } else {
                    System.err.println("Ma sinh vien co kieu la Cxxxxxxx voi x la cac so tu 0-9! Vui long nhap lai!");
                }
            }

        } while (true);
        System.out.println("Nhap ten sv: ");
        do {
            studentName = scan.nextLine().trim();
            if (studentName.length() > 0 && studentName.length() <= 50) {
                break;
            } else {
                System.err.println("Ten sinh vien khong qua 50 ky tu! Vui long nhap lai!");
            }
        } while (true);

        System.out.println("Nhap dia chi sinh vien");
        do {
            address = scan.nextLine().trim();
            if (address.length() > 0 && address.length() <= 200) {
                break;
            } else {
                System.err.println("Ten sinh vien khong qua 50 ky tu! Vui long nhap lai!");
            }
        } while (true);
        System.out.println("Nhap so dien thoai sinh vien: ");
        String regexPhone = "^0[1-9]{9,10}$";
        Pattern patternPhone = Pattern.compile(regexPhone);
        do {
            phone = scan.nextLine();
            Matcher matcherPhone = patternPhone.matcher(phone);
            if (matcherPhone.matches()) {
                break;
            } else {
                System.err.println("So dien thoai khong dung dinh dang!");
                System.err.println("Vui long nhap lai theo mau 0xxxxxxxx voi x la cac so tu 0-9 va x nam trong khoang tu 9 den 10 so");
            }

        } while (true);
        System.out.println("Nhap gioi tinh cho sinh vien");
        System.out.println(" 1 neu la nam ");
        System.out.println(" 0 neu la nu");
        do {

            try {
                int intGender = Integer.parseInt(scan.nextLine());
                if (intGender == 1 || intGender == 0) {
                    gender = intGender;
                    break;
                } else {
                    System.err.println("Gender chi nhan 0 hoac 1! Vui long nhap lai!");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui long nhap vao 1 so nguyen 1 hoac 0!");
            }

        } while (true);
        int id = lastIndex();
        String query = "INSERT INTO tblsinhvien VALUES(" + id + ", ' " + rollNumber + " ',' " + studentName + " ',' " + address + " ',' " + phone + " '," + gender + ")";
        System.out.println(query);
        stmt.executeUpdate(query);
        System.out.println("Da them moi 1 sinh vien!");
    }

    public void updateStudent(Scanner scan) throws SQLException {
        stmt = conn.createStatement();
        System.out.println("Nhap ma sinh vien can sua: ");
        int updateId = 0;
        do {
            try {
                updateId = Integer.parseInt(scan.nextLine());
                if (updateId > 0) {
                    break;
                }
            } catch (Exception e) {
            }
        } while (true);
        System.out.println("Nhap ma sv:");
        String regexRoll = "^C[0-9]{1,14}$";
        Pattern patternRoll = Pattern.compile(regexRoll);
        String rollNumber1, studentName1, address1, phone1;
        int gender1;
        do {

            rollNumber1 = scan.nextLine().trim();
            if (rollNumber1 == null) {
                System.err.println("Ma sinh vien khong duoc de trong!");
            } else {
                Matcher mathRoll = patternRoll.matcher(rollNumber1);
                if (mathRoll.matches()) {

                    break;
                } else {
                    System.err.println("Ma sinh vien co kieu la Cxxxxxxx voi x la cac so tu 0-9! Vui long nhap lai!");
                }
            }

        } while (true);
        System.out.println("Nhap ten sv: ");
        do {
            studentName1 = scan.nextLine().trim();
            if (studentName1.length() > 0 && studentName1.length() <= 50) {
                break;
            } else {
                System.err.println("Ten sinh vien khong qua 50 ky tu! Vui long nhap lai!");
            }
        } while (true);

        System.out.println("Nhap dia chi sinh vien");
        do {
            address1 = scan.nextLine().trim();
            if (address1.length() > 0 && address1.length() <= 200) {
                break;
            } else {
                System.err.println("Ten sinh vien khong qua 50 ky tu! Vui long nhap lai!");
            }
        } while (true);
        System.out.println("Nhap so dien thoai sinh vien: ");
        String regexPhone = "^0[1-9]{9,10}$";
        Pattern patternPhone = Pattern.compile(regexPhone);
        do {
            phone1 = scan.nextLine();
            Matcher matcherPhone = patternPhone.matcher(phone1);
            if (matcherPhone.matches()) {
                break;
            } else {
                System.err.println("So dien thoai khong dung dinh dang!");
                System.err.println("Vui long nhap lai theo mau 0xxxxxxxx voi x la cac so tu 0-9 va x nam trong khoang tu 9 den 10 so");
            }

        } while (true);
        System.out.println("Nhap gioi tinh cho sinh vien");
        System.out.println(" 1 neu la nam ");
        System.out.println(" 0 neu la nu");
        do {

            try {
                int intGender = Integer.parseInt(scan.nextLine());
                if (intGender == 1 || intGender == 0) {
                    gender1 = intGender;
                    break;
                } else {
                    System.err.println("Gender chi nhan 0 hoac 1! Vui long nhap lai!");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui long nhap vao 1 so nguyen 1 hoac 0!");
            }

        } while (true);
        int id = lastIndex();
        String query = "UPDATE  tblsinhvien SET " +
                "rollNumber ='"+rollNumber1+"', "+
                 "name ='"+studentName1+ "',"+
                 "address ='"+address1+"',"+
                 "phoneNumber ='"+phone1+"', "+
                 "gender ="+gender1+" WHERE id ="+updateId ;
                
        
        int check=stmt.executeUpdate(query);
        if(check>0){
            System.out.println("Da sua thanh cong! ");
        }else{
            System.out.println("Khong tim thay ban ghi");
        }
    }

    public static int lastIndex() throws SQLException {
        int number = 0;
        stmt = conn.createStatement();
        rs = stmt.executeQuery("select id from tblsinhvien ORDER BY id DESC LIMIT 1");
        while (rs.next()) {
            System.out.println(rs.getInt(1));
            number = rs.getInt("id") + 1;

        }
        return number;

    }
}
