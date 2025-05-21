package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.tasks.WorldTask;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.Player;


/**
 * @author Kris | 27/11/2018 11:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NightmareEvent extends NPCPlugin {

    private static final Animation POKE_ANIM = new Animation(827);

    private static final Animation VORKATH_RISE_ANIM = new Animation(8567);

    private static final SoundEffect pokeSound = new SoundEffect(2581);

    private static final SoundEffect vorkathWakeSound = new SoundEffect(1522, 10, 0);

    private static final SoundEffect vorkathWakeSoundContinue = new SoundEffect(1518, 10, 120);

    @Override
    public void handle() {
        bind("disturb", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                if (GameCommands.event_started == false) {
                    return;
                }
                if (npc.isLocked()) {
                    return;
                }
                player.setAnimation(POKE_ANIM);
                player.sendSound(pokeSound);
                WorldTasksManager.schedule(new WorldTask() {

                    private int ticks;

                    @Override
                    public void run() {
                        if (ticks++ == 0) {

                            npc.setAnimation(VORKATH_RISE_ANIM);
                            final Location middle = npc.getMiddleLocation();
                            vorkathWakeSound.sendGlobal(middle);
                            vorkathWakeSoundContinue.sendGlobal(middle);
                            return;
                        }
                        npc.setTransformation(378);
                        npc.remove();

                        int NM_NPC = 9416;
                        Location START = new Location(3870, 9949, 3);
                        World.spawnNPC(NM_NPC, START, Direction.SOUTH, 0);
                        stop();
                    }
                }, 0, 0);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc), () -> {
                    player.stopAll();
                    player.faceEntity(npc);
                    handle(player, npc);
                    npc.lock();
                    npc.isLocked();
                }, true));
            }
        });
}
    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.THE_NIGHTMARE };
    }
}
