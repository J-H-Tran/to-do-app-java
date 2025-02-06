package co.jht.serializer;

import co.jht.util.DateTimeFormatterUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static co.jht.constants.ApplicationConstants.ASIA_TOKYO;

public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(p.getText(), DateTimeFormatterUtil.getFormatter());
        return zonedDateTime.withZoneSameInstant(ZoneId.of(ASIA_TOKYO));
    }
}