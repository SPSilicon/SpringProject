package com.hig.hwangingyu.service;

import java.nio.ByteBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

import java.io.OutputStream;
import java.lang.Process;


import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import com.hig.hwangingyu.utils.FfmpegProcess;


public class SocketWebmHandler extends BinaryWebSocketHandler {

    private static final Map<String, FfmpegProcess> manager = new HashMap<>();

    // TODO ffmpeg 분리
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

        System.out.println("error!!" + exception.toString());
        String streamName = session.getAttributes().get("streamName").toString();
        FfmpegProcess process = manager.get(streamName);
        if(process!=null&&process.isAlive()) {
            while(!process.destroy()) {

            }
            manager.remove(streamName);
        }


        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String streamName = session.getAttributes().get("streamName").toString();
        FfmpegProcess process = manager.get(streamName);

        if(process!=null&&process.isAlive()) {
            while(!process.destroy()) {

            }
            manager.remove(streamName);
        }

        super.afterConnectionClosed(session, status);
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
        System.out.println("conn!!");
        FfmpegProcess process = new FfmpegProcess(session.getAttributes().get("streamName").toString());
        manager.put(process.getStreamName(),process);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        
        String streamName = session.getAttributes().get("streamName").toString();
        ByteBuffer blob = message.getPayload();
        FfmpegProcess process = manager.get(streamName);
        process.getOutputStream().write(blob.array());
        
        super.handleBinaryMessage(session, message);

    }



}
