package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LapTimeRestController {
    private final BestTimeDao dao;

    @Autowired
    public LapTimeRestController(BestTimeDao dao) {
        this.dao = dao;
    }

    record lapTimeItem(String lapTimeId, String recordId, String lapTimeNum, String lapTimeRecord,
                       boolean lapTimeFlag, String lapTimeMemo) {

    }

    //モーダルにラップタイム一覧を表示
    @PostMapping(value = "/list_lap_time")
    List<LapTimeRestController.lapTimeItem> listLapTimes(
            @RequestParam("recordId") String recordId) {
        return this.dao.findLapTimes(recordId);
    }

    //ラップタイムの削除（論理削除）
    @PostMapping(value="/delete_lap_time")
    void deleteLapTimes(
            @RequestParam("lapTimeId") String lapTimeId,
            @RequestParam("recordId") String recordId) {
        this.dao.deleteLapTime(lapTimeId);
        this.dao.findLapTimes(recordId);
    }
}