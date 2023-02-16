package com.hig.hwangingyu.repository;

import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.hig.hwangingyu.domain.Stream;

public class StreamJdbcRepository implements StreamRepository{

    private final DataSource datasource;
    private final JdbcTemplate jdbcTemplate;
    public StreamJdbcRepository(DataSource dataSource) {
        this.datasource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Page<Stream> searchByStreamer(Pageable page, String streamer) {
        List<Stream> ret = jdbcTemplate.query("select * from streams where streamer LIKE '%"+streamer+"%' limit ? offset ?", streamRowMapper(),page.getPageSize(),page.getOffset());
        return new PageImpl<Stream>(ret,page,getCount());
    }
    @Override
    public Page<Stream> findAll(Pageable page) {

        List<Stream> ret = jdbcTemplate.query("select * from streams limit "+page.getPageSize() + " offset "+page.getOffset(), streamRowMapper());
        return new PageImpl<Stream>(ret, page, getCount());
    }

    @Override
    public Optional<Stream> findbyStreamer(String streamer) {
        jdbcTemplate.query("select * from streams where streamer=?",streamRowMapper(),streamer);
        return Optional.empty();
    }

    @Override
    public Boolean delete(String streamer) {
        int result = jdbcTemplate.update("delete from streams where streamer = ? ",streamer);

        return result>0;
    }

    @Override
    public void registStream(String streamer) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(datasource);
        jdbcInsert.withTableName("streams");
        Map<String,String> att = new HashMap<>();
        att.put("streamName",streamer+"'s broadcast");
        att.put("streamer",streamer);
        jdbcInsert.execute(att);

        return;
    }

    public int getCount() {
        Optional<Integer> ret = Optional.ofNullable(jdbcTemplate.queryForObject("select count(*) from streams",Integer.class));

        return ret.orElse(0);
    }

    public RowMapper<Stream> streamRowMapper() {
        return (rs, row) -> {
            return new Stream.Builder()
            .setStreamName(rs.getString("streamName"))
            .setStreamer(rs.getString("streamer"))
            .build();
        };
    }
    
    
}
