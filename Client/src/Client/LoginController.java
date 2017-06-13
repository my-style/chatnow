package Client;

/*
* Class name: LoginController.java
* Author:@ Wing Yu Leung 山东大学软件工程八班 梁咏瑜
* 
* 该类主要用于对登陆界面的组件进行控制
* 把在登陆界面获取的登陆信息发送给服务器，并且建立线程连接
* 
*/

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Main main;
    private ChatController logged;
    private ConnectServer connectServer;
    
    public String username;
    
    @FXML
    public TextField nameField;
    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private Button connectButton;
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	

    @FXML public void connectAction(ActionEvent e){ 
    	try{
    		 LoginController t=new LoginController();
  
      	   FXMLLoader fxmlLoader = new FXMLLoader(t.getClass().getResource("ChatFrm.fxml"));
      	   
 
      	    username=nameField.getText(); //获取用户名
    		 
          connectServer = new ConnectServer(ipField.getText(), Integer.parseInt(portField.getText()));
          //将用户输入的ip和port传给服务器
          
        if (connectServer.isConnected()) {
        	
            connectServer.sendMessage(username);
            //如果链接成功，将用户名传给服务器
       		
        	   Parent chatfrm=fxmlLoader.load(); //加载聊天界面
    			Stage stage =new Stage();
    			 Scene scene = new Scene(chatfrm,552,504);//设置窗口大小
    				
    			stage.setScene(scene);
    			stage.setTitle("Let us chatNow!");// 设置标题
    			stage.setResizable(false); //设置窗口不可调节大小
    			stage.show();//显示窗口
            	
    			  ((Node) e.getSource()).getScene().getWindow().hide();	//隐藏登陆窗口
    			  

    			logged = fxmlLoader.getController(); //获得ChatController中的变量
    			logged.nameBox.setText(username); //对聊天面板中的用户名标签进行设置
    			
    			connectServer.connectChat(logged); 
                logged.setConnection(connectServer);//连接login和chat

                Thread x = new Thread(connectServer);
                x.start(); //调用start方法启动线程
        }else {
			
        	Alert alert =new Alert(Alert.AlertType.ERROR);
        	alert.setTitle("Failed to login");
        	alert.setHeaderText("Oops! Something went wrong !!");
        	alert.showAndWait();
        	
		}
        
        }catch (Exception e1) {
			e1.printStackTrace();
		}
    }





}
