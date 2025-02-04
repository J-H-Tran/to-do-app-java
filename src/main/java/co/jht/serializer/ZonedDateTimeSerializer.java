package co.jht.serializer;

import co.jht.util.DateTimeFormatterUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;

public class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

    @Override
    public void serialize(
            ZonedDateTime value,
            JsonGenerator gen,
            SerializerProvider serializers
    ) throws IOException {
        gen.writeString(value.format(DateTimeFormatterUtil.getFormatter()));
    }
}