  

package fun.asgc.neutrino.core.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
public class NumberUtil {

	/**
	 * 大小单位
	 */
	private static final List<String> SIZE_UINT = Lists.newArrayList("B", "K", "M", "G", "T");
	/**
	 * 单位容量
	 */
	private static final int UINT_CAPACITY = 1024;

	/**
	 * 大小转为描述
	 * @param size
	 * @return
	 */
	public static String sizeToDescription(long size, int decimals) {
		double num = size * 1.0;
		int unitIndex = 0;

		while (unitIndex < SIZE_UINT.size() - 1 && num >= UINT_CAPACITY) {
			num /= UINT_CAPACITY;
			unitIndex++;
		}

		return trimZero(String.format("%." + decimals + "f", num)) + SIZE_UINT.get(unitIndex);
	}

	/**
	 * 大小转为描述
	 * @param size
	 * @return
	 */
	public static String sizeToDescription(long size) {
		return sizeToDescription(size, 2);
	}

	public static long descriptionToSize(String description, long defaultValue) {
		try {
			description = description.trim().replaceAll(" ", "").toUpperCase();
			double num = Double.valueOf(description.substring(0, description.length() - 1));
			String unit = description.substring(description.length() - 1);
			int unitIndex = SIZE_UINT.indexOf(unit);
			if (unitIndex > 0) {
				while (unitIndex-- > 0) {
					num *= UINT_CAPACITY;
				}
			}
			return (long) num;
		} catch (Exception e) {
			// ignore
		}
		return defaultValue;
	}

	public static long descriptionToSize(String description) {
		return descriptionToSize(description, 0);
	}

	private static String trimZero(String s) {
		if (s.indexOf(".") > 0) {
			// 去掉多余的0
			s = s.replaceAll("0+?$", "");
			// 如最后一位是.则去掉
			s = s.replaceAll("[.]$", "");
		}
		return s;
	}

}
