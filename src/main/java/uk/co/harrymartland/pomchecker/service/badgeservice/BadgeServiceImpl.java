package uk.co.harrymartland.pomchecker.service.badgeservice;

import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.co.harrymartland.pomchecker.service.completablefuturehttpclient.CompletableFutureHttpClient;

@Service
public class BadgeServiceImpl implements BadgeService {

    @Value("${maven-checker.badge.host}")
    private String host;
    @Value("${maven-checker.badge.path}")
    private String path;

    @Autowired
    private CompletableFutureHttpClient completableFutureHttpClient;

    @Cacheable("badge")
    @Override
    public CompletableFuture<String> getBadge(String subject, String status, String colour) {
        return completableFutureHttpClient.request(new HttpGet(getBadgeUri(subject, status, colour)))
                .thenApplyAsync(this::toString);
    }

    private String toString(HttpResponse httpResponse) {
        try {
            return CharStreams.toString(new InputStreamReader(
                    httpResponse.getEntity().getContent(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getBadgeUri(String subject, String status, String colour) {
        return host + path.replace("{subject}", subject).replace("{status}", status).replace("{colour}", colour);
    }
}
