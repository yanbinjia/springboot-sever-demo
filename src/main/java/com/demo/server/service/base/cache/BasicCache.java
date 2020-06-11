package com.demo.server.service.base.cache;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.stats.CacheStats;

// @Component、@Repository、@Service默认单例,@Controller默认多例
// 如想声明成多例可以使用@Scope("prototype")
@Component("basicCache") // ("basicCache") 定义beanName
public class BasicCache {
	private long maximumSize = 10000 * 10;
	private long durationSec = 60 * 60; // expireAfterAccess从上次读或写发生后,经过durationSec条目过期

	private Cache<String, String> basicCache = null;

	public BasicCache() {
		basicCache = Caffeine.newBuilder()//
				.expireAfterAccess(durationSec, TimeUnit.MINUTES)//
				.maximumSize(maximumSize)//
				.recordStats()//
				.removalListener(new BasicRemovalListener())//
				.build();//
	}

	@PostConstruct // 初始化执行顺序:构造方法->@Autowired->@PostConstruct
	public void init() {
	}

	public class BasicRemovalListener implements RemovalListener<String, String> {
		@Override
		public void onRemoval(@Nullable String key, @Nullable String value, @NonNull RemovalCause cause) {
			System.out.printf("Key %s was removed (%s)%n", key, cause);
		}
	}

	public String get(String key) {
		return this.basicCache.getIfPresent(key);
	}

	public void put(String key, String value) {
		this.basicCache.put(key, value);
	}

	public void remove(String key) {
		this.basicCache.invalidate(key);
	}

	public void removeAll() {
		this.basicCache.invalidateAll();
	}

	/**
	 * 统计信息 CacheStats{hitCount=6, missCount=4, loadSuccessCount=0,
	 * loadFailureCount=0, totalLoadTime=0, evictionCount=0, evictionWeight=0}
	 * 
	 */
	public String stats() {
		CacheStats cacheStats = this.basicCache.stats();
		return cacheStats.toString();
	}
}
