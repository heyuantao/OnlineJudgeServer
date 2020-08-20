package cn.heyuantao.onlinejudgeserver.annotation;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author he_yu
 */
@Aspect
@Component
public class DebugAspect {

    @Pointcut("@annotation(cn.heyuantao.onlinejudgeserver.annotation.Debug)")
    public void pointCut(){}

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{

        if(isDebug(joinPoint).equals(Boolean.TRUE)){
            System.out.println("#############################Before Debug#############################");
            Object result = joinPoint.proceed();
            System.out.println("#############################After Debug#############################");
            return result;
        }else{
            System.out.println("##########################Not in Debug mode###########################");
            return null;
        }
    }

    /**
     * 通过注解的参数来判断是否Debug模式是否打开了
     * @param pointCut
     * @return
     */
    Boolean isDebug(ProceedingJoinPoint pointCut){
        String methodName = pointCut.getSignature().getName();
        try {
            Debug debugAnno = pointCut.getTarget().getClass().getMethod(methodName).getAnnotation(Debug.class);
            String debugAnnoValue = debugAnno.value();
            //当Debug的value为true和on是为调试模式，为false、off或者其他值时为非调试模式
            if(StringUtils.equalsIgnoreCase(debugAnnoValue,"true")){
                return Boolean.TRUE;
            }else if(StringUtils.equalsIgnoreCase(debugAnnoValue,"on")){
                return Boolean.TRUE;
            }else if(StringUtils.equalsIgnoreCase(debugAnnoValue,"false")){
                return Boolean.FALSE;
            }else if(StringUtils.equalsIgnoreCase(debugAnnoValue,"off")){
                return Boolean.FALSE;
            }else{
                return Boolean.FALSE;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

/*    private void display(ProceedingJoinPoint pointCut)  {
        String className = pointCut.getTarget().getClass().getName();
        String methodName = pointCut.getSignature().getName();

        try {
            Debug debugAnno = pointCut.getTarget().getClass().getMethod(methodName).getAnnotation(Debug.class);
            System.out.println("Anno value:"+debugAnno.value());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Object[] args = pointCut.getArgs();
        System.out.println("Class name:"+className);
        System.out.println("Method name:"+methodName);
    }*/
}
