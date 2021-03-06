package repast.yiyou.common.util;

/**
 * 对数据进行MD5加密 <br>
 */
import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Md5 {

	private static final Logger logger = LoggerFactory.getLogger(Md5.class);

	public Md5() {
	}

	public static String getMD5(String sourceStr) {
		logger.debug("正在对数据进行加密,现返回加密值...");
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = sourceStr.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高
										// 4
										// 位的数字转换,
										// >>>
										// 为逻辑右移，将符号位一起右移

				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4
									// 位的数字转换
			}
			return new String(str);
		} catch (Exception e) {
			logger.info("对数据加密失败...");
			return null;
		}
	}
	
	/**
	 * 加密成固定长度的字符串
	 * */
	public static String getFixMD5(String sourceStr,int len) {
		//logger.debug("正在对数据进行加密,现返回加密值...");
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'A', 'B', 'C', 'D', 'F', 'G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
		try {
			byte[] strTemp = sourceStr.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = len;
			char str[] = new char[len * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高
										// 4
										// 位的数字转换,
										// >>>
										// 为逻辑右移，将符号位一起右移

				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4
									// 位的数字转换
			}
			return new String(str);
		} catch (Exception e) {
			logger.info("对数据加密失败...");
			return null;
		}
	}

	public static void main(String[] args) throws Exception{
		System.out.println(getMD5("123456"));
	}

}
