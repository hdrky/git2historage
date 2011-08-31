package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import static jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR_TYPE.*;


/**
 * ���Z�q��\���񋓌^
 * 
 * @author t-miyake
 *
 */
public enum OPERATOR {

    /**
     * �Z�p���Z�q"+"
     */
    PLUS(ARITHMETIC, "+", null, true, false),

    /**
     * �Z�p���Z�q"-"
     */
    MINUS(ARITHMETIC, "-", null, true, false),

    /**
     * �Z�p���Z�q"*"
     */
    STAR(ARITHMETIC, "*", null, true, false),

    /**
     * �Z�p���Z�q"/"
     */
    DIV(ARITHMETIC, "/", null, true, false),

    /**
     * �Z�p���Z�q"%"
     */
    MOD(ARITHMETIC, "%", null, true, false),

    /**
     * ��r���Z�q"=="
     */
    EQUAL(COMPARATIVE, "==", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * ��r���Z�q"!="
     */
    NOT_EQUAL(COMPARATIVE, "!=", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * ��r���Z�q"<"
     */
    LT(COMPARATIVE, "<", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * ��r���Z�q">"
     */
    GT(COMPARATIVE, ">", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * ��r���Z�q"<="
     */
    LE(COMPARATIVE, "<=", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * ��r���Z�q">="
     */
    GE(COMPARATIVE, ">=", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * instanceof���Z�q
     */
    INSTANCEOF(COMPARATIVE, "instanceof", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * �_�����Z�q"&&"
     */
    LAND(LOGICAL, "&&", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * �_�����Z�q"||"
     */
    LOR(LOGICAL, "||", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * �_�����Z�q"!"
     */
    LNOT(LOGICAL, "!", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * �r�b�g���Z�q"&"
     */
    BAND(BITS, "&", null, true, false),

    /**
     * �r�b�g���Z�q"|"
     */
    BOR(BITS, "|", null, true, false),

    /**
     * �r�b�g���Z�q"^"
     */
    BXOR(BITS, "^", null, true, false),

    /**
     * �r�b�g���Z�q"~"
     */
    BNOT(BITS, "~", null, true, false),

    /**
     * �V�t�g���Z�q"<<"
     */
    SL(SHIFT, "<<", null, true, false),

    /**
     * �V�t�g���Z�q">>"
     */
    SR(SHIFT, ">>", null, true, false),

    /**
     * �V�t�g���Z�q">>>"
     */
    BSR(SHIFT, ">>>", null, true, false),

    /**
     * ������Z�q"="
     */
    ASSIGN(ASSIGNMENT, "=", null, false, true),

    /**
     * ������Z�q"+="
     */
    PLUS_ASSIGN(ASSIGNMENT, "+=", null, true, true),

    /**
     * ������Z�q"-="
     */
    MINUS_ASSIGN(ASSIGNMENT, "-=", null, true, true),

    /**
     * ������Z�q"*="
     */
    STAR_ASSIGN(ASSIGNMENT, "*=", null, true, true),

    /**
     * ������Z�q"/="
     */
    DIV_ASSIGN(ASSIGNMENT, "/=", null, true, true),

    /**
     * ������Z�q"%="
     */
    MOD_ASSIGN(ASSIGNMENT, "%=", null, true, true),

    /**
     * ������Z�q"&="
     */
    BAND_ASSIGN(ASSIGNMENT, "&=", null, true, true),

    /**
     * ������Z�q"|="
     */
    BOR_ASSIGN(ASSIGNMENT, "|=", null, true, true),

    /**
     * ������Z�q"^="
     */
    BXOR_ASSIGN(ASSIGNMENT, "^=", null, true, true),

    /**
     * ������Z�q"<<="
     */
    SL_ASSIGN(ASSIGNMENT, "<<=", null, true, true),

    /**
     * ������Z�q">>="
     */
    SR_ASSIGN(ASSIGNMENT, ">>=", null, true, true),

    /**
     * ������Z�q">>>="
     */
    BSR_ASSIGN(ASSIGNMENT, ">>>=", null, true, true),

    /**
     * �Z�p�ꍀ���Z�q"++"
     */
    INC(ARITHMETIC, "++", PrimitiveTypeInfo.INT, true, true),

    /**
     * �Z�p�ꍀ���Z�q"--"
     */
    DEC(ARITHMETIC, "--", PrimitiveTypeInfo.INT, true, true), ;

    /**
     * ���Z�q�̃^�C�v�ƃg�[�N����^���ď�����
     * 
     * @param operatorType ���Z�q�̃^�C�v
     * @param token ���Z�q�̃g�[�N��
     */
    private OPERATOR(final OPERATOR_TYPE operatorType, final String token,
            final PrimitiveTypeInfo specifiedResultType, final boolean firstIsReferencee,
            final boolean firstIsAssignmentee) {
        this.operatorType = operatorType;
        this.token = token;
        this.specifiedResultType = specifiedResultType;
        
        this.firstIsReferencee = firstIsReferencee;
        this.firstIsAssignmentee = firstIsAssignmentee;
    }

    /**
     * ���Z�q�̃^�C�v��Ԃ�
     * 
     * @return ���Z�q�̃^�C�v
     */
    public OPERATOR_TYPE getOperatorType() {
        return this.operatorType;
    }

    /**
     * ���Z�q�̃g�[�N����Ԃ�
     * 
     * @return ���Z�q�̃g�[�N��
     */
    public String getToken() {
        return this.token;
    }

    public static OPERATOR getOperator(final String token) {
        for (final OPERATOR operator : OPERATOR.values()) {
            if (operator.getToken().equals(token)) {
                return operator;
            }
        }
        return null;
    }
    
    public boolean isFirstIsAssignmentee() {
        return this.firstIsAssignmentee;
    }
    
    public boolean isFirstIsReferencee() {
        return this.firstIsReferencee;
    }

    /**
     * ���Z���ʂ̌^�����܂��Ă���ꍇ�͂��̌^��Ԃ�.
     * ���܂��Ă��Ȃ��ꍇ��null��Ԃ�.
     * @return ���Z���ʂ̌^�����܂��Ă���ꍇ�͂��̌^�C���܂��Ă��Ȃ��ꍇ��null
     */
    public PrimitiveTypeInfo getSpecifiedResultType() {
        return this.specifiedResultType;
    }

    final private PrimitiveTypeInfo specifiedResultType;

    /**
     * ���Z�q�̃^�C�v��\���ϐ�
     */
    final private OPERATOR_TYPE operatorType;

    /**
     * ���Z�q�̃g�[�N����\���ϐ�
     */
    final private String token;

    /**
     * �ꍀ�ւ̑�������邩�ǂ�����\��
     */
    private final boolean firstIsAssignmentee;

    /**
     * �ꍀ���Q�ƂƂ��ė��p����邩�ǂ�����\��
     */
    private final boolean firstIsReferencee;
}
