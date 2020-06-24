package controller;

import java.io.File;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import model.Notice;
import model.PoketmonBook1;
import model.User;

public class MainRootController implements Initializable {
	// fx:id ��ġ ��Ű��
	@FXML
	private ImageView imgTrainer;
	@FXML
	private ImageView imgPoket;
	@FXML
	private Label labelTrainer;
	@FXML
	private Button btnRandomPoket;
	@FXML
	private Button btnRandomName;
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
	public User userInfo;
	PoketmonBook1 pkmBook1;
	public int tableViewIndex;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// �̺�Ʈ ����ϱ�
		// ȭ�鿡 �����г��� ���� �����ִ� �Լ�
		loadUserInfomation();
		// DB�� �ִ� ������ ��������
		totalLoadList();
		// ���� �����̴� �̺�Ʈ ���
		labelMoveAction();
		// �����ͺ��̽��� �ִ� ���������� �����ִ� �Լ�
		showNotice();
		// ���� ��������� �̺�Ʈ ��� �� �ڵ鷯 �Լ� ó��
		labelNotice.setOnMouseClicked(e -> handlelblNoticeAction(e));
		// ���ϸ󿵻��� ������ ��쿡 ���� �̺�Ʈ
		btnToday.setOnAction(e -> handleBtnTodayAction(e));
		// ���ϸ� ������ ������ ��쿡 ���� �̺�Ʈ
		btnBook.setOnAction(e -> handleBtnBookAction(e));
		// �������� �����ϴ� ���� �����("C:\icons")
		setSaveIcons();
		// ȭ�鿡 ���� ���ϸ��� �����ִ� �̺�Ʈ ��� �� �ڵ鷯 �Լ�ó��
		btnRandomPoket.setOnAction(e -> loadRamdomPhoto(e));
		
		
	}


	// ȭ�鿡 �����г��� ���� �����ִ� �Լ�
	private void loadUserInfomation() {
		labelTrainer.setText(RootController.userLogin.getUserNickName());
		imgTrainer.setImage(new Image("file:/C:/userimages/" + RootController.userLogin.getUserImage()));
	}

	// DB�� �ִ� �� �������� ����Ʈ
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

	// �������� ����Ǵ� ��ġ ���� �Լ�
	public void setSaveIcons() {
		saveIcons = new File("C:/icons");
		if (!saveIcons.exists()) {
			// ������ ����� �Լ�
			saveIcons.mkdir();
			System.out.println("C:/icons ���丮 ���� ����");
		}

	}

	// ���� Ŭ������ ��� �ڵ鷯
	private void handlelblNoticeAction(MouseEvent e) {
		// ��Ÿ�� ����
		Stage noti = new Stage(StageStyle.UTILITY);
		// ��ް� ��޸��� ����
		noti.initModality(Modality.WINDOW_MODAL);
		// �������� ����
		noti.initOwner(stage);
		Parent p = null;
		try {
			p = FXMLLoader.load(getClass().getResource("/view/cmdpassword.fxml"));
		} catch (IOException e1) {
		}
		// �̺�Ʈ ����� ���� ��ü�� �����´�.
		TextField pwField1 = (TextField) p.lookup("#pwField1");
		Button btnCmdLogin = (Button) p.lookup("#btnCmdLogin");
		Button btnCmdClose = (Button) p.lookup("#btnCmdClose");
		// �̺�Ʈ ���
		// ��� ��ư �̺�Ʈ
		btnCmdClose.setOnAction(event -> noti.close());
		// �α��� ��ư �̺�Ʈ
		btnCmdLogin.setOnAction(event -> {
			if (pwField1.getText().equals("123456")) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("�α��� ����");
				alert.setHeaderText("�����ڴ� ȯ���մϴ�.");
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
				// �̺�Ʈ ����ϱ� ���� ��ü �޾ƿ���
				TextField cmdtxtFieldNotice = (TextField) pCmd.lookup("#txtFieldNotice");
				Button btnRegister = (Button) pCmd.lookup("#btnRegister");
				Button btnCancel = (Button) pCmd.lookup("#btnCancel");
				// ��ҹ�ư �̺�Ʈ
				btnCancel.setOnAction(event2 -> {
					cmd.close();
					noti.close();
				});
				// ��� ��ư �̺�Ʈ
				btnRegister.setOnAction(event2 -> {
					Connection con = null;
					PreparedStatement ppsm = null;
					try {
						con = DBUtil.getConnection();
						String query = "update noticeTBL set notice = ?";
						// ������ ������ �غ�
						ppsm = con.prepareStatement(query);
						// �� ��ġ��Ű��
						ppsm.setString(1, cmdtxtFieldNotice.getText());
						int value = ppsm.executeUpdate();
						if (value != 0) {
							Function.getAlert(3, "���� �Ϸ�", "�������� �����Ϸ�", "����� ���� Ȯ�ο��");
							labelNotice.setText(cmdtxtFieldNotice.getText());
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
					cmd.close();
					noti.close();
				});

				Scene sCmd = new Scene(pCmd);
				cmd.setScene(sCmd);
				cmd.show();
			} else {
				Function.getAlert(2, "��й�ȣ ����", "��й�ȣ�� Ʋ��", "��й�ȣ Ȯ�ο��!!!!");
				pwField1.clear();
			}
		});
		Scene s = new Scene(p);
		noti.setScene(s);
		noti.setResizable(false);
		noti.show();
	}

	// ���� �����̴� �Լ�
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

	// ȭ�鿡 DB�� �ִ� ���������� �����ִ� �Լ�
	private void showNotice() {
		Connection con = null;
		PreparedStatement ppsm = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			// ������ ������ ����
			String query = "SELECT notice FROM noticeTBL";
			// �������� ������ �غ�
			ppsm = con.prepareStatement(query);
			// ������� �޴´�.
			rs = ppsm.executeQuery();
			// �װ��� ArrayList�� �̿��� �޴´�.
			Notice n = null;
			while (rs.next()) {
				n = new Notice(rs.getString(1));
			}
			labelNotice.setText(n.getNotice());
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
	}

	// ���ϸ󿵻��� ������ ��쿡 ���� �Լ�
	private void handleBtnTodayAction(ActionEvent e) {
		Stage pM = new Stage(StageStyle.UTILITY);
		// ��� or ��޸���
		pM.initModality(Modality.WINDOW_MODAL);
		// �������� ����
		pM.initOwner(stage);
		// fxml ���� ��������
		Parent p = null;
		try {
			p = FXMLLoader.load(getClass().getResource("/view/media.fxml"));
			Scene s = new Scene(p);
			// �̺�Ʈ ����� ���� ��ü ��������
			MediaView mdvRandom = (MediaView) p.lookup("#mdvRandom");
			Button btnRandomExit = (Button) p.lookup("#btnRandomExit");
			Button btnReview = (Button) p.lookup("#btnReview");
			// �̵�� ����
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
			pM.setTitle("���ϸ� �̴ϱ���");
			pM.show();

		} catch (IOException e1) {

		}
	}

	// ���ϸ󵵰��� ������ ��쿡 ���� �Լ�
	private void handleBtnBookAction(ActionEvent e) {
		ArrayList<PoketmonBook1> arrayList = new ArrayList<PoketmonBook1>();
		
		// ���� ����
		Stage pkmBook = new Stage(StageStyle.UTILITY);
		// ��� or ��޸���
		pkmBook.initModality(Modality.WINDOW_MODAL);
		// ��������
		pkmBook.initOwner(stage);
		// fxml ���� �ҷ�����
		Parent p = null;
		try {
			p = FXMLLoader.load(getClass().getResource("/view/illustrated.fxml"));
		} catch (IOException e1) {
		}
		Scene s = new Scene(p);
		// �̺�Ʈ ������ ���� ��ü ��������
		TableView tblBook = (TableView) p.lookup("#tblView");
		Button btnOut = (Button) p.lookup("#btnIllustratedCencle");
		
		// �̺�Ʈ �����ϱ�
		// ���ư��� ��ư�� ���� �̺�Ʈ ����
		btnOut.setOnAction(event -> pkmBook.close());
		// �÷��� �����ϴ� �Լ�
		// ���� �÷��� ���� ���� �Է�
		// �̸����� �� ������ ������ �𵨰� ��Ī��Ű��
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
		// DB ����
		BookDAO bookDAO = new BookDAO();
		arrayList = bookDAO.getTotalLoadList();

		for (int i = 0; i < arrayList.size(); i++) {
			PoketmonBook1 poketmonBook = arrayList.get(i);
			obsPkmiList.add(poketmonBook);
		}
		tblBook.setItems(obsPkmiList);
		obsPkmiList.clear();
		totalLoadList();
		// ���̺� �並 ����������� �̺�Ʈ ��� �� �ڵ鷯 �Լ�ó��
		tblBook.setOnMousePressed(event-> {
			tableViewIndex=tblBook.getSelectionModel().getSelectedIndex();
		});
		// ���̺�並 ����Ŭ��������쿡 �̺�Ʈ
		tblBook.setOnMouseClicked(event -> {
			Stage stageBook = new Stage(StageStyle.UTILITY);
			Parent book = null;
			
			if (event.getClickCount() != 2) {
				return;
			}
			try {
				// ȭ�� �ҷ�����
				book = FXMLLoader.load(getClass().getResource("/view/poketmonbook.fxml"));
				// ��� �������� ������� �����ϱ�
				stageBook.initModality(Modality.WINDOW_MODAL);
				// ���� ���� ����
				stageBook.initOwner(pkmBook);
				// �̺�Ʈ ������ ���� ��ü�� ��������
				Label lblInforType1 = (Label) book.lookup("#lblInforType1");
				Label lblInforType2 = (Label) book.lookup("#lblInforType2");
				Label lblInforHeight = (Label) book.lookup("#lblInforHeight");
				Label lblInforWeight = (Label) book.lookup("#lblInforWeight");
				Label lblInforNum = (Label) book.lookup("#lblInforNum");
				Label lblInforName = (Label) book.lookup("#lblInforName");
				ImageView lblInforImage = (ImageView) book.lookup("#lblInforImage");
				TextArea imgInformation = (TextArea) book.lookup("#imgInformation");
				Node tabStatus = book.lookup("#tabStatus");
				Node tabEvolve = book.lookup("#tabEvolve");
				Node tabType = book.lookup("#tabType");
				XYChart statusXYChart = (XYChart) book.lookup("#statusXYChart");
				ImageView imgInforEvolve = (ImageView) book.lookup("#imgInforEvolve");
				// �̺�Ʈ ����
				BookDAO bookDAO2 = new BookDAO();
				ArrayList<PoketmonBook1>arrayList1 = bookDAO2.getPoketmonBookLoadList();
				for(int i = 0; i < arrayList1.size(); i++ ) {
					PoketmonBook1 poketmonBook = arrayList1.get(i);
					obsPkmiList.add(poketmonBook);
				}
				System.out.println(obsPkmiList.get(tableViewIndex).getImage2());
				//�̺�Ʈ����
				lblInforNum.setText(String.valueOf(obsPkmiList.get(tableViewIndex).getNo()));
				lblInforName.setText(obsPkmiList.get(tableViewIndex).getName());
				lblInforImage.setImage(new Image("file:/C:/poketmon/"+obsPkmiList.get(tableViewIndex).getImage2()));
				imgInformation.setText(obsPkmiList.get(tableViewIndex).getInfo());
				lblInforType1.setText(obsPkmiList.get(tableViewIndex).getType1());
				lblInforType2.setText(obsPkmiList.get(tableViewIndex).getType2());
				lblInforHeight.setText(obsPkmiList.get(tableViewIndex).getHeight());
				lblInforWeight.setText(obsPkmiList.get(tableViewIndex).getWeight());
				
				
				
				Scene bookS = new Scene(book);
				stageBook.setScene(bookS);
				stageBook.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		pkmBook.setScene(s);
		pkmBook.setTitle("���ϸ󵵰�");
		pkmBook.setResizable(false);
		pkmBook.show();

	}
	// �������ϸ������ ��Ʈ�� �����ִ� �Լ�
	// ȭ�鿡 ���� ���ϸ��� �����ִ� �Լ�
	private void loadRamdomPhoto(ActionEvent e) {
		BookDAO bookDAO = new BookDAO();
		ArrayList<PoketmonBook1> arrayList = bookDAO.getRandomJoinLoadList();
		Random random = new Random();
		int ran = random.nextInt(arrayList.size());
		PoketmonBook1 Random = arrayList.get(ran);
		
		//���� ���� �̺�Ʈ
		imgPoket.setImage(new Image("file:/C:/poketmon/"+Random.getImage()));
		//��ġ���� ��Ʈ �̺�Ʈ
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

	

}
