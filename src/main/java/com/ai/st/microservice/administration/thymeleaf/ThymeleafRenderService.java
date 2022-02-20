package com.ai.st.microservice.administration.thymeleaf;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class ThymeleafRenderService {

    private final TemplateEngine templateEngine;

    public ThymeleafRenderService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String parse(String template, Map<String, Object> data) {
        Context context = new Context();
        data.forEach(context::setVariable);
        return templateEngine.process(template, context);
    }

}
