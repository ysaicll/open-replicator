package com.google.code.or;

/**
 * @author ys
 * @usage This class contains some useful tool methods
 */
public class Toolmethod {
	
	/**
	 * @param input
	 * @return String with Apostrophe
	 */
	public static String addApos(String input) {
		String apos = "'";
		return apos + input + apos;
	}

    /**
     * @param input
     * @return String with Parentheses
     */
    public static String addPar(String input) {
		String lp = "(";
		String rp = ")";
		return lp + input + rp;
	}
}
