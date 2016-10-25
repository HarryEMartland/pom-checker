package uk.co.harrymartland.pomchecker.domain.pom;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project", namespace = "http://maven.apache.org/POM/4.0.0")
public class Project {

    @XmlElement(name = "Dependency")
    private Dependency parent;

    @XmlElementWrapper(name = "dependencies", namespace = "http://maven.apache.org/POM/4.0.0")
    @XmlElement(name = "dependency", namespace = "http://maven.apache.org/POM/4.0.0")
    private List<Dependency> dependencies;

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public Dependency getParent() {
        return parent;
    }
}
