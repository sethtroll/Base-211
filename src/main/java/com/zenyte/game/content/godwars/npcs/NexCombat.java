package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.tasks.TickTask;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.Player;

import java.util.List;

/**
 * @author Kris | 25. okt 2017 : 11:17.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public class NexCombat extends NPC implements Spawnable, CombatScript {
    private static final Projectile rangedProjectile = new Projectile(1999, 41, 16, 30, 5, 10, 0, 5);
    private static final Projectile magicProjectile = new Projectile(1202, 41, 16, 30, 5, 10, 0, 5);
    private static final Animation meleeAnimation = new Animation(9179);
    private static final Animation rangedAnimation = new Animation(9185);
    private static final Animation mageAnimation = new Animation(9181);
    private static final Graphics MAGIC_HIT_GFX = new Graphics(281);
    private static int attackCycle = 0;
    public NexCombat(final int id, final Location tile, final Direction direction, final int radius) {
        super(id, tile, direction, radius);
        if (isAbstractNPC() || tile.getX() >= 6400) return;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == NpcId.NEX;
    }

    @Override
    public int attack(final Entity target) {
        final NexCombat npc = this;
        if(target.getPosition().withinDistance(npc.getPosition(), 1)){
            npc.setAnimation(meleeAnimation);
            for(final Entity t : npc.getPossibleTargets(EntityType.PLAYER)) {
                if(t.getPosition().withinDistance(npc.getPosition(), 1)) {
                    delayHit(npc, 3, t, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, t), HitType.MELEE));
                }
            }
        }
        else if(attackCycle < 2) {
            npc.setAnimation(mageAnimation);
            for (final Entity t : npc.getPossibleTargets(EntityType.PLAYER)) {
                int damage = getRandomMaxHit(npc, 35, MAGIC, t);
                if (damage > 0) {
                    damage = Utils.random(15, 35);
                }
                delayHit(this, World.sendProjectile(this, t, magicProjectile), t, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), AttackType.MAGIC, target), HitType.MAGIC).onLand(hit -> t.setGraphics(MAGIC_HIT_GFX)));

            }
            attackCycle++;
        }else {
            npc.setAnimation(rangedAnimation);
            for (final Entity t : npc.getPossibleTargets(EntityType.PLAYER)) {
                int damage = getRandomMaxHit(npc, 25, RANGED, t);
                if (damage > 0) {
                    damage = Utils.random(10, 25);
                }
                delayHit(this, World.sendProjectile(this, t, rangedProjectile), t, new Hit(this, Utils.random(getCombatDefinitions().getMaxHit()), HitType.RANGED));
            }
            attackCycle++;
            if(attackCycle == 4){
                attackCycle = 0;
            }
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }


    @Override
    public void sendDeath() {
        onDeath(getAttackers().toArray(new Entity[0]));
        final NPCCombatDefinitions defs = getCombatDefinitions();
        final String name = getDefinitions().getName().toLowerCase();
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                setAnimation(defs.getSpawnDefinitions().getDeathAnimation());
                if (ticks == 0) {
                    for (Entity t : getAttackers()) {
                        Player source = (Player) t;


                    }

                    onFinish(getAttackers().toArray(new Entity[0]));
                    GameCommands.nex_started = false;
                    stop();
                    remove();
                }
            }
        }, 0, 1);
        ;}




@Override
public void drop(final Location tile){
    for(Entity t : getAttackers()){
        Player player = (Player) t;
        final List<DropProcessor> processors = DropProcessorLoader.get(id);
        if (processors != null) {
            for (final DropProcessor processor : processors) {
                processor.onDeath(this, player);
            }
        }
        final NPCDrops.DropTable drops = NPCDrops.getTable(getId());
        if (player == null) {
            return;
        }
        NPCDrops.forEach(drops, drop -> dropItem(player, drop, tile));
        GameCommands.nex_started = false;

    }

}



    @Override
    protected void onFinish(final Entity source) {
        drop(getMiddleLocation());
        reset();
        finish();
        sendNotifications((Player) source);
        GameCommands.nex_started = false;
    }

    @Override
    public int getRespawnDelay() {
        return -1;
    }
}
