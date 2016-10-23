package uk.co.harrymartland.mavenchecker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import uk.co.harrymartland.mavenchecker.domain.maven.MetaData;
import uk.co.harrymartland.mavenchecker.domain.pom.Project;

@Configuration
public class MavenCheckerConfig {

    @Bean
    public CloseableHttpAsyncClient httpAsyncClient() {

        CloseableHttpAsyncClient httpClient = HttpAsyncClientBuilder.create()
                .setMaxConnTotal(Integer.MAX_VALUE)
                .setMaxConnPerRoute(Integer.MAX_VALUE)
                .build();

        httpClient.start();
        return httpClient;
    }

    @Bean
    public JAXBContext jaxbContext() throws JAXBException {
        return JAXBContext.newInstance(Project.class, MetaData.class);
    }

    @Bean
    public GenericObjectPool<Unmarshaller> unmarshallerPool(@Autowired JAXBContext jaxbContext, MBeanExporter mBeanExporter) throws MalformedObjectNameException {
        BasePoolableObjectFactory<Unmarshaller> basePoolableObjectFactory = new BasePoolableObjectFactory<Unmarshaller>() {
            @Override
            public Unmarshaller makeObject() throws Exception {
                return jaxbContext.createUnmarshaller();
            }
        };

        GenericObjectPool<Unmarshaller> unmarshallerGenericObjectPool = new GenericObjectPool<>(basePoolableObjectFactory);
        mBeanExporter.registerManagedResource(unmarshallerGenericObjectPool, ObjectName.getInstance("org.apache:type=GenericObjectPool,name=unmarshallerPool"));
        return unmarshallerGenericObjectPool;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MBeanExporter mBeanExporter() throws MalformedObjectNameException {
        return new MBeanExporter();
    }

}