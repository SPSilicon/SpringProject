package com.hig.hwangingyu.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FfmpegProcess {    
    private static final String[] listenOption = {"-listen", "1", "-i"};
    private static final String[] basicOption = {"-i", "-"}; //
    private static final String[] tcmd = {"-vf","fps=1/10", "-y", "-q:v", "1", "-update", "1"}; //썸네일 출력
    private static final String THUMNAIL_FILE_NAME = "/thumbnail.jpg"; // 썸네일 파일 이름
    private static final String LISTENING_ADDRESS = "rtmp://spsi.kro.kr:1935/stream/"; // rtmp listen 주소
    private static final String LOG_FILE_NAME = "/ffmpeglog.log"; // 로그파일 이름
    private static final String HLS_FILE_NAME = "/vs%v/main.m3u8"; // m3u8 파일 이름
    
    private final String streamerPath = "/home/hig/jetson-ffmpeg/build/ffmpeg/ffmpeg";
    private final String filePath = getClass().getClassLoader().getResource(".").getFile() + "static/stream/vid/";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String[] baseCommand = { 
        "-flags", "+global_header", "-r", "30000/1001",
        "-pix_fmt", "yuv420p",
        "-c:v", "h264", "-b:v:0", "4500K", "-maxrate:v:0", "4500K", "-bufsize:v:0", "4500K/2",
        "-g:v", "30", "-keyint_min:v", "30", "-sc_threshold:v", "0",
        "-color_primaries", "bt709", "-color_trc", "bt709", "-colorspace", "bt709",
        "-c:a", "aac", "-ar", "44100", "-b:a", "96k", 
        "-map", "0:v:0",
        "-map", "0:a:0",
        "-preset", "ultrafast",
        
        //"-tune", "zerolatency",// 지원하면 사용

    }; // mpd

    private Process ffmpegProcess;
    private OutputStream ffmpegInput;
    private File log;
    private File streamFolder;
    private String streamName;

    public FfmpegProcess(String streamName,boolean isListening){
        this.streamName = streamName;
        String [] option;
        if(isListening) {
            option = listenOption;
        } else {
            option = basicOption;
        }

    
        streamFolder = new File(filePath+streamName);
        streamFolder.mkdirs();
        log = new File(streamFolder + LOG_FILE_NAME);

        String[] hlscmd = {                
            "-hls_init_time", "2.002",
            "-hls_time", "2.002",
            "-hls_list_size", "20",
            "-hls_flags", "delete_segments",
            "-strftime", "1",
            "-var_stream_map", "a:0,agroup:a0,default:0 v:0,agroup:a0",
            "-master_pl_name", "test.m3u8",
            "-f", "hls",
            streamFolder + HLS_FILE_NAME
        }; // hls 명령어

        List<String> commands = new ArrayList<>();
        commands.add(streamerPath);
        for(String i : option) {
            commands.add(i);
        }
        if(isListening) commands.add( LISTENING_ADDRESS + streamName);
        
        for (String i : baseCommand) {
            commands.add(i);
        }

        for(String i : hlscmd) {
            commands.add(i);
        }
        
        for(String i : tcmd) {
            commands.add(i);
        }
        commands.add(streamFolder+THUMNAIL_FILE_NAME);
        
        try{
            ProcessBuilder ffmpegProcessBuilder = new ProcessBuilder(commands);

            ffmpegProcessBuilder.redirectErrorStream(true);
        
            ffmpegProcessBuilder.redirectOutput(Redirect.appendTo(log));
            
            ffmpegProcess = ffmpegProcessBuilder.start();
            ffmpegInput = ffmpegProcess.getOutputStream(); 
        } catch(IOException e) {
            logger.error("ffmpegProcessBuild Failed");
        }
        

        return;
    }


    public boolean destroy() {
        try {
            if(ffmpegProcess.isAlive()) {
                ffmpegProcess.destroy();
            }
            
            if(log.exists()) {
                log.delete();
            }

            if(streamFolder.exists()) {
                deleteDir(streamFolder);
            }

        } catch( Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }


    public OutputStream getOutputStream() {
        return ffmpegInput;
    }

    public String getStreamName() {
        return streamName;
    }
    
    public boolean isAlive() {
        return ffmpegProcess.isAlive();
    }
    
    private boolean deleteDir(File dir) {
        File[] isDir = dir.listFiles();
        if(isDir!=null) {
            for(File file : dir.listFiles()) {
                deleteDir(file);
            }
        }
        return dir.delete();
    }


}
