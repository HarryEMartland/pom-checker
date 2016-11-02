package uk.co.harrymartland.pomchecker.domain.pom;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.w3c.dom.Element;

@XmlRootElement(name = "project", namespace = "http://maven.apache.org/POM/4.0.0")
public class Project {

    @XmlElement(name = "Dependency")
    private Dependency parent;

    @XmlElementWrapper(name = "dependencies", namespace = "http://maven.apache.org/POM/4.0.0")
    @XmlElement(name = "dependency", namespace = "http://maven.apache.org/POM/4.0.0")
    private List<Dependency> dependencies = new ArrayList<>();
    @XmlElement(name = "groupId")
    private String groupId;

    @XmlElementWrapper(name = "properties")
    @XmlAnyElement
    private List<org.w3c.dom.Element> properties;

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public Dependency getParent() {
        return parent;
    }

    public String getGroupId() {
        return groupId;
    }

    public List<Element> getProperties() {
        return properties;
    }
}
