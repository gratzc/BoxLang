/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ortus.boxlang.runtime.bifs.global.list;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.BoxLangType;
import ortus.boxlang.runtime.types.ListUtil;

@BoxBIF
@BoxMember( type = BoxLangType.STRING, name = "listAppend" )

public class ListAppend extends BIF {

	/**
	 * Constructor
	 */
	public ListAppend() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, "string", Key.list ),
		    new Argument( true, "string", Key.value ),
		    new Argument( false, "string", Key.delimiter, ListUtil.DEFAULT_DELIMITER ),
		    new Argument( false, "boolean", Key.includeEmptyFields, false ),
		    new Argument( false, "boolean", Key.multiCharacterDelimiter, true )
		};
	}

	/**
	 * Appends an element to a list
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.list string list to filter entries from
	 *
	 * @argument.value The value to append
	 *
	 * @argument.delimiter string the list delimiter
	 *
	 * @argument.includeEmptyFields boolean whether to include empty fields in the returned result
	 *
	 * @argument.multiCharacterDelimiter boolean whether the delimiter is multi-character
	 *
	 * @argument.parallel boolean whether to execute the filter in parallel
	 *
	 * @argument.maxThreads number the maximum number of threads to use in the parallel filter
	 */
	public Object invoke( IBoxContext context, ArgumentsScope arguments ) {
		Boolean	isMultiChar	= arguments.getAsBoolean( Key.multiCharacterDelimiter );
		String	delimiter	= arguments.getAsString( Key.delimiter );
		return ListUtil.asString(
		    ListUtil.asList(
		        arguments.getAsString( Key.list ),
		        arguments.getAsString( Key.delimiter ),
		        arguments.getAsBoolean( Key.includeEmptyFields ),
		        isMultiChar
		    ).push( arguments.getAsString( Key.value ) ),
		    isMultiChar ? delimiter : delimiter.substring( 0, 1 )
		);
	}

}
