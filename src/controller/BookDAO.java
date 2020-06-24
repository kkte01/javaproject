package controller;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.PoketmonBook1;

public class BookDAO {
	ImageView imgView= new ImageView();
	// DB에 있는 값을 가져오는 함수
	public ArrayList<PoketmonBook1> getTotalLoadList() {
		// DB 연동
		Connection con = null;
		PreparedStatement ppsm = null;
		ResultSet rs = null;
		ArrayList<PoketmonBook1> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if(con !=null) {
				System.out.println("데이터베이스 연결완료");
			}else {
				Function.getAlert(1, "데이터 베이스연결오류", "데이터 베이스 연결실패!", "데이터 베이스에 연결확인요망");
			}
			String query = "select * from bookTbl";
			ppsm = con.prepareStatement(query);
			rs = ppsm.executeQuery();
			// ArrayList에 추가하기
			arrayList = new ArrayList<PoketmonBook1>();
			imgView.setFitHeight(80);
			imgView.setFitWidth(150);
			imgView.preserveRatioProperty();
			while (rs.next()) {
				imgView = new ImageView(new Image("file:/C:/icons/"+rs.getString(2)));
				PoketmonBook1 pkmBook1 = new PoketmonBook1(rs.getInt(1), imgView,
						rs.getString(3), rs.getString(4),
						rs.getString(5));
				arrayList.add(pkmBook1);
			}
		} catch (Exception e1) {
			Function.getAlert(2, "TotalLoadList 점검 요망", "TotalLoadList 문제 발생", "문제사항"+e1.getMessage());
		}finally {
				try {
					if (rs != null)
					rs.close();
					if (ppsm != null)
						ppsm.close();
					if (con != null)
						con.close();
				} catch (SQLException e) {
					System.out.println("TotalLoadList :"+ e.getMessage());
				}
		}
		return arrayList;
	}
	//테이블 조인한 값을 랜덤으로 가져오는 함수
	public ArrayList<PoketmonBook1> getRandomJoinLoadList(){
		Connection con = null;
		PreparedStatement ppsm = null;
		ResultSet rs = null;
		ArrayList<PoketmonBook1> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if(con !=null) {
				System.out.println("데이터베이스 연결완료");
			}else {
				Function.getAlert(1, "데이터 베이스연결오류", "데이터 베이스 연결실패!", "데이터 베이스에 연결확인요망");
			}
			String query = "select * from bookTbl a inner join poketmonTBL b on a.pkmNum = b.pkmNUm";
			ppsm = con.prepareStatement(query);
			rs = ppsm.executeQuery();
			//꼭 객체화 시키기
			arrayList = new ArrayList<PoketmonBook1>();
			
			while(rs.next()) {
				PoketmonBook1 poketmonBook1 = 	new PoketmonBook1(rs.getString(6),rs.getString(3),
						rs.getString(8),rs.getString(9),rs.getString(10),
						rs.getString(11),rs.getString(12),rs.getString(13));
				arrayList.add(poketmonBook1);
			}
			
		} catch (Exception e) {
			Function.getAlert(2, "getRandomJoinLoadList 점검 요망", "getRandomJoinLoadList 문제 발생", "문제사항"+e.getMessage());
		}finally {
			try {
				if (rs != null)
				rs.close();
				if (ppsm != null)
					ppsm.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("getRandomJoinLoadList :"+ e.getMessage());
			}
		}
		return arrayList;
	}
	//더블클릭한 도감에 표시할 값을 가져오는 함수
	public ArrayList<PoketmonBook1> getPoketmonBookLoadList(){
		Connection con = null;
		PreparedStatement ppsm = null;
		ResultSet rs = null;
		ArrayList<PoketmonBook1>arrayList = new ArrayList<PoketmonBook1>();
		try {
			con = DBUtil.getConnection();
			if(con !=null) {
				System.out.println("데이터베이스 연결완료");
			}else {
				Function.getAlert(1, "데이터 베이스연결오류", "데이터 베이스 연결실패!", "데이터 베이스에 연결확인요망");
			}
			String query = "select * from bookTbl a inner join poketmonTBL b on a.pkmNum = b.pkmNUm";
			ppsm = con.prepareStatement(query);
			rs = ppsm.executeQuery();
			while(rs.next()) {
				
					PoketmonBook1 poketmonBook1 = new PoketmonBook1(rs.getInt(1), rs.getString(6), rs.getString(3), 
							rs.getString(4), rs.getString(5), rs.getString(7), 
							rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), 
							rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17));
					arrayList.add(poketmonBook1);
			}
			
		} catch (Exception e) {
			Function.getAlert(2, "getPoketmonBookLoadList 점검 요망", "getPoketmonBookLoadList 문제 발생", "문제사항"+e.getMessage());
		}finally {
			try {
				if (rs != null)
				rs.close();
				if (ppsm != null)
					ppsm.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("getPoketmonBookLoadList :"+ e.getMessage());
			}
		}
		return arrayList;
	}
}