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

package com.demigodsrpg.game.aspect;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.bloodlust.BloodlustAspectHero;
import com.demigodsrpg.game.aspect.bloodlust.BloodlustAspectI;
import com.demigodsrpg.game.aspect.bloodlust.BloodlustAspectII;
import com.demigodsrpg.game.aspect.bloodlust.BloodlustAspectIII;
import com.demigodsrpg.game.aspect.crafting.CraftingAspectI;
import com.demigodsrpg.game.aspect.demon.DemonAspectHero;
import com.demigodsrpg.game.aspect.demon.DemonAspectI;
import com.demigodsrpg.game.aspect.demon.DemonAspectII;
import com.demigodsrpg.game.aspect.fire.FireAspectI;
import com.demigodsrpg.game.aspect.lightning.LightningAspectHero;
import com.demigodsrpg.game.aspect.lightning.LightningAspectI;
import com.demigodsrpg.game.aspect.lightning.LightningAspectII;
import com.demigodsrpg.game.aspect.water.WaterAspectHero;
import com.demigodsrpg.game.aspect.water.WaterAspectI;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.deity.Faction;
import org.bukkit.entity.Player;

public class Aspects {
    // -- PUBLIC RELEASE -- //

    public static final LightningAspectHero LIGHTNING_ASPECT_HERO = new LightningAspectHero();
    public static final LightningAspectI LIGHTNING_ASPECT_I = new LightningAspectI();
    public static final LightningAspectII LIGHTNING_ASPECT_II = new LightningAspectII();

    public static final FireAspectI FIRE_ASPECT_I = new FireAspectI();

    public static final WaterAspectHero WATER_ASPECT_HERO = new WaterAspectHero();
    public static final WaterAspectI WATER_ASPECT_I = new WaterAspectI();

    public static final BloodlustAspectHero BLOODLUST_ASPECT_HERO = new BloodlustAspectHero();
    public static final BloodlustAspectI BLOODLUST_ASPECT_I = new BloodlustAspectI();
    public static final BloodlustAspectII BLOODLUST_ASPECT_II = new BloodlustAspectII();
    public static final BloodlustAspectIII BLOODLUST_ASPECT_III = new BloodlustAspectIII();

    public static final CraftingAspectI CRAFTING_ASPECT_I = new CraftingAspectI();

    // -- EXPANSION 1 -- //

    public static final DemonAspectHero DEMON_ASPECT_HERO = new DemonAspectHero();
    public static final DemonAspectI DEMON_ASPECT_I = new DemonAspectI();
    public static final DemonAspectII DEMON_ASPECT_II = new DemonAspectII();


    // -- ASPECT LIST -- //

    private static final Aspect[] aspectList = new Aspect[]{
            // Lightning Aspect
            LIGHTNING_ASPECT_HERO, LIGHTNING_ASPECT_I, LIGHTNING_ASPECT_II,

            // Fire Aspect
            FIRE_ASPECT_I,

            // Water Aspect
            WATER_ASPECT_HERO, WATER_ASPECT_I,

            // Bloodlust Aspect
            BLOODLUST_ASPECT_HERO, BLOODLUST_ASPECT_I, BLOODLUST_ASPECT_II, BLOODLUST_ASPECT_III,

            // Crafting Aspect
            CRAFTING_ASPECT_I,
    };

    // -- PRIVATE CONSTRUCTOR -- //

    private Aspects() {
    }

    // -- HELPER METHODS -- //

    public static Aspect[] values() {
        return aspectList;
    }

    public static Aspect valueOf(final String name) {
        if (name != null) {
            for (Aspect aspect : aspectList) {
                if (aspect.name().equalsIgnoreCase(name)) {
                    return aspect;
                }
            }
        }
        return null;
    }

    public static Aspect fromId(int id) {
        for (Aspect aspect : aspectList) {
            if (aspect.getId() == id) {
                return aspect;
            }
        }
        return null;
    }

    public static boolean hasAspect(Player player, Aspect aspect) {
        return DGGame.PLAYER_R.fromPlayer(player).hasAspect(aspect);
    }

    public static boolean isInFaction(Faction faction, Aspect aspect) {
        for (Deity inFaction : DGGame.DEITY_R.deitiesInFaction(faction)) {
            for (Aspect.Group group : inFaction.getAspectGroups()) {
                if (Groups.aspectsInGroup(group).contains(aspect)) {
                    return true;
                }
            }
        }
        return false;
    }
}
