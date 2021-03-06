suspend fun dummy() {}

val c: suspend () -> Unit = {
    dummy()
    dummy()
}

class A {
    suspend fun foo(a: A, s: String = "", block: suspend A.() -> Unit) {
        block()
        block()
    }
}

// BEs generate continuation classes differently, JVM_IR generates more correctly

// JVM_TEMPLATES
// invokeSuspend
// 1 LOCALVARIABLE this LThisAndResultInLvtKt\$c\$1; L0 L.* 0
// c's lambda and foo's continuation
// 2 LOCALVARIABLE \$result Ljava/lang/Object; L0 L.* 1

// foo and <init>
// 2 LOCALVARIABLE this LA; L0 L.* 0
// 1 LOCALVARIABLE a LA; L0 L.* 1
// 1 LOCALVARIABLE s Ljava/lang/String; L0 L.* 2
// 1 LOCALVARIABLE block Lkotlin/jvm/functions/Function2; L0 L.* 3
// 1 LOCALVARIABLE \$continuation Lkotlin/coroutines/Continuation; L2 L.* 6

// JVM_IR_TEMPLATES
// <init>, invoke, invokeSuspend, create
// 4 LOCALVARIABLE this LThisAndResultInLvtKt\$c\$1; L0 L.* 0
// TODO: Continuation are generated even for tail-call suspend functions: KT-36795
// c's lambda and foo's and dummy's continuation
// 3 LOCALVARIABLE \$result Ljava/lang/Object; L0 L.* 1

// foo and <init>
// 2 LOCALVARIABLE this LA; L0 L.* 0
// 1 LOCALVARIABLE a LA; L0 L.* 1
// 1 LOCALVARIABLE s Ljava/lang/String; L0 L.* 2
// 1 LOCALVARIABLE block Lkotlin/jvm/functions/Function2; L0 L.* 3
// 1 LOCALVARIABLE \$continuation Lkotlin/coroutines/Continuation; L2 L.* 6