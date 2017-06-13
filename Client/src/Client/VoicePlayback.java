package Client;

/*
* Class name: VoicePlaybacks.java
* Author:@ Wing Yu Leung 山东大学软件工程八班 梁咏瑜
* 
* 该类用于播放传入的音频文件
* 
*/


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class VoicePlayback extends VoiceUtil {
	
    public static void playAudio(byte[] audio) {
        try {
        	
            InputStream input = new ByteArrayInputStream(audio);
            //将字节数据转化为输入流对象
            final AudioFormat format = getAudioFormat(); //定义音频格式
            final AudioInputStream ais;
            ais =new AudioInputStream(input, format, audio.length / format.getFrameSize());
            //创建音频输入流对象
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
          //通过指定的格式构造源数据管道的信息对象
            final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            //数据被写入源代码音频管道，管道接受音频数据可将其传给调音台
            line.open(format);
            //以特定格式开启管道，使得这个管道能够接受源代码并且变得可被操作
            line.start();//让该管道能够在IO流里被操作

            Runnable runner = new Runnable() {//新建一个线程
            	
                int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run() {
                	
                    try {
                        int count;
                        while ((count = ais.read(
                                buffer, 0, buffer.length)) != -1) {
                            if (count > 0) { //判断读取的音频数据长度是否有效
                                line.write(buffer, 0, count); //将音频数据写入调音台
                            }
                        }
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        line.drain(); //所有数据播放后返回
                        line.close();
                    }
                }
            };
            
            Thread playThread = new Thread(runner);//传递给thread构造函数
            playThread.start();//调用start方法启动线程
            
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
