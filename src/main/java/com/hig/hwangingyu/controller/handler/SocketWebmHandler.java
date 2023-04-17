package com.hig.hwangingyu.controller.handler;

import java.nio.ByteBuffer;


import java.util.HashMap;
import java.io.IOException;
import java.util.Map;


import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.hig.hwangingyu.service.StreamService;
import com.hig.hwangingyu.utils.FfmpegProcess;



public class SocketWebmHandler extends BinaryWebSocketHandler {

    private static final Map<String, FfmpegProcess> manager = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final StreamService streamService;
    

    public SocketWebmHandler(StreamService streamService) {
        this.streamService = streamService;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        

        String streamName = session.getAttributes().get("streamName").toString();
        FfmpegProcess process = manager.get(streamName);
        if(process!=null&&process.isAlive()) {
            process.destroy();
            manager.remove(streamName);
        }
        logger.info(streamName+" error\n "+exception.toString());

        super.handleTransportError(session, exception);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        String streamName = session.getAttributes().get("streamName").toString();
        streamService.deleteStream(streamName);
        FfmpegProcess process = manager.get(streamName);

        if(process!=null&&process.isAlive()) {
            process.destroy();
            manager.remove(streamName);
        }
        logger.info(streamName+" closed");
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String streamName = session.getAttributes().get("streamName").toString();
        logger.info(streamName+" connected");
        streamService.registStream(streamName);
        boolean listen = Integer.parseInt(session.getAttributes().get("listen").toString()) ==1? true : false;
        FfmpegProcess process = new FfmpegProcess(streamName,listen);
        manager.put(process.getStreamName(),process);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        
        String streamName = session.getAttributes().get("streamName").toString();
        ByteBuffer blob = message.getPayload();
        FfmpegProcess process = manager.get(streamName);
        try{
            process.getOutputStream().write(blob.array());
            super.handleBinaryMessage(session, message);
        } catch(Exception e1) {
            try {
                session.close(CloseStatus.SERVER_ERROR);
            } catch(IOException e2) {
                logger.error("session close Error");
                //세션 종료에러
            }
            
        }
        
        
        

    }



}
