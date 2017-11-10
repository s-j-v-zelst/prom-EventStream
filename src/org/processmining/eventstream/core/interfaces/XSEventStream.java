package org.processmining.eventstream.core.interfaces;

import org.processmining.framework.annotations.AuthoredType;
import org.processmining.stream.core.interfaces.XSStream;

@AuthoredType(typeName = "Event Stream (XSEvent)", author = "A. Burattin, S.J. van Zelst", email = "s.j.v.zelst@tue.nl", affiliation = "Padova University, Eindhoven University of Technology")
public interface XSEventStream extends XSStream<XSEvent> {

}
