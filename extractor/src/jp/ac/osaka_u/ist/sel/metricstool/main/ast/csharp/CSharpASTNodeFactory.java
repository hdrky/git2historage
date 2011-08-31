package jp.ac.osaka_u.ist.sel.metricstool.main.ast.csharp;


import jp.ac.osaka_u.ist.sel.metricstool.main.parse.CSharpTokenTypes;
import antlr.ASTFactory;
import antlr.collections.AST;


public class CSharpASTNodeFactory {

    public CSharpASTNodeFactory(final ASTFactory astFactory) {
        if (null == astFactory) {
            throw new IllegalArgumentException("astFactory is null.");
        }
        this.astFactory = astFactory;
    }

    private ASTFactory astFactory;

    public AST createPropertyGetterHeadNode(final AST modifier, final AST propertyType,
            final AST propertyName) {
        final AST definition = this.createMethodDefinitionNode();

        // �v���p�e�B�̏C���q���Z�b�^���\�b�h�Ƃ��Ĉ����Ƃ��̏C���q�Ƃ��Đݒ�
        definition.addChild(this.cretateCloneAST(modifier));

        // �v���p�e�B�̌^���Q�b�^���\�b�h�Ƃ��Ĉ����Ƃ��̕Ԃ�l�Ƃ��Đݒ�
        definition.addChild(this.cretateCloneAST(propertyType));

        definition.addChild(this.cretateCloneAST(propertyName));

        return definition;
    }

    public AST createPropertySetterHeadNode(final AST modifier, final AST propertyType,
            final AST propertyName) {
        final AST definition = this.createMethodDefinitionNode();
        // �v���p�e�B�̏C���q���Z�b�^���\�b�h�Ƃ��Ĉ����Ƃ��̏C���q�Ƃ��Đݒ�
        definition.addChild(this.cretateCloneAST(modifier));

        // �v���p�e�B���Z�b�^���\�b�h�Ƃ��Ĉ����Ƃ��̕Ԃ�l�̌^void��ݒ�
        final AST methodType = this.astFactory.create(CSharpTokenTypes.TYPE, "TYPE");
        methodType.addChild(this.astFactory.create(CSharpTokenTypes.LITERAL_void, "void"));
        definition.addChild(methodType);

        definition.addChild(this.cretateCloneAST(propertyName));

        // �v���p�e�B���Z�b�^���\�b�h�Ƃ��Ĉ����Ƃ��̃p�����[�^��`��ݒ�
        definition.addChild(this.cretateSetterParameterNode(propertyType));

        return definition;
    }

    public AST cretateSetterParameterNode(final AST type) {
        final AST definitionHeader = this.astFactory.create(CSharpTokenTypes.PARAMETERS,
                "PARAMETERS");

        final AST definition = this.astFactory.create(CSharpTokenTypes.PARAMETER_DEF,
                "PARAMETER_DEF");
        definitionHeader.addChild(definition);

        // �C���q�̃m�[�h��ǉ��D
        definition.addChild(this.astFactory.create(CSharpTokenTypes.MODIFIERS, "MODIFIERS"));

        // �^�̃m�[�h��ǉ�
        definition.addChild(this.cretateCloneAST(type));

        // ���O�̃m�[�h��ǉ��D�v���p�e�B��set���ŏ�����`����Ă���p�����[�^����value
        final AST name = this.astFactory.create(CSharpTokenTypes.NAME, "NAME");
        name.addChild(this.astFactory.create(CSharpTokenTypes.IDENT, "value"));
        definition.addChild(name);

        return definitionHeader;
    }

    private AST createMethodDefinitionNode() {
        return this.astFactory.create(CSharpTokenTypes.METHOD_DEF, "METHOD_DEF");
    }

    private AST cretateCloneAST(final AST ast) {
        final AST cloneAst = this.astFactory.create(ast);
        final AST firstChild = ast.getFirstChild();

        if (null != firstChild) {
            cloneAst.addChild(this.cretateCloneAST(firstChild));
            AST nextChild = firstChild.getNextSibling();
            while (null != nextChild) {
                cloneAst.addChild(this.cretateCloneAST(nextChild));
                nextChild = nextChild.getNextSibling();
            }
        }

        return cloneAst;
    }

    public void setAstFactory(ASTFactory astFactory) {
        this.astFactory = astFactory;
    }

}
