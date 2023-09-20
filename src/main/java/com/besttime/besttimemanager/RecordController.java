package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.util.List;
import java.util.UUID;

@Controller
public class RecordController {
    private final BesttimeDao dao;

    @Autowired
    public RecordController(BesttimeDao dao) {
        this.dao = dao;
    }

    record playerItem(String playerId, String playerName, boolean playerFlag) {

    }

    record eventItem(String eventId, String eventName, boolean eventFlag) {

    }

    record recordItem(String recordId, String playerId, String eventId, String recordTime, boolean recordFlag,
                      boolean bestFlag) {
    }

    record lapTimeItem(String lapTimeId, String recordId, String lapTimeNum, String lapTimeRecord,
                       boolean lapTimeFlag,String lapTimeMemo) {

    }
        @GetMapping("recordlist/{playerId}")
    String listRecord(Model model ,@PathVariable("playerId") String playerId){
        List<recordItem> recordItems = this.dao.findBestRecords(playerId);
        List<EventController.eventItem> eventItems = this.dao.findEvents();
        model.addAttribute("event", eventItems);
        model.addAttribute("record", recordItems);
        model.addAttribute("playerHistory",this.dao.findPlayerName(playerId));
        model.addAttribute("inputPlayerId",playerId);
        return "recordHome";
    }

    @GetMapping("/addrecord")
    String addRecord(Model model,
                    @RequestParam("playerId") String playerId,
                    @RequestParam("eventId") String eventId,
                    @RequestParam(name="recordTime", defaultValue ="00:00:00",required = false) String  recordTime){
        String recordId = UUID.randomUUID().toString().substring(0, 8);
        recordItem item = new recordItem(recordId,playerId,eventId,recordTime,true,true);
        this.dao.updateBestFlag(item);
        this.dao.addRecord(item);
        return "redirect:/recordlist/" + playerId;
    }

    @GetMapping("/deleterecord")
    String deleteItem(@RequestParam("recordId") String recordId,
                      @RequestParam("playerId") String playerId) {
        this.dao.deleteRecord(recordId);

        return "redirect:/recordlist/" + playerId;
    }

    @GetMapping("/updaterecord")
    String updateItem(@RequestParam("recordId") String recordId,
                      @RequestParam("playerId") String playerId,
                      @RequestParam("eventId") String eventId,
                      @RequestParam("recordTime") String recordTime) {
        recordItem item = new recordItem(recordId,playerId,eventId,recordTime,true,true);
        this.dao.updateRecord(item);
        return "redirect:/recordlist/" + playerId;
    }


}
