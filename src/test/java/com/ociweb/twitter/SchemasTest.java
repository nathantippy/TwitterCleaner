package com.ociweb.twitter;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ociweb.pronghorn.network.schema.TwitterEventSchema;
import com.ociweb.pronghorn.network.schema.TwitterStreamControlSchema;
import com.ociweb.pronghorn.pipe.util.build.FROMValidation;

public class SchemasTest {

    
    @Test
    public void testEventsFROMMatchesXML() {
        assertTrue(FROMValidation.checkSchema("/com/ociweb/twitter/pronghorn/TwitterEvent.xml", TwitterEventSchema.class));
    }

    @Test
    public void testTwitterUserStreamControlSchemaFROMMatchesXML() {
        assertTrue(FROMValidation.checkSchema("/com/ociweb/twitter/pronghorn/TwitterUserStreamControl.xml", TwitterStreamControlSchema.class));
    }
    
}
