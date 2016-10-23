package uk.co.harrymartland.pomchecker.domain;

import org.springframework.cache.guava.GuavaCache;

public class JSONCacheStats {

    private final GuavaCache guavaCache;

    public JSONCacheStats(GuavaCache guavaCache) {
        this.guavaCache = guavaCache;
    }

    public String getName() {
        return guavaCache.getName();
    }

    public long getSize() {
        return guavaCache.getNativeCache().size();
    }

    public long getHitCount() {
        return guavaCache.getNativeCache().stats().hitCount();
    }

    public long getMissCount() {
        return guavaCache.getNativeCache().stats().missCount();
    }

    public long getLoadSuccessCount() {
        return guavaCache.getNativeCache().stats().loadSuccessCount();
    }

    public long getLoadExceptionCount() {
        return guavaCache.getNativeCache().stats().loadExceptionCount();
    }

    public long getTotalLoadTime() {
        return guavaCache.getNativeCache().stats().totalLoadTime();
    }

    public long getEvictionCount() {
        return guavaCache.getNativeCache().stats().evictionCount();
    }
}
