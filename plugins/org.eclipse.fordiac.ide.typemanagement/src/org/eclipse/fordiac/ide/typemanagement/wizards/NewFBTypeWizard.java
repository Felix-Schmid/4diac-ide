/*******************************************************************************
 * Copyright (c) 2010 - 2018 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 * 				 2019 Johannes Kepler University Linz
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Matthias Plasch
 *     - initial API and implementation and/or initial documentation
 *   Jose Cabral - Add preferences 
 *   Alois Zoitl - moved openEditor helper function to EditorUtils  
 *******************************************************************************/
package org.eclipse.fordiac.ide.typemanagement.wizards;

import java.io.File;
import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.fordiac.ide.model.Palette.Palette;
import org.eclipse.fordiac.ide.model.Palette.PaletteEntry;
import org.eclipse.fordiac.ide.model.dataexport.AbstractTypeExporter;
import org.eclipse.fordiac.ide.model.dataimport.ImportUtils;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElement;
import org.eclipse.fordiac.ide.model.typelibrary.TypeLibrary;
import org.eclipse.fordiac.ide.systemmanagement.SystemManager;
import org.eclipse.fordiac.ide.typemanagement.Activator;
import org.eclipse.fordiac.ide.typemanagement.Messages;
import org.eclipse.fordiac.ide.typemanagement.preferences.TypeManagementPreferencesHelper;
import org.eclipse.fordiac.ide.ui.FordiacMessages;
import org.eclipse.fordiac.ide.ui.editors.EditorUtils;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class NewFBTypeWizard extends Wizard implements INewWizard {
	private IStructuredSelection selection;
	private NewFBTypeWizardPage page1;
	private PaletteEntry entry;

	public NewFBTypeWizard() {
		setWindowTitle(FordiacMessages.NewType);
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		this.selection = selection;
		setWindowTitle(FordiacMessages.NewType);
	}

	@Override
	public void addPages() {
		page1 = createNewFBTypeWizardPage();
		addPage(page1);
	}

	protected NewFBTypeWizardPage createNewFBTypeWizardPage() {
		return new NewFBTypeWizardPage(selection);
	}

	@Override
	public boolean performFinish() {
		String typeName = page1.getFileName();
		File template = page1.getTemplate();
		if (!checkTemplateAvailable(template.getAbsolutePath())) {
			return false;
		}
		IFile targetTypeFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(page1.getContainerFullPath() + File.separator + typeName));
		try {
			ImportUtils.copyFile(template, targetTypeFile);
			return finishTypeCreation(targetTypeFile);
		} catch (Exception e) {
			Activator.getDefault().logError(e.getMessage(), e);
		}
		return false;
	}

	private static boolean checkTemplateAvailable(String templatePath) {
		if (!new File(templatePath).exists()) {
			templateNotAvailable(templatePath);
			return false;
		}
		return true;
	}

	private static void templateNotAvailable(String templatePath) {
		MessageBox mbx = new MessageBox(Display.getDefault().getActiveShell());
		mbx.setMessage(MessageFormat.format(Messages.NewFBTypeWizard_TemplateNotAvailable, templatePath));
		mbx.open();
	}

	private boolean finishTypeCreation(IFile targetTypeFile) {
		Palette palette = SystemManager.INSTANCE.getPalette(targetTypeFile.getProject());
		entry = TypeLibrary.getPaletteEntry(palette, targetTypeFile);
		if (null == entry) {
			// refresh the palette and retry to fetch the entry
			TypeLibrary.refreshPalette(palette);
			entry = TypeLibrary.getPaletteEntry(palette, targetTypeFile);
		}
		LibraryElement type = entry.getType();
		type.setName(TypeLibrary.getTypeNameFromFile(targetTypeFile));
		TypeManagementPreferencesHelper.setupIdentification(type);
		TypeManagementPreferencesHelper.setupVersionInfo(type);
		AbstractTypeExporter.saveType(entry);
		entry.setType(type);
		if (page1.getOpenType()) {
			openTypeEditor(entry);
		}
		return true;
	}

	private static void openTypeEditor(PaletteEntry entry) {
		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry()
				.getDefaultEditor(entry.getFile().getName());
		EditorUtils.openEditor(new FileEditorInput(entry.getFile()), desc.getId());
	}

	public PaletteEntry getPaletteEntry() {
		return entry;
	}
}
