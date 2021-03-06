package com.sirbizio.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Stream utils
 * @author fabrizio
 *
 */
public final class StreamUtils {

	private StreamUtils(){}
	
	/**
	 * Closes a closable ignoring any exceptions
	 * @param c the closable object
	 */
	public static void closeQuietly(Closeable c) {
		try {
			if(c != null) c.close();
		} catch (IOException ignored) {
		}
	}
	
}
