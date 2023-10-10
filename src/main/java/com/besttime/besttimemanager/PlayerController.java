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
    private final BestTimeDao dao;

    @Autowired
    public PlayerController(BestTimeDao dao) {
        this.dao = dao;
    }

    record playerItem(String playerId, String playerName, boolean playerFlag) {

    }

    //選手一覧
    @GetMapping("player_list")
    String listPlayers(Model model){
        List<playerItem> playerItems = this.dao.findPlayers();
        model.addAttribute("player", playerItems);
        return "home";
    }

    //選手の追加
    @GetMapping("/add_player")
    String addPlayer(@RequestParam(name = "playerName", required = false, defaultValue ="default") String Name){
        String id = UUID.randomUUID().toString().substring(0, 8);
        playerItem item = new playerItem(id,Name,true);
        this.dao.addPlayer(item);
        return "redirect:/player_list";
    }

    //選手の削除（論理削除）
    @GetMapping("/delete_player")
    String deleteItem(@RequestParam("playerId") String id) {
        this.dao.deletePlayer(id);
        return "redirect:/player_list";
    }

    //選手名の更新
    @GetMapping("/update_player")
    String updateItem(@RequestParam("playerId") String id,
                      @RequestParam(name = "playerName", required = false, defaultValue ="default") String name) {
        playerItem item = new playerItem(id,name,true);
        this.dao.updatePlayer(item);
        return "redirect:/player_list";
    }

    //選手一覧画面で選手名検索
    @GetMapping("search_player_in_player")
    String listPlayers(Model model,
                       @RequestParam("searchPlayerName")String searchPlayerName){
        List<PlayerController.playerItem> playerItems = this.dao.searchPlayerInPlayer(searchPlayerName);
        model.addAttribute("player", playerItems);
        return "home";
    }
}
