package com.zenyte.api.client.webhook;

import com.zenyte.api.client.webhook.model.DiscordWebhook;
import com.zenyte.api.client.webhook.model.EmbedObject;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.punishments.Punishment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Corey
 * @since 05/04/2020
 */
public class RevokePunishmentWebhook extends Webhook {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
    private final Player revoker;
    private final String revokeReason;
    private final Punishment punishment;

    public RevokePunishmentWebhook(final Player revoker, final String revokeReason, final Punishment punishment) {
        super("696152982395551784", "MTIwMDQ1MDUyODQ1MTIzNTk1MQ.G4By9j.gPcL2pg9YHHoiHdPjiPwDaBk6y-ZrdJ1QruUlY");
        this.revoker = revoker;
        this.revokeReason = revokeReason;
        this.punishment = punishment;
    }

    private String formatDate(final Date date) {
        if (date == null) {
            return "Never";
        }

        return dateFormat.format(date);
    }

    @Override
    public DiscordWebhook buildMessage() {
        return DiscordWebhook.builder()
                .embed(EmbedObject.builder()
                        .author(new EmbedObject.Author("Punishment revoked", "https://Pharaoh.co.uk/img/", "https://Pharaoh.co.uk/img/2347.png"))
                        .description("-")
                        .footer(new EmbedObject.Footer("Pharaoh Punishments", getAvatarUrl()))
                        .timestamp(new Date())
                        .color(Webhook.EMBED_COLOUR)
                        .field("Offender", Utils.formatString(punishment.getOffender()), true)
                        .field("Type", punishment.getType().getFormattedString(), true)
                        .field("Reporter", Utils.formatString(punishment.getReporter()), true)
                        .field("Date", formatDate(punishment.getTimeOfPunishment()), true)
                        .field("Duration", punishment.getDurationInHours() == 0 ? "Forever" : punishment.getDurationInHours() + "hrs", true)
                        .field("Expiration", formatDate(punishment.getExpirationDate()), true)
                        .field("Original Reason", "`" + punishment.getReason() + "`", false)
                        .field("Staff Member", revoker.getName(), false)
                        .field("Revoke Reason", "`" + revokeReason + "`", false)
                        .build())
                .build();
    }
}
