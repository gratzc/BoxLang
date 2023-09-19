import com.github.javaparser.ast.Node;
import org.junit.Test;
import ortus.boxlang.executor.JavaRunner;
import ortus.boxlang.parser.BoxFileType;
import ortus.boxlang.parser.BoxParser;
import ortus.boxlang.parser.ParsingResult;
import ortus.boxlang.transpiler.BoxLangTranspiler;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

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

public class TestExecution extends TestBase {

	@Test
	public void executeWhile() throws IOException {

		String			statement	= """
		                              variables['system'] = createObject('java','java.lang.System');

		                              variables.a = 10;
		                              while(variables.a > 0) {
		                              	variables.a;
		                              	variables.system.out.println(variables.a);
		                              }
		                              assert(variables["a"] == 10);
		                              """;
		BoxParser		parser		= new BoxParser();
		ParsingResult	result		= parser.parse( statement, BoxFileType.CF );
		assertTrue( result.isCorrect() );

		BoxLangTranspiler	transpiler	= new BoxLangTranspiler();
		Node				javaAST		= transpiler.transpile( result.getRoot() );
		new JavaRunner().run( transpiler.getStatements() );
	}

	@Test
	public void executeFor() throws IOException {

		String			statement	= """
		                                                     variables['system'] = createObject('java','java.lang.System');

		                                                     for(variables.a = 0; variables.a < 10; variables.a++){
		                                                     	variables.system.out.println(variables.a);
		                                                     }
		                              assert(variables["a"] == 10);
		                                                     """;
		BoxParser		parser		= new BoxParser();
		ParsingResult	result		= parser.parse( statement, BoxFileType.CF );
		assertTrue( result.isCorrect() );

		BoxLangTranspiler	transpiler	= new BoxLangTranspiler();
		Node				javaAST		= transpiler.transpile( result.getRoot() );
		new JavaRunner().run( transpiler.getStatements() );
	}

}