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
package ourtus.boxlang.ast.expression;

import ourtus.boxlang.ast.BoxExpr;
import ourtus.boxlang.ast.Position;

public class BoxTernaryOperation extends BoxExpr {

	private final BoxExpr	condition;
	private final BoxExpr	whenTrue;
	private final BoxExpr	whenFalse;

	public BoxTernaryOperation( BoxExpr condition, BoxExpr whenTrue, BoxExpr whenFalse, Position position, String sourceText ) {
		super( position, sourceText );
		this.condition	= condition;
		this.whenTrue	= whenTrue;
		this.whenFalse	= whenFalse;
		this.condition.setParent( this );
		this.whenTrue.setParent( this );
		this.whenFalse.setParent( this );
	}

	public BoxExpr getCondition() {
		return condition;
	}

	public BoxExpr getWhenTrue() {
		return whenTrue;
	}

	public BoxExpr getWhenFalse() {
		return whenFalse;
	}
}
