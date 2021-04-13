/*
  Name: Yu Kit, Foo
  GUID: 2441458f
 */

package detectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * class: UselessControlFlowDetector.java detects a useless control flow
 * Useless control flow defined by finding all control flows(ie: IfStmt.class, ForStmt.class)
 * and handles each control flow differently
 * Useless Control FLow are statements in control flows that doesn't contribute to the program being different
 * ie: print statements and empty blocks are useless control flow
 * other type of statements such as assignment, operational, method calls, return statements are not useless cotrol flows
 */
public class UselessControlFlowDetector extends VoidVisitorAdapter <List<Breakpoints>> {


    @Override
    public void visit(MethodDeclaration md, List<Breakpoints> collector) {
        super.visit(md, collector);

        CompilationUnit cu = md.findCompilationUnit().get();
        String className = cu.findFirst(ClassOrInterfaceDeclaration.class)
                .get().getName().toString();
        String methodName = md.getNameAsString();

        md.findAll(Statement.class).forEach(statement -> {
            if (statement.isIfStmt()) {
                statement.findAll(BlockStmt.class).stream().forEach(blocks -> {
//                    case for when if statement is only comments
                    if (blocks.isEmpty()) {
                        addToList(collector, className,methodName,blocks.getRange().get().begin.line, blocks.getRange().get().end.line);
                    }
                    blocks.getStatements().stream().forEach(expression -> {
//                        return statement are always useful(not useless control flow)
                        if (!expression.isReturnStmt()) {
                            if (expression.isExpressionStmt()) {
//                                checks for method calls
                                expression.findAll(MethodCallExpr.class).forEach(methodcall -> {
//                                    case if it is a print statement, useless as it doesn't affect overall functionality of program
                                    if ((methodcall.getNameAsString().equals("println"))
                                            ||(methodcall.getNameAsString().equals("print"))
                                            ||(methodcall.getNameAsString().equals("printf"))) {
                                        addToList(collector, className,methodName,blocks.getRange().get().begin.line, blocks.getRange().get().end.line);
                                    }
                                });
                            }
                        }
                    });
                });
            } else if (statement.isForStmt()) {
                statement.findAll(BlockStmt.class).stream().forEach(blocks -> {
//                    case for when for statement is only comments
                    if (blocks.isEmpty()) {
                        addToList(collector, className,methodName,blocks.getRange().get().begin.line, blocks.getRange().get().end.line);
                    }
                    blocks.getStatements().stream().forEach(expression -> {
//                        return statement are always useful(not useless control flow)
                        if (!expression.isReturnStmt()) {
                            if (expression.isExpressionStmt()) {
//                                checks for method calls
                                expression.findAll(MethodCallExpr.class).forEach(methodcall -> {
//                                    case if it is a print statement, useless as it doesn't affect overall functionality of program
                                    if ((methodcall.getNameAsString().equals("println"))
                                            ||(methodcall.getNameAsString().equals("print"))
                                            ||(methodcall.getNameAsString().equals("printf"))) {
                                        addToList(collector, className,methodName,blocks.getRange().get().begin.line, blocks.getRange().get().end.line);
                                    }
                                });
                            }
                        }
                    });
                });
            } else if (statement.isWhileStmt()) {
                statement.findAll(BlockStmt.class).stream().forEach(blocks -> {
//                    case for when while statement is only comments
                    if (blocks.isEmpty()) {
                        addToList(collector, className,methodName,blocks.getRange().get().begin.line, blocks.getRange().get().end.line);
                    }
                    blocks.getStatements().stream().forEach(expression -> {
//                        return statement are always useful(not useless control flow)
                        if (!expression.isReturnStmt()) {
                            if (expression.isExpressionStmt()) {
//                                checks for method calls
                                expression.findAll(MethodCallExpr.class).forEach(methodcall -> {
//                                    case if it is a print statement, useless as it doesn't affect overall functionality of program
                                    if ((methodcall.getNameAsString().equals("println"))
                                            ||(methodcall.getNameAsString().equals("print"))
                                            ||(methodcall.getNameAsString().equals("printf"))) {
                                        addToList(collector, className,methodName,blocks.getRange().get().begin.line, blocks.getRange().get().end.line);
                                    }
                                });
                            }
                        }
                    });
                });
            }else if (statement.isDoStmt()) {
                statement.findAll(BlockStmt.class).stream().forEach(blocks -> {
//                    case for when do-while statement is only comments
                    if (blocks.isEmpty()) {
                        addToList(collector, className,methodName,blocks.getRange().get().begin.line, blocks.getRange().get().end.line);
                    }
                    blocks.getStatements().stream().forEach(expression -> {
//                        return statement are always useful(not useless control flow)
                        if (!expression.isReturnStmt()) {
                            if (expression.isExpressionStmt()) {
//                                checks for method calls
                                expression.findAll(MethodCallExpr.class).forEach(methodcall -> {
//                                    case if it is a print statement, useless as it doesn't affect overall functionality of program
                                    if ((methodcall.getNameAsString().equals("println"))
                                            ||(methodcall.getNameAsString().equals("print"))
                                            ||(methodcall.getNameAsString().equals("printf"))) {
                                        addToList(collector, className,methodName,blocks.getRange().get().begin.line, blocks.getRange().get().end.line);
                                    }
                                });
                            }
                        }
                    });
                });
            }else if (statement.isSwitchStmt()) {
                statement.findAll(SwitchEntry.class).stream().forEach(entry -> {
                    entry.findAll(Statement.class).stream().forEach(entryStatement -> {
//                        only consider statements that are not break or return statement as they are useful control flow
                        if ((!entryStatement.isBreakStmt())||(!entryStatement.isReturnStmt())) {
                            if (entryStatement.isExpressionStmt()) {
                                entryStatement.findAll(MethodCallExpr.class).forEach(methodcall -> {
//                                    case when method called is print statements
                                    if ((methodcall.getNameAsString().equals("println"))
                                            ||(methodcall.getNameAsString().equals("print"))
                                            ||(methodcall.getNameAsString().equals("printf"))) {
                                        addToList(collector, className,methodName,entryStatement.getRange().get().begin.line, entryStatement.getRange().get().end.line);
                                    }
                                });
                            }
                        }

                    });
                });
            }
        });
    }

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

