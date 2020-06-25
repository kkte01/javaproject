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
import javafx.scene.control.Label;
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
	PoketmonBook1 pkmBook1;
	public int tableViewIndex;

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
		btnClose.setOnAction(e-> stage.close());
		
	}

	
	// 유저 이미지를 저장하는 폴더 생성 함수
	private void setUserImage() {
		userImagesFile = new File("C:/userImages");
		if(!userImagesFile.exists()){
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
					Connection con = null;
					PreparedStatement ppsm = null;
					try {
						con = DBUtil.getConnection();
						String query = "update noticeTBL set notice = ?";
						// 쿼리문 실행할 준비
						ppsm = con.prepareStatement(query);
						// 값 매치시키기
						ppsm.setString(1, cmdtxtFieldNotice.getText());
						int value = ppsm.executeUpdate();
						if (value != 0) {
							Function.getAlert(3, "수정 완료", "공지사항 수정완료", "변경된 내용 확인요망");
							labelNotice.setText(cmdtxtFieldNotice.getText());
						}
					} catch (Exception e1) {
						Function.getAlert(2, "수정 실패", "내용 확인 요망", "내용을 확인해주세요");
					} finally {
						try {
							if (ppsm != null)
								ppsm.close();
							if (con != null)
								con.close();
						} catch (SQLException e1) {
						}
					}
					cmd.close();
					noti.close();
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
		Duration d = new Duration(9000);
		Node n = labelNotice;
		t.setNode(n);
		t.setDuration(d);
		t.setFromX(600);
		t.setToX(-600);
		t.setCycleCount(50000);
		t.play();
	}

	// 화면에 DB에 있는 공지사항을 보여주는 함수
	private void showNotice() {
		BookDAO bookDAO = new BookDAO();
		ArrayList<Notice>arrayList = bookDAO.getNoticeLoadList();
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
		} catch (IOException e1) {
		}
		Scene s = new Scene(p);
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
		tbcImage.setStyle("-fx-alignmant: CENTER;");
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
		tblBook.setOnMousePressed(event-> {
			tableViewIndex=tblBook.getSelectionModel().getSelectedIndex();
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
				// 나가기 버튼 이벤트 설정
				btnInforClose.setOnAction(e1 ->stageBook.close());
				BookDAO bookDAO2 = new BookDAO();
				ArrayList<PoketmonBook1>arrayList1 = bookDAO2.getPoketmonBookLoadList();
				
				//추가 다시지정
				ObservableList<PoketmonBook1> obsPkmiList = FXCollections.observableArrayList();
				
				for(int i = 0; i < arrayList1.size(); i++ ) {
					PoketmonBook1 poketmonBook = arrayList1.get(i);
					obsPkmiList.add(poketmonBook);
				}
				//이벤트설정
				lblInforNum.setText(String.valueOf(obsPkmiList.get(tableViewIndex).getNo()));
				lblInforName.setText(obsPkmiList.get(tableViewIndex).getName());
				lblInforImage.setImage(new Image("file:/C:/poketmon/"+obsPkmiList.get(tableViewIndex).getImage2()));
				imgInformation.setText(obsPkmiList.get(tableViewIndex).getInfo());
				lblInforType1.setText(obsPkmiList.get(tableViewIndex).getType1());
				lblInforType2.setText(obsPkmiList.get(tableViewIndex).getType2());
				lblInforHeight.setText(obsPkmiList.get(tableViewIndex).getHeight());
				lblInforWeight.setText(obsPkmiList.get(tableViewIndex).getWeight());
				// tabStatus 이벤트설정
					
					XYChart.Series hp = new XYChart.Series();
					hp.setName("HP");
					ObservableList hpList = FXCollections.observableArrayList();
					
						hpList.add(new XYChart.Data(hp.getName(), Integer.parseInt(obsPkmiList.get(tableViewIndex).getHp())));
					
					hp.setData(hpList);
					statusXYChart.getData().add(hp);
					
					XYChart.Series atk = new XYChart.Series();
					atk.setName("Atk");
					ObservableList atkList = FXCollections.observableArrayList();
					
						atkList.add(new XYChart.Data(atk.getName(), Integer.parseInt(obsPkmiList.get(tableViewIndex).getAtk())));
					
					atk.setData(atkList);
					statusXYChart.getData().add(atk);
					
					XYChart.Series def = new XYChart.Series();
					def.setName("Def");
					ObservableList defList = FXCollections.observableArrayList();
					
						defList.add(new XYChart.Data(def.getName(), Integer.parseInt(obsPkmiList.get(tableViewIndex).getDef())));
					
					def.setData(defList);
					statusXYChart.getData().add(def);
					
					XYChart.Series sAtk = new XYChart.Series();
					sAtk.setName("SAtk");
					ObservableList sAtkList = FXCollections.observableArrayList();
					
						sAtkList.add(new XYChart.Data(sAtk.getName(), Integer.parseInt(obsPkmiList.get(tableViewIndex).getsAtk())));
					
					sAtk.setData(sAtkList);
					statusXYChart.getData().add(sAtk);
					
					XYChart.Series sDef = new XYChart.Series();
					sDef.setName("SDef");
					ObservableList	sDefList = FXCollections.observableArrayList();
					
						sDefList.add(new XYChart.Data(sDef.getName(), Integer.parseInt(obsPkmiList.get(tableViewIndex).getsDef())));
					
					sDef.setData(sDefList);
					statusXYChart.getData().add(sDef);
					
					XYChart.Series speed = new XYChart.Series();
					speed.setName("Spd");
					ObservableList speedList = FXCollections.observableArrayList();
				
						speedList.add(new XYChart.Data(speed.getName(), Integer.parseInt(obsPkmiList.get(tableViewIndex).getSpeed())));
					
					speed.setData(speedList);
					statusXYChart.getData().add(speed);
					// 총 능력치 합을 구하는 함수
					Connection con = null;
					PreparedStatement ppsm = null;
					ResultSet rs = null;
					try {
						con = DBUtil.getConnection();
						String query = "select sum(pkmHP+pkmAttack+pkmDefense+pkmSAttack+pkmSDefense+pkmSpeed) from bookTbl a inner join poketmonTBL b on a.pkmNum = b.pkmNUm where a.pkmNum = ?";
						ppsm =con.prepareStatement(query);
						ppsm.setInt(1, obsPkmiList.get(tableViewIndex).getNo());
						rs = ppsm.executeQuery();
						while(rs.next()) {
							int total =rs.getInt(1);
							lblInforTotal.setText(String.valueOf(total));	
						}
					} catch (Exception e1) {
						Function.getAlert(2, "총합 계산 오류", "총합 계산 실패", "문제사항 : "+e1.getMessage());
					} 
				Scene bookS = new Scene(book);
				stageBook.setScene(bookS);
				stageBook.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		pkmBook.setScene(s);
		pkmBook.setTitle("포켓몬도감");
		pkmBook.setResizable(false);
		pkmBook.show();

	}
	// 랜덤포켓몬사진과 차트를 보여주는 함수
	// 화면에 랜덤 포켓몬을 보여주는 함수
	private void loadRamdomPhoto(ActionEvent e) {
		BookDAO bookDAO = new BookDAO();
		ArrayList<PoketmonBook1> arrayList = bookDAO.getRandomJoinLoadList();
		Random random = new Random();
		int ran = random.nextInt(arrayList.size());
		PoketmonBook1 Random = arrayList.get(ran);
		
		//사진 설정 이벤트
		imgPoket.setImage(new Image("file:/C:/poketmon/"+Random.getImage()));
		//수치관련 차트 이벤트
		mainXYchart.getData().clear();
		XYChart.Series hp = new XYChart.Series();
		hp.setName("HP");
		ObservableList hpList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			hpList.add(new XYChart.Data(hp.getName(), Integer.parseInt(Random.getHp())));
		}
		hp.setData(hpList);
		mainXYchart.getData().add(hp);
		
		XYChart.Series atk = new XYChart.Series();
		atk.setName("Atk");
		ObservableList atkList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			atkList.add(new XYChart.Data(atk.getName(), Integer.parseInt(Random.getAtk())));
		}
		atk.setData(atkList);
		mainXYchart.getData().add(atk);
		
		XYChart.Series def = new XYChart.Series();
		def.setName("Def");
		ObservableList defList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			defList.add(new XYChart.Data(def.getName(), Integer.parseInt(Random.getDef())));
		}
		def.setData(defList);
		mainXYchart.getData().add(def);
		
		XYChart.Series sAtk = new XYChart.Series();
		sAtk.setName("SAtk");
		ObservableList sAtkList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			sAtkList.add(new XYChart.Data(sAtk.getName(), Integer.parseInt(Random.getsAtk())));
		}
		sAtk.setData(sAtkList);
		mainXYchart.getData().add(sAtk);
		
		XYChart.Series sDef = new XYChart.Series();
		sDef.setName("SDef");
		ObservableList	sDefList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			sDefList.add(new XYChart.Data(sDef.getName(), Integer.parseInt(Random.getsDef())));
		}
		sDef.setData(sDefList);
		mainXYchart.getData().add(sDef);
		
		XYChart.Series speed = new XYChart.Series();
		speed.setName("Spd");
		ObservableList speedList = FXCollections.observableArrayList();
		for (int i1 = 0; i1 < arrayList.size(); i1++) {
			speedList.add(new XYChart.Data(speed.getName(), Integer.parseInt(Random.getSpeed())));
		}
		speed.setData(speedList);
		mainXYchart.getData().add(speed);
	}
	// 대표이미지 버튼에 대한 함수
	private void handleBtnChageImage(ActionEvent e) {
		//파일츄저 객체참조변수 생성
		FileChooser fileChooser = new FileChooser();
		//입력가능한 이미지 파일형식 입력
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		selectFile = fileChooser.showOpenDialog(stage);
		Connection con = null;
		PreparedStatement ppsm = null;
		String fileName = null;
		
		if(selectFile!=null) {
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
				}catch (Exception e2) {
					Function.getAlert(2, "error", "이미지를 등록해주세요", e2.getMessage());
				}
			try {
				String localURL = selectFile.toURI().toURL().toString();
				Image image = new Image(localURL, false);
				imgTrainer.setImage(image);
				con =DBUtil.getConnection();
				String query ="update userTBL set userImage = ? where userPhone = ? ";
				ppsm =con.prepareStatement(query);
				ppsm.setString(1, fileName);
				ppsm.setString(2, RootController.userLogin.getUserPhone());
				
				int value = ppsm.executeUpdate();
				
				if(value != 0 ) {
					Function.getAlert(3, "사진 수정 완료", "사진 수정에 성공했습니다.", "사진 확인 요망");
				}else {
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
		//파일 가져오기
		Parent cn = null;
		try {
			cn = FXMLLoader.load(getClass().getResource("/view/changename.fxml"));
			//이벤트 등록을 위한  객체 가져오기
			TextField nickNamefield =(TextField) cn.lookup("#nickNamefield");
			Button btnSameCheck = (Button) cn.lookup("#btnSameCheck");
			Button btnChangeName = (Button) cn.lookup("#btnChargeName");
			Button btnChangeClose = (Button) cn.lookup("#btnChangeClose");
			// 취소 버튼에 대한 이벤트
			btnChangeClose.setOnAction(event -> ChangeName.close());
			//중복확인에 대한 이벤트 등록
			btnSameCheck.setOnAction(event-> {
				Connection con = null;
				PreparedStatement ppsm = null;
				ResultSet rs = null;
				
				try {
					con = DBUtil.getConnection();
					String query = "select userNickName from userTBL where userNickName = ?";
					ppsm =con.prepareStatement(query);
					ppsm.setString(1, nickNamefield.getText());
					rs = ppsm.executeQuery();
					
					if(rs!=null && rs.isBeforeFirst()) {
						Function.getAlert(2, "트레이너이름 중복확인", "중복된 트레이너이름 입니다", "다른 트레이너이름을 입력해주세요");
					}else {
						Function.getAlert(2, "트레이너이름 중복확인", "사용가능한 트레이너이름 입니다", "");
					}
				} catch (Exception e3) {
					Function.getAlert(4, "error", "정보를 입력해주세요", "확인후 다시시도 해주세요.");
				}
			});
			//바꾸기 버튼에 대한 이벤트 등록
			btnChangeName.setOnAction(event-> {
				Connection con = null;
				PreparedStatement ppsm = null; 
				try {
					con = DBUtil.getConnection();
					String query = "update userTBL set userNickName = ? where userID = ? ";
					ppsm = con.prepareStatement(query);
					ppsm.setString(1, nickNamefield.getText());
					ppsm.setString(2, RootController.userLogin.getUserID());
					int value = ppsm.executeUpdate();
					if (value != 0 ) {
						Function.getAlert(2, "성공", "닉네임 변경 성공 ", "새로운 닉네임을 확인해주세요.");
						labelTrainer.setText(nickNamefield.getText());
						ChangeName.close();
					}
				} catch (Exception e1) {
					Function.getAlert(4, "error", "닉네임 변경 실패 ", "확인후 다시시도 해주세요.");
				}
			});
			Scene s = new Scene(cn);
			ChangeName.setScene(s);
			ChangeName.setResizable(false);
			ChangeName.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

}
