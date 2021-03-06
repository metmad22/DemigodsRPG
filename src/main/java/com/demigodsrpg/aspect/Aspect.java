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

package com.demigodsrpg.aspect;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public interface Aspect {
    default Group getGroup() {
        return null;
    }

    default ItemStack getItem() {
        return null;
    }

    int getId();

    String[] getInfo();

    Tier getTier();

    String getName();

    default String name() {
        return getName();
    }

    enum Tier {
        I, II, III, HERO, CUSTOM
    }

    interface Group {
        String getName();

        ChatColor getColor();

        Sound getSound();

        MaterialData getClaimMaterial();
    }
}
