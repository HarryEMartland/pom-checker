package uk.co.harrymartland.pomchecker.service.updatecheckservice;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import uk.co.harrymartland.pomchecker.domain.pom.Dependency;
import uk.co.harrymartland.pomchecker.domain.pom.Project;
import uk.co.harrymartland.pomchecker.domain.result.DependencyDifference;
import uk.co.harrymartland.pomchecker.domain.result.DependencyResult;
import uk.co.harrymartland.pomchecker.domain.result.UpdateCheckResult;
import uk.co.harrymartland.pomchecker.service.dependencyresult.DependencyResultService;
import uk.co.harrymartland.pomchecker.service.mavendownloader.MavenDownloader;
import uk.co.harrymartland.pomchecker.service.mavenversiondownloader.MavenVersionDownloaderService;

@Service
public class UpdateCheckServiceImpl implements UpdateCheckService {

    private static final DependencyResult ERROR_DEPENDENCY_RESULT = new DependencyResult("ERROR", "ERROR", "ERROR", "ERROR", DependencyDifference.ERROR);

    @Autowired
    private MavenDownloader mavenDownloader;

    @Autowired
    private DependencyResultService dependencyResultService;

    @Autowired
    private MavenVersionDownloaderService mavenVersionDownloaderService;

    @Override
    public CompletableFuture<UpdateCheckResult> check(String mavenUrl, List<String> ignoreDependencies, List<String> ignoreVersionsContaining) {

        return mavenDownloader.download(mavenUrl).thenCompose(pom -> ofAll(getDependencies(pom)
                .map(dependency -> getResult(dependency, ignoreDependencies, ignoreVersionsContaining))
                .collect(Collectors.toList()))
                .thenApplyAsync(this::createResult));
    }

    private Stream<Dependency> getDependencies(Project pom){
        if(Objects.nonNull(pom.getParent())){
            return Stream.concat(pom.getDependencies().stream(), Stream.of(pom.getParent()));
        }else {
            return pom.getDependencies().stream();
        }
    }

    private CompletableFuture<DependencyResult> getResult(Dependency dependency, List<String> ignoreDependencies, List<String> ignoreVersions) {

        return mavenVersionDownloaderService.getMetaData(dependency.getGroupId(), dependency.getArtifactId())
                .thenApplyAsync(metaData -> dependencyResultService.createDependencyResult(metaData, dependency, ignoreDependencies, ignoreVersions))
                .exceptionally(throwable -> new DependencyResult(dependency.getArtifactId(), dependency.getGroupId(), dependency.getVersion(), "ERROR", DependencyDifference.ERROR));
    }

    private <T> CompletableFuture<List<CompletableFuture<T>>> ofAll(List<CompletableFuture<T>> join) {
        return CompletableFuture.allOf(join.toArray(new CompletableFuture[join.size()]))
                .thenApplyAsync(v -> join);
    }

    private DeferredResult<UpdateCheckResult> setErrorResult(Throwable e, DeferredResult<UpdateCheckResult> deferredResult) {
        UpdateCheckResult updateCheckResult = new UpdateCheckResult(e);
        deferredResult.setResult(updateCheckResult);
        return deferredResult;
    }

    private UpdateCheckResult createResult(List<CompletableFuture<DependencyResult>> mavenSearchStream) {
        return new UpdateCheckResult(mavenSearchStream.stream()
                .map(dependencyResultCompletableFuture -> dependencyResultCompletableFuture.getNow(ERROR_DEPENDENCY_RESULT))
                .filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
