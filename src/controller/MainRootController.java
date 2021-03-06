package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.util.Duration;
import model.Notice;
import model.PoketmonBook1;
import model.User;

public class MainRootController implements Initializable {
	// fx:id 매치 시키기
	@FXML
	private ImageView imgTrainer;
	@FXML
	private ImageView imgPoket;
	@FXML
	private Label labelTrainer;
	@FXML
	private Button btnRandomPoket;
	@FXML
	private Button btnChangeName;
	@FXML
	private Button btnChangeImage;
	@FXML
	private Label labelNotice;
	@FXML
	private Button btnClose;
	@FXML
	private Button btnBook;
	@FXML
	private Button btnMyPoket;
	@FXML
	private Button btnGlow;
	@FXML
	private Button btnToday;
	@FXML
	private BarChart mainXYchart;
	@FXML
	private Label lblMonName;
	public Stage stage;
	public ObservableList<Notice> obsNotiList = FXCollections.observableArrayList();
	public ObservableList<PoketmonBook1> obsPkmiList = FXCollections.observableArrayList();
	public Media media;
	public MediaPlayer mp;
	int i;
	public File saveIcons;
	public File selectFile;
	public File userImagesFile;
	public User userInfo;
	private PoketmonBook1 pkmBook1;
	public int tableViewIndex;
	public File iconSelectFile2;
	public File poketSelectFile3;
	private File poketmonImagesFile;
	private MonsterEXP exp = new MonsterEXP();
	private PoketmonBook1 glownPoketmon;
	boolean evolveFlag = true;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// 이벤트 등록하기
		// 유저 이미지를 저장하는 폴더 생성 함수
		setUserImage();
		// 화면에 유저닉네임 사진 보여주는 함수
		loadUserInfomation();
		// DB에 있는 내용을 가져오기
		totalLoadList();
		// 라벨을 움직이는 이벤트 등록
		labelMoveAction();
		// 데이터베이스에 있는 공지사항을 보여주는 함수
		showNotice();
		// 라벨을 눌렀을경우 이벤트 등록 및 핸들러 함수 처리
		labelNotice.setOnMouseClicked(e -> handlelblNoticeAction(e));
		// 포켓몬영상을 눌렀을 경우에 대한 이벤트
		btnToday.setOnAction(e -> handleBtnTodayAction(e));
		// 포켓몬 도감을 눌렀을 경우에 대한 이벤트
		btnBook.setOnAction(e -> handleBtnBookAction(e));
		// 아이콘을 저장하는 폴더 만들기("C:\icons")
		setSaveIcons();
		// 화면에 랜덤 포켓몬을 보여주는 이벤트 등록 및 핸들러 함수처리
		btnRandomPoket.setOnAction(e -> loadRamdomPhoto(e));
		// 대표이미지 버튼 이벤트 등록 및 핸들러 함수처리
		btnChangeImage.setOnAction(e -> handleBtnChageImage(e));
		// 트레이너 이름 변경 버튼 이벤트 등록 및 핸들러 함수처리
		btnChangeName.setOnAction(e -> handleBtnChageName(e));
		// 종료버튼에 대한 이벤트 처리
		btnClose.setOnAction(e -> stage.close());
		// 나만의포켓몬 을 눌럿을 경우에 대한 이벤트
		btnMyPoket.setOnAction(e -> handleBtnMyPoketAction(e));
		// 포켓몬 이미지를 저장하는 함수
		setPoketmonImagePolderAciton();
		// 육성 버튼에 대한 이벤트 등록 및 핸들러 함수 처리
		btnGlow.setOnAction(e -> handleBtnGlow(e));
	}

	// 포켓몬이미지를 저장폴더 생성 함수
	private void setPoketmonImagePolderAciton() {
		poketmonImagesFile = new File("C:/poketmon");
		if (!poketmonImagesFile.exists()) {
			poketmonImagesFile.mkdir();
		}

	}
	
	// 나만의 포켓몬등록에 관한 함수
	private void handleBtnMyPoketAction(ActionEvent e) {
		try {
			// view 의 mypoketmon 을 사용자정의창 으로 실행하기
			Parent root = FXMLLoader.load(getClass().getResource("/view/mypoketmon.fxml"));
			Scene scene = new Scene(root);
			// css스타일 입혀주기
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			// 스테이지 스타일 정하기
			Stage myPoketmon = new Stage(StageStyle.UTILITY);
			// 스테이지에 scene 셋팅하기
			myPoketmon.setScene(scene);
			// 주종관계 정하기
			myPoketmon.initOwner(this.stage);

			// view의 등록된 것들 가져오기
			ImageView imagebig = (ImageView) root.lookup("#imgMyPkmImage1");
			ImageView imageicon = (ImageView) root.lookup("#imgMyPkmImage2");
			
			TextField MyPkmName = (TextField) root.lookup("#txtMyPkmName");
			TextField MyPkmSAttack = (TextField) root.lookup("#txtMyPkmSAttack");
			TextField MyPkmSDefense = (TextField) root.lookup("#txtMyPkmSDefense");
			TextField MyPkmDefense = (TextField) root.lookup("#txtMyPkmDefense");
			TextField MyPkmAttack = (TextField) root.lookup("#txtMyPkmAttack");
			TextField MyPkmType1 = (TextField) root.lookup("#txtMyPkmType1");
			TextField MyPkmType2 = (TextField) root.lookup("#txtMyPkmType2");
			ComboBox MyPkmEvolve = (ComboBox) root.lookup("#txtMyPkmEvolve");
			TextField MyPkmTrait = (TextField) root.lookup("#txtMyPkmTrait");
			TextField MyPkmSpeed = (TextField) root.lookup("#txtMyPkmSpeed");
			TextField MyPkmHP = (TextField) root.lookup("#txtMyPkmHP");
			TextField MyPkmHeight = (TextField) root.lookup("#txtMyPkmHeight");
			TextField MyPkmWeight = (TextField) root.lookup("#txtMyPkmWeight");
			TextArea MyPkmInfo = (TextArea) root.lookup("#txaMyPkmInfo");

			Button btnMyPkJoin = (Button) root.lookup("#btnMyPkJoin");
			Button btnMyPkcencle = (Button) root.lookup("#btnMyPkcencle");
			Button btnMyPkImage = (Button) root.lookup("#btnMyPkImage");
			Button btnMyPkIcon = (Button) root.lookup("#btnMyPkIcon");
			
			myPoketmon.initModality(Modality.WINDOW_MODAL);
			myPoketmon.setTitle("나만의 포켓몬 등록");
			myPoketmon.show();
			//콤보박스 초기화
			ObservableList<String> evolvedList = FXCollections.observableArrayList();
			evolvedList.addAll("O","X");
			MyPkmEvolve.setItems(evolvedList);
			// 포켓몬 이미지 등록버튼 이벤트설정
			btnMyPkImage.setOnAction((event -> {
				// 파일츄저 객체참조변수 생성
				FileChooser fileChooser = new FileChooser();
				// 입력가능한 이미지 파일형식 입력
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));

				poketSelectFile3 = fileChooser.showOpenDialog(stage);

				if (poketSelectFile3 != null) {
					try {
						String localURL = poketSelectFile3.toURI().toURL().toString();
						Image image = new Image(localURL, false);
						imagebig.setImage(image);
					} catch (Exception e1) {
					}
				}

			}));
			// 포켓몬 아이콘 등록 이벤트 설정
			btnMyPkIcon.setOnAction((event -> {
				// 파일츄저 객체참조변수 생성
				FileChooser fileChooser = new FileChooser();
				// 입력가능한 이미지 파일형식 입력
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));

				iconSelectFile2 = fileChooser.showOpenDialog(stage);

				if (iconSelectFile2 != null) {
					try {
						String localURL = iconSelectFile2.toURI().toURL().toString();
						Image image = new Image(localURL, false);
						imageicon.setImage(image);
					} catch (Exception e1) {
						Function.getAlert(2, "error", "사진파일을 가져올수 없습니다.", e1.getMessage());
					}
				}

			}));

			btnMyPkcencle.setOnAction(e7 -> myPoketmon.close());

			// 각 txtFile에 빈칸이있으면 return null 을 반환해줘서 밑에 있는 등록버튼을 무효화시킴

			// 나만의 포켓몬 창의 정보저장 버튼 이벤트 등록

			btnMyPkJoin.setOnAction(e2 -> {

				try {
				if (MyPkmName.getText().trim().equals("") || MyPkmSAttack.getText().trim().equals("")
						|| MyPkmSDefense.getText().trim().equals("") || MyPkmDefense.getText().trim().equals("")
						|| MyPkmAttack.getText().trim().equals("") || MyPkmType1.getText().trim().equals("")
						|| MyPkmType2.getText().trim().equals("") || MyPkmEvolve.getSelectionModel().getSelectedItem().equals("")
						|| MyPkmTrait.getText().trim().equals("") || MyPkmSpeed.getText().trim().equals("")
						|| MyPkmHP.getText().trim().equals("") || MyPkmHeight.getText().trim().equals("")
						|| MyPkmWeight.getText().trim().equals("") || MyPkmInfo.getText().trim().equals("")) {
					Function.getAlert(4, "error", "포켓몬 정보를 빠짐없이 등록해주세요!", "입력후 다시 등록해주세요");
					return;
				}
				}catch(Exception e1) {
						Function.getAlert(4, "error", "포켓몬 정보를 빠짐없이 등록해주세요!", "입력후 다시 등록해주세요");
						return;
				}
				Connection con = null;
				PreparedStatement pstmt = null;
				String poketFileName = null;
				String iconFileName = null;

				// 파일을 밀리초로 바꾸어서 폴더에 저장하는 함수
				try {
					poketFileName = "user" + System.currentTimeMillis() + poketSelectFile3.getName();
					BufferedInputStream bis = null;
					BufferedOutputStream bos = null;

					bis = new BufferedInputStream(new FileInputStream(poketSelectFile3));
					bos = new BufferedOutputStream(
							new FileOutputStream(poketmonImagesFile.getAbsolutePath() + "\\" + poketFileName));
					int data = -1;
					while ((data = bis.read()) != -1) {
						bos.write(data);
						bos.flush();
					}
				} catch (Exception e6) {
					Function.getAlert(4, "error", "이미지를 등록해주세요!", "error code :" + e6.getMessage());
					return;
				}

				try {
					iconFileName = "user" + System.currentTimeMillis() + iconSelectFile2.getName();
					BufferedInputStream bis = null;
					BufferedOutputStream bos = null;

					bis = new BufferedInputStream(new FileInputStream(iconSelectFile2));
					bos = new BufferedOutputStream(
							new FileOutputStream(saveIcons.getAbsolutePath() + "\\" + iconFileName));
					int data = -1;
					while ((data = bis.read()) != -1) {
						bos.write(data);
						bos.flush();
					}
				} catch (Exception e6) {
					Function.getAlert(4, "error", "이미지를 등록해주세요!", "error code :" + e6.getMessage());
					return;
				}

				try {
					con = DBUtil.getConnection();
					String query = "INSERT INTO bookTbl values(null,?,?,?,?,?)";
					pstmt = con.prepareStatement(query);
					// bookTBL 에 입력 정보값넣기
					pstmt.setString(1, iconFileName);
					pstmt.setString(2, MyPkmName.getText());
					pstmt.setString(3, MyPkmType1.getText());
					pstmt.setString(4, MyPkmType2.getText());
					pstmt.setString(5, poketFileName);

					int returnValue = pstmt.executeUpdate();

				} catch (Exception e1) {
				}

				Connection con2 = null;
				PreparedStatement pstmt2 = null;

				try {
					con = DBUtil.getConnection();
					String query = "INSERT INTO poketmonTBL values(null,?,?,?,?,?,?,?,?,?,?,?)";
					pstmt = con.prepareStatement(query);

					// PoketmonTBL 에 입력 정보값넣기
					pstmt.setString(1, MyPkmHP.getText());
					pstmt.setString(2, MyPkmAttack.getText());
					pstmt.setString(3, MyPkmDefense.getText());
					pstmt.setString(4, MyPkmSAttack.getText());
					pstmt.setString(5, MyPkmSDefense.getText());
					pstmt.setString(6, MyPkmSpeed.getText());
					pstmt.setString(7, MyPkmTrait.getText());
					pstmt.setString(8, MyPkmHeight.getText() + "m");
					pstmt.setString(9, MyPkmWeight.getText() + "kg");
					pstmt.setString(10, MyPkmEvolve.getSelectionModel().getSelectedItem().toString());
					pstmt.setString(11, MyPkmInfo.getText());

					int returnValue = pstmt.executeUpdate();

					if (returnValue == 0) {
					} else {
						Function.getAlert(3, "Success", "나만의포켓몬 등록 성공", "도감에서 나만의 포켓몬을 확인해보세요.");
						myPoketmon.close();
					}

				} catch (Exception e1) {
					Function.getAlert(4, "error", "2번 DB 연결실패!", "error code :" + e1.getMessage());
				}

			});

		} catch (IOException e1) {
			Function.getAlert(4, "error", "나만의 포켓몬창 불러오기 오류", e1.getMessage());
		}

	}

	// 유저 이미지를 저장하는 폴더 생성 함수
	private void setUserImage() {
		userImagesFile = new File("C:/userImages");
		if (!userImagesFile.exists()) {
			userImagesFile.mkdir();
		}

	}

	// 화면에 유저닉네임 사진 보여주는 함수
	private void loadUserInfomation() {
		labelTrainer.setText(RootController.userLogin.getUserNickName());
		imgTrainer.setImage(new Image("file:/C:/userimages/" + RootController.userLogin.getUserImage()));
	}

	// DB에 있는 값 가져오는 리스트
	private void totalLoadList() {
		BookDAO b = new BookDAO();
		ArrayList<PoketmonBook1> arrayList = b.getTotalLoadList();
		if (arrayList == null) {
			return;
		}
		for (int i = 0; i < arrayList.size(); i++) {
			PoketmonBook1 p1 = arrayList.get(i);
			obsPkmiList.add(p1);
		}

	}

	// 아이콘이 저장되는 위치 생성 함수
	public void setSaveIcons() {
		saveIcons = new File("C:/icons");
		if (!saveIcons.exists()) {
			// 없으면 만드는 함수
			saveIcons.mkdir();
			System.out.println("C:/icons 디렉토리 생성 성공");
		}

	}

	// 라벨을 클릭했을 경우 핸들러
	private void handlelblNoticeAction(MouseEvent e) {
		// 스타일 결정
		Stage noti = new Stage(StageStyle.UTILITY);
		// 모달과 모달리스 설정
		noti.initModality(Modality.WINDOW_MODAL);
		// 주종관계 설정
		noti.initOwner(stage);
		Parent p = null;
		try {
			p = FXMLLoader.load(getClass().getResource("/view/cmdpassword.fxml"));
		} catch (IOException e1) {
		}
		// 이벤트 등록을 위한 객체를 가져온다.
		TextField pwField1 = (TextField) p.lookup("#pwField1");
		Button btnCmdLogin = (Button) p.lookup("#btnCmdLogin");
		Button btnCmdClose = (Button) p.lookup("#btnCmdClose");
		// 이벤트 등록
		// 취소 버튼 이벤트
		btnCmdClose.setOnAction(event -> noti.close());
		// 로그인 버튼 이벤트
		btnCmdLogin.setOnAction(event -> {
			if (pwField1.getText().equals("123456")) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("로그인 성공");
				alert.setHeaderText("관리자님 환영합니다.");
				alert.setContentText("d ^-^ b");
				alert.showAndWait();
				Stage cmd = new Stage(StageStyle.UTILITY);
				cmd.initModality(Modality.WINDOW_MODAL);
				cmd.initOwner(stage);
				Parent pCmd = null;
				try {
					pCmd = FXMLLoader.load(getClass().getResource("/view/cmdnotice.fxml"));
				} catch (IOException e1) {
				}
				// 이벤트 등록하기 위한 객체 받아오기
				TextField cmdtxtFieldNotice = (TextField) pCmd.lookup("#txtFieldNotice");
				Button btnRegister = (Button) pCmd.lookup("#btnRegister");
				Button btnCancel = (Button) pCmd.lookup("#btnCancel");
				// 취소버튼 이벤트
				btnCancel.setOnAction(event2 -> {
					cmd.close();
					noti.close();
				});
				// 등록 버튼 이벤트
				btnRegister.setOnAction(event2 -> {
					BookDAO noticeBook = new BookDAO();
					int value = noticeBook.changeNoticeList(cmdtxtFieldNotice);
					if(value != 0) {
							labelNotice.setText(cmdtxtFieldNotice.getText());
							Function.getAlert(3, "수정 완료", "공지사항 수정완료", "변경된 내용 확인요망");
							cmd.close();
							noti.close();
							
					}
					/*
					 * Connection con = null; PreparedStatement ppsm = null; try { con =
					 * DBUtil.getConnection(); String query = "update noticeTBL set notice = ?"; //
					 * 쿼리문 실행할 준비 ppsm = con.prepareStatement(query); // 값 매치시키기 ppsm.setString(1,
					 * cmdtxtFieldNotice.getText()); int value = ppsm.executeUpdate(); if (value !=
					 * 0) { Function.getAlert(3, "수정 완료", "공지사항 수정완료", "변경된 내용 확인요망");
					 * labelNotice.setText(cmdtxtFieldNotice.getText()); } } catch (Exception e1) {
					 * Function.getAlert(3, "수정 완료", "공지사항 수정완료", "변경된 내용 확인요망");); } finally { try { if
					 * (ppsm != null) ppsm.close(); if (con != null) con.close(); } catch
					 * (SQLException e1) { } }
					 */
				});

				Scene sCmd = new Scene(pCmd);
				cmd.setScene(sCmd);
				cmd.show();
			} else {
				Function.getAlert(2, "비밀번호 오류", "비밀번호가 틀림", "비밀번호 확인요망!!!!");
				pwField1.clear();
			}
		});
		Scene s = new Scene(p);
		noti.setScene(s);
		noti.setResizable(false);
		noti.show();
	}

	// 라벨을 움직이는 함수
	private void labelMoveAction() {
		TranslateTransition t = new TranslateTransition();
		Duration d = new Duration(15000);
		Node n = labelNotice;
		t.setNode(n);
		t.setDuration(d);
		t.setFromX(600+(labelNotice.getText().length())*50);
		t.setToX(-900+(labelNotice.getText().length()));
		t.setCycleCount(50000);
		t.play();
	}

	// 화면에 DB에 있는 공지사항을 보여주는 함수
	private void showNotice() {
		BookDAO bookDAO = new BookDAO();
		ArrayList<Notice> arrayList = bookDAO.getNoticeLoadList();
		labelNotice.setText(arrayList.get(0).getNotice());
	}

	// 포켓몬영상을 눌렀을 경우에 대한 함수
	private void handleBtnTodayAction(ActionEvent e) {
		Stage pM = new Stage(StageStyle.UTILITY);
		// 모달 or 모달리스
		pM.initModality(Modality.WINDOW_MODAL);
		// 주종관계 설정
		pM.initOwner(stage);
		// fxml 파일 가져오기
		Parent p = null;
		try {
			p = FXMLLoader.load(getClass().getResource("/view/media.fxml"));
			Scene s = new Scene(p);
			// 이벤트 등록을 위한 객체 가져오기
			MediaView mdvRandom = (MediaView) p.lookup("#mdvRandom");
			Button btnRandomExit = (Button) p.lookup("#btnRandomExit");
			Button btnReview = (Button) p.lookup("#btnReview");
			// 미디어 설정
			i = (int) (Math.random() * 4) + 1;
			media = new Media(getClass().getResource("/medias/" + i + ".mp4").toString());
			mp = new MediaPlayer(media);
			mdvRandom.setMediaPlayer(mp);
			mp.play();

			btnReview.setOnAction(event -> {
				mp.stop();
				i = (int) (Math.random() * 4) + 1;
				media = new Media(getClass().getResource("/medias/" + i + ".mp4").toString());
				mp = new MediaPlayer(media);
				mdvRandom.setMediaPlayer(mp);
				mp.play();
			});

			btnRandomExit.setOnAction(event -> {
				mp.stop();
				pM.close();
			});
			// css스타일 입혀주기
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			pM.setScene(s);
			pM.setTitle("포켓몬 미니극장");
			pM.show();

		} catch (IOException e1) {

		}
	}

	// 포켓몬도감을 눌렀을 경우에 대한 함수
	private void handleBtnBookAction(ActionEvent e) {
		ArrayList<PoketmonBook1> arrayList = new ArrayList<PoketmonBook1>();
		// 무대 설정
		Stage pkmBook = new Stage(StageStyle.UTILITY);
		// 모달 or 모달리스
		pkmBook.initModality(Modality.WINDOW_MODAL);
		// 주종관계
		pkmBook.initOwner(stage);
		// fxml 파일 불러오기
		Parent p = null;
		try {
			p = FXMLLoader.load(getClass().getResource("/view/illustrated.fxml"));
			Scene s = new Scene(p);
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			// 이벤트 설정을 위해 객체 가져오기
			TableView tblBook = (TableView) p.lookup("#tblView");
			Button btnOut = (Button) p.lookup("#btnIllustratedCencle");

			// 이벤트 설정하기
			// 돌아가기 버튼에 대한 이벤트 설정
			btnOut.setOnAction(event -> pkmBook.close());
			// 컬럼을 설정하는 함수
			// 도감 컬럼에 들어가는 정보 입력
			// 이름설정 및 사이즈 설정과 모델과 매칭시키기
			TableColumn tbcNo = new TableColumn("No");
			tbcNo.setMinWidth(100);
			tbcNo.setStyle("-fx-alignmant: CENTER;");
			tbcNo.setCellValueFactory(new PropertyValueFactory<>("no"));

			TableColumn tbcImage = new TableColumn<>("Image");
			tbcImage.setMinWidth(100);
			tbcImage.setStyle("-fx-alignmant: CENTER;");
			tbcImage.setCellValueFactory(new PropertyValueFactory<>("imageView"));

			TableColumn tbcName = new TableColumn("Name");
			tbcName.setMinWidth(80);
			tbcName.setStyle("-fx-alignmant: CENTER;");
			tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));

			TableColumn tbcType1 = new TableColumn("Type1");
			tbcType1.setMinWidth(80);
			tbcType1.setStyle("-fx-alignmant: CENTER;");
			tbcType1.setCellValueFactory(new PropertyValueFactory<>("type1"));

			TableColumn tbcType2 = new TableColumn("Type2");
			tbcType2.setMinWidth(80);
			tbcType2.setStyle("-fx-alignmant: CENTER;");
			tbcType2.setCellValueFactory(new PropertyValueFactory<>("type2"));

			tblBook.getColumns().addAll(tbcNo, tbcImage, tbcName, tbcType1, tbcType2);
			// DB 연동
			BookDAO bookDAO = new BookDAO();
			arrayList = bookDAO.getTotalLoadList();

			for (int i = 0; i < arrayList.size(); i++) {
				PoketmonBook1 poketmonBook = arrayList.get(i);
				obsPkmiList.add(poketmonBook);
			}
			tblBook.setItems(obsPkmiList);
			obsPkmiList.clear();
			totalLoadList();

			// 테이블 뷰를 선택했을경우 이벤트 등록 및 핸들러 함수처리
			tblBook.setOnMousePressed(event -> {
				tableViewIndex = tblBook.getSelectionModel().getSelectedIndex();
			});
			// 테이블뷰를 더블클릭했을경우에 이벤트
			tblBook.setOnMouseClicked(event -> {
				Stage stageBook = new Stage(StageStyle.UTILITY);
				Parent book = null;

				if (event.getClickCount() != 2) {
					return;
				}

				try {
					// 화면 불러오기
					book = FXMLLoader.load(getClass().getResource("/view/poketmonbook.fxml"));
					// 모달 리스인지 모달인지 설정하기
					stageBook.initModality(Modality.WINDOW_MODAL);
					// 주종 관계 설정
					stageBook.initOwner(pkmBook);
					//Scene 설정
					Scene bookS = new Scene(book);
					bookS.getStylesheets().add(getClass().getResource("/application/label.css").toString());
					// 이벤트 설정을 위해 객체들 가져오기
					Label lblInforType1 = (Label) book.lookup("#lblInforType1");
					Label lblInforType2 = (Label) book.lookup("#lblInforType2");
					Label lblInforHeight = (Label) book.lookup("#lblInforHeight");
					Label lblInforWeight = (Label) book.lookup("#lblInforWeight");
					Label lblInforNum = (Label) book.lookup("#lblInforNum");
					Label lblInforName = (Label) book.lookup("#lblInforName");
					Label lblInforTotal = (Label) book.lookup("#lblInforTotal");
					Button btnInforClose = (Button) book.lookup("#btnInforClose");
					ImageView lblInforImage = (ImageView) book.lookup("#lblInforImage");
					TextArea imgInformation = (TextArea) book.lookup("#imgInformation");
					Node tabStatus = book.lookup("#tabStatus");
					Node tabEvolve = book.lookup("#tabEvolve");
					Node tabType = book.lookup("#tabType");
					XYChart statusXYChart = (XYChart) book.lookup("#statusXYChart");
					ImageView imgInforEvolve = (ImageView) book.lookup("#imgInforEvolve");
					// 이벤트 설정
					//이름 크기 조정
					lblInforName.setStyle("-fx-font-size: 18; -fx-font-familly:Arial white;");
					// 나가기 버튼 이벤트 설정
					btnInforClose.setOnAction(e1 -> stageBook.close());
					BookDAO bookDAO2 = new BookDAO();
					ArrayList<PoketmonBook1> arrayList1 = bookDAO2.getPoketmonBookLoadList();
					
					// 추가 다시지정
					ObservableList<PoketmonBook1> obsPkmiList = FXCollections.observableArrayList();

					for (int i = 0; i < arrayList1.size(); i++) {
						PoketmonBook1 poketmonBook = arrayList1.get(i);
						obsPkmiList.add(poketmonBook);
					}
					// 이벤트설정
					lblInforNum.setText(String.valueOf(obsPkmiList.get(tableViewIndex).getNo()));
					lblInforName.setText(obsPkmiList.get(tableViewIndex).getName());
					lblInforImage
							.setImage(new Image("file:/C:/poketmon/" + obsPkmiList.get(tableViewIndex).getImage2()));
					imgInformation.setText(obsPkmiList.get(tableViewIndex).getInfo());
					lblInforType1.setText(obsPkmiList.get(tableViewIndex).getType1());
					lblInforType2.setText(obsPkmiList.get(tableViewIndex).getType2());
					lblInforHeight.setText(obsPkmiList.get(tableViewIndex).getHeight());
					lblInforWeight.setText(obsPkmiList.get(tableViewIndex).getWeight());
					// tabStatus 이벤트설정

					XYChart.Series hp = new XYChart.Series();
					hp.setName("HP");
					ObservableList hpList = FXCollections.observableArrayList();
					hpList.add(new XYChart.Data("", Integer.parseInt(obsPkmiList.get(tableViewIndex).getHp())));
					hp.setData(hpList);
					statusXYChart.getData().add(hp);

					XYChart.Series atk = new XYChart.Series();
					atk.setName("Atk");
					ObservableList atkList = FXCollections.observableArrayList();
					atkList.add(new XYChart.Data("", Integer.parseInt(obsPkmiList.get(tableViewIndex).getAtk())));
					atk.setData(atkList);
					statusXYChart.getData().add(atk);

					XYChart.Series def = new XYChart.Series();
					def.setName("Def");
					ObservableList defList = FXCollections.observableArrayList();
					defList.add(new XYChart.Data("", Integer.parseInt(obsPkmiList.get(tableViewIndex).getDef())));
					def.setData(defList);
					statusXYChart.getData().add(def);

					XYChart.Series sAtk = new XYChart.Series();
					sAtk.setName("SAtk");
					ObservableList sAtkList = FXCollections.observableArrayList();
					sAtkList.add(new XYChart.Data("", Integer.parseInt(obsPkmiList.get(tableViewIndex).getsAtk())));

					sAtk.setData(sAtkList);
					statusXYChart.getData().add(sAtk);

					XYChart.Series sDef = new XYChart.Series();
					sDef.setName("SDef");
					ObservableList sDefList = FXCollections.observableArrayList();
					sDefList.add(new XYChart.Data("", Integer.parseInt(obsPkmiList.get(tableViewIndex).getsDef())));
					sDef.setData(sDefList);
					statusXYChart.getData().add(sDef);

					XYChart.Series speed = new XYChart.Series();
					speed.setName("Spd");
					ObservableList speedList = FXCollections.observableArrayList();
					speedList.add(new XYChart.Data("", Integer.parseInt(obsPkmiList.get(tableViewIndex).getSpeed())));
					speed.setData(speedList);
					statusXYChart.getData().add(speed);

					// 총 능력치 합을 구하는 함수
					Connection con = null;
					PreparedStatement ppsm = null;
					ResultSet rs = null;
					try {
						con = DBUtil.getConnection();
						String query = "select sum(pkmHP+pkmAttack+pkmDefense+pkmSAttack+pkmSDefense+pkmSpeed) from bookTbl a inner join poketmonTBL b on a.pkmNum = b.pkmNUm where a.pkmNum = ?";
						ppsm = con.prepareStatement(query);
						ppsm.setInt(1, obsPkmiList.get(tableViewIndex).getNo());
						rs = ppsm.executeQuery();
						while (rs.next()) {
							int total = rs.getInt(1);
							lblInforTotal.setText(String.valueOf(total));
						}
					} catch (Exception e1) {
						Function.getAlert(2, "총합 계산 오류", "총합 계산 실패", "문제사항 : " + e1.getMessage());
					}
					
					switch(obsPkmiList.get(tableViewIndex).getType1()) {
					case"불": lblInforType1.setStyle("-fx-text-fill:white; -fx-background-color:orange; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;");	break;
					case"물": lblInforType1.setStyle("-fx-text-fill:white; -fx-background-color:#5AAEFF; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
					case"풀": lblInforType1.setStyle("-fx-text-fill:white; -fx-background-color:#59DA50; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
					case"독": lblInforType1.setStyle("-fx-text-fill:white; -fx-background-color:#9253EB; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
					case"비행": lblInforType1.setStyle("-fx-text-fill:white; -fx-background-color:#79ABFF; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
					case"드래곤": lblInforType1.setStyle("-fx-text-fill:white; -fx-background-color:#2924BD; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
					case"전기": lblInforType1.setStyle("-fx-text-fill:white; -fx-background-color:#FFFF24; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
					}
					
					if(obsPkmiList.get(tableViewIndex).getType2() != null) {
						switch(obsPkmiList.get(tableViewIndex).getType2()) {
						case"불": lblInforType2.setStyle("-fx-text-fill:white; -fx-background-color:orange; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;");	break;
						case"물": lblInforType2.setStyle("-fx-text-fill:white; -fx-background-color:#5AAEFF; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
						case"풀": lblInforType2.setStyle("-fx-text-fill:white; -fx-background-color:#59DA50; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
						case"독": lblInforType2.setStyle("-fx-text-fill:white; -fx-background-color:#9253EB; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
						case"비행": lblInforType2.setStyle("-fx-text-fill:white; -fx-background-color:#79ABFF; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
						case"드래곤": lblInforType2.setStyle("-fx-text-fill:white; -fx-background-color:#2924BD; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
						case"전기": lblInforType2.setStyle("-fx-text-fill:white; -fx-background-color:#FFFF24; -fx-font-family: Quicksand; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius:10;"); break;
						}
						
					}

					
					
					stageBook.setScene(bookS);
					stageBook.show();
				} catch (IOException e1) {
					Function.getAlert(2, "화면 불러오기 오류", "화면 불러오기실패", "문제사항 : " + e1.getMessage());
				}
			});
			pkmBook.setScene(s);
			pkmBook.setTitle("포켓몬도감");
			pkmBook.setResizable(false);
			pkmBook.show();
		} catch (IOException e1) {

		}

	}

	// 랜덤포켓몬사진과 차트를 보여주는 함수
	// 화면에 랜덤 포켓몬을 보여주는 함수
	private void loadRamdomPhoto(ActionEvent e) {
		BookDAO bookDAO = new BookDAO();
		ArrayList<PoketmonBook1> arrayList = bookDAO.getRandomJoinLoadList();
		Random random = new Random();
		int ran = random.nextInt(arrayList.size());
		PoketmonBook1 randomPoketmon = arrayList.get(ran);
		// 이름 설정 이벤트
		lblMonName.setText(randomPoketmon.getName());
		// 사진 설정 이벤트
		imgPoket.setImage(new Image("file:/C:/poketmon/" + randomPoketmon.getImage()));
		// 수치관련 차트 이벤트

		mainXYchart.getData().clear();
		XYChart.Series hp = new XYChart.Series();
		hp.setName("HP");
		ObservableList hpList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			hpList.add(new XYChart.Data("", Integer.parseInt(randomPoketmon.getHp())));
		}
		hp.setData(hpList);
		mainXYchart.getData().add(hp);

		XYChart.Series atk = new XYChart.Series();
		atk.setName("Atk");
		ObservableList atkList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			atkList.add(new XYChart.Data("", Integer.parseInt(randomPoketmon.getAtk())));
		}
		atk.setData(atkList);
		mainXYchart.getData().add(atk);

		XYChart.Series def = new XYChart.Series();
		def.setName("Def");
		ObservableList defList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			defList.add(new XYChart.Data("", Integer.parseInt(randomPoketmon.getDef())));
		}
		def.setData(defList);
		mainXYchart.getData().add(def);

		XYChart.Series sAtk = new XYChart.Series();
		sAtk.setName("SAtk");
		ObservableList sAtkList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			sAtkList.add(new XYChart.Data("", Integer.parseInt(randomPoketmon.getsAtk())));
		}
		sAtk.setData(sAtkList);
		mainXYchart.getData().add(sAtk);

		XYChart.Series sDef = new XYChart.Series();
		sDef.setName("SDef");
		ObservableList sDefList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			sDefList.add(new XYChart.Data("", Integer.parseInt(randomPoketmon.getsDef())));
		}
		sDef.setData(sDefList);
		mainXYchart.getData().add(sDef);

		XYChart.Series speed = new XYChart.Series();
		speed.setName("Spd");
		ObservableList speedList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			speedList.add(new XYChart.Data("", Integer.parseInt(randomPoketmon.getSpeed())));
		}
		speed.setData(speedList);
		mainXYchart.getData().add(speed);
	}

	// 대표이미지 버튼에 대한 함수
	private void handleBtnChageImage(ActionEvent e) {
		// 파일츄저 객체참조변수 생성
		FileChooser fileChooser = new FileChooser();
		// 입력가능한 이미지 파일형식 입력
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		selectFile = fileChooser.showOpenDialog(stage);
		Connection con = null;
		PreparedStatement ppsm = null;
		String fileName = null;

		if (selectFile != null) {
			try {
				fileName = "user" + System.currentTimeMillis() + selectFile.getName();
				BufferedInputStream bis = null;
				BufferedOutputStream bos = null;

				bis = new BufferedInputStream(new FileInputStream(selectFile));
				bos = new BufferedOutputStream(
						new FileOutputStream(userImagesFile.getAbsolutePath() + "\\" + fileName));
				int data = -1;
				while ((data = bis.read()) != -1) {
					bos.write(data);
					bos.flush();
				}
			} catch (Exception e2) {
				Function.getAlert(2, "error", "이미지를 등록해주세요", e2.getMessage());
			}
			try {
				String localURL = selectFile.toURI().toURL().toString();
				Image image = new Image(localURL, false);
				imgTrainer.setImage(image);
				con = DBUtil.getConnection();
				String query = "update userTBL set userImage = ? where userPhone = ? ";
				ppsm = con.prepareStatement(query);
				ppsm.setString(1, fileName);
				ppsm.setString(2, RootController.userLogin.getUserPhone());

				int value = ppsm.executeUpdate();

				if (value != 0) {
					Function.getAlert(3, "사진 수정 완료", "사진 수정에 성공했습니다.", "사진 확인 요망");
				} else {
					throw new Exception("수정에 문제 있음");
				}
			} catch (Exception e1) {
				Function.getAlert(2, "error", "사진파일을 가져올수 없습니다.", e1.getMessage());
			}
		}

	}

	// 트레이너 이름 변경에 대한 함수
	private void handleBtnChageName(ActionEvent e) {
		Stage ChangeName = new Stage(StageStyle.UTILITY);
		// modal or none
		ChangeName.initModality(Modality.WINDOW_MODAL);
		// 주종관계
		ChangeName.initOwner(this.stage);
		// 파일 가져오기
		Parent cn = null;
		try {
			cn = FXMLLoader.load(getClass().getResource("/view/changename.fxml"));
			// 이벤트 등록을 위한 객체 가져오기
			TextField nickNamefield = (TextField) cn.lookup("#nickNamefield");
			Button btnSameCheck = (Button) cn.lookup("#btnSameCheck");
			Button btnChangeName = (Button) cn.lookup("#btnChargeName");
			Button btnChangeClose = (Button) cn.lookup("#btnChangeClose");
			
			// 취소 버튼에 대한 이벤트
			btnChangeClose.setOnAction(event -> ChangeName.close());
			// 중복확인에 대한 이벤트 등록
			btnSameCheck.setOnAction(event -> {
				if(nickNamefield.getText().trim().equals("")) {
					Function.getAlert(4, "error", "수정실패", "내용 확인후 다시시도 해주세요.");
					return;
				}
				Connection con = null;
				PreparedStatement ppsm = null;
				ResultSet rs = null;
				try {
					con = DBUtil.getConnection();
					String query = "select userNickName from userTBL where userNickName = ?";
					ppsm = con.prepareStatement(query);
					ppsm.setString(1, nickNamefield.getText());
					rs = ppsm.executeQuery();

					if (rs != null && rs.isBeforeFirst()) {
						Function.getAlert(2, "트레이너이름 중복확인", "중복된 트레이너이름 입니다", "다른 트레이너이름을 입력해주세요");
					} else {
						Function.getAlert(2, "트레이너이름 중복확인", "사용가능한 트레이너이름 입니다", "");
					}
				} catch (Exception e3) {
					Function.getAlert(4, "error", "정보를 입력해주세요", "확인후 다시시도 해주세요.");
				}
			});
			// 바꾸기 버튼에 대한 이벤트 등록
			btnChangeName.setOnAction(event -> {
				Connection con = null;
				PreparedStatement ppsm = null;
				try {
					con = DBUtil.getConnection();
					String query = "update userTBL set userNickName = ? where userID = ? ";
					ppsm = con.prepareStatement(query);
					ppsm.setString(1, nickNamefield.getText());
					ppsm.setString(2, RootController.userLogin.getUserID());
					int value = ppsm.executeUpdate();
					if (value != 0) {
						Function.getAlert(2, "성공", "닉네임 변경 성공 ", "새로운 닉네임을 확인해주세요.");
						labelTrainer.setText(nickNamefield.getText());
						ChangeName.close();
					}
				} catch (Exception e1) {
					Function.getAlert(4, "error", "닉네임 변경 실패 ", "확인후 다시시도 해주세요.");
				}
			});
			Scene s = new Scene(cn);
			// css스타일 입혀주기
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			ChangeName.setScene(s);
			ChangeName.setResizable(false);
			ChangeName.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 육성 버튼에 대한 함수
	private void handleBtnGlow(ActionEvent e) {

		ObservableList<PoketmonBook1> obsPkmiListGlow = FXCollections.observableArrayList();

		// 화면 설정
		Stage glowStage = new Stage(StageStyle.UTILITY);
		// set modal
		glowStage.initModality(Modality.WINDOW_MODAL);
		// set 주종관계
		glowStage.initOwner(this.stage);
		// 화면 fxml파일 불러오기
		Parent promote = null;
		try {
			promote = FXMLLoader.load(getClass().getResource("/view/promote.fxml"));
			// 이벤트 설정을 위한 객체를 가져오기
			Label labelExpUserName = (Label) promote.lookup("#labelExpUserName");
			Label imageExpPoketmonName = (Label) promote.lookup("#imageExpPoketmonName");
			ImageView imgExpPoketmon = (ImageView) promote.lookup("#imgExpPoketmon");
			ProgressBar progressBar = (ProgressBar) promote.lookup("#progressBar");
			Button btnSelect = (Button) promote.lookup("#btnSelect");
			Button btnExp1 = (Button) promote.lookup("#btnExp1");
			Button btnExp2 = (Button) promote.lookup("#btnExp2");
			Button btnExp3 = (Button) promote.lookup("#btnExp3");
			Button btnExpExit = (Button) promote.lookup("#btnExpExit");
			// 이벤트 설정하기
			// 트레이너 이름 세팅
			labelExpUserName.setText(labelTrainer.getText());
			// 종료버튼 이벤트
			btnExpExit.setOnAction(event -> {glowStage.close();
				obsPkmiListGlow.clear();
				evolveFlag(true);
				});

			// 원하는 포켓몬 설정 버튼 이벤트
			btnSelect.setOnAction(event -> {
				obsPkmiListGlow.clear();
				evolveFlag(true);
				ArrayList<PoketmonBook1> arrayList = new ArrayList<PoketmonBook1>();
				// 무대 설정
				Stage pkmBook = new Stage(StageStyle.UTILITY);
				// 모달 or 모달리스
				pkmBook.initModality(Modality.WINDOW_MODAL);
				// 주종관계
				pkmBook.initOwner(glowStage);
				// fxml 파일 불러오기
				Parent p = null;
				try {
					p = FXMLLoader.load(getClass().getResource("/view/illustrated.fxml"));
					Scene s = new Scene(p);
					// css스타일 입혀주기
					s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
					// 이벤트 설정을 위해 객체 가져오기
					TableView tblBook = (TableView) p.lookup("#tblView");
					Button btnOut = (Button) p.lookup("#btnIllustratedCencle");

					// 이벤트 설정하기
					// 돌아가기 버튼에 대한 이벤트 설정
					btnOut.setOnAction(event1 -> pkmBook.close());
					// 컬럼을 설정하는 함수
					// 도감 컬럼에 들어가는 정보 입력
					// 이름설정 및 사이즈 설정과 모델과 매칭시키기
					TableColumn tbcNo = new TableColumn("No");
					tbcNo.setMinWidth(100);
					tbcNo.setStyle("-fx-alignmant: CENTER;");
					tbcNo.setCellValueFactory(new PropertyValueFactory<>("no"));

					TableColumn tbcImage = new TableColumn<>("Image");
					tbcImage.setMinWidth(100);
					tbcImage.setStyle("-fx-alignmant: CENTER;");
					tbcImage.setCellValueFactory(new PropertyValueFactory<>("imageView"));

					TableColumn tbcName = new TableColumn("Name");
					tbcName.setMinWidth(80);
					tbcName.setStyle("-fx-alignmant: CENTER;");
					tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));

					TableColumn tbcType1 = new TableColumn("Type1");
					tbcType1.setMinWidth(80);
					tbcType1.setStyle("-fx-alignmant: CENTER;");
					tbcType1.setCellValueFactory(new PropertyValueFactory<>("type1"));

					TableColumn tbcType2 = new TableColumn("Type2");
					tbcType2.setMinWidth(80);
					tbcType2.setStyle("-fx-alignmant: CENTER;");
					tbcType2.setCellValueFactory(new PropertyValueFactory<>("type2"));

					tblBook.getColumns().addAll(tbcNo, tbcImage, tbcName, tbcType1, tbcType2);
					// DB 연동
					BookDAO bookDAO = new BookDAO();
					arrayList = bookDAO.getTotalLoadList();

					for (int i = 0; i < arrayList.size(); i++) {
						PoketmonBook1 poketmonBook = arrayList.get(i);
						obsPkmiList.add(poketmonBook);
					}
					tblBook.setItems(obsPkmiList);
					obsPkmiList.clear();
					totalLoadList();

					// 테이블 뷰를 선택했을경우 이벤트 등록 및 핸들러 함수처리
					tblBook.setOnMousePressed(event1 -> {
						tableViewIndex = tblBook.getSelectionModel().getSelectedIndex();
					});
					// 테이블뷰를 더블클릭했을경우에 이벤트
					tblBook.setOnMouseClicked(event1 -> {
						
						if (event1.getClickCount() != 2) {
							return;
						}

						BookDAO bookDAOGlow = new BookDAO();
						ArrayList<PoketmonBook1> arrayListGlow = bookDAOGlow.getPoketmonBookLoadList();
						for (int i = 0; i < arrayListGlow.size(); i++) {
							PoketmonBook1 glowPoket = arrayListGlow.get(i);
							obsPkmiListGlow.add(glowPoket);
						}
						
						imageExpPoketmonName.setText(obsPkmiListGlow.get(tableViewIndex).getName());
						imgExpPoketmon.setImage(
								new Image("file:/C:/poketmon/" + obsPkmiListGlow.get(tableViewIndex).getImage2()));
						pkmBook.close();
					});
					// css스타일 입혀주기
					s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
					pkmBook.setScene(s);
					pkmBook.setTitle("포켓몬도감");
					pkmBook.setResizable(false);
					pkmBook.show();
				} catch (IOException e1) {
				}

			});

			exp.setEXP(0.0);

			exp.expProperty().addListener(new ChangeListener<Object>() {

				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
					progressBar.progressProperty().bind(exp.expProperty());
				}

			});

			// 버튼에 관한 이벤트 설정
			btnExp1.setOnAction(e1 -> {
				setExpNoImageException(e1, imgExpPoketmon, 0.03);
				poketmonEvolve(obsPkmiListGlow, imgExpPoketmon, imageExpPoketmonName);

			});

			btnExp2.setOnAction(e2 -> {
				setExpNoImageException(e2, imgExpPoketmon, 0.06);
				poketmonEvolve(obsPkmiListGlow, imgExpPoketmon, imageExpPoketmonName);

			});

			btnExp3.setOnAction(e3 -> {
				setExpNoImageException(e3, imgExpPoketmon, 0.09);
				poketmonEvolve(obsPkmiListGlow, imgExpPoketmon, imageExpPoketmonName);
				/*
				 * ArrayList<PoketmonBook1> glownPoket = new ArrayList<PoketmonBook1>();
				 * 
				 * if (exp.getEXP() >= 1.0 &&
				 * obsPkmiListGlow.get(tableViewIndex).getEvolve().equals("O") && evolveFlag) {
				 * 
				 * Connection con = null; PreparedStatement ppsm = null; ResultSet rs = null;
				 * try { con = DBUtil.getConnection();
				 * 
				 * String query =
				 * "select * from bookTbl a inner join poketmonTBL b on a.pkmNum = b.pkmNUm where a.pkmNum = ?"
				 * ; ppsm = con.prepareStatement(query); ppsm.setInt(1,
				 * (obsPkmiListGlow.get(tableViewIndex).getNo()) + 1); rs = ppsm.executeQuery();
				 * while (rs.next()) { glownPoketmon = new PoketmonBook1(rs.getInt(1),
				 * rs.getString(6), rs.getString(3), rs.getString(17));
				 * glownPoket.add(glownPoketmon); }
				 * 
				 * imgExpPoketmon.setImage(new Image("file:/C:/poketmon/" +
				 * glownPoketmon.getImage2()));
				 * imageExpPoketmonName.setText(glownPoketmon.getName());
				 * 
				 * exp.setEXP(0.0);
				 * 
				 * evolveFlag(false);
				 * 
				 * } catch (Exception e4) { Function.getAlert(1, "진화 포켓몬 오류",
				 * "진화정보를 가져올수 없습니다.", "문제사항 : " + e4.getMessage()); return; }
				 * 
				 * }
				 * 
				 * if (exp.getEXP() >= 1.0 && glownPoketmon.getEvolve().equals("O")) {
				 * Connection con = null; PreparedStatement ppsm = null; ResultSet rs = null;
				 * try { con = DBUtil.getConnection(); String query =
				 * "select * from bookTbl a inner join poketmonTBL b on a.pkmNum = b.pkmNUm where a.pkmNum = ?"
				 * ; ppsm = con.prepareStatement(query); ppsm.setInt(1, (glownPoketmon.getNo())
				 * + 1); rs = ppsm.executeQuery(); while (rs.next()) { glownPoketmon = new
				 * PoketmonBook1(rs.getInt(1), rs.getString(6), rs.getString(3),
				 * rs.getString(17)); imgExpPoketmon.setImage(new Image("file:/C:/poketmon/" +
				 * rs.getString(6))); imageExpPoketmonName.setText(rs.getString(3)); }
				 * exp.setEXP(0.0); } catch (Exception e4) { Function.getAlert(1, "진화 포켓몬 오류",
				 * "진화정보를 가져올수 없습니다.", "문제사항 : " + e4.getMessage()); return; } }
				 */
			});

			Scene s = new Scene(promote);
			glowStage.setScene(s);
			glowStage.show();
		} catch (IOException e1) {
			Function.getAlert(1, "육성 창 오류", "육성 창을 가져올수 없습니다.", "문제사항 : " + e1.getMessage());
		}
	}

	// 진화여부를 파악하는 함수
	private void evolveFlag(boolean flag) {
		evolveFlag = flag;
	}

	// 포켓몬 진화에 대한 함수
	private void poketmonEvolve(ObservableList<PoketmonBook1>obsGlowList, ImageView imageView,Label label) {
		
		if (exp.getEXP() >= 1.0 && obsGlowList.get(tableViewIndex).getEvolve().equals("O") && evolveFlag) {

			Connection con = null;
			PreparedStatement ppsm = null;
			ResultSet rs = null;
			try {
				con = DBUtil.getConnection();

				String query = "select * from bookTbl a inner join poketmonTBL b on a.pkmNum = b.pkmNUm where a.pkmNum = ?";
				ppsm = con.prepareStatement(query);
				ppsm.setInt(1, (obsGlowList.get(tableViewIndex).getNo()) + 1);
				rs = ppsm.executeQuery();
				while (rs.next()) {
					glownPoketmon = new PoketmonBook1(rs.getInt(1), rs.getString(6), rs.getString(3),
							rs.getString(17));
					
				}

				imageView.setImage(new Image("file:/C:/poketmon/" + glownPoketmon.getImage2()));
				label.setText(glownPoketmon.getName());

				exp.setEXP(0.0);

				evolveFlag(false);

			} catch (Exception e4) {
				Function.getAlert(1, "진화 포켓몬 오류", "진화정보를 가져올수 없습니다.", "문제사항 : " + e4.getMessage());
				return;
			}

		}
		
		if (exp.getEXP() >= 1.0 && glownPoketmon.getEvolve().equals("O")) {
			Connection con = null;
			PreparedStatement ppsm = null;
			ResultSet rs = null;
			try {
				con = DBUtil.getConnection();
				String query = "select * from bookTbl a inner join poketmonTBL b on a.pkmNum = b.pkmNUm where a.pkmNum = ?";
				ppsm = con.prepareStatement(query);
				ppsm.setInt(1, (glownPoketmon.getNo()) + 1);
				rs = ppsm.executeQuery();
				while (rs.next()) {
					glownPoketmon = new PoketmonBook1(rs.getInt(1), rs.getString(6), rs.getString(3),
							rs.getString(17));
					imageView.setImage(new Image("file:/C:/poketmon/" + rs.getString(6)));
					label.setText(rs.getString(3));
				}
				exp.setEXP(0.0);
			} catch (Exception e4) {
				Function.getAlert(1, "진화 포켓몬 오류", "진화정보를 가져올수 없습니다.", "문제사항 : " + e4.getMessage());
				return;
			}
		}else if(exp.getEXP() >= 1.0 && glownPoketmon.getEvolve().equals("X")){
			exp.setEXP(0.0);
			Function.getAlert(2, "진화 포켓몬 오류", "진화정보를 가져올수 없습니다.", "더이상 진화를 할 수 없습니다.");
		}
		
	}

	// 이미지가 등록 여부에 대한 오류 파악 함수
	private Event setExpNoImageException(Event event, ImageView image, double expValue) {
		try {
			if (image.getImage().toString().equals(null)) {
				throw new Exception();
			}
		} catch (Exception eImage) {
			Function.getAlert(2, "오류!", "진화할 포켓몬이 없습니다..", "사진을 설정해주세요!");
			exp.setEXP(0.0);
			return event;
		}
		exp.setEXP(exp.getEXP() + expValue);
		return event;

	}
}
