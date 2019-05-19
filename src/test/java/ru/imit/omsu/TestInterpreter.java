package ru.imit.omsu;

import org.junit.jupiter.api.Test;
import ru.imit.omsu.errors.InterpreterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestInterpreter {

    private List<String> getLinesForCalculate1() {
        return new ArrayList<>(Collections.singletonList(
                "(2+2)"
        ));
    }

    @Test
    public void calculateTest1() throws InterpreterException {
        assertEquals(4, Interpreter.run(getLinesForCalculate1()));
    }

    private List<String> getLinesForCalculate2() {
        return new ArrayList<>(Collections.singletonList(
                "(2+((3*4)/5))"
        ));
    }

    @Test
    public void calculateTest2() throws InterpreterException {
        assertEquals(4, Interpreter.run(getLinesForCalculate2()));
    }

    private List<String> getLinesForCalculate3() {
        return new ArrayList<>(Collections.singletonList(
                "(-5%3)"
        ));
    }

    @Test
    public void calculateTest3() throws InterpreterException {
        assertEquals(-2, Interpreter.run(getLinesForCalculate3()));
    }

    private List<String> getLinesForCalculate4() {
        return new ArrayList<>(Collections.singletonList(
                "([(2>5)]?{2}:{7}%3)"
        ));
    }

    @Test
    public void calculateTest4() throws InterpreterException {
        assertEquals(1, Interpreter.run(getLinesForCalculate4()));
    }

    private List<String> getLinesForIfExpression1() {
        return new ArrayList<>(Collections.singletonList(
                "[((10+20)>(20+10))]?{1}:{0}"
        ));
    }

    @Test
    public void ifExpressionTest1() throws InterpreterException {
        assertEquals(0, Interpreter.run(getLinesForIfExpression1()));
    }

    private List<String> getLinesForFunctionDefinitionList1() {
        return new ArrayList<>(Arrays.asList(
                "g(x)={(f(x)+f((x/2)))}",
                "f(x)={[(x>1)]?{(f((x-1))+f((x-2)))}:{x}}",
                "g(10)"
        ));
    }

    @Test
    public void functionDefinitionListTest() throws InterpreterException {
        assertEquals(60, Interpreter.run(getLinesForFunctionDefinitionList1()));
    }

    private List<String> getLinesForError1() {
        return new ArrayList<>(Collections.singletonList(
                "1 + 2 + 3 + 4 + 5"
        ));
    }

    @Test
    public void errorTest1() {
        try {
            Interpreter.run(getLinesForError1());
            fail();
        } catch (InterpreterException ex) {
            assertEquals("SYNTAX ERROR", ex.toString());
        }
    }

    private List<String> getLinesForError2() {
        return new ArrayList<>(Arrays.asList(
                "f(x)={y}",
                "f(10)"
        ));
    }

    @Test
    public void errorTest2() {
        try {
            Interpreter.run(getLinesForError2());
            fail();
        } catch (InterpreterException ex) {
            assertEquals("PARAMETER NOT FOUND y:1", ex.toString());
        }
    }

    private List<String> getLinesForError3() {
        return new ArrayList<>(Arrays.asList(
                "g(x)={f(x)}",
                "g(10)"
        ));
    }

    @Test
    public void errorTest3() {
        try {
            Interpreter.run(getLinesForError3());
            fail();
        } catch (InterpreterException ex) {
            assertEquals("FUNCTION NOT FOUND f:1", ex.toString());
        }
    }

    private List<String> getLinesForError4() {
        return new ArrayList<>(Arrays.asList(
                "g(x)={(x+1)}",
                "g(10,20)"
        ));
    }

    @Test
    public void errorTest4() {
        try {
            Interpreter.run(getLinesForError4());
            fail();
        } catch (InterpreterException ex) {
            assertEquals("ARGUMENT NUMBER MISMATCH g:2", ex.toString());
        }
    }

    private List<String> getLinesForError5() {
        return new ArrayList<>(Arrays.asList(
                "g(a,b)={(a/b)}",
                "g(10,0)"
        ));
    }

    @Test
    public void errorTest5() {
        try {
            Interpreter.run(getLinesForError5());
            fail();
        } catch (InterpreterException ex) {
            assertEquals("RUNTIME ERROR (a/b):1", ex.toString());
        }
    }

}
