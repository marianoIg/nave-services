package com.mariano.spacecrafts.api.v1.aspects;

import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftInvalidDataException;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftsError;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ValidationIdNumberAspect {

    @Pointcut("execution(* com.mariano.spacecrafts.api.v1.SpacecraftController.getById(..)) && args(id,..)")
    public void validateIdPointcut(Long id) {}

    @Before("validateIdPointcut(id)")
    public void logIfNegativeId(Long id) {
        if (id < 0) {
            log.error(SpacecraftsError.NAV_ERR_NEG_005.getDescription(), id);
            throw new SpacecraftInvalidDataException(SpacecraftsError.NAV_ERR_NEG_005, id.toString());
        }
    }
}
