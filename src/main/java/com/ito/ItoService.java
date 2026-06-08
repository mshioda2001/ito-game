package com.ito;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItoService {

    public static class GameRoom {
        public String topic = "辛さ";
        public boolean roundActive = false;
        public final List<Integer> pool = new ArrayList<>();
        public final Map<String, Integer> assignments = new LinkedHashMap<>();

        GameRoom() { initPool(); }

        void initPool() {
            pool.clear();
            for (int i = 1; i <= 100; i++) pool.add(i);
            Collections.shuffle(pool);
        }
    }

    public static final List<String> ROOM_IDS = List.of("1", "2", "3", "4");
    private final Map<String, GameRoom> rooms = new LinkedHashMap<>();

    public ItoService() {
        for (String id : ROOM_IDS) rooms.put(id, new GameRoom());
    }

    public List<String> getRoomIds() { return ROOM_IDS; }

    private GameRoom getRoom(String roomId) {
        return rooms.getOrDefault(roomId, rooms.get("1"));
    }

    public synchronized void startRound(String roomId, String topic) {
        GameRoom room = getRoom(roomId);
        room.topic = topic;
        room.roundActive = true;
        room.assignments.clear();
        room.initPool();
    }

    public synchronized void endRound(String roomId) {
        getRoom(roomId).roundActive = false;
    }

    public synchronized void reset(String roomId) {
        GameRoom room = getRoom(roomId);
        room.roundActive = false;
        room.assignments.clear();
        room.initPool();
    }

    public synchronized int drawNumber(String roomId, String name) {
        GameRoom room = getRoom(roomId);
        if (!room.roundActive) return -1;
        if (room.assignments.containsKey(name)) return room.assignments.get(name);
        if (room.pool.isEmpty()) return -1;
        int number = room.pool.remove(room.pool.size() - 1);
        room.assignments.put(name, number);
        return number;
    }

    public String getTopic(String roomId) { return getRoom(roomId).topic; }
    public boolean isRoundActive(String roomId) { return getRoom(roomId).roundActive; }
    public int getRemainingCount(String roomId) { return getRoom(roomId).pool.size(); }
    public Map<String, Integer> getAssignments(String roomId) { return Collections.unmodifiableMap(getRoom(roomId).assignments); }
    public int getTotalAssigned(String roomId) { return getRoom(roomId).assignments.size(); }
}
