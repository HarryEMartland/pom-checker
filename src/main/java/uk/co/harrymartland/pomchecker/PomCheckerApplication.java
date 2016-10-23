package uk.co.harrymartland.pomchecker;

import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class PomCheckerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws IOException, JAXBException {
        SpringApplication.run(PomCheckerApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PomCheckerApplication.class);
    }

}