package com.hig.hwangingyu.fake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.hig.hwangingyu.domain.Stream;
import com.hig.hwangingyu.repository.StreamRepository;

public class FakeStreamRepository implements StreamRepository{

    static final Map<String,Stream> dataSource = new HashMap<>();

    public FakeStreamRepository() {}
    @Override
    public Boolean delete(String streamer) {
        dataSource.remove(streamer);
        return true;
    }

    @Override
    public Page<Stream> findAll(Pageable page) {
        List<Stream> ret = new ArrayList<>(dataSource.values());
        // TODO Auto-generated method stub
        return new PageImpl<Stream>(ret,page,ret.size());
    }

    @Override
    public Optional<Stream> findbyStreamer(String streamer) {
        return Optional.ofNullable(dataSource.get(streamer));
    }

    @Override
    public void registStream(String streamer) {
        Stream input = new Stream(streamer+"'s broadcast",streamer);
        dataSource.put(streamer,input);
        // TODO Auto-generated method stub
        
    }

    @Override
    public Page<Stream> searchByStreamer(Pageable page, String streamer) {
        List<Stream> ret = new ArrayList<>();
        for(Map.Entry<String,Stream> entry : dataSource.entrySet()) {
            if(entry.getKey().contains(streamer)) {
                ret.add(entry.getValue());
            }
        }
        // TODO Auto-generated method stub
        return new PageImpl<Stream>(ret,page,ret.size());
    }
    
    
}
