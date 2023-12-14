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
package ortus.boxlang.runtime.dynamic.casters;

import java.util.List;

import ortus.boxlang.runtime.interop.DynamicObject;
import ortus.boxlang.runtime.types.Array;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

/**
 * I handle casting anything to a Array
 */
public class ArrayCaster {

	/**
	 * Tests to see if the value can be cast to a Array.
	 * Returns a {@code CastAttempt<T>} which will contain the result if casting was
	 * was successfull, or can be interogated to proceed otherwise.
	 *
	 * @param object The value to cast to a Array
	 *
	 * @return The Array value
	 */
	public static CastAttempt<Array> attempt( Object object ) {
		return CastAttempt.ofNullable( cast( object, false ) );
	}

	/**
	 * Used to cast anything to a Array, throwing exception if we fail
	 *
	 * @param object The value to cast to a Array
	 *
	 * @return The Array value
	 */
	public static Array cast( Object object ) {
		return cast( object, true );
	}

	/**
	 * Used to cast anything to a Array
	 *
	 * @param object The value to cast to a Array
	 * @param fail   True to throw exception when failing.
	 *
	 * @return The Array value
	 */
	@SuppressWarnings( "unchecked" )
	public static Array cast( Object object, Boolean fail ) {
		if ( object == null ) {
			if ( fail ) {
				throw new BoxRuntimeException( "Can't cast null to a Array." );
			} else {
				return null;
			}
		}
		object = DynamicObject.unWrap( object );

		if ( object instanceof Array col ) {
			return col;
		}

		if ( object.getClass().isArray() ) {
			Object[] array = ( Object[] ) object;
			return Array.of( array );
		}

		if ( object instanceof List ) {
			return Array.of( ( List<Object> ) object );
		}

		if ( fail ) {
			throw new BoxRuntimeException(
			    String.format( "Can't cast [%s] to a Array.", object.getClass().getName() )
			);
		} else {
			return null;
		}
	}

}
