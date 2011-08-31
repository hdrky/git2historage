package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.DescriptionToken;
import java.lang.Enum;


/**
 * Java���L�̗v�f��\���g�[�N�����`����N���X
 * 
 * @author kou-tngt
 *
 */
public class JavaAstToken extends AstTokenAdapter {

    /**
     * import�L�q����\���萔�C���X�^���X
     */
    public static final JavaAstToken CLASS_IMPORT = new JavaAstToken("CLASS_IMPORT") {
        @Override
        public boolean isClassImport() {
            return true;
        }
    };

    /**
     * import�L�q����\���萔�C���X�^���X
     */
    public static final JavaAstToken MEMBER_IMPORT = new JavaAstToken("MEMBER_IMPORT") {
        @Override
        public boolean isMemberImport() {
            return true;
        }
    };

    /**
     * throw�L�q����\���萔�C���X�^���X
     */
    //TODO: �ꏊ�����ł����̂���?
    public static final JavaAstToken THROWS = new JavaAstToken("THROWS") {
        @Override
        public boolean isThrows() {
            return true;
        }
    };

    /**
     * enum�v�f�̋L�q����\���萔�C���X�^���X
     */
    public static final JavaAstToken ENUM_CONSTANT = new JavaAstToken("ENUM_CONST") {
        @Override
        public boolean isEnumConstant() {
            return true;
        }
    };

    public static final JavaAstToken EXPR_LIST = new JavaAstToken("ELIST") {
        @Override
        public boolean isExpressionList() {
            return true;
        }
    };

    /**
     * super�L�q����\���萔�C���X�^���X
     */
    public static final JavaAstToken SUPER = new JavaAstToken("SUPER");

    /**
     * �z�񏉊�������\���萔�C���X�^���X
     */
    public static final JavaAstToken ARRAY_INIT = new JavaAstToken("ARRAY_INIT") {
        @Override
        public boolean isArrayInitilizer() {
            return true;
        }
    };

    /**
     * new���ɂ��z��^�w��i[]�j��\���萔�C���X�^���X
     */
    public static final JavaAstToken ARRAY_INSTANTIATION = new JavaAstToken("ARRAY_INSTANTIATION");

    /**
     * this()�̂悤�Ȏ��R���X�g���N�^�̌Ăяo����\���萔�C���X�^���X
     */
    public static final JavaAstToken CONSTRUCTOR_CALL = new JavaAstToken("CONSTRUCTOR_CALL") {
        @Override
        public boolean isThisConstructorCall() {
            return true;
        }
    };

    /**
     * super()�̂悤�Ȑe�R���X�g���N�^�̌Ăяo����\���萔�C���X�^���X
     */
    public static final JavaAstToken SUPER_CONSTRUCTOR_CALL = new JavaAstToken(
            "SUPER_CONSTRUCTOR_CALL") {
        @Override
        public boolean isSuperConstructorCall() {
            return true;
        }
    };

    /**
     * .class���L�q��\���萔�C���X�^���X
     */
    public static final JavaAstToken CLASS = new JavaAstToken("CLASS");

    /**
     * �C���^�t�F�[�X��`����\���萔�C���X�^���X
     */
    public static final JavaAstToken INTERFACE_DEFINITION = new JavaAstToken("INTERFACE_DEFINITION") {
        @Override
        public boolean isClassDefinition() {
            return true;
        }

        @Override
        public boolean isBlockDefinition() {
            return true;
        }
    };

    /**
     * �A�m�e�[�V�������\���萔�C���X�^���X
     */
    public static final JavaAstToken ANNOTATIONS = new JavaAstToken("ANNOTATIONS") {
        @Override
        public boolean isAnnotations() {
            return true;
        }
    };

    /**
     * �A�m�e�[�V������\���萔�C���X�^���X
     */
    public static final JavaAstToken ANNOTATION = new JavaAstToken("ANNOTATION") {
        @Override
        public boolean isAnnotation() {
            return true;
        }
    };

    /**
     * �A�m�e�[�V�����ɓn���z��C���X�^���X
     */
    public static final JavaAstToken ANNOTATION_MEMBER = new JavaAstToken("ANNOTATION_MEMBER") {
        @Override
        public boolean isAnnotationMember() {
            return true;
        }
    };

    /**
     * �A�m�e�[�V�����ɓn��������C���X�^���X
     */
    public static final JavaAstToken ANNOTATION_MEMBER_VALUE_PAIR = new JavaAstToken(
            "ANNOTATION_MEMBER_VALUE_PAIR") {
        @Override
        public boolean isAnnotationMemberValuePair() {
            return true;
        }
    };

    /**
     * �A�m�e�[�V�����ɓn���z��C���X�^���X
     */
    public static final JavaAstToken ANNOTATION_ARRAY_INIT = new JavaAstToken(
            "ANNOTATION_ARRAY_INIT") {
        @Override
        public boolean isAnnotationArrayInit() {
            return true;
        }
    };

    /**
     * �A�m�e�[�V��������(������`��)�C���X�^���X
     */
    public static final JavaAstToken ANNOTATION_STRING = new JavaAstToken("ANNOTATION_STRING") {
        @Override
        public boolean isAnnotationString() {
            return true;
        }
    };

    /**
     * �w�肳�ꂽ�������\���g�[�N�����쐬����.
     * @param text �g�[�N���̕�����
     */
    public JavaAstToken(final String text) {
        super(text);
    }

}
