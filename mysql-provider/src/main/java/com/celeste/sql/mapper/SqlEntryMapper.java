package com.celeste.sql.mapper;

import com.celeste.sql.function.SqlFunction;
import lombok.Builder;
import lombok.Getter;

import java.sql.ResultSet;

@Getter
@Builder
public class SqlEntryMapper<K, V> {

    private final SqlFunction<ResultSet, K> keyMapper;
    private final SqlFunction<ResultSet, V> valueMapper;

    public K transformKey(ResultSet resultSet) {
        return keyMapper.apply(resultSet);
    }

    public V transformValue(ResultSet resultSet) {
        return valueMapper.apply(resultSet);
    }

}

