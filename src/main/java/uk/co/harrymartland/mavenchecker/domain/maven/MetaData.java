package uk.co.harrymartland.mavenchecker.domain.maven;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "metadata")
public class MetaData {

    @XmlElement
    private String groupId;
    @XmlElement
    private String artifactId;


    @XmlElement(name = "versioning")
    private Versioning versioning;

    public Versioning getVersioning() {
        return versioning;
    }
}
