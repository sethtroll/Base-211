package com.zenyte.api.client.query;

import com.zenyte.api.client.APIClient;
import com.zenyte.api.model.LoginRequest;
import com.zenyte.game.util.AES;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
@Deprecated
public class ApiAuthenticationCheck {
    private static final Logger log = LoggerFactory.getLogger(ApiAuthenticationCheck.class);
    private final LoginRequest request;

    /*public ApiAuthenticationCheck(final PlayerInformation info) {
        request = new LoginRequest(info.getUsername().replaceAll("_", " "), Objects.requireNonNull(AES.encrypt(info.getPlainPassword(), AES.TEMP_KEY)));
    }*/
    public ApiAuthenticationCheck(final String username, final String password) {
        request = new LoginRequest(username.replaceAll("_", " "), Objects.requireNonNull(AES.encrypt(password, AES.TEMP_KEY)));
    }

    public String execute() {
        final OkHttpClient http = APIClient.CLIENT;
        final RequestBody body = APIClient.jsonBody(request);
        final HttpUrl url = APIClient.urlBuilder().addPathSegment("account").addPathSegment("login").build();
        final Request request = new Request.Builder().url(url).post(body).build();
        // Make the API request
        try {
            try (Response response = http.newCall(request).execute()) {
                final ResponseBody responseBody = response.body();
                if (responseBody == null || !response.isSuccessful()) {
                    return "empty";
                }
                return responseBody.string();
            }
        } catch (final Exception e) {
            log.error("", e);
        }
        return "empty";
    }
}
