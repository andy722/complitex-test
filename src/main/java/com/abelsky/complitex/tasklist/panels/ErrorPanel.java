package com.abelsky.complitex.tasklist.panels;

import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Feedback panel with custom markup.
 * <p>
 * <strong>Note: as for now there's no need to support errorlevels, all the messages are displayed as errors.</strong>
 * </p>
 *
 * @author andy
 */
public class ErrorPanel extends FeedbackPanel {

    public ErrorPanel(String id) {
        super(id);
    }

}
