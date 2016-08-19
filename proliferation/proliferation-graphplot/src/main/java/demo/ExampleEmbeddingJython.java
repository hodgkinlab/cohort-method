package demo;

import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class ExampleEmbeddingJython
{
	public static void main(String[] args) throws PyException
	{
		PythonInterpreter interp = new PythonInterpreter();

		System.out.println("Hello, brave new world");
		interp.exec("import sys");
		interp.exec("print sys");

		interp.set("a", new PyInteger(42));
		interp.exec("print a");
		interp.exec("x = 2+2");
		PyObject x = interp.get("x");

		System.out.println("x: " + x);
		
		// lets try and get variables out of the interpreter
		PyObject locals = interp.getLocals();
		System.out.println(locals);
		
		PyObject py_int = interp.eval("22");
		System.out.println("Testing evaluation");
		System.out.println("py_int:\t"+py_int);
		
		System.out.println("Goodbye, cruel world");
	}
}
