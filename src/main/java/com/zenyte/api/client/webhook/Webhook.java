package com.zenyte.api.client.webhook;

import com.zenyte.Constants;
import com.zenyte.api.client.APIClient;
import com.zenyte.api.client.webhook.model.DiscordWebhook;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.constants.GameConstants;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Corey
 * @since 05/04/2020
 */
public abstract class Webhook {
    public static final int EMBED_COLOUR = 16743680;
    private static final Logger log = LoggerFactory.getLogger(Webhook.class);
    private static final OkHttpClient client = new OkHttpClient.Builder().build();
    private final HttpUrl webhookUrl;

    /**
     * @param channelId First segment of Discord webhook url
     * @param token     Last segment of Discord webhook url
     */
    public Webhook(final String channelId, final String token) {
        this.webhookUrl = new HttpUrl.Builder().scheme("https").host("discordapp.com").addPathSegment("api").addPathSegment("webhooks").addPathSegment(channelId).addPathSegment(token).build();
    }

    public abstract DiscordWebhook buildMessage();

    public void execute() {
        if (!canExecute()) {
            return;
        }
        CoresManager.getServiceProvider().submit(this::post);
    }

    public boolean canExecute() {
        return !Constants.WORLD_PROFILE.isBeta() && !Constants.WORLD_PROFILE.isDevelopment() && !Constants.WORLD_PROFILE.isPrivate();
    }

    // Default avatar url if one is not specified in the child class.
    public String getAvatarUrl() {
        return "https://Pharaoh.co.uk/ruinous.png";
    }

    // Default username to use if one is not specified in the child class.
    public String getUsername() {
        return GameConstants.SERVER_NAME;
    }

    private void post() {
        final DiscordWebhook message = buildMessage();
        if (message.getUsername() == null) {
            message.setUsername(getUsername());
        }
        if (message.getAvatarUrl() == null) {
            message.setAvatarUrl(getAvatarUrl());
        }
        final String messageString = APIClient.GSON.toJson(message);
        final Request request = new Request.Builder().url(webhookUrl).post(RequestBody.create(APIClient.JSON, messageString)).build();
        log.info(String.format("[%s] Sending Discord webhook", this.getClass().getSimpleName()));
        try {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.warn("Failed to send Discord webhook, response: " + response.message());
                    log.warn("Json sent: " + messageString);
                }
            }
        } catch (final Exception e) {
            log.error("", e);
            log.error("Json sent: " + messageString);
        }
    }
}
