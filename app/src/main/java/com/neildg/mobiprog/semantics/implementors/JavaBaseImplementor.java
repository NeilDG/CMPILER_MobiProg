/**
 * 
 */
package com.neildg.mobiprog.semantics.implementors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import android.util.Log;

import com.neildg.mobiprog.generatedexp.*;
import com.neildg.mobiprog.generatedexp.JavaParser.AnnotationConstantRestContext;
import com.neildg.mobiprog.generatedexp.JavaParser.AnnotationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.AnnotationMethodOrConstantRestContext;
import com.neildg.mobiprog.generatedexp.JavaParser.AnnotationMethodRestContext;
import com.neildg.mobiprog.generatedexp.JavaParser.AnnotationNameContext;
import com.neildg.mobiprog.generatedexp.JavaParser.AnnotationTypeBodyContext;
import com.neildg.mobiprog.generatedexp.JavaParser.AnnotationTypeDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.AnnotationTypeElementDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.AnnotationTypeElementRestContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ArgumentsContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ArrayCreatorRestContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ArrayInitializerContext;
import com.neildg.mobiprog.generatedexp.JavaParser.BlockContext;
import com.neildg.mobiprog.generatedexp.JavaParser.BlockStatementContext;
import com.neildg.mobiprog.generatedexp.JavaParser.CatchClauseContext;
import com.neildg.mobiprog.generatedexp.JavaParser.CatchTypeContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ClassBodyContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ClassBodyDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ClassCreatorRestContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ClassDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ClassOrInterfaceModifierContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ClassOrInterfaceTypeContext;
import com.neildg.mobiprog.generatedexp.JavaParser.CompilationUnitContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ConstDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ConstantDeclaratorContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ConstantExpressionContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ConstructorBodyContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ConstructorDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.CreatedNameContext;
import com.neildg.mobiprog.generatedexp.JavaParser.CreatorContext;
import com.neildg.mobiprog.generatedexp.JavaParser.DefaultValueContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ElementValueArrayInitializerContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ElementValueContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ElementValuePairContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ElementValuePairsContext;
import com.neildg.mobiprog.generatedexp.JavaParser.EnhancedForControlContext;
import com.neildg.mobiprog.generatedexp.JavaParser.EnumBodyDeclarationsContext;
import com.neildg.mobiprog.generatedexp.JavaParser.EnumConstantContext;
import com.neildg.mobiprog.generatedexp.JavaParser.EnumConstantNameContext;
import com.neildg.mobiprog.generatedexp.JavaParser.EnumConstantsContext;
import com.neildg.mobiprog.generatedexp.JavaParser.EnumDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ExplicitGenericInvocationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ExplicitGenericInvocationSuffixContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionListContext;
import com.neildg.mobiprog.generatedexp.JavaParser.FieldDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.FinallyBlockContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ForControlContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ForInitContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ForUpdateContext;
import com.neildg.mobiprog.generatedexp.JavaParser.FormalParameterContext;
import com.neildg.mobiprog.generatedexp.JavaParser.FormalParameterListContext;
import com.neildg.mobiprog.generatedexp.JavaParser.FormalParametersContext;
import com.neildg.mobiprog.generatedexp.JavaParser.GenericConstructorDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.GenericInterfaceMethodDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.GenericMethodDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ImportDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.InnerCreatorContext;
import com.neildg.mobiprog.generatedexp.JavaParser.InterfaceBodyContext;
import com.neildg.mobiprog.generatedexp.JavaParser.InterfaceBodyDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.InterfaceDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.InterfaceMemberDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.InterfaceMethodDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.LastFormalParameterContext;
import com.neildg.mobiprog.generatedexp.JavaParser.LiteralContext;
import com.neildg.mobiprog.generatedexp.JavaParser.LocalVariableDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.LocalVariableDeclarationStatementContext;
import com.neildg.mobiprog.generatedexp.JavaParser.MainDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.MemberDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.MethodBodyContext;
import com.neildg.mobiprog.generatedexp.JavaParser.MethodDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ModifierContext;
import com.neildg.mobiprog.generatedexp.JavaParser.NonWildcardTypeArgumentsContext;
import com.neildg.mobiprog.generatedexp.JavaParser.NonWildcardTypeArgumentsOrDiamondContext;
import com.neildg.mobiprog.generatedexp.JavaParser.PackageDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ParExpressionContext;
import com.neildg.mobiprog.generatedexp.JavaParser.PrimaryContext;
import com.neildg.mobiprog.generatedexp.JavaParser.PrimitiveTypeContext;
import com.neildg.mobiprog.generatedexp.JavaParser.QualifiedNameContext;
import com.neildg.mobiprog.generatedexp.JavaParser.QualifiedNameListContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ResourceContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ResourceSpecificationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ResourcesContext;
import com.neildg.mobiprog.generatedexp.JavaParser.StatementContext;
import com.neildg.mobiprog.generatedexp.JavaParser.StatementExpressionContext;
import com.neildg.mobiprog.generatedexp.JavaParser.SuperSuffixContext;
import com.neildg.mobiprog.generatedexp.JavaParser.SwitchBlockStatementGroupContext;
import com.neildg.mobiprog.generatedexp.JavaParser.SwitchLabelContext;
import com.neildg.mobiprog.generatedexp.JavaParser.TypeArgumentContext;
import com.neildg.mobiprog.generatedexp.JavaParser.TypeArgumentsContext;
import com.neildg.mobiprog.generatedexp.JavaParser.TypeArgumentsOrDiamondContext;
import com.neildg.mobiprog.generatedexp.JavaParser.TypeBoundContext;
import com.neildg.mobiprog.generatedexp.JavaParser.TypeContext;
import com.neildg.mobiprog.generatedexp.JavaParser.TypeDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.TypeListContext;
import com.neildg.mobiprog.generatedexp.JavaParser.TypeParameterContext;
import com.neildg.mobiprog.generatedexp.JavaParser.TypeParametersContext;
import com.neildg.mobiprog.generatedexp.JavaParser.VariableDeclaratorContext;
import com.neildg.mobiprog.generatedexp.JavaParser.VariableDeclaratorIdContext;
import com.neildg.mobiprog.generatedexp.JavaParser.VariableDeclaratorsContext;
import com.neildg.mobiprog.generatedexp.JavaParser.VariableInitializerContext;
import com.neildg.mobiprog.generatedexp.JavaParser.VariableModifierContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.semantics.analyzers.ClassAnalyzer;
import com.neildg.mobiprog.semantics.analyzers.MainAnalyzer;
import com.neildg.mobiprog.semantics.analyzers.MethodAnalyzer;
import com.neildg.mobiprog.semantics.analyzers.StatementAnalyzer;

/**
 * Contains all the required methods from the Java listeners that passes its parameters
 * to corresponding child implementor delegates.
 * @author Patrick
 *
 */
public class JavaBaseImplementor implements JavaListener {

	private final static String TAG = "MobiProg_JavaBaseImplementor";
	
	
	@Override
	public void visitTerminal(TerminalNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitErrorNode(ErrorNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterInnerCreator(InnerCreatorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitInnerCreator(InnerCreatorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterGenericMethodDeclaration(
			GenericMethodDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitGenericMethodDeclaration(GenericMethodDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterExpressionList(ExpressionListContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitExpressionList(ExpressionListContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTypeDeclaration(TypeDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTypeDeclaration(TypeDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterForUpdate(ForUpdateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitForUpdate(ForUpdateContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAnnotation(AnnotationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAnnotation(AnnotationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterEnumConstant(EnumConstantContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEnumConstant(EnumConstantContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterImportDeclaration(ImportDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitImportDeclaration(ImportDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAnnotationMethodOrConstantRest(
			AnnotationMethodOrConstantRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAnnotationMethodOrConstantRest(
			AnnotationMethodOrConstantRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterEnumConstantName(EnumConstantNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEnumConstantName(EnumConstantNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFinallyBlock(FinallyBlockContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFinallyBlock(FinallyBlockContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterVariableDeclarators(VariableDeclaratorsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitVariableDeclarators(VariableDeclaratorsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterElementValuePairs(ElementValuePairsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitElementValuePairs(ElementValuePairsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterInterfaceMethodDeclaration(
			InterfaceMethodDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitInterfaceMethodDeclaration(
			InterfaceMethodDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterInterfaceBodyDeclaration(
			InterfaceBodyDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitInterfaceBodyDeclaration(InterfaceBodyDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterEnumConstants(EnumConstantsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEnumConstants(EnumConstantsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCatchClause(CatchClauseContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCatchClause(CatchClauseContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterConstantExpression(ConstantExpressionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitConstantExpression(ConstantExpressionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterEnumDeclaration(EnumDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEnumDeclaration(EnumDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterExplicitGenericInvocationSuffix(
			ExplicitGenericInvocationSuffixContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitExplicitGenericInvocationSuffix(
			ExplicitGenericInvocationSuffixContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTypeParameter(TypeParameterContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTypeParameter(TypeParameterContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterEnumBodyDeclarations(EnumBodyDeclarationsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEnumBodyDeclarations(EnumBodyDeclarationsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTypeBound(TypeBoundContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTypeBound(TypeBoundContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterStatementExpression(StatementExpressionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitStatementExpression(StatementExpressionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterVariableInitializer(VariableInitializerContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitVariableInitializer(VariableInitializerContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterBlock(BlockContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitBlock(BlockContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterGenericInterfaceMethodDeclaration(
			GenericInterfaceMethodDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitGenericInterfaceMethodDeclaration(
			GenericInterfaceMethodDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterLocalVariableDeclarationStatement(
			LocalVariableDeclarationStatementContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitLocalVariableDeclarationStatement(
			LocalVariableDeclarationStatementContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSuperSuffix(SuperSuffixContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSuperSuffix(SuperSuffixContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFieldDeclaration(FieldDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFieldDeclaration(FieldDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFormalParameterList(FormalParameterListContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFormalParameterList(FormalParameterListContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterExplicitGenericInvocation(
			ExplicitGenericInvocationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitExplicitGenericInvocation(
			ExplicitGenericInvocationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterParExpression(ParExpressionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitParExpression(ParExpressionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSwitchLabel(SwitchLabelContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSwitchLabel(SwitchLabelContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTypeParameters(TypeParametersContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTypeParameters(TypeParametersContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterQualifiedName(QualifiedNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitQualifiedName(QualifiedNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterClassDeclaration(ClassDeclarationContext ctx) {
		ClassAnalyzer classAnalyzer = new ClassAnalyzer();
		classAnalyzer.analyze(ctx);
	}

	@Override
	public void exitClassDeclaration(ClassDeclarationContext ctx) {
		
	}

	@Override
	public void enterAnnotationConstantRest(AnnotationConstantRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAnnotationConstantRest(AnnotationConstantRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterArguments(ArgumentsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitArguments(ArgumentsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterConstructorBody(ConstructorBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitConstructorBody(ConstructorBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFormalParameters(FormalParametersContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFormalParameters(FormalParametersContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTypeArgument(TypeArgumentContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTypeArgument(TypeArgumentContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterForInit(ForInitContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitForInit(ForInitContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterVariableDeclarator(VariableDeclaratorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitVariableDeclarator(VariableDeclaratorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAnnotationTypeDeclaration(
			AnnotationTypeDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAnnotationTypeDeclaration(
			AnnotationTypeDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterExpression(ExpressionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitExpression(ExpressionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterResources(ResourcesContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitResources(ResourcesContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFormalParameter(FormalParameterContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFormalParameter(FormalParameterContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterType(TypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitType(TypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterElementValueArrayInitializer(
			ElementValueArrayInitializerContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitElementValueArrayInitializer(
			ElementValueArrayInitializerContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAnnotationName(AnnotationNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAnnotationName(AnnotationNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterEnhancedForControl(EnhancedForControlContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEnhancedForControl(EnhancedForControlContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAnnotationMethodRest(AnnotationMethodRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAnnotationMethodRest(AnnotationMethodRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterPrimary(PrimaryContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitPrimary(PrimaryContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterClassBody(ClassBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitClassBody(ClassBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterClassOrInterfaceModifier(
			ClassOrInterfaceModifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitClassOrInterfaceModifier(ClassOrInterfaceModifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterDefaultValue(DefaultValueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitDefaultValue(DefaultValueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterVariableModifier(VariableModifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitVariableModifier(VariableModifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterConstDeclaration(ConstDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitConstDeclaration(ConstDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCreatedName(CreatedNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCreatedName(CreatedNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterInterfaceDeclaration(InterfaceDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitInterfaceDeclaration(InterfaceDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterPackageDeclaration(PackageDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitPackageDeclaration(PackageDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterConstantDeclarator(ConstantDeclaratorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitConstantDeclarator(ConstantDeclaratorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCatchType(CatchTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCatchType(CatchTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTypeArguments(TypeArgumentsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTypeArguments(TypeArgumentsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterClassCreatorRest(ClassCreatorRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitClassCreatorRest(ClassCreatorRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterModifier(ModifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitModifier(ModifierContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterStatement(StatementContext ctx) {
		
	}

	@Override
	public void exitStatement(StatementContext ctx) {
		
	}

	@Override
	public void enterInterfaceBody(InterfaceBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitInterfaceBody(InterfaceBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterClassBodyDeclaration(ClassBodyDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitClassBodyDeclaration(ClassBodyDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterLastFormalParameter(LastFormalParameterContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitLastFormalParameter(LastFormalParameterContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterForControl(ForControlContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitForControl(ForControlContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTypeList(TypeListContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTypeList(TypeListContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterLocalVariableDeclaration(
			LocalVariableDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitLocalVariableDeclaration(LocalVariableDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterVariableDeclaratorId(VariableDeclaratorIdContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitVariableDeclaratorId(VariableDeclaratorIdContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCompilationUnit(CompilationUnitContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCompilationUnit(CompilationUnitContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterElementValue(ElementValueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitElementValue(ElementValueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterClassOrInterfaceType(ClassOrInterfaceTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitClassOrInterfaceType(ClassOrInterfaceTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTypeArgumentsOrDiamond(TypeArgumentsOrDiamondContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTypeArgumentsOrDiamond(TypeArgumentsOrDiamondContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAnnotationTypeElementDeclaration(
			AnnotationTypeElementDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAnnotationTypeElementDeclaration(
			AnnotationTypeElementDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterBlockStatement(BlockStatementContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitBlockStatement(BlockStatementContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAnnotationTypeBody(AnnotationTypeBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAnnotationTypeBody(AnnotationTypeBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterQualifiedNameList(QualifiedNameListContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitQualifiedNameList(QualifiedNameListContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCreator(CreatorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCreator(CreatorContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterMemberDeclaration(MemberDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitMemberDeclaration(MemberDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterMethodDeclaration(MethodDeclarationContext ctx) {
		
	}

	@Override
	public void exitMethodDeclaration(MethodDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAnnotationTypeElementRest(
			AnnotationTypeElementRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAnnotationTypeElementRest(
			AnnotationTypeElementRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterResourceSpecification(ResourceSpecificationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitResourceSpecification(ResourceSpecificationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterConstructorDeclaration(ConstructorDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitConstructorDeclaration(ConstructorDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterResource(ResourceContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitResource(ResourceContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterElementValuePair(ElementValuePairContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitElementValuePair(ElementValuePairContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterMethodBody(MethodBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitMethodBody(MethodBodyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterArrayInitializer(ArrayInitializerContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitArrayInitializer(ArrayInitializerContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterNonWildcardTypeArgumentsOrDiamond(
			NonWildcardTypeArgumentsOrDiamondContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitNonWildcardTypeArgumentsOrDiamond(
			NonWildcardTypeArgumentsOrDiamondContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterPrimitiveType(PrimitiveTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitPrimitiveType(PrimitiveTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterNonWildcardTypeArguments(
			NonWildcardTypeArgumentsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitNonWildcardTypeArguments(NonWildcardTypeArgumentsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterArrayCreatorRest(ArrayCreatorRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitArrayCreatorRest(ArrayCreatorRestContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterInterfaceMemberDeclaration(
			InterfaceMemberDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitInterfaceMemberDeclaration(
			InterfaceMemberDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterGenericConstructorDeclaration(
			GenericConstructorDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitGenericConstructorDeclaration(
			GenericConstructorDeclarationContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterLiteral(LiteralContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitLiteral(LiteralContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSwitchBlockStatementGroup(
			SwitchBlockStatementGroupContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSwitchBlockStatementGroup(
			SwitchBlockStatementGroupContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterMainDeclaration(MainDeclarationContext ctx) {
		MainAnalyzer mainAnalyzer = new MainAnalyzer();
		mainAnalyzer.analyze(ctx);
	}

	@Override
	public void exitMainDeclaration(MainDeclarationContext ctx) {

	}

}
