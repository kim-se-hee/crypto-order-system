package ksh.example.mybit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class LockManager {

    private final ConcurrentHashMap<Long, Long> lockMap = new ConcurrentHashMap<>();

    public void tryLock(Long lockIdentifier) {
        System.out.println(lockMap);
        if(lockMap.putIfAbsent(lockIdentifier, System.currentTimeMillis()) != null)
            throw new IllegalStateException("이미 처리 중인 요청이 있습니다");
    }

    public void unLock(Long lockIdentifier) {
        lockMap.remove(lockIdentifier);
    }
    
}
