
package com.nebhale.cyclinglibrary.web.json;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@Component
final class LinkJsonSerializer extends StdSerializer<Link> {

    public LinkJsonSerializer() {
        super(Link.class);
    }

    @Override
    public void serialize(Link value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeStringField("rel", value.getRel());
        jgen.writeStringField("href", value.getHref());
        jgen.writeEndObject();
    }

}
