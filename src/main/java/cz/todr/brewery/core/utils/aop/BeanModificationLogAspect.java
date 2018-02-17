package cz.todr.brewery.core.utils.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Arrays;

@Controller
@Aspect
public class BeanModificationLogAspect {
	
	private static final Logger ASPECT_LOGGER = LoggerFactory.getLogger(BeanModificationLogAspect.class);

	/* within package && with an annotation %% setter */
	@Pointcut("within(cz.todr.*) && @target(cz.todr.brewery.core.utils.aop.BeanModificationLogging) && execution(* set*(..))")
    public void setter() {

    }

    @Before("setter()")
    public void beanModificationLogAdvice(JoinPoint joinPoint) {
    	try {
	    	Class<?> clazz = joinPoint.getSignature().getDeclaringType();
	    	Logger log = LoggerFactory.getLogger(clazz);

	    	String method = joinPoint.getSignature().getName();
	    	String variable = method.substring(3);

	    	Object[] args = joinPoint.getArgs();
	    	if (args.length == 1) {
	    		String argsStr = String.valueOf(args[0]);
	    		String oldValue = String.valueOf(getOldValue(joinPoint.getTarget(), variable, args[0] instanceof Boolean));
	    		log.info("Set {} = {} (old value: {})", variable, argsStr, oldValue);
	    	} else {
	    		log.info("Set {} = {}", variable, Arrays.toString(args));
	    	}
    	} catch (Exception e) {
    		ASPECT_LOGGER.error("Exception during modification logging", e);
    	}
    }


    @Nullable
    private Object getOldValue(@Nonnull Object target, @Nonnull String variable, boolean isBoolean) {
    	try {
    		String prefix = isBoolean ? "is" : "get";
    		Method getter = target.getClass().getMethod(prefix  + variable);
    		return getter.invoke(target);
    	} catch (Exception e) {
    		ASPECT_LOGGER.debug("Could not retrieve variable '{}' value from {} class", variable, target.getClass().getCanonicalName());
    		return "???";
    	}
    }

}
