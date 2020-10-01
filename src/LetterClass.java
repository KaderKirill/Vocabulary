public class LetterClass { // класс, описывающий объект, состоящий из английского слова, перевода на русский и ключевого значения слова
    private String value; // английское слово
    private String translation; // русское слово (перевод с английского)
    private int key; // код слова (ключевое значение)

    public LetterClass(String value, String translation, int key) {
        this.value = value;
        this.translation = translation;
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTranslation() {
        return this.translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
