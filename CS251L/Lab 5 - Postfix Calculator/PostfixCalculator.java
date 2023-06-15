// Anish Adhikari UNM- aaadhikari999@gmail.com
// CS 251 - Lab 5 - Postfix Calculator
// Postfix calculator that calculates math using
// reverse Polish notation

// Extra operators: fact, cos, sin, tan, power, mod
// Exception handling on: NullPointer on invalid operator string

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostfixCalculator {
    
    private Stack<Double> m_operandList = new DoubleStack();
    private Map<String, Operator> m_operatorMap = new HashMap<String, Operator>();
    
    /**
     * PostfixCalculator default constructor, initializes all the string operators that will
     * be given at the command line. These are added to the operator HashMap
     */
    public PostfixCalculator() {
        m_operatorMap.put("+", new add());
        m_operatorMap.put("add", new add());
        m_operatorMap.put("-", new sub());
        m_operatorMap.put("sub", new sub());
        m_operatorMap.put("*", new mult());
        m_operatorMap.put("mult", new mult());
        m_operatorMap.put("/", new div());
        m_operatorMap.put("div", new div());
        m_operatorMap.put("!", new fact());
        m_operatorMap.put("fact", new fact());
        m_operatorMap.put("=", new print());
        m_operatorMap.put("cos", new cos());
        m_operatorMap.put("sin", new sin());
        m_operatorMap.put("tan", new tan());
        m_operatorMap.put("^", new power());
        m_operatorMap.put("power", new power());
        m_operatorMap.put("%", new mod());
        m_operatorMap.put("mod", new mod());
        m_operatorMap.put("print", new print());       
    }
    
    /**
     * Add operator, adds two operands together
     */
    private class add implements Operator {
        public int numArgs() {
            return 2;
        }
        
        public double eval(List<Double> args) {
            return (args.get(1) + args.get(0));
        }
    }
    
    /**
     * Sub operator, subtracts one operand from another
     */
    private class sub implements Operator {
        public int numArgs() {
            return 2;
        }
        
        public double eval(List<Double> args) {
            return (args.get(1) - args.get(0));
        }
    }
    
    /**
     * Mult operator, multiplies two operands together
     */
    private class mult implements Operator {
        public int numArgs() {
            return 2;
        }
        
        public double eval(List<Double> args) {
            return (args.get(1) * args.get(0));
        }
    }
    
    /**
     * Div operator, divides one operand by another
     */
    private class div implements Operator {
        public int numArgs() {
            return 2;
        }
        
        public double eval(List<Double> args) {
            return (args.get(1) / args.get(0));
        }
    }
    
    /**
     * Fact operator, gives the factorial of one operand
     */
    private class fact implements Operator {
        public int numArgs() {
            return 1;
        }
        
        public double eval(List<Double> args) {
            
            double factorialNum = args.get(0);
            
            // The factorial of 0 is 1
            if(factorialNum == 0.0) {
                return 1.0;
            }
            else {
                double sum = 1.0;
                
                for(int i = 1; i < factorialNum + 1; ++i) {
                    sum *= i;
                }
                
                return sum;
            }
        }
    }
    
    /**
     * Cos operator, takes the cosine of one operand
     */
    private class cos implements Operator {
        public int numArgs() {
            return 1;
        }
        
        public double eval(List<Double> args) {
            return Math.cos(args.get(0));
        }
    }
    
    /**
     * Sin operator, takes the sine of one operand
     */
    private class sin implements Operator {
        public int numArgs() {
            return 1;
        }
        
        public double eval(List<Double> args) {
            return Math.sin(args.get(0));
        }
    }
    
    /**
     * Tan operator, takes the tangent of one operand
     */
    private class tan implements Operator {
        public int numArgs() {
            return 1;
        }
        
        public double eval(List<Double> args) {
            return Math.tan(args.get(0));
        }
    }
    
    /**
     * Power operator, exponentiates one operand by another 
     */
    private class power implements Operator {
        public int numArgs() {
            return 2;
        }
        
        public double eval(List<Double> args) {
            return Math.pow(args.get(1), args.get(0));
        }
    }
    
    /**
     * Mod operator, modulos one operand to another
     */
    private class mod implements Operator {
        public int numArgs() {
            return 2;
        }
        
        public double eval(List<Double> args) {
            return (args.get(1) % args.get(0));
        }
    }
    
    /**
     * Print operator, prints whatever item is sitting on the stack
     */
    private class print implements Operator {
        public int numArgs() {
            return 1;
        }
        
        public double eval(List<Double> args) {
            System.out.println(args.get(0));
            return args.get(0);
        }
    }
    
    /**
     * Stores an operand into the list of operands we are working on for future processing
     * @param operand The double to add to th list
     */
    public void storeOperand(Double operand) {
        m_operandList.push(operand);
    }
    
    /**
     * Evaluates an operator based on how many arguments the operator takes.
     * Additionally, this is expected to throw a NullPointerException if the
     * operator is invalid so we're catching this here also.
     * @param operator String coming from stdin for processing operands
     */
    public void evalOperator(String operator) {
        List<Double> listOfOperands = new ArrayList<Double>();
        Operator op = m_operatorMap.get(operator); 
        
        // Wrapping with a try block in case the user entered an operator that
        // isn't in the list. We can catch the null pointer that will be thrown
        try {
            // Adding how many operands were specified by the operator class
            for(int i = 0; i < op.numArgs(); ++i) {
                listOfOperands.add(m_operandList.pop());
            }
            
            // Pushing the output from the evaluation back onto the stack
            storeOperand(op.eval(listOfOperands));
        }
        catch (NullPointerException ex) {
            System.out.println("Not a valid operator!");
        }
    }
    
}
