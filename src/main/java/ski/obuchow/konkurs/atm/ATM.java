package ski.obuchow.konkurs.atm;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

public class ATM extends ATMBasic implements Comparable<ATM> {
	@JsonAttribute(name = "requestType")
	public final Priority priority;
	
	public enum Priority {
		FAILURE_RESTART(0),
		PRIORITY(1),
		SIGNAL_LOW(2),
		STANDARD(3);

		public final int value;

		Priority(int value) {
			this.value = value;
		}
	}
		
	
	@CompiledJson
	public ATM(int region, int atmId, Priority priority) {
		super(region, atmId);
		this.priority = priority;
	}


	@Override
	public int compareTo(ATM other) {
		if (this.region != other.region)
			return this.region - other.region;
		return this.priority.value - other.priority.value;
	}
}