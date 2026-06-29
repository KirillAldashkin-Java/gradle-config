package local.poet

class Java {
    typealias File = com.palantir.javapoet.JavaFile
    typealias FileBuilder = com.palantir.javapoet.JavaFile.Builder
    typealias ClassName = com.palantir.javapoet.ClassName
    typealias TypeName = com.palantir.javapoet.TypeName
    typealias TypeSpec = com.palantir.javapoet.TypeSpec
    typealias TypeSpecBuilder = com.palantir.javapoet.TypeSpec.Builder
    typealias Modifier = javax.lang.model.element.Modifier
    typealias ParametrizedTypeName = com.palantir.javapoet.ParameterizedTypeName

    companion object {
        val LangByte = ClassName.get("java.lang", "Byte")
    }
}
