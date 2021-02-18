package me.hklee.oakcooldown;

import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.TreeSpecies;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Tree;
import org.bukkit.material.Wood;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.permissions.Permissible;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * SAILOR
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
public class Sailor implements Listener {
    private final Map<UUID, Date> coolmap = new HashMap<>();

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (e.getBlock().getType() != Material.LOG) return;
        System.out.println(e.getBlock().getType());
        Configer c = getConfiger();
        UUID u = p.getUniqueId();
        if (!ignoreCool(p) && isCool(u)) {
                e.setCancelled(true);
                playSound(p);
                p.sendMessage(c.getCooltime(BigDecimal
                        .valueOf((coolmap.get(u).getTime() - new Date().getTime()) / 1000.0)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()
                ));
                return;
        }
        Inventory i = p.getInventory();
        TreeSpecies t = ((Tree) e.getBlock().getState().getData()).getSpecies();
        if(!ignoreNeed(p)){
            if(!hasTR(i, t)) {
                e.setCancelled(true);
                playSound(p);
                p.sendMessage(c.getShortage());
                return;
            }
        }
        if(!ignoreCool(p)) setCool(u);
        if(!ignoreNeed(p)) removeTR(i, t);
    }

    public boolean ignoreCool(Permissible p) { return p.hasPermission("oakcooldown.ignorecool"); }
    public boolean ignoreNeed(Permissible p) { return p.hasPermission("oakcooldown.ignoreneed"); }

    private boolean isCool(UUID u) { return coolmap.containsKey(u) && coolmap.get(u).after(new Date()); }
    private void setCool(UUID u) { coolmap.put(u, new Date(new Date().getTime() + getConfiger().TIME)); }
    private void playSound(Player p) { p.playSound(p.getLocation(), getConfiger().SOUND, SoundCategory.RECORDS, 10, 10); }

    private boolean hasTR(Inventory i, TreeSpecies t) {
        int amount = getConfiger().NEED - 1;
        for(ItemStack is : i.getContents()){
            if(is != null && is.getType() == Material.LOG && ((Tree) is.getData()).getSpecies() == t) {
                amount -= is.getAmount();
                if(amount <= 0) return true;
            }
        }
        return false;
    }
    private void removeTR(Inventory i, TreeSpecies t) {
        int amount = getConfiger().NEED - 1;
        int ind = -1;
        for (ItemStack is : i.getContents()) {
            ind++;
            if (is != null && is.getType() == Material.LOG && ((Tree) is.getData()).getSpecies() == t) {
                int newamount = is.getAmount() - amount;
                if (newamount > 0) {
                    is.setAmount(newamount);
                    i.setItem(ind, is);
                    break;
                } else {
                    i.remove(is);
                    amount = -newamount;
                    if (amount == 0) break;
                }
            }
        }
    }

    private Configer getConfiger() { return Oakcooldown.getInstance().getConfiger(); }
}
