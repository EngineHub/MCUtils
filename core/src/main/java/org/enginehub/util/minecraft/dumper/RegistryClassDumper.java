package org.enginehub.util.minecraft.dumper;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterators;
import com.google.common.io.MoreFiles;
import com.squareup.javapoet.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static javax.lang.model.element.Modifier.*;

public abstract class RegistryClassDumper extends AbstractDumper {

    private static final ClassName JAVAX_NULLABLE =
            ClassName.get("javax.annotation", "Nullable");
    private static final String FOUR_SPACES = Strings.repeat(" ", 4);

    private static UnaryOperator<String> inlineAnnotation(String name) {
        Pattern pattern = Pattern.compile("@" + name + "\n" + FOUR_SPACES);
        return content -> pattern.matcher(content).replaceAll("@" + name + " ");
    }

    private static final UnaryOperator<String> INLINE_NULLABLE = inlineAnnotation("Nullable");
    private static final UnaryOperator<String> INLINE_DEPRECATED = inlineAnnotation("Deprecated");

    /**
     * Handles simple pluralization rules.
     */
    private static String makePlural(String name) {
        if (name.endsWith("y")) {
            return name.substring(0, name.length() - 1) + "ies";
        }
        return name + "s";
    }

    protected record DumperInfo(ClassName type, boolean nullable) {
        private void generate(Iterator<String> ids, Iterator<String> deprecatedIdsIter) {
            Set<String> deprecatedIds = ImmutableSortedSet.copyOf(deprecatedIdsIter);
            Set<String> resources = ImmutableSortedSet
                    .<String>naturalOrder()
                    .addAll(ids)
                    .addAll(deprecatedIds)
                    .build();
            ClassName pluralType = type.peerClass(makePlural(type.simpleName()));
            TypeSpec.Builder builder = TypeSpec.classBuilder(pluralType);
            builder.addModifiers(PUBLIC, FINAL);
            builder.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                    .addMember("value", "$S", "unused")
                    .build());
            builder.addJavadoc(
                    "Stores a list of common {@link $1T $2N}.\n\n@see $1T",
                    type, pluralType.simpleName()
            );
            builder.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(PRIVATE)
                    .build());
            builder.addMethod(createGetMethod());
            for (String resourceLocation : resources) {
                String name = resourceLocation.toUpperCase().replace('/', '_');
                FieldSpec.Builder fieldBuilder = FieldSpec.builder(
                        type,
                        name,
                        PUBLIC, STATIC, FINAL
                );
                if (deprecatedIds.contains(resourceLocation)) {
                    fieldBuilder.addAnnotation(Deprecated.class);
                }
                if (nullable) {
                    fieldBuilder.addAnnotation(JAVAX_NULLABLE);
                }
                fieldBuilder.initializer("get($S)", resourceLocation);
                builder.addField(fieldBuilder.build());
            }
            TypeSpec spec = builder.build();
            JavaFile javaFile = JavaFile.builder(type.packageName(), spec)
                    .indent(FOUR_SPACES)
                    .skipJavaLangImports(true)
                    .build();

            try {
                // TODO Try to migrate OUTPUT to a Path object
                Path outputFile = OUTPUT.toPath().resolve(spec.name + ".java");
                String content = fixContent(javaFile.toString());
                MoreFiles.asCharSink(outputFile, StandardCharsets.UTF_8).write(content);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private MethodSpec createGetMethod() {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("get");
            builder
                    .addJavadoc("Gets the {@link $T} associated with the given id.", type)
                    .addModifiers(PUBLIC, STATIC)
                    .addParameter(String.class, "id");
            if (nullable) {
                builder.returns(type.annotated(AnnotationSpec.builder(JAVAX_NULLABLE).build()))
                        .addStatement("return $T.REGISTRY.get(id)", type);
            } else {
                builder.returns(type)
                        .addCode(CodeBlock.builder()
                                .addStatement("$1T entry = $1T.REGISTRY.get(id)", type)
                                .beginControlFlow("if (entry == null)")
                                .addStatement("return new $T(id)", type)
                                .endControlFlow()
                                .addStatement("return entry")
                                .build());
            }
            return builder.build();
        }

        private String fixContent(String content) {
            if (nullable) {
                content = INLINE_NULLABLE.apply(content);
            }
            content = INLINE_DEPRECATED.apply(content);
            content = content.replace(");\n\n", ");\n")
                    .replace(FOUR_SPACES + "private ", "\n" + FOUR_SPACES + "private ");
            return content;
        }
    }

    private final DumperInfo idDumper;
    private final DumperInfo tagDumper;

    protected RegistryClassDumper(String packageName, String baseName) {
        this.idDumper = new DumperInfo(ClassName.get(packageName, baseName + "Type"), true);
        this.tagDumper = new DumperInfo(ClassName.get(packageName, baseName + "Category"), false);
    }

    protected Iterator<String> getIds() {
        return Iterators.forArray();
    }

    protected Iterator<String> getTags() {
        return Iterators.forArray();
    }

    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray();
    }

    protected Iterator<String> getDeprecatedTags() {
        return Iterators.forArray();
    }

    @Override
    public void run() {
        idDumper.generate(getIds(), getDeprecatedIds());
        tagDumper.generate(getTags(), getDeprecatedTags());
    }

}