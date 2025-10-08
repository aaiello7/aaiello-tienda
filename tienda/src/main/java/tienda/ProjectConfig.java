package tienda;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ProjectConfig implements WebMvcConfigurer {

    /* Los siguiente métodos son para implementar el tema de seguridad dentro del proyecto */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/ejemplo2").setViewName("ejemplo2");
        registry.addViewController("/multimedia").setViewName("multimedia");
        registry.addViewController("/iframes").setViewName("iframes");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registro/nuevo").setViewName("/registro/nuevo");
    }

    /* El siguiente método se utilizar para publicar en la nube, independientemente  */
    @Bean
    public SpringResourceTemplateResolver templateResolver_0() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setOrder(0);
        resolver.setCheckExistence(true);
        return resolver;
    }

    @Bean
    public LocaleResolver localeResolver() {
        var slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.getDefault());
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezone");
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(localeChangeInterceptor());
    }
    
    //Bean para poder acceder a los messages.properties en código...
    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    @Value("${firebase.json.path}")
    private String jsonPath;

    @Value("${firebase.json.file}")
    private String jsonFile;

    @Bean
    public Storage storage() throws IOException {
        ClassPathResource resource = new ClassPathResource(jsonPath + File.separator + jsonFile);
        try (InputStream inputStream = resource.getInputStream()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }
    }

}