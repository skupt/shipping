package rozaryonov.shipping.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class I18NConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(10); //refresh cache once per 10 sec
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setBasenames("classpath:locale/messages/index",
        		"classpath:locale/messages/root", 
        		"classpath:locale/messages/manager", 
        		"classpath:locale/messages/user", 
        		"classpath:locale/messages/deliveryCost");
        return messageSource;
    }	
}
