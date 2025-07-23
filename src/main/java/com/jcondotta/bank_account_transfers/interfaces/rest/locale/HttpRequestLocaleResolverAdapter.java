package com.jcondotta.bank_account_transfers.interfaces.rest.locale;

import com.jcondotta.bank_account_transfers.application.ports.output.LocaleResolverPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component("httpRequestLocaleResolver")
public class HttpRequestLocaleResolverAdapter implements LocaleResolverPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestLocaleResolverAdapter.class);

    private final LocaleResolver localeResolver;
    private final Locale defaultLocale;

    public HttpRequestLocaleResolverAdapter(LocaleResolver localeResolver, @Value("${spring.application.locale.default:en}") String defaultLocaleCode) {
        this.localeResolver = localeResolver;
        this.defaultLocale = Locale.forLanguageTag(defaultLocaleCode);
    }

    @Override
    public Locale resolveLocale() {
        var requestAttributes = RequestContextHolder.getRequestAttributes();

        if (!(requestAttributes instanceof ServletRequestAttributes servletRequestAttributes)) {
            LOGGER.warn("No request context found. Defaulting to: {}", defaultLocale);
            return defaultLocale;
        }

        var request = servletRequestAttributes.getRequest();
        var acceptLanguage = request.getHeader("Accept-Language");
        var resolvedLocale = localeResolver.resolveLocale(request);

        LOGGER.debug("Resolved locale: {} (Accept-Language: {})", resolvedLocale, acceptLanguage);
        return resolvedLocale;
    }
}
