package Client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

/*
* Class name: VoicePlaybacks.java
* Author:@ Wing Yu Leung 山东大学软件工程八班 梁咏瑜
* 
* 该类用于录制音频
* 
*/


public class VoiceRecorder extends VoiceUtil{
	
	public static void recordAudio(){
		try{
			final AudioFormat format=getAudioFormat();//定义音频格式
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			//通过指定的格式构造目标数据管道的信息对象
			final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
			//接受调音台通过麦克风获取的音频数据，并且写入目标数据管道
            line.open(format);//以特定格式开启管道，使得这个管道能够接受源代码并且变得可被操作
            line.start();//让该管道能够在IO流里被操作
            
            Runnable runner = new Runnable() {//新建线程
                int bufferSize = (int)format.getSampleRate() * format.getFrameSize();//定义缓冲大小
                byte buffer[] = new byte[bufferSize]; //新建字节数组

                public void run() {
                    out = new ByteArrayOutputStream();
                    isRecording = true;
                    try {
                        while (isRecording) {
                            int count = line.read(buffer, 0, buffer.length);
                            if (count > 0) { //判断读入的音频数据长度是否有效
                                out.write(buffer, 0, count); //写入音频数据
                            }
                        }
                    } finally {
                        try {
                            out.close(); //关闭字节数组输出流
                            out.flush();//强制冲刷缓冲区数据
                            line.close(); //关闭音频数据管道
                            line.flush();
                           ConnectServer.sendVoiceMessage(out.toByteArray());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            
            Thread recordThread = new Thread(runner);
            recordThread.start();
            
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " );
            e.printStackTrace();
        }
    }

		
	}


