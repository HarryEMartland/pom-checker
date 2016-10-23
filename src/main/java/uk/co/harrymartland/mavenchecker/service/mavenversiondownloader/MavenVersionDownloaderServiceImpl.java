package uk.co.harrymartland.mavenchecker.service.mavenversiondownloader;

import java.util.concurrent.CompletableFuture;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.co.harrymartland.mavenchecker.domain.maven.MetaData;
import uk.co.harrymartland.mavenchecker.service.completablefuturehttpclient.CompletableFutureHttpClient;
import uk.co.harrymartland.mavenchecker.service.unmarshaller.UnmarshallerService;

@Service
public class MavenVersionDownloaderServiceImpl implements MavenVersionDownloaderService {

    private static final Logger LOG = LoggerFactory.getLogger(MavenVersionDownloaderServiceImpl.class);

    @Value("${maven-checker.maven.repository}")
    private String mavenRepository;

    @Autowired
    private CompletableFutureHttpClient completableFutureHttpClient;

    @Autowired
    private UnmarshallerService unmarshallerService;

    @Override
    @Cacheable("mavenMetaData")
    public CompletableFuture<MetaData> getMetaData(String groupId, String artifact) {
        return completableFutureHttpClient.request(new HttpGet(getMavenUri(groupId, artifact)))
                .thenApplyAsync(httpResponse -> {
                    try {
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            return unmarshallerService.unmarshal(httpResponse.getEntity().getContent());
                        } else {
                            return new MetaData();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private String getMavenUri(String groupId, String artifact) {
        return mavenRepository + "/" + groupId.replace(".", "/") + "/" + artifact + "/maven-metadata.xml";
    }
}
