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
import java.util.Objects;

@Service
public class BestTimeDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BestTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    * 選手機能
    * */

    //    選手追加
    public void addPlayer(PlayerController.playerItem item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("player");
        insert.execute(param);
    }

    //DB選手テーブルからデータ取得
    public List<PlayerController.playerItem> findPlayers() {
        String query = "SELECT * FROM " + "player WHERE playerFlag=true";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        return result.stream().map(
                (Map<String, Object> row) -> new PlayerController.playerItem(
                        row.get("playerId").toString(),
                        row.get("playerName").toString(),
                        (Boolean) row.get("playerFlag")
                )).toList();
    }

    //選手削除（論理削除）
    public void deletePlayer(String id) {
        jdbcTemplate.update("UPDATE player SET playerFlag=? WHERE playerId = ?", false, id);
    }

    //選手名更新
    public void updatePlayer(PlayerController.playerItem playerItem) {
        jdbcTemplate.update("UPDATE player set playerName=? where playerId = ?",
                playerItem.playerName(),
                playerItem.playerId());
    }


    //選手一覧画面で選手名検索
    public List<PlayerController.playerItem> searchPlayerInPlayer(String searchPlayerName) {
        String query = "SELECT * FROM " + "player WHERE playerFlag=true AND playerName like '%" + searchPlayerName + "%'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<PlayerController.playerItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new PlayerController.playerItem(
                        row.get("playerId").toString(),
                        row.get("playerName").toString(),
                        (Boolean) row.get("playerFlag")
                )).toList();
        return list;
    }

    /*
    * 種目機能
    * */
    //DB種目テーブルからデータ取得
    public List<EventController.eventItem> findEvents() {
        String query = "SELECT * FROM " + "event WHERE eventFlag=true ORDER BY eventName ASC";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<EventController.eventItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new EventController.eventItem(
                        row.get("eventId").toString(),
                        row.get("eventName").toString(),
                        (Boolean) row.get("eventFlag")
                )).toList();
        return list;
    }

    //競技種目機能
    public void addEvent(EventController.eventItem item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("event");
        insert.execute(param);
    }

    //種目の削除（論理削除）
    public void deleteEvent(String id) {
        jdbcTemplate.update("UPDATE event SET eventFlag=? WHERE eventId = ?", false, id);
    }

    //種目名の更新
    public void updateEvent(EventController.eventItem eventItem) {
        jdbcTemplate.update("UPDATE event set eventName=? where eventId = ?",
                eventItem.eventName(),
                eventItem.eventId());
    }

    //種目一覧画面で種目名検索
    public List<EventController.eventItem> searchEventInEvent(String searchEventName) {
        String query = "SELECT * FROM " + "event WHERE eventFlag=true AND eventName like '%" + searchEventName + "%'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<EventController.eventItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new EventController.eventItem(
                        row.get("eventId").toString(),
                        row.get("eventName").toString(),
                        (Boolean) row.get("eventFlag")
                )).toList();
        return list;
    }

    /*
    * 記録機能
    * */
    //記録の登録
    public void addRecord(RecordController.recordItem item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("record");
        insert.execute(param);
    }

    //記録のベストタイムフラグの更新
    public int updateBestFlag(RecordController.recordItem recordItem) {
        String query = "SELECT recordTime FROM " + "record WHERE playerId='" + recordItem.playerId() + "' AND eventId='" + recordItem.eventId() + "'AND BestFlag=true";
        String bestTime;
        try {
            bestTime = this.jdbcTemplate.queryForObject(query, String.class);
        } catch (EmptyResultDataAccessException e) {
            bestTime = "99:99:99";
        }
        int bestI =Integer.parseInt(Objects.requireNonNull(bestTime).replace(":", ""));
        String newTime = recordItem.recordTime();
        int newI =Integer.parseInt(newTime.replace(":", ""));
        int number;
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

    //DB記録テーブルからデータ取得（bestFlagがtrueの物のみ）
    public List<RecordController.recordItem> findBestRecords(String playerId) {
        String query = "SELECT * FROM " + "record INNER JOIN event ON record.eventId = event.eventId WHERE bestFlag=true AND playerId='" + playerId + "'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<RecordController.recordItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new RecordController.recordItem(
                        row.get("recordId").toString(),
                        row.get("playerId").toString(),
                        row.get("eventId").toString(),
                        row.get("eventName").toString(),
                        row.get("addNowDate").toString(),
                        row.get("recordTime").toString(),
                        (Boolean) row.get("bestFlag")
                )).toList();
        return list;
    }

    //記録の削除
    public void deleteRecord(String id) {
        jdbcTemplate.update("UPDATE record SET bestFlag=? WHERE recordId = ?", false, id);
    }

    //記録の更新
    public void updateRecord(RecordController.recordItem recordItem) {
        jdbcTemplate.update("UPDATE record set addNowDate=?,recordTime=? where recordId = ?",
                recordItem.addNowDate(),
                recordItem.recordTime(),
                recordItem.recordId());
    }

    //記録画面に選手名を送信
    public String findPlayerName(String playerId) {
        String query = "SELECT playerName FROM " + "player WHERE playerId='" + playerId + "'";
        String name;
        name = this.jdbcTemplate.queryForObject(query, String.class);
        return name;
    }

    //記録一覧画面に種目名を送信
    public String searchEventName(String eventId) {
        String query = "SELECT eventName FROM event WHERE eventId='"+ eventId +"'" ;
        return this.jdbcTemplate.queryForObject(query, String.class);
    }

    //記録一覧画面で種目名検索
    public List<RecordController.recordItem> searchEventInRecord(String playerId ,String searchEventName) {
        String query = "SELECT * FROM " + "record INNER JOIN event ON record.eventId = event.eventId WHERE bestFlag=true AND eventName like '%" + searchEventName + "%' AND playerId='"+playerId+"'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<RecordController.recordItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new RecordController.recordItem(
                        row.get("recordId").toString(),
                        row.get("playerId").toString(),
                        row.get("eventId").toString(),
                        row.get("eventName").toString(),
                        row.get("addNowDate").toString(),
                        row.get("recordTime").toString(),
                        (Boolean) row.get("bestFlag")
                )).toList();
        return list;
    }

    /*
    * ラップタイム機能
    * */
    //ラップタイムを追加
    public void addLapTime(LapTimeController.lapTimeItem item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("lapTime");
        insert.execute(param);
    }

    //記録のラップタイム表示フラグの更新
    public void updateLapTimeFlag(LapTimeController.lapTimeItem lapTimeItem) {
        jdbcTemplate.update("UPDATE lapTime SET lapTimeFlag=false WHERE recordId = ? AND lapTimeNum = ?",
                lapTimeItem.recordId(),
                lapTimeItem.lapTimeNum());
    }

    //画面にラップタイムを送信
    public List<LapTimeRestController.lapTimeItem> findLapTimes(String recordId) {
        String query = "SELECT * FROM lapTime WHERE lapTimeFlag=true AND recordId='" + recordId + "' ORDER BY lapTimeNum ASC";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<LapTimeRestController.lapTimeItem> list;
        list = result.stream().map(
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

    //ラップタイムの削除（論理削除）
    public void deleteLapTime(String lapTimeId) {
        jdbcTemplate.update("UPDATE lapTime SET lapTimeFlag=? WHERE lapTimeId = ?", false, lapTimeId);
    }

    /*
    * アーカイブ機能
    * */
    //DB記録テーブルからデータ取得（全件）
    public List<RecordArchiveController.recordItem> findArchiveRecords(String playerId,String eventId) {
        String query = "SELECT * FROM " + "record INNER JOIN event ON record.eventId = event.eventId WHERE playerId='" + playerId + "' AND record.eventId='" + eventId + "' ORDER BY addNowDate ASC";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<RecordArchiveController.recordItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new RecordArchiveController.recordItem(
                        row.get("recordId").toString(),
                        row.get("playerId").toString(),
                        row.get("eventId").toString(),
                        row.get("eventName").toString(),
                        row.get("addNowDate").toString(),
                        row.get("recordTime").toString(),
                        (Boolean) row.get("bestFlag")
                )).toList();
        return list;
    }

    //アーカイブ画面に種目名を送信
    public String findEventName(String eventId) {
        String query = "SELECT eventName FROM " + "event WHERE eventId='"+eventId+"'";
        String name;
        name = this.jdbcTemplate.queryForObject(query,String.class);
        return name;
    }
}
