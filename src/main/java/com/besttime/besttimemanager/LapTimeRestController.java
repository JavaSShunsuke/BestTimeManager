package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LapTimeRestController {
    private final BesttimeDao dao;

    @Autowired
    public LapTimeRestController(BesttimeDao dao) {
        this.dao = dao;
    }

    record playerItem(String playerId, String playerName, boolean playerFlag) {

    }

    record eventItem(String eventId, String eventName, boolean eventFlag) {

    }

    record recordItem(String recordId, String playerId, String eventId, String addNowDate,String recordTime, boolean recordFlag,
                      boolean bestFlag) {
    }

    record lapTimeItem(String lapTimeId, String recordId, String lapTimeNum, String lapTimeRecord,
                       boolean lapTimeFlag, String lapTimeMemo) {

    }


    @PostMapping(value = "/listlaptime")
    List<LapTimeRestController.lapTimeItem> listLapTimes(
            @RequestParam("recordId") String recordId) {
//        List<LapTimeController.lapTimeItem> lapTimeItems = this.dao.findLapTimes(recordId);
//        model.addAttribute("lapTime", lapTimeItems);\
        return this.dao.findLapTimes(recordId);
    }

    @PostMapping(value="/deletelaptime")
    String deleteLapTimes(
            @RequestParam("lapTimeId") String lapTimeId,
            @RequestParam("recordId") String recordId) {
//        List<LapTimeController.lapTimeItem> lapTimeItems = this.dao.findLapTimes(recordId);
//        model.addAttribute("lapTime", lapTimeItems);\
        this.dao.deleteLapTime(lapTimeId);
        this.dao.findLapTimes(recordId);
        return "^_^";
    }

}