package ru.job4j.articles.service.generator;

import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomArticleGenerator implements ArticleGenerator {
    @Override
    public Article generate(List<Word> words) {
        var wordsCopy = new ArrayList<>(words);
        Collections.shuffle(wordsCopy);
        /*var content = wordsCopy.stream()
                .map(Word::getValue)
                .collect(Collectors.joining(" "));*/
        List<String> temp = new ArrayList<>();
        for (Word w : wordsCopy) {
            temp.add(w.getValue());
        }
        String content = String.join(" ", temp);
        return new Article(content);
    }
}