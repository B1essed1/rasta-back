package uz.rasta.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SmsService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int CODE_LENGTH = 6;
    private static final long CODE_TTL_MS = 1 * 60 * 1000; // 5 minutes

    private final Map<String, CodeEntry> codeStore = new ConcurrentHashMap<>();

    public void sendCode(String phone) {
        //String code = generateCode();
        String code = generateStaticCode();
        codeStore.put(phone, new CodeEntry(code, System.currentTimeMillis()));

        // Mock SMS sending - just log the code
        log.info("SMS code for {}: {}", phone, code);
    }

    public boolean verifyCode(String phone, String code) {
        CodeEntry entry = codeStore.get(phone);
        if (entry == null) {
            return false;
        }

        if (System.currentTimeMillis() - entry.createdAt > CODE_TTL_MS) {
            codeStore.remove(phone);
            return false;
        }

        if (entry.code.equals(code)) {
            codeStore.remove(phone);
            return true;
        }

        return false;
    }

    private String generateCode() {
        int code = RANDOM.nextInt((int) Math.pow(10, CODE_LENGTH));
        return String.format("%0" + CODE_LENGTH + "d", code);
    }

    private String generateStaticCode() {
        int code = RANDOM.nextInt((int) Math.pow(10, CODE_LENGTH));
        return String.format("%0" + CODE_LENGTH + "d", 123321);
    }

    private record CodeEntry(String code, long createdAt) {
    }
}
