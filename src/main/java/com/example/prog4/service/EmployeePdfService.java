package com.example.prog4.service;

import com.example.prog4.config.CompanyConf;
import com.example.prog4.model.exception.InternalServerErrorException;
import com.example.prog4.repository.entity.Employee;
import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import org.thymeleaf.context.Context;



import static org.thymeleaf.templatemode.TemplateMode.HTML;

@Service
public class EmployeePdfService {

    public byte[] generateEmployeePdf(Employee employee, CompanyConf companyConf, String template) {
        ITextRenderer renderer = new ITextRenderer();
        applyStylesAndLayout(renderer, employee, companyConf, template);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            renderer.createPDF(outputStream);
        } catch (DocumentException e) {
            throw new InternalServerErrorException("PDF generation failed: " + e.getMessage());
        }
        return outputStream.toByteArray();
    }

    private void applyStylesAndLayout(ITextRenderer renderer, Employee employee, CompanyConf companyConf, String template) {
        renderer.setDocumentFromString(renderHtmlToString(employee, companyConf, template));
    }

    private String renderHtmlToString(Employee employee, CompanyConf companyConf, String template) {
        TemplateEngine templateEngine = createTemplateEngine();
        Context context = createContext(employee, companyConf);
        return templateEngine.process(template, context);
    }

    private Context createContext(Employee employee, CompanyConf companyConf) {
        Context context = new Context();
        context.setVariable("employee", employee);
        context.setVariable("companyConf", companyConf);
        return context;
    }

    private TemplateEngine createTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(HTML);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }
}
