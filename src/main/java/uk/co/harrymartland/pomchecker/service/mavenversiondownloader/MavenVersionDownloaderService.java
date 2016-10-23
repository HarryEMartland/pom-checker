package uk.co.harrymartland.pomchecker.service.mavenversiondownloader;

import java.util.concurrent.CompletableFuture;
import uk.co.harrymartland.pomchecker.domain.maven.MetaData;

public interface MavenVersionDownloaderService {

    CompletableFuture<MetaData> getMetaData(String groupId, String artifact);
}
