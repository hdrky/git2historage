package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR_TYPE;


/**
 * ���Z�q��\���g�[�N���N���X
 * 
 * @author kou-tngt
 *
 */
public class OperatorToken extends AstTokenAdapter {

    /**
     * �L���X�g�͉��Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken CAST = new OperatorToken(null, "CAST", 2, false, false,
            new int[] { 0 });

    /**
     * �C���N�������g���Z�q�ƃf�N�������g���Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken INCL_AND_DECL = new OperatorToken(null, "INCLEMENT", 1, true,
            true, new int[] { 0 });

    /**
     * ������Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken ASSIGNMENT = new OperatorToken(OPERATOR_TYPE.ASSIGNMENT,
            "ASSIMENT", 2, true, false, new int[] { 0 });

    /**
     * ����������Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken COMPOUND_ASSIGNMENT = new OperatorToken(
            OPERATOR_TYPE.ASSIGNMENT, "COMPOUND_ASSIGNMENT", 2, true, true, new int[] { 0 });

    /**
     * �P���Z�p���Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken ARITHMETHIC_UNARY = new OperatorToken(
            OPERATOR_TYPE.ARITHMETIC, "ARITHMETIC_UNARY", 1, false, true, new int[] { 0 });

    /**
     * �񍀎Z�p���Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken ARITHMETICH_BINOMIAL = new OperatorToken(
            OPERATOR_TYPE.ARITHMETIC, "ARITHMETIC_BINOMIAL", 2, false, true, new int[] { 0, 1 });

    /**
     * �P���_�����Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken LOGICAL_UNARY = new OperatorToken(OPERATOR_TYPE.LOGICAL,
            "NOT_UNARY", 1, false, true, new int[] { 0 });

    /**
     * �񍀘_�����Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken LOGICAL_BINOMIAL = new OperatorToken(OPERATOR_TYPE.LOGICAL,
            "LOGICAL_BINOMIAL", 2, false, true, new int[] { 0, 1 });

    /**
     * �P���r�b�g���Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken BIT_UNARY = new OperatorToken(OPERATOR_TYPE.BITS,
            "BIT_UNARY", 1, false, true, new int[] { 0 });

    /**
     * �񍀃r�b�g���Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken BIT_BINOMIAL = new OperatorToken(OPERATOR_TYPE.BITS,
            "BIT_BINOMIAL", 2, false, true, new int[] { 0, 1 });

    /**
     * �񍀃V�t�g���Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken SHIFT = new OperatorToken(OPERATOR_TYPE.SHIFT, "SHIFT", 2,
            false, true, new int[] { 0, 1 });

    /**
     * �񍀔�r���Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken COMPARATIVE = new OperatorToken(OPERATOR_TYPE.COMPARATIVE,
            "COMPARATIVE", 2, false, true, new int[] {});

    /**
     * �O�����Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken TERNARY = new OperatorToken(null, "TERNARY", 3, false, true,
            new int[] { 1, 2 });

    /**
     * �z��L�q�q��\���萔�C���X�^���X
     */
    public static final OperatorToken ARRAY = new OperatorToken(null, "ARRAY", 2, false, true,
            new int[] {});

    /**
     * ���Z�q�̕�����C�������̐��C���Ӓl�ւ̎Q�ƂƑ�����s�����ǂ����C���Z���ʂ̌^���w�肷��R���X�g���N�^.
     * 
     * @param text ���Z�q�̕�����
     * @param termCount �������̐�
     * @param leftIsAssignmentee ���Ӓl�ւ̑��������ꍇ��true
     * @param leftIsReferencee ���Ӓl�ւ̂�����ꍇ��true
     * @param specifiedResultType ���Z���ʂ̌^�����܂��Ă���ꍇ�͂��̌^���C���܂��Ă��Ȃ��ꍇ��null���w�肷��
     * @throws IllegalArgumentException termCount��0�ȉ��̏ꍇ
     */
    public OperatorToken(final OPERATOR_TYPE operator, final String text, final int termCount,
            final boolean leftIsAssignmentee, final boolean leftIsReferencee,
            final int[] typeSpecifiedTermIndexes) {
        super(text);

        if (termCount <= 0) {
            throw new IllegalArgumentException("Operator must treat one or more terms.");
        }

        this.operator = operator;
        this.leftIsAssignmentee = leftIsAssignmentee;
        this.leftIsReferencee = leftIsReferencee;
        this.termCount = termCount;
        this.typeSpecifiedTermIndexes = typeSpecifiedTermIndexes;
    }

    /**
     * ���̉��Z�q����舵�����̐���Ԃ�.
     * @return ���̉��Z�q����舵�����̐�
     */
    public int getTermCount() {
        return this.termCount;
    }

    /**
     * ���Ӓl�ւ̑�������邩�ǂ�����Ԃ�.
     * @return�@���Ӓl�ւ̑��������ꍇ��true
     */
    @Override
    public boolean isAssignmentOperator() {
        return this.leftIsAssignmentee;
    }

    /**
     * ���Z�q��\���g�[�N�����ǂ�����Ԃ�.
     * 
     * @return�@true
     */
    @Override
    public boolean isOperator() {
        return true;
    }

    /**
     * ���Ӓl���Q�ƂƂ��ė��p����邩�ǂ�����Ԃ�.
     * @return�@���Ӓl���Q�ƂƂ��ė��p�����ꍇ��true
     */
    public boolean isLeftTermIsReferencee() {
        return this.leftIsReferencee;
    }

    /**
     * ���O�����������p����Enum OPERATOR�̗v�f��Ԃ��D
     * ���O�������Ɍ^�������Ϗ����Ȃ���ނ̉��Z�q�̏ꍇ��null��Ԃ��D
     * @return ���O�����������p����Enum OPERATOR�̗v�f�C���O�������Ɍ^�������Ϗ����Ȃ���ނ̉��Z�q�̏ꍇ��null
     */
    public OPERATOR_TYPE getOperator() {
        return this.operator;
    }

    /**
     * ���Z���ʂ̌^�����肳���ۂɍl������鍀�̃C���f�b�N�X�̔z���Ԃ�.
     * ���̌^�Ƃ͊֌W�Ȃ��^�����肳���ꍇ�͋�̔z���Ԃ�.
     * @return ���Z���ʂ̌^�����肳���ۂɍl������鍀�̃C���f�b�N�X�̔z��
     */
    public int[] getTypeSpecifiedTermIndexes() {
        return this.typeSpecifiedTermIndexes;
    }

    /**
     * ���Ӓl�ւ̑�������邩�ǂ�����\��
     */
    private final boolean leftIsAssignmentee;

    /**
     * ���Ӓl���Q�ƂƂ��ė��p����邩�ǂ�����\��
     */
    private final boolean leftIsReferencee;

    /**
     * ���̉��Z�q����舵�����̐�
     */
    private final int termCount;

    /**
     * ���Z�q
     */
    private final OPERATOR_TYPE operator;

    /**
     * ���Z���ʂ̌^�����肳���ۂɍl������鍀�̃C���f�b�N�X�̔z��
     */
    private final int[] typeSpecifiedTermIndexes;
}
