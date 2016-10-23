package uk.co.harrymartland.pomchecker.domain.result;

public class DependencyResult {

    private String artifact;
    private String group;
    private String projectVersion;
    private String latestVersion;
    private DependencyDifference dependencyDifference;

    public DependencyResult(String artifact, String group, String projectVersion, String latestVersion, DependencyDifference dependencyDifference) {
        this.artifact = artifact;
        this.group = group;
        this.projectVersion = projectVersion;
        this.latestVersion = latestVersion;
        this.dependencyDifference = dependencyDifference;
    }

    public String getArtifact() {
        return artifact;
    }

    public String getGroup() {
        return group;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public DependencyDifference getDependencyDifference() {
        return dependencyDifference;
    }
}
