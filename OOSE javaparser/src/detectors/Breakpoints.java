/*
  Name: Yu Kit, Foo
  GUID: 2441458f
 */

package detectors;

/**
 * container class Breakpoints
 * defined what fields needed to be stored
 * toString is overriden to fit the format given on the worksheet
 */
public class Breakpoints {
    private String className;
    private String methodName;
    private int startLine;
    private int endLine;

    public Breakpoints(String className, String methodName, int startLine, int endLine) {
        this.className = className;
        this.methodName = methodName;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    @Override
    public String toString() {
        return "className ="+this.className+",methodName ="+this.methodName+",startline ="+this.startLine+", endline ="+this.endLine;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Breakpoints breakpoint = (Breakpoints) o;
        return className.equals(breakpoint.className) &&
                methodName.equals(breakpoint.methodName) &&
                startLine == breakpoint.startLine &&
                endLine == breakpoint.endLine;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
