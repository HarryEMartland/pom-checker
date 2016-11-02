package uk.co.harrymartland.pomchecker.service.dependencyresult;

import static java.util.Objects.isNull;
import static uk.co.harrymartland.pomchecker.domain.result.DependencyDifference.BEHIND;
import static uk.co.harrymartland.pomchecker.domain.result.DependencyDifference.IGNORED;
import static uk.co.harrymartland.pomchecker.domain.result.DependencyDifference.MINOR_VERSION_BEHIND;
import static uk.co.harrymartland.pomchecker.domain.result.DependencyDifference.UP_TO_DATE;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.springframework.stereotype.Service;
import uk.co.harrymartland.pomchecker.domain.maven.MetaData;
import uk.co.harrymartland.pomchecker.domain.pom.Dependency;
import uk.co.harrymartland.pomchecker.domain.result.DependencyDifference;
import uk.co.harrymartland.pomchecker.domain.result.DependencyResult;

@Service
public class DependencyResultServiceImpl implements DependencyResultService {

    @Override
    public DependencyResult createDependencyResult(MetaData metaData, Dependency dependency, List<String> ignoreDependencies, List<String> ignoreVersionsContaining, Map<String, String> properties) {

        final String latestVersion = getLatestVersionExcludingVersions(metaData, ignoreVersionsContaining);
        final DependencyDifference difference;
        if (isIgnoredDependency(dependency, ignoreDependencies) || isNull(dependency.getVersion())) {
            difference = IGNORED;
        }
        else {
            difference = getDifference(dependency.getVersion(), latestVersion);
        }
        return new DependencyResult(dependency.getArtifactId(), dependency.getGroupId(),
                dependency.getVersion(), latestVersion, difference);
    }

    private boolean isIgnoredDependency(Dependency dependency, List<String> ignoreDependencies) {
        return ignoreDependencies.contains(dependency.getGroupId() + "." + dependency.getArtifactId());
    }

    private String getLatestVersionExcludingVersions(MetaData metaData, List<String> ignoreVersionsContaining) {
        return Lists.reverse(metaData.getVersioning().getVersions()).stream()
                .filter(version -> !ignoreVersionsContaining.stream()
                        .filter(version::contains)
                        .findFirst()
                        .isPresent())
                .findFirst().orElse("Error finding version");
    }

    private DependencyDifference getDifference(String projectVersion, String latestVersion) {
        if (Objects.equals(projectVersion, latestVersion)) {
            return UP_TO_DATE;
        } else {
            DefaultArtifactVersion projectVersionObject = new DefaultArtifactVersion(projectVersion);
            int projectMinorVersion = projectVersionObject.getMinorVersion();
            int projectMajorVersion = projectVersionObject.getMajorVersion();

            DefaultArtifactVersion latestVersionObject = new DefaultArtifactVersion(latestVersion);
            int currentMinorVersion = latestVersionObject.getMinorVersion();
            int currentMajorVersion = latestVersionObject.getMajorVersion();
            if (Objects.equals(projectMajorVersion, currentMajorVersion)
                    && Objects.equals(projectMinorVersion, currentMinorVersion)) {
                return MINOR_VERSION_BEHIND;
            } else {
                return BEHIND;
            }
        }
    }
}
