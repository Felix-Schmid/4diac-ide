/*******************************************************************************
 * Copyright (c) 2020 Johannes Kepler University
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Ernst Blecha
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.fordiac.ide.model.commands.change;

import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

import java.util.Collection;
import java.util.List;

import org.eclipse.fordiac.ide.model.commands.testinfra.VersionInfoTestBase;
import org.junit.runners.Parameterized.Parameters;

//see org.eclipse.fordiac.ide.util.ColorHelperTest.java for information on implementing tests

public class ChangeVersionCommandTest extends VersionInfoTestBase {

	private static final String NEW_VERSION = "3.141592"; //$NON-NLS-1$

	private static State executeCommand(State state) {
		state.setCommand(new ChangeVersionCommand(state.getVersionInfo(), NEW_VERSION));
		assumeNotNull(state.getCommand());
		assumeTrue(state.getCommand().canExecute());
		state.getCommand().execute();
		return state;
	}

	private static void verifyState(State state, State oldState, TestFunction t) {
		t.test(state.getVersionInfo().getVersion().equals(NEW_VERSION));
		t.test(state.getVersionInfo().getDate().equals(oldState.getVersionInfo().getDate()));
		t.test(state.getVersionInfo().getOrganization().equals(oldState.getVersionInfo().getOrganization()));
		t.test(state.getVersionInfo().getRemarks().equals(oldState.getVersionInfo().getRemarks()));
		t.test(state.getVersionInfo().getAuthor().equals(oldState.getVersionInfo().getAuthor()));
	}

	private static State executeCommandToNull(State state) {
		state.setCommand(new ChangeVersionCommand(state.getVersionInfo(), null));
		assumeNotNull(state.getCommand());
		assumeTrue(state.getCommand().canExecute());
		state.getCommand().execute();
		return state;
	}

	private static void verifyStateNull(State state, State oldState, TestFunction t) {
		t.test(state.getVersionInfo().getVersion().equals(EMPTY));
		t.test(state.getVersionInfo().getDate().equals(oldState.getVersionInfo().getDate()));
		t.test(state.getVersionInfo().getOrganization().equals(oldState.getVersionInfo().getOrganization()));
		t.test(state.getVersionInfo().getRemarks().equals(oldState.getVersionInfo().getRemarks()));
		t.test(state.getVersionInfo().getAuthor().equals(oldState.getVersionInfo().getAuthor()));
	}

	// parameter creation function, also contains description of how the textual
	// description will be used
	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> data() {
		List<Object> executionDescriptions = ExecutionDescription.commandList( //
				new ExecutionDescription<State>("Change Version", //
						ChangeVersionCommandTest::executeCommand, //
						ChangeVersionCommandTest::verifyState //
				), //
				new ExecutionDescription<State>("Change Version to null", //
						ChangeVersionCommandTest::executeCommandToNull, //
						ChangeVersionCommandTest::verifyStateNull //
				) //
		);

		return createCommands(executionDescriptions);
	}

}
