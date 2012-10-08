package com.abelsky.complitex.tasklist.util;

import org.apache.ibatis.io.Resources;
import org.apache.wicket.util.io.IOUtils;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Yet another one utility class for accessing application resources.
 *
 * @author andy
 */
public class ResourceReader {

    public static final String HOVER_SUPPORT = "com/abelsky/complitex/tasklist/common/hoverSupport.js";

    /**
     * Reads contents of the specified resource to string.
     */
    public static String getResource(String resource) {
        try {
            final StringWriter hoverSupportTemplate = new StringWriter();
            IOUtils.copy(Resources.getResourceAsReader(resource), hoverSupportTemplate);
            return hoverSupportTemplate.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
