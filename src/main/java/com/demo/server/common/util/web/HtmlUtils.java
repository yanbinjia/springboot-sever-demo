/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-26T14:23:37.599+08:00
 */

package com.demo.server.common.util.web;

import org.springframework.util.Assert;

public abstract class HtmlUtils {
    private static final HtmlCharacterEntityReferences characterEntityReferences = new HtmlCharacterEntityReferences();

    public HtmlUtils() {
    }

    public static String htmlEscape(String input) {
        return htmlEscape(input, "ISO-8859-1");
    }

    public static String htmlEscape(String input, String encoding) {
        Assert.notNull(input, "Input is required");
        Assert.notNull(encoding, "Encoding is required");
        StringBuilder escaped = new StringBuilder(input.length() * 2);

        for (int i = 0; i < input.length(); ++i) {
            char character = input.charAt(i);
            String reference = characterEntityReferences.convertToReference(character, encoding);
            if (reference != null) {
                escaped.append(reference);
            } else {
                escaped.append(character);
            }
        }

        return escaped.toString();
    }

    public static String htmlEscapeDecimal(String input) {
        return htmlEscapeDecimal(input, "ISO-8859-1");
    }

    public static String htmlEscapeDecimal(String input, String encoding) {
        Assert.notNull(input, "Input is required");
        Assert.notNull(encoding, "Encoding is required");
        StringBuilder escaped = new StringBuilder(input.length() * 2);

        for (int i = 0; i < input.length(); ++i) {
            char character = input.charAt(i);
            if (characterEntityReferences.isMappedToReference(character, encoding)) {
                escaped.append("&#");
                escaped.append(character);
                escaped.append(';');
            } else {
                escaped.append(character);
            }
        }

        return escaped.toString();
    }

    public static String htmlEscapeHex(String input) {
        return htmlEscapeHex(input, "ISO-8859-1");
    }

    public static String htmlEscapeHex(String input, String encoding) {
        Assert.notNull(input, "Input is required");
        Assert.notNull(encoding, "Encoding is required");
        StringBuilder escaped = new StringBuilder(input.length() * 2);

        for (int i = 0; i < input.length(); ++i) {
            char character = input.charAt(i);
            if (characterEntityReferences.isMappedToReference(character, encoding)) {
                escaped.append("&#x");
                escaped.append(Integer.toString(character, 16));
                escaped.append(';');
            } else {
                escaped.append(character);
            }
        }

        return escaped.toString();
    }

    public static String htmlUnescape(String input) {
        return (new HtmlCharacterEntityDecoder(characterEntityReferences, input)).decode();
    }
}
