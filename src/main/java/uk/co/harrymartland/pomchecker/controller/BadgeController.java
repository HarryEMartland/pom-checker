package uk.co.harrymartland.pomchecker.controller;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import uk.co.harrymartland.pomchecker.service.badgeservice.BadgeService;
import uk.co.harrymartland.pomchecker.service.updatecheckservice.UpdateCheckService;

@RestController
@RequestMapping(value = "/badge", method = RequestMethod.GET)
public class BadgeController {

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private UpdateCheckService updateCheckService;

    @Value("${maven-checker.cacheControl.badge.size}")
    private long badgeCacheSize;

    @Value("${maven-checker.cacheControl.badge.unit}")
    private String badgeCacheUnit;

    private Long badgeCacheSizeInSeconds;

    private Long getBadgeCacheSizeInSeconds(){
        if(Objects.isNull(badgeCacheSizeInSeconds)){
            badgeCacheSizeInSeconds = TimeUnit.SECONDS.convert(badgeCacheSize, TimeUnit.valueOf(badgeCacheUnit));
        }
        return badgeCacheSizeInSeconds;
    }

    @RequestMapping("upToDate.svg")
    public DeferredResult<String> upToDate(
            @RequestParam("mavenUrl") String mavenUrl,
            @RequestParam(value = "ignoreVersionsContaining", defaultValue = "") List<String> ignoreVersionsContaining,
            @RequestParam(value = "ignoreDependency", defaultValue = "") List<String> ignoreDependencies,
            final HttpServletResponse response,
            DeferredResult<String> deferredResult) {

        response.setHeader("Cache-Control", "max-age=" + getBadgeCacheSizeInSeconds());

        updateCheckService.check(mavenUrl, ignoreDependencies, ignoreVersionsContaining)
                .thenCompose(updateCheckResult -> badgeService.getBadge("Dependencies",
                        updateCheckResult.getDependencyDifference().getBadgeText(),
                        updateCheckResult.getDependencyDifference().getBadgeColor())
                ).exceptionally(Throwable::getMessage)
                .thenAcceptAsync(deferredResult::setResult);

        return deferredResult;
    }
}
