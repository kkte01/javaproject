package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureClassLoader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.text.View;
import java.sql.Connection;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;
import model.User;

public class RootController implements Initializable {
	// /view/user.fxml ȭ��
	@FXML
	private Button btnLogin; // �α���
	@FXML
	private Button btnExit; // ����
	@FXML
	private Button btnUser; // ȸ������
	@FXML
	private Button btnFind; // ID, PWã��
	@FXML
	private TextField txtID; // ID �Է� �ؽ�Ʈ�ʵ�
	@FXML
	private PasswordField pwfPW; // PW �Է� �ؽ�Ʈ�ʵ�

	private File userImagesFile; // �̹��� ��������
	public Stage stage; // �������� ���� �������� ��������
	public File selectFile; // �̹��� ��������
	private FXMLLoader fxmlLoader = null;
	private Parent view = null;
	private Scene scene = null;
	private User user;
	public static User userLogin = null;
	public ArrayList<User> userList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// ȸ������ ��ư �̺�Ʈ��� �Լ� �ڵ鷯ó��.
		btnUser.setOnAction((e) -> btnUserAction(e));
		// ȸ������ (ID,PW) ã�� ��ư �Լ� �ڵ鷯ó��
		btnFind.setOnAction((e) -> btnFindAction(e));
		// �α��ι�ư �̺�Ʈ��� �Լ� �ڵ鷯���
		btnLogin.setOnAction((e) -> btnLoginAction(e));
		// �����ư �̺�Ʈ���
		btnExit.setOnAction((e) -> stage.close());
		// userImage���� ���������ϱ�
		setUserImagePolderAciton();
	}

	// �α��ι�ư �̺�Ʈ��� �Լ� �ڵ鷯���
	private void btnLoginAction(ActionEvent e) {

		if(txtID.getText().trim().equals("")&&pwfPW.getText().trim().equals("")) {
			Function.getAlert(4, "�α��� ����", "ID / PW �� �Է� ���ּ���","Ȯ���� �ٽ÷α��� �ٶ��ϴ�.");
			return;
		}
		
		Connection con =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// ID, PW�Է� �����ʾ����� ����� ����
			if (txtID.getText().trim().equals("") && pwfPW.getText().trim().equals("")) {
				Function.getAlert(4, "�α��� ����", "ID �Ǵ� PW �� �Է����� �ʾҽ��ϴ�.", "ID/PW �� Ȯ���� �ٽ� �α��� ���ּ���!");
			}
			// DBUtil �� �ּҰ��� ��������

			con = DBUtil.getConnection();

			String query = "select * from userTBL where userID = ? and userPassword = ?";

			pstmt = con.prepareStatement(query);

			pstmt.setString(1, txtID.getText());
			pstmt.setString(2, pwfPW.getText());

			rs = pstmt.executeQuery();

			if (rs != null && rs.isBeforeFirst()) {
				Function.getAlert(3, "�α���", "�α��� ����", "�ݰ����ϴ� Ʈ���̳ʴ�!");
				fxmlLoader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
				while (rs.next()) {
					userLogin = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getString(7));
				}

				Stage stage = new Stage();
				view = fxmlLoader.load();
				scene = new Scene(view);
				//css��Ÿ�� �����ֱ�
				scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
				stage.setScene(scene);
				MainRootController mainController = fxmlLoader.getController();
				mainController.stage = stage;
				mainController.userInfo = userLogin;
				this.stage.close();
				stage.show();
			} else {
				Function.getAlert(2, "�α���", "�α��� ����", "ID/PW�� Ȯ�����ּ���");
			}

		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e2) {
				System.out.println("RootController.totalLoadList: " + e2.getMessage());
			}
		}
	}

	// userImage���� ���������ϱ�
	private void setUserImagePolderAciton() {
		userImagesFile = new File("C:/userImages");
		if (!userImagesFile.exists()) {
			userImagesFile.mkdir();
		}
	}

	private void btnUserAction(ActionEvent e) {
		try {
			
			
			//view������ ȭ�������� �����´�.
			Parent root = FXMLLoader.load(getClass().getResource("/view/user.fxml"));
			//Scene �� FXMLLoader ����������
			Scene scene = new Scene(root);
			//Stage ��Ÿ�����ϱ�
			Stage user = new Stage(StageStyle.UTILITY);
			//�������� �� ������� ��޸������� ���ϱ�
			user.initModality(Modality.WINDOW_MODAL);
			//�������� �������ϱ�
			user.initOwner(stage);
			
			///view/user.fxml �� ������ �������ֱ�
			TextField userID=(TextField) root.lookup("#txtUserID");
			TextField userEmail=(TextField) root.lookup("#txtUserEmail");
			TextField userNickName=(TextField) root.lookup("#txtUserNickName");
			TextField userPhone=(TextField) root.lookup("#txtUserPhone");
			TextField userName=(TextField) root.lookup("#txtUserName");
			PasswordField userPW=(PasswordField) root.lookup("#pwUserPW");
			
			ImageView userImage = (ImageView) root.lookup("#imgUserImage");
			
			Button btnUserOk = (Button) root.lookup("#btnUserOk");
			Button btnUserCencel = (Button) root.lookup("#btnUserCencel");
			Button btnImageOk = (Button) root.lookup("#btnImageOk");
			Button btnCheckID = (Button) root.lookup("#btnCheckID");
			
			btnUserOk.setDisable(true);
			//�������� �����ϱ�
			user.setTitle("ȸ������");
			user.centerOnScreen();
			user.setResizable(false);
			user.setScene(scene);
			
			//ȸ������ txtField �� ����� ��ư ��Ȱ��ȭ �̺���v��
			userID.setOnKeyPressed(e1 -> {
				if(userID.getText().trim().equals("")||userEmail.getText().trim().equals("")||userNickName.getText().trim().equals("")||userPhone.getText().trim().equals("")||userName.getText().trim().equals("")||userPW.getText().trim().equals("") ) {
					btnUserOk.setDisable(true);
				}else {
					btnUserOk.setDisable(false);
				}
			});
			//ȸ������ txtField �� ����� ��ư ��Ȱ��ȭ �̺���v��
			userEmail.setOnKeyPressed(e2 ->{
				if(userID.getText().trim().equals("")||userEmail.getText().trim().equals("")||userNickName.getText().trim().equals("")||userPhone.getText().trim().equals("")||userName.getText().trim().equals("")||userPW.getText().trim().equals("") ) {
					btnUserOk.setDisable(true);
				}else {
					btnUserOk.setDisable(false);
				}
			});
			//ȸ������ txtField �� ����� ��ư ��Ȱ��ȭ �̺���v��
			userNickName.setOnKeyPressed(e3 ->{
				if(userID.getText().trim().equals("")||userEmail.getText().trim().equals("")||userNickName.getText().trim().equals("")||userPhone.getText().trim().equals("")||userName.getText().trim().equals("")||userPW.getText().trim().equals("") ) {
					btnUserOk.setDisable(true);
				}else {
					btnUserOk.setDisable(false);
				}
			});
			//ȸ������ txtField �� ����� ��ư ��Ȱ��ȭ �̺���v��
			userPhone.setOnKeyPressed(e4 -> {
				if(userID.getText().trim().equals("")||userEmail.getText().trim().equals("")||userNickName.getText().trim().equals("")||userPhone.getText().trim().equals("")||userName.getText().trim().equals("")||userPW.getText().trim().equals("") ) {
					btnUserOk.setDisable(true);
				}else {
					btnUserOk.setDisable(false);
				}
			});
			//ȸ������ txtField �� ����� ��ư ��Ȱ��ȭ �̺���v��
			userName.setOnKeyPressed(e5 -> {
				if(userID.getText().trim().equals("")||userEmail.getText().trim().equals("")||userNickName.getText().trim().equals("")||userPhone.getText().trim().equals("")||userName.getText().trim().equals("")||userPW.getText().trim().equals("") ) {
					btnUserOk.setDisable(true);
				}else {
					btnUserOk.setDisable(false);
				}
			});
			userPW.setOnKeyPressed(e6 ->{
				if(userID.getText().trim().equals("")||userEmail.getText().trim().equals("")||userNickName.getText().trim().equals("")||userPhone.getText().trim().equals("")||userName.getText().trim().equals("")||userPW.getText().trim().equals("") ) {
					btnUserOk.setDisable(true);
				}else {
					btnUserOk.setDisable(false);
				}
			});
			
				
			
			
			//�������� �����ֱ�
			user.show();
			
			//�̹��� ��Ϲ�ư �Լ�����
			btnImageOk.setOnAction((event-> {
				//�������� ��ü�������� ����
				FileChooser fileChooser = new FileChooser();
				//�Է°����� �̹��� �������� �Է�
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
				
				selectFile = fileChooser.showOpenDialog(stage);
				
				if(selectFile!=null) {
					try {
						String localURL = selectFile.toURI().toURL().toString();
						Image image = new Image(localURL, false);
						userImage.setImage(image);
					} catch (Exception e1) {
						Function.getAlert(2, "error", "���������� �����ü� �����ϴ�.", e1.getMessage());
					}
				}
				
			}));
		//ȸ�����Թ�ư �̺�Ʈ ���
		btnUserOk.setOnAction(event->{

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String fileName = null;
			//����� �����̹����� �и��ʸ��־ �ߺ��� ������
			
			try {
			fileName = "user"+System.currentTimeMillis()+selectFile.getName();
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			
				bis = new BufferedInputStream(new FileInputStream(selectFile));
				bos = new BufferedOutputStream(new FileOutputStream(userImagesFile.getAbsolutePath() + "\\" + fileName));
				int data = -1;
				while((data = bis.read()) != -1) {
					bos.write(data);
					bos.flush();
				}
			} catch (Exception e2) {
				Function.getAlert(2, "error", "�̹����� ������ּ���", e2.getMessage());
			}
			try {
				con = DBUtil.getConnection();
				
				
				String query = "insert into userTBL values(?,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(query);
				

				pstmt.setString(1, userID.getText());
				pstmt.setString(2, userPW.getText());
				pstmt.setString(3, userName.getText());
				pstmt.setString(4, userEmail.getText());
				pstmt.setString(5, userPhone.getText());
				pstmt.setString(6, userNickName.getText());
				pstmt.setString(7, fileName);
				
				int returnValue= pstmt.executeUpdate();
				if(returnValue == 0) {
					Function.getAlert(2, "error", "ȸ������ ����", "�Է������� Ȯ���ϼ���!");
				}else {
					Function.getAlert(3, "Hello", "ȸ������ ����", "���� ���ּż� �����մϴ�.");
					user.close();
				}
				
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
			});
		
		//���̵� �ߺ�üũ��ư �̺�Ʈ���
		btnCheckID.setOnAction(e2->{
			if(txtID.getText().trim().equals("")) {
				Function.getAlert(4, "error", "���̵� �Է����ּ���!", "Ȯ���� ��õ� ���ּ���");
				return;
			}
		
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				con = DBUtil.getConnection();
				String query = "select userID from userTBL where userID = ?";
				pstmt =con.prepareStatement(query);
				pstmt.setString(1, userID.getText());
				rs = pstmt.executeQuery();
				
				if(rs!=null && rs.isBeforeFirst()) {
					Function.getAlert(2, "ID �ߺ�Ȯ��", "�ߺ��� ID �Դϴ�", "�ٸ� ID�� �Է����ּ���");
				}else {
					Function.getAlert(2, "ID �ߺ�Ȯ��", "��밡���� ID �Դϴ�", "");
				}
				
				
			} catch (Exception e3) {
				Function.getAlert(4, "error", "������ �Է����ּ���", "Ȯ���� �ٽýõ� ���ּ���.");
			}
		});
		
		//��ҹ�ư �̺�Ʈ ���
		btnUserCencel.setOnAction(e1->user.close());
			
		} catch (Exception e1) {
			Function.getAlert(4, "ȸ������ ����", "�Է� �������� �������ֽ��ϴ�.", "Ȯ���� �ٽýõ� ���ּ���.");
		}
		
	}

				
				
	// ȸ������ (ID,PW) ã�� ��ư �Լ� �ڵ鷯ó��
	private void btnFindAction(ActionEvent e) {
		try {
			// view/find.fxml ����� ����â ����
			Parent root = FXMLLoader.load(getClass().getResource("/view/find.fxml"));
			Scene scene = new Scene(root);
			//css��Ÿ�� �����ֱ�
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			Stage find = new Stage(StageStyle.UTILITY);
			find.setScene(scene);
			find.initOwner(stage);

			// view ���� ��ä���� ID�� �����´�
			TextField IDName = (TextField) root.lookup("#txtIDName");
			TextField IDEmail = (TextField) root.lookup("#txtIDEmail");
			TextField IDPhone = (TextField) root.lookup("#txtIDPhone");

			TextField pwID = (TextField) root.lookup("#txtPWID");
			TextField pwPhone = (TextField) root.lookup("#txtPWPhone");

			Button btnFindOk = (Button) root.lookup("#btnFindOk");
			Button btnFindCancel = (Button) root.lookup("#btnFindCancel");
			Button btnPWOK = (Button) root.lookup("#btnPWOK");
			Button btnPWCancel = (Button) root.lookup("#btnPWCancel");

			btnFindOk.setOnAction(event -> {
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;

				try {
					if(IDName.getText().trim().equals("")&&IDEmail.getText().trim().equals("")) {
						Function.getAlert(4, "IDã�� ����", "�̸� �̸��� �ڵ��� �� �Է����� �ʾҽ��ϴ�.", "�̸� �� �̸��� �� Ȯ���� �ٽ� ã�� ���ּ���!");
						return;
					}
					con = DBUtil.getConnection();

					String query = "select * from userTBL where userName = ? and userEmail = ? and userPhone = ?";
					pstmt = con.prepareStatement(query);

					pstmt.setString(1, IDName.getText());
					pstmt.setString(2, IDEmail.getText());
					pstmt.setString(3, IDPhone.getText());

					rs = pstmt.executeQuery();

					while (rs.next()) {
						user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
					}

					if (user != null) {

						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("PW ã��");
						alert.setHeaderText("PW :" + user.getUserID());
						alert.setContentText("PW ã��Ϸ�");
						alert.show();

					} else {
						Function.getAlert(4, "ID ã��", "��ġ�ϴ� ������ �����ϴ�", "Ȯ���� �ٽ� �õ����ּ���");
					}

				} catch (Exception e1) {
					Function.getAlert(3, "Error", "DB ���� ����", e1.getMessage());
				} finally {
					try {
						if (rs != null)
							rs.close();
						if (pstmt != null)
							pstmt.close();
						if (con != null)
							con.close();
					} catch (SQLException e2) {
						System.out.println("RootController.totalLoadList: " + e2.getMessage());
					}
				}

			});

			btnPWOK.setOnAction(event -> {
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;

				try {
					con = DBUtil.getConnection();

					if (pwID.getText().trim().equals("") && pwPhone.getText().trim().equals("")) {
						Function.getAlert(4, "PWã�� ����", "ID �Ǵ� ��ȭ��ȣ �Է����� �ʾҽ��ϴ�.", "ID �� ��ȭ��ȣ �� Ȯ���� �ٽ� ã�� ���ּ���!");
						return;
					}

					System.out.println("1");

					String query = "select * from userTBL where userID = ? and userPhone = ?";
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, pwID.getText());
					pstmt.setString(2, pwPhone.getText());

					rs = pstmt.executeQuery();

					while (rs.next()) {
						user = new User(rs.getString(1), rs.getString(2), rs.getString(3));
					}

					if (user != null) {

						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("PW ã��");
						alert.setHeaderText("PW :" + user.getUserPassword());
						alert.setContentText("PW ã��Ϸ�");
						alert.show();

					} else {
						Function.getAlert(4, "PW ã��", "��ġ�ϴ� ������ �����ϴ�", "Ȯ���� �ٽ� �õ����ּ���");
					}
				} catch (Exception e1) {
					Function.getAlert(3, "Error", "DB ���� ����", e1.getMessage());
				}

			});

			// ID,PW �����ư �̺�Ʈ���
			btnFindCancel.setOnAction(event -> find.close());
			btnPWCancel.setOnAction(event -> find.close());

			find.initModality(Modality.WINDOW_MODAL);
			find.setTitle("ID, PW ã��");
			find.setResizable(false);
			find.centerOnScreen();
			find.show();

		} catch (IOException e1) {

		}
	}
}
