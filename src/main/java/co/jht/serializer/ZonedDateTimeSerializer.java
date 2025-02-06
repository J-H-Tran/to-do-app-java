package co.jht.serializer;

import co.jht.util.DateTimeFormatterUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static co.jht.constants.ApplicationConstants.ASIA_TOKYO;

public class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatterUtil.getFormatter();

    public ZonedDateTimeSerializer() {
        this(null);
    }

    public ZonedDateTimeSerializer(Class<ZonedDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(
            ZonedDateTime value,
            JsonGenerator gen,
            SerializerProvider serializers
    ) throws IOException {
        ZonedDateTime tokyoTime = value.withZoneSameInstant(ZoneId.of(ASIA_TOKYO));
        gen.writeString(tokyoTime.format(formatter));
    }
}