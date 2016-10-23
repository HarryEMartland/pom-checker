package uk.co.harrymartland.mavenchecker;

import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MavenCheckerApplication {

    public static void main(String[] args) throws IOException, JAXBException {
        SpringApplication.run(MavenCheckerApplication.class, args);
    }

}