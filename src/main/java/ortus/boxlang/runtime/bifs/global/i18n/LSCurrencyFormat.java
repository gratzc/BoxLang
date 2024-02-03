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
package ortus.boxlang.runtime.bifs.global.i18n;

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.BoxLangType;

@BoxBIF
@BoxMember( type = BoxLangType.NUMERIC )

public class LSCurrencyFormat extends LSNumberFormat {

	/**
	 * Constructor
	 */
	public LSCurrencyFormat() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, "any", Key.number ),
		    new Argument( false, "string", Key.type ),
		    new Argument( false, "string", Key.locale )
		};
	}

}
