package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
public class RecordController {
    private final BestTimeDao dao;

    @Autowired
    public RecordController(BestTimeDao dao) {
        this.dao = dao;
    }

    record recordItem(String recordId, String playerId, String eventId, String eventName,
                      String addNowDate, String recordTime, boolean best_is_showed) {
    }

    //記録一覧画面を表示
    @GetMapping("record_list/{playerId}")
    String listRecord(Model model, @PathVariable("playerId") String playerId) {
        List<recordItem> recordItems = this.dao.findBestRecords(playerId);
        List<EventController.eventItem> eventItems = this.dao.findEvents();
        model.addAttribute("event", eventItems);
        model.addAttribute("record", recordItems);
        model.addAttribute("playerHistory", this.dao.findPlayerName(playerId));
        model.addAttribute("inputPlayerId", playerId);
        return "recordHome";
    }

    //記録を追加
    @GetMapping("/add_record")
    String addRecord(Model model,
                     @RequestParam("playerId") String playerId,
                     @RequestParam("eventId") String eventId,
                     @RequestParam(name = "addNowDate", defaultValue = "0000-00-00", required = false) String addNowDate,
                     @RequestParam(name = "recordTime", defaultValue = "00:00:00", required = false) String recordTime) {
        String recordId = UUID.randomUUID().toString().substring(0, 8);
        String eventName = this.dao.searchEventName(eventId);
        recordItem item = new recordItem(recordId, playerId, eventId, eventName, addNowDate, recordTime, true);
        if (this.dao.updateBestFlag(item) < 0) {
            item = new recordItem(recordId, playerId, eventId, eventName, addNowDate, recordTime, false);
        }
        this.dao.addRecord(item);
        return "redirect:/record_list/" + playerId;
    }

    //記録を削除（論理削除）
    @GetMapping("/delete_record")
    String deleteItem(@RequestParam("recordId") String recordId,
                      @RequestParam("playerId") String playerId) {
        this.dao.deleteRecord(recordId);
        return "redirect:/record_list/" + playerId;
    }

    //記録の更新
    @GetMapping("/update_record")
    String updateItem(@RequestParam("recordId") String recordId,
                      @RequestParam("playerId") String playerId,
                      @RequestParam("eventId") String eventId,
                      @RequestParam(name = "addNowDate", defaultValue = "0000-00-00", required = false) String addNowDate,
                      @RequestParam("recordTime") String recordTime) {
        String eventName = this.dao.searchEventName(eventId);
        recordItem item = new recordItem(recordId, playerId, eventId, eventName, addNowDate, recordTime, true);
        this.dao.updateRecord(item);
        return "redirect:/record_list/" + playerId;
    }

    //記録を種目名で検索
    @GetMapping("/record_list/{playerId}/search_event_in_record")
    String listPlayers(Model model,
                       @PathVariable("playerId") String playerId,
                       @RequestParam("searchEventName") String searchEventName) {
        List<RecordController.recordItem> recordItems = this.dao.searchEventInRecord(playerId, searchEventName);
        List<EventController.eventItem> eventItems = this.dao.findEvents();
        model.addAttribute("record", recordItems);
        model.addAttribute("event", eventItems);
        model.addAttribute("playerHistory", this.dao.findPlayerName(playerId));
        model.addAttribute("inputPlayerId", playerId);
        return "recordHome";
    }
}
