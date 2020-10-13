package com.xdl.jjg.util;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * 地区对象
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/5/2
 */
public class PropertyUpperStrategy {

    public static class UpperCamelCaseStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase
    {
        /**
         * Converts camelCase to PascalCase
         *
         * For example, "userName" would be converted to
         * "UserName".
         *
         * @param input formatted as camelCase string
         * @return input converted to PascalCase format
         */
        @Override
        public String translate(String input) {
            if (input == null || input.length() == 0){
                return input; // garbage in, garbage out
            }
            // Replace first lower-case letter with upper-case equivalent
            char c = input.charAt(0);
            char uc = Character.toUpperCase(c);
            if (c == uc) {
                return input;
            }
            StringBuilder sb = new StringBuilder(input);
            sb.setCharAt(0, uc);
            String s = sb.toString();
            String so = s.substring(0, 1).toLowerCase();
            String j = s.substring(1, sb.length());
            return (so+j).toString();
        }
    }
}
