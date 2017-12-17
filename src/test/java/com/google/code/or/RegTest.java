package com.google.code.or;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest {
  public static void main(String[] args) {
	String string = "rows=[Rowcolumns=[1, c], Rowcolumns=[2, java], Rowcolumns=[3, ruby], Rowcolumns=[4, go], "
			+ "Rowcolumns=[5, python], Rowcolumns=[6, lua], Rowcolumns=[7, csharp], Rowcolumns=[8, ajax], Rowcolumns=[9, jsp]]";
//	Pattern pattern = Pattern.compile("row.*");
//	Matcher matcher = pattern.matcher(string);
//	if(matcher.find()){
//    System.out.println(matcher.group());
// }
	Pattern pattern2 = Pattern.compile("(?<=ns\\=\\[).*?(?=\\])");
	Matcher matcher2 = pattern2.matcher(string);
	while(matcher2.find()){
		System.out.println(matcher2.group(0));
	}

   }
}
