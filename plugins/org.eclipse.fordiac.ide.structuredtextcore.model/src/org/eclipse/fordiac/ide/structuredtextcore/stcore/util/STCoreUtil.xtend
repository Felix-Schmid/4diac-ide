/*******************************************************************************
 * Copyright (c) 2022 - 2023 Martin Erich Jobst
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Martin Jobst - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.structuredtextcore.stcore.util

import java.lang.reflect.Method
import java.math.BigDecimal
import java.math.BigInteger
import java.text.MessageFormat
import java.util.List
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EObject
import org.eclipse.fordiac.ide.model.data.AnyBitType
import org.eclipse.fordiac.ide.model.data.AnyDurationType
import org.eclipse.fordiac.ide.model.data.AnyIntType
import org.eclipse.fordiac.ide.model.data.AnyMagnitudeType
import org.eclipse.fordiac.ide.model.data.AnyNumType
import org.eclipse.fordiac.ide.model.data.AnyRealType
import org.eclipse.fordiac.ide.model.data.AnyStringType
import org.eclipse.fordiac.ide.model.data.AnyUnsignedType
import org.eclipse.fordiac.ide.model.data.ArrayType
import org.eclipse.fordiac.ide.model.data.BoolType
import org.eclipse.fordiac.ide.model.data.ByteType
import org.eclipse.fordiac.ide.model.data.CharType
import org.eclipse.fordiac.ide.model.data.DataFactory
import org.eclipse.fordiac.ide.model.data.DataType
import org.eclipse.fordiac.ide.model.data.DateAndTimeType
import org.eclipse.fordiac.ide.model.data.DateType
import org.eclipse.fordiac.ide.model.data.DintType
import org.eclipse.fordiac.ide.model.data.DwordType
import org.eclipse.fordiac.ide.model.data.IntType
import org.eclipse.fordiac.ide.model.data.LdateType
import org.eclipse.fordiac.ide.model.data.LdtType
import org.eclipse.fordiac.ide.model.data.LintType
import org.eclipse.fordiac.ide.model.data.LrealType
import org.eclipse.fordiac.ide.model.data.LtodType
import org.eclipse.fordiac.ide.model.data.LwordType
import org.eclipse.fordiac.ide.model.data.RealType
import org.eclipse.fordiac.ide.model.data.SintType
import org.eclipse.fordiac.ide.model.data.StringType
import org.eclipse.fordiac.ide.model.data.Subrange
import org.eclipse.fordiac.ide.model.data.TimeOfDayType
import org.eclipse.fordiac.ide.model.data.UdintType
import org.eclipse.fordiac.ide.model.data.UintType
import org.eclipse.fordiac.ide.model.data.UlintType
import org.eclipse.fordiac.ide.model.data.UsintType
import org.eclipse.fordiac.ide.model.data.WcharType
import org.eclipse.fordiac.ide.model.data.WordType
import org.eclipse.fordiac.ide.model.data.WstringType
import org.eclipse.fordiac.ide.model.datatype.helper.IecTypes.ElementaryTypes
import org.eclipse.fordiac.ide.model.datatype.helper.IecTypes.GenericTypes
import org.eclipse.fordiac.ide.model.datatype.helper.TypeDeclarationParser
import org.eclipse.fordiac.ide.model.eval.function.Comment
import org.eclipse.fordiac.ide.model.eval.variable.Variable
import org.eclipse.fordiac.ide.model.helpers.ArraySizeHelper
import org.eclipse.fordiac.ide.model.libraryElement.AdapterDeclaration
import org.eclipse.fordiac.ide.model.libraryElement.FB
import org.eclipse.fordiac.ide.model.libraryElement.ICallable
import org.eclipse.fordiac.ide.model.libraryElement.INamedElement
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STArrayAccessExpression
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STArrayInitElement
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STArrayInitializerExpression
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STAssignmentStatement
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STBinaryExpression
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STBinaryOperator
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STCallArgument
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STCallNamedInputArgument
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STCallStatement
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STCallUnnamedArgument
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STCaseCases
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STCoreFactory
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STCorePackage
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STExpression
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STExpressionSource
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STFeatureExpression
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STForStatement
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STIfStatement
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STInitializerExpression
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STInitializerExpressionSource
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STMemberAccessExpression
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STNumericLiteral
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STRepeatStatement
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STStandardFunction
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STString
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STStructInitElement
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STStructInitializerExpression
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STUnaryExpression
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STUnaryOperator
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STVarDeclaration
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STWhileStatement

import static extension org.eclipse.emf.ecore.util.EcoreUtil.copy
import static extension org.eclipse.fordiac.ide.model.eval.function.Functions.*

final class STCoreUtil {
	public static final String OPTION_EXPECTED_TYPE = STCoreUtil.name + ".EXPECTED_TYPE"

	private new() {
	}

	def static boolean isArithmetic(STUnaryOperator operator) {
		switch (operator) {
			case PLUS,
			case MINUS: true
			default: false
		}
	}

	def static boolean isLogical(STUnaryOperator operator) {
		switch (operator) {
			case NOT: true
			default: false
		}
	}

	def static boolean isArithmetic(STBinaryOperator operator) {
		switch (operator) {
			case ADD,
			case SUB,
			case MUL,
			case DIV,
			case MOD,
			case POWER: true
			default: false
		}
	}

	def static boolean isComparison(STBinaryOperator operator) {
		switch (operator) {
			case EQ,
			case NE,
			case GE,
			case GT,
			case LE,
			case LT: true
			default: false
		}
	}

	def static boolean isLogical(STBinaryOperator operator) {
		switch (operator) {
			case AMPERSAND,
			case AND,
			case OR,
			case XOR: true
			default: false
		}
	}

	def static boolean isRange(STBinaryOperator operator) {
		operator == STBinaryOperator.RANGE
	}

	def static boolean isApplicableTo(STUnaryOperator operator, INamedElement type) {
		switch (operator) {
			case type.anyType: false
			case PLUS,
			case MINUS: type instanceof AnyMagnitudeType && !(type instanceof AnyUnsignedType)
			case NOT: type instanceof AnyBitType
		}
	}

	def static boolean isApplicableTo(STBinaryOperator operator, INamedElement first, INamedElement second) {
		switch (operator) {
			case first.anyType || second.anyType:
				false
			case ADD:
				(first instanceof AnyMagnitudeType && second instanceof AnyMagnitudeType) ||
					(first.instanceofAnyTimeOfDayType && second instanceof AnyDurationType) ||
					(first.instanceofAnyDateAndTimeType && second instanceof AnyDurationType)
			case SUB:
				(first instanceof AnyMagnitudeType && second instanceof AnyMagnitudeType) ||
					(first.instanceofAnyTimeOfDayType && second instanceof AnyDurationType) ||
					(first.instanceofAnyDateAndTimeType && second instanceof AnyDurationType) ||
					(first.instanceofAnySimpleDateType && second.instanceofAnySimpleDateType) ||
					(first.instanceofAnyDateAndTimeType && second.instanceofAnyDateAndTimeType) ||
					(first.instanceofAnyTimeOfDayType && second.instanceofAnyTimeOfDayType)
			case MUL,
			case DIV:
				first instanceof AnyMagnitudeType && second instanceof AnyNumType
			case MOD:
				first instanceof AnyIntType && second instanceof AnyIntType
			case POWER:
				first instanceof AnyRealType && second instanceof AnyNumType
			case AMPERSAND,
			case AND,
			case OR,
			case XOR:
				first instanceof AnyBitType && second instanceof AnyBitType
			case EQ,
			case NE,
			case GE,
			case GT,
			case LE,
			case LT,
			case RANGE:
				(first instanceof AnyNumType && second instanceof AnyNumType) ||
					(first instanceof AnyDurationType && second instanceof AnyDurationType) ||
					(first instanceof AnyBitType && second instanceof AnyBitType) ||
					(first instanceof DataType && second instanceof DataType &&
						((first as DataType).isAssignableFrom(second as DataType) ||
							(second as DataType).isAssignableFrom(first as DataType)))
		}
	}

	def static AccessMode getAccessMode(STExpression expression) {
		switch (container : expression.eContainer) {
			STCallStatement: AccessMode.NONE
			STAssignmentStatement case container.left == expression: AccessMode.WRITE
			STMemberAccessExpression case container.member == expression: container.accessMode
			STArrayAccessExpression case container.receiver == expression: container.accessMode
			STCallArgument: container.accessMode
			default: AccessMode.READ
		}
	}

	def static AccessMode getAccessMode(STCallArgument argument) {
		switch (container : argument.eContainer) {
			STFeatureExpression case container.mappedInputArguments.containsValue(argument): AccessMode.READ
			STFeatureExpression case container.mappedOutputArguments.containsValue(argument): AccessMode.WRITE
			STFeatureExpression case container.mappedInOutArguments.containsValue(argument): AccessMode.READ_WRITE
			default: AccessMode.NONE
		}
	}

	def static boolean isNumericValueValid(DataType type, Object value) {
		switch (value) {
			Boolean:
				type instanceof BoolType
			BigDecimal:
				switch (type) {
					RealType: Float.isFinite(value.floatValue)
					LrealType: Double.isFinite(value.doubleValue)
					default: false
				}
			BigInteger:
				switch (type) {
					SintType: value.checkRange(Byte.MIN_VALUE, Byte.MAX_VALUE)
					IntType: value.checkRange(Short.MIN_VALUE, Short.MAX_VALUE)
					DintType: value.checkRange(Integer.MIN_VALUE, Integer.MAX_VALUE)
					LintType: value.checkRange(Long.MIN_VALUE, Long.MAX_VALUE)
					UsintType: value.checkRangeUnsigned(0xff#bi)
					UintType: value.checkRangeUnsigned(0xffff#bi)
					UdintType: value.checkRangeUnsigned(0xffffffff#bi)
					UlintType: value.checkRangeUnsigned(0xffffffffffffffff#bi)
					BoolType: value.checkRangeUnsigned(1bi)
					ByteType: value.checkRangeUnsigned(0xff#bi)
					WordType: value.checkRangeUnsigned(0xffff#bi)
					DwordType: value.checkRangeUnsigned(0xffffffff#bi)
					LwordType: value.checkRangeUnsigned(0xffffffffffffffff#bi)
					default: false
				}
			default:
				false
		}
	}

	def static boolean isStringValueValid(DataType type, STString value) {
		switch (type) {
			CharType: !value.wide && value.length === 1
			StringType: !value.wide
			WcharType: value.wide && value.length === 1
			WstringType: value.wide
			default: false
		}
	}

	def static checkRange(BigInteger value, long lower, long upper) {
		value >= BigInteger.valueOf(lower) && value <= BigInteger.valueOf(upper)
	}

	def static checkRangeUnsigned(BigInteger value, BigInteger upper) {
		value.signum >= 0 && value <= upper
	}

	def static INamedElement getExpectedType(STExpression expression) {
		switch (it : expression.eContainer) {
			STUnaryExpression case op.arithmetic:
				expectedType.equivalentAnyNumType
			STUnaryExpression case op.logical:
				expectedType.equivalentAnyBitType
			STBinaryExpression case op.arithmetic || op.range:
				(expression === left ? right?.declaredResultType : left.declaredResultType).equivalentAnyNumType ?:
					expectedType.equivalentAnyNumType
			STBinaryExpression case op.logical:
				(expression === left ? right?.declaredResultType : left.declaredResultType).equivalentAnyBitType ?:
					expectedType.equivalentAnyBitType
			STBinaryExpression case op.comparison:
				expression === left ? right?.declaredResultType : left.declaredResultType
			STAssignmentStatement:
				expression === left ? right.declaredResultType : left.declaredResultType
			STIfStatement,
			STWhileStatement,
			STRepeatStatement:
				ElementaryTypes.BOOL
			STForStatement:
				variable.type
			STCaseCases:
				statement.selector.declaredResultType
			STInitializerExpression:
				expectedType
			STArrayInitElement:
				if (initExpressions.empty || expression !== indexOrInitExpression)
					expectedType
				else
					ElementaryTypes.INT
			STStructInitElement:
				variable.featureType
			STCallNamedInputArgument:
				// get parameter (target) but don't resolve
				switch (parameter: parameterNoresolve) {
					VarDeclaration: parameter.type
					STVarDeclaration: parameter.type
				}
			STCallUnnamedArgument:
				// get parameter (target) but don't resolve
				switch (parameter: parameterNoresolve) {
					VarDeclaration: parameter.type
					STVarDeclaration: parameter.type
				}
			STExpressionSource:
				eResource?.resourceSet?.loadOptions?.get(OPTION_EXPECTED_TYPE) as INamedElement
		}
	}

	def static INamedElement getExpectedType(STInitializerExpression expression) {
		switch (it : expression.eContainer) {
			STVarDeclaration:
				featureType
			STArrayInitElement case initExpressions.empty || initExpressions.contains(expression):
				expectedType
			STStructInitElement:
				variable.featureType
			STArrayInitializerExpression:
				expectedType
			STStructInitializerExpression:
				expectedType
			STInitializerExpressionSource:
				eResource?.resourceSet?.loadOptions?.get(OPTION_EXPECTED_TYPE) as INamedElement
		}
	}

	def static INamedElement getExpectedType(STArrayInitElement initElement) {
		switch (it : initElement.eContainer) {
			STArrayInitializerExpression:
				switch (type : expectedType) {
					ArrayType:
						if (type.subranges.size > 1) // not consumed all dimensions
							type.baseType.newArrayType(type.subranges.tail.map[copy])
						else // consumed all dimensions
							type.baseType
				}
		}
	}

	def package static getParameterNoresolve(STCallNamedInputArgument argument) {
		switch (target : argument.eGet(STCorePackage.eINSTANCE.STCallNamedInputArgument_Parameter, false)) {
			INamedElement case !target.eIsProxy: target
		}
	}

	def package static getParameterNoresolve(STCallUnnamedArgument argument) {
		val featureExpression = argument.featureExpression
		if (featureExpression !== null) {
			val index = featureExpression.parameters.indexOf(argument)
			if (index >= 0) {
				val feature = featureExpression.featureNoresolve
				if (feature instanceof ICallable) {
					val inputParameters = feature.inputParameters
					val outputParameters = feature.outputParameters
					val inOutParameters = feature.inOutParameters
					if (index < inputParameters.size)
						inputParameters.get(index)
					else if (feature.callableVarargs)
						inputParameters.last
					else if (index < inputParameters.size + inOutParameters.size)
						inOutParameters.get(index - inputParameters.size)
					else if (index < inputParameters.size + inOutParameters.size + outputParameters.size)
						outputParameters.get(index - inputParameters.size - inOutParameters.size)
				}
			}
		}
	}

	def package static getFeatureNoresolve(STFeatureExpression expr) {
		switch (feature : expr.eGet(STCorePackage.eINSTANCE.STFeatureExpression_Feature, false)) {
			INamedElement case !feature.eIsProxy: feature
		}
	}

	def package static getFeatureExpression(STCallUnnamedArgument argument) {
		switch (container: argument.eContainer) { STFeatureExpression: container }
	}

	def static List<? extends INamedElement> computeInputParameters(ICallable callable,
		Iterable<STCallArgument> arguments) {
		if (callable instanceof STStandardFunction)
			callable.javaMethod.inferParameterVariables(arguments.map[resultType].filter(DataType).toList, true)
		else
			callable.inputParameters
	}

	def static List<? extends INamedElement> computeOutputParameters(ICallable callable,
		Iterable<STCallArgument> arguments) {
		if (callable instanceof STStandardFunction)
			callable.javaMethod.inferParameterVariables(arguments.map[resultType].filter(DataType).toList, false)
		else
			callable.outputParameters
	}

	def static List<? extends INamedElement> computeInOutParameters(ICallable callable,
		Iterable<STCallArgument> arguments) {
		callable.inOutParameters
	}

	def static boolean isCallableVarargs(ICallable callable) {
		switch (callable) {
			STStandardFunction: callable.varargs
			default: false
		}
	}

	def package static List<STVarDeclaration> inferParameterVariables(Method method, List<DataType> argumentTypes,
		boolean input) {
		val ptypes = method.inferParameterTypesFromDataTypes(argumentTypes)
		(0 ..< ptypes.size).map [ index |
			if (input.xor(method.getParameterType(index) == Variable)) {
				STCoreFactory.eINSTANCE.createSTVarDeclaration => [
					name = '''«IF input»IN«ELSE»OUT«ENDIF»«index»'''
					comment = MessageFormat.format(method.getParameter(index)?.getAnnotation(Comment)?.value ?: "",
						index)
					type = ptypes.get(index)
				]
			}
		].filterNull.toList
	}

	def static getFeatureType(INamedElement feature) {
		switch (feature) {
			VarDeclaration:
				if (feature.array)
					TypeDeclarationParser.parseTypeDeclaration(feature.type, ArraySizeHelper.getArraySize(feature))
				else
					feature.type
			STVarDeclaration: {
				val type = switch (type: feature.type) {
					AnyStringType case feature.maxLength instanceof STNumericLiteral:
						type.newStringType(feature.maxLength.asConstantInt)
					DataType:
						type
				}
				if (feature.array)
					type.newArrayType(
						if (feature.ranges.empty)
							feature.count.map[DataFactory.eINSTANCE.createSubrange]
						else
							feature.ranges.map[toSubrange]
					)
				else
					type
			}
			AdapterDeclaration:
				feature.adapterFB?.type
			FB:
				feature.type
			default:
				null
		}
	}

	def static ArrayType newArrayType(DataType arrayBaseType, Subrange... arraySubranges) {
		arrayBaseType.newArrayType(arraySubranges as Iterable<Subrange>)
	}

	def static ArrayType newArrayType(DataType arrayBaseType, Iterable<Subrange> arraySubranges) {
		if (arrayBaseType !== null)
			DataFactory.eINSTANCE.createArrayType => [
				name = '''ARRAY [«arraySubranges.map['''«IF setLowerLimit && setUpperLimit»«lowerLimit»..«upperLimit»«ELSE»*«ENDIF»'''].join(", ")»] OF «arrayBaseType.name»'''
				baseType = arrayBaseType
				subranges.addAll(arraySubranges)
			]
		else
			null
	}

	def static Subrange toSubrange(STExpression expr) {
		try {
			switch (expr) {
				STBinaryExpression case expr.op === STBinaryOperator.RANGE:
					newSubrange(expr.left.asConstantInt, expr.right.asConstantInt)
				default:
					newSubrange(0, expr.asConstantInt - 1)
			}
		} catch (ArithmeticException e) {
			DataFactory.eINSTANCE.createSubrange
		}
	}

	def static AnyStringType newStringType(AnyStringType template, int maxLengthValue) {
		if (template !== null)
			DataFactory.eINSTANCE.create(template.eClass) as AnyStringType => [
				name = '''«template.name»[«maxLengthValue»]'''
				maxLength = maxLengthValue
			]
		else
			null
	}

	def static int asConstantInt(STExpression expr) {
		switch (expr) {
			STNumericLiteral: (expr.value as BigInteger).intValueExact
			default: throw new ArithmeticException("Not a constant integer")
		}
	}

	def static newSubrange(int lower, int upper) {
		DataFactory.eINSTANCE.createSubrange => [
			lowerLimit = lower
			upperLimit = upper
		]
	}

	def static getEquivalentAnyNumType(INamedElement type) {
		switch (type) {
			AnyNumType: type
			BoolType: ElementaryTypes.SINT
			ByteType: ElementaryTypes.USINT
			WordType: ElementaryTypes.UINT
			DwordType: ElementaryTypes.UDINT
			LwordType: ElementaryTypes.ULINT
			default: null
		}
	}

	def static getEquivalentAnyBitType(INamedElement type) {
		switch (type) {
			AnyBitType: type
			SintType,
			UsintType: ElementaryTypes.BYTE
			IntType,
			UintType: ElementaryTypes.WORD
			DintType,
			UdintType: ElementaryTypes.DWORD
			LintType,
			UlintType: ElementaryTypes.LWORD
			default: null
		}
	}

	def static getEquivalentAnyLDateType(INamedElement type) {
		switch (type) {
			case type.instanceofAnyLDateType: type
			DateType: ElementaryTypes.LDATE
			TimeOfDayType: ElementaryTypes.LTOD
			DateAndTimeType: ElementaryTypes.LDT
			default: null
		}
	}

	def static boolean isInstanceofAnySimpleDateType(INamedElement type) {
		return type instanceof DateType || type instanceof LdateType
	}

	def static boolean isInstanceofAnyTimeOfDayType(INamedElement type) {
		return type instanceof TimeOfDayType || type instanceof LtodType
	}

	def static boolean isInstanceofAnyDateAndTimeType(INamedElement type) {
		return type instanceof DateAndTimeType || type instanceof LdtType
	}

	def static boolean isInstanceofAnySDateType(INamedElement type) {
		return type instanceof DateType || type instanceof TimeOfDayType || type instanceof DateAndTimeType
	}

	def static boolean isInstanceofAnyLDateType(INamedElement type) {
		return type instanceof LdateType || type instanceof LtodType || type instanceof LdtType
	}

	def static boolean isAnyType(INamedElement element) {
		switch (element) {
			DataType: GenericTypes.isAnyType(element)
			default: false
		}
	}

	def static EObject getVariableScope(STVarDeclaration decl) {
		decl?.eContainer?.eContainer
	}

	def static boolean isAncestor(EClassifier clazz, EObject object) {
		if (object === null)
			false
		else if (clazz.isInstance(object))
			true
		else
			clazz.isAncestor(object.eContainer)
	}
}
