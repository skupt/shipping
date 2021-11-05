package rozaryonov.shipping.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class LocaleConfig implements WebMvcConfigurer {
	//public class LocaleConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
  
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }
    
    @Bean
    public LocaleResolver localeResolver() {
       SessionLocaleResolver slr = new SessionLocaleResolver();
       //Назначает локаль по умончанию, которая используется когда к сесии не прикреплена никакая локаль.
       //Если не назначать локаль по умолчанию, то локаль будет назначена согласно Accept-Language хэдера запроса
       slr.setDefaultLocale(Locale.US);
       return slr;
    }
    
	@Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(10); //refresh cache once per 10 sec
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setBasenames(
        		"classpath:locale/messages/index",
        		"classpath:locale/messages/root", 
        		"classpath:locale/messages/manager", 
        		"classpath:locale/messages/user", 
        		"classpath:locale/messages/deliveryCost");
        return messageSource;
    }	
	
	@Bean
	public LocalValidatorFactoryBean validator(MessageSource messageSource) {
	    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	    bean.setValidationMessageSource(messageSource);
	    return bean;
	}
    
}
