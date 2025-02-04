/*******************************************************************************
 * Copyright (c) 2024 Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Sebastian Hollersbacher - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.ui.widget;

import org.eclipse.gef.dnd.SimpleObjectTransfer;

public class ImportTransfer extends SimpleObjectTransfer {
	private static final ImportTransfer INSTANCE = new ImportTransfer();
	private static final String TYPE_NAME = "org.eclipse.4diac.clipboard.transfer.imports"; //$NON-NLS-1$
	private static final int TYPE_ID = registerType(TYPE_NAME);

	private ImportTransfer() {
	}

	public static ImportTransfer getInstance() {
		return INSTANCE;
	}

	@Override
	protected int[] getTypeIds() {
		return new int[] { TYPE_ID };
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { TYPE_NAME };
	}
}
