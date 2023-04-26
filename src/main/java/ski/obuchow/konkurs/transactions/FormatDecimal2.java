package ski.obuchow.konkurs.transactions;

import java.io.IOException;
import java.math.BigDecimal;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.NumberConverter;

public abstract class FormatDecimal2 {
	public static BigDecimal read(JsonReader reader) throws IOException {
		if (reader.wasNull()) return null;
		return NumberConverter.deserializeDecimal(reader).setScale(2);
	}
	public static void write(JsonWriter writer, BigDecimal value) {
		if (value == null) {
			writer.writeNull();
		} else {
			NumberConverter.serializeNullable(value.setScale(2), writer);
		}
	}
}