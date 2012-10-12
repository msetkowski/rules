package org.wowbagger.rules;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.wowbagger.rules.annotation.Invariant;
import org.wowbagger.rules.annotation.Invariants;

/**
 * Rule for testing class's invariants. Based on OOD and DDD principles.
 * 
 * This rule must be used as ClassRule! Implementation provides two annotation, one should be used in 
 * source code as definition of invariants, second one is designed for tests to verify if all invariants are tested
 * 
 * Example:
 * <pre>
    &#064;ClassRule public static InvariantRule invariantRule = new InvariantRule(InvariantObject.class);
    
    &#064;Invariant(name="counter")
    &#064;Test
    public void test() {
       int i=0;
    }
	
    Correspondent code for InvariantObject contains code like below:
    &#064;Invariants(
        names={"date","counter"}, 
        descriptions={"some date rules","greater than 0"})
    class InvariantObject{
       ...
	
 * </pre>
 *
 */
public class InvariantRule implements TestRule {

	private static final Logger LOGGER = Logger.getLogger(InvariantRule.class.getName());
	
	private Map<String, Integer> invariants = new HashMap<String, Integer>();	
		
	private Class<?> testObject;
	
	public InvariantRule(Class<?> clazz)  {
		
		try {
			testObject = (Class<?>) Class.forName(clazz.getCanonicalName());
		} catch (SecurityException e) {
			LOGGER.severe(e.getMessage());
		} catch (ClassNotFoundException e) {
			LOGGER.severe(e.getMessage());
		}
	}
	
	public void before() {
		//scan all invariants to initialize map
		Invariants inv = (Invariants) testObject.getAnnotation(Invariants.class);
		if(inv == null){
			return;
		}		
		for (int i = 0; i < inv.names().length; i++) {
			invariants.put(inv.names()[i], 0);
		}
	}

	
	public void after() {
		//check map for pairs with 0-values
		for (Map.Entry<String, Integer> entry : invariants.entrySet()){
			if(entry.getValue().equals(0)){
				Assert.fail("Some invarians are not covered with tests " + entry.getKey());
			}
		}
		invariants.clear();
	}
	
	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				before();
				try {
					verifyInvariants(description);
					base.evaluate();
				} catch (Throwable t) {
					throw t;
				} finally {
					after();
				}
			}

			private void verifyInvariants(Description description) {
				Method[] methods = description.getTestClass().getMethods();
				for (int i = 0; i < methods.length; i++) {
					if(methods[i].getAnnotation(Test.class)!=null){
						Ignore ignore = methods[i].getAnnotation(Ignore.class);
						Invariant inv = methods[i].getAnnotation(Invariant.class);
						if(inv!=null && ignore==null){
							Integer val = invariants.get(inv.name());
							invariants.put(inv.name(), val+1);
						}
					}
				}
			}
		};
	}

}
