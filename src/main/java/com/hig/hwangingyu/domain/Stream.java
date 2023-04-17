package com.hig.hwangingyu.domain;

public class Stream {
    String streamName;
    String streamer;


    public Stream(String streamName, String streamer) {
        this.streamName = streamName;
        this.streamer = streamer;
    }

    public String getStreamName() {
        return streamName;
    }
    public String getStreamer() {
        return streamer;
    }
    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }
    public void setStreamer(String streamer) {
        this.streamer = streamer;
    }

    public static class Builder {
        private String streamName;
        private String streamer;
        
        public Builder() {
        }

        public Builder setStreamName(String streamName) {
            this.streamName = streamName;
            return this;
        }

        public Builder setStreamer(String streamer) {
            this.streamer = streamer;
            return this;
        }

        public Stream build() {
            return new Stream(streamName,streamer);
        }
    }
}
