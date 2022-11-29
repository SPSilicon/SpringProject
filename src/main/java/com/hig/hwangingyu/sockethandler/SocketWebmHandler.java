package com.hig.hwangingyu.sockethandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.lang.Process;
import java.lang.ProcessBuilder.Redirect;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

public class SocketWebmHandler extends BinaryWebSocketHandler {
    private final ClassLoader classLoader = getClass().getClassLoader();
    private Process ffmpegProcess;
    private File log;
    private File streamFolder;

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // TODO Auto-generated method stub
        super.handleTransportError(session, exception);

        if(ffmpegProcess.isAlive()) {
            ffmpegProcess.destroy();
        }

        if(streamFolder.exists()) {
            deleteDir(streamFolder);
        }

        System.out.println("error!!" + exception.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // TODO Auto-generated method stub
        super.afterConnectionClosed(session, status);

        if(ffmpegProcess.isAlive()) {
            ffmpegProcess.destroy();
        }
        
        if(streamFolder.exists()) {
            deleteDir(streamFolder);
        }

        if (log.exists()) {
            log.createNewFile();
        }

        System.out.println("closed!!");
    }

    /* ffmpeg args
            "-i", "-", // webm으로 바꿔보기
             "-filter:v", "tblend", "-r", "60",
            "-flags", "+global_header", "-r", "30000/1001",
            "-pix_fmt", "yuv420p",
            "-c:v", "h264", "-b:v", "4500K", "-bufsize:v:0", "4500K/2",
            "-g:v", "30", "-keyint_min:v", "30", "-sc_threshold:v", "0",
            "-color_primaries", "bt709", "-color_trc", "bt709", "-colorspace", "bt709",
              // video codec config: low latency,
            //"-vf","scale=1920:1080",         // adaptive bitrate
            "-c:a", "aac", "-ar", "44100", "-b:a", "96k", // audio codec config: sampling frequency (11025, 22050,44100), bitrate 64 kbits
            "-map", "0:v:0",
            "-map", "0:a:0",
            "-use_wallclock_as_timestamps", "1", // used for audio sync
            "-async", "1", "-use_timeline", "1", "-use_template", "1", "-seg_duration", "1",
            "-window_size", "10", "-streaming", "1", "-ldash", "1", "-update_period", "1",
            "-f", "dash" 
            */
                //  /home/hig/ffmpeg/jetson-ffmpeg/build/ffmpeg/ffmpeg
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub
        
        System.out.println("conn!!");
        String filePath = classLoader.getResource(".").getFile() + "static/stream/"+session.getAttributes().get("streamName");
        String streamerPath = "/home/hig/ffmpeg/jetson-ffmpeg/build/ffmpeg/ffmpeg";

        streamFolder = new File(filePath);
        streamFolder.mkdirs();
        
        String[] argvs = { 
            "-i", "-", // webm으로 바꿔보기
            "-pix_fmt", "yuv420p",
            "-c:v", "h264", "-b:v", "4500K", "-bufsize:v:0", "4500K/2",
            "-g:v", "30", "-keyint_min:v", "30", "-sc_threshold:v", "0",
            "-color_primaries", "bt709", "-color_trc", "bt709", "-colorspace", "bt709",
            "-c:a", "aac", "-ar", "44100", "-b:a", "96k", // audio codec config
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
        List<String> commands = new ArrayList<>();
        commands.add(streamerPath);
        for (String i : argvs) {
            commands.add(i);
        }
        commands.add(filePath + "/test.mpd");
        ProcessBuilder ffmpegProcessBuilder = new ProcessBuilder(commands);
        ffmpegProcessBuilder.redirectErrorStream(true);
        log = new File(filePath + "/logggg.log");
        ffmpegProcessBuilder.redirectOutput(Redirect.appendTo(log));
        ffmpegProcess = ffmpegProcessBuilder.start();
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        // TODO Auto-generated method stub
        super.handleBinaryMessage(session, message);

        ByteBuffer blob = message.getPayload();
        ffmpegProcess.getOutputStream().write(blob.array());
    }

    boolean deleteDir(File dir) {
        File[] isDir = dir.listFiles();
        if(isDir!=null) {
            for(File file : dir.listFiles()) {
                deleteDir(file);
            }
        }


        return dir.delete();
    }

}
