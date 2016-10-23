package uk.co.harrymartland.mavenchecker.service.mavenversiondownloader;

import java.util.concurrent.CompletableFuture;
import uk.co.harrymartland.mavenchecker.domain.maven.MetaData;

public interface MavenVersionDownloaderService {

    CompletableFuture<MetaData> getMetaData(String groupId, String artifact);
}
