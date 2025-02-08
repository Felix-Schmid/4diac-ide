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
 *     - initial implementation and/or documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.contractSpec.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.fordiac.ide.contractSpec.Age;
import org.eclipse.fordiac.ide.contractSpec.CausalAge;
import org.eclipse.fordiac.ide.contractSpec.CausalFuncDecl;
import org.eclipse.fordiac.ide.contractSpec.CausalReaction;
import org.eclipse.fordiac.ide.contractSpec.ClockDefinition;
import org.eclipse.fordiac.ide.contractSpec.ContractSpecPackage;
import org.eclipse.fordiac.ide.contractSpec.EventExpr;
import org.eclipse.fordiac.ide.contractSpec.EventList;
import org.eclipse.fordiac.ide.contractSpec.EventSpec;
import org.eclipse.fordiac.ide.contractSpec.Interval;
import org.eclipse.fordiac.ide.contractSpec.Jitter;
import org.eclipse.fordiac.ide.contractSpec.Model;
import org.eclipse.fordiac.ide.contractSpec.Offset;
import org.eclipse.fordiac.ide.contractSpec.Port;
import org.eclipse.fordiac.ide.contractSpec.Reaction;
import org.eclipse.fordiac.ide.contractSpec.Repetition;
import org.eclipse.fordiac.ide.contractSpec.RepetitionOptions;
import org.eclipse.fordiac.ide.contractSpec.SingleEvent;
import org.eclipse.fordiac.ide.contractSpec.TimeExpr;
import org.eclipse.fordiac.ide.contractSpec.TimeSpec;
import org.eclipse.fordiac.ide.contractSpec.Value;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance
 * hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object and proceeding up the
 * inheritance hierarchy until a non-null result is returned, which is the
 * result of the switch. <!-- end-user-doc -->
 *
 * @see org.eclipse.fordiac.ide.contractSpec.ContractSpecPackage
 * @generated
 */
public class ContractSpecSwitch<T> extends Switch<T> {
	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static ContractSpecPackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @generated
	 */
	public ContractSpecSwitch() {
		if (modelPackage == null) {
			modelPackage = ContractSpecPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(final EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a
	 * non null result; it yields that result. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(final int classifierID, final EObject theEObject) {
		switch (classifierID) {
		case ContractSpecPackage.MODEL: {
			final Model model = (Model) theEObject;
			T result = caseModel(model);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.TIME_SPEC: {
			final TimeSpec timeSpec = (TimeSpec) theEObject;
			T result = caseTimeSpec(timeSpec);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.SINGLE_EVENT: {
			final SingleEvent singleEvent = (SingleEvent) theEObject;
			T result = caseSingleEvent(singleEvent);
			if (result == null) {
				result = caseTimeSpec(singleEvent);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.REPETITION: {
			final Repetition repetition = (Repetition) theEObject;
			T result = caseRepetition(repetition);
			if (result == null) {
				result = caseTimeSpec(repetition);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.REPETITION_OPTIONS: {
			final RepetitionOptions repetitionOptions = (RepetitionOptions) theEObject;
			T result = caseRepetitionOptions(repetitionOptions);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.JITTER: {
			final Jitter jitter = (Jitter) theEObject;
			T result = caseJitter(jitter);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.OFFSET: {
			final Offset offset = (Offset) theEObject;
			T result = caseOffset(offset);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.REACTION: {
			final Reaction reaction = (Reaction) theEObject;
			T result = caseReaction(reaction);
			if (result == null) {
				result = caseTimeSpec(reaction);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.AGE: {
			final Age age = (Age) theEObject;
			T result = caseAge(age);
			if (result == null) {
				result = caseTimeSpec(age);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.CAUSAL_REACTION: {
			final CausalReaction causalReaction = (CausalReaction) theEObject;
			T result = caseCausalReaction(causalReaction);
			if (result == null) {
				result = caseTimeSpec(causalReaction);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.CAUSAL_AGE: {
			final CausalAge causalAge = (CausalAge) theEObject;
			T result = caseCausalAge(causalAge);
			if (result == null) {
				result = caseTimeSpec(causalAge);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.EVENT_EXPR: {
			final EventExpr eventExpr = (EventExpr) theEObject;
			T result = caseEventExpr(eventExpr);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.EVENT_LIST: {
			final EventList eventList = (EventList) theEObject;
			T result = caseEventList(eventList);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.EVENT_SPEC: {
			final EventSpec eventSpec = (EventSpec) theEObject;
			T result = caseEventSpec(eventSpec);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.PORT: {
			final Port port = (Port) theEObject;
			T result = casePort(port);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.INTERVAL: {
			final Interval interval = (Interval) theEObject;
			T result = caseInterval(interval);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.TIME_EXPR: {
			final TimeExpr timeExpr = (TimeExpr) theEObject;
			T result = caseTimeExpr(timeExpr);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.VALUE: {
			final Value value = (Value) theEObject;
			T result = caseValue(value);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.CAUSAL_FUNC_DECL: {
			final CausalFuncDecl causalFuncDecl = (CausalFuncDecl) theEObject;
			T result = caseCausalFuncDecl(causalFuncDecl);
			if (result == null) {
				result = caseTimeSpec(causalFuncDecl);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractSpecPackage.CLOCK_DEFINITION: {
			final ClockDefinition clockDefinition = (ClockDefinition) theEObject;
			T result = caseClockDefinition(clockDefinition);
			if (result == null) {
				result = caseTimeSpec(clockDefinition);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Model</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModel(final Model object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Time
	 * Spec</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Time
	 *         Spec</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTimeSpec(final TimeSpec object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Single
	 * Event</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Single
	 *         Event</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSingleEvent(final SingleEvent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Repetition</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Repetition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRepetition(final Repetition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Repetition Options</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Repetition Options</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRepetitionOptions(final RepetitionOptions object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Jitter</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Jitter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseJitter(final Jitter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Offset</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Offset</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOffset(final Offset object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Reaction</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Reaction</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReaction(final Reaction object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Age</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Age</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAge(final Age object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Causal
	 * Reaction</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Causal
	 *         Reaction</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCausalReaction(final CausalReaction object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Causal
	 * Age</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Causal
	 *         Age</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCausalAge(final CausalAge object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Event
	 * Expr</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Event
	 *         Expr</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEventExpr(final EventExpr object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Event
	 * List</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Event
	 *         List</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEventList(final EventList object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Event
	 * Spec</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Event
	 *         Spec</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEventSpec(final EventSpec object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Port</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Port</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePort(final Port object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Interval</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Interval</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInterval(final Interval object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Time
	 * Expr</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Time
	 *         Expr</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTimeExpr(final TimeExpr object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Value</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseValue(final Value object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Causal
	 * Func Decl</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Causal
	 *         Func Decl</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCausalFuncDecl(final CausalFuncDecl object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Clock
	 * Definition</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Clock
	 *         Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseClockDefinition(final ClockDefinition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>EObject</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last
	 * case anyway. <!-- end-user-doc -->
	 *
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(final EObject object) {
		return null;
	}

} // ContractSpecSwitch
