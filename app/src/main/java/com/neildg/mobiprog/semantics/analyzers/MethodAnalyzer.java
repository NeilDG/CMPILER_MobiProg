/**
 * 
 */
package com.neildg.mobiprog.semantics.analyzers;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.neildg.mobiprog.builder.errorcheckers.MultipleFuncDecChecker;
import com.neildg.mobiprog.execution.ExecutionManager;
import com.neildg.mobiprog.generatedexp.JavaParser.BlockContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ClassOrInterfaceTypeContext;
import com.neildg.mobiprog.generatedexp.JavaParser.FormalParametersContext;
import com.neildg.mobiprog.generatedexp.JavaParser.MethodBodyContext;
import com.neildg.mobiprog.generatedexp.JavaParser.MethodDeclarationContext;
import com.neildg.mobiprog.generatedexp.JavaParser.PrimitiveTypeContext;
import com.neildg.mobiprog.generatedexp.JavaParser.TypeContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.semantics.representations.MobiFunction;
import com.neildg.mobiprog.semantics.representations.MobiFunction.FunctionType;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;
import com.neildg.mobiprog.semantics.symboltable.scopes.LocalScopeCreator;
import com.neildg.mobiprog.semantics.utils.IdentifiedTokens;
import com.neildg.mobiprog.semantics.utils.RecognizedKeywords;

/**
 * Analyzes method declarations and properly stores them in the symbol table
 * @author NeilDG
 *
 */
public class MethodAnalyzer implements ParseTreeListener {
	private final static String TAG = "MobiProg_MethodAnalyzer";
	
	private ClassScope declaredClassScope;
	private IdentifiedTokens identifiedTokens;
	private MobiFunction declaredMobiFunction;
	
	public MethodAnalyzer(IdentifiedTokens identifiedTokens, ClassScope declaredClassScope) {
		this.identifiedTokens = identifiedTokens;
		this.declaredClassScope = declaredClassScope;
		this.declaredMobiFunction = new MobiFunction();
	}
	
	public void analyze(MethodDeclarationContext ctx) {
		ExecutionManager.getInstance().openFunctionExecution(this.declaredMobiFunction);
		
		ParseTreeWalker treeWalker = new ParseTreeWalker();
		treeWalker.walk(this, ctx);
	}
	
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
		if(ctx instanceof MethodDeclarationContext) {
			MethodDeclarationContext methodDecCtx = (MethodDeclarationContext) ctx;
			MultipleFuncDecChecker funcDecChecker = new MultipleFuncDecChecker(methodDecCtx);
			funcDecChecker.verify();
			
			this.analyzeIdentifier(methodDecCtx.Identifier()); //get the function identifier
		}
		else {
			this.analyzeMethod(ctx);
		}
		
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		if(ctx instanceof MethodDeclarationContext) {
			ExecutionManager.getInstance().closeFunctionExecution();
		}
	}
	
	private void analyzeMethod(ParserRuleContext ctx) {
		
		if(ctx instanceof TypeContext) {
			TypeContext typeCtx = (TypeContext) ctx;
			
			//return type is a primitive type
			if(typeCtx.primitiveType() != null) {
				PrimitiveTypeContext primitiveTypeCtx = typeCtx.primitiveType();
				this.declaredMobiFunction.setReturnType(MobiFunction.identifyFunctionType(primitiveTypeCtx.getText()));
			}
			//return type is a string or a class type
			else {
				this.analyzeClassOrInterfaceType(typeCtx.classOrInterfaceType());
			}
		}
		
		else if(ctx instanceof FormalParametersContext) {
			FormalParametersContext formalParamsCtx = (FormalParametersContext) ctx;
			this.analyzeParameters(formalParamsCtx);
			this.storeMobiFunction();
		}
		
		else if(ctx instanceof MethodBodyContext) {
			
			BlockContext blockCtx = ((MethodBodyContext) ctx).block();
			
			BlockAnalyzer blockAnalyzer = new BlockAnalyzer();
			this.declaredMobiFunction.setParentLocalScope(LocalScopeCreator.getInstance().getActiveLocalScope());
			blockAnalyzer.analyze(blockCtx);
			
		}
		
	}
	
	private void analyzeClassOrInterfaceType(ClassOrInterfaceTypeContext classOrInterfaceCtx) {
		//a string identified
		if(classOrInterfaceCtx.getText().contains(RecognizedKeywords.PRIMITIVE_TYPE_STRING)) {
			this.declaredMobiFunction.setReturnType(FunctionType.STRING_TYPE);
		}
		//a class identified
		else {
			Console.log(LogType.DEBUG, "Class identified: " + classOrInterfaceCtx.getText());
		}
	}
	
	private void analyzeIdentifier(TerminalNode identifier) {
		this.declaredMobiFunction.setFunctionName(identifier.getText());
	}
	
	private void analyzeParameters(FormalParametersContext formalParamsCtx) {
		if(formalParamsCtx.formalParameterList() != null) {
			ParameterAnalyzer parameterAnalyzer = new ParameterAnalyzer(this.declaredMobiFunction);
			parameterAnalyzer.analyze(formalParamsCtx.formalParameterList());
		}
	}
	
	/*
	 * Stores the created function in its corresponding class scope
	 */
	private void storeMobiFunction() {
		if(this.identifiedTokens.containsTokens(ClassAnalyzer.ACCESS_CONTROL_KEY)) {
			String accessToken = this.identifiedTokens.getToken(ClassAnalyzer.ACCESS_CONTROL_KEY);
			
			if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.CLASS_MODIFIER_PRIVATE, accessToken)) {
				this.declaredClassScope.addPrivateMobiFunction(this.declaredMobiFunction.getFunctionName(), this.declaredMobiFunction);
			}
			else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.CLASS_MODIFIER_PUBLIC, accessToken)) {
				this.declaredClassScope.addPublicMobiFunction(this.declaredMobiFunction.getFunctionName(), this.declaredMobiFunction);
			}
			
			this.identifiedTokens.clearTokens(); //clear tokens for reuse
		}
	}

}
