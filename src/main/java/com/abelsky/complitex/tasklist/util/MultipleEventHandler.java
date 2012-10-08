package com.abelsky.complitex.tasklist.util;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

import java.io.Serializable;

/**
 * Event handling utilities.
 *
 * @author andy
 */
public class MultipleEventHandler {

    /**
     * Attaches an instance of {@link AjaxFormComponentUpdatingBehavior} to the {@code target}
     * for each of the specified {@code events).
     */
    public static void attachHandler(Component target, final UpdateHandler handler, String... events) {
        for (String event : events) {
            target.add(new AjaxFormComponentUpdatingBehavior(event) {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    handler.onUpdate(target);
                }
            });
        }
    }

    public static interface UpdateHandler extends Serializable {
        void onUpdate(AjaxRequestTarget target);
    }
}
