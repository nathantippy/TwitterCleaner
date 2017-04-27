package com.ociweb.twitter;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ociweb.pronghorn.pipe.util.build.FROMValidation;
import com.ociweb.twitter.schema.HoseBirdSubscriptionSchema;
import com.ociweb.twitter.schema.TwitterEventSchema;
import com.ociweb.twitter.schema.TwitterStreamControlSchema;

public class SchemasTest {

    
    @Test
    public void testEventsFROMMatchesXML() {
        assertTrue(FROMValidation.checkSchema("/com/ociweb/twitter/pronghorn/TwitterEvent.xml", TwitterEventSchema.class));
    }

    @Test
    public void testHoseBirdFROMMatchesXML() {
        assertTrue(FROMValidation.checkSchema("/com/ociweb/twitter/pronghorn/HoseBirdSubscription.xml", HoseBirdSubscriptionSchema.class));
    }
    
    @Test
    public void testTwitterUserStreamControlSchemaFROMMatchesXML() {
        assertTrue(FROMValidation.checkSchema("/com/ociweb/twitter/pronghorn/TwitterUserStreamControl.xml", TwitterStreamControlSchema.class));
    }
    
}
