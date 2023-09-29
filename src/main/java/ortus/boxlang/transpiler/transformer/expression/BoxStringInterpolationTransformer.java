
package ortus.boxlang.transpiler.transformer.expression;

import com.github.javaparser.ast.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ortus.boxlang.ast.BoxNode;
import ortus.boxlang.ast.expression.BoxStringInterpolation;
import ortus.boxlang.transpiler.BoxLangTranspiler;
import ortus.boxlang.transpiler.transformer.AbstractTransformer;
import ortus.boxlang.transpiler.transformer.TransformerContext;

import java.util.HashMap;
import java.util.stream.Collectors;

public class BoxStringInterpolationTransformer extends AbstractTransformer {

	Logger logger = LoggerFactory.getLogger( BoxStringInterpolationTransformer.class );

	@Override
	public Node transform( BoxNode node, TransformerContext context ) throws IllegalStateException {
		BoxStringInterpolation	interpolation	= ( BoxStringInterpolation ) node;
		String					expr			= interpolation.getValues()
		    .stream()
		    .map( it -> resolveScope( BoxLangTranspiler.transform( it, TransformerContext.RIGHT ), context ).toString() )
		    .collect( Collectors.joining( "+" ) );

		Node					javaExpr		= parseExpression( expr, new HashMap<>() );
		logger.info( "{} -> {}", node.getSourceText(), javaExpr );
		addIndex( javaExpr, node );
		return javaExpr;
	}
}
