package org.processmining.eventstream.core.interfaces;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.processmining.stream.core.interfaces.XSDataPacket;

/**
 * ...
 * <p/>
 * Please note that this type is actually equivalent to {@link XAttributeMap}.
 */
public interface XSEvent extends XSDataPacket<String, XAttribute> {

}
