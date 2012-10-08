package com.abelsky.complitex.tasklist.components.models;

import org.apache.wicket.model.Model;

import java.io.Serializable;

/**
 * A model tracking update state.
 *
* @author andy
*/
public class UpdateableModel<T extends Serializable> extends Model<T> {
    private T lastValue;

    public UpdateableModel() {
    }

    /**
     * @return {@code true} iff contained object has been changed
     * and {@link #setUpdated()} hasn't been called since.
     */
    public boolean isUpdated() {
        return lastValue != getObject();
    }

    /**
     * @see #isUpdated()
     */
    public void setUpdated() {
        lastValue = getObject();
    }
}
