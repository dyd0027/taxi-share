package taxi.share.back.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final CacheManager cacheManager;

    public void evict(String cacheName, Object key) {
        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }

    public <T>T getFromCache(String cacheName, String cacheKey, Class<T> type){
        var cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            // 원하는 처리를 넣을 수 있음 (로그, 예외 등)
            return null;
        }
        return cache.get(cacheKey, type);
    }
}
