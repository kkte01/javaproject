package model;

public class User {
	private String userID;
	private String userPassword;
	private String userName;
	private String userEmail;
	private String userPhone;
	private String userNickName;
	private String userImage;

	public User(String userID, String userPassword, String userName, String userEmail, String userPhone,
			String userNickName, String userImage) {
		super();
		this.userID = userID;
		this.userPassword = userPassword;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPhone = userPhone;
		this.userNickName = userNickName;
		this.userImage = userImage;
	}

	public User(String userID, String userPassword, String userPhone) {
		this.userID = userID;
		this.userPassword = userPassword;
		this.userPhone = userPhone;
	}

	public User(String userID, String userName, String userEmail, String userPhone) {
		this.userID = userID;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPhone = userPhone;
	}
	
	public User(String userName, String userEmail) {
		this.userName = userName;
		this.userEmail = userEmail;
	}
	//닉네임 바꿀때 쓰는 생성자
	public User(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

}
