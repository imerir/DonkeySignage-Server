package Donkey;

import Donkey.Storage.Service.StorageService;
import Donkey.Storage.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
@EnableConfigurationProperties(StorageProperties.class)
@EnableScheduling
public class MainClass {


    public static void main(String args[]){
        SpringApplication.run(MainClass.class, args);

    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }
}
