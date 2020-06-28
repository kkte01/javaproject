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
	// /view/user.fxml 화면
	@FXML
	private Button btnLogin; // 로그인
	@FXML
	private Button btnExit; // 종료
	@FXML
	private Button btnUser; // 회원가입
	@FXML
	private Button btnFind; // ID, PW찾기
	@FXML
	private TextField txtID; // ID 입력 텍스트필드
	@FXML
	private PasswordField pwfPW; // PW 입력 텍스트필드

	private File userImagesFile; // 이미지 저장폴더
	public Stage stage; // 스테이지 연결 스테이지 변수생성
	public File selectFile; // 이미지 선택파일
	private FXMLLoader fxmlLoader = null;
	private Parent view = null;
	private Scene scene = null;
	private User user;
	public static User userLogin = null;
	public ArrayList<User> userList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 회원가입 버튼 이벤트등록 함수 핸들러처리.
		btnUser.setOnAction((e) -> btnUserAction(e));
		// 회원정보 (ID,PW) 찾기 버튼 함수 핸들러처리
		btnFind.setOnAction((e) -> btnFindAction(e));
		// 로그인버튼 이벤트등록 함수 핸들러등록
		btnLogin.setOnAction((e) -> btnLoginAction(e));
		// 종료버튼 이벤트등록
		btnExit.setOnAction((e) -> stage.close());
		// userImage저장 폴더생성하기
		setUserImagePolderAciton();
	}

	// 로그인버튼 이벤트등록 함수 핸들러등록
	private void btnLoginAction(ActionEvent e) {

		if(txtID.getText().trim().equals("")&&pwfPW.getText().trim().equals("")) {
			Function.getAlert(4, "로그인 오류", "ID / PW 를 입력 해주세요","확인후 다시로그인 바랍니다.");
			return;
		}
		
		Connection con =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// ID, PW입력 하지않았을때 경고문구 띄우기
			if (txtID.getText().trim().equals("") && pwfPW.getText().trim().equals("")) {
				Function.getAlert(4, "로그인 오류", "ID 또는 PW 를 입력하지 않았습니다.", "ID/PW 를 확인후 다시 로그인 해주세요!");
			}
			// DBUtil 의 주소값을 가져오기

			con = DBUtil.getConnection();

			String query = "select * from userTBL where userID = ? and userPassword = ?";

			pstmt = con.prepareStatement(query);

			pstmt.setString(1, txtID.getText());
			pstmt.setString(2, pwfPW.getText());

			rs = pstmt.executeQuery();

			if (rs != null && rs.isBeforeFirst()) {
				Function.getAlert(3, "로그인", "로그인 성공", "반갑습니다 트레이너님!");
				fxmlLoader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
				while (rs.next()) {
					userLogin = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getString(7));
				}

				Stage stage = new Stage();
				view = fxmlLoader.load();
				scene = new Scene(view);
				//css스타일 입혀주기
				scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
				stage.setScene(scene);
				MainRootController mainController = fxmlLoader.getController();
				mainController.stage = stage;
				mainController.userInfo = userLogin;
				this.stage.close();
				stage.show();
			} else {
				Function.getAlert(2, "로그인", "로그인 실패", "ID/PW를 확인해주세요");
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

	// userImage저장 폴더생성하기
	private void setUserImagePolderAciton() {
		userImagesFile = new File("C:/userImages");
		if (!userImagesFile.exists()) {
			userImagesFile.mkdir();
		}
	}

	private void btnUserAction(ActionEvent e) {
		try {
			
			
			//view폴더에 화면정보를 가져온다.
			Parent root = FXMLLoader.load(getClass().getResource("/view/user.fxml"));
			//Scene 에 FXMLLoader 를가져오기
			Scene scene = new Scene(root);
			//Stage 스타일정하기
			Stage user = new Stage(StageStyle.UTILITY);
			//스테이지 에 모달인지 모달리스인지 정하기
			user.initModality(Modality.WINDOW_MODAL);
			//스테이지 주인정하기
			user.initOwner(stage);
			
			///view/user.fxml 의 변수들 선언해주기
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
			//스테이지 설정하기
			user.setTitle("회원가입");
			user.centerOnScreen();
			user.setResizable(false);
			user.setScene(scene);
			
			//회원가입 txtField 에 공백시 버튼 비활성화 이벤드틍록
			userID.setOnKeyPressed(e1 -> {
				if(userID.getText().trim().equals("")||userEmail.getText().trim().equals("")||userNickName.getText().trim().equals("")||userPhone.getText().trim().equals("")||userName.getText().trim().equals("")||userPW.getText().trim().equals("") ) {
					btnUserOk.setDisable(true);
				}else {
					btnUserOk.setDisable(false);
				}
			});
			//회원가입 txtField 에 공백시 버튼 비활성화 이벤드틍록
			userEmail.setOnKeyPressed(e2 ->{
				if(userID.getText().trim().equals("")||userEmail.getText().trim().equals("")||userNickName.getText().trim().equals("")||userPhone.getText().trim().equals("")||userName.getText().trim().equals("")||userPW.getText().trim().equals("") ) {
					btnUserOk.setDisable(true);
				}else {
					btnUserOk.setDisable(false);
				}
			});
			//회원가입 txtField 에 공백시 버튼 비활성화 이벤드틍록
			userNickName.setOnKeyPressed(e3 ->{
				if(userID.getText().trim().equals("")||userEmail.getText().trim().equals("")||userNickName.getText().trim().equals("")||userPhone.getText().trim().equals("")||userName.getText().trim().equals("")||userPW.getText().trim().equals("") ) {
					btnUserOk.setDisable(true);
				}else {
					btnUserOk.setDisable(false);
				}
			});
			//회원가입 txtField 에 공백시 버튼 비활성화 이벤드틍록
			userPhone.setOnKeyPressed(e4 -> {
				if(userID.getText().trim().equals("")||userEmail.getText().trim().equals("")||userNickName.getText().trim().equals("")||userPhone.getText().trim().equals("")||userName.getText().trim().equals("")||userPW.getText().trim().equals("") ) {
					btnUserOk.setDisable(true);
				}else {
					btnUserOk.setDisable(false);
				}
			});
			//회원가입 txtField 에 공백시 버튼 비활성화 이벤드틍록
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
			
				
			
			
			//스테이지 보여주기
			user.show();
			
			//이미지 등록버튼 함수생성
			btnImageOk.setOnAction((event-> {
				//파일츄저 객체참조변수 생성
				FileChooser fileChooser = new FileChooser();
				//입력가능한 이미지 파일형식 입력
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
				
				selectFile = fileChooser.showOpenDialog(stage);
				
				if(selectFile!=null) {
					try {
						String localURL = selectFile.toURI().toURL().toString();
						Image image = new Image(localURL, false);
						userImage.setImage(image);
					} catch (Exception e1) {
						Function.getAlert(2, "error", "사진파일을 가져올수 없습니다.", e1.getMessage());
					}
				}
				
			}));
		//회원가입버튼 이벤트 등록
		btnUserOk.setOnAction(event->{

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String fileName = null;
			//저장된 사진이미지에 밀리초를주어서 중복성 없에기
			
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
				Function.getAlert(2, "error", "이미지를 등록해주세요", e2.getMessage());
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
					Function.getAlert(2, "error", "회원가입 실패", "입력정보를 확인하세요!");
				}else {
					Function.getAlert(3, "Hello", "회원가입 성공", "가입 해주셔서 감사합니다.");
					user.close();
				}
				
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
			});
		
		//아이디 중복체크버튼 이벤트등록
		btnCheckID.setOnAction(e2->{
			if(txtID.getText().trim().equals("")) {
				Function.getAlert(4, "error", "아이디를 입력해주세요!", "확인후 재시도 해주세요");
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
					Function.getAlert(2, "ID 중복확인", "중복된 ID 입니다", "다른 ID를 입력해주세요");
				}else {
					Function.getAlert(2, "ID 중복확인", "사용가능한 ID 입니다", "");
				}
				
				
			} catch (Exception e3) {
				Function.getAlert(4, "error", "정보를 입력해주세요", "확인후 다시시도 해주세요.");
			}
		});
		
		//취소버튼 이벤트 등록
		btnUserCencel.setOnAction(e1->user.close());
			
		} catch (Exception e1) {
			Function.getAlert(4, "회원가입 오류", "입력 하지않은 정보가있습니다.", "확인후 다시시도 해주세요.");
		}
		
	}

				
				
	// 회원정보 (ID,PW) 찾기 버튼 함수 핸들러처리
	private void btnFindAction(ActionEvent e) {
		try {
			// view/find.fxml 사용자 정의창 생성
			Parent root = FXMLLoader.load(getClass().getResource("/view/find.fxml"));
			Scene scene = new Scene(root);
			//css스타일 입혀주기
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			Stage find = new Stage(StageStyle.UTILITY);
			find.setScene(scene);
			find.initOwner(stage);

			// view 안의 객채들의 ID를 가져온다
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
						Function.getAlert(4, "ID찾기 오류", "이름 이메일 핸드폰 을 입력하지 않았습니다.", "이름 과 이메일 을 확인후 다시 찾기 해주세요!");
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
						alert.setTitle("PW 찾기");
						alert.setHeaderText("PW :" + user.getUserID());
						alert.setContentText("PW 찾기완료");
						alert.show();

					} else {
						Function.getAlert(4, "ID 찾기", "일치하는 정보가 없습니다", "확인후 다시 시도해주세요");
					}

				} catch (Exception e1) {
					Function.getAlert(3, "Error", "DB 연결 오류", e1.getMessage());
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
						Function.getAlert(4, "PW찾기 오류", "ID 또는 전화번호 입력하지 않았습니다.", "ID 와 전화번호 를 확인후 다시 찾기 해주세요!");
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
						alert.setTitle("PW 찾기");
						alert.setHeaderText("PW :" + user.getUserPassword());
						alert.setContentText("PW 찾기완료");
						alert.show();

					} else {
						Function.getAlert(4, "PW 찾기", "일치하는 정보가 없습니다", "확인후 다시 시도해주세요");
					}
				} catch (Exception e1) {
					Function.getAlert(3, "Error", "DB 연결 오류", e1.getMessage());
				}

			});

			// ID,PW 종료버튼 이벤트등록
			btnFindCancel.setOnAction(event -> find.close());
			btnPWCancel.setOnAction(event -> find.close());

			find.initModality(Modality.WINDOW_MODAL);
			find.setTitle("ID, PW 찾기");
			find.setResizable(false);
			find.centerOnScreen();
			find.show();

		} catch (IOException e1) {

		}
	}
}
