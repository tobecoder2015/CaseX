package com.autonavi.poi.core;


import com.autonavi.poi.domain.Group;
import com.autonavi.poi.domain.casex.ApiCaseContent;
import com.autonavi.poi.domain.casex.TestCase;
import com.autonavi.poi.domain.contant.CaseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qingshan.wqs on 2016/11/10.
 */
@Slf4j
public class ParaHandler {
    public static void preTreat(TestCase apiCase) throws Exception {

        if(apiCase.getCaseType()== CaseTypeEnum.流程用例.value())
            return;
        log.info("方法计算替换字符串");

        Group group = new Group();

        Map<String, Object> allMap = new HashMap();

        Map<String, Object> groupMap = group.getGroupMap();
        Map<String, Object> dictionMap = group.getDictionMap();
        ApiCaseContent apiCaseContent=null;
        if(apiCase.getCaseContent()!=null)
            apiCaseContent=(ApiCaseContent)apiCase.getCaseContent();

//        Map<String, Object> paraMap = apiCaseContent.getParaMap();

                Map<String, Object> paraMap = new HashMap<>();

        if(groupMap!=null) {
            if(paraMap==null) {
                paraMap = new HashMap<>();
                paraMap.putAll(groupMap);
                log.debug("paraMap 使用groupMap");
            }else {
                Iterator<String> iterator = groupMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();

                    if (!paraMap.containsKey(key)) {
                        paraMap.put(key, groupMap.get(key));
                        log.debug("参数补充groupMap中键值  " + key);
                    }
                }
            }
        }

        if(dictionMap!=null)
            allMap.putAll(dictionMap);
        allMap.putAll(paraMap);


        Iterator<String> iterator = paraMap.keySet().iterator();
        while (iterator.hasNext()) {
            String property = iterator.next();
            Object value = paraMap.get(property);
            if (value == null)
                continue;

            if (value instanceof String) {
                String paraValue = (String) value;

                //#开头    字典替换
                if (paraValue.startsWith("#")) {
                    String key = paraValue.substring(1);
                    if (dictionMap.containsKey(key.toLowerCase())) {
                        paraMap.put(property, dictionMap.get(key.toLowerCase()));
                        log.debug(property + " 进行了替换，替换为 " + dictionMap.get(key.toLowerCase()));
                    } else if (key.equalsIgnoreCase("AUTO") && groupMap.containsKey(property)) {
                        paraMap.put(property, groupMap.get(property));
                        log.info(property + " 进行了替换，替换为 " + groupMap.get(property));
                    } else {
                        throw new IllegalAccessException("关键字 " + key + " 不存在,请确认是否小写");
                    }
                }
                //@开头  方法调用
                if (paraValue.startsWith("@")) {
                    String methodInfo = paraValue.substring(1);
                    String methodName = null;

                    String newvalue = null;
                    try {

                        Class<?> threadClazz = Class.forName("com.autonavi.poi.dataflow.utils.ValueGet");
                        if (methodInfo.contains("(")) {
                            methodName = methodInfo.substring(1, methodInfo.indexOf("("));
                            String[] paraKeys = methodInfo.substring(methodInfo.indexOf("(") + 1, methodInfo.indexOf(")")).split(",");
                            Object[] paras = new Object[paraKeys.length];
                            int count = 0;
                            for (String paraKey : paraKeys) {
                                paras[count++] = allMap.get(paraKey);
                            }
                            Method method = threadClazz.getMethod(methodName);
                            newvalue = (String) method.invoke(paras);

                        } else {
                            methodName = methodInfo;
                            Method method = threadClazz.getMethod(methodName);
                            newvalue = (String) method.invoke(null);

                        }
                    } catch (Exception e) {
                        log.error("方法运算替换时出现异常", e);
                        throw new IllegalAccessException("调用方法 " + methodInfo + "  出错，请确认方法和参数key值是否存在");
                    }
                    paraMap.put(property, newvalue);
                    log.info(property + " 进行了替换，替换为 " + newvalue);
                }

                allMap.putAll(paraMap);


                //$文本替换 从allMap 中获取
                String findKey = null;
                String findValue = null;
                if (paraValue.indexOf("${") != -1) {

                    Pattern p = Pattern.compile("\\$\\{([a-zA-Z0-9]+)\\}");
                    Matcher m = p.matcher(paraValue);
                    while (m.find()) {
                        findKey = m.group(1);
                        if (allMap.containsKey(findKey)) {
                            findValue = (String) allMap.get(findKey);
                        } else {
                            throw new IllegalAccessException("$替换操作时没有找到对应的键值  " + findKey);
                        }
                        paraValue = paraValue.replace("${" + findKey + "}", findValue);
                    }
                    log.info("$替换操作找到对应的键值 " + findKey + " 对应的值为 " + findValue);
                    paraMap.put(property, paraValue);
                }
            }
        }
    }
}
