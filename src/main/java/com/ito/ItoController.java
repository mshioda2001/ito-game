package com.ito;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class ItoController {

    private final ItoService service;

    public ItoController(ItoService service) {
        this.service = service;
    }

    // ===== 参加者画面 =====

    @GetMapping("/")
    public String playerSelect(Model model) {
        model.addAttribute("roomIds", service.getRoomIds());
        return "player-select";
    }

    @GetMapping("/room/{roomId}")
    public String playerPage(@PathVariable String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        model.addAttribute("roomName", "ルーム " + roomId);
        model.addAttribute("roundActive", service.isRoundActive(roomId));
        model.addAttribute("topic", service.getTopic(roomId));
        return "player";
    }

    @PostMapping("/room/{roomId}/draw")
    @ResponseBody
    public Map<String, Object> draw(@PathVariable String roomId, @RequestParam String name) {
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            return Map.of("success", false, "message", "名前を入力してください");
        }
        int number = service.drawNumber(roomId, trimmed);
        if (number == -1) {
            if (!service.isRoundActive(roomId)) {
                return Map.of("success", false, "message", "まだラウンドが始まっていません。進行役をお待ちください。");
            }
            return Map.of("success", false, "message", "数字が残っていません。進行役に連絡してください。");
        }
        return Map.of("success", true, "number", number, "topic", service.getTopic(roomId), "name", trimmed);
    }

    @GetMapping("/room/{roomId}/status")
    @ResponseBody
    public Map<String, Object> status(@PathVariable String roomId) {
        return Map.of(
            "roundActive", service.isRoundActive(roomId),
            "topic", service.getTopic(roomId)
        );
    }

    // ===== 進行役画面 =====

    @GetMapping("/admin")
    public String adminSelect(Model model) {
        model.addAttribute("roomIds", service.getRoomIds());
        return "admin-select";
    }

    @GetMapping("/admin/{roomId}")
    public String adminPage(@PathVariable String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        model.addAttribute("roomName", "ルーム " + roomId);
        model.addAttribute("roomIds", service.getRoomIds());
        model.addAttribute("roundActive", service.isRoundActive(roomId));
        model.addAttribute("topic", service.getTopic(roomId));
        model.addAttribute("remaining", service.getRemainingCount(roomId));
        model.addAttribute("assignments", service.getAssignments(roomId));
        model.addAttribute("totalAssigned", service.getTotalAssigned(roomId));
        return "admin";
    }

    @PostMapping("/admin/{roomId}/start")
    public String startRound(@PathVariable String roomId, @RequestParam String topic) {
        service.startRound(roomId, topic.trim().isEmpty() ? "辛さ" : topic.trim());
        return "redirect:/admin/" + roomId;
    }

    @PostMapping("/admin/{roomId}/end")
    public String endRound(@PathVariable String roomId) {
        service.endRound(roomId);
        return "redirect:/admin/" + roomId;
    }

    @PostMapping("/admin/{roomId}/reset")
    public String reset(@PathVariable String roomId) {
        service.reset(roomId);
        return "redirect:/admin/" + roomId;
    }

    @GetMapping("/admin/{roomId}/data")
    @ResponseBody
    public Map<String, Object> adminData(@PathVariable String roomId) {
        return Map.of(
            "roundActive", service.isRoundActive(roomId),
            "topic", service.getTopic(roomId),
            "remaining", service.getRemainingCount(roomId),
            "assignments", service.getAssignments(roomId),
            "totalAssigned", service.getTotalAssigned(roomId)
        );
    }

    // ===== 共有画面 =====

    @GetMapping("/share")
    public String shareSelect(Model model) {
        model.addAttribute("roomIds", service.getRoomIds());
        return "share-select";
    }

    @GetMapping("/share/{roomId}")
    public String sharePage(@PathVariable String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        model.addAttribute("roomName", "ルーム " + roomId);
        return "share";
    }

    @GetMapping("/share/{roomId}/data")
    @ResponseBody
    public Map<String, Object> shareData(@PathVariable String roomId) {
        return Map.of(
            "roundActive", service.isRoundActive(roomId),
            "topic", service.getTopic(roomId),
            "assignments", service.getAssignments(roomId)
        );
    }
}
