import org.asdt.Translator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TranslatorTest {
    Translator translator;

    @BeforeEach
    void setUp() {
        translator = new Translator();

        translator.addTranslation("katze", "cat");
        translator.addTranslation("freundin", "friend");
        translator.addTranslation("freundin", "girlfriend");
        translator.addTranslation("du", "you");
        translator.addTranslation("hast", "had");
        translator.addTranslation("mich", "me");
        translator.addTranslation("hallo", "hello");
        translator.addTranslation("ja", "yes");
    }

    @Test
    void emptyTranslatorTest() {
        Translator translator1 = new Translator();

        assertEquals(0, translator1.getTranslatorSize());
    }

    @Test
    void translateUnknownWordTest() {
        String word = "çikolata";

        String actualTranslation = translator.translate(word);

        assertEquals(word, actualTranslation);
    }

    @Test
    void addTranslationTest() {
        int initialTranslatorSize = translator.getTranslatorSize();

        translator.addTranslation("hund", "dog");

        assertTrue(translator.getTranslatorSize() > initialTranslatorSize);
    }

    @Test
    void translateKnownWordTest() {
        String expectedTranslation = "cat";

        String actualTranslation = translator.translate("katze");

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void translateSynonymTest() {
        String expectedTranslation = "friend/girlfriend";

        String actualTranslation = translator.translate("freundin");

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void translateSentenceTest() {
        String sentence = "du hast mich";
        String expectedTranslation = "You had me";

        String actualTranslation = translator.translateSentence(sentence);

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void loadTranslationsFromPropertiesTest() throws Exception {
        Translator translator1 = new Translator();

        translator1.loadTranslationsFromProperties();

        assertTrue(translator1.getTranslatorSize() > 0);
    }

    @Test
    void caseInsensitiveTranslationTest() {
        String sentence = "Du hast mich";
        String expectedTranslation = "You had me";

        String actualTranslation = translator.translateSentence(sentence);

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void punctuationPreservingSentenceTranslationTest() {
        String sentence = "Hallo, hast du mich?";
        String expectedTranslation = "Hello, had you me?";

        String actualTranslation = translator.translateSentence(sentence);

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void multipleSentenceTranslationTest() {
        String sentence = "Hallo, hast du mich? Ja, du hast mich!";
        String expectedTranslation = "Hello, had you me? Yes, you had me!";

        String actualTranslation = translator.translateSentence(sentence);

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void translateSentence_withNamesTest() {
        String sentence = "Du hast Moretti.";
        String expectedTranslation = "You had Moretti.";

        String actualTranslation = translator.translateSentence(sentence);

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void translateSentence_whenSentenceStartsWithCapitalTest() {
        String sentence = "Hast du Heineken?";
        String expectedTranslation = "Had you Heineken?";

        String actualTranslation = translator.translateSentence(sentence);

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void translateSentence_whenInnerSentenceStartsWithCapitalTest() {
        String sentence = "Hast du Heineken?";
        String expectedTranslation = "Had you Heineken?";

        String actualTranslation = translator.translateSentence(sentence);

        assertEquals(expectedTranslation, actualTranslation);
    }

}
