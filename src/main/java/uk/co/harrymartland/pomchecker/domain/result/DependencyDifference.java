package uk.co.harrymartland.pomchecker.domain.result;

public enum DependencyDifference {
    ERROR("error", "red"),
    BEHIND("behind", "red"),
    MINOR_VERSION_BEHIND("minor%20version%20behind", "orange"),
    UP_TO_DATE("up%20to%20date", "green"),
    IGNORED("ignored", "green");

    private String badgeText;
    private String badgeColor;

    DependencyDifference(String badgeText, String badgeColour) {
        this.badgeText = badgeText;
        this.badgeColor = badgeColour;
    }

    public String getBadgeText() {
        return badgeText;
    }

    public String getBadgeColor() {
        return badgeColor;
    }

    public DependencyDifference getWorse(DependencyDifference dependencyDifference) {
        if (this.ordinal() > dependencyDifference.ordinal()) {
            return dependencyDifference;
        } else {
            return this;
        }
    }
}
