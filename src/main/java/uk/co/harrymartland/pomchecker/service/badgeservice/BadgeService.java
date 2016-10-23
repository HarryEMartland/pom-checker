package uk.co.harrymartland.pomchecker.service.badgeservice;

import java.util.concurrent.CompletableFuture;

public interface BadgeService {
    CompletableFuture<String> getBadge(String subject, String status, String colour);
}
