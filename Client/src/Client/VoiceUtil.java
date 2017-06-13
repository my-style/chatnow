package Client;

/*
* Class name: Main.java
* Author:@ Wing Yu Leung 山东大学软件工程八班 梁咏瑜
* 
* 该类用于定义有关录音的信息，
* 包括音频格式以及录音状态
* 
*/


import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;

public class VoiceUtil {
	
	protected static boolean isRecording=false;
	 static ByteArrayOutputStream out; 
	
	public static void setRecording(boolean choice){
		isRecording=choice; //设置录音状态
	}
	
	public static boolean isRecording(){
		return isRecording; //返回录音状态
	}
	
	static AudioFormat getAudioFormat(){
		float sampleRate=16000;// 采样频率
		int sampleSizeInBits=8;// 样本位数
		int channels=2;//双声道
		boolean signed=true;
		boolean bigEndian=true;//大端格式
		
		AudioFormat format =new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		return format; //定义音频格式并且返回
	}

}
