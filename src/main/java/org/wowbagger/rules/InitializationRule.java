package org.wowbagger.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Base class for implementing rules, that can replace repeatable initialization and shutdown environment for UnitTests.
 *  
 * @author setkomac
 *
 */
public abstract class InitializationRule implements TestRule {

	/**
	 * Method will be called before test body.
	 */
	public abstract void before();

	/**
	 * Method will be called after test body (also for failed test).
	 */
	public abstract void after();

	/**
	 * @see TestRule#apply(Statement, Description)
	 */
	@Override
	public Statement apply(final Statement base, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				before();
				try {
					base.evaluate();
				} catch (Throwable t) {
					throw t;
				} finally {
					after();
				}
			}
		};
	}
}
