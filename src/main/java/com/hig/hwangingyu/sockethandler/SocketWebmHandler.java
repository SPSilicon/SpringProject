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
    File log;

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // TODO Auto-generated method stub
        super.handleTransportError(session, exception);
        if (ffmpegProcess != null) {
            ffmpegProcess.destroy();
        }
        System.out.println("error!!" + exception.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // TODO Auto-generated method stub
        super.afterConnectionClosed(session, status);
        if (ffmpegProcess != null) {
            ffmpegProcess.destroy();
        }

        if (log.exists()) {
            log.createNewFile();
        }

        System.out.println("closed!!");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub
        // super.afterConnectionEstablished(session);
        System.out.println("conn!!");
        String filePath = classLoader.getResource(".").getFile() + "static/stream/";
        String ffmpegPath = "ffmpeg";
        String[] argvs = { "-re", "-i", "-", // webm으로 바꿔보기
                "-c:v", "h264", // video codec config: low latency,
                                // adaptive bitrate
                "-c:a", "aac", "-ar", "44100", "-b:a", "64k", // audio codec config: sampling frequency (11025, 22050,
                                                              // 44100), bitrate 64 kbits
                "-y", // force to overwrite
                "-use_wallclock_as_timestamps", "1", // used for audio sync
                "-async", "1", "-use_timeline", "1", "-use_template", "1",
                "-window_size", "10",
                "-f", "dash"
        };
        List<String> commands = new ArrayList<>();
        commands.add(ffmpegPath);
        for (String i : argvs) {
            commands.add(i);
        }
        commands.add(filePath + "test.mpd");
        ProcessBuilder ffmpegProcessBuilder = new ProcessBuilder(commands);
        ffmpegProcessBuilder.redirectErrorStream(true);
        log = new File(filePath + "logggg.log");
        ffmpegProcessBuilder.redirectOutput(Redirect.appendTo(log));
        ffmpegProcess = ffmpegProcessBuilder.start();
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        // TODO Auto-generated method stub
        super.handleBinaryMessage(session, message);

        ByteBuffer blob = message.getPayload();
        ffmpegProcess.getOutputStream().write(blob.array());
    }

}
