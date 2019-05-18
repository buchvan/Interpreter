package ru.imit.omsu;

import ru.imit.omsu.errors.ErrorCode;
import ru.imit.omsu.errors.GrammarException;
import ru.imit.omsu.models.Program;
import ru.imit.omsu.models.expressions.Expression;
import ru.imit.omsu.models.functions.FunctionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Interpreter {

    public static Program program = new Program();

    public static final String RE_CHARACTER = "[A-Za-z_]";
    public static final String RE_DIGIT = "\\d";
    public static final String RE_NUMBER = RE_DIGIT + "+";

    public static final String RE_IDENTIFIER_INSERTED = RE_CHARACTER + "+";
    public static final String RE_IDENTIFIER_COMPLETELY = "^" + RE_IDENTIFIER_INSERTED + "$";

    public static final Pattern IDENTIFIER_INSERTED_PATTERN = Pattern.compile(RE_IDENTIFIER_INSERTED);
    public static final Pattern IDENTIFIER_COMPLETELY_PATTERN = Pattern.compile(RE_IDENTIFIER_COMPLETELY);

    public static final String RE_OPERATION = "[+\\-*/%><=]";

    public static final Pattern OPERATION_PATTERN = Pattern.compile(RE_OPERATION);

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Print lines of program (Enter - new line, ^ - end of input):");
            List<String> lines = new ArrayList<>();
            lines.add(scanner.nextLine());
            while (!lines.get(lines.size() - 1).equals("^")) {
                lines.add(scanner.nextLine());
            }
            lines.remove(lines.size() - 1);
            String errorRe = "[~!@#$^.\t\\s]";
            Pattern patternError = Pattern.compile(errorRe);
            Expression expression;
            int i = 0;
            if (lines.size() > 1) {
                for (i = 0; i < lines.size() - 1; i++) {
                    if (patternError.matcher(lines.get(i)).find()) {
                        throw new GrammarException(ErrorCode.SYNTAX_ERROR);
                    }
                    program.addFunctionDefinition(FunctionDefinition.getFunctionDefinition(lines.get(i), i + 1));
                }
            }
            i++;
            expression = Expression.getExpression(lines.get(lines.size() - 1), i);
            program.setExpression(expression);
            System.out.println(program.run());
        } catch (GrammarException ex) {
            System.out.println(ex.toString());
        }
    }

}
