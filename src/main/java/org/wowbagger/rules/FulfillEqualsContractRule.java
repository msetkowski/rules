package org.wowbagger.rules;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


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
 * Usage: Rule should be as ClassRule.
 * 
 * Example:
 * 
 * <pre>
 * 	&#064;ClassRule
 *	public static FulfillEqualsContractRule&lt;MyObject> rule = new FulfillEqualsContractRule&lt;MyObject>( new MyObject() );
 *	
 *	
 * </pre>
 *  
 * @see TestRule
 * @see Rule
 * 
 * @author setkomac
 * 
 * @param <T> Type of tests object
 * <br/> 

 */
public class FulfillEqualsContractRule<T> implements TestRule {

	private final static Logger LOGGER = Logger.getLogger(FulfillEqualsContractRule.class.getName());
	
	private T testObject;


	public FulfillEqualsContractRule(T object)  {
		testObject = object;		
	}
	
	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {				
				Method equals = null;
				try {
					equals = testObject.getClass().getDeclaredMethod("equals",
							new Object().getClass());
				} catch (NoSuchMethodException e) {
					base.evaluate(); // OK class might not implementing equals - we should proceed
				}
				if (equals != null) {
					// should implement also hashCode
					try {
						testObject.getClass().getDeclaredMethod("hashCode");
					} catch (NoSuchMethodException e) {

						throw new AssertionFailedError("Class "
								+ testObject.getClass().getName()
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
				
				base.evaluate();
			}

			/**
			 * if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
			 */
			@SuppressWarnings("unchecked")
			private void testTransitivity() throws Exception {
				LOGGER.info("Transitivity Test");
				T objectX = (T) Class.forName(testObject.getClass().getName()).newInstance();
				T objectY = (T) Class.forName(testObject.getClass().getName()).newInstance();
				T objectZ = (T) Class.forName(testObject.getClass().getName()).newInstance();
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
			@SuppressWarnings("unchecked")
			private void testHashCode() throws Exception {
				LOGGER.info("Hashcode Test");
				T objectX = (T) Class.forName(testObject.getClass().getName()).newInstance();
				T objectY = (T) Class.forName(testObject.getClass().getName()).newInstance();
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
			@SuppressWarnings("unchecked")
			private void testConsistency() throws Exception {
				LOGGER.info("Consistency Test");
				T objectX = (T) Class.forName(testObject.getClass().getName()).newInstance();
				T objectY = (T) Class.forName(testObject.getClass().getName()).newInstance();
				boolean result = objectX.equals(objectY);
				Assert.assertEquals(result, objectX.equals(objectY));
				Assert.assertEquals(result, objectX.equals(objectY));
				Assert.assertEquals("multiple invocations of x.equals(y) consistently return true or consistently return false", result, objectX.equals(objectY));
				
			}

			/**
			 * x.equals(y) and y.equals(x) should give same result
			 */
			@SuppressWarnings("unchecked")
			private void testSymetricaly() throws Exception {
				LOGGER.info("Symetricaly Test");
				T objectX = (T) Class.forName(testObject.getClass().getName()).newInstance();
				T objectY = (T) Class.forName(testObject.getClass().getName()).newInstance();
				boolean result = objectX.equals(objectY);
				Assert.assertEquals("x.equals(y) and y.equals(x) should give same result", result, objectX.equals(objectY));
				Assert.assertEquals("x.equals(y) and y.equals(x) should give same result", result, objectY.equals(objectX));
			}

			/**
			 * x.equals(x) should be true
			 * @throws NoSuchMethodException 
			 * @throws InvocationTargetException 
			 * @throws SecurityException 
			 * @throws IllegalArgumentException 
			 */
			@SuppressWarnings("unchecked")
			private void testReflexivity() throws Exception {
				LOGGER.info("Reflexivity Test");
				T object = (T) Class.forName(testObject.getClass().getName()).newInstance(); 
				Assert.assertTrue("x.equals(x) should be true", object.equals(object));
				
			}
		};
	}
}
