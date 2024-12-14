package com.example.demo.core.exception;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class BatchSkipPolicy implements SkipPolicy {

    @Override
    public boolean shouldSkip(@SuppressWarnings("null") Throwable t, long skipCount) throws SkipLimitExceededException {
        return t instanceof AppException;
    }
    
}
