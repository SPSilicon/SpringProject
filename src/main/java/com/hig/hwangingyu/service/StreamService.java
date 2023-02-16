package com.hig.hwangingyu.service;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hig.hwangingyu.domain.Stream;
import com.hig.hwangingyu.repository.StreamRepository;

public class StreamService {
    
    private final StreamRepository streamRepository;

    public StreamService(StreamRepository streamRepository) {
        this.streamRepository = streamRepository;
    }
    
    public Page<Stream> findAll(Pageable page) {       
        return streamRepository.findAll(page);    
    }

    public Optional<Stream> findByid(String id) {
        return streamRepository.findbyStreamer(id);
    }

    public void registStream(String streamer) {
        deleteStream(streamer);
        streamRepository.registStream(streamer);
        return;
    }

    public boolean deleteStream(String streamer) {
        return streamRepository.delete(streamer);
    }

    public Page<Stream> searchByStreamer(Pageable page,String streamer) {
        return streamRepository.searchByStreamer(page, streamer);
    }
}
