package com.peer39.service.impl;

import com.peer39.dto.ParsedWebDataCategoriesDto;
import com.peer39.dto.UrlDto;
import com.peer39.dto.UrlResultDto;
import com.peer39.entities.Category;
import com.peer39.exceptions.UrlDownloadException;
import com.peer39.service.CategoryStorage;
import com.peer39.service.ConverterService;
import com.peer39.service.UrlService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class UrlServiceImpl implements UrlService {

    private ConverterService converterService;

    private CategoryStorage categoryStorage;

    @Override
    public List<UrlResultDto> getTextFromUrls(List<UrlDto> urls) {
        return urls.stream().map(this::getTextFromUrl).toList();
    }

    public UrlResultDto getTextFromUrl(UrlDto urlDto) {
        String inputUrl = urlDto.getInputUrl();
        String htmlToText;
        try {
            htmlToText = converterService.convertHtml(inputUrl);

        } catch (Exception e) {
            log.error("Error occurred during downloading url {}", inputUrl);
            throw new UrlDownloadException(inputUrl);
        }
        log.info("Getting text from HTML body for url {}", inputUrl);
        return new UrlResultDto(inputUrl, htmlToText);
    }

    @Override
    public List<ParsedWebDataCategoriesDto> getListParsedWebDataCategories(List<UrlDto> urls) {
        return urls.stream().map(this::getParsedWebDataCategories).toList();
    }

    @Override
    public ParsedWebDataCategoriesDto getParsedWebDataCategories(UrlDto urlDto) {
        String inputUrl = urlDto.getInputUrl();
        String textFromHtml = getTextFromUrl(urlDto)
                .getTextResult()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-zA-Z0-9]", " ");

        Set<String> categories = categoryStorage.getCategories().stream()
                .filter(category -> matchCategory(category, textFromHtml))
                .map(Category::getCategoryName)
                .collect(Collectors.toSet());

        return new ParsedWebDataCategoriesDto(inputUrl, categories);
    }

    private boolean matchCategory(Category category, String text) {
        AtomicBoolean res = new AtomicBoolean(false);
        category.getKeywords().forEach(el -> {
            if (text.contains(el.getKeyword().toLowerCase(Locale.ROOT))) {
                res.set(true);
            }
        });
        return res.get();
    }
}
