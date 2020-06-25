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
	public File selectFile2;
	public File selectFile3;
	private File poketmonImagesFile;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// �̺�Ʈ ����ϱ�
		// ���� �̹����� �����ϴ� ���� ���� �Լ�
		setUserImage();
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
		// ��ǥ�̹��� ��ư �̺�Ʈ ��� �� �ڵ鷯 �Լ�ó��
		btnChangeImage.setOnAction(e -> handleBtnChageImage(e));
		// Ʈ���̳� �̸� ���� ��ư �̺�Ʈ ��� �� �ڵ鷯 �Լ�ó��
		btnChangeName.setOnAction(e -> handleBtnChageName(e));
		// �����ư�� ���� �̺�Ʈ ó��
		btnClose.setOnAction(e -> stage.close());
		// ���������ϸ� �� ������ ��쿡 ���� �̺�Ʈ
		btnMyPoket.setOnAction(e -> handleBtnMyPoketAction(e));
		setPoketmonImagePolderAciton();
	}

	private void setPoketmonImagePolderAciton() {
		poketmonImagesFile = new File("C:/poketmon");
		if (!poketmonImagesFile.exists()) {
			poketmonImagesFile.mkdir();
		}

	}

	private void handleBtnMyPoketAction(ActionEvent e) {
		try {
			// view �� mypoketmon �� ���������â ���� �����ϱ�
			Parent root = FXMLLoader.load(getClass().getResource("/view/mypoketmon.fxml"));
			Scene scene = new Scene(root);
			// �������� ��Ÿ�� ���ϱ�
			Stage myPoketmon = new Stage(StageStyle.UTILITY);
			// ���������� scene �����ϱ�
			myPoketmon.setScene(scene);
			// �������� ���ϱ�
			myPoketmon.initOwner(stage);

			// view�� ��ϵ� �͵� ��������
			ImageView imagebig = (ImageView) root.lookup("#imgMyPkmImage1");
			ImageView imageicon = (ImageView) root.lookup("#imgMyPkmImage2");

			TextField MyPkmName = (TextField) root.lookup("#txtMyPkmName");
			TextField MyPkmSAttack = (TextField) root.lookup("#txtMyPkmSAttack");
			TextField MyPkmSDefense = (TextField) root.lookup("#txtMyPkmSDefense");
			TextField MyPkmDefense = (TextField) root.lookup("#txtMyPkmDefense");
			TextField MyPkmAttack = (TextField) root.lookup("#txtMyPkmAttack");
			TextField MyPkmType1 = (TextField) root.lookup("#txtMyPkmType1");
			TextField MyPkmType2 = (TextField) root.lookup("#txtMyPkmType2");
			TextField MyPkmEvolve = (TextField) root.lookup("#txtMyPkmEvolve");
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
			myPoketmon.setTitle("������ ���ϸ� ���");
			myPoketmon.show();

			btnMyPkImage.setOnAction((event -> {
				// �������� ��ü�������� ����
				FileChooser fileChooser = new FileChooser();
				// �Է°����� �̹��� �������� �Է�
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));

				selectFile3 = fileChooser.showOpenDialog(stage);

				if (selectFile3 != null) {
					try {
						String localURL = selectFile3.toURI().toURL().toString();
						Image image = new Image(localURL, false);
						imagebig.setImage(image);
					} catch (Exception e1) {
					}
				}

			}));

			btnMyPkIcon.setOnAction((event -> {
				// �������� ��ü�������� ����
				FileChooser fileChooser = new FileChooser();
				// �Է°����� �̹��� �������� �Է�
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));

				selectFile2 = fileChooser.showOpenDialog(stage);

				if (selectFile2 != null) {
					try {
						String localURL = selectFile2.toURI().toURL().toString();
						Image image = new Image(localURL, false);
						imageicon.setImage(image);
					} catch (Exception e1) {
						Function.getAlert(2, "error", "���������� �����ü� �����ϴ�.", e1.getMessage());
					}
				}

			}));

			btnMyPkcencle.setOnAction(e7 -> myPoketmon.close());

			// �� txtFile�� ��ĭ�������� return null �� ��ȯ���༭ �ؿ� �ִ� ��Ϲ�ư�� ��ȿȭ��Ŵ

			// ������ ���ϸ� â�� �������� ��ư �̺�Ʈ ���

			btnMyPkJoin.setOnAction(e2 -> {

				if (MyPkmName.getText().trim().equals("") || MyPkmSAttack.getText().trim().equals("")
						|| MyPkmSDefense.getText().trim().equals("") || MyPkmDefense.getText().trim().equals("")
						|| MyPkmAttack.getText().trim().equals("") || MyPkmType1.getText().trim().equals("")
						|| MyPkmType2.getText().trim().equals("") || MyPkmEvolve.getText().trim().equals("")
						|| MyPkmTrait.getText().trim().equals("") || MyPkmSpeed.getText().trim().equals("")
						|| MyPkmHP.getText().trim().equals("") || MyPkmHeight.getText().trim().equals("")
						|| MyPkmWeight.getText().trim().equals("") || MyPkmInfo.getText().trim().equals("")) {
					Function.getAlert(4, "error", "���ϸ���� ������ �������� ������ּ���!", "�Է��� �ٽ� ������ּ���");
					return;
				}

				Connection con = null;
				PreparedStatement pstmt = null;
				String fileName = null;
				String fileName2 = null;

				// ������ �и��ʷ� �ٲپ ������ �����ϴ� �Լ�
				try {

					System.out.println("DDDD");
					fileName = "user" + System.currentTimeMillis() + selectFile3.getName();
					BufferedInputStream bis = null;
					BufferedOutputStream bos = null;

					bis = new BufferedInputStream(new FileInputStream(selectFile3));
					bos = new BufferedOutputStream(
							new FileOutputStream(poketmonImagesFile.getAbsolutePath() + "\\" + fileName));
					int data = -1;
					while ((data = bis.read()) != -1) {
						bos.write(data);
						bos.flush();
					}
				} catch (Exception e6) {
					Function.getAlert(4, "error", "�̹����� ������ּ���!", "error code :" + e6.getMessage());
					return;
				}

				try {
					fileName2 = "user" + System.currentTimeMillis() + selectFile2.getName();
					BufferedInputStream bis = null;
					BufferedOutputStream bos = null;

					bis = new BufferedInputStream(new FileInputStream(selectFile2));
					bos = new BufferedOutputStream(
							new FileOutputStream(saveIcons.getAbsolutePath() + "\\" + fileName2));
					int data = -1;
					while ((data = bis.read()) != -1) {
						bos.write(data);
						bos.flush();
					}
				} catch (Exception e6) {
					Function.getAlert(4, "error", "�̹����� ������ּ���!", "error code :" + e6.getMessage());
					return;
				}

				try {
					con = DBUtil.getConnection();
					String query = "INSERT INTO bookTbl values(null,?,?,?,?,?)";
					pstmt = con.prepareStatement(query);
					// bookTBL �� �Է� �������ֱ�
					pstmt.setString(1, fileName);
					pstmt.setString(2, MyPkmName.getText());
					pstmt.setString(3, MyPkmType1.getText());
					pstmt.setString(4, MyPkmType2.getText());
					pstmt.setString(5, fileName2);

					int returnValue = pstmt.executeUpdate();

				} catch (Exception e1) {
				}

				Connection con2 = null;
				PreparedStatement pstmt2 = null;

				try {
					con = DBUtil.getConnection();
					String query = "INSERT INTO poketmonTBL values(null,?,?,?,?,?,?,?,?,?,?,?)";
					pstmt = con.prepareStatement(query);

					// PoketmonTBL �� �Է� �������ֱ�
					pstmt.setString(1, MyPkmHP.getText());
					pstmt.setString(2, MyPkmAttack.getText());
					pstmt.setString(3, MyPkmDefense.getText());
					pstmt.setString(4, MyPkmSAttack.getText());
					pstmt.setString(5, MyPkmSDefense.getText());
					pstmt.setString(6, MyPkmSpeed.getText());
					pstmt.setString(7, MyPkmTrait.getText());
					pstmt.setString(8, MyPkmHeight.getText() + "m");
					pstmt.setString(9, MyPkmWeight.getText() + "kg");
					pstmt.setString(10, MyPkmEvolve.getText());
					pstmt.setString(11, MyPkmInfo.getText());

					int returnValue = pstmt.executeUpdate();

					if (returnValue == 0) {
					} else {
						Function.getAlert(3, "Success", "���������ϸ� ��� ����", "�������� ������ ���ϸ��� Ȯ���غ�����.");
						myPoketmon.close();
					}

				} catch (Exception e1) {
					Function.getAlert(4, "error", "2�� DB �������!", "error code :" + e1.getMessage());
				}

			});

		} catch (IOException e1) {
			Function.getAlert(4, "error", "������ ���ϸ�â �ҷ����� ����", e1.getMessage());
		}

	}

	// ���� �̹����� �����ϴ� ���� ���� �Լ�
	private void setUserImage() {
		userImagesFile = new File("C:/userImages");
		if (!userImagesFile.exists()) {
			userImagesFile.mkdir();
		}

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
		BookDAO bookDAO = new BookDAO();
		ArrayList<Notice> arrayList = bookDAO.getNoticeLoadList();
		labelNotice.setText(arrayList.get(0).getNotice());
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
		tblBook.setOnMousePressed(event -> {
			tableViewIndex = tblBook.getSelectionModel().getSelectedIndex();
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
				Label lblInforTotal = (Label) book.lookup("#lblInforTotal");
				Button btnInforClose = (Button) book.lookup("#btnInforClose");
				ImageView lblInforImage = (ImageView) book.lookup("#lblInforImage");
				TextArea imgInformation = (TextArea) book.lookup("#imgInformation");
				Node tabStatus = book.lookup("#tabStatus");
				Node tabEvolve = book.lookup("#tabEvolve");
				Node tabType = book.lookup("#tabType");
				XYChart statusXYChart = (XYChart) book.lookup("#statusXYChart");
				ImageView imgInforEvolve = (ImageView) book.lookup("#imgInforEvolve");
				// �̺�Ʈ ����
				// ������ ��ư �̺�Ʈ ����
				btnInforClose.setOnAction(e1 -> stageBook.close());
				BookDAO bookDAO2 = new BookDAO();
				ArrayList<PoketmonBook1> arrayList1 = bookDAO2.getPoketmonBookLoadList();

				// �߰� �ٽ�����
				ObservableList<PoketmonBook1> obsPkmiList = FXCollections.observableArrayList();

				for (int i = 0; i < arrayList1.size(); i++) {
					PoketmonBook1 poketmonBook = arrayList1.get(i);
					obsPkmiList.add(poketmonBook);
				}
				// �̺�Ʈ����
				lblInforNum.setText(String.valueOf(obsPkmiList.get(tableViewIndex).getNo()));
				lblInforName.setText(obsPkmiList.get(tableViewIndex).getName());
				lblInforImage.setImage(new Image("file:/C:/poketmon/" + obsPkmiList.get(tableViewIndex).getImage2()));
				imgInformation.setText(obsPkmiList.get(tableViewIndex).getInfo());
				lblInforType1.setText(obsPkmiList.get(tableViewIndex).getType1());
				lblInforType2.setText(obsPkmiList.get(tableViewIndex).getType2());
				lblInforHeight.setText(obsPkmiList.get(tableViewIndex).getHeight());
				lblInforWeight.setText(obsPkmiList.get(tableViewIndex).getWeight());
				// tabStatus �̺�Ʈ����

				XYChart.Series hp = new XYChart.Series();
				hp.setName("HP");
				ObservableList hpList = FXCollections.observableArrayList();

				hpList.add(new XYChart.Data(hp.getName(), Integer.parseInt(obsPkmiList.get(tableViewIndex).getHp())));

				hp.setData(hpList);
				statusXYChart.getData().add(hp);

				XYChart.Series atk = new XYChart.Series();
				atk.setName("Atk");
				ObservableList atkList = FXCollections.observableArrayList();

				atkList.add(
						new XYChart.Data(atk.getName(), Integer.parseInt(obsPkmiList.get(tableViewIndex).getAtk())));

				atk.setData(atkList);
				statusXYChart.getData().add(atk);

				XYChart.Series def = new XYChart.Series();
				def.setName("Def");
				ObservableList defList = FXCollections.observableArrayList();

				defList.add(
						new XYChart.Data(def.getName(), Integer.parseInt(obsPkmiList.get(tableViewIndex).getDef())));

				def.setData(defList);
				statusXYChart.getData().add(def);

				XYChart.Series sAtk = new XYChart.Series();
				sAtk.setName("SAtk");
				ObservableList sAtkList = FXCollections.observableArrayList();

				sAtkList.add(
						new XYChart.Data(sAtk.getName(), Integer.parseInt(obsPkmiList.get(tableViewIndex).getsAtk())));

				sAtk.setData(sAtkList);
				statusXYChart.getData().add(sAtk);

				XYChart.Series sDef = new XYChart.Series();
				sDef.setName("SDef");
				ObservableList sDefList = FXCollections.observableArrayList();

				sDefList.add(
						new XYChart.Data(sDef.getName(), Integer.parseInt(obsPkmiList.get(tableViewIndex).getsDef())));

				sDef.setData(sDefList);
				statusXYChart.getData().add(sDef);

				XYChart.Series speed = new XYChart.Series();
				speed.setName("Spd");
				ObservableList speedList = FXCollections.observableArrayList();

				speedList.add(new XYChart.Data(speed.getName(),
						Integer.parseInt(obsPkmiList.get(tableViewIndex).getSpeed())));

				speed.setData(speedList);
				statusXYChart.getData().add(speed);
				// �� �ɷ�ġ ���� ���ϴ� �Լ�
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
					Function.getAlert(2, "���� ��� ����", "���� ��� ����", "�������� : " + e1.getMessage());
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
		// �̸� ���� �̺�Ʈ
		lblMonName.setText(Random.getName());
		// ���� ���� �̺�Ʈ
		imgPoket.setImage(new Image("file:/C:/poketmon/" + Random.getImage()));
		// ��ġ���� ��Ʈ �̺�Ʈ
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
		ObservableList sDefList = FXCollections.observableArrayList();
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

	// ��ǥ�̹��� ��ư�� ���� �Լ�
	private void handleBtnChageImage(ActionEvent e) {
		// �������� ��ü�������� ����
		FileChooser fileChooser = new FileChooser();
		// �Է°����� �̹��� �������� �Է�
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
				Function.getAlert(2, "error", "�̹����� ������ּ���", e2.getMessage());
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
					Function.getAlert(3, "���� ���� �Ϸ�", "���� ������ �����߽��ϴ�.", "���� Ȯ�� ���");
				} else {
					throw new Exception("������ ���� ����");
				}
			} catch (Exception e1) {
				Function.getAlert(2, "error", "���������� �����ü� �����ϴ�.", e1.getMessage());
			}
		}

	}

	// Ʈ���̳� �̸� ���濡 ���� �Լ�
	private void handleBtnChageName(ActionEvent e) {
		Stage ChangeName = new Stage(StageStyle.UTILITY);
		// modal or none
		ChangeName.initModality(Modality.WINDOW_MODAL);
		// ��������
		ChangeName.initOwner(this.stage);
		// ���� ��������
		Parent cn = null;
		try {
			cn = FXMLLoader.load(getClass().getResource("/view/changename.fxml"));
			// �̺�Ʈ ����� ���� ��ü ��������
			TextField nickNamefield = (TextField) cn.lookup("#nickNamefield");
			Button btnSameCheck = (Button) cn.lookup("#btnSameCheck");
			Button btnChangeName = (Button) cn.lookup("#btnChargeName");
			Button btnChangeClose = (Button) cn.lookup("#btnChangeClose");
			// ��� ��ư�� ���� �̺�Ʈ
			btnChangeClose.setOnAction(event -> ChangeName.close());
			// �ߺ�Ȯ�ο� ���� �̺�Ʈ ���
			btnSameCheck.setOnAction(event -> {
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
						Function.getAlert(2, "Ʈ���̳��̸� �ߺ�Ȯ��", "�ߺ��� Ʈ���̳��̸� �Դϴ�", "�ٸ� Ʈ���̳��̸��� �Է����ּ���");
					} else {
						Function.getAlert(2, "Ʈ���̳��̸� �ߺ�Ȯ��", "��밡���� Ʈ���̳��̸� �Դϴ�", "");
					}
				} catch (Exception e3) {
					Function.getAlert(4, "error", "������ �Է����ּ���", "Ȯ���� �ٽýõ� ���ּ���.");
				}
			});
			// �ٲٱ� ��ư�� ���� �̺�Ʈ ���
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
						Function.getAlert(2, "����", "�г��� ���� ���� ", "���ο� �г����� Ȯ�����ּ���.");
						labelTrainer.setText(nickNamefield.getText());
						ChangeName.close();
					}
				} catch (Exception e1) {
					Function.getAlert(4, "error", "�г��� ���� ���� ", "Ȯ���� �ٽýõ� ���ּ���.");
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
