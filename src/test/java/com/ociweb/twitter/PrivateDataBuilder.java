package com.ociweb.twitter;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ociweb.pronghorn.util.BloomFilter;

public class PrivateDataBuilder {
    
    static String[] bookWords = new String[] {
            "Romance", "Kindle", "iBooks", "BookBoost", "BookBoost", "Kindle", "manuscript", "BookTweeter", "bestseller", "Shocking",
            "free book", "Novel", "buy my", "mystery", "my book", "Fiction", "NewRelease", "GOODREAD", "Science Fiction", "local news",
            "Book 1", "author", "Novel" , "Romance", "HORROR", "FollowTrick", "TeamFollowBack", "love story", "SciFi", "Fantasy", "Art",
            "Sci-Fi"
    };
    
    static String[] spanishWords = new String[] {
            //spanish filter
            "más", "él", "estar", "hacer","poder", "qué", "seguir", "haber", " para ", "como",
            "Noticias", "Estados", "Unidos", "el", "Mundo", "en", "Español", "tener", "poder",
            "humilde", "mas","aqui","meximalos", "Simposio","ser","por","años"
    };

    static String[] frenchWords = new String[] {
            //french filter
            "être", "être", "celui", "même", "pouvoir", "avec", "autre", "mettre", "très", "après", "de la",
            
            "sei","Hiç","της","μη","için","они",
            
            "oggi","andare","bello","brutto", //italian
            "Saya","Tidak","Satu", //maylay
            "Mais","uma","isto","Pouco","Muito" //portuguese
    };

    static String[] politicsWords = new String[] {
            "republican", "democrate", "gop", "left-wing", "right-wing", "trump", "poll", "bill",
            "libertarian", "monocracy", "recession", "sjw"
    };

    static String[] sportWords = new String[] {
            "football", "soccer", "basketball", "baseball", "archery", "climbing", "cycling", "bicycle",
            "skibob", "unicycle", "ufc", "karate", "fishing", "golf", "hockey", "skiing", "shooting",
            "gymnastics", "hunting", "sailing", "running", "rafting", "rowing", "kayaking", "canoeing"
    };

    static String[] crimeWords = new String[] {
            "shooting", "drugs", "death", "dead", "bombing", "police", "suspect", "terrorism", "mass",
            "injury", "jail", "prison", "jury", "judge", "stabbing", "shot", "officer", "kidnapping",
            "justice", "case", "abuse", "alcohol", "bail"
    };

    static String[] healthWords = new String[] {
            "cancer", "diet", "diabetes", "nutrition", "weight", "loss", "vitamins", "minerals", "exercise",
            "healthy", "set", "rep", "allergy", "anxiety", "cure", "depression", "doctor", "drug", "genes",
            "goal", "kinetic", "nurse", "pharmacy", "prevention", "strength", "stretching", "workout"
    };

    static String[] techWords = new String[] {
            "laptop", "phone", "video", "game", "password", "image", "gif", "email", "keybaord", "mouse",
            "media", "offline", "online", "radio", "search", "script", "geek", "facebook", "twitter", "instagram",
            "snapchat"
    };

    static String[] programmingWords = new String[] {
            "java", "c++", "cpp", "python", "static", "class", "programming", "ide", "compiler", "file", "editor",
            "swift", "c#", "comments", "javascript", "html", "css", "sql", "design"
    };
    
    public static void main(String[] args) {
        saveFilter(buildBloomFilter(bookWords, .00000000001), new File("bookWords.dat"));
        saveFilter(buildBloomFilter(spanishWords, .000000001), new File("spanishWords.dat"));
        saveFilter(buildBloomFilter(frenchWords, .000000001), new File("frenchWords.dat"));
        saveFilter(buildBloomFilter(politicsWords, .000000001), new File("politicsWords.dat"));
        saveFilter(buildBloomFilter(sportWords, .000000001), new File("sportWords.dat"));
        saveFilter(buildBloomFilter(crimeWords, .000000001), new File("crimeWords.dat"));
        saveFilter(buildBloomFilter(healthWords, .000000001), new File("healthWords.dat"));
        saveFilter(buildBloomFilter(techWords, .000000001), new File("techWords.dat"));
        saveFilter(buildBloomFilter(programmingWords, .000000001), new File("programmingWords.dat"));
    }

    public static BloomFilter buildBloomFilter(String[] strings,double failure) {
        BloomFilter localFilter = new BloomFilter(strings.length*12,failure);
        int j = strings.length;
        while( --j>=0 ) {
        	
        	//TODO: add value with different "white space" replacing the blank between the two words with
        	//      nothing, comma, hyphen, return, phantom white? 
        	
            //DO NOT ADD MORE THAN 12 FORMS SINCE WE ONLY MADE ROOM FOR 12 ABOVE.
            addValue(localFilter, strings[j].trim());  
            addValue(localFilter, strings[j].trim().toLowerCase());
            addValue(localFilter, strings[j].trim().toUpperCase());
            addValue(localFilter, "#"+strings[j].trim());  
            addValue(localFilter, "#"+strings[j].trim().toLowerCase());
            addValue(localFilter, "#"+strings[j].trim().toUpperCase()); 
            
            addValue(localFilter, strings[j].trim()+"s");  
            addValue(localFilter, (strings[j].trim()+"s").toLowerCase());
            addValue(localFilter, (strings[j].trim()+"s").toUpperCase());
            addValue(localFilter, "#"+strings[j].trim()+"s");  
            addValue(localFilter, ("#"+strings[j].trim()+"s").toLowerCase());
            addValue(localFilter, ("#"+strings[j].trim()+"s").toUpperCase());
            
        }
        return localFilter;
    }    
    
    private static void addValue(BloomFilter filter, String value) {
    	//add as simple char string
    	filter.addValue(value);
    	
    	//add as array of utf8 encoded bytes
    	byte[] asBytes = value.getBytes();
    	filter.addValue(asBytes,0, asBytes.length, Integer.MAX_VALUE);
    }
    
    
    
    private static void saveFilter(BloomFilter filter, File storage) {
        File backup = new File(storage.getAbsolutePath()+".bak");
        try {
            if (backup.exists()) {
                backup.delete();
            }
            storage.renameTo(backup);
            
            FileOutputStream fost = new FileOutputStream(storage);
            ObjectOutputStream oost = new ObjectOutputStream(fost);
            oost.writeObject(filter);
            oost.close();
            
            System.out.println(storage+" "+ storage.length());
            
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
    
    @Test
    public void testNewBloomfilter() {
    	
    	BloomFilter bf = new BloomFilter(365_000,.0000025); //one user only need to remember 1000 per day for 365 days
    	System.err.println("estimated size "+bf.estimatedSize());
    	//after 1 year we will rebuild the filter, so you can attempt to follow again once per year.
    	//2Mb per user for 1000 users is only 2G total.
    	
    	//never follow, never unfollow, should both be small lists
    	
    	
    	
    	
    	
    	
    	BloomFilter bloomFilter = buildBloomFilter(bookWords, .00000000001);
    	
    	
    	assertTrue(bloomFilter.mayContain("romance"));
    	assertFalse(bloomFilter.mayContain("computer"));
    	 	    	
		byte[] data = "romance".getBytes();
		assertTrue(bloomFilter.mayContain(data,0,data.length,Integer.MAX_VALUE));
    	
    }
    

    @Test
    public void testLoadedBloomfilter() {
    	
    	try {
			ObjectInputStream in = new ObjectInputStream(PrivateDataBuilder.class.getResourceAsStream("/bookWords.dat"));
			
			BloomFilter bloomFilter = (BloomFilter)in.readObject();
			in.close();
			
			assertTrue(bloomFilter.mayContain("romance"));
			assertFalse(bloomFilter.mayContain("computer"));
			
			byte[] data = "romance".getBytes();
			assertTrue(bloomFilter.mayContain(data,0,data.length,Integer.MAX_VALUE));
			
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	 	    	
    }
    
}
