package uk.co.harrymartland.pomchecker.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.co.harrymartland.pomchecker.domain.JSONCacheStats;

@RestController
@RequestMapping(value = "cache", method = RequestMethod.GET)
public class CacheController {

    @Autowired
    private Collection<GuavaCache> guavaCaches;

    @RequestMapping("guava/stats")
    public List<JSONCacheStats> cacheStats() {
        return guavaCaches.stream()
                .map(JSONCacheStats::new)
                .collect(Collectors.toList());
    }

}
