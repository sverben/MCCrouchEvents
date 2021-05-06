package nl.sverben;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.Random;

public class crouch extends JavaPlugin implements Listener {
    String events = "none";
    int effectlength = 1000;

    @Override
    public void onEnable() {
        Logger logger = getLogger();
        logger.info("CrouchEvents enabled");
        getServer().getPluginManager().registerEvents(this, this);
    }

    private Material getRandomBlock() {
        List<Material> blocks = new ArrayList<Material>();
        for (Material block : Material.values()) {
            if (block.isBlock() && block.isSolid() && !block.isAir()) {
                blocks.add(block);
            }
        }
        Material randomblock = blocks.get(new Random().nextInt(blocks.size()));

        return randomblock;

    }

    private PotionEffect getRandomEffect() {
        List<PotionEffectType> effects = new ArrayList<PotionEffectType>();
        for (PotionEffectType effect : PotionEffectType.values()) {
            effects.add(effect);
        }
        PotionEffectType randomeffecttype = effects.get(new Random().nextInt(effects.size()));

        PotionEffect randomeffect = new PotionEffect(randomeffecttype, effectlength, 0);

        return randomeffect;
    }

    private Enchantment getRandomEnchantment() {
        List<Enchantment> enchantments = new ArrayList<Enchantment>();
        for(Enchantment enchantment : Enchantment.values()) {
            enchantments.add(enchantment);
        }

        Enchantment randomenchant = enchantments.get(new Random().nextInt(enchantments.size()));

        return randomenchant;
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        Block block = world.getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ());
        if (player.isSneaking() && !block.getType().isAir()) {
            if (events == "block") {
                block.setType(getRandomBlock());
            }
        }
    }

    @EventHandler
    public void OnPlayerCrouchEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking()) {
            if (events == "effect") {
                player.addPotionEffect(getRandomEffect());
            }
            if (events == "enchant") {
                try {
                    player.getItemInHand().addUnsafeEnchantment(getRandomEnchantment(), new Random().nextInt(100 - 1) + 1);
                } catch (Exception e){};
            }
        }
    }

    private Boolean EventCommand(String[] args, Player player) {
        if (args[0].equals("effect")) {
            events = "effect";

            try {
                effectlength = Integer.parseInt(args[1]) * 20;
            } catch (Exception e) {
                    effectlength = 1000;
            }

            return true;
        } else if (args[0].equals("block")) {
            events = "block";
            return true;
        } else if (args[0].equals("enchant")) {
            events = "enchant";
            return true;
        } else if (args[0].equals("none")) {
            events = "none";
            return true;
        }
        else {
            return false;
        }
    }

    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("event")) {
            if (EventCommand(args, player)) {
                player.sendMessage(ChatColor.GREEN + "Set the event to: " + args[0]);
            } else {
                player.sendMessage(ChatColor.RED + "The event " + args[0] + " doesnt exist");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("event")) {
            if (args.length == 1) {
                List<String> arguments = new ArrayList<>();
                arguments.add("effect");
                arguments.add("block");
                arguments.add("enchant");
                arguments.add("none");

                return arguments;
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("effect")) {
                List<String> arguments = new ArrayList<>();

                return arguments;
            }
        }
        return null;
    }
}
