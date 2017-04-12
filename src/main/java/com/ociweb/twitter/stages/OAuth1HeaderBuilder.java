package com.ociweb.twitter.stages;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpHeaders;  //TODO: remove with TrieParser
import org.apache.http.NameValuePair;  //TODO: remove with TrieParser
import org.apache.http.client.utils.URLEncodedUtils;  //TODO: remove with TrieParser

import com.google.common.base.Charsets;  //TODO: remove with TrieParser
import com.ociweb.pronghorn.util.Appendables;
import com.twitter.joauth.OAuthParams;
import com.twitter.joauth.Request;
import com.twitter.joauth.Request.Pair;
import com.twitter.joauth.Signer;
import com.twitter.joauth.UrlCodec;

public class OAuth1HeaderBuilder {

  private final Signer signer;

  private final String consumerKey;
  private final String consumerSecret;
  private final String token;
  private final String tokenSecret;

  private final SecureRandom secureRandom;

  public OAuth1HeaderBuilder(String consumerKey, String consumerSecret, String token, String tokenSecret) {
    this.consumerKey = consumerKey;
    this.consumerSecret = consumerSecret;
    
    this.token = token;
    this.tokenSecret = tokenSecret;

    assert(consumerKey!=null);
    assert(consumerSecret!=null);
    assert(token!=null);
    assert(tokenSecret!=null);
    
    this.signer = Signer.getStandardSigner();

    this.secureRandom = new SecureRandom();
  }

  
  public void addHeaders(StringBuilder builder, String rawQuery, int port, String scheme, String upperVerb, String host, String path) {

	    List<NameValuePair> httpGetParams = URLEncodedUtils.parse(rawQuery, Charsets.UTF_8);
	     
	    
	    List<Pair> javaParams = new ArrayList<Pair>(httpGetParams.size());
	    for (NameValuePair params : httpGetParams) {
	      Pair tuple = new Pair(UrlCodec.encode(params.getName()), UrlCodec.encode(params.getValue()));
	      javaParams.add(tuple);
	    }

	    long timestampSecs = generateTimestamp();
	    String nonce = generateNonce();
	    
	    OAuthParams.OAuth1Params oAuth1Params = new OAuthParams.OAuth1Params(
	    	      token, consumerKey, nonce, timestampSecs, Long.toString(timestampSecs), "",
	    	      OAuthParams.HMAC_SHA1, OAuthParams.ONE_DOT_OH
	    	    );
	    
		// We only need the stringbuilder for the duration of this method
		  StringBuilder paramsBuilder = new StringBuilder(512);
		
		  // first, concatenate the params and the oAuth1Params together.
		  // the parameters are already URLEncoded, so we leave them alone
		  ArrayList<Pair> sigParams = new ArrayList<Request.Pair>();
		  sigParams.addAll(javaParams);
		  sigParams.addAll(oAuth1Params.toList(false));
		
		  Collections.sort(sigParams, new Comparator<Request.Pair>() {
		    @Override
		    public int compare(Request.Pair thisPair, Request.Pair thatPair) {
		      // sort params first by key, then by value
		      int keyCompare = thisPair.key.compareTo(thatPair.key);
		      if (keyCompare == 0) {
		        return thisPair.value.compareTo(thatPair.value);
		      } else {
		        return keyCompare;
		      }
		    }
		  });
		
		  if (!sigParams.isEmpty()) {
		    Request.Pair head = sigParams.get(0);
		    paramsBuilder.append(head.key).append('=').append(head.value);
		    
		    for (int i=1; i<sigParams.size(); i++) {
		      Request.Pair pair = sigParams.get(i);
		      paramsBuilder.append('&').append(pair.key).append('=').append(pair.value);
		    }
		  }
		
		  StringBuilder requestUrlBuilder = new StringBuilder(512);
		  requestUrlBuilder.append(scheme.toLowerCase());
		  requestUrlBuilder.append("://");
		  requestUrlBuilder.append(host.toLowerCase());
		  if (includePortString(port, scheme)) {
		    requestUrlBuilder.append(":").append(port);
		  }
		  requestUrlBuilder.append(path);
		
		  StringBuilder normalizedBuilder = new StringBuilder(512);
		
		  normalizedBuilder.append(upperVerb.toUpperCase());
		  normalizedBuilder.append('&').append(UrlCodec.encode(requestUrlBuilder.toString()));
		  normalizedBuilder.append('&').append(UrlCodec.encode(paramsBuilder.toString()));
	    
		
		String normalized = normalizedBuilder.toString();
	  
		//
	 //   System.out.println(normalized);
	 //   System.out.println("FAAST EXIT");
	 //  System.exit(-1);
	  
	    
	    String signature;
	    try {
	      signature = signer.getString(normalized, tokenSecret, consumerSecret);
	    } catch (InvalidKeyException e) {
	      throw new RuntimeException(e);
	    } catch (NoSuchAlgorithmException e) {
	      throw new RuntimeException(e);
	    }
	  
	    
	    builder.append(HttpHeaders.AUTHORIZATION).append(": ");
	    

	    builder.append("OAuth ");
	    builder.append(OAuthParams.OAUTH_CONSUMER_KEY).append("=\"").append((consumerKey)).append("\", ");
	    builder.append(OAuthParams.OAUTH_TOKEN).append("=\"").append((token)).append("\", ");
	    
	    builder.append(OAuthParams.OAUTH_SIGNATURE).append("=\"").append((signature)).append("\", ");
	    builder.append(OAuthParams.OAUTH_SIGNATURE_METHOD).append("=\"").append((OAuthParams.HMAC_SHA1)).append("\", ");
	    builder.append(OAuthParams.OAUTH_TIMESTAMP).append('=');
	    
	    Appendables.appendValue(builder, "\"", timestampSecs, "\", ");
	    
	    builder.append(OAuthParams.OAUTH_NONCE).append("=\"").append((nonce)).append("\", ");
	    builder.append(OAuthParams.OAUTH_VERSION).append("=\"").append((OAuthParams.ONE_DOT_OH)).append("\"");
  
	  
  }
  


  private long generateTimestamp() {
    long timestamp = System.currentTimeMillis();
    return timestamp / 1000;
  }

  private String generateNonce() {
    return Long.toString(Math.abs(secureRandom.nextLong())) + System.currentTimeMillis();
  }

  /**
	     * The OAuth 1.0a spec says that the port should not be included in the normalized string
	     * when (1) it is port 80 and the scheme is HTTP or (2) it is port 443 and the scheme is HTTPS
	     */
	    boolean includePortString(int port, String scheme) {
	      return !((port == 80 && "HTTP".equalsIgnoreCase(scheme)) || (port == 443 && "HTTPS".equalsIgnoreCase(scheme)));
	    }
	  

}

