package uk.co.harrymartland.mavenchecker.config;

import com.google.common.cache.CacheBuilder;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jmx.export.MBeanExporter;

@Configuration
@EnableCaching
public class MavenCheckerCacheConfig {

    private static final String CACHE_TTL_SUFFIX = ".ttl.";
    private final String CACHE_PREFIX = "maven-checker.cache.";
    private final String CACHE_TTL_UNIT_SUFFIX = CACHE_TTL_SUFFIX + "unit";
    private final String CACHE_TTL_SIZE_SUFFIX = CACHE_TTL_SUFFIX + "size";

    @Bean
    public GuavaCache mavenMetaDataCache(@Autowired MBeanExporter mBeanExporter, @Autowired Environment environment) throws MalformedObjectNameException {
        return createCache(mBeanExporter, "mavenMetaData", environment);
    }

    @Bean
    public GuavaCache badgeCache(@Autowired MBeanExporter mBeanExporter, @Autowired Environment environment) throws MalformedObjectNameException {
        return createCache(mBeanExporter, "badge", environment);
    }

    @Bean
    public GuavaCache mavenDownloadCache(@Autowired MBeanExporter mBeanExporter, @Autowired Environment environment) throws MalformedObjectNameException {
        return createCache(mBeanExporter, "mavenDownload", environment);
    }

    private GuavaCache createCache(MBeanExporter mBeanExporter, String cacheName, Environment environment) throws MalformedObjectNameException {
        GuavaCache mavenDownload = new GuavaCache(cacheName, CacheBuilder.newBuilder()
                .expireAfterWrite(Long.parseLong(environment.getProperty(CACHE_PREFIX + cacheName + CACHE_TTL_SIZE_SUFFIX)),
                        TimeUnit.valueOf(environment.getProperty(CACHE_PREFIX + cacheName + CACHE_TTL_UNIT_SUFFIX)))
                .recordStats()
                .build());
        return addToJMX(mavenDownload, mBeanExporter);
    }

    @Bean
    public CacheManager cacheManager(@Autowired Collection<GuavaCache> guavaCaches) throws MalformedObjectNameException {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(guavaCaches);
        return simpleCacheManager;
    }

    private GuavaCache addToJMX(GuavaCache guavaCache, MBeanExporter mBeanExporter) throws MalformedObjectNameException {
        mBeanExporter.registerManagedResource(guavaCache, ObjectName.getInstance("org.guava:type=GuavaCache,name=" + guavaCache.getName()));
        mBeanExporter.registerManagedResource(guavaCache.getNativeCache(), ObjectName.getInstance("org.guava:type=Cache,name=" + guavaCache.getName()));
        return guavaCache;
    }
}