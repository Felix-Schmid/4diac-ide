/*******************************************************************************
 * Copyright (c) 2023 Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Dunja Životin - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.typemanagement.wizards;

import org.eclipse.fordiac.ide.typemanagement.Messages;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class ExtractedLibraryImportWizard extends Wizard implements IImportWizard {
	
	private ExtractedLibraryImportWizardPage firstPage;
	private StructuredSelection selection;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = new StructuredSelection(selection.toList());
		setWindowTitle(Messages.ExtractedLibraryImportWizard);
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {
		firstPage.importLib();
		return true;
	}
	
	@Override
	public void addPages() {
		firstPage = new ExtractedLibraryImportWizardPage(Messages.ImportExtractedFiles, selection);
        addPage(firstPage);
	}

}
