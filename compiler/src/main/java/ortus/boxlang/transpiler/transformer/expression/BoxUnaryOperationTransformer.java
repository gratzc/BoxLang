/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package ortus.boxlang.transpiler.transformer.expression;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ortus.boxlang.ast.BoxNode;
import ortus.boxlang.ast.expression.BoxBooleanLiteral;
import ortus.boxlang.ast.expression.BoxIdentifier;
import ortus.boxlang.ast.expression.BoxNegateOperation;
import ortus.boxlang.ast.expression.BoxUnaryOperation;
import ortus.boxlang.transpiler.BoxLangTranspiler;
import ortus.boxlang.transpiler.transformer.AbstractTransformer;
import ortus.boxlang.transpiler.transformer.TransformerContext;
import ortus.boxlang.transpiler.transformer.statement.BoxAssertTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Transform a BoxUnaryOperatorTransformer Node the equivalent Java Parser AST node
 */
public class BoxUnaryOperationTransformer extends AbstractTransformer {

	Logger logger = LoggerFactory.getLogger( BoxUnaryOperationTransformer.class );

	/**
	 * Transform a unary operator
	 *
	 * @param node    a BoxUnaryOperator instance
	 * @param context transformation context
	 *
	 * @return Generates a Method invocation to the Runtime Increment/Increment
	 *
	 * @throws IllegalStateException
	 */
	@Override
	public Node transform( BoxNode node, TransformerContext context ) throws IllegalStateException {
		BoxUnaryOperation	operation	= ( BoxUnaryOperation ) node;
		Map<String, String>	values		= new HashMap<>();

		Expression			expr		= ( Expression ) BoxLangTranspiler.transform( operation.getExpr() );
		values.put( "expr", expr.toString() );

		if ( expr instanceof MethodCallExpr ) {
			MethodCallExpr methodCall = ( MethodCallExpr ) expr;
			values.put( "expr", methodCall.getScope().get().toString() );
			values.put( "key", methodCall.getArguments().get( 0 ).toString() );

		}

		String	template	= switch ( operation.getOperator() ) {
								case PrePlusPlus -> "Increment.invokePre(${expr},${key})";
								case PostPlusPlus -> "Increment.invokePost(${expr},${key})";
								case PreMinusMinus -> "Decrement.invokePre(${expr},${key})";
								case PostMinusMinus -> "Decrement.invokePost(${expr},${key})";
								case Minus -> "Negate.invoke(${expr})";
								default -> "";
							};
		Node	javaExpr	= parseExpression( template, values );
		logger.info( node.getSourceText() + " -> " + javaExpr );
		addIndex( javaExpr, node );
		return javaExpr;
	}
}
