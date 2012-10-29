package org.wowbagger.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.wowbagger.rules.intern.GetterSetterTester;

/**
 * Rule for automatic test of getters and setters.
 * 
 * Implementation is a kind of wrapper for GetterSetterTester implemented by Steven Grimm.
 * <b>Note:</b> Implementation assume that getter for boolean value has form is[FieldName]
 * This rule should be used as ClassRule
 * Sample usage:
 * <pre>
	&#064;ClassRule
	public static GetterSetterRule&lt;TestedObject> tester = new GetterSetterRule&lt;TestedObject>(new TestedObject(), new String[]{"ignored"}, true);
 * </pre>
 * 
 * @author setkomac
 *
 */
public class GetterSetterRule<T> implements TestRule {
	private T testClass;
	private String[] ignores;
	private boolean verbose;
	
	/**
	 * Rule constructor 
	 * @param testClass tested instance
	 * @param ignores list of ignored fields
	 * @param verbose output mode, true produces messages for each tested method
	 */
	public GetterSetterRule(T testClass, String[] ignores, boolean verbose) {
		this.testClass = testClass;
		this.ignores = ignores;
		this.verbose = verbose;
	}

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
			
				try {
					executeTests();
					base.evaluate();
				} catch (Throwable t) {
					throw t;
				}
			}

			private void executeTests() throws Exception {
				
				GetterSetterTester tester = new GetterSetterTester(testClass);
				for (int i = 0; i < ignores.length; i++) {
					tester.exclude(ignores[i]);
				}
				tester.setVerbose(verbose);
				tester.test();
				
			}			
		};
	}
	
	
	
	
}
