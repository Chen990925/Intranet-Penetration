
package fun.asgc.neutrino.core.aop.proxy;

import fun.asgc.neutrino.core.aop.Invocation;
import fun.asgc.neutrino.core.util.ArrayUtil;
import fun.asgc.neutrino.core.util.Assert;
import fun.asgc.neutrino.core.util.ClassUtil;
import fun.asgc.neutrino.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/14
 */
@Slf4j
public class SubClassProxyFactory implements ProxyFactory {
    private static String classNameTemplate = "%sSubClassProxy$$%s";
    private static AtomicLong proxyClassCounter = new AtomicLong();
    private ProxyCompiler compiler = new ProxyCompiler();
    private ProxyClassLoader classLoader = new ProxyClassLoader();

    @Override
    public <T> T get(Class<T> clazz) {
        Assert.isTrue(canProxy(clazz), String.format("类[%s]无法被代理!", clazz.getName()));
        try {
            return doGet(clazz);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canProxy(Class<?> clazz) {
        return !ClassUtil.isFinal(clazz) && !ClassUtil.isAbstract(clazz) && ClassUtil.isPublic(clazz) && !ClassUtil.isStatic(clazz);
    }

    private <T> T doGet(Class<T> clazz) throws ReflectiveOperationException {
        ProxyClass proxyClass = new ProxyClass(clazz);
        proxyClass.setName(generateClassName(clazz));
        String sourceCode = generateProxyClassSourceCode(clazz, proxyClass.getName());
        proxyClass.setSourceCode(sourceCode);
        log.debug("类:{} 的代理类源码:\n{}", clazz.getName(), sourceCode);

        compiler.compile(proxyClass);
        Class<T> retClass = (Class<T>)classLoader.loadProxyClass(proxyClass);
        T obj = retClass.newInstance();
        return obj;
    }

    private String generateClassName(Class<?> clazz) {
        return String.format(classNameTemplate, clazz.getSimpleName(), proxyClassCounter.incrementAndGet());
    }

    /**
     * 生成代理类源代码 - 继承方式
     * 1、类不能有final修饰符
     * 2、被代理方法不能有final修饰符
     * @param clazz
     * @return
     */
    private String generateProxyClassSourceCode(Class<?> clazz, String proxyClassName) {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + clazz.getPackage().getName() + ";").append("\n");
        sb.append("import ").append(Invocation.class.getName()).append(";\n");
        sb.append("public class ").append(proxyClassName).append(" extends ").append(clazz.getSimpleName()).append("{\n");
        Method[] methods = clazz.getMethods();
        if (ArrayUtil.notEmpty(methods)) {
            for (Method method : methods) {
                if (Modifier.isFinal(method.getModifiers())) {
                    continue;
                }
                Long methodId = ProxyCache.setMethod(method);
                Class<?> returnType = method.getReturnType();
                boolean isVoid = returnType == void.class;
                String parametersString = buildParametersString(method.getParameters());
                String parameterNamesString = buildParameterNamesString(method.getParameters());

                sb.append("\t").append("public").append(" ").append(returnType.getName()).append(" ").append(method.getName()).append("(").append(parametersString).append(") {").append("\n")
                        .append("\t\tInvocation inv = new Invocation(").append(methodId + "L,").append("this,").append("() -> {").append("\n")
                        .append("\t\t\t").append(isVoid ? "" : "return ").append("super.").append(method.getName()).append("(").append(parameterNamesString).append(");").append("\n")
                        .append(isVoid ? "\t\t\treturn null;\n" : "")
                        .append("\t\t").append("}").append(StringUtil.isEmpty(parameterNamesString) ? "" : "," + parameterNamesString).append(");").append("\n")
                        .append("\t\t").append("inv.invoke();").append("\n")
                        .append(isVoid ? "" : "\t\treturn inv.getReturnValue();\n")
                        .append("\t").append("}").append("\n");
            }
        }
        sb.append("}").append("\n");
        return sb.toString();
    }

    private String buildParametersString(Parameter[] parameters) {
        if (ArrayUtil.isEmpty(parameters)) {
            return "";
        }
        return Stream.of(parameters).map(item -> item.getType().getName() + " " + item.getName()).collect(Collectors.joining(","));
    }

    private String buildParameterNamesString(Parameter[] parameters) {
        if (ArrayUtil.isEmpty(parameters)) {
            return "";
        }
        return Stream.of(parameters).map(Parameter::getName).collect(Collectors.joining(","));
    }
}
