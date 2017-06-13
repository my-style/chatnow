package Server;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shared.Message;
import shared.User;

/*
* Class name: Server.Java
* Author:@ Wing Yu Leung 山东大学软件工程八班 梁咏瑜
* 
* 该类主要设置socket，监听特定端口，创建用户线程，
* 接受每个用户发来的消息，并且转发出去，让所有用户都看见。
* 
*/

public class Server {

    private static int uniqueId;
    private ArrayList<ClientThread> clientThreads;
    private SimpleDateFormat simpleDateFormat;
    private int port;
    private boolean onActive;
    private ArrayList<ObjectOutputStream> writers= new ArrayList<>() ;
    public ArrayList<User>list=new ArrayList<>();

    public Server(int port) { //有参构造函数用于传入用户端口号
        
        this.port = port;
        clientThreads = new ArrayList<>();
        simpleDateFormat=new SimpleDateFormat("HH:mm:ss"); //格式化时间
    }


  
    public static void main(String[] args) {
  
        int portNumber = 8189;

        Server server = new Server(portNumber); 
        server.start();
        
    }

    public void start() {
    	
        onActive = true;
      
        try {
          
            ServerSocket serverSocket = new ServerSocket(port); //创建ServerSocket，监听端口

            while (onActive) {
     
            	 broadcast("Server waiting for Clients on port " + port + ".");
                Socket socket = serverSocket.accept(); //使用accept()阻塞等待客户连接请求
  
                if (!onActive){
                    break;}
                
                ClientThread t = new ClientThread(socket); //实例化ClientThread对象并且传入socket对象
                clientThreads.add(t); 
                t.start();
                
            }

            try {
                serverSocket.close();
                for (int i = 0; i < clientThreads.size(); ++i) {
                    ClientThread tc = clientThreads.get(i);
                    try {
                        tc.sInput.close(); //依次关闭输入流、输出流、socket
                        tc.sOutput.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                        // not much I can do
                    }
                }
            } catch (Exception e) {
                broadcast("Exception closing the server and clients: " + e);
            }
        }

        catch (IOException e) {
            String msg =  " Exception on new ServerSocket: " + e + "\n";
            broadcast(msg);
        }
    }



    protected void stop() {
        onActive = false;

    }



    private synchronized void broadcast(String msg) {
       
        String time = simpleDateFormat.format(new Date()); //获取时间
        String message=time+" " + msg;
   
            System.out.println(message); //在控制台中显示信息
      
    }




    class ClientThread extends Thread { //继承Thread类

        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;

        int id;
        String username;
        Message inputMsg;
        String date;


        ClientThread(Socket socket) { //有参构造函数用于接收Socket对象
       
            id = ++uniqueId;
            this.socket = socket;
  
            try {
      
                sOutput = new ObjectOutputStream(socket.getOutputStream());//创建对象输出流
                sInput = new ObjectInputStream(socket.getInputStream());//创建对象输入流

                writers.add(sOutput); //将每个线程的对象输出流对象都储存进数组中待用
                
                username = (String) sInput.readObject(); //获取用户名
                User user=new User();
                user.setUserName(username);
                list.add(user);
                
                broadcast(username + " just connected.");
                
              
            } catch (IOException e) {
            	 broadcast("Exception creating new Input/output Streams: " + e);
                return;
            }
       
            catch (ClassNotFoundException e) {
            	e.printStackTrace();
            }
            
            date = new Date().toString() + "\n";
        }


        public void run() { //重写父类的run函数
          
        	boolean onActive=true;
        	while (onActive) {
            
                try{
                    	
                 	inputMsg =(Message)sInput.readObject(); //读取对象输入流中的Message对象
                      
                      switch(inputMsg.getType()){ //识别消息类型
                      
                      case Message.MESSAGE:{
                    	  write(inputMsg); 
                       String message = inputMsg.getMessage();
                       broadcast(username+":"+message);	
                       break;}
                      
                      case Message.VOICES:{
                    	  write(inputMsg); 
                    	  broadcast(username+" send a voice message");	
                    	  break;
                      }
                      case Message.PICTURE:{
                    	  write(inputMsg); 
                    	  broadcast(username+" send a image message");	
                    	  break;
                      }
                      case Message.ONLINEUSERS:{
                    	
                    	  Message msg=new Message();
                    	  msg.setType(Message.ONLINEUSERS); 
                   	      msg.setList(list);
                    	  write(msg);
                    	  break;
                      }
				}
                }catch (EOFException e) {
					e.printStackTrace();
				}
					catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}finally{
					
					}
        } 	
                }
 
    

        
  
        private void write(Message msg) throws IOException{
        	
        	 for (ObjectOutputStream writer : writers) { //遍历对象输出流数组对象
        		msg.setMessage(msg.getMessage()); 
        		msg.setUsername(username);
        		try{
        		writer.writeObject(msg); //将Message对象写入对象输出流
        		writer.reset();
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
        	
        }
        
    }
    }
}