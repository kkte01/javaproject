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
	



	public User(String userID ,String userPassword, String userPhone) {
		super();
		this.userID = userID;
		this.userPassword = userPassword;
		this.userPhone = userPhone;
	}


	public User(String userID, String userName, String userEmail, String userPhone) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPhone = userPhone;
	}




	public User(String userName, String userEmail) {
		super();
		this.userName = userName;
		this.userEmail = userEmail;
	}




	@Override
	public String toString() {
		return "User [userID=" + userID + ", userPassword=" + userPassword + ", userName=" + userName + ", userEmail="
				+ userEmail + ", userPhone=" + userPhone + ", userNickName=" + userNickName + ", userImage=" + userImage
				+ "]";
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
