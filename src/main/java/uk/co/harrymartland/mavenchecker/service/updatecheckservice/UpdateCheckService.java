package uk.co.harrymartland.mavenchecker.service.updatecheckservice;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import uk.co.harrymartland.mavenchecker.domain.result.UpdateCheckResult;

public interface UpdateCheckService {

    CompletableFuture<UpdateCheckResult> check(String mavenUrl, List<String> ignoreDependencies, List<String> ignoreVersionsContaining);

}
