
package fun.asgc.neutrino.core.config.impl;

import fun.asgc.neutrino.core.config.AbstractConfigurationParser;
import fun.asgc.neutrino.core.constant.MetaDataConstant;
import fun.asgc.neutrino.core.exception.ConfigurationParserException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * 缓存类模板
 * @author: chenjunlin
 * @date: 2024/4/25
 */
public class YmlConfigurationParser extends AbstractConfigurationParser {

    @Override
    public Map<String, Object> parse2Map(InputStream in) throws ConfigurationParserException {
        try {
            return new Yaml().load(in);
        } catch (Exception e) {
            throw new ConfigurationParserException("yml配置解析异常！");
        }
    }

    @Override
    protected String defaultFileName() {
        return MetaDataConstant.DEFAULT_YML_CONFIG_FILE_NAME;
    }
}
