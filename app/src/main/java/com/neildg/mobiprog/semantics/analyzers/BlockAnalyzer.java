/**
 * 
 */
package com.neildg.mobiprog.semantics.analyzers;

import java.util.List;

import com.neildg.mobiprog.generatedexp.JavaParser.BlockContext;
import com.neildg.mobiprog.generatedexp.JavaParser.BlockStatementContext;
import com.neildg.mobiprog.generatedexp.JavaParser.LocalVariableDeclarationStatementContext;
import com.neildg.mobiprog.generatedexp.JavaParser.StatementContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.semantics.symboltable.scopes.LocalScopeCreator;

/**
 * Analyzes a statement block
 * @author NeilDG
 *
 */
public class BlockAnalyzer {

	public BlockAnalyzer() {
		LocalScopeCreator.getInstance().openLocalScope();
	}
	
	public void analyze(BlockContext ctx) {
		
		List<BlockStatementContext> blockListCtx = ctx.blockStatement();
		
		for(BlockStatementContext blockStatementCtx : blockListCtx) {
			if(blockStatementCtx.statement() != null) {
				StatementContext statementCtx = blockStatementCtx.statement();
				StatementAnalyzer statementAnalyzer = new StatementAnalyzer();
				statementAnalyzer.analyze(statementCtx);
			}
			else if(blockStatementCtx.localVariableDeclarationStatement() != null) {
				LocalVariableDeclarationStatementContext localVarDecStatementCtx = blockStatementCtx.localVariableDeclarationStatement();
				
				LocalVariableAnalyzer localVarAnalyzer = new LocalVariableAnalyzer();
				localVarAnalyzer.analyze(localVarDecStatementCtx.localVariableDeclaration());
			}
		}
		
		LocalScopeCreator.getInstance().closeLocalScope();
	}
}
