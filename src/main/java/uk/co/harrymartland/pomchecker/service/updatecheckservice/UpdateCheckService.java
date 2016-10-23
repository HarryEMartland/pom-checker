package uk.co.harrymartland.pomchecker.service.updatecheckservice;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import uk.co.harrymartland.pomchecker.domain.result.UpdateCheckResult;

public interface UpdateCheckService {

    CompletableFuture<UpdateCheckResult> check(String mavenUrl, List<String> ignoreDependencies, List<String> ignoreVersionsContaining);

}
