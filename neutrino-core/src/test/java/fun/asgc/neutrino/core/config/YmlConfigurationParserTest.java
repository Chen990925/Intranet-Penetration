/**
 * Copyright (c) 2022 aoshiguchen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fun.asgc.neutrino.core.config;

import com.alibaba.fastjson.JSONObject;
import fun.asgc.neutrino.core.annotation.Configuration;
import fun.asgc.neutrino.core.annotation.Value;
import fun.asgc.neutrino.core.config.impl.YmlConfigurationParser;
import fun.asgc.neutrino.core.util.FileUtil;
import lombok.Data;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author: aoshiguchen
 * @date: 2022/6/16
 */
public class YmlConfigurationParserTest {

	/**
	 * 指定test1.yml地址，将test1.yml的内容读成一个Config1
	 * @throws FileNotFoundException
	 */
	@Test
	public void test1() throws FileNotFoundException {
		ConfigurationParser parser = new YmlConfigurationParser();
		Config1 config1 = parser.parse(FileUtil.getInputStream("classpath:/test1.yml"), Config1.class);
		System.out.println(JSONObject.toJSONString(config1));
	}

	/**
	 * 指定@Configuration注解 将test1.yml的内容读成一个Config1
	 * @throws FileNotFoundException
	 */
	@Test
	public void test2() throws FileNotFoundException {
		ConfigurationParser parser = new YmlConfigurationParser();
		Config1 config1 = parser.parse(Config1.class);
		System.out.println(JSONObject.toJSONString(config1));
	}

	/**
	 * 对@Configuration指定的文件进行属性过滤，只将test1.xml下的c.d的内容读出来然后转成Config2返回
	 * @throws FileNotFoundException
	 */
	@Test
	public void test3() throws FileNotFoundException {
		ConfigurationParser parser = new YmlConfigurationParser();
		Config2 config2 = parser.parse(Config2.class);
		System.out.println(JSONObject.toJSONString(config2));
	}

	@Configuration(file = "test1.yml")
	@Data
	public static class Config1 {
		@Value("server")
		private Server serve;
		private Integer a;
		private String b;
		private C c;
		@Value("server")
		private Object server;
		@Value("c")
		private Object cc;

		@Data
		public static class Server {
			private String name;
			private int port;
		}

		@Data
		public static class C {
			private D d;
			public static class D {
				private int e;
				private Long f;
				private String g;
				private List<String> h;
				@Value("f")
				private Integer ff;
				@Value("h")
				private Set<String> hh;
				@Value("h")
				private String[] hhh;
				private Integer[] i;
				@Value("i")
				private List<Integer> ii;
				@Value("i")
				private Object iii;
			}
		}
	}

	@Configuration(file = "test1.yml", prefix = "c.d")
	public static class Config2 {
		private int e;
		private Long f;
		private String g;
		private List<String> h;
		@Value("f")
		private Integer ff;
		@Value("h")
		private Set<String> hh;
		@Value("h")
		private String[] hhh;
		private Integer[] i;
		@Value("i")
		private List<Integer> ii;
		@Value("i")
		private Object iii;
	}

}
