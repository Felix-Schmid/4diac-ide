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
package org.eclipse.fordiac.ide.ui.quickfix;

import org.eclipse.fordiac.ide.contractSpec.Interval;
import org.eclipse.fordiac.ide.contractSpec.Value;
import org.eclipse.fordiac.ide.validation.ContractSpecValidator;
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;

/**
 * Custom quickfixes.
 *
 * See
 * https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#quick-fixes
 */
public class ContractSpecQuickfixProvider extends DefaultQuickfixProvider {

	@Fix(ContractSpecValidator.EMPTY_INTERVAL)
	public static void swapIntervalValues(final Issue issue, final IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, "Swap values", "Swaps the two values of the interval.", "", (element, context) -> {
			if (element instanceof final Interval inter) {
				final Value tmp = inter.getV1();
				inter.setV1(inter.getV2());
				inter.setV2(tmp);
			}
		});
	}
}
