package com.framework.socket.util;

import java.util.regex.Pattern;

public class StandardUtil {
	
	public static final String PWD_LEGAL = "ok";
	
	public static String stdPassword(String pwd){
		if(pwd == null || "".equals(pwd.trim())){
			return "输入不合法";
		}
		if(pwd.indexOf(" ")!=-1){
			return "输入不合法";
		}
		Pattern p = Pattern.compile("^[0-9]*\\d$");
		int size = pwd.length();
		if(size < 6 || size > 15 ){
			if(size < 6){
				return "输入不能小于六位";
			}
			return "输入不能超过15位";
		}
		if(p.matcher(pwd).find()){
			return "输入不能全数字";
		}
		p = Pattern.compile("^[a-zA-Z]*$");
		if(p.matcher(pwd).find()){
			return "输入不能全字符";
		}
		//检验中文
		String chinese = "[\u0391-\uFFE5]";
		for (int i = 0; i < pwd.length(); i++) {
			String temp = pwd.substring(i, i + 1);
			if (temp.matches(chinese)) {
				return "不能包含中文";
			} else {
				continue;
			}
		}
//		p = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
//		if(p.matcher(str).find()){
//			DebugLog.e("erro","不能有特殊字符");
//			return RESULT_OF_HAVESPECIAL;
//		}
		return PWD_LEGAL;
	}
}
