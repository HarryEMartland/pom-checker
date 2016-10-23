package uk.co.harrymartland.pomchecker.service.dependencyresult;

import java.util.List;
import uk.co.harrymartland.pomchecker.domain.maven.MetaData;
import uk.co.harrymartland.pomchecker.domain.pom.Dependency;
import uk.co.harrymartland.pomchecker.domain.result.DependencyResult;

public interface DependencyResultService {

    DependencyResult createDependencyResult(MetaData metaData, Dependency dependency, List<String> ignoreDependencies, List<String> ignoreVersions);
}
