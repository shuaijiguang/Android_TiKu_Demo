
package tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IOHelper {
	
	/**
	 * 从网络路径中获取资源名称
	 * @param url
	 * @return
	 */
	public static String getName(String url){
		String result = null;
		if(null != url){
			result = url.substring(url.lastIndexOf("/") + 1);
		}
		return result;
	}
	
	/**
	 * 获取指定文件名的扩展名
	 * @param name
	 * @return
	 */
	public static String getExtension(String name){
		return name.substring(name.lastIndexOf(".") + 1);
	}
	
	/**
	 * 完成IO流的拷贝功能
	 * @param is 输入流--->数据来源
	 * @param os 输出流 ---> 数据的存储目标
	 */
	public static void copy(InputStream is, OutputStream os){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		byte[] b = new byte[2048];
		try{
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(os);
		
			for(int count = 0; (count = bis.read(b))!= -1;){
				bos.write(b, 0, count);
			}
			bos.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 文件复制功能
	 * @param src 源文件
	 * @param dest 目标文件
	 */
	public static void copy(File src, File dest){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		
		byte[] b = new byte[2048];
		
		try {
			bis = new BufferedInputStream(new FileInputStream(src));
			bos = new BufferedOutputStream(new FileOutputStream(dest));
			
			for(int count = -1; (count = bis.read(b)) != -1;){
				bos.write(b, 0, count);
			}
			bos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null!= bis){
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null != bos){
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 统计指定目录下的子文件的大小（字节数），包括子孙目录下的所有文件
	 * @param baseDir
	 * @return
	 */
	public static long totalFileSize(File baseDir){
		long size = 0; 
		if ((baseDir != null) && (baseDir.isDirectory())) {
			File[] subs = baseDir.listFiles();
			int length = subs == null ? 0 : subs.length;
			for(int i = 0; i < length; i++){
				File sub = subs[i];
				if(sub.isFile()){
					size += sub.length();
				}else{
					size += totalFileSize(sub);
				}
			}
		}
		return size;
	}
	
	/**
	 * 刪除指定目录下的所有文件，包括子孙目录下的文件，并返回被删除的文件数量
	 * @param baseDir
	 * @return 被删除的文件数量
	 */
	public static int clearFolder(File baseDir) {
		int count = 0;
		if ((baseDir != null) && (baseDir.isDirectory())) {
			File[] subs = baseDir.listFiles();
			int length = subs == null ? 0 : subs.length;
			for(int i = 0; i < length; i++){
				File sub = subs[i];
				if(sub.isFile()){
					if(sub.delete()){
						count++;
					}
				}else{
					count += clearFolder(sub);
				}
			}
		}
		return count;
	}
	
	/**
	 * 下载文件并写入outputStream
	 * 
	 * @param urlString
	 * @param outputStream
	 * @return
	 */
	public boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
			out = new BufferedOutputStream(outputStream, 8 * 1024);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 将字符串写入缓存
	 * 
	 * @param txt
	 * @param outputStream
	 * @return
	 */
	public static boolean stringToStream(String txt, OutputStream outputStream) {
		try {
			outputStream.write(txt.getBytes());
			outputStream.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
