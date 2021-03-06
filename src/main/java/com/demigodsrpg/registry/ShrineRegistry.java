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

package com.demigodsrpg.registry;

import com.demigodsrpg.model.ShrineModel;
import com.demigodsrpg.util.datasection.DataSection;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShrineRegistry extends AbstractDemigodsDataRegistry<ShrineModel> {
    private static final String FILE_NAME = "shrines";

    public Collection<ShrineModel> getShrines(final Location location, final int range) {
        return getRegistered().stream().filter(model -> model.getLocation().getWorld().equals(location.getWorld()) && model.getLocation().distance(location) <= range).collect(Collectors.toList());
    }

    public ShrineModel getShrine(final Location location) {
        Optional<ShrineModel> model = getRegistered().stream().filter(model1 -> model1.getShrineType().getLocations(model1.getLocation()).contains(location)).findAny();

        if (model.isPresent()) {
            return model.get();
        }
        return null;
    }

    public void generate() {
        for (ShrineModel model : getRegistered()) {
            model.getShrineType().generate(model.getLocation());
        }
    }

    @Override
    public ShrineModel valueFromData(String stringKey, DataSection data) {
        return new ShrineModel(stringKey, data);
    }

    @Override
    public String getName() {
        return FILE_NAME;
    }
}
