/*
  Name: Yu Kit, Foo
  GUID: 2441458f
 */

package detectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * class : Driver.java
 * feeds compilation unit to UselessControlFlowDetector.java and RecursionDetector.java
 */
public class Driver {
//    to use FILE_PATH, put test code in the same directory as src code
//    then change the name of Calculator.java to file name to be tested


    public static void main(String[] args) {
        try {
            final String FILE_PATH = args[0];
            // opens the file parse it as compilation unit
            CompilationUnit cu = JavaParser.parse(new FileInputStream(FILE_PATH));

//            find useless control flows in the compiled java file
            UselessControlFlowDetector controlFlowAnalyzer = new UselessControlFlowDetector();
            List<Breakpoints> collectorUCFD = new ArrayList<Breakpoints>();
            controlFlowAnalyzer.visit(cu,collectorUCFD);
            System.out.println("Useless Control Flows:");
            for (Breakpoints b : collectorUCFD) System.out.println(b);

//            finds recursions in the compiled java file
            RecursionDetector recursionAnalyzer = new RecursionDetector();
            List<Breakpoints> collectorRD = new ArrayList<Breakpoints>();
            recursionAnalyzer.visit(cu, collectorRD);
            System.out.println("\nRecursions:");
            for (Breakpoints b : collectorRD) System.out.println(b);


        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
