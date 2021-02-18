package me.hklee.oakcooldown;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * CONFIG
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
public class Configer {
    public final int TIME;
    public final int NEED;

    public final String COOLTIME;
    public final String SHORTAGE;

    public final Sound SOUND;

    public String getCooltime(double remain) { return COOLTIME.replace("<time>", Double.toString(remain));}
    public String getShortage() { return SHORTAGE; }

    public Configer(FileConfiguration f){
        TIME = f.getInt("time");
        NEED = f.getInt("need");

        COOLTIME = f.getString("cooltime");
        SHORTAGE = f.getString("shortage").replace("<need>", Integer.toString(NEED - 1));

        SOUND = Sound.valueOf(f.getString("sound"));
    }
}
