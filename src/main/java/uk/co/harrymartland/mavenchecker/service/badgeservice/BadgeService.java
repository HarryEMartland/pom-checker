package uk.co.harrymartland.mavenchecker.service.badgeservice;

import java.util.concurrent.CompletableFuture;

public interface BadgeService {
    CompletableFuture<String> getBadge(String subject, String status, String colour);
}
