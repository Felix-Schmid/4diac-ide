/*******************************************************************************
 * Copyright (c) 2021, 2022 Johannes Kepler University Linz and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Antonio Garmenda, Bianca Wiesmayr
 *       - initial implementation and/or documentation
 *   Paul Pavlicek
 *   	 -cleanup and us factory methods
 *******************************************************************************/
package org.eclipse.fordiac.ide.test.fb.interpreter.infra;

import java.io.IOException;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fordiac.ide.fb.interpreter.OpSem.BasicFBTypeRuntime;
import org.eclipse.fordiac.ide.fb.interpreter.OpSem.EventManager;
import org.eclipse.fordiac.ide.fb.interpreter.OpSem.EventOccurrence;
import org.eclipse.fordiac.ide.fb.interpreter.OpSem.FBNetworkRuntime;
import org.eclipse.fordiac.ide.fb.interpreter.OpSem.FBRuntimeAbstract;
import org.eclipse.fordiac.ide.fb.interpreter.OpSem.FBTransaction;
import org.eclipse.fordiac.ide.fb.interpreter.OpSem.OperationalSemanticsFactory;
import org.eclipse.fordiac.ide.fb.interpreter.OpSem.SimpleFBTypeRuntime;
import org.eclipse.fordiac.ide.fb.interpreter.OpSem.Transaction;
import org.eclipse.fordiac.ide.fb.interpreter.api.EventOccFactory;
import org.eclipse.fordiac.ide.fb.interpreter.api.RuntimeFactory;
import org.eclipse.fordiac.ide.fb.interpreter.api.TransactionFactory;
import org.eclipse.fordiac.ide.fb.interpreter.mm.utils.EventManagerUtils;
import org.eclipse.fordiac.ide.fb.interpreter.mm.utils.SequenceMatcher;
import org.eclipse.fordiac.ide.fb.interpreter.mm.utils.ServiceSequenceUtils;
import org.eclipse.fordiac.ide.fb.interpreter.mm.utils.VariableUtils;
import org.eclipse.fordiac.ide.model.libraryElement.AutomationSystem;
import org.eclipse.fordiac.ide.model.libraryElement.BaseFBType;
import org.eclipse.fordiac.ide.model.libraryElement.BasicFBType;
import org.eclipse.fordiac.ide.model.libraryElement.ECState;
import org.eclipse.fordiac.ide.model.libraryElement.Event;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.fordiac.ide.model.libraryElement.FBType;
import org.eclipse.fordiac.ide.model.libraryElement.InputPrimitive;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementFactory;
import org.eclipse.fordiac.ide.model.libraryElement.OutputPrimitive;
import org.eclipse.fordiac.ide.model.libraryElement.Service;
import org.eclipse.fordiac.ide.model.libraryElement.ServiceSequence;
import org.eclipse.fordiac.ide.model.libraryElement.ServiceTransaction;
import org.eclipse.fordiac.ide.model.libraryElement.SimpleFBType;
import org.eclipse.fordiac.ide.model.typelibrary.FBTypeEntry;
import org.eclipse.fordiac.ide.model.typelibrary.TypeLibrary;
import org.eclipse.fordiac.ide.model.typelibrary.TypeLibraryManager;
import org.eclipse.fordiac.ide.systemmanagement.FordiacProjectLoader;
import org.junit.Test;
import org.osgi.framework.Bundle;

public abstract class AbstractInterpreterTest {
	public static final String START_STATE = "START"; //$NON-NLS-1$
	private static final Bundle bundle = Platform.getBundle("org.eclipse.fordiac.ide.test.fb.interpreter"); //$NON-NLS-1$

	@Test
	public abstract void test() throws IllegalArgumentException;

	public static FBType loadFBType(final String name) {
		return loadFBType(name, true);
	}

	protected static FBNetwork loadFbNetwork(final String projectName, final String systemName) {
		return loadFbNetwork(projectName, systemName, null);
	}

	protected static FBNetwork loadFbNetwork(final String projectName, final String systemName, final String appName) {
		final Path projectPath = new Path("data/" + projectName); //$NON-NLS-1$
		FordiacProjectLoader loader;
		try {
			loader = new FordiacProjectLoader(bundle, projectPath);
		} catch (CoreException | IOException e) {
			return null;
		}

		final AutomationSystem system = loader.getAutomationSystem(systemName);
		if (appName == null) {
			return system.getApplication().get(0).getFBNetwork();
		}
		return system.getApplication().stream().filter(app -> app.getName().equals(appName)).findAny().orElseThrow()
				.getFBNetwork();
	}

	protected static FBType loadFBType(final String name, final boolean emptyService) {
		final Path projectPath = new Path("data/TestFBs"); //$NON-NLS-1$
		final Bundle bundle = Platform.getBundle("org.eclipse.fordiac.ide.test.fb.interpreter"); //$NON-NLS-1$
		FordiacProjectLoader loader;
		try {
			loader = new FordiacProjectLoader(bundle, projectPath);
		} catch (CoreException | IOException e) {
			return null;
		}

		final TypeLibrary typeLib = TypeLibraryManager.INSTANCE.getTypeLibrary(loader.getEclipseProject());
		final FBTypeEntry typeEntry = typeLib.getFBTypeEntry(name);
		final FBType fbt = typeEntry.getType();

		if (emptyService) {
			fbt.setService(ServiceSequenceUtils.createEmptyServiceModel());
		}
		return fbt;
	}

	protected static ServiceTransaction addTransaction(final ServiceSequence seq,
			final org.eclipse.fordiac.ide.test.fb.interpreter.infra.FBTransaction fbtrans) {
		final ServiceTransaction transaction = LibraryElementFactory.eINSTANCE.createServiceTransaction();
		seq.getServiceTransaction().add(transaction);
		if (fbtrans.getInputEvent() != null) {
			final InputPrimitive inputPrimitive = LibraryElementFactory.eINSTANCE.createInputPrimitive();
			inputPrimitive.setEvent(fbtrans.getInputEvent());
			transaction.setInputPrimitive(inputPrimitive);
		}

		if (!fbtrans.getOutputEvent().isEmpty()) {
			for (final String event : fbtrans.getOutputEvent()) {
				final OutputPrimitive outputPrimitive = LibraryElementFactory.eINSTANCE.createOutputPrimitive();
				outputPrimitive.setEvent(event);
				outputPrimitive.setInterface(((Service) seq.eContainer()).getLeftInterface());
				outputPrimitive.setParameters(""); //$NON-NLS-1$
				for (final String parameter : fbtrans.getOutputParameter()) {
					outputPrimitive.setParameters(outputPrimitive.getParameters() + parameter + ";"); //$NON-NLS-1$
				}
				transaction.getOutputPrimitive().add(outputPrimitive);
			}
		}
		return transaction;
	}

	public static BaseFBType runFBTest(final BaseFBType fb, final ServiceSequence seq) throws IllegalArgumentException {
		if (fb instanceof BasicFBType) {
			return runTest((BasicFBType) fb, seq, START_STATE);
		}
		return runSimpleFBTest((SimpleFBType) fb, seq);
	}

	public static EList<Transaction> runFBNetworkTest(final FBNetwork network, final Event event) {
		final EventManager eventManager = OperationalSemanticsFactory.eINSTANCE.createEventManager();
		// network.eResource().getContents().add(eventManager);
		// TODO create convenience methods in eventManagerUtils

		final EventOccurrence eventOccurrence = EventOccFactory.createFrom(event);
		// final FBNetworkRuntime runtime = RuntimeFactory.createFrom(network);
		final FBNetworkRuntime runtime = OperationalSemanticsFactory.eINSTANCE.createFBNetworkRuntime();
		runtime.setFbnetwork(EcoreUtil.copy(network));
		eventOccurrence.setFbRuntime(runtime);

		final FBTransaction transaction = TransactionFactory.createFrom(eventOccurrence);
		eventManager.getTransactions().add(transaction);

		EventManagerUtils.processNetwork(eventManager);
		return eventManager.getTransactions();
	}

	private static BaseFBType runSimpleFBTest(final SimpleFBType fb, final ServiceSequence seq) {
		final EventManager eventManager = OperationalSemanticsFactory.eINSTANCE.createEventManager();
		final SimpleFBTypeRuntime simpleFBTypeRT = OperationalSemanticsFactory.eINSTANCE.createSimpleFBTypeRuntime();
		simpleFBTypeRT.setSimpleFBType(fb);
		if (seq.getServiceTransaction().isEmpty()) {
			seq.getServiceTransaction().add(LibraryElementFactory.eINSTANCE.createServiceTransaction());
		}
		final FBTransaction transaction = TransactionFactory.createFrom(fb, seq.getServiceTransaction().get(0));
		if ((transaction != null) && (transaction.getInputEventOccurrence() != null)) {
			transaction.getInputEventOccurrence().setFbRuntime(simpleFBTypeRT);
		}
		eventManager.getTransactions().add(transaction);

		EventManagerUtils.process(eventManager);

		checkResults(seq, eventManager);

		return null;
	}

	public static BasicFBType runTest(final BasicFBType fb, final ServiceSequence seq, final String startStateName)
			throws IllegalArgumentException {
		final ResourceSet reset = new ResourceSetImpl();
		final Resource resource = reset.createResource(URI.createURI("platform:/resource/" + fb.getName() + ".xmi")); //$NON-NLS-1$ //$NON-NLS-2$
		final EventManager eventManager = OperationalSemanticsFactory.eINSTANCE.createEventManager();
		resource.getContents().add(eventManager);
		final BasicFBTypeRuntime basicFBTypeRT = ((BasicFBTypeRuntime) RuntimeFactory.createFrom(fb));
		// set the start state
		final EList<ECState> stateList = basicFBTypeRT.getBasicfbtype().getECC().getECState();
		final ECState startState = stateList.stream().filter(s -> s.getName().equals(startStateName))
				.collect(Collectors.toList()).get(0);
		basicFBTypeRT.setActiveState(startState);

		eventManager.getTransactions().addAll(TransactionFactory.createFrom(fb, seq, basicFBTypeRT));

		EventManagerUtils.process(eventManager);
		// TODO save the transactions

		checkResults(seq, eventManager);

		final int nT = eventManager.getTransactions().size();
		final FBTransaction t = (FBTransaction) eventManager.getTransactions().get(nT - 1);
		BasicFBType next = null;
		if (!t.getOutputEventOccurrences().isEmpty()) {
			final int nEv = t.getOutputEventOccurrences().size();
			final BasicFBTypeRuntime last = (BasicFBTypeRuntime) (t.getOutputEventOccurrences().get(nEv - 1)
					.getFbRuntime());
			next = last.getBasicfbtype();
		} else {
			next = fb;
		}

		eventManager.getTransactions().clear();
		return next;
	}

	private static void checkResults(final ServiceSequence seq, final EventManager eventManager)
			throws IllegalArgumentException {
		final EList<ServiceTransaction> expectedResults = seq.getServiceTransaction();
		final EList<Transaction> results = eventManager.getTransactions();

		if (expectedResults.size() != results.size()) { // correct test data
			throw new IllegalArgumentException("test data is incorrect"); //$NON-NLS-1$
		}

		for (int i = 0; i < expectedResults.size(); i++) {
			final FBTransaction result = (FBTransaction) results.get(i);
			final ServiceTransaction expectedResult = expectedResults.get(i);
			checkTransaction(result, expectedResult);
		}
	}

	private static void checkTransaction(final FBTransaction result, final ServiceTransaction expectedResult) {
		// input event was correctly generated
		if (!result.getInputEventOccurrence().getEvent().getName()
				.equals(expectedResult.getInputPrimitive().getEvent())) {
			throw new IllegalArgumentException("Input event was not generated correctly"); //$NON-NLS-1$
		}

		// no unwanted output event occurrences
		final long outputEvents = expectedResult.getOutputPrimitive().stream().filter(
				p -> !p.getInterface().getName().toLowerCase().contains(ServiceSequenceUtils.INTERNAL_INTERFACE))
				.count();
		if (outputEvents != result.getOutputEventOccurrences().size()) {
			throw new IllegalArgumentException("Unwanted output event occurrence"); //$NON-NLS-1$
		}

		// check all output primitives
		for (int j = 0; j < outputEvents; j++) {
			final OutputPrimitive p = expectedResult.getOutputPrimitive().get(j);
			checkOutputPrimitive(result, j, p);
		}
	}

	private static void checkOutputPrimitive(final FBTransaction result, final int j, final OutputPrimitive p) {
		if (!p.getInterface().getName().toLowerCase().contains(ServiceSequenceUtils.INTERNAL_INTERFACE)) {
			// generated output event is correct
			if (!p.getEvent().equals(result.getOutputEventOccurrences().get(j).getEvent().getName())) {
				throw new IllegalArgumentException("Generated output event is incorrect"); //$NON-NLS-1$
			}
			// the associated data is correct
			if (!processParameters(p.getParameters(), result)) {
				throw new IllegalArgumentException("Parameter values do not match the data"); //$NON-NLS-1$
			}
		}
	}

	private static boolean processParameters(final String parameters, final FBTransaction result) {
		if ((parameters == null) || parameters.isBlank()) {
			return true;
		}
		final int length = result.getOutputEventOccurrences().size();
		final FBRuntimeAbstract captured = result.getOutputEventOccurrences().get(length - 1).getFbRuntime();
		final var parameterList = ServiceSequenceUtils.splitAndCleanList(parameters, ";"); //$NON-NLS-1$
		final SequenceMatcher sm = new SequenceMatcher(getFBType(captured));

		for (final String assumption : parameterList) {
			if (!sm.matchVariable(assumption, false)) {
				return false;
			}
		}
		return true;
	}

	private static FBType getFBType(final FBRuntimeAbstract captured) {
		final EObject model = captured.getModel();
		if (model instanceof FBType) {
			return (FBType) model;
		}
		return null;
	}

	protected static void setVariable(final BaseFBType fb, final String string, final String string2) {
		VariableUtils.setVariable(fb, string, string2);

	}
}
