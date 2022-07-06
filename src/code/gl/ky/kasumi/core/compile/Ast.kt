package gl.ky.kasumi.core.compile

import gl.ky.kasumi.core.shared.KPrimitive

sealed interface KNode {
    class Module(val functions: List<Function>): KNode
    interface TopLevel : KNode
    class Imports(val imports: List<String>) : TopLevel
    class Function(val name: String, /*val args: List<String>,*/ val body: Block) : TopLevel
    class TopLevelVarAssign(val name: String, val value: Expression) : TopLevel
    interface Statement : KNode
    object Nope : Statement
    class Block(val statements: List<Statement>) : Statement
    class Command(val subject: Expression, val action: String, val target: Expression) : Statement
    class VarAssign(val name: String, val value: Expression) : Statement
    class Conditional(val condition: Expression, val thenBranch: Statement, val elseBranch: Statement) : Statement
    class ConditionalLoop(val condition: Expression, val body: Statement) : Statement
    class LimitedLoop(val times: Expression, val body: Statement) : Statement
    object Continue : Statement
    object Break : Statement
    class TryCatch(val tryBranch: Statement, val catchBranch: Statement) : Statement
    class Try(val body: Statement) : Statement
    interface Expression : KNode
    class Transform(val value: Expression, val transformer: String) : Expression
    class Supplier(val function: String) : Expression
    class Literal(val value: KPrimitive) : Expression
}