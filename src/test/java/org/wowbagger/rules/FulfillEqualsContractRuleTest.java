package org.wowbagger.rules;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

class SampleObjectForEquals{
	String value;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SampleObjectForEquals other = (SampleObjectForEquals) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}

public class FulfillEqualsContractRuleTest {

	@ClassRule
	public static FulfillEqualsContractRule<SampleObjectForEquals> rule = new FulfillEqualsContractRule<SampleObjectForEquals>(new SampleObjectForEquals());
	
	@Test
	public void someTest() {
		Assert.assertTrue(true);
	}

}
