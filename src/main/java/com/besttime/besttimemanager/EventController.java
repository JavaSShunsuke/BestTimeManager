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
    private final BesttimeDao dao;

    @Autowired
    public EventController(BesttimeDao dao) {
        this.dao = dao;
    }

    record playerItem(String playerId, String playerName, boolean playerFlag) {

    }
    record eventItem(String eventId, String eventName, boolean eventFlag) {

    }
    record recordItem(String recordId, String swimmerId, String eventId, String bestTime, boolean recordFlag) {

    }

    @GetMapping("eventlist")
    String listPlayers(Model model){
        List<eventItem> eventItems = this.dao.findEvents();
        model.addAttribute("event", eventItems);
        return "eventHome";
    }

    @GetMapping("/addevent")
    String addEvent(@RequestParam("eventName") String Name){
        String id = UUID.randomUUID().toString().substring(0, 8);
        eventItem item = new eventItem(id,Name,true);
        this.dao.addEvent(item);

        return "redirect:/eventlist";
    }

    @GetMapping("/deleteevent")
    String deleteItem(@RequestParam("eventId") String id) {
        this.dao.deleteEvent(id);
        return "redirect:/eventlist";
    }

    @GetMapping("/updateevent")
    String updateItem(@RequestParam("eventId") String id,
                      @RequestParam("eventName") String name) {
        eventItem item = new eventItem(id,name,true);
        this.dao.updateEvent(item);
        return "redirect:/eventlist";
    }
}
