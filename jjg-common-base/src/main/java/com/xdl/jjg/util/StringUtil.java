package com.xdl.jjg.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字串工具类
 */
public class StringUtil {
    protected static final Log logger = LogFactory.getLog(StringUtil.class);

    /**
     * 将一个Double转为int的String，将省略小数点后面的值
     *
     * @param d
     * @return
     */
    public static String doubleToIntString(Double d) {
        int value = ((Double) d).intValue();
        return String.valueOf(value);
    }

    /**
     * 检查浮点数
     *
     * @param num
     * @param type "0+":非负浮点数 "+":正浮点数 "-0":非正浮点数 "-":负浮点数 "":浮点数
     * @return
     */
    public static boolean checkFloat(String num, String type) {
        String eL = "";
        if ("0+".equals(type)) {
            // 非负浮点数
            eL = "^\\d+(\\.\\d+)?$";
        } else if ("+".equals(type)) {
            // 正浮点数
            eL = "^((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*))$";
        } else if ("-0".equals(type)) {
            // 非正浮点数
            eL = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";
        } else if ("-".equals(type)) {
            // 负浮点数
            eL = "^(-((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*)))$";
        } else {
            // 浮点数
            eL = "^(-?\\d+)(\\.\\d+)?$";
        }
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(num);
        boolean b = m.matches();
        return b;
    }

    /**
     * MD5加密
     *
     * @param str     内容
     * @param charset 编码方式
     * @throws Exception
     */
    public static String md5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < result.length; i++) {
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }


    /**
     * 检测某字串是否存在某数组中
     *
     * @param value
     * @param array
     * @return 存在返回真，不存在返回假
     */
    public static boolean isInArray(String value, String[] array) {
        if (array == null) {
            return false;
        }
        for (String v : array) {
            if (v.equals(value)) {
                return true;
            }
        }
        return false;

    }

    public static boolean isInArray(int value, String[] array) {
        if (array == null) {
            return false;
        }
        for (String v : array) {
            if (Integer.valueOf(v).intValue() == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将数组成str连接成字符串
     *
     * @param str
     * @param array
     * @return
     */
    public static String implode(String str, Object[] array) {
        if (str == null || array == null) {
            return "";
        }
        String result = "";
        for (int i = 0; i < array.length; i++) {
            if (i == array.length - 1) {
                result += array[i].toString();
            } else {
                result += array[i].toString() + str;
            }
        }
        return result;
    }

    public static String implodeValue(String str, Object[] array) {
        if (str == null || array == null) {
            return "";
        }
        String result = "";
        for (int i = 0; i < array.length; i++) {
            if (i == array.length - 1) {
                result += "?";
            } else {
                result += "?" + str;
            }
        }
        return result;
    }

    /**
     * MD5加密方法
     *
     * @param str String
     * @return String
     */
    public static String md5(String str) {
        return md5(str, true);
    }

    public static String md5(String str, boolean zero) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
        byte[] resultByte = messageDigest.digest(str.getBytes());
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < resultByte.length; ++i) {
            int v = 0xFF & resultByte[i];
            if (v < 16 && zero) {
                result.append("0");
            }
            result.append(Integer.toHexString(v));
        }
        return result.toString();
    }

    /**
     * 验证Email地址是否有效
     *
     * @param sEmail
     * @return
     */
    public static boolean validEmail(String sEmail) {
        String pattern = "^([a-z0-9A-Z]+[-|\\.|_]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return sEmail.matches(pattern);
    }

    /**
     * 验证字符最大长度
     *
     * @param str
     * @return
     */
    public static boolean validMaxLen(String str, int length) {
        if (str == null || "".equals(str)) {
            return false;
        }
        if (str.length() > length) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证字符最小长度
     *
     * @param str
     * @return
     */
    public static boolean validMinLen(String str, int length) {
        if (str == null || "".equals(str)) {
            return false;
        }
        if (str.length() < length) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证一个字符串是否为空
     *
     * @param str
     * @return 如果为空返回真，如果不为空返回假
     */

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim()) || "undefined".equals(str)) {
            return true;
        }
        String pattern = "\\S";
        Pattern p = Pattern.compile(pattern, 2 | Pattern.DOTALL);
        Matcher m = p.matcher(str);
        return !m.find();
    }

    /**
     * 验证两个字符串是否相等且不能为空
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null || "".equals(str1) || str2 == null || "".equals(str2)) {
            return false;
        }
        return str1.equals(str2);
    }

    /**
     * 将字串转为数字
     *
     * @param str
     * @param checked 如果为treu格式不正确抛出异常
     * @return
     */
    public static int toInt(String str, boolean checked) {
        int value = 0;
        if (str == null || "".equals(str)) {
            return 0;
        }
        try {
            value = Integer.parseInt(str);
        } catch (Exception ex) {
            if (checked) {
                throw new RuntimeException("整型数字格式不正确");
            } else {
                return 0;
            }
        }
        return value;
    }

    /**
     * 将object转为数字
     *
     * @param obj     需要转object的对象
     * @param checked 如果为true格式不正确抛出异常
     * @return
     */
    public static int toInt(Object obj, boolean checked) {
        int value = 0;
        if (obj == null) {
            return 0;
        }
        try {
            value = Integer.parseInt(obj.toString());
        } catch (Exception ex) {
            if (checked) {
                throw new RuntimeException("整型数字格式不正确");
            } else {
                return 0;
            }
        }
        return value;
    }

    /**
     * 将一个字串转为int，如果无空，则返回默认值
     *
     * @param str          要转换的数字字串
     * @param defaultValue 默认值
     * @return
     */
    public static Integer toInt(String str, Integer defaultValue) {
        Integer value = defaultValue;
        if (str == null || "".equals(str)) {
            return defaultValue;
        }
        try {
            value = Integer.parseInt(str);
        } catch (Exception ex) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 将字符型转为Int型
     *
     * @param str
     * @return
     */
    @Deprecated
    public static int toInt(String str) {
        int value = 0;
        if (str == null || "".equals(str)) {
            return 0;
        }
        try {
            value = Integer.parseInt(str);
        } catch (Exception ex) {
            value = 0;
            ex.printStackTrace();
        }
        return value;
    }

    @Deprecated
    public static Double toDouble(String str) {
        Double value = 0d;
        if (str == null || "".equals(str)) {
            return 0d;
        }
        try {
            value = Double.valueOf(str);
        } catch (Exception ex) {
            value = 0d;
            // ex.printStackTrace();
        }
        return value;
    }

    /**
     * 将一个字串转为double
     *
     * @param str
     * @param checked 如果为treu格式不正确抛出异常
     * @return
     */
    public static Double toDouble(String str, boolean checked) {
        Double value = 0d;
        if (str == null || "".equals(str)) {
            return 0d;
        }
        try {
            value = Double.valueOf(str);
        } catch (Exception ex) {
            if (checked) {
                throw new RuntimeException("数字格式不正确");
            } else {
                return 0D;
            }
        }
        return value;
    }

    /**
     * 将一个object转为double 如果object 为 null 则返回0；
     *
     * @param obj     需要转成Double的对象
     * @param checked 如果为true格式不正确抛出异常
     * @return
     */
    public static Double toDouble(Object obj, boolean checked) {
        Double value = 0d;
        if (obj == null) {
            if (checked) {
                throw new RuntimeException("数字格式不正确");
            } else {
                return 0D;
            }
        }
        try {
            value = Double.valueOf(obj.toString());
        } catch (Exception ex) {
            if (checked) {
                throw new RuntimeException("数字格式不正确");
            } else {
                return 0D;
            }
        }
        return value;
    }

    public static Double toDouble(String str, Double defaultValue) {
        Double value = defaultValue;
        if (str == null || "".equals(str)) {
            return 0d;
        }
        try {
            value = Double.valueOf(str);
        } catch (Exception ex) {
            ex.printStackTrace();
            value = defaultValue;
        }
        return value;
    }

    /**
     * 把数组转换成String
     *
     * @param array
     * @return
     */
    public static String arrayToString(Object[] array, String split) {
        if (array == null) {
            return "";
        }
        String str = "";
        for (int i = 0; i < array.length; i++) {
            if (i != array.length - 1) {
                str += array[i].toString() + split;
            } else {
                str += array[i].toString();
            }
        }
        return str;
    }

    /**
     * 将一个list转为以split分隔的string
     *
     * @param list
     * @param split
     * @return
     */
    public static String listToString(List list, String split) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Object obj : list) {
            if (sb.length() != 0) {
                sb.append(split);
            }
            sb.append(obj.toString());
        }
        return sb.toString();
    }


    /**
     * 格式化页码
     *
     * @param page
     * @return
     */
    public static int formatPage(String page) {
        int iPage = 1;
        if (page == null || "".equals(page)) {
            return iPage;
        }
        try {
            iPage = Integer.parseInt(page);
        } catch (Exception ex) {
            iPage = 1;
        }
        return iPage;
    }

    /**
     * 将计量单位字节转换为相应单位
     *
     * @param fileSize
     * @return
     */
    public static String getFileSize(String fileSize) {
        String temp = "";
        DecimalFormat df = new DecimalFormat("0.00");
        double dbFileSize = Double.parseDouble(fileSize);
        if (dbFileSize >= 1024) {
            if (dbFileSize >= 1048576) {
                if (dbFileSize >= 1073741824) {
                    temp = df.format(dbFileSize / 1024 / 1024 / 1024) + " GB";
                } else {
                    temp = df.format(dbFileSize / 1024 / 1024) + " MB";
                }
            } else {
                temp = df.format(dbFileSize / 1024) + " KB";
            }
        } else {
            temp = df.format(dbFileSize / 1024) + " KB";
        }
        return temp;
    }

    /**
     * 得到一个32位随机字符
     *
     * @return
     */
    public static String getEntry() {
        Random random = new Random(100);
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(new String("yyyyMMddHHmmssS"));
        return md5(formatter.format(now) + random.nextDouble());
    }

    /**
     * 将中文汉字转成UTF8编码
     *
     * @param str
     * @return
     */
    public static String toUTF8(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        try {
            return new String(str.getBytes("ISO8859-1"), "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String to(String str, String charset) {
        if (str == null || "".equals(str)) {
            return "";
        }
        try {
            return new String(str.getBytes("UTF-8"), charset);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String getRandStr(int n) {
        Random random = new Random();
        String sRand = "";
        for (int i = 0; i < n; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
        }
        return sRand;
    }

    /**
     * 得到一个数字的大写(一到十之内)
     *
     * @param num
     * @return
     */
    public static String getChineseNum(int num) {
        String[] chineseNum = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
        return chineseNum[num];
    }

    public static String replaceEnter(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("\r", "").replaceAll("\n", "");
    }

    public static String replaceAll(String source, String target, String content) {
        StringBuffer buffer = new StringBuffer(source);
        int start = buffer.indexOf(target);
        if (start > 0) {
            source = buffer.replace(start, start + target.length(), content).toString();
        }
        return source;
    }

    private static final Pattern HTMLPATTERN = Pattern.compile("<[^<|^>]*>");

    /**
     * 去除HTML 元素
     *
     * @param element
     * @return
     */
    public static String getTxtWithoutHTMLElement(String element) {
        if (null == element || "".equals(element.trim())) {
            return element;
        }

        Matcher matcher = HTMLPATTERN.matcher(element);
        StringBuffer txt = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group();
            if (group.matches("<[\\s]*>")) {
                matcher.appendReplacement(txt, group);
            } else {
                matcher.appendReplacement(txt, "");
            }
        }
        matcher.appendTail(txt);
        String temp = txt.toString().replaceAll("\n", "");
        temp = temp.replaceAll(" ", "");
        return temp;
    }

    /**
     * clear trim to String
     *
     * @return
     */
    public static String toTrim(String strtrim) {
        if (null != strtrim && !"".equals(strtrim)) {
            return strtrim.trim();
        }
        return "";
    }

    /**
     * 转义字串的$
     *
     * @param str
     * @return
     */
    public static String filterDollarStr(String str) {
        String sReturn = "";
        if (!"".equals(toTrim(str))) {
            if (str.indexOf('$', 0) > -1) {
                while (str.length() > 0) {
                    if (str.indexOf('$', 0) > -1) {
                        sReturn += str.subSequence(0, str.indexOf('$', 0));
                        sReturn += "\\$";
                        str = str.substring(str.indexOf('$', 0) + 1, str.length());
                    } else {
                        sReturn += str;
                        str = "";
                    }
                }

            } else {
                sReturn = str;
            }
        }
        return sReturn;
    }

    public static String compressHtml(String html) {
        if (html == null) {
            return null;
        }

        html = html.replaceAll("[\\t\\n\\f\\r]", "");
        return html;
    }

    public static String toCurrency(Double d) {
        if (d != null) {
            DecimalFormat df = new DecimalFormat("￥#,###.00");
            return df.format(d);
        }
        return "";
    }

    public static String toString(Integer i) {
        if (i != null) {
            return String.valueOf(i);
        }
        return "";
    }

    public static String toString(Double d) {
        if (null != d) {
            return String.valueOf(d);
        }
        return "";
    }

    /**
     * Object转String的方法
     *
     * @param o       需要转String的对象
     * @param checked 是否检测异常
     * @return String 转换之后的字符串
     */
    public static String toString(Object o, boolean checked) {
        String value = "";
        if (null != o) {
            try {
                value = o.toString();

            } catch (Exception e) {
                if (checked) {
                    throw new RuntimeException("String类型不正确");
                }
            }
        }
        return value;
    }

    /**
     * Object转String的方法 若为空，或者转换出现异常
     *
     * @param o 需要转String的对象
     * @return 转换之后的字符串
     */
    public static String toString(Object o) {
        String value = "";
        if (null != o) {
            value = o.toString();
        }
        return value;
    }

    public static String getRandom() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < 6; i++) {
            result = result * 10 + array[i];
        }

        return "" + result;
    }

    /**
     * 处理树型码 获取本级别最大的code 如:301000 返回301999
     *
     * @param code
     * @return
     */
    public static int getMaxLevelCode(int code) {
        String codeStr = "" + code;
        StringBuffer str = new StringBuffer();
        boolean flag = true;
        for (int i = codeStr.length() - 1; i >= 0; i--) {
            char c = codeStr.charAt(i);
            if (c == '0' && flag) {
                str.insert(0, '9');
            } else {
                str.insert(0, c);
                flag = false;
            }
        }
        return Integer.valueOf(str.toString());
    }

    /**
     * 去掉sql的注释
     */
    public static String delSqlComment(String content) {
        String pattern = "/\\*(.|[\r\n])*?\\*/";
        Pattern p = Pattern.compile(pattern, 2 | Pattern.DOTALL);
        Matcher m = p.matcher(content);
        if (m.find()) {
            content = m.replaceAll("");
        }
        return content;
    }

    public static String inputStream2String(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static String decode(String keyword) {
        try {
            keyword = URLDecoder.decode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("解码失败:", e);
        }

        return keyword;
    }

    /**
     * 进行解析
     *
     * @param regex
     * @param rpstr
     * @param source
     * @return
     */
    public static String doFilter(String regex, String rpstr, String source) {
        Pattern p = Pattern.compile(regex, 2 | Pattern.DOTALL);
        Matcher m = p.matcher(source);
        return m.replaceAll(rpstr);
    }

    /**
     * 脚本过滤
     *
     * @param source
     * @return
     */
    public static String formatScript(String source) {
        source = source.replaceAll("javascript", "&#106avascript");
        source = source.replaceAll("jscript:", "&#106script:");
        source = source.replaceAll("js:", "&#106s:");
        source = source.replaceAll("value", "&#118alue");
        source = source.replaceAll("about:", "about&#58");
        source = source.replaceAll("file:", "file&#58");
        source = source.replaceAll("document.cookie", "documents&#46cookie");
        source = source.replaceAll("vbscript:", "&#118bscript:");
        source = source.replaceAll("vbs:", "&#118bs:");
        source = doFilter("(on(mouse|exit|error|click|key))", "&#111n$2", source);
        return source;
    }

    /**
     * 格式化HTML代码
     *
     * @param htmlContent
     * @return
     */
    public static String htmlDecode(String htmlContent) {
        htmlContent = formatScript(htmlContent);
        htmlContent = htmlContent.replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("\n\r", "<br>").replaceAll("\r\n", "<br>").replaceAll("\r", "<br>");
        return htmlContent;
    }

    /**
     * 得到异常的字串
     *
     * @param aThrowable
     * @return
     */
    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();

    }

    /**
     * 判断Integer是否为空
     *
     * @param i           需要校验的Integer对象
     * @param includeZero 是否包含0
     * @return
     */
    public static boolean isEmpty(Integer i, boolean includeZero) {
        if (includeZero) {
            if (i == null || i.equals(0)) {
                return true;
            }
        }
        if (i == null) {
            return true;
        }
        return false;
    }

    /**
     * 不为空的判断
     *
     * @param str
     * @return
     */
    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 是否是整数
     */
    private static final Pattern PATTERNINTEGER = Pattern.compile("^[-\\+]?[\\d]*$");

    /**
     * 是否是小数
     */
    private static final Pattern PATTERNDOUBLE = Pattern.compile("^[-\\+]?[.\\d]*$");

    /**
     * 判断是否是数字，传入字符串，判断是否是数字，true 是 false 否
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNumber(String str) {

        return PATTERNINTEGER.matcher(str).matches() || PATTERNDOUBLE.matcher(str).matches();
    }

    /**
     * 判断一个数组是否为空，并且长度大于0 by fk
     *
     * @param list
     * @return true 不空/false 空
     */
    public static boolean isNotEmpty(List list) {

        return list != null && list.size() > 0;
    }

    /**
     * 将下划线转成大写字母
     *
     * @param columnName
     * @return
     */
    public static String lowerUpperCaseColumn(String columnName) {

        StringBuffer propName = new StringBuffer("");

        for (int i = 0; i < columnName.length(); ) {
            if ("_".equals(columnName.charAt(i) + "")) {
                propName.append(Character.toUpperCase(columnName.charAt(i + 1)));
                i += 2;
            } else {
                propName.append(columnName.charAt(i));
                i += 1;
            }
        }
        return propName.toString();

    }
}
