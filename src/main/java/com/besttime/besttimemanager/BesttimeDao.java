package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BesttimeDao {


        private final JdbcTemplate jdbcTemplate;

        @Autowired
        public BesttimeDao(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
    }

    public int playerAdd(PlayerController.playerItem item){
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("player");
        return insert.execute(param);
    }

    public <LIst> List<PlayerController.playerItem> findPlayers(){
        String query = "SELECT * FROM " + "player WHERE playerFlag=true";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<PlayerController.playerItem> list = result.stream().map(
                (Map<String, Object> row) -> new PlayerController.playerItem(
                        row.get("playerId").toString(),
                        row.get("playerName").toString(),
                        (Boolean)row.get("playerFlag")

                )).toList();
        return list;
    }

    public int delete(String id) {
        int number = jdbcTemplate.update("UPDATE player SET playerFlag=? WHERE playerId = ?",false,id);
        return number;
    }

    public int update(PlayerController.playerItem playerItem){
        int number2 = jdbcTemplate.update("update player set playerName=? where playerId = ?",
                playerItem.playerName(),
                playerItem.playerId());
        return number2;
    }

}
