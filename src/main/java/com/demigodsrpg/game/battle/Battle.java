/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.game.battle;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Battle {
    private final String id;

    private ConcurrentMap<String, BattleMetaData> involved;

    private Location startLocation;

    private long startTimeMillis;
    private long lastInteract;
    private long endTimeMillis;

    // -- CONSTRUCTORS -- //

    public Battle() {
        id = UUID.randomUUID().toString();
        involved = new ConcurrentHashMap<>();
    }

    public Battle(Participant... participants) {
        id = UUID.randomUUID().toString();
        if (participants.length < 1) {
            throw new IllegalArgumentException("A battle needs at least 1 participant to make sense.");
        }
        involved = new ConcurrentHashMap<>();
        for (Participant participant : participants) {
            involved.put(participant.getPersistentId(), new BattleMetaData());
        }
        startLocation = participants[0].getLocation();
        startTimeMillis = System.currentTimeMillis();
        lastInteract = System.currentTimeMillis();
        DGGame.BATTLE_R.register(this);
    }

    // -- GETTERS -- //

    public String getId() {
        return id;
    }

    public ConcurrentMap<String, BattleMetaData> getInvolved() {
        return involved;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public long getLastInteract() {
        return lastInteract;
    }

    public long getEndTimeMillis() {
        return endTimeMillis;
    }

    public boolean isInvolved(Participant participant) {
        return involved.keySet().contains(participant.getPersistentId());
    }

    public boolean isInvolved(Player player) {
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        return isInvolved(model);
    }

    // -- MUTATORS -- //

    public void setStartLocation(Location location) {
        this.startLocation = location;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
        this.lastInteract = startTimeMillis;
    }

    public void setEndTimeMillis(long endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
    }

    public void hit(Participant attacking, Participant hit) {
        putIfAbsent(attacking, hit);
        if (okayToHit(attacking, hit)) {
            involved.get(attacking.getPersistentId()).hits++;
        }
        lastInteract = System.currentTimeMillis();
        DGGame.BATTLE_R.register(this);
    }

    public void deny(Participant attacking, Participant target, Participant denier) {
        putIfAbsent(attacking, target, denier);
        if (!attacking.getFaction().equals(denier.getFaction()) && okayToHit(attacking, target)) {
            involved.get(denier.getPersistentId()).denies++;
        }
        lastInteract = System.currentTimeMillis();
        DGGame.BATTLE_R.register(this);
    }

    public void assist(Participant attacking, Participant hit, Participant assistant) {
        putIfAbsent(attacking, hit, assistant);
        if (!attacking.getFaction().equals(assistant.getFaction()) && okayToHit(attacking, hit)) {
            involved.get(assistant.getPersistentId()).assists++;
        }
        lastInteract = System.currentTimeMillis();
        DGGame.BATTLE_R.register(this);
    }

    public void kill(Participant attacking, Participant killed) {
        putIfAbsent(attacking, killed);
        if (attacking.getFaction().equals(killed.getFaction())) {
            involved.get(attacking.getPersistentId()).teamKills++;
            attacking.addTeamKill();
        } else {
            involved.get(attacking.getPersistentId()).kills++;
        }
        lastInteract = System.currentTimeMillis();
        die(killed);
    }

    public void die(Participant dead) {
        putIfAbsent(dead);
        involved.get(dead.getPersistentId()).deaths++;
        DGGame.BATTLE_R.register(this);
    }

    public Report end() {
        endTimeMillis = System.currentTimeMillis();
        for (Map.Entry<String, BattleMetaData> entry : involved.entrySet()) {
            Participant participant = DGGame.PLAYER_R.fromId(entry.getKey()); // FIXME This restricts to players
            participant.reward(entry.getValue());
        }
        DGGame.BATTLE_R.unregister(this);
        return new Report(this);
    }

    // -- PRIVATE HELPER METHODS -- //

    private boolean okayToHit(Participant attacking, Participant defending) {
        return Faction.NEUTRAL.equals(attacking.getFaction()) ||
                Faction.NEUTRAL.equals(defending.getFaction()) ||
                Faction.EXCOMMUNICATED.equals(attacking.getFaction()) ||
                Faction.EXCOMMUNICATED.equals(defending.getFaction()) ||
                !attacking.getFaction().equals(defending.getFaction());
    }

    private void putIfAbsent(Participant... toPut) {
        for (Participant participant : toPut) {
            involved.putIfAbsent(participant.getPersistentId(), new BattleMetaData());
        }
    }
}
