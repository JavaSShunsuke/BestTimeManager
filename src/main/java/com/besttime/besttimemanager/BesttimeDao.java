package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    //    選手機能
    public int addplayer(PlayerController.playerItem item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("player");
        return insert.execute(param);
    }

    public <LIst> List<PlayerController.playerItem> findPlayers() {
        String query = "SELECT * FROM " + "player WHERE playerFlag=true";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<PlayerController.playerItem> list = result.stream().map(
                (Map<String, Object> row) -> new PlayerController.playerItem(
                        row.get("playerId").toString(),
                        row.get("playerName").toString(),
                        (Boolean) row.get("playerFlag")

                )).toList();
        return list;
    }

    public int deletePlayer(String id) {
        int number = jdbcTemplate.update("UPDATE player SET playerFlag=? WHERE playerId = ?", false, id);
        return number;
    }

    public int updatePlayer(PlayerController.playerItem playerItem) {
        int number2 = jdbcTemplate.update("UPDATE player set playerName=? where playerId = ?",
                playerItem.playerName(),
                playerItem.playerId());
        return number2;
    }

    //競技種目機能
    public int addEvent(EventController.eventItem item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("event");
        return insert.execute(param);
    }

    public <LIst> List<EventController.eventItem> findEvents() {
        String query = "SELECT * FROM " + "event WHERE eventFlag=true ORDER BY eventName ASC";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<EventController.eventItem> list = result.stream().map(
                (Map<String, Object> row) -> new EventController.eventItem(
                        row.get("eventId").toString(),
                        row.get("eventName").toString(),
                        (Boolean) row.get("eventFlag")

                )).toList();
        return list;
    }

    public int deleteEvent(String id) {
        int number = jdbcTemplate.update("UPDATE event SET eventFlag=? WHERE eventId = ?", false, id);
        return number;
    }

    public int updateEvent(EventController.eventItem eventItem) {
        int number = jdbcTemplate.update("UPDATE event set eventName=? where eventId = ?",
                eventItem.eventName(),
                eventItem.eventId());
        return number;
    }

    //競技記録機能
    public int addRecord(RecordController.recordItem item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("record");
        return insert.execute(param);
    }

    public int updateBestFlag(RecordController.recordItem recordItem) {
        String query = "SELECT recordTime FROM " + "record WHERE playerId='" + recordItem.playerId() + "' AND eventId='" + recordItem.eventId() + "'AND BestFlag=true";
        String bestTime = "00:00:00";
        try {
            bestTime = this.jdbcTemplate.queryForObject(query, String.class);
        } catch (EmptyResultDataAccessException e) {
            bestTime = "99:99:99";
        }
        String newTime = recordItem.recordTime();
        int bestI =Integer.parseInt(bestTime.replace(":", ""));
        int newI =Integer.parseInt(newTime.replace(":", ""));

        int number = 0;
        if (bestI>=newI) {
            number = jdbcTemplate.update("UPDATE record SET BestFlag=false WHERE playerId = ? AND eventId = ?",
                    recordItem.playerId(),
                    recordItem.eventId()
            );
        } else {
            number = -1;
        }
        return number;
    }

    public <LIst> List<RecordController.recordItem> findBestRecords(String playerId) {
        String query = "SELECT * FROM " + "record INNER JOIN event ON record.eventId = event.eventId WHERE recordFlag=true AND bestFlag=true AND playerId='" + playerId + "'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<RecordController.recordItem> list = result.stream().map(
                (Map<String, Object> row) -> new RecordController.recordItem(
                        row.get("recordId").toString(),
                        row.get("playerId").toString(),
                        row.get("eventId").toString(),
                        row.get("eventName").toString(),
                        row.get("addNowDate").toString(),
                        row.get("recordTime").toString(),
                        (Boolean) row.get("recordFlag"),
                        (Boolean) row.get("bestFlag")

                )).toList();
        return list;
    }

    public int deleteRecord(String id) {
        int number = jdbcTemplate.update("UPDATE record SET recordFlag=? WHERE recordId = ?", false, id);
        return number;
    }

    public int updateRecord(RecordController.recordItem recordItem) {
        int number = jdbcTemplate.update("UPDATE record set addNowDate=?,recordTime=? where recordId = ?",
                recordItem.addNowDate(),
                recordItem.recordTime(),
                recordItem.recordId());
        return number;
    }

    public String findPlayerName(String playerId) {
        String query = "SELECT playerName FROM " + "player WHERE playerId='" + playerId + "'";
        String name = this.jdbcTemplate.queryForObject(query, String.class);
        return name;
    }

        public String findEventName(String eventId) {
        String query = "SELECT eventName FROM " + "event WHERE eventId='"+eventId+"'";
        String name =this.jdbcTemplate.queryForObject(query,String.class);
        return name;
    }
    public int addLapTime(LapTimeController.lapTimeItem item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("lapTime");
        return insert.execute(param);
    }

    public int updateLapTimeFlag(LapTimeController.lapTimeItem lapTimeItem) {
        int number = jdbcTemplate.update("UPDATE lapTime SET lapTimeFlag=false WHERE recordId = ? AND lapTimeNum = ?",
                lapTimeItem.recordId(),
                lapTimeItem.lapTimeNum());
        return number;
    }

    public <LIst> List<LapTimeRestController.lapTimeItem> findLapTimes(String recordId) {
        String query = "SELECT * FROM lapTime WHERE lapTimeFlag=true AND recordId='" + recordId + "' ORDER BY lapTimeNum ASC";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<LapTimeRestController.lapTimeItem> list = result.stream().map(
                (Map<String, Object> row) -> new LapTimeRestController.lapTimeItem(
                        row.get("lapTimeId").toString(),
                        row.get("recordId").toString(),
                        row.get("lapTimeNum").toString(),
                        row.get("lapTimeRecord").toString(),
                        (Boolean) row.get("lapTimeFlag"),
                        row.get("lapTimeMemo").toString()
                )).toList();
        return list;
    }
    public int deleteLapTime(String lapTimeId) {
        int number = jdbcTemplate.update("UPDATE lapTime SET lapTimeFlag=? WHERE lapTimeId = ?", false, lapTimeId);
        return number;
    }

    public <LIst> List<RecordController.recordItem> searchEventInRecord(String playerId ,String searchEventName) {
        String query = "SELECT * FROM " + "record INNER JOIN event ON record.eventId = event.eventId WHERE recordFlag=true AND bestFlag=true AND eventName like '%" + searchEventName + "%' AND playerId='"+playerId+"'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<RecordController.recordItem> list = result.stream().map(
                (Map<String, Object> row) -> new RecordController.recordItem(
                        row.get("recordId").toString(),
                        row.get("playerId").toString(),
                        row.get("eventId").toString(),
                        row.get("eventName").toString(),
                        row.get("addNowDate").toString(),
                        row.get("recordTime").toString(),
                        (Boolean) row.get("recordFlag"),
                        (Boolean) row.get("bestFlag")

                )).toList();
        return list;
    }

    public <LIst> List<EventController.eventItem> searchEventInEvent(String searchEventName) {
        String query = "SELECT * FROM " + "event WHERE eventFlag=true AND eventName like '%" + searchEventName + "%'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<EventController.eventItem> list = result.stream().map(
                (Map<String, Object> row) -> new EventController.eventItem(
                        row.get("eventId").toString(),
                        row.get("eventName").toString(),
                        (Boolean) row.get("eventFlag")

                )).toList();
        return list;
    }

    public <LIst> List<PlayerController.playerItem> searchPlayerInPlayer(String searchPlayerName) {
        String query = "SELECT * FROM " + "player WHERE playerFlag=true AND playerName like '%" + searchPlayerName + "%'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<PlayerController.playerItem> list = result.stream().map(
                (Map<String, Object> row) -> new PlayerController.playerItem(
                        row.get("playerId").toString(),
                        row.get("playerName").toString(),
                        (Boolean) row.get("playerFlag")

                )).toList();
        return list;
    }

    public String searchEventName(String eventId) {
        String query = "SELECT eventName FROM event WHERE eventId='"+ eventId +"'" ;
        return this.jdbcTemplate.queryForObject(query, String.class);
    }

    public <LIst> List<RecordArchiveController.recordItem> findArchiveRecords(String playerId,String eventId) {
        String query = "SELECT * FROM " + "record INNER JOIN event ON record.eventId = event.eventId WHERE playerId='" + playerId + "' AND record.eventId='" + eventId + "' ORDER BY addNowDate ASC";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<RecordArchiveController.recordItem> list = result.stream().map(
                (Map<String, Object> row) -> new RecordArchiveController.recordItem(
                        row.get("recordId").toString(),
                        row.get("playerId").toString(),
                        row.get("eventId").toString(),
                        row.get("eventName").toString(),
                        row.get("addNowDate").toString(),
                        row.get("recordTime").toString(),
                        (Boolean) row.get("recordFlag"),
                        (Boolean) row.get("bestFlag")

                )).toList();
        return list;
    }
}
