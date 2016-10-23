package uk.co.harrymartland.mavenchecker.domain.maven;

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "versioning")
public class Versioning {

    @XmlElement(name = "latest")
    private String latest;
    @XmlElement(name = "release")
    private String release;
    @XmlElementWrapper(name = "versions")
    @XmlElement(name = "version")
    private List<String> versions;

    public String getLatest() {
        return latest;
    }

    public String getRelease() {
        return release;
    }

    public List<String> getVersions() {
        return Collections.unmodifiableList(versions);
    }
}
