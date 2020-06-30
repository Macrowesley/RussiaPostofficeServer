package cc.mrbird.febs.common.configure;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanContext implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanContext.applicationContext = applicationContext;
    }
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static <T> T getBean(Class<T> var1){
        return applicationContext.getBean(var1);
    }
}
