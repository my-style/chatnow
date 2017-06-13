package shared;
import java.io.Serializable;
import java.util.ArrayList;

/*
* Class name: Message.Java
* Author:@ Wing Yu Leung 山东大学软件工程八班 梁咏瑜
* 
* 创建一个Message类，
* 定义信息的发送者、接收者、信息类型，
* 以及图片和音频的字节信息。
* 
*/

public class Message implements Serializable {


    public static final int MESSAGE = 0;
    public static final int VOICES= 1;
    public static final int ONLINEUSERS = 2;
    public static final int PICTURE = 3;   

    protected static final long serialVersionUID = 1112122200L;
    private int type;
    private String message;
    private String username;
    private String gettername;
    
    private byte[] voiceMsg;
    private byte[] imageMsg;
    
    private ArrayList<User> list;
    private ArrayList<User> users;

    // constructor
    public Message(int type, String message) {
        this.type = type;
        this.message = message;
    }
    
    public ArrayList<User> getList() {
		return list;
	}

	public void setList(ArrayList<User> list) {
		this.list = list;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public Message(){};
    
	public byte[] getImageMsg(){
    	return imageMsg;
    }

    public void setImageMsg(byte[] imageMsg) {
        this.imageMsg=imageMsg;
    }
    

	public byte[] getVoiceMsg(){
    	return voiceMsg;
    }

    public void setVoiceMsg(byte[] voiceMsg) {
        this.voiceMsg = voiceMsg;
    }
    


    // getters
    public int getType() {
        return type;
    }

    public void setType(int type) {
		this.type = type;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setGettername(String gettername) {
		this.gettername = gettername;
	}

	public String getUsername() {
        return username;
    }
    public String getGettername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}