package com.gamebuster19901.excite.bot.game.character;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gamebuster19901.excite.util.pdf.PDFText;

public class DNDBeyondPDFPlayerBuilder extends PlayerBuilder {

	public static final Pattern PDF_OBJECT_PATTERN = Pattern.compile("\\d* \\d* obj.*?endobj", Pattern.DOTALL);
	
	public DNDBeyondPDFPlayerBuilder(String charSheet) {
		try {
			if(!charSheet.startsWith("https://")) {
				charSheet = charSheet + "https://";
			}
			URL url = new URL(charSheet);
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			Matcher matcher = PDF_OBJECT_PATTERN.matcher(new String(is.readAllBytes()));
			
			int count = 0;
			while(matcher.find()) {
				count++;
				String result = matcher.toMatchResult().group();
				if(result.contains("\n/FT/Tx\n")) {
					PDFText text = new PDFText(result);
					System.out.println(text.getName() + ": " + text);
				}
			}
			System.out.println(count + " matches");
		}
		catch(IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
