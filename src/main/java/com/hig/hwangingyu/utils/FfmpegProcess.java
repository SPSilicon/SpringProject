package com.hig.hwangingyu.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

public class FfmpegProcess {
 // 
    private static final String[] argvs = { 
        // webm으로 바꿔보기
        "-pix_fmt", "yuv420p",
        "-c:v", "h264", "-b:v", "9000K", "-bufsize:v:0", "9000K/2",
        "-g:v", "60", "-keyint_min:v", "60", "-sc_threshold:v", "0",
        "-color_primaries", "bt709", "-color_trc", "bt709", "-colorspace", "bt709",
        "-c:a", "aac", "-ar", "44100", "-b:a", "96k", // audio codec config
        "-map", "0:v:0",
        "-map", "0:a:0",
        "-adaptation_sets", "id=0,seg_duration=1.001,streams=v id=1,seg_duration=1.001,streams=a",
        "-use_timeline", "1",
        "-streaming", "1",
        "-window_size", "5",
        "-frag_type", "every_frame",
        "-ldash", "1",
        "-utc_timing_url", "https://time.akamai.com?iso&amp;ms",
        "-format_options", "movflags=cmaf",
        "-timeout", "0.1",
        "-write_prft", "1",
        "-target_latency", "4.0",
        "-http_user_agent", "Akamai_Broadcaster_v1.0",
        "-http_persistent", "1",
        "-media_seg_name", "chunk-stream_$RepresentationID$-$Number%05d$.$ext$",
        "-init_seg_name", "init-stream_$RepresentationID$.$ext$",
        "-update_period", "1",
        "-f", "dash"
    };
    private static final String[] listenOption = {"-listen", "1", "-i"};
    private static final String[] basicOption = {"-i", "-"};
    private final String streamerPath = "/home/hig/jetson-ffmpeg/build/ffmpeg/ffmpeg";
    private final String filePath = getClass().getClassLoader().getResource(".").getFile() + "static/stream/";
    private Process ffmpegProcess;
    private OutputStream ffmpegInput;
    private File log;
    private File streamFolder;
    private String streamName;

    public FfmpegProcess(String streamName,boolean isListening) throws IOException{
        this.streamName = streamName;
        String [] option;
        if(isListening) {
            option = listenOption;
        } else {
            option = basicOption;
        }


        streamFolder = new File(filePath+streamName);
        streamFolder.mkdirs();
        log = new File(streamFolder + "/logggg.log");

        List<String> commands = new ArrayList<>();
        commands.add(streamerPath);
        for(String i : option) {
            commands.add(i);
        }
        if(isListening) commands.add("rtmp://192.168.35.177:1935/stream/"+streamName);
        for (String i : argvs) {
            commands.add(i);
        }
        commands.add(streamFolder + "/test.mpd");

        ProcessBuilder ffmpegProcessBuilder = new ProcessBuilder(commands);
        ffmpegProcessBuilder.redirectErrorStream(true);
        
        ffmpegProcessBuilder.redirectOutput(Redirect.appendTo(log));
        
        ffmpegProcess = ffmpegProcessBuilder.start();
        ffmpegInput = ffmpegProcess.getOutputStream(); 
        return;
    }
    public boolean destroy() {
        try {
            while(ffmpegProcess.isAlive()) {
                ffmpegProcess.destroyForcibly();
            }
            
            while(log.exists()) {
                log.delete();
            }

            while(streamFolder.exists()) {
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
