package ru.job4j.articles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;
import ru.job4j.articles.service.generator.ArticleGenerator;
import ru.job4j.articles.store.Store;

import java.util.ArrayList;
import java.util.List;

public class SimpleArticleService implements ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArticleService.class.getSimpleName());

    private final ArticleGenerator articleGenerator;

    private static int counterArticles = 0;

    public SimpleArticleService(ArticleGenerator articleGenerator) {
        this.articleGenerator = articleGenerator;
    }

    @Override
    public void generate(Store<Word> wordStore, int count, Store<Article> articleStore) {
        if (count < 1) {
            throw new RuntimeException("0 статей для генерации");
        }
        int sizePortionSave = 10_000;
        int numberOfOperationSave = count / sizePortionSave;
        int restOfDateForSave = 0;
        if (count % sizePortionSave != 0) {
            restOfDateForSave = count % sizePortionSave;
        }
        LOGGER.info("Генерация статей в количестве {}", count);
        var words = wordStore.findAll();
        /*var articles = IntStream.iterate(0, i -> i < count, i -> i + 1)
                .peek(i -> LOGGER.info("Сгенерирована статья № {}", i))
                .mapToObj((x) -> articleGenerator.generate(words))
                .collect(Collectors.toList());*/
        for (int i = 0; i < numberOfOperationSave; i++) {
            List<Article> articles = generatePortionArticles(sizePortionSave, words);
            articles.forEach(articleStore::save);
        }
        if (restOfDateForSave != 0) {
            List<Article> articles = generatePortionArticles(restOfDateForSave, words);
            articles.forEach(articleStore::save);
        }
    }

    public List<Article> generatePortionArticles(int sizePortionSave, List<Word> words) {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < sizePortionSave; i++) {
            LOGGER.info("Сгенерирована статья № {}", counterArticles);
            articles.add(articleGenerator.generate(words));
            counterArticles++;
        }
        return articles;
    }
}