package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
public class EventController {
    private final BestTimeDao dao;

    @Autowired
    public EventController(BestTimeDao dao) {
        this.dao = dao;
    }

    record eventItem(String eventId, String eventName, boolean event_is_showed) {
    }

    //種目一覧画面
    @GetMapping("event_list")
    String listPlayers(Model model){
        List<eventItem> eventItems = this.dao.findEvents();
        model.addAttribute("event", eventItems);
        return "eventHome";
    }

    //種目追加
    @GetMapping("/add_event")
    String addEvent(
            @RequestParam(name="eventName", defaultValue = "default") String Name
    ){
        String id = UUID.randomUUID().toString().substring(0, 8);
        eventItem item = new eventItem(id,Name,true);
        this.dao.addEvent(item);
        return "redirect:/event_list";
    }

    //種目削除（論理削除）
    @GetMapping("/delete_event")
    String deleteItem(@RequestParam("eventId") String id) {
        this.dao.deleteEvent(id);
        return "redirect:/event_list";
    }

    //種目名更新
    @GetMapping("/update_event")
    String updateItem(@RequestParam("eventId") String id,
                      @RequestParam(name="eventName", defaultValue = "default") String name) {
        eventItem item = new eventItem(id,name,true);
        this.dao.updateEvent(item);
        return "redirect:/event_list";
    }

    //種目一覧で種目名検索
    @GetMapping("search_event_in_event")
    String listPlayers(Model model,
                       @RequestParam("searchEventName")String searchEventName){
        List<eventItem> eventItems = this.dao.searchEventInEvent(searchEventName);
        model.addAttribute("event", eventItems);
        return "eventHome";
    }
}
