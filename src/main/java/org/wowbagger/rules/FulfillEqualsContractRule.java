package org.wowbagger.rules;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.wowbagger.rules.annotation.TestEqualsContract;


/**
 * This rule runs checks for equals implementation. 
 * Correct implementation should provide also hashCode() method and equals should fulfill some additional requirements like
 * <ul>
 *  <li>reflexive</li>
 *  <li>symmetric</li>
 *  <li>transitive</li>
 *  <li>consistent</li>
 *  <li>null comparison</li>
 * </ul>  
 * 
 * Usage: Rule should be defined in test body and one of the tests should be marked with annotation {@link TestEqualsContract}.
 * 
 * Example:
 * 
 * <pre>
 * 	&#064;Rule
 *	public FulfillEqualsContractRule&lt;MyObject> rule = new FulfillEqualsContractRule&lt;MyObject>(MyObject.class);
 *	
 *	&#064;Test
 *	&#064;TestEqualsContract
 *	public void testEqualsContract() {
 *	}
 * </pre>
 * 
 * @see TestEqualsContract
 * @see MethodRule
 * @see Rule
 * 
 * @author setkomac
 * 
 * @param <T> Type of tests object
 * <br/> 

 */
@SuppressWarnings("deprecation")
public class FulfillEqualsContractRule<T> implements MethodRule {

	private final static Logger LOGGER = Logger.getLogger(FulfillEqualsContractRule.class.getName());
	
	public T field;
	
	private Class<T> testObject;

	@SuppressWarnings("unchecked")
	public FulfillEqualsContractRule()  {
		try {
			testObject = (Class<T>) Class.forName(getClass().getField("field").getType().getCanonicalName());
		} catch (SecurityException e) {
			LOGGER.severe(e.getMessage());
		} catch (NoSuchFieldException e) {
			LOGGER.severe(e.getMessage());
		} catch (ClassNotFoundException e) {
			LOGGER.severe(e.getMessage());
		}
	}
	
	/**
	 * @see MethodRule#apply(Statement, FrameworkMethod, Object)
	 */
	public Statement apply(Statement base, final FrameworkMethod method, final Object target) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				
				TestEqualsContract contract = method.getAnnotation(TestEqualsContract.class);
                if (contract == null) {
                	return;
                }
				Method equals = null;
				try {
					equals = testObject.getDeclaredMethod("equals",
							new Object().getClass());
				} catch (NoSuchMethodException e) {
					return; // OK class could not implementing equals
				}
				if (equals != null) {
					// should implement also hashCode
					try {
						testObject.getDeclaredMethod("hashCode");
					} catch (NoSuchMethodException e) {

						throw new AssertionFailedError("Class "
								+ testObject.getName()
								+ " implements method equals but hashCode() isn't present!");
					}
				}
				//implementation is here so time for testing real part of contract
				testReflexivity();
				testSymetricaly();
				testTransitivity();
				testConsistency();
				testNullable();
				testHashCode();				
			}

			/**
			 * if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
			 */
			private void testTransitivity() throws InstantiationException, IllegalAccessException {
				LOGGER.info("Transitivity Test");
				T objectX = testObject.newInstance();
				T objectY = testObject.newInstance();
				T objectZ = testObject.newInstance();
				boolean resultOne = objectX.equals(objectY);
				boolean resultTwo = objectY.equals(objectZ);
				if(resultOne == resultTwo){
					Assert.assertEquals("if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.", 
							resultOne, objectX.equals(objectZ));
				}
			}

			/**
			 * equal objects must produce the same hash code
			 */
			private void testHashCode() throws InstantiationException, IllegalAccessException {
				LOGGER.info("Hashcode Test");
				T objectX = testObject.newInstance();
				T objectY = testObject.newInstance();
				boolean result = objectX.equals(objectY);
				if(result){
					Assert.assertTrue("equal objects must produce the same hash code", objectX.hashCode() == objectY.hashCode());
				} 
			}

			/**
			 * x.equals(null) should return false
			 */
			private void testNullable() {
				LOGGER.info("nullable Test");
				Assert.assertFalse("x.equals(null) should return false", testObject.equals(null));
				
			}

			/**
			 * multiple invocations of x.equals(y) consistently return true or consistently return false
			 */
			private void testConsistency() throws InstantiationException, IllegalAccessException {
				LOGGER.info("Consistency Test");
				T objectX = testObject.newInstance();
				T objectY = testObject.newInstance();
				boolean result = objectX.equals(objectY);
				Assert.assertEquals(result, objectX.equals(objectY));
				Assert.assertEquals(result, objectX.equals(objectY));
				Assert.assertEquals("multiple invocations of x.equals(y) consistently return true or consistently return false", result, objectX.equals(objectY));
				
			}

			/**
			 * x.equals(y) and y.equals(x) should give same result
			 */
			private void testSymetricaly() throws InstantiationException, IllegalAccessException {
				LOGGER.info("Symetricaly Test");
				T objectX = testObject.newInstance();
				T objectY = testObject.newInstance();
				boolean result = objectX.equals(objectY);
				Assert.assertEquals("x.equals(y) and y.equals(x) should give same result", result, objectX.equals(objectY));
				Assert.assertEquals("x.equals(y) and y.equals(x) should give same result", result, objectY.equals(objectX));
			}

			/**
			 * x.equals(x) should be true
			 */
			private void testReflexivity() throws InstantiationException, IllegalAccessException {
				LOGGER.info("Reflexivity Test");
				T object = testObject.newInstance();
				Assert.assertTrue("x.equals(x) should be true", object.equals(object));
				
			}
		};
	}
}
