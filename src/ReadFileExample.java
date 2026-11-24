//В директории, указанной пользователем, обработать файл с расширением .java.
//Сжать код за счет удаления лишних пробелов и символов перехода на новую строку
//Сжать код за счет удаления комментариев
//Заменить имя класса, а также соответственно имя файла и имена конструкторов
//Заменить идентификаторы на односимвольные или двухсимвольные имена в зависимости от количества найденных идентификаторов в коде


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

class Replace {
    static final String[] keywords = {
            "public", "private", "protected", "class", "static", "void", "final",
            "int", "long", "double", "float", "boolean", "char", "byte", "short",
            "new", "return", "if", "else", "for", "while", "do", "switch", "case",
            "break", "continue", "try", "catch", "finally", "throw", "throws",
            "import", "package", "true", "false", "null", "this", "super",
            "String", "System", "out", "println", "main", "args"
    };

    static boolean isKeyword(String word) {
        for (String kw : keywords) {
            if (kw.equals(word)) {
                return true;
            }
        }
        return false;
    }

    static String shortName(int index, boolean twoChars) {
        if (!twoChars) {
            return String.valueOf((char) ('a' + (index % 26)));
        }
        char first = (char) ('a' + ((index / 26) % 26));
        char second = (char) ('a' + (index % 26));
        return "" + first + second;
    }
}

public class ReadFileExample {
    public static void main(String[] args) {
        String oldClassName = "Shifr";
        String newClassName = "Lab10";

        Scanner console = new Scanner(System.in);
        System.out.print("Путь к директории или .java файлу (Enter = по умолчанию): ");
        String dirInput = console.nextLine().trim();

        Path input = dirInput.isEmpty()
                ? Path.of("C:\\Users\\Привет\\IdeaProjects\\shifr\\src\\Main.java")
                : Path.of(dirInput);

        if (Files.isDirectory(input)) {
            input = input.resolve("Main.java");
        }

        if (!input.toString().endsWith(".java")) {
            System.out.println("Нужен файл с расширением .java");
            return;
        }

        Path output = Path.of(newClassName + ".java");

        try (
                BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
                BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)
        ) {
            String line;
            int count = 0;
            boolean twoChars = false; 

            while ((line = reader.readLine()) != null) {
                System.out.println(line);

                Scanner scanner = new Scanner(line);

                while (scanner.hasNext()) {
                    String data = scanner.next();
                    String result1 = data.replaceAll("\\s+", " ").trim();
                    String result2 = result1.replaceAll("\n", "");
                    String result3 = result2.replaceAll("//.*", "");
                    result3 = result3.replaceAll("/\\*.*?\\*/", "");
                    String updateName = result3.replaceAll("\\b" + oldClassName + "\\b", newClassName);

                    String finalWord = updateName;
                    if (!finalWord.isEmpty()
                            && finalWord.matches("[a-zA-Z_][a-zA-Z0-9_]*")
                            && !Replace.isKeyword(finalWord)
                            && !finalWord.equals(oldClassName)
                            && !finalWord.equals(newClassName)) {//equals ранвы ли объекты
                        finalWord = Replace.shortName(count++, twoChars);
                    }

                    if (!finalWord.isEmpty()) {
                        writer.write(finalWord);
                        writer.newLine();
                    }
                }
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}