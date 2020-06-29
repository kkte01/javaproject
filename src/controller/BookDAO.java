package controller;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Notice;
import model.PoketmonBook1;
import model.User;

public class BookDAO {
	ImageView imgView= new ImageView();
	// DB�� �ִ� ���� �������� �Լ�
	public ArrayList<PoketmonBook1> getTotalLoadList() {
		// DB ����
		Connection con = null;
		PreparedStatement ppsm = null;
		ResultSet rs = null;
		ArrayList<PoketmonBook1> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if(con !=null) {
				System.out.println("�����ͺ��̽� ����Ϸ�");
			}else {
				Function.getAlert(1, "������ ���̽��������", "������ ���̽� �������!", "������ ���̽��� ����Ȯ�ο��");
			}
			String query = "select * from bookTbl";
			ppsm = con.prepareStatement(query);
			rs = ppsm.executeQuery();
			// ArrayList�� �߰��ϱ�
			arrayList = new ArrayList<PoketmonBook1>();
			imgView.setFitHeight(100);
			imgView.setFitWidth(150);
			//imgView.preserveRatioProperty();
			while (rs.next()) {
				imgView = new ImageView(new Image("file:/C:/icons/"+rs.getString(2)));
				PoketmonBook1 pkmBook1 = new PoketmonBook1(rs.getInt(1), imgView,
						rs.getString(3), rs.getString(4),
						rs.getString(5));
				arrayList.add(pkmBook1);
			}
		} catch (Exception e1) {	
			Function.getAlert(2, "TotalLoadList ���� ���", "TotalLoadList ���� �߻�", "��������"+e1.getMessage());
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
	//���̺� ������ ���� �������� �������� �Լ�
	public ArrayList<PoketmonBook1> getRandomJoinLoadList(){
		Connection con = null;
		PreparedStatement ppsm = null;
		ResultSet rs = null;
		ArrayList<PoketmonBook1> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if(con !=null) {
				System.out.println("�����ͺ��̽� ����Ϸ�");
			}else {
				Function.getAlert(1, "������ ���̽��������", "������ ���̽� �������!", "������ ���̽��� ����Ȯ�ο��");
			}
			String query = "select * from bookTbl a inner join poketmonTBL b on a.pkmNum = b.pkmNUm";
			ppsm = con.prepareStatement(query);
			rs = ppsm.executeQuery();
			//�� ��üȭ ��Ű��
			arrayList = new ArrayList<PoketmonBook1>();
			
			while(rs.next()) {
				PoketmonBook1 poketmonBook1 = 	new PoketmonBook1(rs.getString(6),rs.getString(3),
						rs.getString(8),rs.getString(9),rs.getString(10),
						rs.getString(11),rs.getString(12),rs.getString(13));
				arrayList.add(poketmonBook1);
			}
			
		} catch (Exception e) {
			Function.getAlert(2, "getRandomJoinLoadList ���� ���", "getRandomJoinLoadList ���� �߻�", "��������"+e.getMessage());
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
	//����Ŭ���� ������ ǥ���� ���� �������� �Լ�
	public ArrayList<PoketmonBook1> getPoketmonBookLoadList(){
		Connection con = null;
		PreparedStatement ppsm = null;
		ResultSet rs = null;
		ArrayList<PoketmonBook1>arrayList = new ArrayList<PoketmonBook1>();
		try {
			con = DBUtil.getConnection();
			if(con !=null) {
				System.out.println("�����ͺ��̽� ����Ϸ�");
			}else {
				Function.getAlert(1, "������ ���̽��������", "������ ���̽� �������!", "������ ���̽��� ����Ȯ�ο��");
			}
			String query = "select * from bookTbl a inner join poketmonTBL b on a.pkmNum = b.pkmNUm";
			ppsm = con.prepareStatement(query);
			rs = ppsm.executeQuery();
			while(rs.next()) {
				
					PoketmonBook1 poketmonBook1 = new PoketmonBook1(rs.getInt(1), rs.getString(6), rs.getString(3), 
							rs.getString(4), rs.getString(5), rs.getString(8), 
							rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), 
							rs.getString(15), rs.getString(16), rs.getString(17), rs.getString(18));
					arrayList.add(poketmonBook1);
			}
			
		} catch (Exception e) {
			Function.getAlert(2, "getPoketmonBookLoadList ���� ���", "getPoketmonBookLoadList ���� �߻�", "��������"+e.getMessage());
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
	//DB�� �ִ� �������� ���� �������� �Լ�
	public ArrayList<Notice> getNoticeLoadList(){
		Connection con = null;
		PreparedStatement ppsm = null;
		ResultSet rs = null;
		ArrayList<Notice> arrayList = new ArrayList<Notice>();
		try {
			con = DBUtil.getConnection();
			// ������ ������ ����
			String query = "SELECT notice FROM noticeTBL";
			// �������� ������ �غ�
			ppsm = con.prepareStatement(query);
			// ������� �޴´�.
			rs = ppsm.executeQuery();
			// �װ��� ArrayList�� �̿��� �޴´�.
			while (rs.next()) {
				Notice n = new Notice(rs.getString(1));
				arrayList.add(n);
			}
		} catch (Exception e1) {
			Function.getAlert(2, "�������׿���", "���������� ���� �Է¿��", e1.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ppsm != null)
					ppsm.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return arrayList;
	}
	//DB�� �ִ� �������� ���� �����ϴ� �Լ�
	public int changeNoticeList(TextField textField) {
		Connection con = null;
		PreparedStatement ppsm = null;
		int value = 0;
		try {
		if(!(textField.getText().trim().equals(""))) {
			con = DBUtil.getConnection();
			String query = "update noticeTBL set notice = ?";
			// ������ ������ �غ�
			ppsm = con.prepareStatement(query);
			// �� ��ġ��Ű��
			ppsm.setString(1, textField.getText());
			value = ppsm.executeUpdate();
			}else {
				throw new Exception();
			}
		} catch (Exception e1) {
			Function.getAlert(2, "���� ����", "���� Ȯ�� ���", "������ Ȯ�����ּ���");
		} finally {
			try {
				if (ppsm != null)
					ppsm.close();
				if (con != null)
					con.close();
			} catch (SQLException e1) {
			}
		}
		return value;
	}
}