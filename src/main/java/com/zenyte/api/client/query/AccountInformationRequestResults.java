package com.zenyte.api.client.query;

import com.zenyte.api.client.APIClient;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author Corey
 * @since 07/05/19
 */
public class AccountInformationRequestResults {
    private final int memberId;
    private final long joinDate;
    private final int unreadMessageCount;
    @Nullable
    private final String hashedPassword;
    private final boolean twoFactorAuthentication;

    AccountInformationRequestResults(final String jsonInput) {
        final Map results = APIClient.fromJson(Map.class, jsonInput);
        this.memberId = Integer.parseInt(results.get("member_id").toString());
        this.joinDate = Long.parseLong(results.get("joined").toString()) * 1000;
        this.unreadMessageCount = Integer.parseInt(results.get("msg_count_new").toString());
        final Object hashResult = results.get("members_pass_hash");
        this.hashedPassword = hashResult == null ? null : hashResult.toString();
        this.twoFactorAuthentication = "enabled".equals(results.get("mfa_details"));
    }

    public int getMemberId() {
        return this.memberId;
    }

    public long getJoinDate() {
        return this.joinDate;
    }

    public int getUnreadMessageCount() {
        return this.unreadMessageCount;
    }

    @Nullable
    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public boolean isTwoFactorAuthentication() {
        return this.twoFactorAuthentication;
    }
}
