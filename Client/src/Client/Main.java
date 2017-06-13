package Client;
/*
* Class name: Main.java
* Author:@ Wing Yu Leung 山东大学软件工程八班 梁咏瑜
* 
* 该类为程序主入口，
* 用于加载并显示登陆界面。
* 
*/


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.InetAddress;

public class Main extends Application {


	   public static void main(String[] args) {
	        launch(args);
	    }

    @Override
    public void start(Stage primaryStage) {
    	try{
    		
    		Main t=new Main();
    		InetAddress address=InetAddress.getLocalHost(); //获取本机地址
			System.out.println(address);
    		
			  FXMLLoader fxmlLoader = new FXMLLoader(t.getClass().getResource("LoginFrm.fxml"));
          	 Parent root=fxmlLoader.load(); //加载登陆界面
  
    	Scene scene = new Scene(root,318,444); //新建界面
    	
        primaryStage.setScene(scene);  //将界面设置入舞台
        
        scene.getStylesheets().add(
        		t.getClass().getResource("LoginFrm.css").toExternalForm()); //给登陆界面载入css样式
        
        
    	primaryStage.setTitle("Let us chat now!"); //设置登陆程序的标题
    	primaryStage.setResizable(false); //设置不能够改变程序窗口大小
    	primaryStage.show(); //显示舞台
  

        
     
    	}catch (IOException e) {
			e.printStackTrace();
		} 
    	

    }




 
}
