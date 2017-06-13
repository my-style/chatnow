package Client;

/*
* Class name: ConnectServer.java
* Author:@ Wing Yu Leung 山东大学软件工程八班 梁咏瑜
* 
* 该类主要用于实现客户端与服务器的连接，设置Socket和创建新的线程
* 实现文字、图片、音频的读取和发送
* 
*/

import javafx.embed.swing.SwingFXUtils;

import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
	import java.io.ObjectInputStream;
	import java.io.ObjectOutputStream;
	import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import shared.Message;
import shared.User;

	public class ConnectServer implements Runnable { //实现Runnable 借口


	    private Socket socket;
	    private static ObjectOutputStream output;
	    private ObjectInputStream input;
	    
	    public ChatController chatController;
	    public LoginController loginController;
	    
	    public int port;
	    public String host;
	    
	    public ArrayList<User>list;

	    public ConnectServer(String host, int port) {
	    	//创建有参构造函数，用于传入ip地址和端口号
	        try {
	        	
	          this.host=host;
	          this.port=port;
	   
	          socket = new Socket(host, port); //向本机为port的端口号发起用户请求
	          output = new ObjectOutputStream(socket.getOutputStream());//由socket对象得到输出流
	          input = new ObjectInputStream(socket.getInputStream()); //由socket对象得到输入流
	          
	        } catch (Exception e) {
	           e.printStackTrace();
	        }
	        }
	    
	    @Override
		public void run() { //重写run()函数
	   							    	
	    	    try{
	    	    	 	
	    	    	while(socket.isConnected()){ //判断socket是否连接上
	    	    		
	    	    		
	    	    	Message message=(Message)input.readObject(); 
	    	    	//读取输入流中的对象 并且实例化一个Message对象
	    	    		
	    	    	if(message!=null){ 
	    	    		//判断message是否为空 如果不为空则执行下列函数
	    	    		
	    	 switch(message.getType()){ //判断message的类型
	    	 
	    	 case Message.ONLINEUSERS:{
	    		 
	    			ArrayList<User>list=message.getList(); //获得消息中的在线用户列表
					
					String usrs = "";
	    			for(User lists:list){
	    				usrs+=lists.getUserName()+","; 
	    				//创建字符串，添加所有在线用户名，用逗号隔开
	    			}
	    			chatController.receiveUsr(usrs);
	    			//将该字符串传递给聊天界面控制器以便显示
	    		 
	    			break;
	    	 }
	    	 
	    	 case Message.MESSAGE:{
	    		 
	    	    		String inputmsg=message.getMessage(); //获取信息内容
	    	    		String sender=message.getUsername(); //获取信息发送人
	    	    		
	    	    		chatController.appendSender("  ["+sender+"]："); 
	    	    		//把信息添加到聊天面板
	    	    		chatController.appendMsg(inputmsg);
	    	    		chatController.appendMsg("\r\n");
	    	    		
	    	    		break;
	    	    	}
	    	 
	    	 case Message.VOICES:{
	    		 
	    		 String sender=message.getUsername();
	    		 chatController.appendSender("  ["+sender+"]：");
	    		 chatController.appendMsg("has sent a voice message"); 
	    		 chatController.appendMsg("\r\n");
	    		 
	    		 VoicePlayback.playAudio(message.getVoiceMsg());
	    		 //调用playAudio()函数 来播放录音
	    		 
	    		 break;
	    	 }
	    	 
	    	 case Message.PICTURE:{
	    		 
	    		 String sender=message.getUsername();

	    		 
	       try{
	    	   
	    	Runnable runner =new Runnable() { 
	    		//给读取图片单独创建一个线程
				
				@Override
				public void run() {

					try {
						
						 ByteArrayInputStream bais; //创建一个字节数组输入流
						 bais=new ByteArrayInputStream(message.getImageMsg()); 
						 //实例化并且把message里的图片信息传入
			    		 BufferedImage bImage;//创建一个缓冲图片对象
					   	 bImage = ImageIO.read(bais);
					   	//读取字节数组输入流的内容，储存为图片在bImage中
					    Image image=SwingFXUtils.toFXImage(bImage, null);
					    //指定BufferedImage并将其副本存储到JavaFX Image对象以便显示
					    
			    		 chatController.appendSender("  ["+sender+"]：");
			    		 chatController.appendMsg("\n"); 
					    chatController.appendPicture(image);//添加图片
			    		 chatController.appendMsg("\n");
			    		 
					} catch (IOException e) {
					e.printStackTrace();
					
					}catch (NullPointerException e) {
				
					}
		    	
				}
						 
	       };
	       
	       Thread playThread = new Thread(runner);//传递给thread构造函数
           playThread.start();//调用start方法启动线程
           
	       }catch (Exception e) {
			e.printStackTrace();
		}
	       break;
	    	 }
	    	    		
	    	    	}
	    	    	}
	    	    	}
	    	    }catch (EOFException e) {
					// TODO: handle exception
				}
	    	 
	    	    catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}finally {
					try {//关闭文件流和socket
						input.close();
						output.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			
		}
	    

	    public void connectChat(ChatController con){ 
	    	//用于传入ChatController的变量
	    	chatController=con;
	    }
	    
	    public void connectLogin(LoginController con){
	    	//用于传入LoginController的变量
	    	loginController=con;
	    }
	    

	    public void sendMessage(Object msg) { 
	    	//发送给普通文本信息，把对象写入对象输出流
	        try {
	            output.writeObject(msg);
	            output.flush();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public static void sendVoiceMessage(byte[] voice) throws IOException{
	    //发送语音信息	
	    Message voicemsg=new Message();
	    voicemsg.setType(Message.VOICES);
	    voicemsg.setVoiceMsg(voice);
	    output.writeObject(voicemsg);
	    output.flush();	
	    
	    }
	    
	    public boolean isConnected() {
	        if (socket != null && socket.isConnected()){
	            return true;
	            }else{
	        return false;}
	    }

	    public ObjectInputStream getInput() {
	        return input;
	    }
	    
	    
	    
	    public static void sendImageMessgae(byte[] image)throws IOException{
	    //发送图片信息	
	    Message imagemsg=new Message();
	    imagemsg.setType(Message.PICTURE);//定义图片类型
	    imagemsg.setImageMsg(image);//储存有关的图片数组信息
	    output.writeObject(imagemsg);//写入对象输出流
	    output.flush();
	    
	    }
	       
	    public void sendImage(File file){
	    	
	   try{
	    	Runnable runnable =new Runnable() {
	    		//给发送图片单独创建一个线程
				
				public void run() {
					// TODO Auto-generated method stub
				    BufferedImage image;
				    ByteArrayOutputStream baos=new ByteArrayOutputStream();
				    byte[]b;
				    
					try {
						   image = ImageIO.read(file); 
						   //读取选取的文件，储存在image中
				    	    ImageIO.write(image, "jpg", baos);//写图片
				    	    b=baos.toByteArray();
				    	    //把字节数组输出流中的有效内容转换为字节数组
				    	    
				    	    sendImageMessgae(b);
				    	    //调用函数，发送图片信息
				    	    
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						try {
							baos.close();
							baos.flush();//冲刷输出流
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			};
	    	
			Thread thread =new Thread(runnable);//传递给thread构造函数
			thread.start();//调用start启动线程
			
	    }catch (Exception e) {
			e.printStackTrace();
		}

		

	}

	};


