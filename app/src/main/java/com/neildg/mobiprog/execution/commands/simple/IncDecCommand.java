/**
 * 
 */
package com.neildg.mobiprog.execution.commands.simple;

import com.neildg.mobiprog.builder.ParserHandler;
import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.generatedexp.JavaLexer;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.semantics.mapping.ClassIdentifierMapper;
import com.neildg.mobiprog.semantics.mapping.IValueMapper;
import com.neildg.mobiprog.semantics.mapping.IdentifierMapper;
import com.neildg.mobiprog.semantics.representations.MobiValue;
import com.neildg.mobiprog.semantics.representations.MobiValueSearcher;
import com.neildg.mobiprog.semantics.representations.MobiValue.PrimitiveType;
import com.neildg.mobiprog.semantics.symboltable.SymbolTableManager;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;

/**
 * An increment or decrement command
 * @author NeilDG
 *
 */
public class IncDecCommand implements ICommand {

	private ExpressionContext exprCtx;
	private int tokenSign;
	
	public IncDecCommand(ExpressionContext exprCtx, int tokenSign) {
		this.exprCtx = exprCtx;
		this.tokenSign = tokenSign;
	}
	
	/* (non-Javadoc)
	 * @see com.neildg.mobiprog.execution.commands.ICommand#execute()
	 */
	@Override
	public void execute() {
		//String identifier = this.exprCtx.primary().Identifier().getText();
		//MobiValue mobiValue = MobiValueSearcher.searchMobiValue(identifier);
		
		IValueMapper leftHandMapper = new IdentifierMapper(
				this.exprCtx.getText());
		leftHandMapper.analyze(this.exprCtx);

		MobiValue mobiValue = leftHandMapper.getMobiValue();
		
		this.performOperation(mobiValue);
	}
	
	/*
	 * Attempts to perform an increment/decrement operation
	 */
	private void performOperation(MobiValue mobiValue) {
		if(mobiValue.getPrimitiveType() == PrimitiveType.INT) {
			int value = Integer.valueOf(mobiValue.getValue().toString());
			
			if(this.tokenSign == JavaLexer.INC) {
				value++;
				mobiValue.setValue(String.valueOf(value));
			}
			else if(this.tokenSign == JavaLexer.DEC) {
				value--;
				mobiValue.setValue(String.valueOf(value));
			}
		}
		else if(mobiValue.getPrimitiveType() == PrimitiveType.LONG) {
			long value = Long.valueOf(mobiValue.getValue().toString());
			
			if(this.tokenSign == JavaLexer.INC) {
				value++;
				mobiValue.setValue(String.valueOf(value));
			}
			else if(this.tokenSign == JavaLexer.DEC) {
				value--;
				mobiValue.setValue(String.valueOf(value));
			}
		}
		else if(mobiValue.getPrimitiveType() == PrimitiveType.BYTE) {
			byte value = Byte.valueOf(mobiValue.getValue().toString());
			
			if(this.tokenSign == JavaLexer.INC) {
				value++;
				mobiValue.setValue(String.valueOf(value));
			}
			else if(this.tokenSign == JavaLexer.DEC) {
				value--;
				mobiValue.setValue(String.valueOf(value));
			}
		}
		else if(mobiValue.getPrimitiveType() == PrimitiveType.SHORT) {
			short value = Short.valueOf(mobiValue.getValue().toString());
			
			if(this.tokenSign == JavaLexer.INC) {
				value++;
				mobiValue.setValue(String.valueOf(value));
			}
			else if(this.tokenSign == JavaLexer.DEC) {
				value--;
				mobiValue.setValue(String.valueOf(value));
			}
		}
		else if(mobiValue.getPrimitiveType() == PrimitiveType.FLOAT) {
			float value = Float.valueOf(mobiValue.getValue().toString());
			
			if(this.tokenSign == JavaLexer.INC) {
				value++;
				mobiValue.setValue(String.valueOf(value));
			}
			else if(this.tokenSign == JavaLexer.DEC) {
				value--;
				mobiValue.setValue(String.valueOf(value));
			}
		}
		else if(mobiValue.getPrimitiveType() == PrimitiveType.DOUBLE) {
			double value = Double.valueOf(mobiValue.getValue().toString());
			
			if(this.tokenSign == JavaLexer.INC) {
				value++;
				mobiValue.setValue(String.valueOf(value));
			}
			else if(this.tokenSign == JavaLexer.DEC) {
				value--;
				mobiValue.setValue(String.valueOf(value));
			}
		}
	}

}
