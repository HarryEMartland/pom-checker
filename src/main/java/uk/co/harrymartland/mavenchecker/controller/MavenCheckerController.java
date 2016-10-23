package uk.co.harrymartland.mavenchecker.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import uk.co.harrymartland.mavenchecker.domain.result.UpdateCheckResult;
import uk.co.harrymartland.mavenchecker.service.updatecheckservice.UpdateCheckService;

@RestController()
@RequestMapping(value = "upToDate", method = RequestMethod.GET)
public class MavenCheckerController {

    @Autowired
    private UpdateCheckService updateCheckService;

    @RequestMapping()
    public DeferredResult<UpdateCheckResult> isUpToDate(
            @RequestParam("mavenUrl") String mavenUrl,
            @RequestParam(value = "ignoreVersionsContaining", defaultValue = "") List<String> ignoreVersionsContaining,
            @RequestParam(value = "ignoreDependency", defaultValue = "") List<String> ignoreDependencies,
            DeferredResult<UpdateCheckResult> deferredResult) {

        updateCheckService.check(mavenUrl, ignoreDependencies, ignoreVersionsContaining)
                .exceptionally(UpdateCheckResult::new)
                .thenAcceptAsync(deferredResult::setResult);
        return deferredResult;
    }

}
