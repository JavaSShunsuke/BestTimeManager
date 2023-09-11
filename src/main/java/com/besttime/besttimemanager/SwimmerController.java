package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
public class SwimmerController {
    private final BesttimeDao dao;

    @Autowired
    public SwimmerController(BesttimeDao dao) {
        this.dao = dao;
    }

    record swimmerItem(String swimmerId, String swimmerName, boolean swimmerFlag) {

    }
    record eventItem(String eventId, String eventName, boolean eventFlag) {

    }
    record recordItem(String recordId, String swimmerId, String eventId, String bestTime, boolean recordFlag) {

    }

    @GetMapping("/swimmerlist")
    String listSwimmers(Model model){
        List<swimmerItem> swimmerItems = this.dao.findSwimmers();
        model.addAttribute("swimmer", swimmerItems);
        return "home";
    }

    @GetMapping("/swimmeradd")
    String addSwimmer(@RequestParam("swimmerName") String Name){
        String id = UUID.randomUUID().toString().substring(0, 8);
        swimmerItem item = new swimmerItem(id,Name,true);
        this.dao.swimmerAdd(item);

        return "redirect:/swimmerlist";
    }

    @GetMapping("/swimmerdelete")
    String deleteItem(@RequestParam("swimmerId") String id) {
        this.dao.delete(id);
        return "redirect:/swimmerlist";
    }

    @GetMapping("/swimmerupdate")
    String updateItem(@RequestParam("swimmerId") String id,
                      @RequestParam("swimmerName") String name) {
        swimmerItem swimmerItem = new swimmerItem(id,name,true);
        this.dao.update(swimmerItem);
        return "redirect:/swimmerlist";
    }
}
