package shared;

import java.io.Serializable;

public class User implements Serializable{ //实现序列化接口
	
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}
