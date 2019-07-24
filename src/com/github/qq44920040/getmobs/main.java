package com.github.qq44920040.getmobs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;

public class main extends JavaPlugin implements Listener {

    private HashMap<Integer,MobEntity> DamgeMap = new HashMap<>();

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Player player = (Player) sender;
        //&&sender.hasPermission("spawnsupermob.use")
        if (args.length==11&&sender.hasPermission("spawnsupermob.use")){
            String[] split = args[0].split("\\|");
            int Damage = Integer.parseInt(args[1]);
            int Health = Integer.parseInt(args[2]);
            EntityType entityType = EntityType.fromName(args[3]);
            String DisPlayerName = args[4];
            ItemStack itemStack = new ItemStack(Integer.parseInt(args[5]));
            final boolean onDamage = Boolean.parseBoolean(args[6]);
            int Delay = Integer.parseInt(args[7]);
            final LivingEntity entity = (LivingEntity)player.getWorld().spawnEntity(new Location(player.getWorld(),Double.parseDouble(split[0]),Double.parseDouble(split[1]),Double.parseDouble(split[2])), entityType);
            final int entityId = entity.getEntityId();
            entity.addPotionEffect(new PotionEffect(PotionEffectType.getById(Integer.parseInt(args[8])),Integer.parseInt(args[9]),Integer.parseInt(args[10])),true);
            DamgeMap.put(entityId,new MobEntity(Damage,sender.getName(),onDamage));
            entity.setMaxHealth((double) Health);
            entity.setHealth((double) Health);
            entity.setCustomNameVisible(true);
            entity.setCustomName(DisPlayerName);
            entity.setRemoveWhenFarAway(true);

            if (args[3].equalsIgnoreCase("Zombie")){
                Zombie zombie = (Zombie) entity;
                zombie.getEquipment().setItemInHand(itemStack);
                //System.out.println("进来了");
            }else if (args[3].equalsIgnoreCase("PigZombie")){
                PigZombie pigZombie = (PigZombie) entity;
                pigZombie.getEquipment().setItemInHand(itemStack);
                //System.out.println("进来了");
            }else if(args[3].equalsIgnoreCase("Skeleton")){
                Skeleton Skeleton = (Skeleton) entity;
                Skeleton.getEquipment().setItemInHand(itemStack);
                //System.out.println("进来了");
            }
            //sender.sendMessage("§e§l召唤成功");
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (onDamage){
                        DamgeMap.remove(entityId);
                    }
                    entity.remove();
                    //player.sendMessage("§e§l召唤物已收回");
                }
            }.runTaskLater(this,20L*Delay);

        }
        return super.onCommand(sender, command, label, args);
    }


    @EventHandler
    public void EntityDath(EntityDeathEvent event){
        int entityId = event.getEntity().getEntityId();
        if (DamgeMap.containsKey(entityId)){
            Player killer = event.getEntity().getKiller();
            MobEntity mobEntity = DamgeMap.get(entityId);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),getConfig().getString("KillCmd").replace("{Killername}",killer.getName()).replace("{MobMaster}",mobEntity.getPlayername()));
        }
    }

    @EventHandler
    public void PlayerDamageEntity(EntityDamageByEntityEvent event){
        if (event.getEntityType()!=EntityType.PLAYER){
            //System.out.println("不是玩家回去");
            return;
        }
        int entityId = event.getDamager().getEntityId();
        if (DamgeMap.containsKey(entityId)){
            Player entity = (Player)event.getEntity();
            //System.out.println("在里面");
            MobEntity mobEntity = DamgeMap.get(entityId);
            event.setDamage(mobEntity.getDamage());
            if (mobEntity.Nodamage&&mobEntity.getPlayername().equalsIgnoreCase(entity.getName())){
                //System.out.println("攻击取消");
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(),"config.yml");
        if (!file.exists()){
            saveDefaultConfig();
        }
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        super.onEnable();
    }
}
