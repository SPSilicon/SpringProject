package com.hig.hwangingyu.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hig.hwangingyu.domain.Stream;

public interface StreamRepository {
    public Optional<Stream> findbyStreamer(String streamer);
    public Page<Stream> findAll(Pageable Page);
    public Boolean delete(String streamer);
    public void registStream(String streamer);
    public Page<Stream> searchByStreamer(Pageable Page, String streamer);
}
