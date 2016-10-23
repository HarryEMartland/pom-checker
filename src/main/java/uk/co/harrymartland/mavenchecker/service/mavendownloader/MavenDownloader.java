package uk.co.harrymartland.mavenchecker.service.mavendownloader;

import java.util.concurrent.CompletableFuture;
import uk.co.harrymartland.mavenchecker.domain.pom.Project;

public interface MavenDownloader {

    CompletableFuture<Project> download(String url);

}
