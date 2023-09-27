package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class RecordArchiveController {
    private final BesttimeDao dao;

    @Autowired
    public RecordArchiveController(BesttimeDao dao) {
        this.dao = dao;
    }
    record eventItem(String eventId, String eventName, boolean eventFlag) {

    }

    record recordItem(String recordId, String playerId, String eventId,String eventName, String addNowDate, String recordTime,
                      boolean recordFlag,
                      boolean bestFlag) {
    }

    @GetMapping("recordlist/{playerId}/{eventId}")
    String listRecord(Model model ,
                      @PathVariable("playerId") String playerId,
                      @PathVariable("eventId") String eventId){
        List<RecordArchiveController.recordItem> archiveRecordItems = this.dao.findArchiveRecords(playerId,eventId);
        List<EventController.eventItem> eventItems = this.dao.findEvents();
        model.addAttribute("event", eventItems);
        model.addAttribute("archiveRecord", archiveRecordItems);
        model.addAttribute("playerHistory",this.dao.findPlayerName(playerId));
        model.addAttribute("eventHistory",this.dao.findEventName(eventId));
        model.addAttribute("inputPlayerId",playerId);
        return "recordArchiveHome";
    }
}
