package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.sql.Time;
import java.util.List;
import java.util.UUID;

@Controller
public class LapTimeController {
    private final BesttimeDao dao;

    @Autowired
    public LapTimeController(BesttimeDao dao) {
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
                       boolean lapTimeFlag,String lapTimeMemo) {

    }

    @GetMapping("/addlaptime")
    String addLapTime(
            @RequestParam("recordId") String recordId,
            @RequestParam("playerId") String playerId,
            @RequestParam("lapTimeNum") String lapTimeNum,
            @RequestParam(name = "lapTimeRecord", defaultValue = "00:00:00", required = false) String lapTimeRecord,
            @RequestParam(name="lapTimeMemo",defaultValue = "") String lapTimeMemo) {
        String lapTimeId = UUID.randomUUID().toString().substring(0, 8);
        LapTimeController.lapTimeItem item = new LapTimeController.lapTimeItem(lapTimeId, recordId, lapTimeNum, lapTimeRecord, true,lapTimeMemo);
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        this.dao.updateLapTimeFlag(item);
        this.dao.addLapTime(item);
        return "redirect:/recordlist/" + playerId;

    }
}