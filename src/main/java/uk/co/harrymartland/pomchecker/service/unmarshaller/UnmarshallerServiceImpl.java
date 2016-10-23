package uk.co.harrymartland.pomchecker.service.unmarshaller;

import java.io.InputStream;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnmarshallerServiceImpl implements UnmarshallerService {

    private static final Logger LOG = LoggerFactory.getLogger(UnmarshallerServiceImpl.class);

    @Autowired
    private GenericObjectPool<Unmarshaller> unmarshallerPool;

    @Override
    public <T> T unmarshal(InputStream inputStream) {
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = unmarshallerPool.borrowObject();
            return (T) unmarshaller.unmarshal(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                unmarshallerPool.returnObject(unmarshaller);
            } catch (Exception e) {
                LOG.warn("error returning unmarshaller to pool", e);
            }
        }
    }
}
