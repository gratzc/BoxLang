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

package ortus.boxlang.runtime.config.segments;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariConfig;

import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Struct;

class DatasourceConfigTest {

	@DisplayName( "It can generate hikari config" )
	@Test
	void testItCanGenerateHikariConfig() {
		DatasourceConfig	datasource		= new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "connectionString", "jdbc:postgresql://localhost:5432/foo"
		) );
		HikariConfig		hikariConfig	= datasource.toHikariConfig();

		assert hikariConfig.getJdbcUrl().equals( "jdbc:postgresql://localhost:5432/foo" );
	}

	@DisplayName( "It can load config" )
	@Test
	void testItCanConstructConnectionString() {
		DatasourceConfig	datasource		= new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "driver", "mysql",
		    "host", "127.0.0.1",
		    "port", 3306,
		    "database", "foo",
		    "custom", Struct.of( "useSSL", false )
		) );
		HikariConfig		hikariConfig	= datasource.toHikariConfig();

		assertEquals( "jdbc:mysql://127.0.0.1:3306/foo?useSSL=false", hikariConfig.getJdbcUrl() );
	}

	@DisplayName( "It can load a config with placeholders" )
	@Test
	void testItCanConstructConnectionStringWithPlaceholders() {
		DatasourceConfig	datasource		= new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "driver", "mysql",
		    "url", "jdbc:mysql://{host}:{port}/{database}",
		    "host", "localhost",
		    "port", 3306,
		    "database", "foo",
		    "custom", Struct.of( "useSSL", false )
		) );
		HikariConfig		hikariConfig	= datasource.toHikariConfig();

		assertEquals( "jdbc:mysql://localhost:3306/foo?useSSL=false", hikariConfig.getJdbcUrl() );
	}

	@DisplayName( "It can load config" )

	@Test
	void testItCanConstructMinimalConnectionString() {
		DatasourceConfig	datasource		= new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "driver", "postgresql",
		    "host", "127.0.0.1",
		    "port", 5432
		) );
		HikariConfig		hikariConfig	= datasource.toHikariConfig();

		assertEquals( "jdbc:postgresql://127.0.0.1:5432/?", hikariConfig.getJdbcUrl() );
	}

	@DisplayName( "It can create a unique name for the datasource" )
	@Test
	void testItCanCreateUniqueName() {
		DatasourceConfig	datasource	= new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "driver", "postgresql",
		    "host", "localhost",
		    "port", 5432,
		    "database", "foo",
		    "custom", "useSSL=false"
		) );

		Key					name		= datasource.getUniqueName();

		assertThat( name.getName() ).contains( "bx_" );
		assertThat( name.getName() ).contains( "_Foo_" );
		// third element should be a hashcode
		assertThat( name.getName().split( "_" )[ 2 ] ).matches( "\\d+" );
	}

	@DisplayName( "It can create a unique name for the datasource with an application name" )
	@Test
	void testItCanCreateUniqueNameWithApplication() {
		DatasourceConfig	datasource	= new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "driver", "postgresql",
		    "host", "localhost",
		    "port", 5432,
		    "database", "foo",
		    "custom", "useSSL=false"
		) ).withAppName( Key.of( "myAppName" ) );

		Key					name		= datasource.getUniqueName();

		// This name should be: bx_myAppName_Foo_<hashcode>
		assertThat( name.getName() ).contains( "bx_" );
		assertThat( name.getName() ).contains( "_myAppName_" );
		assertThat( name.getName() ).contains( "_Foo_" );
		// fourth element should be a hashcode
		assertThat( name.getName().split( "_" )[ 3 ] ).matches( "\\d+" );
	}

	@DisplayName( "It can create a unique name for an on the fly datasource" )
	@Test
	void testItCanCreateUniqueNameForOnTheFly() {
		DatasourceConfig	datasource	= new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "driver", "postgresql",
		    "host", "localhost",
		    "port", 5432,
		    "database", "foo",
		    "custom", "useSSL=false"
		) ).onTheFly();

		Key					name		= datasource.getUniqueName();

		// The result should be: bx_onTheFly_Foo_<hashcode>
		assertThat( name.getName() ).contains( "bx_" );
		assertThat( name.getName() ).contains( "onthefly_" );
		assertThat( name.getName() ).contains( "_Foo_" );
		// fourth element should be a hashcode
		assertThat( name.getName().split( "_" )[ 3 ] ).matches( "\\d+" );
	}

	@DisplayName( "I can get a unique hash code for a datasource" )
	@Test
	void testItCanGetUniqueHashCode() {
		DatasourceConfig	datasource	= new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "driver", "postgresql",
		    "host", "localhost",
		    "port", 5432,
		    "database", "foo",
		    "custom", "useSSL=false"
		) );

		int					hashCode	= datasource.hashCode();
		// Verify that the hashcode is not 0
		assertThat( hashCode ).isNotEqualTo( 0 );
	}

	@DisplayName( "It can validate equality of datasources" )
	@Test
	void testItCanValidateEquality() {
		DatasourceConfig	datasource1	= new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "driver", "postgresql",
		    "host", "localhost",
		    "port", 5432,
		    "database", "foo",
		    "custom", "useSSL=false"
		) );

		DatasourceConfig	datasource2	= new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "driver", "postgresql",
		    "host", "localhost",
		    "port", 5432,
		    "database", "foo",
		    "custom", "useSSL=false"
		) );

		assertThat( datasource1 ).isEqualTo( datasource2 );
	}

	@DisplayName( "It can validate inequality of datasources" )
	@Test
	void testItCanValidateInequality() {
		DatasourceConfig	datasource1	= new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "driver", "postgresql",
		    "host", "localhost",
		    "port", 5432,
		    "database", "foo",
		    "custom", "useSSL=false"
		) );

		DatasourceConfig	datasource2	= new DatasourceConfig( Key.of( "Bar" ), Key.of( "Derby" ), Struct.of(
		    "driver", "postgresql",
		    "host", "localhost",
		    "port", 5432,
		    "database", "foo",
		    "custom", "useSSL=false"
		) );

		assertThat( datasource1 ).isNotEqualTo( datasource2 );

		// Now test with different properties
		datasource2 = new DatasourceConfig( Key.of( "Foo" ), Key.of( "Derby" ), Struct.of(
		    "driver", "postgresql",
		    "host", "localhost",
		    "port", 5432,
		    "database", "bar",
		    "custom", "useSSL=false"
		) );

		assertThat( datasource1 ).isNotEqualTo( datasource2 );
	}

}
