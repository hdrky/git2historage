package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * ���@����\���g�[�N���N���X
 * 
 * @author kou-tngt
 *
 */
public class SyntaxToken extends AstTokenAdapter {

    /**
     * ���O��؂��\���萔�C���X�^���X
     */
    public static final SyntaxToken NAME_SEPARATOR = new SyntaxToken("NAME_SEPARATOR") {
        @Override
        public boolean isNameSeparator() {
            return true;
        }
    };

    /**
     * �N���X�u���b�N�ȊO�̃u���b�N�̊J�n��\���萔�C���X�^���X
     */
    public static final SyntaxToken BLOCK_START = new SyntaxToken("BLOCK_START") {
        @Override
        public boolean isBlock() {
            return true;
        }
    };

    /**
     * �N���X�u���b�N�̊J�n��\���萔�C���X�^���X
     */
    public static final SyntaxToken CLASSBLOCK_START = new SyntaxToken("CLASSBLOCK_START") {
        @Override
        public boolean isBlock() {
            return true;
        }

        @Override
        public boolean isClassBlock() {
            return true;
        }
    };

    /**
     * ���\�b�h�Ăяo������\���萔�C���X�^���X
     */
    public static final SyntaxToken METHOD_CALL = new SyntaxToken("METHOD_CALL") {
        @Override
        public boolean isMethodCall() {
            return true;
        }
    };

    /**
     * new����\���萔�C���X�^���X
     */
    public static final SyntaxToken NEW = new SyntaxToken("NEW") {
        @Override
        public boolean isInstantiation() {
            return true;
        }
    };

    /**
     * �z��錾��\���萔�C���X�^���X
     */
    public static final SyntaxToken ARRAY = new SyntaxToken("ARRAY") {
        @Override
        public boolean isArrayDeclarator() {
            return true;
        }
    };

    /**
     * break����\���萔�C���X�^���X
     */
    public static final SyntaxToken BREAK = new SyntaxToken("BREAK") {
        @Override
        public boolean isJump() {
            return true;
        }

        @Override
        public boolean isStatement() {
            return true;
        }
    };

    public static final SyntaxToken CONTINUE = new SyntaxToken("CONTINUE") {
        @Override
        public boolean isJump() {
            return true;
        }

        @Override
        public boolean isStatement() {
            return true;
        }
    };

    /**
     * return����\���萔�C���X�^���X
     */
    public static final SyntaxToken RETURN = new SyntaxToken("RETURN") {
        @Override
        public boolean isReturn() {
            return true;
        }

        @Override
        public boolean isStatement() {
            return true;
        }
    };

    /**
     * throw����\���萔�C���X�^���X
     */
    public static final SyntaxToken THROW = new SyntaxToken("THROW") {
        @Override
        public boolean isThrow() {
            return true;
        }

        @Override
        public boolean isStatement() {
            return true;
        }
    };

    /**
     * assert����\���萔�C���X�^���X
     */
    public static final SyntaxToken ASSERT = new SyntaxToken("ASSERT") {

        @Override
        public boolean isAssertStatement() {
            return true;
        }
        
        @Override
        public boolean isStatement() {
            return true;
        }

    };

    /**
     * �v���p�e�B��get����\���萔�C���X�^���X
     */
    public static final SyntaxToken PROPERTY_GET_BODY = new SyntaxToken("PROPERTY_GET_BODY") {
        @Override
        public boolean isPropertyGetBody() {
            return true;
        };

        @Override
        public boolean isBlock() {
            return true;
        }

    };

    /**
     * �v���p�e�B��set����\���萔�C���X�^���X
     */
    public static final SyntaxToken PROPERTY_SET_BODY = new SyntaxToken("PROPERTY_SET_BODY") {
        @Override
        public boolean isPropertySetBody() {
            return true;
        };

        @Override
        public boolean isBlock() {
            return true;
        }
    };

    /**
     * �w�肳�ꂽ������ŕ\�����g�[�N�����쐬����R���X�g���N�^
     * @param text �g�[�N����\��������
     */
    public SyntaxToken(final String text) {
        super(text);
    }
}
