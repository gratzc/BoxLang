package ortus.boxlang.runtime.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.ArrayCaster;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.interop.DynamicJavaInteropService;
import ortus.boxlang.runtime.types.Array;
import ortus.boxlang.runtime.types.Function;

public class ArrayUtil {

	/**
	 * StringUtil constant method names for splitting strings to arrays
	 */
	private static final String	fn_Split				= "split";
	private static final String	fn_splitWholePreserve	= "splitByWholeSeparatorPreserveAllTokens";
	private static final String	fn_splitWhole			= "splitByWholeSeparator";
	private static final String	fn_splitPreserve		= "splitPreserveAllTokens";

	/**
	 * Creates an array from a delimited list
	 * 
	 * @param list           The string lists
	 * @param delimiter      The delimiter(s) of the list
	 * @param includeEmpty   Whether to include empty items in the result array
	 * @param wholeDelimiter Whether the delimiter contains multiple characters which should be matched. Otherwise all characters in the delimiter are
	 *                       treated as separate delimiters
	 * 
	 * @return
	 */
	public static Array ofList(
	    String list,
	    String delimiter,
	    Boolean includeEmpty,
	    Boolean wholeDelimiter ) {

		String utilFn = fn_Split;
		if ( wholeDelimiter ) {
			if ( includeEmpty ) {
				utilFn = fn_splitWholePreserve;
			} else {
				utilFn = fn_splitWhole;
			}
		} else if ( includeEmpty ) {
			utilFn = fn_splitPreserve;
		}
		return new Array(
		    ( String[] ) DynamicJavaInteropService.invokeStatic( StringUtils.class, utilFn, list, delimiter )
		);
	}

	/**
	 * Converts an Array object to a delimited list using the specified delimiter
	 *
	 * @param array
	 * @param delimiter
	 *
	 * @return
	 */
	public static String toList(
	    Array array,
	    String delimiter ) {
		return array.stream()
		    .map( StringCaster::cast )
		    .collect( Collectors.joining( ( delimiter ) ) );
	}

	/**
	 * Method to filter an array with a function callback and context
	 *
	 * @param array           The array object to filter
	 * @param callback        The callback Function object
	 * @param callbackContext The context in which to execute the callback
	 * @param parallel        Whether to process the filter in parallel
	 * @param maxThreads      Optional max threads for parallel execution
	 *
	 * @return A filtered array
	 */
	public static Array filter(
	    Array array,
	    Function callback,
	    IBoxContext callbackContext,
	    Boolean parallel,
	    Integer maxThreads ) {

		IntPredicate	test	= idx -> ( boolean ) callbackContext.invokeFunction( callback,
		    new Object[] { array.get( idx ), idx + 1, array } );

		ForkJoinPool	pool	= null;
		if ( parallel ) {
			pool = new ForkJoinPool( maxThreads );
		}

		return ArrayCaster.cast(
		    pool == null
		        ? array.intStream()
		            .filter( test )
		            .mapToObj( idx -> array.get( idx ) )
		            .toArray()

		        : CompletableFuture.supplyAsync(
		            () -> array.intStream().parallel().filter( test ).mapToObj( idx -> array.get( idx ) ),
		            pool
		        ).join().toArray()
		);
	}

}
