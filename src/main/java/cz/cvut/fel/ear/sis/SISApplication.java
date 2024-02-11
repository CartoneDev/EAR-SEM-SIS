package cz.cvut.fel.ear.sis;

import cz.cvut.fel.ear.sis.controller.CourseController;
import cz.cvut.fel.ear.sis.model.Program;
import cz.cvut.fel.ear.sis.model.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Random;

@SpringBootApplication(scanBasePackages = "cz.cvut.fel.ear.sis")
@EnableJpaRepositories(basePackages = "cz.cvut.fel.ear.sis.DAO")
public class SISApplication {
    private static final Logger LOG = LoggerFactory.getLogger(SISApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SISApplication.class, args);

    }
}