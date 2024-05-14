package fun.asgc.neutrino.proxy.server.config;

import fun.asgc.neutrino.proxy.core.ProxyClientConfig;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
public class ProxyServerConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 更新配置后保证在其他线程即时生效
     */
    @Getter
    private static ProxyServerConfig instance = new ProxyServerConfig();

    /**
     * 代理服务器为各个代理客户端（key）开启对应的端口列表（value）
     */
    private volatile Map<String, List<Integer>> clientInetPortMapping = new HashMap<String, List<Integer>>();

    /**
     * 代理服务器上的每个对外端口（key）对应的代理客户端背后的真实服务器信息（value）
     */
    private volatile Map<Integer, String> inetPortLanInfoMapping = new HashMap<Integer, String>();

    public void addClientConfig(ProxyClientConfig clientConfig) {
        String clientKey = clientConfig.getClientKey();
        List<Integer> ports = new ArrayList<>();
        for (ProxyClientConfig.Proxy proxy : clientConfig.getProxy()) {
            ports.add(proxy.getServerPort());
            inetPortLanInfoMapping.put(proxy.getServerPort(), proxy.getClientInfo());
        }
        clientInetPortMapping.put(clientKey, ports);
    }

    /**
     * 获取代理客户端对应的代理服务器端口
     */
    public List<Integer> getClientInetPorts(String clientKey) {
        return clientInetPortMapping.get(clientKey);
    }

    /**
     * 根据代理服务器端口获取后端服务器代理信息
     */
    public String getLanInfo(Integer port) {
        return inetPortLanInfoMapping.get(port);
    }

    /**
     * 返回需要绑定在代理服务器的端口（用于用户请求）
     */
    public List<Integer> getUserPorts() {
        List<Integer> ports = new ArrayList<Integer>();
        Iterator<Integer> ite = inetPortLanInfoMapping.keySet().iterator();
        while (ite.hasNext()) {
            ports.add(ite.next());
        }
        return ports;
    }


    /**
     * 代理客户端与其后面真实服务器映射关系
     *
     * @author fengfei
     *
     */
    @Data
    public static class ClientProxyMapping {

        /**
         * 代理服务器端口
         */
        private Integer inetPort;

        /**
         * 需要代理的网络信息（代理客户端能够访问），格式 192.168.1.99:80 (必须带端口)
         */
        private String lan;

    }
}
