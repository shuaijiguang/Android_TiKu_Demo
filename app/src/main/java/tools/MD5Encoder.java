/**
 *  ClassName: MD5Encoder.java
 *  created on 2010-6-4
 *  Copyrights 2010 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package tools;

import java.security.MessageDigest;

/**
 * MD5加密工具类
 * @author qiujy
 */
public class MD5Encoder {
	/**
	 * 使用MD5算法加密字符串
	 * @param 源字符串
	 * @return 加密后的字符串
	 */
	public static String encode(final String src) {
		String dest = new String(src);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			//进行加密,获取字节数组
			byte[] b = md.digest(dest.getBytes());
			dest = byte2hexString(b);
		} catch (Exception ex) {
		}
		return dest;
	}

	/**
	 * 方法说明:把字节数组转换成字符串.
	 * @param bytes
	 * @return 字符串
	 */
	protected static final String byte2hexString(byte[] bytes) {
		StringBuilder buf = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Integer.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}
}
