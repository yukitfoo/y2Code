/*
  Name: Yu Kit, Foo
  GUID: 2441458f
 */

package detectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * class: RecursionDetector.java detects when a recursion is called in a method
 */
public class RecursionDetector  extends VoidVisitorAdapter<List<Breakpoints>> {

    @Override
    public void visit(MethodDeclaration md, List<Breakpoints> collector) {
        super.visit(md, collector);

        CompilationUnit cu = md.findCompilationUnit().get();
        String className = cu.findFirst(ClassOrInterfaceDeclaration.class)
                .get().getName().toString();
        String methodName = md.getNameAsString();
        
        md.findAll(MethodCallExpr.class).stream().forEach(methodcall -> {
            // detects recursion
            if (methodcall.getNameAsString().equals(methodName)) {
                addToList(collector, className,methodName,methodcall.getRange().get().begin.line, methodcall.getRange().get().end.line);
            }
        });

    }

//    helper function to add breakpoint to collector (List<Breakpoints>)
    private void addToList(List<Breakpoints> collector, String className, String methodName, int startLine, int endLine) {
        Breakpoints newB = new Breakpoints(className, methodName, startLine, endLine);
        boolean inList = false;
        for (Breakpoints b: collector) {
            if (b.equals(newB)) inList = true;
        }
        if (!inList) {
            collector.add(new Breakpoints(className, methodName, startLine, endLine));
        }
    }
}


