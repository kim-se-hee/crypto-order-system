package ksh.example.mybit.global.util.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockService {

    private final LockManager lockManager;

    public void tryLock(Long id) {
        lockManager.tryLock(id);
    }

    public void unLock(Long id) {
        lockManager.unLock(id);
    }
}
