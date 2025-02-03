/*******************************************************************************
 * Copyright (c) 2024 Primetals Technology Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Michael Oberlehner - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.model.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fordiac.ide.model.commands.QualNameChangeListener.QualNameChangeState;
import org.eclipse.fordiac.ide.model.libraryElement.INamedElement;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElement;
import org.eclipse.fordiac.ide.model.typelibrary.TypeEntry;

public interface QualNameAffectedCommand extends ScopedCommand {

	String getOldQualName(INamedElement elemt);

	String getNewQualName(INamedElement element);

	List<INamedElement> getChangedElements();

	// private

	/**
	 * encapsulate the change to not provide the command to the receiver
	 */
	default List<QualNameChange> getQualNameChanges(final QualNameChangeState state) {

		final List<QualNameChange> qualNameChanges = new ArrayList<>();

		for (final INamedElement element : getChangedElements()) {
			qualNameChanges.add(new QualNameChange(getOldQualName(element), getNewQualName(element), element,
					getTypeEntry(element), state));
		}

		return qualNameChanges;
	}

	static TypeEntry getTypeEntry(final INamedElement notifier) {
		final EObject rootContainer = EcoreUtil.getRootContainer(notifier);
		Assert.isTrue(rootContainer instanceof LibraryElement);
		return ((LibraryElement) rootContainer).getTypeEntry();
	}

}
