package com.hig.hwangingyu.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.slf4j.Log4jLogger;
import org.apache.logging.slf4j.Log4jLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FfmpegProcess {
 // 
    private static final String[] argvs = { 
        "-flags", "+global_header", "-r", "30",
        "-pix_fmt", "yuv420p",
        "-c:v", "h264", "-b:v", "9000K", "-bufsize:v:0", "9000K/2",
        "-g:v", "30", "-keyint_min:v", "30", "-sc_threshold:v", "0",
        "-color_primaries", "bt709", "-color_trc", "bt709", "-colorspace", "bt709",
        "-c:a", "aac", "-ar", "44100", "-b:a", "96k", 
        "-map", "0:v:0",
        "-map", "0:a:0?",
        "-preset", "ultrafast",
        //"-tune", "zerolatency", 지원하면 사용
        "-adaptation_sets", "id=0,seg_duration=1.000,streams=v id=1,seg_duration=1.000,streams=a",
        "-use_timeline", "0",
        "-streaming", "1",
        "-window_size", "3",
        "-frag_type", "every_frame",
        "-ldash", "1",
        "-utc_timing_url", "https://time.akamai.com?iso&amp;ms",
        "-format_options", "movflags=cmaf",
        "-timeout", "0.5",
        "-write_prft", "1",
        "-target_latency", "3.0",
        "-http_user_agent", "Akamai_Broadcaster_v1.0",
        "-http_persistent", "1",
        "-media_seg_name", "chunk-stream_$RepresentationID$-$Number%05d$.$ext$",
        "-init_seg_name", "init-stream_$RepresentationID$.$ext$",
        "-f", "dash",
        
    }; // mpd
    private static final String[] listenOption = {"-listen", "1", "-i"};
    private static final String[] basicOption = {"-i", "-"};
    private static final String[] tcmd = {"-vf","fps=1/10", "-y", "-q:v", "1", "-update", "1"}; //썸네일 출력
    private static final String THUMNAIL_FILE_NAME = "/thumbnail.jpg";
    private static final String LISTENING_ADDRESS = "rtmp://192.168.35.177:1935/stream/";
    private static final String LOG_FILE_NAME = "/ffmpeglog.log";
    private static final String MPD_FILE_NAME = "/test.mpd";
    
    private final String streamerPath = "/home/hig/jetson-ffmpeg/build/ffmpeg/ffmpeg";
    private final String filePath = getClass().getClassLoader().getResource(".").getFile() + "static/stream/vid/";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

        List<String> commands = new ArrayList<>();
        commands.add(streamerPath);
        for(String i : option) {
            commands.add(i);
        }
        if(isListening) commands.add( LISTENING_ADDRESS + streamName);

        for (String i : argvs) {
            commands.add(i);
        }
        commands.add(streamFolder + MPD_FILE_NAME);
    
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
