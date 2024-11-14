package org.asdt;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Translator {
    private final String TRANSLATION_PROPERTIES_PATH = "src/main/resources/translations.properties";
    private final Map<String, List<String>> translations = new HashMap<>();

    public int getTranslatorSize() {
        return translations.size();
    }

    public String translate(String word) {
        String preprocessedWord = preprocessWord(word);
        return translations.containsKey(preprocessedWord)
                ? String.join("/", translations.get(preprocessedWord))
                : preprocessedWord;
    }

    public void addTranslation(String word, String translation) {
        String preprocessedWord = preprocessWord(word);
        String preprocessedTranslation = preprocessWord(translation);
        translations.computeIfAbsent(preprocessedWord, k -> new ArrayList<>())
                .add(preprocessedTranslation);
    }

    public String translateSentence(String sentence) {
        List<String> words = breakDownSentence(sentence);
        List<String> translatedWords = new ArrayList<>();

        for (String word : words) {
            translatedWords.add(translateWordInSentence(word, words));
        }

        return mergeSentence(translatedWords).trim();
    }

    private String translateWordInSentence(String word, List<String> words) {
        if (checkTranslatable(word, words)) {
            return formatTranslatedWord(word, translate(word), words);
        }
        return formatUntranslatedWord(word, words);
    }

    private String formatTranslatedWord(String word, String translation, List<String> words) {
        if (shouldCapitalize(word, words)) {
            return capitalizeInitialLetter(translation);
        }
        return translation;
    }

    private String formatUntranslatedWord(String word, List<String> words) {
        return shouldCapitalize(word, words) ? capitalizeInitialLetter(word) : word;
    }

    private boolean shouldCapitalize(String word, List<String> words) {
        int index = words.indexOf(word);
        return index == 0 || (checkPunctuation(words.get(index - 1)) && Character.isUpperCase(word.charAt(0)));
    }

    public void loadTranslationsFromProperties() throws Exception {
        validatePropertiesLoadable();
        loadPropertiesFile();
    }

    private void loadPropertiesFile() {
        try (FileInputStream fis = new FileInputStream(TRANSLATION_PROPERTIES_PATH)) {
            Properties properties = new Properties();
            properties.load(fis);
            parsePropertiesToMap(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validatePropertiesLoadable() throws Exception {
        if (!translations.isEmpty()) {
            throw new Exception("Cannot load to a non-empty translator.");
        }
    }

    private String capitalizeInitialLetter(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    private boolean checkTranslatable(String word, List<String> words) {
        int wordIndex = words.indexOf(word);
        return wordIndex == 0 || checkPunctuation(words.get(wordIndex - 1)) || !Character.isUpperCase(word.charAt(0));
    }

    private String mergeSentence(List<String> words) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : words) {
            appendWordWithSpacing(stringBuilder, word);
        }
        return stringBuilder.toString();
    }

    private void appendWordWithSpacing(StringBuilder stringBuilder, String word) {
        if (checkPunctuation(word)) {
            stringBuilder.append(word);
        } else {
            stringBuilder.append(" ").append(word);
        }
    }

    private List<String> breakDownSentence(String sentence) {
        return separatePunctuation(sentence.split(" "));
    }

    private List<String> separatePunctuation(String[] words) {
        List<String> processedWords = new ArrayList<>();
        for (String word : words) {
            splitWordWithPunctuation(processedWords, word);
        }
        return processedWords;
    }

    private void splitWordWithPunctuation(List<String> processedWords, String word) {
        if (checkPunctuation(word)) {
            processedWords.add(word.substring(0, word.length() - 1));
            processedWords.add(String.valueOf(word.charAt(word.length() - 1)));
        } else {
            processedWords.add(word);
        }
    }

    private boolean checkPunctuation(String word) {
        String lastChar = String.valueOf(word.charAt(word.length() - 1));
        return Pattern.matches("\\p{Punct}", lastChar);
    }

    private void parsePropertiesToMap(Properties properties) {
        for (String key : properties.stringPropertyNames()) {
            translations.put(preprocessWord(key), parseTranslationValues(properties.getProperty(key)));
        }
    }

    private List<String> parseTranslationValues(String value) {
        return Arrays.asList(preprocessWord(value).split(","));
    }

    private String preprocessWord(String word) {
        return word.toLowerCase();
    }
}
