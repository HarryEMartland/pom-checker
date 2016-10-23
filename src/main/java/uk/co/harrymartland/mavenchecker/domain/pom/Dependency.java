package uk.co.harrymartland.mavenchecker.domain.pom;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "dependency")
public class Dependency {

    @XmlElement(namespace = "http://maven.apache.org/POM/4.0.0")
    private String groupId;
    @XmlElement(namespace = "http://maven.apache.org/POM/4.0.0")
    private String artifactId;
    @XmlElement(namespace = "http://maven.apache.org/POM/4.0.0")
    private String version;

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }
}
