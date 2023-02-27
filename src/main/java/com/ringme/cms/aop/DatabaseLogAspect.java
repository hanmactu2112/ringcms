package com.ringme.cms.aop;

import com.ringme.cms.dto.UserSecurity;
import com.ringme.cms.model.EntityBase;
import com.ringme.cms.model.Log;
import com.ringme.cms.logrepository.LogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class DatabaseLogAspect {

    private final LogRepository logRepository;

    @Autowired
    public DatabaseLogAspect(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Around("execution(* com.ringme.cms.repository.*.*(..))&& !execution(* com.ringme.cms.repository.UserRepository.findUserByUserName(..))" +
            "&& !execution(* com.ringme.cms.repository.UserRoleRepository.findUserRoleByUserId(..))" +
            "&& !execution(* com.ringme.cms.repository.RouterRoleRepository.findAllRouterRoleByListRoleId(..))")
    public Object logDatabase(ProceedingJoinPoint joinPoint) throws Throwable {
        // get object ID if available
        Object[] args = joinPoint.getArgs();
        Long objectId = null;
        String objectName = "";
        for (Object arg : args) {
            if (arg instanceof Long) {
                objectId = (Long) arg;
                break;
            } else if (arg instanceof EntityBase) {
                objectName = arg.getClass().getSimpleName();
            }
        }

        // get account information from security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String accountId = ((UserSecurity)authentication.getPrincipal()).getId().toString();
            String accountName = ((UserDetails) authentication.getPrincipal()).getUsername();

            // get method name and action (CRUD)
            String methodName = joinPoint.getSignature().getName();
            String action="";
            if (methodName.startsWith("save") || methodName.toLowerCase().startsWith("update")) {
                action = "CREATE OR UPDATE";
            } else if (methodName.startsWith("delete")) {
                action = "DELETE";
            }
            // get object name from repository interface name
            String repositoryName1 = joinPoint.getSignature().getName();
            String repositoryName = joinPoint.getSignature().getDeclaringTypeName();
            System.err.println("repositoryName1: "+repositoryName1);
            System.err.println("repositoryName: "+repositoryName);
            if (objectName.equals("")) {
                objectName = repositoryName.substring(repositoryName.lastIndexOf(".") + 1, repositoryName.indexOf("Repository"));

            }

            // invoke method and measure execution time
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            LocalDateTime localDateTime = LocalDateTime.now();

            // create log object and save to database
            if (!action.equals("")){
                Log log = new Log();
                log.setAccountId(accountId);
                log.setAction(action);
                log.setObjectId(objectId);
                log.setMethodName(methodName);
                log.setObjectName(objectName);
                log.setAccountName(accountName);
                log.setTime(localDateTime);
                logRepository.save(log);
            }
            return result;
    }
}

