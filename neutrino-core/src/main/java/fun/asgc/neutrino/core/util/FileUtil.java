  

package fun.asgc.neutrino.core.util;

import fun.asgc.neutrino.core.constant.MetaDataConstant;
import fun.asgc.neutrino.core.exception.InternalException;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.stream.Collectors;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
public class FileUtil {

	/**
	 * 根据文件路径获取输入流
	 * @param path
	 * @return
	 */
	public static InputStream getInputStream(String path) throws FileNotFoundException {
		if (StringUtils.isEmpty(path)) {
			throw new NullPointerException("path 不能为空!");
		}
		if (path.startsWith(MetaDataConstant.CLASSPATH_RESOURCE_IDENTIFIER)) {
			String subPath = path.substring(MetaDataConstant.CLASSPATH_RESOURCE_IDENTIFIER.length());
			return FileUtil.class.getResourceAsStream(subPath);
		}

		return new FileInputStream(path);
	}

	/**
	 * 根据文件路径获取输出流
	 * @param path
	 * @return
	 */
	public static OutputStream getOutputStream(String path) throws FileNotFoundException {
		if (StringUtils.isEmpty(path)) {
			throw new NullPointerException("path 不能为空!");
		}
		if (path.startsWith(MetaDataConstant.CLASSPATH_RESOURCE_IDENTIFIER)) {
			throw new InternalException("不支持路径以'" + MetaDataConstant.CLASSPATH_IDENTIFIER + "'开头");
		}
		return new FileOutputStream(path);
	}

	/**
	 * 读取文件内容
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String readContentAsString(String path) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(getInputStream(path)))){
			return br.lines().collect(Collectors.joining("\n"));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 读取字节内容
	 * @param path
	 * @return
	 */
	public static byte[] readBytes(String path) {
		try (InputStream in = getInputStream(path)){
			byte[] bytes = new byte[1024];
			int length = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((length = in.read(bytes)) != -1) {
				baos.write(bytes, 0, length);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}

	public static void write(String path, String content) {
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getOutputStream(path)))){
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File getDirectory(String path) {
		if (path.endsWith("/./")) {
			path = path.substring(0, path.length() - 2);
		}
		File file = new File(path);
		if (file.isDirectory() || path.endsWith("/")) {
			return file;
		}
		return file.getParentFile();
	}

	public static void makeDirs(String path) {
		try {
			File file = getDirectory(path);
			if (null != file && !file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			//  ignore
		}
	}

	public static boolean deleteDirs(String path) {
		File file = getDirectory(path);
		try {
			FileDeleteStrategy.FORCE.delete(file);
		} catch (Exception e) {
			file = getDirectory(file.getPath());
			try {
				FileDeleteStrategy.FORCE.delete(file);
			} catch (IOException ex) {
				return false;
			}
		}
		return true;
	}

}
