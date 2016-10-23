package uk.co.harrymartland.pomchecker.service.mavendownloader;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.co.harrymartland.pomchecker.domain.pom.Project;
import uk.co.harrymartland.pomchecker.service.completablefuturehttpclient.CompletableFutureHttpClient;
import uk.co.harrymartland.pomchecker.service.unmarshaller.UnmarshallerService;

@Service
public class MavenDownloaderImpl implements MavenDownloader {

    @Autowired
    private CompletableFutureHttpClient httpClient;

    @Autowired
    private UnmarshallerService unmarshallerService;

    @Override
    @Cacheable("mavenDownload")
    public synchronized CompletableFuture<Project> download(String url) {
        HttpGet httpGet = new HttpGet(url);
        return httpClient.request(httpGet).thenApplyAsync(this::mapToProject);
    }

    private Project mapToProject(HttpResponse httpResponse) {
        try {
            return (Project) unmarshallerService.unmarshal(httpResponse.getEntity().getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
