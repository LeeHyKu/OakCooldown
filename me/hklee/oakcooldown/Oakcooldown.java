package me.hklee.oakcooldown;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * CORE
 *
 * Copyright (C) 2021 Hyun-Ku Lee
 * This program is free software: you can redistribute it and/or modify
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
public final class Oakcooldown extends JavaPlugin {
    private static Oakcooldown instance = null;
    public static Oakcooldown getInstance() { return instance; }

    private Configer configer;
    private Sailor sailor;
    public Configer getConfiger() { return configer; }
    public Sailor getSailor() { return sailor; }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        saveDefaultConfig();
        configer = new Configer(getConfig());
        sailor = new Sailor();
        getServer().getPluginManager().registerEvents(sailor, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }
}
