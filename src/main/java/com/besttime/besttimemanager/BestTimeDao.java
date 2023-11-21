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
        String query = "SELECT * FROM " + "player WHERE player_is_showed=true";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        return result.stream().map(
                (Map<String, Object> row) -> new PlayerController.playerItem(
                        row.get("player_id").toString(),
                        row.get("player_name").toString(),
                        (Boolean) row.get("player_is_showed")
                )).toList();
    }

    //選手削除（論理削除）
    public void deletePlayer(String id) {
        jdbcTemplate.update("UPDATE player SET player_is_showed=? WHERE player_id = ?", false, id);
    }

    //選手名更新
    public void updatePlayer(PlayerController.playerItem playerItem) {
        jdbcTemplate.update("UPDATE player set player_name=? where player_id = ?",
                playerItem.playerName(),
                playerItem.playerId());
    }


    //選手一覧画面で選手名検索
    public List<PlayerController.playerItem> searchPlayerInPlayer(String searchPlayerName) {
        String query = "SELECT * FROM " + "player WHERE player_is_showed=true AND player_name like '%" + searchPlayerName + "%'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<PlayerController.playerItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new PlayerController.playerItem(
                        row.get("player_id").toString(),
                        row.get("player_name").toString(),
                        (Boolean) row.get("player_is_showed")
                )).toList();
        return list;
    }

    /*
    * 種目機能
    * */
    //DB種目テーブルからデータ取得
    public List<EventController.eventItem> findEvents() {
        String query = "SELECT * FROM " + "event WHERE event_is_showed=true ORDER BY event_name ASC";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<EventController.eventItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new EventController.eventItem(
                        row.get("event_id").toString(),
                        row.get("event_name").toString(),
                        (Boolean) row.get("event_is_showed")
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
        jdbcTemplate.update("UPDATE event SET event_is_showed=? WHERE event_id = ?", false, id);
    }

    //種目名の更新
    public void updateEvent(EventController.eventItem eventItem) {
        jdbcTemplate.update("UPDATE event set event_name=? where event_id = ?",
                eventItem.eventName(),
                eventItem.eventId());
    }

    //種目一覧画面で種目名検索
    public List<EventController.eventItem> searchEventInEvent(String searchEventName) {
        String query = "SELECT * FROM " + "event WHERE event_is_showed=true AND event_name like '%" + searchEventName + "%'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<EventController.eventItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new EventController.eventItem(
                        row.get("event_id").toString(),
                        row.get("event_name").toString(),
                        (Boolean) row.get("event_is_showed")
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
        String query = "SELECT record_time FROM " + "record WHERE player_id='" + recordItem.playerId() + "' AND event_id='" + recordItem.eventId() + "'AND best_is_showed=true";
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
            number = jdbcTemplate.update("UPDATE record SET best_is_showed=false WHERE player_id = ? AND event_id = ?",
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
        String query = "SELECT * FROM " + "record INNER JOIN event ON record.event_id = event.event_id WHERE best_is_showed=true AND player_id='" + playerId + "' ORDER BY event_name ASC";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<RecordController.recordItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new RecordController.recordItem(
                        row.get("record_id").toString(),
                        row.get("player_id").toString(),
                        row.get("event_id").toString(),
                        row.get("event_name").toString(),
                        row.get("add_now_date").toString(),
                        row.get("record_time").toString(),
                        (Boolean) row.get("best_is_showed")
                )).toList();
        return list;
    }

    //記録の削除
    public void deleteRecord(String id) {
        jdbcTemplate.update("UPDATE record SET best_is_showed=? WHERE record_id = ?", false, id);
    }

    //記録の更新
    public void updateRecord(RecordController.recordItem recordItem) {
        jdbcTemplate.update("UPDATE record set add_now_date=?,record_time=? where record_id = ?",
                recordItem.addNowDate(),
                recordItem.recordTime(),
                recordItem.recordId());
    }

    //記録画面に選手名を送信
    public String findPlayerName(String playerId) {
        String query = "SELECT player_name FROM " + "player WHERE player_id='" + playerId + "'";
        String name;
        name = this.jdbcTemplate.queryForObject(query, String.class);
        return name;
    }

    //記録一覧画面に種目名を送信
    public String searchEventName(String eventId) {
        String query = "SELECT event_name FROM event WHERE event_id='"+ eventId +"'" ;
        return this.jdbcTemplate.queryForObject(query, String.class);
    }

    //記録一覧画面で種目名検索
    public List<RecordController.recordItem> searchEventInRecord(String playerId ,String searchEventName) {
        String query = "SELECT * FROM " + "record INNER JOIN event ON record.event_id = event.event_id WHERE best_is_showed=true AND event_name like '%" + searchEventName + "%' AND player_id='"+playerId+"'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<RecordController.recordItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new RecordController.recordItem(
                        row.get("record_id").toString(),
                        row.get("player_id").toString(),
                        row.get("event_id").toString(),
                        row.get("event_name").toString(),
                        row.get("add_now_date").toString(),
                        row.get("record_time").toString(),
                        (Boolean) row.get("best_is_showed")
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
                .withTableName("lap_time");
        insert.execute(param);
    }

    //記録のラップタイム表示フラグの更新
    public void updateLapTimeFlag(LapTimeController.lapTimeItem lapTimeItem) {
        jdbcTemplate.update("UPDATE lap_time SET lap_time_is_showed=false WHERE record_id = ? AND lap_time_num = ?",
                lapTimeItem.recordId(),
                lapTimeItem.lapTimeNum());
    }

    //画面にラップタイムを送信
    public List<LapTimeRestController.lapTimeItem> findLapTimes(String recordId) {
        String query = "SELECT * FROM lap_time WHERE lap_time_is_showed=true AND record_id='" + recordId + "' ORDER BY lap_time_num ASC";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<LapTimeRestController.lapTimeItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new LapTimeRestController.lapTimeItem(
                        row.get("lap_time_id").toString(),
                        row.get("record_id").toString(),
                        row.get("lap_time_num").toString(),
                        row.get("lap_time_record").toString(),
                        (Boolean) row.get("lap_time_is_showed"),
                        row.get("lap_time_memo").toString()
                )).toList();
        return list;
    }

    //ラップタイムの削除（論理削除）
    public void deleteLapTime(String lapTimeId) {
        jdbcTemplate.update("UPDATE lap_time SET lap_time_is_showed=? WHERE lap_time_id = ?", false, lapTimeId);
    }

    /*
    * アーカイブ機能
    * */
    //DB記録テーブルからデータ取得（全件）
    public List<RecordArchiveController.recordItem> findArchiveRecords(String playerId,String eventId) {
        String query = "SELECT * FROM " + "record INNER JOIN event ON record.event_id = event.event_id WHERE player_id='" + playerId + "' AND record.event_id='" + eventId + "' ORDER BY add_now_date ASC";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(query);
        List<RecordArchiveController.recordItem> list;
        list = result.stream().map(
                (Map<String, Object> row) -> new RecordArchiveController.recordItem(
                        row.get("record_id").toString(),
                        row.get("player_id").toString(),
                        row.get("event_id").toString(),
                        row.get("event_name").toString(),
                        row.get("add_now_date").toString(),
                        row.get("record_time").toString(),
                        (Boolean) row.get("best_is_showed")
                )).toList();
        return list;
    }

    //アーカイブ画面に種目名を送信
    public String findEventName(String eventId) {
        String query = "SELECT event_name FROM " + "event WHERE event_id='"+eventId+"'";
        String name;
        name = this.jdbcTemplate.queryForObject(query,String.class);
        return name;
    }
}
