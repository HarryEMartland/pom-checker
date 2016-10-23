package uk.co.harrymartland.mavenchecker.domain.result;

import java.util.Collections;
import java.util.List;

public class UpdateCheckResult {

    private final Throwable throwable;
    private final List<DependencyResult> dependencyResults;
    private final DependencyDifference dependencyDifference;

    public UpdateCheckResult(Throwable throwable) {
        this.throwable = throwable;
        this.dependencyResults = Collections.emptyList();
        dependencyDifference = DependencyDifference.ERROR;
    }

    public UpdateCheckResult(List<DependencyResult> dependencyResults) {
        this.throwable = null;
        this.dependencyResults = dependencyResults;

        DependencyDifference currentDifference = DependencyDifference.UP_TO_DATE;
        for (DependencyResult dependencyResult : dependencyResults) {
            currentDifference = dependencyResult.getDependencyDifference().getWorse(currentDifference);
        }
        dependencyDifference = currentDifference;
    }

    public List<DependencyResult> getDependencyResults() {
        return dependencyResults;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public DependencyDifference getDependencyDifference() {
        return dependencyDifference;
    }

    public Boolean getUpToDate() {
        return dependencyDifference.equals(DependencyDifference.UP_TO_DATE);
    }
}
