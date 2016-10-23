package uk.co.harrymartland.mavenchecker.service.dependencyresult;

import java.util.List;
import uk.co.harrymartland.mavenchecker.domain.maven.MetaData;
import uk.co.harrymartland.mavenchecker.domain.pom.Dependency;
import uk.co.harrymartland.mavenchecker.domain.result.DependencyResult;

public interface DependencyResultService {

    DependencyResult createDependencyResult(MetaData metaData, Dependency dependency, List<String> ignoreDependencies, List<String> ignoreVersions);
}
