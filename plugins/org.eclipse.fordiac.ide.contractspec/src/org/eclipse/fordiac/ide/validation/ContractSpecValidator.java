/*******************************************************************************
 * Copyright (c) 2024 Felix Schmid
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Felix Schmid
 *     - initial commit of contract specification editor
 *******************************************************************************/
package org.eclipse.fordiac.ide.validation;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fordiac.ide.contractSpec.ContractSpecPackage;
import org.eclipse.fordiac.ide.contractSpec.EventExpr;
import org.eclipse.fordiac.ide.contractSpec.EventSpec;
import org.eclipse.fordiac.ide.contractSpec.Interval;
import org.eclipse.fordiac.ide.contractSpec.Port;
import org.eclipse.fordiac.ide.contractSpec.Reaction;
import org.eclipse.fordiac.ide.contractSpec.Value;
import org.eclipse.xtext.validation.Check;

/**
 * This class contains custom validation rules.
 *
 * See
 * https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
public class ContractSpecValidator extends AbstractContractSpecValidator {
	private static final int INPUT = 1;
	private static final int OUTPUT = 0;

	public static final String EMPTY_INTERVAL = "emptyInterval"; //$NON-NLS-1$
	public static final String PORT_NOT_INPUT = "portNotInput"; //$NON-NLS-1$
	public static final String PORT_NOT_OUTPUT = "portNotOutput"; //$NON-NLS-1$

	@Check
	public void checkInterval(final Interval interval) {
		if (interval.getTime() != null) {
			return; // was a single value -> no check needed
		}
		final double begin = value2Double(interval.getV1());
		final double end = value2Double(interval.getV2());

		if (end < begin) {
			warning("Interval is empty. The first number should be smaller than the second.",
					ContractSpecPackage.Literals.INTERVAL__V1, EMPTY_INTERVAL);
		}
	}

	@Check
	public void checkReaction(final Reaction reaction) {
		checkPortsOfType(reaction.getTrigger(), INPUT, ContractSpecPackage.Literals.REACTION__TRIGGER);
		checkPortsOfType(reaction.getReaction(), OUTPUT, ContractSpecPackage.Literals.REACTION__REACTION);
	}
	// age & causalAge is reverse of reaction: whenever OUTPUT then INPUT

	// TODO: for input/output ports check:
	// age, CausalReaction, CausalAge, CausalFuncDecl

	// SingleEvent, Repetition still unclear regarding input/output ports...

	private void checkPortsOfType(final EventExpr expr, final int type, final EStructuralFeature feature) {
		if (expr.getEvent() != null) {
			checkPortOfType(expr.getEvent().getPort(), type, feature);
		} else if (expr.getEvents() != null) {
			checkPortsOfType(expr.getEvents().getEvents(), type, feature);
		}
	}

	private void checkPortsOfType(final EList<EventSpec> list, final int type, final EStructuralFeature feature) {
		if (list == null) {
			return; // nothing to check
		}

		for (final EventSpec es : list) {
			checkPortOfType(es.getPort(), type, feature);
		}
	}

	private void checkPortOfType(final Port port, final int type, final EStructuralFeature feature) {
		if (port != null && port.getIsInput() != type) {
			if (type == INPUT) {
				final String s = String.format("Expected input ports, but %s is an output port.", port.getName());
				error(s, feature, PORT_NOT_INPUT);
			} else {
				final String s = String.format("Expected output ports, but %s is an input port.", port.getName());
				error(s, feature, PORT_NOT_OUTPUT);
			}
		}
	}

	private static double value2Double(final Value value) {
		final double i = value.getInteger();
		double f = value.getFraction();

		while (f >= 1) {
			f /= 10.0;
		}
		return i + f;
	}
}
