// WITH_STDLIB

class A<T> {
    private fun foo(x: T) = x
    internal inline fun callFoo(x: T) = foo(x)

    private fun <U> baz(x: T, y: U) = x to y
    internal inline fun <U> callBaz(x: T, y: U) = baz(x, y)

    inner class B<S> {
        private fun bar(x: T, y: S) = x to y
        internal inline fun callBar(x: T, y: S) = bar(x, y)
    }

    inner class C<S> private constructor(val x: S) {
        @Suppress("INVISIBLE_REFERENCE")
        internal inline fun copy() = C<Int>(42)
    }
    @Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
    internal inline fun getC(s: String) = C<String>(s)

    companion object Companion {
        private fun bar(x: Any) = x
        internal inline fun callBar(x: Any) = bar(x)
    }

    class Nested {
        private fun bar(x: Any) = x
        internal inline fun callBar(x: Any) = bar(x)
    }
}

fun box(): String {
    var res = ""
    res += A<String>().callFoo("OK1 ")
    res += A<String>().callBaz("OK2 ", "NO2 ").first
    res += A<String>().callBaz("NO3 ", "OK3 ").second
    res += A<String>().B<String>().callBar("OK4 ", "NO4 ").first
    res += A<String>().B<String>().callBar("NO5", "OK5 ").second
    res += A<String>().getC("OK6 ").x
    res += if (A<String>().getC("OK").copy().x == 42) "OK7" else "NO7"
    if (res != "OK1 OK2 OK3 OK4 OK5 OK6 OK7") return res
    else return "OK"
}