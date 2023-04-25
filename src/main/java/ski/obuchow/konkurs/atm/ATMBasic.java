package ski.obuchow.konkurs.atm;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

public class ATMBasic{
	public final int region;
	public final int atmId;
		
	
	@CompiledJson
	public ATMBasic(int region, int atmId) {
		this.region = region;
		this.atmId = atmId;
	}
}
