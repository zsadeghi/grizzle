package me.theyinspire.projects.grizzle.importer.web;

import me.theyinspire.projects.grizzle.importer.input.InputConverter;
import me.theyinspire.projects.grizzle.importer.web.controllers.ConverterController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {

    @Bean
    public ConverterController converterController(final InputConverter converter) {
        return new ConverterController(converter);
    }

}
