package uk.co.harrymartland.mavenchecker.service.unmarshaller;

import java.io.InputStream;

public interface UnmarshallerService {
    <T> T unmarshal(InputStream inputStream);
}
