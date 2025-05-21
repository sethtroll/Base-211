package com.zenyte.game.world.entity.npc.combatdefs;

import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;

/**
 * @author Kris | 18/11/2018 02:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SpawnDefinitions {
    private int respawnDelay = 25;
    private Animation deathAnimation = Animation.STOP;
    private Animation spawnAnimation = Animation.STOP;
    private SoundEffect deathSound;
    private SoundEffect spawnSound;

    public static SpawnDefinitions construct(final SpawnDefinitions clone) {
        final SpawnDefinitions defs = new SpawnDefinitions();
        if (clone == null) return defs;
        defs.respawnDelay = clone.respawnDelay;
        defs.deathAnimation = clone.deathAnimation;
        defs.spawnAnimation = clone.spawnAnimation;
        defs.deathSound = clone.deathSound;
        defs.spawnSound = clone.spawnSound;
        return defs;
    }

    public int getRespawnDelay() {
        return this.respawnDelay;
    }

    public void setRespawnDelay(final int respawnDelay) {
        this.respawnDelay = respawnDelay;
    }

    public Animation getDeathAnimation() {
        return this.deathAnimation;
    }

    public void setDeathAnimation(final Animation deathAnimation) {
        this.deathAnimation = deathAnimation;
    }

    public Animation getSpawnAnimation() {
        return this.spawnAnimation;
    }

    public void setSpawnAnimation(final Animation spawnAnimation) {
        this.spawnAnimation = spawnAnimation;
    }

    public SoundEffect getDeathSound() {
        return this.deathSound;
    }

    public void setDeathSound(final SoundEffect deathSound) {
        this.deathSound = deathSound;
    }

    public SoundEffect getSpawnSound() {
        return this.spawnSound;
    }

    public void setSpawnSound(final SoundEffect spawnSound) {
        this.spawnSound = spawnSound;
    }
}
