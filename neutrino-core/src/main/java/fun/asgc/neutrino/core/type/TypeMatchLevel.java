  
package fun.asgc.neutrino.core.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类型匹配等级
 * 用于表示类型匹配的不同等级，便于在程序中进行类型匹配时选择合适的策略
 * @author: chenjunlin
 * @date: 2024/4/29
 */
@Getter
@AllArgsConstructor
public enum TypeMatchLevel {
	// 当且仅当匹配类为空（即匹配对象为null没有类型）时
	NULL(0, 0, 0, "空匹配"),
	// 当且仅当匹配类与目标类完全相等
	PERFECT(1, 1, 1, "完美匹配"),
	// 当且仅当匹配类为严格基本数据类型，且目标类为对应的包装类型
	PACKING(2, 100, 100, "装箱匹配"),
	// 当且仅当目标类为严格基本数据类型，且匹配类为对应的包装类型
	UNPACKING(3, 200,200,"拆箱匹配"),
	// 当且仅当匹配类和目标类均为一般基本数据类型（非严格），且目标类型的范围更加宽泛时
	ASCENSION(4, 1000, 2000, "类型提升匹配"),
	// 超类匹配，当且仅当目标类是匹配类的超类时
	SUPER(5, 1000000, 2000000, "超类匹配"),
	// 内置的扩展匹配
	EXTENSION(6, 10000000, 50000000, "扩展匹配"),
	// 自定义匹配
	CUSTOM(7, 100000000, 500000000, "自定义匹配"),
	// 不匹配
	NOT(8, Integer.MAX_VALUE, Integer.MAX_VALUE, "不匹配");

	/**
	 * 将匹配级别和对应的枚举常量存储起来
	 * 方便通过匹配级别获取对应的枚举常量
	 */
	private static Map<Integer,TypeMatchLevel> levelMap = Stream.of(TypeMatchLevel.values()).collect(Collectors.toMap(TypeMatchLevel::getLevel, Function.identity()));

	/**
	 * 匹配级别
	 */
	private int level;
	/**
	 * 类型距离最小值
	 */
	private int distanceMin;
	/**
	 * 类型距离最大值
	 */
	private int distanceMax;
	/**
	 * 描述
	 */
	private String desc;

	/**
	 * 据匹配级别返回对应的枚举常量。在 levelMap 中查找并返回匹配级别对应的枚举常量
	 * @param level
	 * @return
	 */
	public static TypeMatchLevel byLevel(int level) {
		return levelMap.get(level);
	}

	/**
	 * 根据类型距离返回对应的枚举常量。
	 * 遍历所有枚举常量，找到类型距离在对应范围内的枚举常量并返回
	 * @param typeDistance
	 * @return
	 */
	public static TypeMatchLevel byTypeDistance(int typeDistance) {
		for (TypeMatchLevel item : values()) {
			if (typeDistance >= item.getDistanceMin() && typeDistance <= item.getDistanceMax()) {
				return item;
			}
		}
		return null;
	}
}
