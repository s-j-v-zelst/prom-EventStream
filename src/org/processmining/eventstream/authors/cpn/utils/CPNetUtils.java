package org.processmining.eventstream.authors.cpn.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import org.cpntools.accesscpn.engine.highlevel.HighLevelSimulator;
import org.cpntools.accesscpn.engine.highlevel.instance.Instance;
import org.cpntools.accesscpn.model.Arc;
import org.cpntools.accesscpn.model.HLAnnotation;
import org.cpntools.accesscpn.model.ModelFactory;
import org.cpntools.accesscpn.model.Node;
import org.cpntools.accesscpn.model.Page;
import org.cpntools.accesscpn.model.PetriNet;
import org.cpntools.accesscpn.model.Place;
import org.cpntools.accesscpn.model.PlaceNode;
import org.cpntools.accesscpn.model.Transition;

import com.google.common.collect.Iterables;

/**
 * Utility class for PetriNets originating from Access CPN. Adopted code from
 * org.cpntools.accesscpn.model.util.BuildCPNUtil originally authored by
 * dfahland.
 * 
 * @author svzelst, dfahland
 *
 */
public class CPNetUtils {

	private static int id = 0;

	private static ModelFactory mf = ModelFactory.INSTANCE;

	/**
	 * Add a new place to the page.
	 * 
	 * @param page
	 * @param name
	 * @param type
	 * @return
	 */
	public static Place addPlace(final Page page, final String name, final String type) {
		return addPlace(page, name, type, "");
	}

	/**
	 * Add a new place to the page.
	 * 
	 * @param page
	 * @param name
	 * @param type
	 * @param initialMarking
	 * @return
	 */
	public static Place addPlace(final Page page, final String name, final String type, final String initialMarking) {
		final Place p = mf.createPlace();
		p.setId(getNextID());
		p.setName(mf.createName());
		p.getName().setText(name);
		p.setSort(mf.createSort());
		p.getSort().setText(type);

		p.setInitialMarking(mf.createHLMarking());
		p.getInitialMarking().setText(initialMarking);

		p.setPage(page);

		return p;
	}

	/**
	 * Add a new transition to the page.
	 * 
	 * @param page
	 * @param name
	 * @return
	 */
	public static Transition addTransition(final Page page, final String name) {
		return addTransition(page, name, "");
	}

	/**
	 * Add a new transition to the page.
	 * 
	 * @param page
	 * @param name
	 * @param guard
	 * @return
	 */
	public static Transition addTransition(final Page page, final String name, final String guard) {
		final Transition t = mf.createTransition();
		t.setId(getNextID());
		t.setPage(page);
		t.setName(mf.createName());
		t.getName().setText(name);
		t.setTime(mf.createTime());
		t.setCode(mf.createCode());
		t.setPriority(mf.createPriority());

		t.setCondition(mf.createCondition());
		t.getCondition().setText(guard);

		return t;
	}

	/**
	 * Extract the Instance object of a given place p
	 * 
	 * @param p
	 *            place
	 * @param simulator
	 *            highlevelsimulator to use for estraction
	 * @return instance iff it exists, null otherwise
	 */
	public static Instance<? extends PlaceNode> extractInstance(Place p, HighLevelSimulator simulator) {
		Instance<? extends PlaceNode> instance = null;
		try {
			for (Instance<? extends PlaceNode> i : simulator.getAllPlaceInstances()) {
				if (i.getNode().getId().equals(p.getId())) {
					instance = i;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	private static String getNextID() {
		return "PNU_GEN_ID" + id++;
	}

	/**
	 * Tries to find the *root* page of this PetriNet
	 * 
	 * @param pn
	 *            PetriNet
	 * @return Page which is the root of the PetriNet, null if we can not find a
	 *         root page
	 */
	public static Page getRootPage(PetriNet pn) {
		Page page = null;
		if (pn.getPage().size() == 1) {
			page = pn.getPage().get(0);
		} else if (pn.getPage().size() > 1) {
			List<Page> candidates = new LinkedList<Page>();
			for (Page p : pn.getPage()) {
				if (Iterables.size(p.portPlace()) == 0) {
					candidates.add(p);
				}
			}
			if (candidates.size() == 1) {
				page = candidates.get(0);
			}
		}
		return page;
	}

	/**
	 * Find the source place within this page, assuming only one source page
	 * exists
	 * 
	 * @param page
	 *            Page to search in
	 * @return Place iff found, null otherwise
	 */
	public static Place getSourcePlace(Page page) {
		Place place = null;
		if (Iterables.size(page.place()) > 0) {
			List<Place> lp = new LinkedList<Place>();
			for (Place p : page.place()) {
				if (p.getTargetArc().isEmpty()) {
					lp.add(p);
				}
			}
			if (lp.size() == 1) {
				place = lp.get(0);
			}
		}
		return place;
	}

	/**
	 * Add a new arc to the page.
	 * 
	 * @param page
	 * @param src
	 * @param tgt
	 * @param annotation
	 * @return
	 */
	public static Arc addArc(final Page page, final Node src, final Node tgt, final String annotation) {
		final Arc a = mf.createArc();
		a.setSource(src);
		a.setTarget(tgt);
		a.setPage(page);
		a.setId(getNextID());

		final HLAnnotation a_inscr = mf.createHLAnnotation();
		a_inscr.setText(annotation);
		a.setHlinscription(a_inscr);

		return a;
	}

	/*
	 * Adapted from:
	 * http://stackoverflow.com/questions/21706722/fetch-only-first
	 * -n-lines-of-a-stack-trace
	 */
	public static String getCPNParseError(Exception e) {
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		String[] lines = writer.toString().split("\n");
		StringBuilder sb = new StringBuilder();
		sb.append(lines[0]);
		return sb.toString();
	}

}
