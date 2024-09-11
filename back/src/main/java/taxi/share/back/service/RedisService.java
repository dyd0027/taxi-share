package taxi.share.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis에 데이터 저장
    public void cacheData(String key, Object data) {
        redisTemplate.opsForValue().set(key, data);
    }

    // Redis에서 데이터 조회
    public Object getCachedData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Redis에서 데이터 삭제
    public void evictCachedData(String key) {
        redisTemplate.delete(key);
    }

    // 복잡한 비즈니스 로직이 포함된 Redis 작업
    public Object performComplexCacheOperation(String key, Object defaultValue) {
        Object cachedData = redisTemplate.opsForValue().get(key);
        if (cachedData == null) {
            // Redis에 없는 경우, 기본값 저장
            redisTemplate.opsForValue().set(key, defaultValue);
            return defaultValue;
        }
        return cachedData;
    }
}
