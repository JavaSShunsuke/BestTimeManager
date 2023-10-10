package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RecordArchiveController {
    private final BestTimeDao dao;

    @Autowired
    public RecordArchiveController(BestTimeDao dao) {
        this.dao = dao;
    }

    record recordItem(String recordId, String playerId, String eventId,String eventName,
                      String addNowDate, String recordTime, boolean bestFlag) {
    }

    //フラグ関係なく記録一覧を表示、折れ線グラフも表示される
    @GetMapping("/record_archive/{playerId}/{eventId}")
    String listRecord(Model model ,
                      @PathVariable("playerId") String playerId,
                      @PathVariable("eventId") String eventId){
        List<RecordArchiveController.recordItem> archiveRecordItems = this.dao.findArchiveRecords(playerId,eventId);
        List<EventController.eventItem> eventItems = this.dao.findEvents();
        List<PlayerController.playerItem> playerItems = this.dao.findPlayers();
        model.addAttribute("event", eventItems);
        model.addAttribute("player", playerItems);
        model.addAttribute("archiveRecord", archiveRecordItems);
        model.addAttribute("playerHistory",this.dao.findPlayerName(playerId));
        model.addAttribute("eventHistory",this.dao.findEventName(eventId));
        model.addAttribute("inputPlayerId",playerId);
        return "recordArchiveHome";
    }

    //セレクトボックスで指定した別のアーカイブに遷移
    @GetMapping("/select_archive")
        String archiveSelect(
                @RequestParam("playerId") String playerId,
                @RequestParam("eventId") String eventId
                ){
        return "redirect:/record_archive/" + playerId + "/" + eventId;
    }
}
