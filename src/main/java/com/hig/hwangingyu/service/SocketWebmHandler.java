package com.hig.hwangingyu.service;

import java.nio.ByteBuffer;


import java.util.HashMap;

import java.util.Map;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import com.hig.hwangingyu.utils.FfmpegProcess;


public class SocketWebmHandler extends BinaryWebSocketHandler {

    private static final Map<String, FfmpegProcess> manager = new HashMap<>();

    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

        System.out.println("error!!" + exception.toString());
        String streamName = session.getAttributes().get("streamName").toString();
        FfmpegProcess process = manager.get(streamName);
        if(process!=null&&process.isAlive()) {
            process.destroy();
            manager.remove(streamName);
        }

        System.out.println("closed!");
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String streamName = session.getAttributes().get("streamName").toString();
        FfmpegProcess process = manager.get(streamName);

        if(process!=null&&process.isAlive()) {
            process.destroy();
            manager.remove(streamName);
        }
        System.out.println("closed!");
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("conn!!");
        boolean listen = Integer.parseInt(session.getAttributes().get("listen").toString()) ==1? true : false;
        FfmpegProcess process = new FfmpegProcess(session.getAttributes().get("streamName").toString(),listen);
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
