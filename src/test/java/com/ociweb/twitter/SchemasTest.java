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
        assertTrue(FROMValidation.testForMatchingFROMs("/com/ociweb/twitter/pronghorn/TwitterEvent.xml", TwitterEventSchema.instance));
    };
    
    @Test
    public void testEventsConstantFields() { //too many unused constants.
        assertTrue(FROMValidation.testForMatchingLocators(TwitterEventSchema.instance));
    }
    
    
    @Test
    public void testHoseBirdFROMMatchesXML() {
        assertTrue(FROMValidation.testForMatchingFROMs("/com/ociweb/twitter/pronghorn/HoseBirdSubscription.xml", HoseBirdSubscriptionSchema.instance));
    };
    
    @Test
    public void testHoseBirdConstantFields() { //too many unused constants.
        assertTrue(FROMValidation.testForMatchingLocators(HoseBirdSubscriptionSchema.instance));
    }

    
    @Test
    public void testTwitterUserStreamControlSchemaFROMMatchesXML() {
        assertTrue(FROMValidation.testForMatchingFROMs("/com/ociweb/twitter/pronghorn/TwitterUserStreamControl.xml", TwitterStreamControlSchema.instance));
    };
    
    @Test
    public void testTwitterUserStreamControlSchemaConstantFields() { //too many unused constants.
        assertTrue(FROMValidation.testForMatchingLocators(TwitterStreamControlSchema.instance));
    }
    
    
}
