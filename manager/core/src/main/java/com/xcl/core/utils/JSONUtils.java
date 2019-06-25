package com.xcl.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import java.util.List;

/**
 * @author :xiaochanglu
 * @Description : json处理工具类
 * @date :2019/3/14 9:28
 */
public class JSONUtils {
    /**
     * @Description  ：过滤属性方法
     * @author       : xcl
     * @param        : [property] [待过滤的属性]
     * @return       : com.alibaba.fastjson.serializer.PropertyFilter
     * @date         : 2019/3/14 9:41
     */
    private static PropertyFilter profileter(String[] property){
        PropertyFilter profilter = new PropertyFilter(){
            @Override
            public boolean apply(Object o, String s, Object o1) {
                for(String one:property){
                    if(one.equals(s)){
                        //false表示 s 字段将被排除在外
                        return false;
                    }
                }
                return true;
            }
        };
        return profilter;
    }
    /**
     * @Description  ：javaBean 转为 JSONObject
     * @author       : xcl
     * @param        : [object]
     * @return       : com.alibaba.fastjson.JSONObject
     * @date         : 2019/3/14 14:58
     */
    public static JSONObject beanToJSON(Object object){
        return (JSONObject)JSON.toJSON(object);
    }
    /**
     * @Description  ：字符串 转为 JSONObject
     * @author       : xcl
     * @param        : [str]
     * @return       : com.alibaba.fastjson.JSONObject
     * @date         : 2019/3/14 14:59
     */
    public static JSONObject stringTOJSON(String str){
        return JSON.parseObject(str);
    }
    /**
     * @Description  ：javaBean 转为 JSON字符串
     * @author       : xcl
     * @param        : [object]
     * @return       : java.lang.String
     * @exception    :
     * @date         : 2019/3/14 14:59
     */
    public static String beanToString(Object object){
        return JSON.toJSONString(object);
    }
    /**
     * @Description  ：javaBean 转为 JSON字符串
     * 过滤 某些属性
     * @author       : xcl
     * @param        : [object, property]
     * @return       : java.lang.String
     * @date         : 2019/3/14 15:06
     */
    public static String beanToString(Object object, String[] property){
        return JSON.toJSONString(object,profileter(property));
    }

    public static JSONArray listToJSONArray(List list){
        return JSONArray.parseArray(JSON.toJSONString(list));
    }

    public static JSONArray listToJSONArray(List list, String[] property){
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<list.size();i++){
            Object one = list.get(i);
            JSONObject jsonObject = stringTOJSON(beanToString(one,property));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public static List JSONArrayToList(JSONArray array){
        return JSONObject.parseArray(array.toJSONString());
    }

    public static <T> List<T> stringToList(String string, Class<T> clazz){
        return JSONObject.parseArray(string,clazz);
    }

    public static <T> T stringToJavaObj(String string, Class<T> clazz){
        return JSONObject.parseObject(string,clazz);
    }

//    public static void main(String[] args) {
//        Map<Integer,String> map = new ConcurrentHashMap<Integer,String>(8);
//        if(map.size()<=8){
//
//        }
//        map.put(1,"111");
//        map.put(2,"222");
//        map.put(3,"333");
//        map.put(4,"444");
//        map.put(5,"555");
//        map.put(6,"666");
//        map.put(7,"777");
//        map.put(8,"888");
//        map.put(9,"999");
//        for(Entry<Integer,String> one:map.entrySet()){
//            System.out.println(one.getKey()+"====="+one.getValue());
//        }
//        class Person{
//            private String name;
//            private int age;
//            private String sex;
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//
//            public int getAge() {
//                return age;
//            }
//
//            public void setAge(int age) {
//                this.age = age;
//            }
//
//            public String getSex() {
//                return sex;
//            }
//
//            public void setSex(String sex) {
//                this.sex = sex;
//            }
//
//            public Person(String name, int age, String sex) {
//                this.name = name;
//                this.age = age;
//                this.sex = sex;
//            }
//        }
//        Person p1 = new Person("张三",18,"男");
//        Person p2 = new Person("李四",14,"女");
//        Person p3 = new Person("王五",17,"男");
//        List<Person> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        list.add(p3);
//
//        Person[] ps = {p1,p2,p3};
//        System.out.println(beanToString(ps));
//        System.out.println(beanToString(list));
//
//        System.out.println(listToJSONArray(list));
//
//        System.out.println(listToJSONArray(list,new String[]{"sex"}));
//
//        System.out.println(beanToString(p1));
//        System.out.println(beanToString(p1,new String[]{"sex"}));
//    }

}
