import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Vocabulary_Object {
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        LineList<LetterClass>[] masL = new LineList[26]; // массив списков, формирующий таблицу. Каждая ячейка массива содержит списки слов, начинающиеся с одной и той же буквы
        for (int i = 0; i < masL.length; i++) {
            masL[i] = new LineList<>();
        }
        if (!Files.exists(Paths.get("file_final.txt"))) {  // если файл с данными не существует, заполняем список начальными данными
            initialValues(masL);
        }
        boolean ansB = true;
        while (ansB) { // меню
            System.out.println(" 1 - узнать перевод слова ");
            System.out.println(" 2 - добавить слово с переводом ");
            System.out.println(" 3 - удалить слово и его перевод");
            System.out.println(" 4 - печать всего словаря ");
            System.out.println(" 5 - запись данных в файл ");
            System.out.println(" 6 - чтение данных из файла ");
            System.out.println(" 7 - выход ");
            String ansS = scan.nextLine();
            switch (ansS) {
                case ("1"):
                    findTranslation(masL);
                    break;
                case ("2"):
                    addLetter(masL);
                    break;
                case ("3"):
                    removeLetter(masL);
                    break;
                case ("4"):
                    printVoc(masL);
                    break;
                case ("5"):
                    fileWrite(masL);
                    break;
                case ("6"):
                    fileRead(masL, formFileToString());
                    break;
                case ("7"):
                    ansB = false;
                    break;
            }
        }
    }

    // методы меню

    static LineList<LetterClass>[] addLetter(LineList<LetterClass>[] masL) { // Добавление слова и его перевода в рассчитываемое место
        boolean ans = true;
        String s_l = enterValue();
        String s_t = "";
        System.out.println("Введите русское слово с заглавной буквы");
        while (true) {
            s_t = scan.nextLine();
            if (checkTranslation(s_t)) {
                break;
            } else {
                System.out.println("Некорректный ввод");
            }
        }
        char[] ch = s_l.toCharArray();
        LetterClass newLetter = new LetterClass(s_l, s_t, countKey(s_l));
        if (masL[ch[0] - 65].size() == 0) {  // Взависимости от первой буквы английского слова определяем ячейку с нужным списком (далее не комментирую этот процесс)
            masL[ch[0] - 65].addLast(newLetter);
        } else {
            for (int i = 0; i < masL[ch[0] - 65].size(); i++) {
                if (newLetter.getKey() < masL[ch[0] - 65].getElByIdx(i).getKey()) {
                    masL[ch[0] - 65].addByIndex(newLetter, i);
                    ans = false;
                    break;
                }
            }
            if (ans) {
                masL[ch[0] - 65].addLast(newLetter);
            }
        }
        return masL;
    }

    static LineList<LetterClass>[] removeLetter(LineList<LetterClass>[] masL) { // удаление элемента по слову
        String s = enterValue();
        char ch[] = s.toCharArray();
        int key = countKey(s);
        for (int i = 0; i < masL[ch[0] - 65].size(); i++) {
            if (masL[ch[0] - 65].getElByIdx(i).getKey() == key) {
                masL[ch[0] - 65].removeElByIdx(i);
            }
        }
        return masL;
    }

    static void findTranslation(LineList<LetterClass>[] masL) { // найти перевод слова
        String s = enterValue();
        char[] ch = s.toCharArray();
        int key = countKey(s);
        boolean ans = true;
        for (int i = 0; i < masL[ch[0] - 65].size(); i++) {
            if (key == masL[ch[0] - 65].getElByIdx(i).getKey()) {
                System.out.println(masL[ch[0] - 65].getElByIdx(i).getValue() + " - " + masL[ch[0] - 65].getElByIdx(i).getTranslation());
                ans = false;
                break;
            }
        }
        if (ans) {
            System.out.println("Слово не найдено");
        }
    }

    static void printVoc(LineList<LetterClass>[] masL) { // вывод всего словаря
        for (int i = 0; i < masL.length; i++) {
            int a = 65 + i;
            char b = (char) a;
            System.out.println(b + "\n");
            System.out.println(transformToString(masL[i]) + "\n");
        }
    }


    static File fileWrite(LineList<LetterClass>[] masL) { // запись текущего словаря в файл
        String s = "";
        for (int i = 0; i < masL.length; i++) {
            s += transformToString(masL[i]);
        }
        File file = new File("file_final.txt");
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(s);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    static LineList<LetterClass>[] fileRead(LineList<LetterClass>[] masL, String s) { // чтение из файла в список
        for (int i = 0; i < masL.length; i++) {
            masL[i].clear();
        }
        String[] masBig = s.split("____________________\n");
        for (int i = 0; i < masBig.length; i++) {
            String[] masMedium = masBig[i].split("\n");
            for (int j = 0; j < masMedium.length; j++) {
                String[] masLittle = masMedium[j].split(" - ");
                masL[i].addLast(new LetterClass(masLittle[0], masLittle[1], countKey(masLittle[0])));
            }
        }
        return masL;
    }

    static LineList<LetterClass>[] initialValues(LineList<LetterClass>[] masL) { // чтение данных из начального файла (при учете, что другого не было создано)
        try {
            File file = new File("file_init.txt");
            FileReader fr = new FileReader(file);
            BufferedReader buf = new BufferedReader(fr);
            String line = buf.readLine(); // если что-то пойдет не так, попробовать StringBuilder
            while (line != null) {
                boolean ans = true;
                String[] mas = line.split(" - ");
                char[] ch = mas[0].toCharArray();
                LetterClass newLetter = new LetterClass(mas[0], mas[1], countKey(mas[0]));
                if (masL[ch[0] - 65].size() == 0) {
                    masL[ch[0] - 65].addLast(newLetter);
                } else {
                    for (int i = 0; i < masL[ch[0] - 65].size(); i++) {
                        if (newLetter.getKey() < masL[ch[0] - 65].getElByIdx(i).getKey()) {
                            masL[ch[0] - 65].addByIndex(newLetter, i);
                            ans = false;
                            break;
                        }
                    }
                    if (ans) {
                        masL[ch[0] - 65].addLast(newLetter);
                    }
                }
                line = "";
                line = buf.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return masL;
    }

// ____________________________________________________________________________________________________________________________________________
    // вспомогательные методы

    static String formFileToString() { // считывание данных из файла с строку для дальнейшего считыванния из строки в список
        String s = "";
        try {
            InputStream is = new FileInputStream("file_final.txt");
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
                s = sb.toString();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return s;

    }

    static String transformToString(LineList<LetterClass> letter) { // преобразование списка в строку
        String s = "";
        for (int j = 0; j < letter.size(); j++) {
            s += letter.getElByIdx(j).getValue();
            s += " - ";
            s += letter.getElByIdx(j).getTranslation() + "\n";
        }
        s += "____________________\n"; // 20 подчеркиваний
        return s;
    }

    static int countKey(String s) { // вычисление клоючевого значения (сумма кодов каждой буквы английского слова и последующее деление суммы на количество букв в слове)
        int key = 0;
        char[] mas = s.toCharArray();
        for (int i = 0; i < mas.length; i++) {
            key += mas[i];
        }
        key /= mas.length;
        return key;
    }

    static String enterValue() { // ввод новых данных с консоли
        String s = "";
        System.out.println("Введите английское слово с заглавной буквы");
        while (true) {
            s = scan.nextLine();
            if (checkValue(s)) {
                break;
            } else {
                System.out.println("Некорректный ввод");
            }
        }
        return s;
    }

    static boolean checkValue(String s) { // Проверка на ввод с консоли английского слова
        char[] mas = s.toCharArray();
        boolean ans = true;
        if (mas[0] >= 'A' && mas[0] <= 'Z') {
            for (int i = 1; i < mas.length; i++) {
                if (!(mas[i] >= 'a' && mas[i] <= 'z')) {
                    ans = false;
                    break;
                }
            }
        } else {
            ans = false;
        }
        return ans;
    }

    static boolean checkTranslation(String s) { // Проверка на ввод с консоли русского слова
        char[] mas = s.toCharArray();
        boolean ans = true;
        if (mas[0] >= 'А' && mas[0] <= 'Я') {
            for (int i = 1; i < mas.length; i++) {
                if (!(mas[i] >= 'а' && mas[i] <= 'я')) {
                    ans = false;
                    break;
                }
            }
        } else {
            ans = false;
        }
        return ans;
    }

}
