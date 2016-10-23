package uk.co.harrymartland.pomchecker.service.mavendownloader;

import java.util.concurrent.CompletableFuture;
import uk.co.harrymartland.pomchecker.domain.pom.Project;

public interface MavenDownloader {

    CompletableFuture<Project> download(String url);

}
