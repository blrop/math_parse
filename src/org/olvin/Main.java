// 08.2014
package org.olvin;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please give an expression as a first parameter. (Do not forget for quotes if the expression has spaces!)");
            System.exit(0);
        }

        String input = args[0];

        // убираем пробелы:
        input = input.replaceAll(" ", "");

        // заменяем константы значениями:
        input = input.replaceAll("PI", Double.toString(Math.PI));
        input = input.replaceAll("E", Double.toString(Math.E));

        // проверяем парность скобок:
        if (!isBracketsPair(input)) {
            System.out.println("Error: Brackets are not pair.");
            System.exit(1);
        }

        double result = eval(input);
        result = (double)Math.round(result * 100000) / 100000;
	    System.out.println("Result: " + result);
    }

    public static boolean isBracketsPair(String input) {
        int inputLenght = input.length();
        int bracketsCount = 0;
        for(int i = 0; i < inputLenght; i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '(') {
                bracketsCount++;
            } else if (currentChar == ')') {
                bracketsCount--;
            }
            if (bracketsCount < 0) {
                return false;
            }
        }
        return bracketsCount == 0;
    }

    public static double eval(String expr) {
        // убираем внешние лишние скобки:
        while (expr.charAt(0) == '(' && expr.charAt(expr.length() - 1) == ')'
                && isBracketsPair(expr.substring(1, expr.length() - 1))) {
            expr = expr.substring(1, expr.length() - 1);
        }

        int exprLength = expr.length();
        int openedBrackets = 0;
        int prior1pos = -1;
        int prior2pos = -1;
        for(int i = 0; i < exprLength; i++) {
            char currentChar = expr.charAt(i);
            if (currentChar == '(') {
                openedBrackets++;
                continue;
            }
            if (currentChar == ')') {
                openedBrackets--;
                continue;
            }
            if (openedBrackets > 0) {
                continue;
            }

            // находим по последнему оператору с приоритетами 1 и 2:
            if ((currentChar == '*' || currentChar == '/')) {
                prior1pos = i;
            }
            if ((currentChar == '+' || currentChar == '-')) {
                prior2pos = i;
            }
        }
        if (prior2pos != -1) {
            String expr1 = expr.substring(0, prior2pos);
            String expr2 = expr.substring(prior2pos + 1);
            if (expr.charAt(prior2pos) == '+') {
                return eval(expr1) + eval(expr2);
            }
            if (expr.charAt(prior2pos) == '-') {
                return eval(expr1) - eval(expr2);
            }
        }
        if (prior1pos != -1) {
            String expr1 = expr.substring(0, prior1pos);
            String expr2 = expr.substring(prior1pos + 1);
            if (expr.charAt(prior1pos) == '*') {
                return eval(expr1) * eval(expr2);
            }
            if (expr.charAt(prior1pos) == '/') {
                double operand2 = eval(expr2);
                if (operand2 == 0) {
                    System.out.println("Error: Division by zero.");
                    System.exit(200); // Turbo Pascal - вы помните это?
                }
                return eval(expr1) / operand2;
            }
        }

        // здесь только члены выражения, без операторов:
        if (expr.contains("sin(")) {
            return Math.sin(eval(expr.substring(4, expr.length() - 1)));
        }
        if (expr.contains("cos(")) {
            return Math.cos(eval(expr.substring(4, expr.length() - 1)));
        }
        if (expr.contains("exp(")) {
            return Math.exp(eval(expr.substring(4, expr.length() - 1)));
        }
        return Double.parseDouble(expr);
    }
}
