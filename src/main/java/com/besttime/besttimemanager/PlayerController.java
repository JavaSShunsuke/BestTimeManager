package com.besttime.besttimemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
public class PlayerController {
    private final BesttimeDao dao;

    @Autowired
    public PlayerController(BesttimeDao dao) {
        this.dao = dao;
    }

    record playerItem(String playerId, String playerName, boolean playerFlag) {

    }
    record eventItem(String eventId, String eventName, boolean eventFlag) {

    }
    record recordItem(String recordId, String swimmerId, String eventId, String bestTime, boolean recordFlag) {

    }

    @GetMapping("playerlist")
    String listPlayers(Model model){
        List<playerItem> playerItems = this.dao.findPlayers();
        model.addAttribute("player", playerItems);
        return "home";
    }

    @GetMapping("/addplayer")
    String addPlayer(@RequestParam("playerName") String Name){
        String id = UUID.randomUUID().toString().substring(0, 8);
        playerItem item = new playerItem(id,Name,true);
        this.dao.addplayer(item);

        return "redirect:/playerlist";
    }

    @GetMapping("/deleteplayer")
    String deleteItem(@RequestParam("playerId") String id) {
        this.dao.deletePlayer(id);
        return "redirect:/playerlist";
    }

    @GetMapping("/updateplayer")
    String updateItem(@RequestParam("playerId") String id,
                      @RequestParam("playerName") String name) {
        playerItem item = new playerItem(id,name,true);
        this.dao.updatePlayer(item);
        return "redirect:/playerlist";
    }
}
