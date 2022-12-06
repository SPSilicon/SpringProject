package com.hig.hwangingyu.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

public class FfmpegProcess {

    private final String streamerPath = "/home/hig/ffmpeg/jetson-ffmpeg/build/ffmpeg/ffmpeg";
    private final String filePath = getClass().getClassLoader().getResource(".").getFile() + "static/stream/";
    private Process ffmpegProcess;
    private OutputStream ffmpegInput;
    private File log;
    private File streamFolder;
    private String streamName;

    public FfmpegProcess(String streamName) throws IOException{
        this.streamName = streamName;
        String[] argvs = { 
            "-i", "-", // webm으로 바꿔보기
            "-pix_fmt", "yuv420p",
            "-c:v", "copy",//"h264", "-b:v", "4500K", "-bufsize:v:0", "4500K/2",
            "-g:v", "30", "-keyint_min:v", "30", "-sc_threshold:v", "0",
            "-color_primaries", "bt709", "-color_trc", "bt709", "-colorspace", "bt709",
            "-c:a", "copy",//"aac", "-ar", "44100", "-b:a", "96k", // audio codec config
            "-map", "0:v:0",
            "-map", "0:a:0",
            "-adaptation_sets", "id=0,seg_duration=1.001,streams=v id=1,seg_duration=1.001,streams=a",
            "-use_timeline", "1",
            "-streaming", "1",
            "-window_size", "5",
            //"-frag_type", "every_frame",
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
        streamFolder = new File(filePath+streamName);
        streamFolder.mkdirs();
        log = new File(streamFolder + "/logggg.log");

        List<String> commands = new ArrayList<>();
        commands.add(streamerPath);
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
            if(ffmpegProcess.isAlive()) {
                ffmpegProcess.destroyForcibly();
            }
            
            if(streamFolder.exists()) {
            deleteDir(streamFolder);
            }

            if (log.exists()) {
            log.delete();
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
