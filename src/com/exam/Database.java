package com.exam;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Database {
    String url;
    String user;
    String password;

    public Database(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    // 1
    public void selectMem(){
        System.out.println();
        System.out.println("                                       회원정보목록");
        System.out.println("===============================================================================================");
        System.out.printf("%-4s   %-5s   %-14s   %-13s   %-5s   %-13s   %-15s %n","번호","이름","연락처","이메일","그룹","생년월일","등록일");
        System.out.println("===============================================================================================");

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        int row = 0;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            stmt = conn.createStatement();
            String sql = "select * from member";
            rs = stmt.executeQuery(sql);

            while (rs.next()){
                String number = rs.getString("number");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String grouped = rs.getString("grouped");
                String birth = rs.getString("birth");
                String enroll = rs.getString("enroll");

                System.out.printf("%5s | %-5s | %-15s | %-15s | %-5s | %-15s | %-15s %n", number, name, phone, email, grouped, birth, enroll);
                row++;
            }
            System.out.printf("총 %3s 명=======================================================================================%n%n", row);

        } catch (ClassNotFoundException e) {
            System.out.println("Error : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.out.println("Error : " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.out.println("Error : " + e.getMessage()); }}
            if (conn != null) { try { conn.close(); } catch (SQLException e) { System.out.println("Error : " + e.getMessage()); }}
        }
    }

    //2
    public void insertMem(){
        Connection conn = null;
        Statement stmt = null;
        Scanner sc = new Scanner(System.in);

        String name = "";
        String phone = null;
        String email = null;
        String grouped = null;
        String birth = null;

        System.out.println("등록할 회원정보를 입력해주세요");
        while (name.isEmpty()) {
            System.out.printf("> 이름(필수) : ");
            name = sc.nextLine();
        }
        System.out.printf("> 연락처 : ");
        phone = sc.nextLine();
        System.out.printf("> 이메일 : ");
        email = sc.nextLine();
        System.out.printf("> 그룹 : ");
        grouped = sc.nextLine();
        System.out.printf("> 생년월일 : ");
        birth = sc.nextLine();
        if(birth.isEmpty()){
            birth = "1900-01-01";
        }

        System.out.printf("회원 정보를 등록하시겠습니까(y/n) ?");
        String answer = sc.nextLine();
        if (answer.equals("y")){
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, password);

                stmt = conn.createStatement();
                stmt.executeUpdate("insert into member values ( 0, '" + name + "','" + phone + "','" + email + "','" + grouped + "','" + birth + "', now() )");
                System.out.println("회원정보를 정상적으로 등록하였습니다.");
            } catch (ClassNotFoundException e) {
                System.out.println("Error : " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
            } finally {
                if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.out.println("Error : " + e.getMessage()); }}
                if (conn != null) { try { conn.close(); } catch (SQLException e) { System.out.println("Error : " + e.getMessage()); }}
            }

        } else {
            System.out.println("회원정보등록에 실패했습니다.");
        }
        System.out.println();
        System.out.println("===============================================================================================");
        System.out.println();
        System.out.println();
    }
    //3
    public void updateMem(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String name = "";
        String phone = null;
        String email = null;
        String grouped = null;
        String birth = null;

        Scanner sc = new Scanner(System.in);
        int number = 0;

        System.out.printf("%n%n수정할 회원정보를 입력해주세요 : ");
        number = sc.nextInt();
        sc.nextLine();

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "select * from member where number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, number);
            rs = pstmt.executeQuery();

            if(rs.next()){// 값이 존재한다면
                //rs.next();
                System.out.printf("[ %s ]님의 회원정보%n", number);
                System.out.println("이름 : " + rs.getString("name"));
                System.out.println("연락처 : " + rs.getString("phone"));
                System.out.println("이메일 : " + rs.getString("email"));
                System.out.println("그룹 : " + rs.getString("grouped"));
                System.out.println("생년월일 : " + rs.getString("birth"));
                System.out.println();

                System.out.printf("회원정보수정을 계속하시겠습니까(y/n) ?");
                String answer = sc.nextLine();

                if (answer.equals("y")){
                    System.out.println();
                    System.out.println("** 입력하지 않으면 기존의 정보가 그대로 유지됩니다.");
                    System.out.println("** 생년월일은 YYYY-MM-DD 형태로 입력하지 않으면 수정되지 않습니다.");
                    
                    System.out.printf("> 이름 : ");
                    name = sc.nextLine();
                    if(name.isEmpty()){
                        name = rs.getString("name");
                    }

                    System.out.printf("> 연락처 : ");
                    phone = sc.nextLine();
                    if(phone.isEmpty()){
                        phone = rs.getString("phone");
                    }

                    System.out.printf("> 이메일 : ");
                    email = sc.nextLine();
                    if(email.isEmpty()){
                        email = rs.getString("email");
                    }

                    System.out.printf("> 그룹 : ");
                    grouped = sc.nextLine();
                    if(grouped.isEmpty()){
                        grouped = rs.getString("grouped");
                    }

                    System.out.printf("> 생년월일 : ");
                    birth = sc.nextLine();
                    if(birth.isEmpty()){
                        birth = rs.getString("birth");
                    }
                    if(validDate(birth) == false){
                        birth = rs.getString("birth");
                    }

                    System.out.printf("회원정보를 수정하시겠습니까(y/n) ?");
                    String danswer = sc.nextLine();

                    if (danswer.equals("y")){
                        String updatesql = "update member set name = ?, phone = ?, email = ?, grouped = ?, birth = ? where number = ?";
                        pstmt = conn.prepareStatement(updatesql);
                        pstmt.setString(1, name);
                        pstmt.setString(2, phone);
                        pstmt.setString(3, email);
                        pstmt.setString(4, grouped);
                        pstmt.setString(5, birth);
                        pstmt.setInt(6, number);
                        pstmt.executeUpdate();
                        System.out.println("회원정보를 정상적으로 수정하였습니다.");
                    } else {
                        System.out.println("회원정보수정에 실패했습니다.");
                    }
                } else{
                    System.out.println("회원정보수정에 실패했습니다.");
                }

            }else{
                System.out.println("입력하신 회원등록번호에 해당하는 회원은 존재하지 않습니다.");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Error : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.out.println("Error : " + e.getMessage()); }}
            if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) { System.out.println("Error : " + e.getMessage()); }}
            if (conn != null) { try { conn.close(); } catch (SQLException e) { System.out.println("Error : " + e.getMessage()); }}
        }

        System.out.println();
        System.out.println("===============================================================================================");
        System.out.println();
        System.out.println();
    }
    //4
    public void deleteMem(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Scanner sc = new Scanner(System.in);
        int number = 0;

        System.out.printf("%n%n삭제할 회원정보를 입력해주세요 : ");
        number = sc.nextInt();
        sc.nextLine();

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "select * from member where number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, number);
            rs = pstmt.executeQuery();

            if(rs.next()){// 값이 존재한다면
                //rs.next();
                System.out.printf("[ %s ]님의 회원정보%n", number);
                System.out.println("이름 : " + rs.getString("name"));
                System.out.println("연락처 : " + rs.getString("phone"));
                System.out.println("이메일 : " + rs.getString("email"));
                System.out.println("그룹 : " + rs.getString("grouped"));
                System.out.println("생년월일 : " + rs.getString("birth"));
                System.out.println();

                System.out.printf("회원정보삭제를 계속하시겠습니까(y/n) ?");
                String answer = sc.nextLine();

                if (answer.equals("y")){
                    String deletesql = "delete from member where number = ?";
                    pstmt = conn.prepareStatement(deletesql);
                    pstmt.setInt(1, number);
                    pstmt.executeUpdate();
                    System.out.println("회원정보를 정상적으로 삭제하였습니다.");
                } else{
                    System.out.println("회원정보삭제에 실패했습니다.");
                }
                
            }else{
                System.out.println("입력하신 회원등록번호에 해당하는 회원은 존재하지 않습니다.");
            }
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.out.println("Error : " + e.getMessage()); }}
            if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) { System.out.println("Error : " + e.getMessage()); }}
            if (conn != null) { try { conn.close(); } catch (SQLException e) { System.out.println("Error : " + e.getMessage()); }}
        }

        System.out.println();
        System.out.println("===============================================================================================");
        System.out.println();
        System.out.println();
    }
    //5
    public void messageMem(){

    }

    // 생년월일 형식 체크용 메서드
    public boolean validDate(String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false; // 유효한 날짜가 아니면 false
        }
    }

}
