package controller;

import java.sql.Connection;
import java.sql.DriverManager;

import model.User;

public class DBUtil {
   public static final String DRIVER = "org.mariadb.jdbc.Driver";
   public static final String URL = "jdbc:mariadb://jbstv.synology.me:3307/poketmon";
   public static Connection getConnection() throws Exception {
      Class.forName(DRIVER);
      Connection con = DriverManager.getConnection(URL,"pocketmon","Class61234!");
      //üũ
      if(con != null) {
    	  
    	  System.out.println("�����ͺ��̽� ���Ἲ��");
      }
      
      return con;
   }
}