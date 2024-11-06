package com.exam;

import javax.xml.crypto.Data;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean run = true; // 프로그램 실행 여부
        Database db = new Database("jdbc:mariadb://localhost:3306/MMS", "root", "!123456");

        while (run) {
            System.out.println("                                       회원 관리 프로그램");
            System.out.println("===============================================================================================");
            System.out.println("1.회원정보목록");
            System.out.println("2.회원정보등록");
            System.out.println("3.회원정보수정");
            System.out.println("4.회원정보삭제");
            System.out.println("5.쪽지 보내기");
            System.out.println("6.종료");
            System.out.println("===============================================================================================");
            System.out.print("메뉴를 입력하세요 : ");
            Scanner sc = new Scanner(System.in);

            try {
                int input = sc.nextInt();;
                switch (input){
                    case 1:
                        db.selectMem();
                        break;
                    case 2:
                        db.insertMem();
                        break;
                    case 3:
                        db.updateMem();
                        break;
                    case 4:
                        db.deleteMem();
                        break;
                    case 5:
                        db.messageMem();
                        break;
                    case 6:
                        run = false;
                        break;

                }
            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력");
            }
        }

    }
}
