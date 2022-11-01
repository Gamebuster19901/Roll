package com.gamebuster19901.roll.util.pdf;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFText {
	
	private static final Pattern NAME_PATTERN = Pattern.compile("/T\\((?<name>.*)\\)\\n");
	private static final Pattern VALUE_PATTERN	= Pattern.compile("/V\\((?<val>.*)\\)\\n");
	private static final Pattern HEX_VALUE_PATTERN = Pattern.compile("/V\\<(?<val>.*)\\>\\n");

	String name;
	String value;
	
	public PDFText(String text) {
		try {
			Matcher nameMatcher = NAME_PATTERN.matcher(text);
			Matcher valueMatcher = VALUE_PATTERN.matcher(text);
			if(nameMatcher.find()) {
				this.name = nameMatcher.group("name");
			}
			else {
				throw new IllegalArgumentException("No name found for PDFText! " + text);
			}
			
			if(valueMatcher.find()) {
				this.value = valueMatcher.group("val");
			}
			else {
				valueMatcher = HEX_VALUE_PATTERN.matcher(text);
				if(valueMatcher.find()) {
					String val = valueMatcher.group("val");
					ByteBuffer buff = ByteBuffer.allocate(val.length() / 2);
					for(int i = 0; i < val.length(); i+=2) {
						buff.put((byte)Integer.parseInt(val.substring(i, i+2), 16));
					}
					buff.rewind();
					Charset cs = StandardCharsets.UTF_16;
					value = cs.decode(buff).toString();
				}
				else {
					this.value = "";
				}
			}
		}
		catch(Throwable t) {
			System.out.println(text + " caused exception");
			t.printStackTrace();
			throw t;
		}
	}
	
	public String toString() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
}
