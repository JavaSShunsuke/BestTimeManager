package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class LapTimeController {
    private final BestTimeDao dao;

    @Autowired
    public LapTimeController(BestTimeDao dao) {
        this.dao = dao;
    }

    record lapTimeItem(String lapTimeId, String recordId, String lapTimeNum, String lapTimeRecord,
                       boolean lapTimeFlag,String lapTimeMemo) {

    }

    //ラップタイムの追加
    @GetMapping("/add_lap_time")
    String addLapTime(
            @RequestParam("recordId") String recordId,
            @RequestParam("playerId") String playerId,
            @RequestParam("lapTimeNum") String lapTimeNum,
            @RequestParam(name = "lapTimeRecord", defaultValue = "00:00:00", required = false) String lapTimeRecord,
            @RequestParam(name="lapTimeMemo",defaultValue = "") String lapTimeMemo) {
        String lapTimeId = UUID.randomUUID().toString().substring(0, 8);
        LapTimeController.lapTimeItem item = new LapTimeController.lapTimeItem(lapTimeId, recordId, lapTimeNum, lapTimeRecord, true,lapTimeMemo);
        this.dao.updateLapTimeFlag(item);
        this.dao.addLapTime(item);
        return "redirect:/record_list/" + playerId;
    }
}