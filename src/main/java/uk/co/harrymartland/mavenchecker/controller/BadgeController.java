package uk.co.harrymartland.mavenchecker.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import uk.co.harrymartland.mavenchecker.service.badgeservice.BadgeService;
import uk.co.harrymartland.mavenchecker.service.updatecheckservice.UpdateCheckService;

@RestController
@RequestMapping(value = "/badge", method = RequestMethod.GET)
public class BadgeController {

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private UpdateCheckService updateCheckService;

    @RequestMapping("upToDate.svg")
    public DeferredResult<String> upToDate(
            @RequestParam("mavenUrl") String mavenUrl,
            @RequestParam(value = "ignoreVersionsContaining", defaultValue = "") List<String> ignoreVersionsContaining,
            @RequestParam(value = "ignoreDependency", defaultValue = "") List<String> ignoreDependencies,
            DeferredResult<String> deferredResult) {

        updateCheckService.check(mavenUrl, ignoreDependencies, ignoreVersionsContaining)
                .thenCompose(updateCheckResult -> badgeService.getBadge("Dependencies",
                        updateCheckResult.getDependencyDifference().getBadgeText(),
                        updateCheckResult.getDependencyDifference().getBadgeColor())
                ).exceptionally(Throwable::getMessage)
                .thenAcceptAsync(deferredResult::setResult);

        return deferredResult;
    }
}
