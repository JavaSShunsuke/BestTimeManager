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

    public int swimmerAdd(SwimmerController.swimmerItem item){
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("swimmer");
        return insert.execute(param);
    }

    public <LIst> List<SwimmerController.swimmerItem> findSwimmers(){
        String query = "SELECT * FROM " + "swimmer WHERE swimmerFlag=true";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<SwimmerController.swimmerItem> list = result.stream().map(
                (Map<String, Object> row) -> new SwimmerController.swimmerItem(
                        row.get("swimmerId").toString(),
                        row.get("swimmerName").toString(),
                        (Boolean)row.get("swimmerFlag")

                )).toList();
        return list;
    }

    public int delete(String id) {
        int number = jdbcTemplate.update("UPDATE swimmer SET swimmerFlag=? WHERE swimmerId = ?",false,id);
        return number;
    }

    public int update(SwimmerController.swimmerItem swimmerItem){
        int number2 = jdbcTemplate.update("update swimmer set swimmerName=? where swimmerId = ?",
                swimmerItem.swimmerName(),
                swimmerItem.swimmerId());
        return number2;
    }

}
