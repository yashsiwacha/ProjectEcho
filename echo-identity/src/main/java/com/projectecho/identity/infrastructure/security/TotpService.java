package com.projectecho.identity.infrastructure.security;

import com.projectecho.identity.application.MfaService;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

/**
 * Standard RFC 6238 Time-Based One-Time Password (TOTP) validator service (SEC-02). Works
 * out-of-the-box with standard Authenticator applications.
 */
@Service
public class TotpService implements MfaService {

    private static final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final int CODE_DIGITS = 6;
    private static final int TIME_WINDOW_SEC = 30;

    private final SecureRandom secureRandom = new SecureRandom();

    /** Generates a new 16-character Base32 TOTP secret. */
    public String generateSecretKey() {
        final StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(BASE32_CHARS.charAt(secureRandom.nextInt(BASE32_CHARS.length())));
        }
        return sb.toString();
    }

    /** Verifies a 6-digit TOTP code against the provided secret. */
    public boolean verifyCode(final String secret, final int code) {
        if (secret == null || secret.isBlank()) {
            return false;
        }
        final long timeIndex = System.currentTimeMillis() / 1000 / TIME_WINDOW_SEC;
        // Verify with window of 1 interval to handle minor clock drifts
        for (int i = -1; i <= 1; i++) {
            if (getOtp(secret, timeIndex + i) == code) {
                return true;
            }
        }
        return false;
    }

    private int getOtp(final String secret, final long timeIndex) {
        try {
            final byte[] key = decodeBase32(secret);
            final byte[] data = ByteBuffer.allocate(8).putLong(timeIndex).array();

            final Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            final byte[] hash = mac.doFinal(data);

            final int offset = hash[hash.length - 1] & 0xf;
            final int binary =
                    ((hash[offset] & 0x7f) << 24)
                            | ((hash[offset + 1] & 0xff) << 16)
                            | ((hash[offset + 2] & 0xff) << 8)
                            | (hash[offset + 3] & 0xff);

            return binary % (int) Math.pow(10, CODE_DIGITS);
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            return -1;
        }
    }

    private byte[] decodeBase32(final String base32) {
        final String normalized = base32.toUpperCase().replace("-", "").replace(" ", "");
        final int length = (normalized.length() * 5) / 8;
        final byte[] bytes = new byte[length];

        int buffer = 0;
        int next = 0;
        int bitsLeft = 0;

        for (int i = 0; i < normalized.length(); i++) {
            final char character = normalized.charAt(i);
            final int val = BASE32_CHARS.indexOf(character);
            if (val < 0) {
                throw new IllegalArgumentException("Invalid Base32 character");
            }

            buffer = (buffer << 5) | val;
            bitsLeft += 5;
            if (bitsLeft >= 8) {
                if (next < length) {
                    bytes[next++] = (byte) (buffer >> (bitsLeft - 8));
                }
                bitsLeft -= 8;
            }
        }
        return bytes;
    }
}
