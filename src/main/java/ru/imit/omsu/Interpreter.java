package ru.imit.omsu;

import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;
import ru.imit.omsu.models.Program;
import ru.imit.omsu.models.expressions.Expression;
import ru.imit.omsu.models.functions.FunctionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Interpreter {

    public static Program program;

    public static final String RE_CHARACTER = "[A-Za-z_]";
    public static final String RE_DIGIT = "\\d";
    public static final String RE_NUMBER = RE_DIGIT + "+";

    public static final String RE_IDENTIFIER_INSERTED = RE_CHARACTER + "+";
    public static final String RE_IDENTIFIER_COMPLETELY = "^" + RE_IDENTIFIER_INSERTED + "$";

    public static final Pattern IDENTIFIER_COMPLETELY_PATTERN = Pattern.compile(RE_IDENTIFIER_COMPLETELY);

    public static final String RE_OPERATION = "[+\\-*/%><=]";

    public static final Pattern OPERATION_PATTERN = Pattern.compile(RE_OPERATION);

    public static final String RE_ERROR = "[~!@#$^.\t\\s]";

    public static final Pattern ERROR_PATTERN = Pattern.compile(RE_ERROR);


    public static void checkOfIdentifier(String identifier) throws InterpreterException {
        if (!Interpreter.IDENTIFIER_COMPLETELY_PATTERN.matcher(identifier).find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
    }


    public static int run(List<String> lines) throws InterpreterException {
        String currentLine;
        int currentLineIndex = 0;
        program = new Program();
        if (lines.size() > 1) {
            for (currentLineIndex = 0; currentLineIndex < lines.size() - 1; currentLineIndex++) {
                currentLine = lines.get(currentLineIndex);
                if (ERROR_PATTERN.matcher(currentLine).find()) {
                    throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
                }
                program.addFunctionDefinition(FunctionDefinition.getFunctionDefinition(currentLine, currentLineIndex + 1));
            }
        }
        currentLineIndex++;
        currentLine = lines.get(lines.size() - 1);
        if (ERROR_PATTERN.matcher(currentLine).find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
        Expression expression = Expression.getExpression(currentLine);
        program.setExpression(expression);
        program.setLinesCount(currentLineIndex);
        return program.run();
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Print lines of program (Enter - new line, ^ - end of input):");
        List<String> lines = new ArrayList<>();
        lines.add(scanner.nextLine());
        while (!lines.get(lines.size() - 1).equals("^")) {
            lines.add(scanner.nextLine());
        }
        lines.remove(lines.size() - 1);
        try {
            System.out.println(run(lines));
        } catch (InterpreterException ex) {
            System.out.println(ex.toString());
        }
    }

}
