package uk.co.harrymartland.pomchecker.service.unmarshaller;

import java.io.InputStream;

public interface UnmarshallerService {
    <T> T unmarshal(InputStream inputStream);
}
