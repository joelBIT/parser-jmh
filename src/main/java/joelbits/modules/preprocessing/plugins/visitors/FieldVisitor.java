package joelbits.modules.preprocessing.plugins.visitors;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import joelbits.model.ast.protobuf.ASTProtos.Modifier;
import joelbits.model.ast.protobuf.ASTProtos.Variable;
import joelbits.modules.preprocessing.plugins.utils.TypeConverter;
import joelbits.modules.preprocessing.utils.ASTNodeCreator;

import java.util.List;

/**
 * A visitor for top-level fields in a class.
 */
public final class FieldVisitor extends VoidVisitorAdapter<List<Variable>> {
    private final ASTNodeCreator astNodeCreator = new ASTNodeCreator();
    private final TypeConverter typeConverter = new TypeConverter();

    @Override
    public void visit(FieldDeclaration field, List<Variable> fieldsInDeclaration) {
        List<Modifier> modifiers = typeConverter.convertModifiers(field.getModifiers());

        for (AnnotationExpr annotationExpr : field.getAnnotations()) {
            List<String> annotationMembers = typeConverter.convertAnnotationMembers(annotationExpr);
            modifiers.add(astNodeCreator.createAnnotationModifier(annotationExpr.getNameAsString(), annotationMembers));
        }

        for (VariableDeclarator test : field.getVariables()) {
            fieldsInDeclaration.add(astNodeCreator.createVariable(test.getName().asString(), field.getElementType().asString(), modifiers));
        }
    }
}