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
package ortus.boxlang.runtime.types.immutable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import ortus.boxlang.runtime.dynamic.casters.KeyCaster;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.NullValue;
import ortus.boxlang.runtime.types.Struct;
import ortus.boxlang.runtime.types.exceptions.ApplicationException;
import ortus.boxlang.runtime.types.exceptions.UnmodifiableException;

/**
 * Represents an immutable Struct. All data you want needs to be passed in the constructor or
 * provided to a static creation method. Once instantiated, the Struct cannot be modified. An
 * exception will be thrown if you invoke any mutator method.
 */
public class ImmutableStruct extends Struct implements IImmutable {

	/**
	 * The type of struct
	 */
	public final Type type;

	/**
	 * --------------------------------------------------------------------------
	 * Constructors
	 * --------------------------------------------------------------------------
	 */

	/**
	 * Constructor
	 *
	 * @param type The type of struct to create: DEFAULT, LINKED, SORTED
	 */
	public ImmutableStruct( Type type ) {
		// Immutable struct does not use syncronized maps
		super(
		    switch ( type ) {
			    case DEFAULT -> new HashMap<>( INITIAL_CAPACITY );
			    case LINKED -> new LinkedHashMap<Key, Object>( INITIAL_CAPACITY );
			    case SORTED -> new TreeMap<Key, Object>();
			    default -> throw new ApplicationException( "Invalid struct type [" + type.name() + "]" );
		    },
		    true
		);
		this.type = type;
	}

	/**
	 * Create a default struct
	 */
	public ImmutableStruct() {
		this( Type.DEFAULT );
	}

	/**
	 * Construct a struct from a map
	 *
	 * @param map The map to create the struct from
	 */
	public ImmutableStruct( Map<? extends Object, ? extends Object> map ) {
		this( Type.DEFAULT, map );
	}

	/**
	 * Construct a struct of a specific type from a map
	 *
	 * @param map  The map to create the struct from
	 * @param type The type of struct to create: DEFAULT, LINKED, SORTED
	 */
	public ImmutableStruct( Type type, Map<? extends Object, ? extends Object> map ) {
		this( type );
		_addAll( map );
	}

	/**
	 * Create a struct from a map
	 *
	 * @param map The map to create the struct from
	 */
	public static ImmutableStruct fromMap( Map<Object, Object> map ) {
		return new ImmutableStruct( map );
	}

	/**
	 * Construct a struct of a specific type from a map
	 *
	 * @param map  The map to create the struct from
	 * @param type The type of struct to create: DEFAULT, LINKED, SORTED
	 */
	public static ImmutableStruct fromMap( Type type, Map<Object, Object> map ) {
		return new ImmutableStruct( type, map );
	}

	/**
	 * Create a struct from a list of values. The values must be in pairs, key, value, key, value, etc.
	 *
	 * @param values The values to create the struct from
	 *
	 * @return The struct
	 */
	public static ImmutableStruct of( Object... values ) {
		if ( values.length % 2 != 0 ) {
			throw new ApplicationException( "Invalid number of arguments.  Must be an even number." );
		}
		ImmutableStruct struct = new ImmutableStruct();
		for ( int i = 0; i < values.length; i += 2 ) {
			struct._put( KeyCaster.cast( values[ i ] ), values[ i + 1 ] );
		}
		return struct;
	}

	/**
	 * --------------------------------------------------------------------------
	 * Map Interface Methods
	 * --------------------------------------------------------------------------
	 */

	/**
	 * Set a value in the struct by a Key object
	 *
	 * @param key   The key to set
	 * @param value The value to set
	 *
	 * @return The previous value of the key, or null if not found
	 */
	@Override
	public Object put( Key key, Object value ) {
		throw new UnmodifiableException( "Cannot modify immutable Struct" );
	}

	/**
	 * Set a value in the struct by a Key object
	 *
	 * @param key   The key to set
	 * @param value The value to set
	 *
	 * @return The previous value of the key, or null if not found
	 */
	private Object _put( Key key, Object value ) {
		return wrapped.put( key, wrapNull( value ) );
	}

	/**
	 * Set a value in the struct by a string key, which we auto-convert to a Key object
	 *
	 * @param key   The string key to set
	 * @param value The value to set
	 *
	 * @return The previous value of the key, or null if not found
	 */
	public Object put( String key, Object value ) {
		throw new UnmodifiableException( "Cannot modify immutable Struct" );
	}

	/**
	 * Put a value in the struct if the key doesn't exist
	 *
	 * @param key   The key to set
	 * @param value The value to set
	 *
	 * @return The previous value of the key, or null if not found
	 */
	@Override
	public Object putIfAbsent( Key key, Object value ) {
		throw new UnmodifiableException( "Cannot modify immutable Struct" );
	}

	/**
	 * Put a value in the struct if the key doesn't exist
	 *
	 * @param key   The String key to set
	 * @param value The value to set
	 *
	 * @return The previous value of the key, or null if not found
	 */
	public Object putIfAbsent( String key, Object value ) {
		throw new UnmodifiableException( "Cannot modify immutable Struct" );
	}

	/**
	 * Remove a value from the struct by a Key object
	 *
	 * @param key The key to remove
	 */
	@Override
	public Object remove( Object key ) {
		throw new UnmodifiableException( "Cannot modify immutable Struct" );
	}

	/**
	 * Remove a value from the struct by a Key object
	 *
	 * @param key The String key to remove
	 */
	public Object remove( String key ) {
		throw new UnmodifiableException( "Cannot modify immutable Struct" );
	}

	/**
	 * Copies all of the mappings from the specified map to this map
	 * (optional operation). It expects the specific key and object generics.
	 */
	@Override
	public void putAll( Map<? extends Key, ? extends Object> map ) {
		throw new UnmodifiableException( "Cannot modify immutable Struct" );
	}

	/**
	 * Copies all of the mappings from the specified map to this map (optional operation).
	 * This method will automatically convert the keys to Key objects
	 *
	 * @param map
	 */
	private void _addAll( Map<? extends Object, ? extends Object> map ) {
		map.entrySet()
		    .parallelStream()
		    .forEach( entry -> {
			    Key key;
			    if ( entry.getKey() instanceof Key entryKey ) {
				    key = entryKey;
			    } else {
				    key = Key.of( entry.getKey().toString() );
			    }
			    _put( key, entry.getValue() );
		    } );
	}

	/**
	 * Copies all of the mappings from the specified map to this map (optional operation).
	 * This method will automatically convert the keys to Key objects
	 *
	 * @param map
	 */
	public void addAll( Map<? extends Object, ? extends Object> map ) {
		throw new UnmodifiableException( "Cannot modify immutable Struct" );
	}

	/**
	 * Removes all of the mappings from this map (optional operation).
	 */
	@Override
	public void clear() {
		throw new UnmodifiableException( "Cannot modify immutable Struct" );
	}

	/**
	 * --------------------------------------------------------------------------
	 * IReferenceable Interface Methods
	 * --------------------------------------------------------------------------
	 */

	/**
	 * Assign a value to a key
	 *
	 * @param key   The key to assign
	 * @param value The value to assign
	 */
	@Override
	public Object assign( Key key, Object value ) {
		throw new UnmodifiableException( "Cannot modify immutable Struct" );
	}

	/**
	 * Unwrap null values from the NullValue class
	 *
	 * @param value The value to unwrap
	 *
	 * @return The unwrapped value which can be null
	 */
	public static Object unWrapNull( Object value ) {
		if ( value instanceof NullValue ) {
			return null;
		}
		return value;
	}

}
