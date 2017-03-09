package com.ociweb.twitter.stages.text;

import com.ociweb.pronghorn.util.TrieParser;
import com.ociweb.pronghorn.util.TrieParserReader;

public class TextContentSplitter {
	
	 static TrieParser parser = extractWordPatterns(new TrieParser(2048));
    
	 
	 public static TrieParserReader reader() {
		 return new TrieParserReader(2, 0, 2048);
	 }
	 

     static TrieParser extractWordPatterns(TrieParser trie) {
        
        final int ignore = 1;
        final int word   = 2;

        trie.setUTF8Value("#",  ignore);  //Ignores
        trie.setUTF8Value(":",  ignore);  //Ignores
        trie.setUTF8Value(";",  ignore);  //Ignores
        trie.setUTF8Value(",",  ignore);  //Ignores
        trie.setUTF8Value("!",  ignore);  //Ignores
        trie.setUTF8Value("?",  ignore);  //Ignores
        trie.setUTF8Value("\\", ignore);  //Ignores
        trie.setUTF8Value("/",  ignore);  //Ignores
        trie.setUTF8Value(" ",  ignore);  //Ignores
        trie.setUTF8Value("\"", ignore);  //Ignores
        trie.setUTF8Value(" ",  ignore);  //Ignores
        trie.setUTF8Value("'",  ignore);  //Ignores
        trie.setUTF8Value("&",  ignore);  //Ignores
        trie.setUTF8Value("-",  ignore);  //Ignores
        trie.setUTF8Value("+",  ignore);  //Ignores
        trie.setUTF8Value("|",  ignore);  //Ignores
        trie.setUTF8Value(">",  ignore);  //Ignores
        trie.setUTF8Value("_",  ignore);  //Ignores
        trie.setUTF8Value("^",  ignore);  //Ignores
        trie.setUTF8Value(".",  ignore);  //Ignores
        trie.setUTF8Value(")",  ignore);  //Ignores
        trie.setUTF8Value("<",  ignore);  //Ignores
        trie.setUTF8Value("[",  ignore);  //Ignores
        trie.setUTF8Value("]",  ignore);  //Ignores
        trie.setUTF8Value("$",  ignore);  //Ignores
        trie.setUTF8Value("~",  ignore);  //Ignores
        
        //DO NOT ADD : OR // SINCE THAT WILL BLOCK URLS
        trie.setUTF8Value("%b?",  word); //new word
        trie.setUTF8Value("%b\"", word); //new word
        trie.setUTF8Value("%b ",  word); //new word
        trie.setUTF8Value("%b.",  word); //new word
        trie.setUTF8Value("%b,",  word); //new word
        trie.setUTF8Value("%b!",  word); //new word
        trie.setUTF8Value("%b:",  word); //new word        
        trie.setUTF8Value("%b(",  word); //new word
        trie.setUTF8Value("%b)",  word); //new word
        trie.setUTF8Value("%b+",  word); //new word
        trie.setUTF8Value("%b-",  word); //new word
        trie.setUTF8Value("%b_",  word); //new word
        trie.setUTF8Value("%b[",  word); //new word
        trie.setUTF8Value("%b]",  word); //new word
        trie.setUTF8Value("%b{",  word); //new word
        trie.setUTF8Value("%b}",  word); //new word
        
        trie.setUTF8Value("https://%b " ,4);//URL 
        trie.setUTF8Value("http://%b " ,3);//URL 
        
        return trie;
    }
	
}
