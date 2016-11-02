package uk.co.harrymartland.pomchecker.service.updatecheckservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
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

        return mavenDownloader.download(mavenUrl).thenCompose(pom -> {
            Map<String, String> properties = getProperties(pom);
            return ofAll(getDependencies(pom)
                    .map(dependency -> replaceProperties(dependency, properties))
                    .map(dependency -> getResult(properties, dependency, ignoreDependencies, ignoreVersionsContaining))
                    .collect(Collectors.toList()))
                    .thenApplyAsync(this::createResult);
        });
    }

    private Dependency replaceProperties(Dependency dependency, Map<String,String> properties){
        return new Dependency(
                replaceProperties(dependency.getGroupId(), properties),
                replaceProperties(dependency.getArtifactId(), properties),
                replaceProperties(dependency.getVersion(), properties)
        );
    }

    private Stream<Dependency> getDependencies(Project pom) {
        if (Objects.nonNull(pom.getParent())) {
            return Stream.concat(pom.getDependencies().stream(), Stream.of(pom.getParent()));
        } else {
            return pom.getDependencies().stream();
        }
    }

    private CompletableFuture<DependencyResult> getResult(Map<String, String> properties, Dependency dependency, List<String> ignoreDependencies, List<String> ignoreVersions) {

        return mavenVersionDownloaderService.getMetaData(replaceProperties(dependency.getGroupId(), properties), replaceProperties(dependency.getArtifactId(), properties))
                .thenApplyAsync(metaData -> dependencyResultService.createDependencyResult(metaData, dependency, ignoreDependencies, ignoreVersions, properties))
                .exceptionally(throwable -> new DependencyResult(dependency.getArtifactId(), dependency.getGroupId(), dependency.getVersion(), "ERROR", DependencyDifference.ERROR));
    }

    private String replaceProperties(String value, Map<String, String> properties) {
        if(value == null){
            return null;
        }
        String updated = value;
        for (Map.Entry<String, String> property : properties.entrySet()) {
            updated = updated.replace(property.getKey(), property.getValue());
        }
        return updated;
    }

    private Map<String, String> getProperties(Project project) {
        Map<String, String> properties = new HashMap<>();
        properties.put("${project.groupId}", project.getGroupId());

        for (Element element : project.getProperties()) {
            properties.put("${" + element.getTagName() + "}", element.getChildNodes().item(0).getTextContent());
        }

        return properties;
    }

    private <T> CompletableFuture<List<CompletableFuture<T>>> ofAll(List<CompletableFuture<T>> join) {
        return CompletableFuture.allOf(join.toArray(new CompletableFuture[join.size()]))
                .thenApplyAsync(v -> join);
    }

    private UpdateCheckResult createResult(List<CompletableFuture<DependencyResult>> mavenSearchStream) {
        return new UpdateCheckResult(mavenSearchStream.stream()
                .map(dependencyResultCompletableFuture -> dependencyResultCompletableFuture.getNow(ERROR_DEPENDENCY_RESULT))
                .filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
