package com.zenyte.game.world.entity.player.dialogue;

public enum Expression {

    CALM(588),
    ANXIOUS(589),
    CALM_TALK(590),
    DEFAULT(591),
    EVIL(592),
    BAD(593),
    WICKED(594),
    ANNOYED(595),
    DISTRESSED(596),
    AFFLICTED(597),
    ALMOST_CRYING(598),
    DRUNK_LEFT(600),
    DRUNK_RIGHT(601),
    DISINTERESTED(602),
    SLEEPY(603),
    PLAIN_EVIL(604),
    LAUGH(604),
    SNIGGER(606),
    HAVE_FUN(607),
    GUFFAW(608),
    EVIL_LAUGH(609),
    SAD(610),
    MORE_SAD(611),
    ON_ONE_HAND(612),
    NEARLY_CRYING(613),
    ANGRY(614),
    FURIOUS(615),
    ENRAGED(616),
    MAD(617),
    WEISS_TROLL_NORMAL(8154),
    ORRVOR_QUO_MATEN(8215),
    IKKLE_HYDRA(8265),
    VERZIK_ENJOY(8055),
    VERZIK_OTHERWISE(8054),
    HIGH_REV_NORMAL(15073),
    HIGH_REV_SHOCKED(15075),
    HIGH_REV_SAD(15076),
    HIGH_REV_SCARED(15077),
    HIGH_REV_MAD(15078),
    HIGH_REV_WONDERING(15079),
    HIGH_REV_JOLLY(15080),
    HIGH_REV_HAPPY(15081),
    ;

    private final int id;

    Expression(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
