package Client;

/*
* Class name: ChatController.java
* Author:@ Wing Yu Leung 山东大学软件工程八班 梁咏瑜
* 
* 该类用来控制主聊天面板的组件和定义一些行为
* 
*/

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import shared.Message;

public class ChatController implements Initializable {

	
    private ConnectServer connect;

    @FXML
    private TextArea messageBox=new TextArea();

    @FXML
   // 定义聊天面板中的JAVAFX组件
    private TextFlow chatArea;
    
    @FXML
    private Button sendButton;
    
    @FXML
    private Button uploadButton;
    
    @FXML
    private Button downloadButton;
    
    @FXML
    private Button recordButton;
    
    @FXML
    public Label nameBox;
    
    @FXML
    public Button showUsersButton;
    
    @FXML
    public Button imageButton;
    
    @FXML
    private Button emojiButton;
    
    @FXML
    private Button drawButton;
    
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");

    private final Desktop desktop=Desktop.getDesktop();
    
    public static Stage emojiStage;
    public Button [] emojis=new Button[Emoji.emoji.length]; 
    //新建数组用于存放表情按钮
    
    public String chatLog="";
    public String path;
    BufferedReader bufferedReader;
   
    {
        messageBox.setStyle(
        		//对messageBox设置css样式
    			"-fx-font-family:\"OpenSansEmoji\";");
        
    for(int i=0;i<Emoji.emoji.length;i++){
    	
		emojis[i] = new Button(Emoji.emoji[i]); 
		//把Emoji类的各个Emoji表情一一对应储存入数组中
		emojis[i].setPrefSize(70, 70);
		emojis[i].setStyle(  
				//对每个emoji表情按钮设置css样式
				"-fx-font-size:25;"
				+ "-fx-font-family:\"OpenSansEmoji\";"
				+ "-fx-background-color:white;"	
						);
		emojis[i].setOnAction((event)->{ 
			//对每一个表情按钮添加点击触发事件
			messageBox.appendText(((Button) event.getSource()).getText());
			//获得按钮中的该表情，并在信息输入框内显示
	});
	}
    

    
	emojiStage=new Stage(); 

}
    
    public String usrs=null;
    //一组用户名
    public String[] users=null;
    
    public void receiveUsr(String usrs){
    this.usrs=usrs;    	
    }
    
 
    @FXML
    public ListView<String> userList= new ListView<>();
    
    
    @FXML
    public void showUsersAction(ActionEvent e)throws IOException{
    	
    	Message message=new Message();
    	message.setType(Message.ONLINEUSERS); 
    	connect.sendMessage(message);
    	//给服务器发送一个要求显示在线列表的消息
    	
    	if(usrs!=null){ 
    		//当经过服务器及其他相关类的处理后，生成并返还一个在线用户列表字符串

    	users=usrs.split(","); 
    	//将字符串用逗号隔开，生成一个字符串数组。
        
        try{
        
      ObservableList<String> userNames = FXCollections.observableArrayList(users);
    	 userList.setItems(userNames);
    	 }
        catch (Exception e2) {
			e2.printStackTrace();
		}
        
    	}
    	}
    
    
    @FXML
    public void drawAction(ActionEvent e){
    	
    	Stage stage=new Stage();
    	Draw draw=new Draw();
    	draw.start(stage);
    	
    }
    
    
   @FXML
   public void recordAction(ActionEvent e)throws IOException{ 

	   VoiceUtil.setRecording(true); 
	   //设置录音状态为true
	   
	   if(VoiceUtil.isRecording()){
	   
        	    recordButton.setTextFill(Color.RED); 
        	    //进入录音状态，改变字色
        	    recordButton.setText("recording");   
        	    //改变按钮文本，提示正在recording
                VoiceRecorder.recordAudio(); 
                //调用recordAudio()函数开始录音
	   
	   }
	   
       }
   
   @FXML 
   public void openLogAction(ActionEvent e){
	   
	   Stage stage =new Stage();
	   
	   try {
		   
		   File log=new File(path); //通过路径载入文件
		   
		bufferedReader=new BufferedReader(new FileReader(log)); 
		//用BufferedReader读入文件
		   String currentLine="";
		   String fileContents="";
		   
		   try {
			   
			while(bufferedReader.readLine()!=null){ 
				currentLine=bufferedReader.readLine();
				   fileContents+=currentLine+"\n";
				   //判断当所读文件不为空的时候，不断添加文件的行字符串
			   }
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		   
		   TextArea chatLog=new TextArea(); //新建文本域
		   chatLog.setText(fileContents); //将聊天记录文本加载入文本域
		   FlowPane flowPane=new FlowPane();
		   flowPane.getChildren().add(chatLog); //给flowPane添加子类
		   
		   chatLog.setPrefSize(400, 500);
		   flowPane.setPrefSize(400, 500); //设置窗口预大小
		   Scene scene=new Scene(flowPane, 400, 500);
		   stage.setScene(scene); //给舞台设置场景
		   stage.setTitle("chat log");
		  // stage.show(); //显示新窗口
		 
		   try {
			desktop.open(log);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		   
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}
	   
   }
   
   @FXML
   public void saveAction(ActionEvent e)throws IOException{
	   
	    Stage stage=new Stage();
	    FileChooser fileChooser =new FileChooser(); 
	    //新建文件选择器
	    
	    fileChooser.getExtensionFilters().add(
	    		new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt")); 
	    //过滤文件的类型，设置只能储存到txt文件中
	    
	    fileChooser.setTitle("Saving chat records");
	    
	    File file=fileChooser.showOpenDialog(stage); 
	    //实例化一个File对象
	     path=file.getAbsolutePath();
	     //获得绝对路径
	    
	    saveFile(chatLog, file);
	   
   }
   
   public void saveFile(String text,File file){

	   try{
		   
		   FileWriter fileWriter;
		   fileWriter=new FileWriter(file);
		   fileWriter.write(text);
		   fileWriter.close();
		   //调用FileWriter来写入文件
		   
	   }catch (IOException e) {
		e.printStackTrace();
	}
   }
   
   
   
   @FXML
   public void stopAction(ActionEvent e)throws IOException{
	   
	   VoiceUtil.setRecording(false); 
	   //设置录音状态为false
	   recordButton.setText("♪record"); 
	   //退出录音状态，恢复原文本
	   recordButton.setTextFill(Color.web("#8c8c8c")); 
	   //恢复原字色
   }


    @FXML
    public void sendAction(ActionEvent e) throws IOException{  
    	//发送文本信息
    	
    	if (!messageBox.getText().isEmpty()) {
    		//判断信息输入框内容是否为空
        connect.sendMessage(new Message(Message.MESSAGE, messageBox.getText()));
        //设置message类型，获取信息输入框中的文本，
        //通过connect对象调用ConnectServer类中的sendMessage
        messageBox.clear(); 
        //清除信息输入框中的文本
    	}
    	
    	}
  
    @FXML
    public void imageAction(ActionEvent e)throws IOException{
    Stage stage=new Stage();
    FileChooser fileChooser =new FileChooser(); 
    //新建文件选择器
    fileChooser.getExtensionFilters().add(
    		new FileChooser.ExtensionFilter("JPG", "*.jpg","png files (*.png)", "*.png")); 
    //过滤文件的类型，设置只能发送jpg文件
    File file=fileChooser.showOpenDialog(stage); 
    //实例化一个File对象储存所选择的文件
    
    connect.sendImage(file);
    //将图片文件通过调用ConnectServer类中的sendImage函数发送出去
    
    }
    
    
    @FXML
    public void emojiAction(ActionEvent e) throws IOException{
    
   FlowPane pane=new FlowPane();
   //新建一个流动面板
   for(int i=0;i<Emoji.emoji.length;i++){ 
	   pane.getChildren().add(emojis[i]); 
	   //给面板添加emoji按钮
   }
   Scene emojiScene=new Scene(pane,560,420);
   //新建secene
   emojiStage.setScene(emojiScene);
   //设置secene
   emojiStage.setTitle("Emoji ChoiceBox");
   //设置窗口标题
   emojiStage.setResizable(false); 
   //设置不能改变窗口大小
   emojiStage.show();
    	
    }
    	
    


    public void setConnection(ConnectServer conn) {
        connect = conn;
    }

    public void appendMsg(String msg) { 
    	
    	//chatArea.appendText(msg);
    	try{
       Text text=new Text(msg);
       text.setStyle("-fx-font-family:\"OpenSansEmoji\";"); 
       //设置字体
       text.setFill(Color.web("#999999"));
       //设置字色
       
       chatLog+=msg+"\n";
       
       Platform.runLater(() -> {
       chatArea.getChildren().add(text);
       //给聊天面板中的聊天区域添加文本信息内容
       });}catch (Exception e) {
		e.printStackTrace();
	}
       
    }
    
  public void appendPicture(Image image) { 
    	
    	try{
    		
       ImageView imageView=new ImageView(image); 
       //实例化ImageView组件并将image传入
       
       Platform.runLater(() -> {   
       chatArea.getChildren().add(imageView);
       //给聊天面板中的聊天区域添加图片信息内容
       
       });}catch (Exception e) {
		e.printStackTrace();
	}
       
    }
    
  public void appendSender(String msg) { 
    	
    	//chatArea.appendText(msg);
    	try{
    		
       String time=simpleDateFormat.format(new Date());
       Text text=new Text("  "+time+msg);
       text.setFill(Color.web("#db7272"));
       chatLog+=time+msg;
       //设置字色
       
       Platform.runLater(() -> {
       chatArea.getChildren().add(text);
       //给聊天面板中的聊天区域添加信息发送人昵称
       });}catch (Exception e) {
		e.printStackTrace();
	}
       
    }



	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}

}



